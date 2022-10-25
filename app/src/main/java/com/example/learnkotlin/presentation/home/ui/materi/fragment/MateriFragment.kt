package com.example.learnkotlin.presentation.home.ui.materi.fragment

import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.ViewCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.Observer
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.learnkotlin.R
import com.example.learnkotlin.data.remote.dto.DeleteResponse
import com.example.learnkotlin.data.remote.dto.GetAllInputMateriResponse
import com.example.learnkotlin.data.remote.dto.GetInputMateriByIdResponse
import com.example.learnkotlin.databinding.FragmentMateriBinding
import com.example.learnkotlin.presentation.detail.activity.materi.DetailMateriActivity
import com.example.learnkotlin.presentation.home.ui.materi.adapter.allMateri.AllMateriAdapter
import com.example.learnkotlin.presentation.home.ui.materi.adapter.inputMateriById.GetInputMateriByIdAdapter
import com.example.learnkotlin.presentation.home.ui.materi.viewmodel.MateriViewModel
import com.example.learnkotlin.util.AUTH_STATUS.ADMIN
import com.example.learnkotlin.util.AUTH_STATUS.USER
import com.example.learnkotlin.util.MESSAGE
import com.example.learnkotlin.util.MESSAGE.STATUS_ERROR
import com.example.learnkotlin.util.MESSAGE.STATUS_SUCCESS
import com.example.learnkotlin.util.MarginItemDecorationVertical
import com.example.learnkotlin.util.P_E_M
import com.example.learnkotlin.util.Result
import com.example.learnkotlin.util.SESSION.ID
import com.example.learnkotlin.util.SessionManager
import com.example.learnkotlin.util.removeView
import com.example.learnkotlin.util.showView
import com.example.learnkotlin.util.simpleName
import com.example.learnkotlin.util.snackbar
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@AndroidEntryPoint
class MateriFragment : Fragment(R.layout.fragment_materi) {

    private var _binding: FragmentMateriBinding? = null

    private val binding get() = _binding as FragmentMateriBinding

    private lateinit var allMateriAdapter: AllMateriAdapter

    private lateinit var getInputMateribyIdAdapter: GetInputMateriByIdAdapter

    private val viewModel: MateriViewModel by viewModels()

    private lateinit var sessionManager: SessionManager

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentMateriBinding.inflate(layoutInflater)
        sessionManager = SessionManager(requireContext())
        allMateriAdapter = AllMateriAdapter.instance()
        getInputMateribyIdAdapter = GetInputMateriByIdAdapter.instance()
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initLaunch()
        initAdapter()
        initView()
    }

    private fun initAdapter() {
        sessionManager.status?.let { status ->
            if (status == USER) {
                allMateriAdapter.let { adapter ->
                    binding.rvMateri.apply {
                        this.adapter = adapter
                        this.layoutManager = LinearLayoutManager(requireContext())
                        this.addItemDecoration(MarginItemDecorationVertical(16))
                        ViewCompat.setNestedScrollingEnabled(this, true)
                    }

                    adapter.setOnItemClickListener { id ->
                        startActivity(
                            Intent(
                                requireContext(),
                                DetailMateriActivity::class.java
                            ).putExtra(ID, id)
                        )
                    }
                }
                return@let
            }

            if (status == ADMIN) {
                getInputMateribyIdAdapter.let { adapter ->
                    binding.rvMateri.apply {
                        this.adapter = adapter
                        this.layoutManager = LinearLayoutManager(requireContext())
                        this.addItemDecoration(MarginItemDecorationVertical(16))
                        ViewCompat.setNestedScrollingEnabled(this, true)
                    }

                    adapter.setOnDeleteItemClickListener { id ->
                        viewModel.fetchDeleteMateri(id)
                    }

                    adapter.setOnItemClickListener { id ->
                        startActivity(
                            Intent(
                                requireContext(),
                                DetailMateriActivity::class.java
                            ).putExtra(ID, id)
                        )
                    }
                }

                return@let
            }
        }
    }

    private fun initLaunch() {
        observerGetAllMateri?.let {
            viewModel.getAllMateri().observe(viewLifecycleOwner, it)
        }

        observerGetInputMateriById?.let {
            viewModel.getInputMateriById().observe(viewLifecycleOwner, it)
        }

        observerDeleteMateri?.let {
            viewModel.geDeleteMateri().observe(viewLifecycleOwner, it)
        }
    }

    private var observerGetAllMateri: Observer<Result<GetAllInputMateriResponse>>? =
        Observer { result ->
            lifecycleScope.launchWhenStarted {
                lifecycle.repeatOnLifecycle(Lifecycle.State.STARTED) {
                    launch {
                        when (result) {
                            is Result.Loading -> {
                                binding.pbLoading.showView()
                            }
                            is Result.Success -> {
                                binding.pbLoading.removeView()
                                result.data?.dataItem?.let { item ->
                                    if (item.isEmpty()) {
                                        binding.emptyLayout.showView()
                                        return@launch
                                    }
                                    allMateriAdapter.differ.submitList(item)
                                }
                            }
                            is Result.Error -> {
                                binding.pbLoading.removeView()
                                result.message?.let { msg ->
                                    snackbar(binding.root, msg, STATUS_ERROR)
                                } ?: result.data?.message?.let { msg ->
                                    snackbar(binding.root, msg, STATUS_ERROR)
                                }
                            }
                        }
                    }
                }
            }
        }

    private var observerGetInputMateriById: Observer<Result<GetInputMateriByIdResponse>>? =
        Observer { result ->
            lifecycleScope.launchWhenStarted {
                lifecycle.repeatOnLifecycle(Lifecycle.State.STARTED) {
                    launch {
                        when (result) {
                            is Result.Loading -> {
                                binding.pbLoading.showView()
                            }
                            is Result.Success -> {
                                binding.pbLoading.removeView()
                                result.data?.dataItem?.let { item ->
                                    if (item.isEmpty()) {
                                        binding.emptyLayout.showView()
                                        return@launch
                                    }
                                    getInputMateribyIdAdapter.differ.submitList(item)
                                }
                            }
                            is Result.Error -> {
                                binding.pbLoading.removeView()
                                result.message?.let { msg ->
                                    snackbar(binding.root, msg, STATUS_ERROR)
                                } ?: result.data?.message?.let { msg ->
                                    snackbar(binding.root, msg, STATUS_ERROR)
                                }
                            }
                        }
                    }
                }
            }
        }

    private var observerDeleteMateri: Observer<Result<DeleteResponse>>? = Observer { result ->
        lifecycleScope.launchWhenStarted {
            lifecycle.repeatOnLifecycle(Lifecycle.State.STARTED) {
                launch {
                    when (result) {
                        is Result.Loading -> {
                            binding.pbLoading.showView()
                        }
                        is Result.Success -> {
                            binding.pbLoading.removeView()
                            result.message?.let { msg ->
                                snackbar(binding.root, msg, STATUS_SUCCESS)
                            } ?: result.data?.message?.let { msg ->
                                snackbar(binding.root, msg, STATUS_SUCCESS)
                            }
                            delay(1000)
                                parentFragmentManager.beginTransaction().attach(this@MateriFragment).commitNow()
                        }
                        is Result.Error -> {
                            binding.pbLoading.removeView()
                            result.message?.let { msg ->
                                snackbar(binding.root, msg, STATUS_ERROR)
                            } ?: result.data?.message?.let { msg ->
                                snackbar(binding.root, msg, STATUS_ERROR)
                            }
                        }
                    }
                }
            }
        }
    }

    private fun initView() {
        sessionManager.status?.let { status ->
            if (status == USER) {
                viewModel.fetchAllMateri()
                return@let
            }

            if (status == ADMIN) {
                viewModel.fetchInpuMateribyId()
                return@let
            }
        }
    }

    override fun onDetach() {
        super.onDetach()
        parentFragmentManager.beginTransaction().detach(this@MateriFragment).commitNow()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
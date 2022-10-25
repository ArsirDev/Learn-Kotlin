package com.example.learnkotlin.presentation.home.ui.kuis.fragment

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.ViewCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.Observer
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.learnkotlin.data.remote.dto.GetAllInputKuisResponse
import com.example.learnkotlin.data.remote.dto.GetInputKuisByIdResponse
import com.example.learnkotlin.databinding.FragmentKuisBinding
import com.example.learnkotlin.presentation.detail.activity.kuis.DetailKuisActivity
import com.example.learnkotlin.presentation.detail.activity.materi.DetailMateriActivity
import com.example.learnkotlin.presentation.home.ui.kuis.adapter.allKuis.AllKuisAdapter
import com.example.learnkotlin.presentation.home.ui.kuis.adapter.inputKuisById.GetInputKuisByIdAdapter
import com.example.learnkotlin.presentation.home.ui.kuis.viewmodel.KuisViewModel
import com.example.learnkotlin.util.AUTH_STATUS
import com.example.learnkotlin.util.MESSAGE
import com.example.learnkotlin.util.MarginItemDecorationVertical
import com.example.learnkotlin.util.Result
import com.example.learnkotlin.util.SESSION
import com.example.learnkotlin.util.SessionManager
import com.example.learnkotlin.util.removeView
import com.example.learnkotlin.util.showView
import com.example.learnkotlin.util.snackbar
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class KuisFragment : Fragment() {

    private var _binding: FragmentKuisBinding? = null

    private val binding get() = _binding as FragmentKuisBinding

    private val viewModel: KuisViewModel by viewModels()

    private lateinit var allKuisAdapter: AllKuisAdapter

    private lateinit var getInputKuisByIdAdapter: GetInputKuisByIdAdapter

    private lateinit var sessionManager: SessionManager

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentKuisBinding.inflate(layoutInflater)
        sessionManager = SessionManager(requireContext())
        allKuisAdapter = AllKuisAdapter.instance()
        getInputKuisByIdAdapter = GetInputKuisByIdAdapter.instance()
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
            if (status == AUTH_STATUS.USER) {
                allKuisAdapter.let { adapter ->
                    binding.rvMateri.apply {
                        this.adapter = adapter
                        this.layoutManager = LinearLayoutManager(requireContext())
                        this.addItemDecoration(MarginItemDecorationVertical(16))
                        ViewCompat.setNestedScrollingEnabled(this, true)
                    }

                    adapter.setOnItemClickListener { id ->
                        startActivity(
                            Intent(requireContext(), DetailKuisActivity::class.java).putExtra(
                                SESSION.ID, id))
                    }
                }
                return@let
            }

            if (status == AUTH_STATUS.ADMIN) {
                getInputKuisByIdAdapter.let { adapter ->
                    binding.rvMateri.apply {
                        this.adapter = adapter
                        this.layoutManager = LinearLayoutManager(requireContext())
                        this.addItemDecoration(MarginItemDecorationVertical(16))
                        ViewCompat.setNestedScrollingEnabled(this, true)
                    }

                    adapter.setOnItemClickListener { id ->
                        startActivity(
                            Intent(requireContext(), DetailKuisActivity::class.java).putExtra(
                                SESSION.ID, id))
                    }
                }

                return@let
            }
        }
    }

    private fun initLaunch() {
        observerGetAllKuis?.let {
            viewModel.getAllKuis().observe(viewLifecycleOwner, it)
        }

        observerGetInputKuisById?.let {
            viewModel.getInputKuisById().observe(viewLifecycleOwner, it)
        }
    }

    private var observerGetAllKuis: Observer<Result<GetAllInputKuisResponse>>? =
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
                                result.data?.data?.let { item ->

                                    if (item.isEmpty()) {
                                        binding.emptyLayout.showView()
                                        return@launch
                                    }
                                    allKuisAdapter.differ.submitList(item)
                                }
                            }
                            is Result.Error -> {
                                binding.pbLoading.removeView()
                                result.message?.let { msg ->
                                    snackbar(binding.root, msg, MESSAGE.STATUS_ERROR)
                                } ?: result.data?.message?.let { msg ->
                                    snackbar(binding.root, msg, MESSAGE.STATUS_ERROR)
                                }
                            }
                        }
                    }
                }
            }
        }

    private var observerGetInputKuisById: Observer<Result<GetInputKuisByIdResponse>>? =
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
                                result.data?.data?.let { item ->
                                    if (item.isEmpty()) {
                                        binding.emptyLayout.showView()
                                        return@launch
                                    }
                                    getInputKuisByIdAdapter.differ.submitList(item)
                                }
                            }
                            is Result.Error -> {
                                binding.pbLoading.removeView()
                                result.message?.let { msg ->
                                    snackbar(binding.root, msg, MESSAGE.STATUS_ERROR)
                                } ?: result.data?.message?.let { msg ->
                                    snackbar(binding.root, msg, MESSAGE.STATUS_ERROR)
                                }
                            }
                        }
                    }
                }
            }
        }

    private fun initView() {
        sessionManager.status?.let { status ->
            if (status == AUTH_STATUS.USER) {
                viewModel.fetchAllKuis()
                return@let
            }

            if (status == AUTH_STATUS.ADMIN) {
                viewModel.fetchInpuKuisbyId()
                return@let
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}
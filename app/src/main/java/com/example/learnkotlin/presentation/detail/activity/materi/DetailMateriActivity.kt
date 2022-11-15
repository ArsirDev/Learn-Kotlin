package com.example.learnkotlin.presentation.detail.activity.materi

import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.Observer
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.example.learnkotlin.R
import com.example.learnkotlin.data.remote.dto.DataDetailMateriItem
import com.example.learnkotlin.data.remote.dto.GetDetailMateriResponse
import com.example.learnkotlin.databinding.ActivityDetailMateriBinding
import com.example.learnkotlin.presentation.detail.viewmodel.MateriDetailViewModel
import com.example.learnkotlin.util.*
import com.example.learnkotlin.util.SESSION.ID
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class DetailMateriActivity : AppCompatActivity() {

    private var _binding: ActivityDetailMateriBinding? = null

    private val binding get() = _binding as ActivityDetailMateriBinding

    private val viewModelMateri: MateriDetailViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initInstance()
        setContentView(binding.root)
        setSupportActionBar(findViewById(R.id.toolbar))
        initIntent()
        initLaunch()
    }

    private fun initIntent() {
        intent.extras?.getInt(ID)?.let { id ->
            if (id != 0) {
                viewModelMateri.fetchDetailMateri(id)
                return@let
            }
        }
    }
    private fun initLaunch() {
        observerDetailMateri?.let {
            viewModelMateri.getDetailMateri().observe(this, it)
        }
    }

    private var observerDetailMateri: Observer<Result<GetDetailMateriResponse>>? = Observer { result ->
        lifecycleScope.launchWhenStarted {
            lifecycle.repeatOnLifecycle(Lifecycle.State.STARTED) {
                launch {
                    when(result) {
                        is Result.Loading -> {
                            binding.contentLayout.pbLoading.showView()
                        }
                        is Result.Success -> {
                            binding.contentLayout.pbLoading.removeView()
                            result.data?.let { dataResponse ->
                                initView(dataResponse.dataDetailMateriItem)
                            }
                        }
                        is Result.Error -> {
                            binding.contentLayout.pbLoading.removeView()
                        }
                    }
                }
            }
        }
    }

    private fun initView(dataItem: DataDetailMateriItem) {
        with(binding) {
            binding.toolbarLayout.title = dataItem.title
            ivImage.loadImage(dataItem.image)
            with(contentLayout) {
                tvFirstDescription.text = dataItem.description
                tvSecondDescription.text = dataItem.anotherDescription
            }
        }
    }

    private fun initInstance() {
        _binding = ActivityDetailMateriBinding.inflate(layoutInflater)
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}
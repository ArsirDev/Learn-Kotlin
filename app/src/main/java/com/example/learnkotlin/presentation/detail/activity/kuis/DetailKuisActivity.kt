package com.example.learnkotlin.presentation.detail.activity.kuis

import android.os.Bundle
import android.widget.RadioButton
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.Observer
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.example.learnkotlin.R
import com.example.learnkotlin.data.remote.dto.DataDetailKuisItem
import com.example.learnkotlin.data.remote.dto.GetDetailKuisResponse
import com.example.learnkotlin.databinding.ActivityDetailKuisBinding
import com.example.learnkotlin.presentation.detail.viewmodel.KuisDetailViewModel
import com.example.learnkotlin.util.MESSAGE.STATUS_ERROR
import com.example.learnkotlin.util.Result
import com.example.learnkotlin.util.SESSION.ID
import com.example.learnkotlin.util.customFailureDialog
import com.example.learnkotlin.util.customSuccessDialog
import com.example.learnkotlin.util.loadImage
import com.example.learnkotlin.util.removeView
import com.example.learnkotlin.util.setOnClickListenerWithDebounce
import com.example.learnkotlin.util.showView
import com.example.learnkotlin.util.snackbar
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@AndroidEntryPoint
class DetailKuisActivity : AppCompatActivity() {

    private var _binding: ActivityDetailKuisBinding? = null

    private val binding get() = _binding as ActivityDetailKuisBinding

    private val viewModel: KuisDetailViewModel by viewModels()

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
            viewModel.fetchDetailKuis(id)
        }
    }

    private fun initLaunch() {
        observerDetailKuis?.let {
            viewModel.getDetailKuis().observe(this, it)
        }
    }

    private var observerDetailKuis: Observer<Result<GetDetailKuisResponse>>? = Observer { result ->
        lifecycleScope.launchWhenStarted {
            lifecycle.repeatOnLifecycle(Lifecycle.State.STARTED) {
                launch {
                    when(result) {
                        is Result.Loading -> {
                            binding.contentLayout.pbLoading?.showView()

                        }
                        is Result.Success -> {
                            binding.contentLayout.pbLoading?.removeView()
                            result.data?.dataDetailKuisItem?.let { dataItem->
                                initView(dataItem)
                            }
                        }
                        is Result.Error -> {
                            binding.contentLayout.pbLoading?.removeView()
                        }
                    }
                }
            }
        }
    }

    private fun initView(dataItem: DataDetailKuisItem) {
        with(binding) {
            toolbarLayout.title = dataItem.title ?: title
            ivImage.loadImage(dataItem.image)
            with(contentLayout) {
                tvQuiz?.text = dataItem.question
                rbA?.text = dataItem.answerA
                rbB?.text = dataItem.answerB
                rbC?.text = dataItem.answerC
                rbD?.text = dataItem.answerD

                btnAnswer?.setOnClickListenerWithDebounce {
                    val rgStatus = rgAnswer?.checkedRadioButtonId
                    onValidation(rgStatus as Int, dataItem)
                }
            }
        }
    }

    private fun onValidation(rgStatus: Int, dataItem: DataDetailKuisItem) {
        if (rgStatus <= 0) {
            snackbar(binding.root, "Harap pilih jawaban anda", STATUS_ERROR)
            return
        }
        val rb: RadioButton = this.findViewById(rgStatus)
        if (rb.text != dataItem.correctAnswer) {
            customFailureDialog(this@DetailKuisActivity)
            return
        }
        lifecycleScope.launch {
            customSuccessDialog(this@DetailKuisActivity)
            delay(1000L)
        }
    }

    private fun initInstance() {
        _binding = ActivityDetailKuisBinding.inflate(layoutInflater)
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}
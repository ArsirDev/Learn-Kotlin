package com.example.learnkotlin.presentation.input.ui.kuis

import android.Manifest
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.text.format.Formatter
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.Observer
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.example.learnkotlin.data.remote.dto.SetInputKuisResponse
import com.example.learnkotlin.databinding.FragmentInputKuisBinding
import com.example.learnkotlin.presentation.home.HomeActivity
import com.example.learnkotlin.util.MESSAGE
import com.example.learnkotlin.util.MESSAGE.STATUS_ERROR
import com.example.learnkotlin.util.Result
import com.example.learnkotlin.util.getFileFromContentUri
import com.example.learnkotlin.util.loadImage
import com.example.learnkotlin.util.removeView
import com.example.learnkotlin.util.setOnClickListenerWithDebounce
import com.example.learnkotlin.util.showView
import com.example.learnkotlin.util.snackbar
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.io.File

@AndroidEntryPoint
class InputKuisFragment : Fragment() {

    private var _binding: FragmentInputKuisBinding? = null

    private val binding get() = _binding as FragmentInputKuisBinding

    private var permissionRequest: ActivityResultLauncher<Array<String>>? = null

    private lateinit var imageLauncher: ActivityResultLauncher<String?>

    private var newImage: File? = null

    private val viewModel: InputKuisViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentInputKuisBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initCheckPermission()
        initImageActivityResultLauncher()
        initLaunch()
        initView()
    }

    private fun initLaunch() {
        observerInputKuis?.let {
            viewModel.getInputKuis().observe(viewLifecycleOwner, it)
        }
    }

    private var observerInputKuis: Observer<Result<SetInputKuisResponse>>? = Observer { result ->
        lifecycleScope.launchWhenStarted {
            lifecycle.repeatOnLifecycle(Lifecycle.State.STARTED) {
                launch {
                    when(result) {
                        is Result.Loading -> {
                            binding.pbLoading.showView()
                        }
                        is Result.Success -> {
                            binding.pbLoading.removeView()
                            result.message?.let { msg ->
                                snackbar(binding.root, msg , MESSAGE.STATUS_SUCCESS)
                            } ?: result.data?.message?.let { msg ->
                                snackbar(binding.root, msg , MESSAGE.STATUS_SUCCESS)
                            }
                            delay(1000)
                            toHome()
                        }
                        is Result.Error -> {
                            binding.pbLoading.removeView()
                            result.message?.let { msg ->
                                snackbar(binding.root, msg , MESSAGE.STATUS_ERROR)
                            } ?: result.data?.message?.let { msg ->
                                snackbar(binding.root, msg , MESSAGE.STATUS_ERROR)
                            }
                        }
                    }
                }
            }
        }
    }

    private fun initView() {
        with(binding) {
            cardView.setOnClickListenerWithDebounce {
                try {
                    imageLauncher.launch("image/*")
                } catch (e: Exception) {
                    initCheckPermission()
                }
            }
            btnSave.setOnClickListenerWithDebounce {
                val title = etTitle.text.toString().trim()
                val question = etQuestion.text.toString().trim()
                val answer_a = etJawabanA.text.toString().trim()
                val answer_b = etJawabanB.text.toString().trim()
                val answer_c = etJawabanC.text.toString().trim()
                val answer_d = etJawabanD.text.toString().trim()
                val correct_answer = etJawabanBenar.text.toString().trim()

                onValidation(
                    title,
                    question,
                    answer_a,
                    answer_b,
                    answer_c,
                    answer_d,
                    correct_answer,
                    newImage
                )
            }
        }
    }

    private fun onValidation(
        title: String,
        question: String,
        answerA: String,
        answerB: String,
        answerC: String,
        answerD: String,
        correctAnswer: String,
        image: File?
    ) {
        if (title.isEmpty() || question.isEmpty() || answerA.isEmpty() || answerB.isEmpty() || answerC.isEmpty() || answerD.isEmpty() || correctAnswer.isEmpty() || newImage == null) {
            snackbar(binding.root, "Field tidak boleh kosong", STATUS_ERROR)
            return
        }

        viewModel.setInputKuis(
            title,
            question,
            answerA,
            answerB,
            answerC,
            answerD,
            correctAnswer,
            image as File
        )
    }

    private fun initImageActivityResultLauncher() {
        permissionRequest =
            registerForActivityResult(ActivityResultContracts.RequestMultiplePermissions()) {
                it?.entries?.forEach { permission ->
                }
            }

        imageLauncher = registerForActivityResult(ActivityResultContracts.GetContent()) { result ->
            result?.let { uri ->
                val file = requireActivity().getFileFromContentUri(uri)
                newImage = file
                val fileFormatted = Formatter.formatShortFileSize(requireContext(), file.length())
                val fileSizeNum = fileFormatted.replace(" ", "").dropLast(2).toDouble()
                val fileSizeUnit = fileFormatted.replace(" ", "").takeLast(2)
                if (fileSizeNum > 2.0 && fileSizeUnit.contains("mb", true)) {
                    snackbar(binding.root, "Gambar Melebihi 2Mb", MESSAGE.STATUS_ERROR)
                    newImage = null
                } else {
                    binding.ivImageMateri.loadImage(uri.toString(), DiskCacheStrategy.RESOURCE)
                }
            }
        }
    }

    private fun initCheckPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            val permissions =
                arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.INTERNET)
            permissionRequest?.launch(permissions)
        }
    }


    private fun toHome() {
        startActivity(Intent(requireContext(), HomeActivity::class.java))
        requireActivity().finishAffinity()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
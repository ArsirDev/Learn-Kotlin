package com.example.learnkotlin.presentation.input.ui.materi

import android.Manifest
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.text.format.Formatter
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.Observer
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.example.learnkotlin.data.remote.dto.DataInputMateriByIdItem
import com.example.learnkotlin.data.remote.dto.GeneralResponse
import com.example.learnkotlin.data.remote.dto.SetInputMateriResponse
import com.example.learnkotlin.databinding.FragmentInputMateriBinding
import com.example.learnkotlin.presentation.home.HomeActivity
import com.example.learnkotlin.util.*
import com.example.learnkotlin.util.MESSAGE.STATUS_ERROR
import com.example.learnkotlin.util.MESSAGE.STATUS_SUCCESS
import com.example.learnkotlin.util.SESSION.EDITMATERI
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.io.File

@AndroidEntryPoint
class InputMateriFragment : Fragment() {

    private var _binding: FragmentInputMateriBinding? = null

    private val binding get() = _binding as FragmentInputMateriBinding

    private var permissionRequest: ActivityResultLauncher<Array<String>>? = null

    private lateinit var imageLauncher: ActivityResultLauncher<String?>

    private var newImage: File? = null

    private val viewmodel: InputMateriViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentInputMateriBinding.inflate(layoutInflater)
        onBack()
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initCheckPermission()
        initImageActivityResultLauncher()
        initArgument()
        initAction()
        initLaunch()
    }

    private fun initAction() {
        with(binding) {
            cardView.setOnClickListenerWithDebounce {
                try {
                    imageLauncher.launch("image/*")
                } catch (e: Exception) {
                    initCheckPermission()
                }
            }
        }
    }

    private fun onBack() {
        val onBackPressedCallback = object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                startActivity(Intent(requireContext(), HomeActivity::class.java))
                requireActivity().finishAffinity()
            }
        }
        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner, onBackPressedCallback)
    }

    private fun initArgument() {
        arguments?.getString(EDITMATERI)?.fromJson<DataInputMateriByIdItem>().let { dataUpdate ->
            initUpdate(dataUpdate)
        }
    }

    private fun initUpdate(dataUpdate: DataInputMateriByIdItem?) {
        with(binding) {
            if (dataUpdate != null) {
                ivImageMateri.loadImage(dataUpdate.image)
                etTitle.setText(dataUpdate.title)
                etDescription.setText(dataUpdate.description)
                etSecondDescription.setText(dataUpdate.anotherDescription)

                btnSave.setOnClickListenerWithDebounce {
                    val title = etTitle.text.toString().trim()
                    val description = etDescription.text.toString().trim()
                    val another_description = etSecondDescription.text.toString().trim()

                    if (newImage == null) {
                        snackbar(binding.root, "Gambar silahkan ambil ulang", STATUS_ERROR)
                        return@setOnClickListenerWithDebounce
                    }

                    viewmodel.setUpdateMateri(
                        dataUpdate.id,
                        title,
                        description,
                        another_description,
                        newImage!!
                    )
                }
            } else {
                btnSave.setOnClickListenerWithDebounce {
                    val title = etTitle.text.toString().trim()
                    val description = etDescription.text.toString().trim()
                    val another_description = etSecondDescription.text.toString().trim()
                    viewmodel.setInputMateri(
                        title,
                        description,
                        another_description,
                        newImage!!
                    )
                }
            }
        }
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
                    snackbar(binding.root, "Gambar Melebihi 2Mb", STATUS_ERROR)
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

    private fun initLaunch() {
        observerInputMateri.let {
            viewmodel.getInputMateri().observe(viewLifecycleOwner, it)
        }
        observerUpdateMateri.let {
            viewmodel.getUpdate().observe(viewLifecycleOwner, it)
        }
    }

    private var observerUpdateMateri: Observer<Result<GeneralResponse>> = Observer { result ->
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
                                snackbar(binding.root, msg , STATUS_SUCCESS)
                            } ?: result.data?.message?.let { msg ->
                                snackbar(binding.root, msg , STATUS_SUCCESS)
                            }
                            delay(1000)
                            toHome()
                        }
                        is Result.Error -> {
                            binding.pbLoading.removeView()
                            result.message?.let { msg ->
                                snackbar(binding.root, msg , STATUS_ERROR)
                            } ?: result.data?.message?.let { msg ->
                                snackbar(binding.root, msg , STATUS_ERROR)
                            }
                        }
                    }
                }
            }
        }
    }

    private var observerInputMateri: Observer<Result<SetInputMateriResponse>> = Observer { result ->
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
                                snackbar(binding.root, msg , STATUS_SUCCESS)
                            } ?: result.data?.message?.let { msg ->
                                snackbar(binding.root, msg , STATUS_SUCCESS)
                            }
                            delay(1000)
                            toHome()
                        }
                        is Result.Error -> {
                            binding.pbLoading.removeView()
                            result.message?.let { msg ->
                                snackbar(binding.root, msg , STATUS_ERROR)
                            } ?: result.data?.message?.let { msg ->
                                snackbar(binding.root, msg , STATUS_ERROR)
                            }
                        }
                    }
                }
            }
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
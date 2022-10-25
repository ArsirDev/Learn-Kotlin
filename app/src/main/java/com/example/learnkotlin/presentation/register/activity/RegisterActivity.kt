package com.example.learnkotlin.presentation.register.activity

import android.Manifest
import android.content.Intent
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.format.Formatter
import android.util.Patterns
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.Observer
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.example.learnkotlin.R
import com.example.learnkotlin.data.remote.dto.RegisterResponse
import com.example.learnkotlin.databinding.ActivityRegisterBinding
import com.example.learnkotlin.presentation.login.activity.LoginActivity
import com.example.learnkotlin.presentation.register.viewmodel.RegisterViewModel
import com.example.learnkotlin.util.MESSAGE.STATUS_ERROR
import com.example.learnkotlin.util.MESSAGE.STATUS_SUCCESS
import com.example.learnkotlin.util.Result
import com.example.learnkotlin.util.convertHtml
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
class RegisterActivity : AppCompatActivity() {

    private var _binding: ActivityRegisterBinding? = null

    private val binding get() = _binding as ActivityRegisterBinding

    private var permissionRequest: ActivityResultLauncher<Array<String>>? = null

    private lateinit var imageLauncher: ActivityResultLauncher<String?>

    private var newImage: File? = null

    private val viewModel: RegisterViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initInstance()
        setContentView(binding.root)
        initCheckPermission()
        initImageActivityResultLauncher()
        initLaunch()
        initView()
    }

    private fun initView() {

        binding.cbAgree.convertHtml(R.string.register_agree)

        binding.ivProfile.setOnClickListenerWithDebounce {
            try {
                imageLauncher.launch("image/*")
            } catch (e: Exception) {
                initCheckPermission()
            }
        }

        binding.cbAgree.setOnCheckedChangeListener { _, isChecked ->
            binding.btnCreateAccount.isEnabled = isChecked
        }

        binding.btnCreateAccount.setOnClickListenerWithDebounce {
            val name = binding.etName.text.toString()
            val email = binding.etEmail.text.toString()
            val phone = binding.etPhone.text.toString()
            val password = binding.etPassword.text.toString()
            val confirmation_password = binding.etConfirmPassword.text.toString()

            onValidation(
                name,
                email,
                newImage,
                phone,
                password,
                confirmation_password
            )
        }
    }

    private fun onValidation(
        name: String,
        email: String,
        newImage: File?,
        phone: String,
        password: String,
        confirmation_password: String
    ) {
        if (
            name.isEmpty() ||
            email.isEmpty() ||
            newImage == null ||
            phone.isEmpty() ||
            password.isEmpty() ||
            confirmation_password.isEmpty()
        ) {
            snackbar(binding.root, "Field tidak boleh kosong", STATUS_ERROR)
            return
        }

        if (password != confirmation_password){
            snackbar(binding.root, "Password tidak sama", STATUS_ERROR)
            return
        }

        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            snackbar(binding.root, "Email tidak valid", STATUS_ERROR)
            return
        }

        viewModel.setRegister(
            name,
            email,
            "User",
            newImage,
            phone,
            password,
            confirmation_password
        )
    }

    private fun initLaunch() {
        observerRegisterResponse?.let {
            viewModel.getRegister().observe(this, it)
        }
    }

    private var observerRegisterResponse: Observer<Result<RegisterResponse>>? = Observer { result ->
        lifecycleScope.launchWhenStarted {
            lifecycle.repeatOnLifecycle(Lifecycle.State.STARTED) {
                launch {
                    when(result) {
                        is Result.Loading -> {
                            binding.pbLoading.showView()
                        }
                        is Result.Success -> {
                            binding.pbLoading.showView()
                            result.message?.let { msg ->
                                snackbar(binding.root, msg, STATUS_SUCCESS)
                            } ?: result.data?.message?.let { msg ->
                                snackbar(binding.root, msg, STATUS_SUCCESS)
                            }
                            delay(800)
                            toLogin()
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

    private fun toLogin() {
        startActivity(Intent(this, LoginActivity::class.java))
    }

    private fun initImageActivityResultLauncher() {
        permissionRequest =
            registerForActivityResult(ActivityResultContracts.RequestMultiplePermissions()) {
                it?.entries?.forEach { permission ->
                }
            }

        imageLauncher = registerForActivityResult(ActivityResultContracts.GetContent()) { result ->
            result?.let { uri ->
                val file = getFileFromContentUri(uri)
                newImage = file
                val fileFormatted = Formatter.formatShortFileSize(this, file.length())
                val fileSizeNum = fileFormatted.replace(" ", "").dropLast(2).toDouble()
                val fileSizeUnit = fileFormatted.replace(" ", "").takeLast(2)
                if (fileSizeNum > 2.0 && fileSizeUnit.contains("mb", true)) {
                    snackbar(binding.root, "Gambar Melebihi 2Mb", STATUS_ERROR)
                    newImage = null
                } else {
                    binding.ivProfile.loadImage(uri.toString(), DiskCacheStrategy.RESOURCE)
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

    private fun initInstance() {
        _binding = ActivityRegisterBinding.inflate(layoutInflater)
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
        newImage = null
    }
}
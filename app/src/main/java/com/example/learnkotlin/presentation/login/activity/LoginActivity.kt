package com.example.learnkotlin.presentation.login.activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.Observer
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.example.learnkotlin.data.remote.dto.LoginResponse
import com.example.learnkotlin.databinding.ActivityLoginBinding
import com.example.learnkotlin.presentation.home.HomeActivity
import com.example.learnkotlin.presentation.login.viewmodel.LoginViewModel
import com.example.learnkotlin.presentation.register.activity.RegisterActivity
import com.example.learnkotlin.util.MESSAGE
import com.example.learnkotlin.util.Result
import com.example.learnkotlin.util.SessionManager
import com.example.learnkotlin.util.removeView
import com.example.learnkotlin.util.setOnClickListenerWithDebounce
import com.example.learnkotlin.util.showView
import com.example.learnkotlin.util.snackbar
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@AndroidEntryPoint
class LoginActivity : AppCompatActivity() {

    private var _binding: ActivityLoginBinding? = null

    private val binding get() = _binding as ActivityLoginBinding

    private val viewModel: LoginViewModel by viewModels()

    private lateinit var sessionManager: SessionManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initInstance()
        setContentView(binding.root)
        initLaunch()
        initView()
    }

    private fun initLaunch() {
        observerLoginResponse.let {
            viewModel.getLogin().observe(this, it)
        }

        lifecycleScope.launchWhenStarted {
            lifecycle.repeatOnLifecycle(Lifecycle.State.STARTED) {
                launch {
                    viewModel.uiEvent.collectLatest { msg ->
                        snackbar(binding.root, msg, MESSAGE.STATUS_ERROR)
                    }
                }
            }
        }
    }

    private var observerLoginResponse: Observer<Result<LoginResponse>> = Observer { result ->
        lifecycleScope.launchWhenStarted {
            lifecycle.repeatOnLifecycle(Lifecycle.State.STARTED) {
                launch {
                    when(result) {
                        is Result.Loading -> {
                            binding.pbLoading.showView()
                        }
                        is Result.Success -> {
                            binding.pbLoading.removeView()
                            delay(800)
                            result.data?.data?.let { response ->
                                val name = response.name
                                val email = response.email
                                val status = response.status
                                val image = response.image
                                val phone = response.numberPhone
                                val token = response.token

                                sessionManager.createAuthSession(
                                    name,
                                    email,
                                    status,
                                    image,
                                    phone,
                                    token
                                )
                                toHome()
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

    private fun toHome() {
        startActivity(Intent(this@LoginActivity, HomeActivity::class.java))
        finishAffinity()
    }

    private fun initView() {
        with(binding) {
            btnLogin.setOnClickListenerWithDebounce {
                val email = binding.etEmail.text.toString().trim()
                val password = binding.etPassword.text.toString().trim()
                viewModel.onValidation(email, password)
            }

            tvSignIn.setOnClickListenerWithDebounce {
                startActivity(Intent(this@LoginActivity, RegisterActivity::class.java))
            }
        }
    }

    private fun initInstance() {
        _binding = ActivityLoginBinding.inflate(layoutInflater)
        sessionManager = SessionManager(this)
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}
package com.example.learnkotlin.presentation.welcome

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.learnkotlin.databinding.ActivityWelcomeBinding
import com.example.learnkotlin.presentation.login.activity.LoginActivity
import com.example.learnkotlin.presentation.register.activity.RegisterActivity
import com.example.learnkotlin.util.setOnClickListenerWithDebounce
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class WelcomeActivity : AppCompatActivity() {

    private var _binding: ActivityWelcomeBinding? = null

    private val binding get() = _binding as ActivityWelcomeBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initInstance()
        setContentView(binding.root)
        initView()
    }

    private fun initView() {
        with(binding) {
            btnLogin.setOnClickListenerWithDebounce {
                startActivity(Intent(this@WelcomeActivity, LoginActivity::class.java))
            }
            btnRegister.setOnClickListenerWithDebounce {
                startActivity(Intent(this@WelcomeActivity, RegisterActivity::class.java))
            }
        }
    }

    private fun initInstance() {
        _binding = ActivityWelcomeBinding.inflate(layoutInflater)
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}
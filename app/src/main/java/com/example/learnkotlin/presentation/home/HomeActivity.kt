package com.example.learnkotlin.presentation.home

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import androidx.navigation.ui.setupWithNavController
import com.example.learnkotlin.R
import com.example.learnkotlin.databinding.ActivityHomeBinding
import com.example.learnkotlin.presentation.compiler.CompilerActivity
import com.example.learnkotlin.presentation.input.InputActivity
import com.example.learnkotlin.util.AUTH_STATUS.USER
import com.example.learnkotlin.util.SessionManager
import com.example.learnkotlin.util.removeView
import com.example.learnkotlin.util.setOnClickListenerWithDebounce
import com.google.android.material.bottomnavigation.BottomNavigationView
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class HomeActivity : AppCompatActivity() {

    private var _binding: ActivityHomeBinding? = null

    private val binding get() = _binding as ActivityHomeBinding

    private lateinit var sessionManager: SessionManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initInstance()
        setContentView(binding.root)
        iniView()
        val navView: BottomNavigationView = binding.navView
        val navController = findNavController(R.id.nav_host_fragment_activity_home)
        navView.setupWithNavController(navController)
    }

    private fun initInstance() {
        _binding = ActivityHomeBinding.inflate(layoutInflater)
        sessionManager = SessionManager(this)
    }

    private fun iniView() {
        sessionManager.status?.let { status ->
            if (status == USER) {
                binding.btnInput.removeView()
                return@let
            }
        }

        binding.btnCompiler.setOnClickListenerWithDebounce {
            startActivity(Intent(this, CompilerActivity::class.java))
        }

        binding.btnInput.setOnClickListenerWithDebounce {
            startActivity(Intent(this, InputActivity::class.java))
        }
    }
}
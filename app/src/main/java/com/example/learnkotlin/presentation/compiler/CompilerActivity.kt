package com.example.learnkotlin.presentation.compiler

import android.annotation.SuppressLint
import android.content.Intent
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.webkit.WebViewClient
import com.example.learnkotlin.R
import com.example.learnkotlin.databinding.ActivityCompilerBinding
import com.example.learnkotlin.presentation.home.HomeActivity
import com.example.learnkotlin.util.setOnClickListenerWithDebounce

class CompilerActivity : AppCompatActivity() {

    private var _binding: ActivityCompilerBinding? = null

    private val binding get() = _binding as ActivityCompilerBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initInstance()
        setContentView(binding.root)
        initWebView()
    }

    @SuppressLint("SetJavaScriptEnabled")
    private fun initWebView() {
        with(binding) {
            val setting = wv.settings
            setting.javaScriptEnabled = true
            setting.domStorageEnabled = true
            setting.allowContentAccess = true
            setting.useWideViewPort = true
            setting.loadWithOverviewMode = true

            wv.webViewClient = WebViewClient()
            wv.setBackgroundColor(Color.TRANSPARENT)
            wv.loadUrl("https://play.kotlinlang.org/#eyJ2ZXJzaW9uIjoiMS43LjIxIiwicGxhdGZvcm0iOiJqYXZhIiwiYXJncyI6IiIsIm5vbmVNYXJrZXJzIjp0cnVlLCJ0aGVtZSI6ImlkZWEiLCJjb2RlIjoiZnVuIG1haW4oKSB7XG4gICAgcHJpbnRsbihcIkhlbGxvLCB3b3JsZCEhIVwiKVxufSJ9")

            btnBack.setOnClickListenerWithDebounce {
                startActivity(Intent(this@CompilerActivity, HomeActivity::class.java))
                finishAffinity()
            }
        }
    }

    private fun initInstance() {
        _binding = ActivityCompilerBinding.inflate(layoutInflater)
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}
package com.example.learnkotlin.presentation.input

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.os.bundleOf
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.ui.setupWithNavController
import com.example.learnkotlin.R
import com.example.learnkotlin.data.remote.dto.DataInputKuisByIdItem
import com.example.learnkotlin.data.remote.dto.DataInputMateriByIdItem
import com.example.learnkotlin.databinding.ActivityInputBinding
import com.example.learnkotlin.util.SESSION.EDITKUIS
import com.example.learnkotlin.util.SESSION.EDITMATERI
import com.example.learnkotlin.util.fromJson
import com.example.learnkotlin.util.toJson
import com.google.android.material.bottomnavigation.BottomNavigationView
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class InputActivity : AppCompatActivity() {

    private var _binding: ActivityInputBinding? = null

    private val binding get() = _binding as ActivityInputBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initInstance()
        setContentView(binding.root)
        val navView: BottomNavigationView = binding.navView
        val navController = findNavController(R.id.nav_host_fragment_activity_input)
        val materi = intent.extras?.getString(EDITMATERI)?.fromJson<DataInputMateriByIdItem>()
        val kuis = intent.extras?.getString(EDITKUIS)?.fromJson<DataInputKuisByIdItem>()

        if (materi == null && kuis == null) {
            navView.setupWithNavController(navController)
        } else {
            navController.navigate(
                R.id.navigation_input_materi,
                bundleOf(EDITMATERI to materi?.toJson(DataInputMateriByIdItem::class.java))
            )
            navView.setOnItemSelectedListener { menuItem ->
                when(menuItem.itemId) {
                    R.id.navigation_input_materi -> {
                        navController.navigate(
                            R.id.navigation_input_materi,
                            bundleOf(EDITMATERI to materi?.toJson(DataInputMateriByIdItem::class.java))
                        )
                    }
                    R.id.navigation_input_kuis -> {
                        navController.navigate(
                            R.id.navigation_input_kuis,
                            bundleOf(EDITKUIS to kuis?.toJson(DataInputKuisByIdItem::class.java))
                        )
                    }
                }
                return@setOnItemSelectedListener true
            }
        }

    }

    private fun initAction(
        navView: BottomNavigationView,
        navController: NavController,
        dataMateri: DataInputMateriByIdItem?,
        dataKuis: DataInputKuisByIdItem?
    ) {

    }


    private fun initInstance() {
        _binding = ActivityInputBinding.inflate(layoutInflater)
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}
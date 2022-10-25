package com.example.learnkotlin.presentation.home.ui.profile

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import com.example.learnkotlin.R
import com.example.learnkotlin.databinding.FragmentProfileBinding
import com.example.learnkotlin.presentation.login.activity.LoginActivity
import com.example.learnkotlin.presentation.welcome.WelcomeActivity
import com.example.learnkotlin.util.SessionManager
import com.example.learnkotlin.util.loadImage
import com.example.learnkotlin.util.setOnClickListenerWithDebounce

class ProfileFragment : Fragment() {

    private var _binding: FragmentProfileBinding? = null

    private val binding get() = _binding as FragmentProfileBinding

    private lateinit var sessionManager: SessionManager

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentProfileBinding.inflate(layoutInflater)
        sessionManager = SessionManager(requireContext())
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initView()
    }

    private fun initView() {
        with(binding) {
            sessionManager.let { profile ->
                ivProfile.loadImage(profile.image.toString())
                tvEmail.text = profile.Email
                tvName.text = profile.Name
                tvPhone.text = profile.phone
            }
            btnLogout.setOnClickListenerWithDebounce {
                onLogout()
            }
        }
    }

    private fun onLogout() {
        AlertDialog.Builder(requireContext())
            .setTitle(R.string.title_logout)
            .setMessage(R.string.logout_message)
            .setPositiveButton("Ya") { _, _ ->
                sessionManager.logout()
                startActivity(Intent(requireContext(), WelcomeActivity::class.java))
                requireActivity().finishAffinity()
            }
            .setNegativeButton("Tidak") { i, _ ->
                i.dismiss()
            }
            .show()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
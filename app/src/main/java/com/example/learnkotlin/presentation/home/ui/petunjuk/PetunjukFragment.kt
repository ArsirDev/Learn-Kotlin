package com.example.learnkotlin.presentation.home.ui.petunjuk

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.learnkotlin.databinding.FragmentPetunjukBinding

class PetunjukFragment : Fragment() {

    private var _binding: FragmentPetunjukBinding? = null

    private val binding get() = _binding as FragmentPetunjukBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentPetunjukBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
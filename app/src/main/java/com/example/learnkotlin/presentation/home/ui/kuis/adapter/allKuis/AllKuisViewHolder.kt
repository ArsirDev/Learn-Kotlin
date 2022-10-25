package com.example.learnkotlin.presentation.home.ui.kuis.adapter.allKuis

import androidx.recyclerview.widget.RecyclerView
import com.example.learnkotlin.data.remote.dto.DataAllInputKuisItem
import com.example.learnkotlin.data.remote.dto.DataAllInputMateriItem
import com.example.learnkotlin.databinding.AllMateriItemLayoutBinding
import com.example.learnkotlin.util.loadImage

class AllKuisViewHolder(
    private val binding: AllMateriItemLayoutBinding
): RecyclerView.ViewHolder(binding.root) {
    fun bind(item: DataAllInputKuisItem) {
        with(binding) {
            ivMateri.loadImage(item.image)
            tvTitle.text = item.title
            tvDescription.text = item.question
        }
    }
}
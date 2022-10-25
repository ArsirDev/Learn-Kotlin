package com.example.learnkotlin.presentation.home.ui.materi.adapter.allMateri

import androidx.recyclerview.widget.RecyclerView
import com.example.learnkotlin.data.remote.dto.DataAllInputMateriItem
import com.example.learnkotlin.databinding.AllMateriItemLayoutBinding
import com.example.learnkotlin.util.loadImage

class AllMateriViewHolder(
    private val binding: AllMateriItemLayoutBinding
): RecyclerView.ViewHolder(binding.root) {
    fun bind(item: DataAllInputMateriItem) {
        with(binding) {
            ivMateri.loadImage(item.image)
            tvTitle.text = item.title
            tvDescription.text = item.description
        }
    }
}
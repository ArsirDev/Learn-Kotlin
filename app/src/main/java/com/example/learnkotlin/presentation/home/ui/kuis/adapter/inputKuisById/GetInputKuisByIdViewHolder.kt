package com.example.learnkotlin.presentation.home.ui.kuis.adapter.inputKuisById

import androidx.recyclerview.widget.RecyclerView
import com.example.learnkotlin.data.remote.dto.DataInputKuisByIdItem
import com.example.learnkotlin.data.remote.dto.DataInputMateriByIdItem
import com.example.learnkotlin.databinding.AdminItemLayoutBinding
import com.example.learnkotlin.databinding.AllMateriItemLayoutBinding
import com.example.learnkotlin.util.loadImage

class GetInputKuisByIdViewHolder(
    val binding: AdminItemLayoutBinding
): RecyclerView.ViewHolder(binding.root) {
    fun bind(item: DataInputKuisByIdItem) {
        with(binding) {
            ivMateri.loadImage(item.image)
            tvTitle.text = item.title
            tvDescription.text = item.question
        }
    }
}
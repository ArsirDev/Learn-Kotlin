package com.example.learnkotlin.presentation.home.ui.kuis.adapter.allKuis

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.learnkotlin.data.remote.dto.DataAllInputKuisItem
import com.example.learnkotlin.data.remote.dto.DataAllInputMateriItem
import com.example.learnkotlin.databinding.AllMateriItemLayoutBinding
import com.example.learnkotlin.util.setOnClickListenerWithDebounce

class AllKuisAdapter: RecyclerView.Adapter<AllKuisViewHolder>() {

    private val differCallback = object :DiffUtil.ItemCallback<DataAllInputKuisItem>() {
        override fun areItemsTheSame(
            oldItem: DataAllInputKuisItem,
            newItem: DataAllInputKuisItem
        ): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(
            oldItem: DataAllInputKuisItem,
            newItem: DataAllInputKuisItem
        ): Boolean {
            return oldItem == newItem
        }
    }

    val differ = AsyncListDiffer(this, differCallback)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AllKuisViewHolder {
        return AllKuisViewHolder(AllMateriItemLayoutBinding.inflate(LayoutInflater.from(parent.context), parent, false))
    }

    override fun onBindViewHolder(holder: AllKuisViewHolder, position: Int) {
        holder.apply {
            bind(differ.currentList[position].also { item ->
                itemView.setOnClickListenerWithDebounce {
                    onItemClickListener?.let { id ->
                        id(item.id)
                    }
                }
            })
        }
    }

    override fun getItemCount(): Int {
        return differ.currentList.size
    }

    private var onItemClickListener: ((Int) -> Unit)? = null

    fun setOnItemClickListener(listener: (Int) -> Unit) {
        onItemClickListener = listener
    }

    companion object {
        fun instance() = AllKuisAdapter()
    }
}
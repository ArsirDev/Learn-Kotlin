package com.example.learnkotlin.presentation.home.ui.materi.adapter.allMateri

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.learnkotlin.data.remote.dto.DataAllInputMateriItem
import com.example.learnkotlin.databinding.AllMateriItemLayoutBinding
import com.example.learnkotlin.util.setOnClickListenerWithDebounce

class AllMateriAdapter: RecyclerView.Adapter<AllMateriViewHolder>() {

    private val differCallback = object :DiffUtil.ItemCallback<DataAllInputMateriItem>() {
        override fun areItemsTheSame(
            oldItem: DataAllInputMateriItem,
            newItem: DataAllInputMateriItem
        ): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(
            oldItem: DataAllInputMateriItem,
            newItem: DataAllInputMateriItem
        ): Boolean {
            return oldItem == newItem
        }
    }

    val differ = AsyncListDiffer(this, differCallback)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AllMateriViewHolder {
        return AllMateriViewHolder(AllMateriItemLayoutBinding.inflate(LayoutInflater.from(parent.context), parent, false))
    }

    override fun onBindViewHolder(holder: AllMateriViewHolder, position: Int) {
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
        fun instance() = AllMateriAdapter()
    }
}
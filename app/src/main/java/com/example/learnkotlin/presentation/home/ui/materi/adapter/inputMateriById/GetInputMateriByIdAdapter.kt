package com.example.learnkotlin.presentation.home.ui.materi.adapter.inputMateriById

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.learnkotlin.data.remote.dto.DataAllInputMateriItem
import com.example.learnkotlin.data.remote.dto.DataInputMateriByIdItem
import com.example.learnkotlin.databinding.AdminItemLayoutBinding
import com.example.learnkotlin.databinding.AllMateriItemLayoutBinding
import com.example.learnkotlin.util.setOnClickListenerWithDebounce

class GetInputMateriByIdAdapter: RecyclerView.Adapter<GetInputMateriByIdViewHolder>() {

    private val differCallback = object :DiffUtil.ItemCallback<DataInputMateriByIdItem>() {
        override fun areItemsTheSame(
            oldItem: DataInputMateriByIdItem,
            newItem: DataInputMateriByIdItem
        ): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(
            oldItem: DataInputMateriByIdItem,
            newItem: DataInputMateriByIdItem
        ): Boolean {
            return oldItem == newItem
        }
    }

    val differ = AsyncListDiffer(this, differCallback)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): GetInputMateriByIdViewHolder {
        return GetInputMateriByIdViewHolder(AdminItemLayoutBinding.inflate(LayoutInflater.from(parent.context), parent, false))
    }

    override fun onBindViewHolder(holder: GetInputMateriByIdViewHolder, position: Int) {
        holder.apply {
            bind(differ.currentList[position].also { item ->
                itemView.setOnClickListenerWithDebounce {
                    Log.e("TAG", "onBindViewHolder: onItemView", )
                    onItemClickListener?.let { id ->
                        id(item.id)
                    }
                }
                binding.ivDelete.setOnClickListenerWithDebounce {
                    onDeleteItemClickListener?.let { id ->
                        id(item.id)
                    }
                }
                binding.ivUpdate.setOnClickListenerWithDebounce {
                    onUpdateItemClickListener?.let { data ->
                        data(item)
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

    private var onDeleteItemClickListener: ((Int) -> Unit)? = null

    fun setOnDeleteItemClickListener(listener: (Int) -> Unit) {
        onDeleteItemClickListener = listener
    }

    private var onUpdateItemClickListener: ((DataInputMateriByIdItem) -> Unit)? = null

    fun setOnUpdateItemCLickListener(listener: (DataInputMateriByIdItem) -> Unit) {
        onUpdateItemClickListener = listener
    }

    companion object {
        fun instance() = GetInputMateriByIdAdapter()
    }
}
package com.example.learnkotlin.presentation.home.ui.kuis.adapter.inputKuisById

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.learnkotlin.data.remote.dto.DataInputKuisByIdItem
import com.example.learnkotlin.databinding.AdminItemLayoutBinding
import com.example.learnkotlin.util.setOnClickListenerWithDebounce

class GetInputKuisByIdAdapter: RecyclerView.Adapter<GetInputKuisByIdViewHolder>() {

    private val differCallback = object :DiffUtil.ItemCallback<DataInputKuisByIdItem>() {
        override fun areItemsTheSame(
            oldItem: DataInputKuisByIdItem,
            newItem: DataInputKuisByIdItem
        ): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(
            oldItem: DataInputKuisByIdItem,
            newItem: DataInputKuisByIdItem
        ): Boolean {
            return oldItem == newItem
        }
    }

    val differ = AsyncListDiffer(this, differCallback)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): GetInputKuisByIdViewHolder {
        return GetInputKuisByIdViewHolder(AdminItemLayoutBinding.inflate(LayoutInflater.from(parent.context), parent, false))
    }

    override fun onBindViewHolder(holder: GetInputKuisByIdViewHolder, position: Int) {
        holder.apply {
            bind(differ.currentList[position].also { item ->
                binding.ivDelete.setOnClickListenerWithDebounce {
                    onDeleteItemClickListener?.let { id ->
                        id(item.id)
                    }
                }
                itemView.setOnClickListenerWithDebounce {
                    onItemClickListener?.let { id ->
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

    private var onUpdateItemClickListener: ((DataInputKuisByIdItem) -> Unit)? = null

    fun setOnUpdateItemCLickListener(listener: (DataInputKuisByIdItem) -> Unit) {
        onUpdateItemClickListener = listener
    }

    companion object {
        fun instance() = GetInputKuisByIdAdapter()
    }
}
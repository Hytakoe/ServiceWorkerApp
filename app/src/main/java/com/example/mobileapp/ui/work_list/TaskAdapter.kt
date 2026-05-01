package com.example.mobileapp.ui.workList

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.mobileapp.databinding.RvMainRowBinding
import com.example.mobileapp.ui.work_list.WorkListUiItem

class TaskAdapter(
    private val onItemClick: (WorkListUiItem) -> Unit = {},
    private val onCompleteClick: (Int) -> Unit = {}
) : ListAdapter<WorkListUiItem, TaskAdapter.ViewHolder>(WorkListUiItemDiffCallback()) {

    class ViewHolder(val binding: RvMainRowBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = RvMainRowBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val uiItem = getItem(position)
        val task = uiItem.task

        with(holder.binding) {
            tvCarName.text = task.carName
            tvJob.text = task.job
            tvComment.text = task.comment ?: ""

            val isCompleted = task.finishDate != null

            if (isCompleted) {
                imageView.visibility = View.VISIBLE
            } else {
                imageView.visibility = View.GONE
            }
        }
    }
}

class WorkListUiItemDiffCallback : DiffUtil.ItemCallback<WorkListUiItem>() {
    override fun areItemsTheSame(oldItem: WorkListUiItem, newItem: WorkListUiItem): Boolean {
        return oldItem.task.id == newItem.task.id
    }

    override fun areContentsTheSame(oldItem: WorkListUiItem, newItem: WorkListUiItem): Boolean {
        return oldItem.task == newItem.task &&
                oldItem.task.finishDate == newItem.task.finishDate
    }
}
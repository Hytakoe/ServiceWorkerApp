package com.example.mobileapp.ui.workList

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.mobileapp.R
import com.example.mobileapp.data.model.Task
import com.example.mobileapp.ui.catalogue.WorkListUiItem

class TaskAdapter (): ListAdapter<WorkListUiItem, TaskAdapter.TaskViewHolder>(TaskCallBack()) {

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TaskViewHolder {
            val view = LayoutInflater.from(parent.context).inflate(R.layout.fragment_main, parent, false)
            return TaskViewHolder(view)
        }

        override fun onBindViewHolder(holder: TaskViewHolder, position: Int) {
            holder.bind(getItem(position))
        }

        inner class TaskViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
            private val ivTaskImage: ImageView = itemView.findViewById(R.id.imageView)
            private val tvCar: TextView = itemView.findViewById(R.id.tvCarName)
            private val tvJob: TextView = itemView.findViewById(R.id.tvJob)
            private val tvComment: TextView = itemView.findViewById(R.id.tvComment)


            fun bind(workListUiItem: WorkListUiItem) {
                val task: Task = workListUiItem.task

                tvCar.text = workListUiItem.task.carName
                tvJob.text = workListUiItem.task.job
                tvComment.text = workListUiItem.task.comment
            }
        }

        class TaskCallBack: DiffUtil.ItemCallback<WorkListUiItem>() {
            override fun areItemsTheSame(oldItem: WorkListUiItem, newItem: WorkListUiItem): Boolean {
                return oldItem.task.id == newItem.task.id
            }

            override fun areContentsTheSame(oldItem: WorkListUiItem, newItem: WorkListUiItem): Boolean {
                return oldItem == newItem
            }
        }
}
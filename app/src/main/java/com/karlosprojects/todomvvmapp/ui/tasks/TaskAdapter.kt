package com.karlosprojects.todomvvmapp.ui.tasks

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.karlosprojects.todomvvmapp.data.Task
import com.karlosprojects.todomvvmapp.databinding.ItemTaskBinding

class TaskAdapter : ListAdapter<Task, TaskAdapter.TaskViewHolder>(DiffCallback()) {
    /**
     * ListAdapter is a zip class of recyclerview.adapter
     * listAdapter is better whenever you use a reactive date source, when you get a completely new list passed to you,
     * when data in the tables changes, we get a complete new list of task through this Flow getAllTasks function, so
     * listAdapter can handle this properly because it can calculates the difference between the old and new list (in a background thread)
     *
     * ViewHolder is just a smart class that knows the single views in our layout and what data should be put there
     * ItemTaskBinding is automatically generated from our Item task layout
     * binding.root which is the root of our item task layout (the relative layout)
     */

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TaskViewHolder {
        //Define how to instantiate all of our viewholder classes
        val binding = ItemTaskBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return TaskViewHolder(binding)
    }

    override fun onBindViewHolder(holder: TaskViewHolder, position: Int) {
        //Define how to bind the data to the viewholder
        val currentItem = getItem(position)
        holder.bind(currentItem)
    }

    class TaskViewHolder(private val binding: ItemTaskBinding) : RecyclerView.ViewHolder(binding.root) {

        fun bind(task: Task) {
            binding.apply {
                itemTaskCkbCompleted.isChecked = task.completed
                itemTaskTxtName.text = task.name
                itemTaskTxtName.paint.isStrikeThruText = task.completed
                itemTaskImgPriority.isVisible = task.important
            }
        }
    }

    class DiffCallback : DiffUtil.ItemCallback<Task>() {

        //When the data remains the same (logic)
        override fun areItemsTheSame(oldItem: Task, newItem: Task) = oldItem.id == newItem.id

        //When the content changes, this function will know when the data is not the same anymore
        override fun areContentsTheSame(oldItem: Task, newItem: Task) = oldItem == newItem

    }

}
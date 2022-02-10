package com.coolightman.note.presentation.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.ListAdapter
import com.coolightman.note.databinding.TaskItemBinding
import com.coolightman.note.domain.entity.Task

class TasksAdapter(private val clickListener: (Long) -> Unit) :
    ListAdapter<Task, TaskViewHolder>(Task.DIFF) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TaskViewHolder {
        val binding = TaskItemBinding
            .inflate(LayoutInflater.from(parent.context), parent, false)
        return TaskViewHolder(binding)
    }

    override fun onBindViewHolder(holder: TaskViewHolder, position: Int) {
        val task = getItem(position)
        holder.binding.apply {
            tvDescription.text = task.description
            cvTaskItemRoot.setCardBackgroundColor(
                ContextCompat.getColor(root.context, task.color.colorResId)
            )
            root.setOnClickListener { clickListener(task.taskId) }
        }
    }
}
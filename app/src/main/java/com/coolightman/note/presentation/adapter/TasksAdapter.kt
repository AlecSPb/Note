package com.coolightman.note.presentation.adapter

import android.graphics.Paint.STRIKE_THRU_TEXT_FLAG
import android.view.LayoutInflater
import android.view.View.GONE
import android.view.View.VISIBLE
import android.view.ViewGroup
import androidx.appcompat.widget.AppCompatImageView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.ListAdapter
import com.coolightman.note.R
import com.coolightman.note.databinding.TaskItemBinding
import com.coolightman.note.domain.entity.Task
import com.google.android.material.textview.MaterialTextView

class TasksAdapter(
    private val clickListener: (Long) -> Unit,
    private val checkClickListener: (Long) -> Unit
) :
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
            setImportance(imgImportance, task.isImportant)
            setNotification(imgNotification, task.isReminding)
            setActive(this, task.isActive)
            imgCheck.setOnClickListener { checkClickListener(task.taskId) }
            root.setOnClickListener { clickListener(task.taskId) }
        }
    }

    private fun setActive(binding: TaskItemBinding, active: Boolean) {
        val view = binding.tvDescription
        val image = binding.imgCheck
        view.paintFlags = if (active) {
            image.setImageResource(R.drawable.ic_baseline_check_empty_circle_24)
            view.paintFlags and STRIKE_THRU_TEXT_FLAG.inv()
        } else {
            image.setImageResource(R.drawable.ic_baseline_task_alt_24)
            view.paintFlags or STRIKE_THRU_TEXT_FLAG
        }
    }

    private fun setNotification(view: AppCompatImageView, reminding: Boolean) {
        if (reminding) view.visibility = VISIBLE
        else view.visibility = GONE
    }

    private fun setImportance(view: AppCompatImageView, important: Boolean) {
        if (important) view.visibility = VISIBLE
        else view.visibility = GONE
    }
}
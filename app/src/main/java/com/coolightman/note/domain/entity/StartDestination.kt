package com.coolightman.note.domain.entity

import com.coolightman.note.R

enum class StartDestination(val destinationId: Int) {
    NOTES(R.id.navigation_notes),
    TASKS(R.id.navigation_tasks)
}
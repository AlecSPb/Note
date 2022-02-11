package com.coolightman.note.util

import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import com.coolightman.note.data.database.dbModel.TaskDb

fun MediatorLiveData<List<TaskDb>>.chainData(
    vararg lists: LiveData<List<TaskDb>>
): MediatorLiveData<List<TaskDb>> {
    for (list in lists) {
        this.addSource(list) { value = combineLists(*lists) }
    }
    return this
}

private fun combineLists(vararg lists: LiveData<List<TaskDb>>): List<TaskDb> {
    val result = mutableListOf<TaskDb>()

    for (list in lists) {
        val value = list.value
        value?.let { it.forEach { item -> result.add(item) } }
    }

    return result
}
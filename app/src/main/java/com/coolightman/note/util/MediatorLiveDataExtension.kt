package com.coolightman.note.util

import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import com.coolightman.note.data.database.dbModel.TaskDb

fun MediatorLiveData<List<TaskDb>>.chainData(
    list1: LiveData<List<TaskDb>>,
    list2: LiveData<List<TaskDb>>,
    list3: LiveData<List<TaskDb>>,
    list4: LiveData<List<TaskDb>>,
): MediatorLiveData<List<TaskDb>> {

    this.addSource(list1) {
        value = combineLists(list1, list2, list3, list4)
    }
    this.addSource(list2) {
        value = combineLists(list1, list2, list3, list4)
    }
    this.addSource(list3) {
        value = combineLists(list1, list2, list3, list4)
    }
    this.addSource(list4) {
        value = combineLists(list1, list2, list3, list4)
    }
    return this
}

private fun combineLists(vararg lists: LiveData<List<TaskDb>>): List<TaskDb> {
    val result = mutableListOf<TaskDb>()

    for (list in lists) {
        val value = list.value
        value?.let { it.forEach { taskDb -> result.add(taskDb) } }
    }

    return result
}
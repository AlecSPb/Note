package com.coolightman.note.data.converter

import com.coolightman.note.data.database.dbModel.TaskDb
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

fun List<TaskDb>.toJson(): String {
    return Gson().toJson(this)
}

fun convertJsonToTasks(json: String): List<TaskDb> {
    val itemType = object : TypeToken<List<TaskDb>>() {}.type
    return Gson().fromJson(json, itemType)
}
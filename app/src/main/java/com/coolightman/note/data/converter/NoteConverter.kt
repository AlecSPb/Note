package com.coolightman.note.data.converter

import com.coolightman.note.data.database.dbModel.NoteDb
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

fun List<NoteDb>.toJson(): String {
    return Gson().toJson(this)
}

fun convertJsonToNotes(json: String): List<NoteDb> {
    val itemType = object : TypeToken<List<NoteDb>>() {}.type
    return Gson().fromJson(json, itemType)
}
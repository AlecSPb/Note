package com.coolightman.note

import android.app.Application
import com.coolightman.note.di.DaggerApplicationComponent

class NoteApp: Application() {

    val component by lazy {
        DaggerApplicationComponent.factory().create(this)
    }
}
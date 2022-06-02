package com.coolightman.note.di

import android.content.Context
import com.coolightman.note.presentation.fragment.*
import dagger.BindsInstance
import dagger.Component


@Component(
    modules = [
        ViewModelModule::class,
        RepositoryBindModule::class,
        DatabaseModule::class
    ]
)
@ApplicationScope
interface ApplicationComponent {

    fun inject(notesFragment: NotesFragment)
    fun inject(editNoteFragment: EditNoteFragment)
    fun inject(notesTrashFragment: NotesTrashFragment)

    fun inject(tasksFragment: TasksFragment)
    fun inject(editTaskFragment: EditTaskFragment)

    @Component.Factory
    interface ApplicationComponentFactory {

        fun create(
            @BindsInstance context: Context
        ): ApplicationComponent
    }
}
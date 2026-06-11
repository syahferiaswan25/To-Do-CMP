package com.aswan.todo.presentation.screen.task.di

import com.aswan.todo.presentation.screen.task.TaskViewModel
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module

val taskModule = module {
    viewModelOf(::TaskViewModel)
}

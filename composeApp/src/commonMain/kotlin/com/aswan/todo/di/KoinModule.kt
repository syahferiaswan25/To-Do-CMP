package com.aswan.todo.di

import com.aswan.todo.data.FakeToDoRepositoryImpl
import com.aswan.todo.data.ToDoRepository
import com.aswan.todo.navigation.Navigator
import com.aswan.todo.presentation.screen.home.HomeViewModel
import com.aswan.todo.presentation.screen.task.TaskViewModel
import org.koin.core.KoinApplication
import org.koin.core.context.startKoin
import org.koin.core.module.dsl.singleOf
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module

val koinModule = module {
    singleOf(constructor = ::Navigator)
    single<ToDoRepository> { FakeToDoRepositoryImpl() }
    viewModelOf(::HomeViewModel)
    viewModelOf(::TaskViewModel)
}

fun initializeKoin(
    config: (KoinApplication.() -> Unit)? = null
) {
    startKoin {
        config?.invoke(this)
        modules(koinModule)
    }
}
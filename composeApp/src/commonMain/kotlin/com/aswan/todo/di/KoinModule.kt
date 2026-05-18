package com.aswan.todo.di

import com.aswan.todo.data.ToDoRepositoryImpl
import com.aswan.todo.domain.repository.ToDoRepository
import com.aswan.todo.navigation.Navigator
import com.aswan.todo.presentation.screen.home.HomeViewModel
import com.aswan.todo.presentation.screen.task.TaskViewModel
import org.koin.core.KoinApplication
import org.koin.core.context.startKoin
import org.koin.core.module.Module
import org.koin.core.module.dsl.singleOf
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module

expect val targetModule: Module
val koinModule = module {
    singleOf(constructor = ::Navigator)
    //single<ToDoRepository> { FakeToDoRepositoryImpl() }
    single<ToDoRepository> { ToDoRepositoryImpl(get()) }
    viewModelOf(::HomeViewModel)
    viewModelOf(::TaskViewModel)
}

fun initializeKoin(
    config: (KoinApplication.() -> Unit)? = null
) {
    startKoin {
        config?.invoke(this)
        modules(koinModule, targetModule)
    }
}
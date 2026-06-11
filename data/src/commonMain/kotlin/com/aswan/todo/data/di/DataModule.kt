package com.aswan.todo.data.di

import com.aswan.todo.data.ToDoRepositoryImpl
import com.aswan.todo.domain.repository.ToDoRepository
import org.koin.core.module.Module
import org.koin.dsl.module

expect val platformDataModule: Module

val dataModule = module {
    single<ToDoRepository> { ToDoRepositoryImpl(get()) }
}

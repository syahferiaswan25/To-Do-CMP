package com.aswan.todo.di

import com.aswan.todo.data.DatabaseDrivenFactory
import com.aswan.todo.data.IosDatabaseDriverFactory
import org.koin.core.module.Module
import org.koin.dsl.module

actual val targetModule = module {
    single<DatabaseDrivenFactory> { IosDatabaseDriverFactory() }
}
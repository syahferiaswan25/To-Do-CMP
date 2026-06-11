package com.aswan.todo.data.di

import com.aswan.todo.data.IosDatabaseDriverFactory
import com.aswan.todo.data.DatabaseDrivenFactory
import org.koin.core.module.Module
import org.koin.dsl.module

actual val platformDataModule: Module = module {
    single<DatabaseDrivenFactory> { IosDatabaseDriverFactory() }
}

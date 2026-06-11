package com.aswan.todo.data.di

import com.aswan.todo.data.AndroidDatabaseDrivenFactory
import com.aswan.todo.data.DatabaseDrivenFactory
import org.koin.android.ext.koin.androidContext
import org.koin.core.module.Module
import org.koin.dsl.module

actual val platformDataModule: Module = module {
    single<DatabaseDrivenFactory> { AndroidDatabaseDrivenFactory(androidContext()) }
}

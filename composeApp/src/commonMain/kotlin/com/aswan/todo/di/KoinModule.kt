package com.aswan.todo.di

import com.aswan.todo.data.di.dataModule
import com.aswan.todo.data.di.platformDataModule
import com.aswan.todo.navigation.Navigator
import com.aswan.todo.presentation.screen.home.di.homeModule
import com.aswan.todo.presentation.screen.task.di.taskModule
import org.koin.core.KoinApplication
import org.koin.core.context.startKoin
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module

val appModule = module {
    singleOf(constructor = ::Navigator)
}

fun initializeKoin(
    config: (KoinApplication.() -> Unit)? = null
) {
    startKoin {
        config?.invoke(this)
        modules(
            appModule,
            dataModule,
            platformDataModule,
            homeModule,
            taskModule
        )
    }
}

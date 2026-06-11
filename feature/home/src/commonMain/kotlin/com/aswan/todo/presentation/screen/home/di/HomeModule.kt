package com.aswan.todo.presentation.screen.home.di

import com.aswan.todo.presentation.screen.home.HomeViewModel
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module

val homeModule = module {
    viewModelOf(::HomeViewModel)
}

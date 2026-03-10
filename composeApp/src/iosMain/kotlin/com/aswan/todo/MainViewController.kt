package com.aswan.todo

import androidx.compose.ui.window.ComposeUIViewController
import com.aswan.todo.di.initializeKoin

fun MainViewController() = ComposeUIViewController(
    configure = { initializeKoin() }
) { App() }
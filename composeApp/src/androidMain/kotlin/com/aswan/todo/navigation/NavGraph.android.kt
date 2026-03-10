package com.aswan.todo.navigation

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.navigation3.runtime.entryProvider
import androidx.navigation3.ui.NavDisplay

@Composable
actual fun NavGraph() {
    val navigator = remember { Navigator() }

    NavDisplay(
        backStack = navigator.backstack,
        onBack = { navigator.goBack() },
        entryProvider = entryProvider {
            entry<Screen.Home>{
                Text("hello world")
            }
            entry<Screen.Task> {

            }
        }
    )
}
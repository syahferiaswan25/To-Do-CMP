package com.aswan.todo.navigation

import androidx.compose.runtime.Composable
import androidx.navigation3.runtime.entryProvider
import androidx.navigation3.ui.NavDisplay
import com.aswan.todo.presentation.screen.home.HomeScreen
import com.aswan.todo.presentation.screen.task.TaskScreen
import org.koin.compose.koinInject

@Composable
actual fun NavGraph() {
    val navigator = koinInject<Navigator>()

    NavDisplay(
        backStack = navigator.backstack,
        onBack = { navigator.goBack() },
        entryProvider = entryProvider {
            entry<Screen.Home> {
                HomeScreen(
                    navigateToTask = { taskId ->
                        navigator.navigateTo(Screen.Task(taskId))
                    }
                )
            }
            entry<Screen.Task> {
                TaskScreen(
                    id = it.id,
                    navigateBack = { navigator.goBack() }
                )
            }
        }
    )
}
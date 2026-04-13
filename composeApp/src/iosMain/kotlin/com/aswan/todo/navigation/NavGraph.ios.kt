package com.aswan.todo.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.toRoute
import com.aswan.todo.presentation.screen.home.HomeScreen
import com.aswan.todo.presentation.screen.task.TaskScreen

@Composable
actual fun NavGraph() {
    val navController = rememberNavController()

    NavHost(
        navController= navController,
        startDestination = Screen.Home
    ) {
        composable<Screen.Home> {
            HomeScreen(
                navigateToTask = { taskId ->
                    navController.navigate(Screen.Task(taskId))
                }
            )
        }
        composable<Screen.Task>{
            TaskScreen(
                id = it.toRoute<Screen.Task>().id,
                navigateBack = { navController.navigateUp() }
            )
        }
    }
}
package com.aswan.todo.navigation

import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.snapshots.SnapshotStateList

class Navigator {

    val backstack: SnapshotStateList<Screen> = mutableStateListOf(Screen.Home)

    fun navigateTo(screen: Screen) {
        backstack.add(screen)
    }

    fun navigateToTask(taskId: String?) {
        if (backstack.lastOrNull() is Screen.Task) {
            backstack[backstack.lastIndex] = Screen.Task(id = taskId)
        } else {
            backstack.add(Screen.Task(id = taskId))
        }
    }

    fun goBack() {
        backstack.removeLastOrNull()
    }
}
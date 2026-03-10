package com.aswan.todo.navigation

import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.snapshots.SnapshotStateList

class Navigator {

    val backstack: SnapshotStateList<Screen> = mutableStateListOf(Screen.Home)

    fun navigateTo(screen: Screen) {
        backstack.add(screen)
    }

    fun goBack() {
        backstack.removeLastOrNull()
    }
}
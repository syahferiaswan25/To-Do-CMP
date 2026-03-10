package com.aswan.todo

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import com.aswan.todo.navigation.NavGraph
import com.aswan.todo.util.darkScheme
import com.aswan.todo.util.lightScheme

@Composable
@Preview
fun App() {
    val colorScheme = if (isSystemInDarkTheme()) darkScheme else lightScheme

    MaterialTheme(colorScheme = colorScheme) {
        NavGraph()
    }
}
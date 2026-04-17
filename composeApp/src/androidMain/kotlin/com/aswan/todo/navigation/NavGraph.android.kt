package com.aswan.todo.navigation

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.adaptive.ExperimentalMaterial3AdaptiveApi
import androidx.compose.material3.adaptive.currentWindowAdaptiveInfo
import androidx.compose.material3.adaptive.layout.calculatePaneScaffoldDirective
import androidx.compose.material3.adaptive.navigation3.ListDetailSceneStrategy
import androidx.compose.material3.adaptive.navigation3.rememberListDetailSceneStrategy
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.unit.dp
import androidx.navigation3.runtime.entryProvider
import androidx.navigation3.ui.NavDisplay
import com.aswan.todo.presentation.component.InfoCard
import com.aswan.todo.presentation.screen.home.HomeScreen
import com.aswan.todo.presentation.screen.task.TaskScreen
import com.aswan.todo.util.Resource
import org.koin.compose.koinInject

@OptIn(ExperimentalMaterial3AdaptiveApi::class)
@Composable
actual fun NavGraph() {
    val navigator = koinInject<Navigator>()
    val windowAdapterInfo = currentWindowAdaptiveInfo()
    val directive = remember(key1 = windowAdapterInfo) {
        calculatePaneScaffoldDirective(windowAdapterInfo)
            .copy(horizontalPartitionSpacerSize = 0.dp)
    }
    val listDetailStrategy = rememberListDetailSceneStrategy<Any>(directive = directive)

    NavDisplay(
        backStack = navigator.backstack,
        onBack = { navigator.goBack() },
        sceneStrategy = listDetailStrategy,
        entryProvider = entryProvider {
            entry<Screen.Home>(
                metadata = ListDetailSceneStrategy.listPane(
                    detailPlaceholder = {
                        InfoCard(
                            lightModeIcon = Resource.Image.PAINTING_LIGHT,
                            darkModeIcon = Resource.Image.PAINTING_DARK,
                            message = "Select an existing Task or create new one",
                            contentColor = MaterialTheme.colorScheme.surfaceVariant,
                        )
                    }
                )
            ) {
                HomeScreen(
                    navigateToTask = { taskId ->
                        navigator.navigateToTask(taskId = taskId)
                    }
                )
            }
            entry<Screen.Task>(
                metadata = ListDetailSceneStrategy.detailPane()
            ) {
                TaskScreen(
                    id = it.id,
                    navigateBack = { navigator.goBack() }
                )
            }
        }
    )
}
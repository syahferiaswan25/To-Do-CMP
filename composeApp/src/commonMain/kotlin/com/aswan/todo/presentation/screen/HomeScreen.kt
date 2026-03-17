package com.aswan.todo.presentation.screen

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.FloatingActionButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.aswan.todo.presentation.component.InfoCard
import com.aswan.todo.presentation.component.LoadingCard
import com.aswan.todo.presentation.component.TaskCard
import com.aswan.todo.util.DisplayResult
import com.aswan.todo.util.Resource
import org.jetbrains.compose.resources.painterResource
import org.koin.compose.viewmodel.koinViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    navigateToTask: (String?) -> Unit
) {
    val viewModel = koinViewModel<HomeViewModel>()
    val allTasks by viewModel.allTasks.collectAsStateWithLifecycle()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(text = "To Do") },
                navigationIcon = {
                    IconButton(onClick = {}) {
                        Icon(
                            painter = painterResource(Resource.Icon.HAMBURGER_MENU),
                            contentDescription = "Hamburger menu icon"
                        )
                    }
                }
            )
        },
        floatingActionButton = {
            ExtendedFloatingActionButton(
                onClick = { navigateToTask(null)},
                elevation = FloatingActionButtonDefaults.elevation(defaultElevation = 0.dp)
            ) {
                Icon(
                    modifier = Modifier.size(16.dp),
                    painter = painterResource(Resource.Icon.ADD),
                    contentDescription = "this is icon"
                )
                Spacer(modifier = Modifier.width(4.dp))
                Text(text = "New Task")
            }
        }
    ) { padding ->
        allTasks.DisplayResult(
            modifier = Modifier.padding(
                top = padding.calculateTopPadding(),
                bottom = padding.calculateBottomPadding(),
            ),
            onLoading = { LoadingCard() },
            onSuccess = { tasks ->
                if (tasks.isNotEmpty()) {
                    LazyColumn(
                        verticalArrangement = Arrangement.spacedBy(12.dp),
                        contentPadding = PaddingValues(all = 12.dp)
                    ) {
                        items(
                            items = tasks,
                            key = { it.id },
                        ) {
                            TaskCard(
                                task = it,
                                onClick = {

                                }
                            )
                        }
                    }
                }
            },
            onError = { message ->
                InfoCard(
                    message = message,
                    lightModeIcon = Resource.Image.WARNING_LIGHT,
                    darkModeIcon = Resource.Image.WARNING_DARK
                )
            }
        )
    }
}
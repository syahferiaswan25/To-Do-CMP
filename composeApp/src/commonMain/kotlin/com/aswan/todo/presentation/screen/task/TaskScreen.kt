package com.aswan.todo.presentation.screen.task

import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.ime
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.aswan.todo.domain.Priority
import com.aswan.todo.presentation.component.InfoCard
import com.aswan.todo.presentation.component.LoadingCard
import com.aswan.todo.presentation.component.PriorityChip
import com.aswan.todo.presentation.component.PriorityChipSize
import com.aswan.todo.util.Alpha
import com.aswan.todo.util.DisplayResult
import com.aswan.todo.util.Resource
import kotlinx.coroutines.launch
import org.jetbrains.compose.resources.painterResource
import org.koin.compose.viewmodel.koinViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TaskScreen(
    id: String?,
    navigateBack: () -> Unit
) {
    val viewModel = koinViewModel<TaskViewModel>()
    val uiState by viewModel.uiState
    val snackbarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()

    LaunchedEffect(Unit) {
        viewModel.loadData(id)
    }

    Scaffold(
        snackbarHost = { SnackbarHost(snackbarHostState) },
        topBar = {
            TopAppBar(
                title = { Text(text = "Task Form") },
                navigationIcon = {
                    IconButton(onClick = navigateBack) {
                        Icon(
                            painter = painterResource(Resource.Icon.BACK_ARROW),
                            contentDescription = "Hamburger menu icon"
                        )
                    }
                }
            )
        },
    ) { paddingValues ->
        /*uiState.DisplayResult(
            onLoading = {
                LoadingCard()
            },
            onSuccess = { data ->
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(
                            top = paddingValues.calculateTopPadding(),
                            bottom = paddingValues.calculateBottomPadding() + 10.dp
                        )
                        .windowInsetsPadding(WindowInsets.ime)
                ) {
                    Column(
                        modifier = Modifier.weight(1f)
                            .padding(all = 16.dp)
                            .verticalScroll(rememberScrollState()),
                        verticalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        TaskInputSection(
                            formFieldState = FormFieldState(
                                title = "Task Title",
                                value = data.title,
                                placeholder = "Enter task title",
                                isRequired = true,
                                minLines = 1,
                                maxLines = 1,
                            ),
                            onValueChange = viewModel::updateTitle,
                        )

                        TaskInputSection(
                            formFieldState = FormFieldState(
                                title = "Task Description",
                                value = data.description,
                                placeholder = "Enter task description",
                                isRequired = false,
                                minLines = 3,
                                maxLines = 6,
                            ),
                            onValueChange = viewModel::updateDescription,
                        )

                        PrioritySection(
                            selectedPriority = data.priority,
                            onPrioritySelected = viewModel::updatePriority
                        )
                    }

                    Button(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(56.dp)
                            .padding(horizontal = 16.dp),
                        onClick = {
                            viewModel.saveTask(
                                onSuccess = {
                                    scope.launch {
                                        snackbarHostState.showSnackbar(
                                            message = if (id != null) "Task updated"
                                            else "New task created"
                                        )
                                    }
                                },
                                onError = { message ->
                                    scope.launch {
                                        snackbarHostState.showSnackbar(message)
                                    }
                                }
                            )
                        }
                    )
                    {
                        Text(text = if (id != null) "Update Task" else "Create Task")
                    }
                }
            },
            onError = { message ->
                InfoCard(
                    message = message,
                    lightModeIcon = Resource.Image.WARNING_LIGHT,
                    darkModeIcon = Resource.Image.WARNING_DARK
                )
            },
            transitionSpec = slideInVertically() + fadeIn() togetherWith
                    slideOutVertically() + fadeOut()
        )*/
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(
                    top = paddingValues.calculateTopPadding(),
                    bottom = paddingValues.calculateBottomPadding() + 10.dp
                )
                .windowInsetsPadding(WindowInsets.ime)
        ) {
            Column(
                modifier = Modifier.weight(1f)
                    .padding(all = 16.dp)
                    .verticalScroll(rememberScrollState()),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                TaskInputSection(
                    formFieldState = FormFieldState(
                        title = "Task Title",
                        value = uiState.title,
                        placeholder = "Enter task title",
                        isRequired = true,
                        minLines = 1,
                        maxLines = 1,
                    ),
                    onValueChange = viewModel::updateTitle,
                )

                TaskInputSection(
                    formFieldState = FormFieldState(
                        title = "Task Description",
                        value = uiState.description,
                        placeholder = "Enter task description",
                        isRequired = false,
                        minLines = 3,
                        maxLines = 6,
                    ),
                    onValueChange = viewModel::updateDescription,
                )

                PrioritySection(
                    selectedPriority = uiState.priority,
                    onPrioritySelected = viewModel::updatePriority
                )
            }

            Button(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp)
                    .padding(horizontal = 16.dp),
                onClick = {
                    viewModel.saveTask(
                        onSuccess = {
                            scope.launch {
                                snackbarHostState.showSnackbar(
                                    message = if (id != null) "Task updated"
                                    else "New task created"
                                )
                            }
                        },
                        onError = { message ->
                            scope.launch {
                                snackbarHostState.showSnackbar(message)
                            }
                        }
                    )
                }
            )
            {
                Text(text = if (id != null) "Update Task" else "Create Task")
            }
        }
    }
}

@Composable
fun TaskInputSection(
    modifier: Modifier = Modifier,
    onValueChange: (String) -> Unit,
    formFieldState: FormFieldState,
) {
    Column(modifier = modifier) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Text(
                text = formFieldState.title,
                style = MaterialTheme.typography.titleMedium
            )
            if (formFieldState.isRequired) {
                Text(
                    text = "*",
                    color = MaterialTheme.colorScheme.error,
                    fontWeight = FontWeight.Bold
                )
            }
        }

        Spacer(modifier = Modifier.height(6.dp))

        OutlinedTextField(
            modifier = Modifier.fillMaxWidth(),
            value = formFieldState.value,
            onValueChange = onValueChange,

            placeholder = {
                Text(
                    text = formFieldState.placeholder,
                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = Alpha.HALF)
                )
            },
            shape = RoundedCornerShape(12.dp),
            minLines = formFieldState.minLines,
            maxLines = formFieldState.maxLines,
        )
    }
}

@Composable
private fun PrioritySection(
    selectedPriority: Priority,
    onPrioritySelected: (Priority) -> Unit
) {
    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
        Text(
            text = "Priority",
            style = MaterialTheme.typography.titleMedium
        )

        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            Priority.entries.forEach { priority ->
                PriorityChip(
                    priority = priority,
                    size = PriorityChipSize.Large,
                    isSelected = priority == selectedPriority,
                    onSelect = onPrioritySelected,
                )
            }
        }
    }
}

@Preview
@Composable
fun TaskInputSectionPreview() {
    TaskScreen(null, navigateBack = {})
}

data class FormFieldState(
    val title: String,
    val value: String,
    val placeholder: String,
    val isRequired: Boolean,
    val minLines: Int,
    val maxLines: Int,
)
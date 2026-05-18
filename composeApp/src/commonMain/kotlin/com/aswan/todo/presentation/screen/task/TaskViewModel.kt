package com.aswan.todo.presentation.screen.task

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.aswan.todo.domain.repository.ToDoRepository
import com.aswan.todo.domain.Priority
import com.aswan.todo.domain.ToDoTask
import com.aswan.todo.util.RequestState
import kotlinx.coroutines.launch
import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid

data class TaskUiState(
    val id: String? = null,
    val title: String = "",
    val description: String = "",
    val priority: Priority = Priority.Low,
    val error: String? = null,
)

class TaskViewModel(
    private val repository: ToDoRepository,
) : ViewModel() {
    private var _uiState: MutableState<TaskUiState> = mutableStateOf(TaskUiState())
    val uiState: State<TaskUiState> = _uiState

    fun loadData(taskId: String?) {
        if (taskId != null) {
            val existingTask = repository.readSelectedTask(taskId)
            viewModelScope.launch {
                existingTask.collect { res ->
                    when {
                        res.isSuccess() -> {
                            _uiState.value = TaskUiState(
                                id = taskId,
                                title = res.getSuccessData().title,
                                description = res.getSuccessData().description,
                                priority = res.getSuccessData().priority
                            )
                        }
                        res.isError() -> {
                            _uiState.value = TaskUiState(
                                error = res.getErrorMessage()
                            )
                        }
                        else -> {
                            // todo loading ui
                        }
                    }
                }
            }
        } else {
            _uiState.value = TaskUiState()
        }
    }

    fun updateTitle(title: String) {
        /*if (_uiState.value.isSuccess()) {
            _uiState.value = RequestState.Success(
                data = _uiState.value.getSuccessData().copy(title = title)
            )
        }*/
        _uiState.value = _uiState.value.copy(title = title)
    }

    fun updateDescription(description: String) {
        /*if (_uiState.value.isSuccess()) {
            _uiState.value = RequestState.Success(
                data = _uiState.value.getSuccessData().copy(description = description)
            )
        }*/
        _uiState.value = _uiState.value.copy(description = description)
    }

    fun updatePriority(priority: Priority) {
        /*if (_uiState.value.isSuccess()) {
            _uiState.value = RequestState.Success(
                data = _uiState.value.getSuccessData().copy(priority = priority)
            )
        }*/
        _uiState.value = _uiState.value.copy(priority = priority)
    }

    @OptIn(ExperimentalUuidApi::class)
    fun saveTask(
        onSuccess: () -> Unit,
        onError: (String) -> Unit,
    ) {
        val uiStateData = _uiState.value
        val task = ToDoTask(
            id = uiStateData.id ?: Uuid.random().toHexString() ,
            title = uiStateData.title,
            description = uiStateData.description,
            priority = uiStateData.priority
        )

        viewModelScope.launch {
            val result = if (uiStateData.id != null) {
                repository.updateTask(task)
            } else {
                repository.createTask(task)
            }

            result.collect { res ->
                when {
                    res.isSuccess() -> {
                        // Handle success, e.g., navigate back or show a success message
                        onSuccess()
                    }
                    res.isError() -> {
                        onError(res.getErrorMessage())
                        // _uiState.value = result.getErrorMessage()
                    }
                    else -> {
                        // todo loading ui
                    }
                }
            }
        }
    }
}
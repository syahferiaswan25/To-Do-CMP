package com.aswan.todo.presentation.screen.task

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import com.aswan.todo.data.ToDoRepository
import com.aswan.todo.domain.Priority
import com.aswan.todo.domain.ToDoTask
import com.aswan.todo.util.RequestState
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
            if (existingTask.isSuccess()) {
                _uiState.value = TaskUiState(
                    id = taskId,
                    title = existingTask.getSuccessData().title,
                    description = existingTask.getSuccessData().description,
                    priority = existingTask.getSuccessData().priority
                )
            } /*else if (existingTask.isError()) {
                _uiState.value = RequestState.Error(existingTask.getErrorMessage())
            }*/
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
        /*if (_uiState.value.isSuccess()) {
            val uiStateData = _uiState.value.getSuccessData()

            val task = ToDoTask(
                id = uiStateData.id ?: Uuid.random().toHexString() ,
                title = uiStateData.title,
                description = uiStateData.description,
                priority = uiStateData.priority
            )

            val result = if (uiStateData.id != null) {
                repository.updateTask(task)
            } else {
                repository.createTask(task)
            }

            if (result.isSuccess()) {
                // Handle success, e.g., navigate back or show a success message
            } else if (result.isError()) {
                _uiState.value = RequestState.Error(result.getErrorMessage())
            }

        }*/

        val uiStateData = _uiState.value

        val task = ToDoTask(
            id = uiStateData.id ?: Uuid.random().toHexString() ,
            title = uiStateData.title,
            description = uiStateData.description,
            priority = uiStateData.priority
        )

        val result = if (uiStateData.id != null) {
            repository.updateTask(task)
        } else {
            repository.createTask(task)
        }

        if (result.isSuccess()) {
            // Handle success, e.g., navigate back or show a success message
            onSuccess()
        } else if (result.isError()) {
            onError(result.getErrorMessage())
           // _uiState.value = result.getErrorMessage()
        }
    }
}
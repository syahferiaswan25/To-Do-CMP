package com.aswan.todo.presentation.screen

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.aswan.todo.data.ToDoRepository
import com.aswan.todo.domain.ToDoTask
import com.aswan.todo.util.RequestState
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.stateIn

class HomeViewModel(
    private val repository: ToDoRepository
): ViewModel() {

    val allTasks = repository.readAllTask()
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = RequestState.Loading
        )

    /*val allTasks : StateFlow<RequestState<List<ToDoTask>>> = flowOf(RequestState.Error("Error fetching tasks"))
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = RequestState.Loading
        )*/

   /* val allTasks : StateFlow<RequestState<List<ToDoTask>>> = flowOf(RequestState.Loading)
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = RequestState.Loading
        )*/
}
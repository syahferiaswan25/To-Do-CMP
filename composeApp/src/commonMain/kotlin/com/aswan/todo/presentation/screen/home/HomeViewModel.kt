package com.aswan.todo.presentation.screen.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.aswan.todo.domain.Priority
import com.aswan.todo.domain.repository.ToDoRepository
import com.aswan.todo.domain.ToDoTask
import com.aswan.todo.util.RequestState
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn

class HomeViewModel(
    private val repository: ToDoRepository
) : ViewModel() {

    private var _searchQuery = MutableStateFlow("")
    val searchQuery: StateFlow<String> = _searchQuery

    private var _priorityFilter = MutableStateFlow<Priority>(Priority.None)
    val priorityFilter: StateFlow<Priority> = _priorityFilter

    val allTasks = combine(
        repository.readAllTask(context = viewModelScope.coroutineContext),
        _priorityFilter,
        _searchQuery
    ) { tasks, priority, query ->
        when (tasks) {
            is RequestState.Success -> {
                val filteredTask = tasks.data
                    .let { list ->
                        if (priority == Priority.None) list
                        else list.filter { it.priority == priority }
                    }
                    .let { list ->
                        query.let {
                            if (query.isBlank()) list
                            else list.filter {
                                it.title.contains(query, ignoreCase = false) ||
                                        it.description.contains(query, ignoreCase = false)
                            }
                        }
                    }.sortedByDescending { it.priority.ordinal }
                RequestState.Success(data = filteredTask)
            }

            else -> tasks
        }
    }.stateIn(
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

    fun markTaskAsCompleted(task: ToDoTask): RequestState<Unit> {
        return repository.updateTask(task)
    }

    fun removeTask(taskId: String): RequestState<Unit> {
        return repository.removeTask(taskId)
    }

    fun updateSearchQuery(query: String) {
        _searchQuery.value = query
    }

    fun updatePriorityFilter(priority: Priority) {
        _priorityFilter.value = priority
    }
}
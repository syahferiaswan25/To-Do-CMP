package com.aswan.todo.domain.repository

import com.aswan.todo.domain.ToDoTask
import com.aswan.todo.util.RequestState
import kotlinx.coroutines.flow.Flow
import kotlin.coroutines.CoroutineContext

interface ToDoRepository {
    fun createTask(task: ToDoTask): Flow<RequestState<Unit>>
    fun updateTask(task: ToDoTask): Flow<RequestState<Unit>>
    fun readSelectedTask(taskId: String): Flow<RequestState<ToDoTask>>
    fun readAllTask(context: CoroutineContext): Flow<RequestState<List<ToDoTask>>>
    fun removeTask(taskId: String): Flow<RequestState<Unit>>
}
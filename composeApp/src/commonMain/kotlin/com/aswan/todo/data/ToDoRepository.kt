package com.aswan.todo.data

import com.aswan.todo.domain.ToDoTask
import com.aswan.todo.util.RequestState
import kotlinx.coroutines.flow.Flow
import kotlin.coroutines.CoroutineContext

interface ToDoRepository {
    fun createTask(task: ToDoTask): RequestState<Unit>
    fun updateTask(task: ToDoTask): RequestState<Unit>
    fun readSelectedTask(taskId: String): RequestState<ToDoTask>
    fun readAllTask(/*context: CoroutineContext*/): Flow<RequestState<List<ToDoTask>>>
    fun removeTask(taskId: String): Flow<RequestState<ToDoTask>>
}
package com.aswan.todo.data

import androidx.compose.runtime.mutableStateListOf
import com.aswan.todo.domain.Priority
import com.aswan.todo.domain.ToDoTask
import com.aswan.todo.util.RequestState
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf
import kotlin.coroutines.CoroutineContext

class FakeToDoRepository : ToDoRepository {
    private val tasks = mutableStateListOf<ToDoTask>()

    init {
        tasks.addAll(
            listOf(
                ToDoTask(
                    title = "Simple task 1",
                    description = "Some random task",
                    isCompleted = true,
                    priority = Priority.Low
                ),
                ToDoTask(
                    title = "Simple task 2",
                    description = "Some random task",
                    isCompleted = false,
                    priority = Priority.Medium
                )
            )
        )
    }

    override fun createTask(task: ToDoTask): RequestState<Unit> {
        return try {
            val existingTask = tasks.find { it.title == task.title }
            if (existingTask != null) {
                RequestState.Error(message = "Task with the same title already exists")
            } else {
                tasks.add(task)
                RequestState.Success(data = Unit)
            }
        } catch (e: Exception) {
            RequestState.Error(message = "Failed to create task: ${e.message}")
        }
    }

    override fun updateTask(task: ToDoTask): RequestState<Unit> {
        return try {
            val index = tasks.indexOfFirst { it.id == task.id }
            if (index != -1) {
                tasks[index] = task
                RequestState.Success(data = Unit)
            } else {
                RequestState.Error(message = "Task with id: ${task.id} not found")
            }
        } catch (e: Exception) {
            RequestState.Error(message = "Failed to update task: ${e.message}")
        }
    }

    override fun readSelectedTask(taskId: String): RequestState<ToDoTask> {
        return try {
            val existingTask = tasks.find { it.id == taskId }
            if (existingTask != null) {
                RequestState.Success(data = existingTask)
            } else {
                RequestState.Error(message = "Task with id: $taskId not found")
            }
        } catch (e: Exception) {
            RequestState.Error(message = "Failed to update task: ${e.message}")
        }
    }

    override fun readAllTask(/*context: CoroutineContext*/): Flow<RequestState<List<ToDoTask>>> {
        return try {
            flowOf(RequestState.Success(data = tasks))
        } catch (e: Exception) {
            flowOf(RequestState.Error(message = "Failed to read all task: ${e.message}"))
        }
    }

    override fun removeTask(taskId: String): Flow<RequestState<ToDoTask>> {
        return try {
            val taskToRemove = tasks.find { it.id == taskId }
            if (taskToRemove != null) {
                tasks.remove(taskToRemove)
                flowOf(RequestState.Success(data = taskToRemove))
            } else {
                flowOf(RequestState.Error(message = "Task with id: $taskId not found"))
            }
        } catch (e: Exception) {
            flowOf(RequestState.Error(message = "Failed to remove task: ${e.message}"))

        }
    }
}
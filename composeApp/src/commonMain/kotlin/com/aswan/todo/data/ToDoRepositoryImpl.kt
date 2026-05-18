package com.aswan.todo.data

import app.cash.sqldelight.coroutines.asFlow
import app.cash.sqldelight.coroutines.mapToList
import com.aswan.todo.TaskDatabase
import com.aswan.todo.TaskTable
import com.aswan.todo.domain.Priority
import com.aswan.todo.domain.ToDoTask
import com.aswan.todo.domain.repository.ToDoRepository
import com.aswan.todo.util.RequestState
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import kotlin.coroutines.CoroutineContext
import kotlin.time.Clock

class ToDoRepositoryImpl(
    private val databaseDrivenFactory: DatabaseDrivenFactory
) : ToDoRepository {

    private val database = TaskDatabase(
        databaseDrivenFactory.createDriver()
    )
    private val query = database.taskDatabaseQueries

    override fun createTask(task: ToDoTask): Flow<RequestState<Unit>> {
        return flow {
            try {
                query.insertTask(
                    id = task.id,
                    title = task.title,
                    description = task.description,
                    isCompleted = if (task.isCompleted) 1 else 0,
                    priority = task.priority.name,
                    createdAt = Clock.System.now().toEpochMilliseconds(),
                    updatedAt = Clock.System.now().toEpochMilliseconds(),
                )

                emit(RequestState.Success(data = Unit))
            } catch (e: Exception) {
                emit(RequestState.Error(message = "${e.message}"))
            }
        }
    }

    override fun updateTask(task: ToDoTask): Flow<RequestState<Unit>> {
        return flow {
            try {
                query.updateTask(
                    title = task.title,
                    description = task.description,
                    isCompleted = if (task.isCompleted) 1 else 0,
                    priority = task.priority.name,
                    updatedAt = Clock.System.now().toEpochMilliseconds(),
                    id = task.id,
                )
                emit(RequestState.Success(data = Unit))
            } catch (e: Exception) {
                emit(RequestState.Error(message = "${e.message}"))
            }
        }
    }

    override fun readSelectedTask(taskId: String): Flow<RequestState<ToDoTask>> {
        return flow {
            try {
                val task = query.selectTaskById(taskId).executeAsOneOrNull()
                if (task != null) {
                    emit(
                        RequestState.Success(
                            data = task.convert()
                        )
                    )
                } else {
                    emit(RequestState.Error(message = "Task not found"))
                }
            } catch (e: Exception) {
                emit(RequestState.Error(message = "${e.message}"))
            }
        }
    }

    override fun readAllTask(context: CoroutineContext): Flow<RequestState<List<ToDoTask>>> {
        return query.selectAllTasks().asFlow().mapToList(context).map {
            RequestState.Success(data = it.map { it.convert() })
        }.catch {
            RequestState.Error(message = "${it.message}")
        }
    }

    override fun removeTask(taskId: String): Flow<RequestState<Unit>> {
        return flow {
            try {
                query.deleteTaskById(taskId)
                emit(RequestState.Success(data = Unit))
            } catch (e: Exception) {
                emit(RequestState.Error(message = "${e.message}"))
            }
        }
    }

    fun TaskTable.convert(): ToDoTask {
        return ToDoTask(
            id = this.id,
            title = this.title,
            description = this.description,
            isCompleted = this.isCompleted == 1L,
            priority = Priority.valueOf(this.priority),
            createdAt = this.createdAt,
            updatedAt = this.updatedAt,
        )
    }
}
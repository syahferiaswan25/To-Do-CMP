package com.aswan.todo.domain.usecase

import com.aswan.todo.domain.repository.ToDoRepository
import kotlinx.coroutines.CoroutineDispatcher

class CreateTaskUseCase(
    private val repository: ToDoRepository,
    coroutineDispatcher: CoroutineDispatcher,
) {

}
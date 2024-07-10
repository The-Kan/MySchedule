package com.my.schedule.ui.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.my.schedule.ui.api.Repository
import com.my.schedule.ui.data.todo.Todo
import com.my.schedule.ui.data.todo.TodoRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class TodoViewModel @Inject constructor(
    private val repository: TodoRepository,
    private val apiRepository: Repository
) : ViewModel() {
    val items: LiveData<List<Todo>> = repository.getAllTodos()
    val completedTodos: LiveData<List<Todo>> = repository.getAllCompletedTodos()

    suspend fun post() {
        apiRepository.post()
    }

    fun insert(todo: Todo) = viewModelScope.launch {
        repository.insert(todo)
    }

    fun update(todo: Todo) = viewModelScope.launch {
        repository.update(todo)
    }

    fun delete(todo: Todo) = viewModelScope.launch {
        repository.delete(todo)
    }

    fun deleteAll() = viewModelScope.launch {
        repository.deleteAll()
    }
}

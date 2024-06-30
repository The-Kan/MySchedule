package com.my.schedule.ui.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.my.schedule.ui.data.Todo
import com.my.schedule.ui.data.TodoRepository
import kotlinx.coroutines.launch

class TodoViewModel(private val repository: TodoRepository) : ViewModel() {
    val items: LiveData<List<Todo>> = repository.getAllTodos()

    fun insert(todo: Todo) = viewModelScope.launch {
        repository.insert(todo)
    }

    fun delete(todo: Todo) = viewModelScope.launch {
        repository.delete(todo)
    }

    fun deleteAll() = viewModelScope.launch {
        repository.deleteAll()
    }
}

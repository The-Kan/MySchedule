package com.my.schedule.ui.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.my.schedule.ui.data.Todo
import com.my.schedule.ui.data.TodoRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class TodoViewModel(private val repository: TodoRepository) : ViewModel() {
    private val _items = MutableLiveData<List<Todo>>(emptyList())
    val items: LiveData<List<Todo>> = _items

    fun insert(todo: Todo) = viewModelScope.launch {
        repository.insert(todo)
    }

    fun updateAll() = viewModelScope.launch {
        _items.value = repository.getAllTodos()
    }

//    fun updateItem(index: Int, newItem: String) {
//        _items.value = _items.value?.toMutableList()?.apply {
//            set(index, newItem)
//        }
//    }

    fun deleteAll() = viewModelScope.launch {
        repository.deleteAll()
    }
}

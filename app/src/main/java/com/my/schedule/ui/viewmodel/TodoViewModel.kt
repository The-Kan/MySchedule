package com.my.schedule.ui.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.my.schedule.ui.data.Todo
import com.my.schedule.ui.data.TodoRepository
import kotlinx.coroutines.launch

class TodoViewModel(private val repository: TodoRepository) : ViewModel() {
    private val _items = MutableLiveData<List<Todo>>(emptyList())
    val items: LiveData<List<Todo>> = _items

    fun insert(todo: Todo) = viewModelScope.launch {
        _items.value = _items.value?.toMutableList()?.apply {
            add(todo)
        }
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

    fun delete(todo: Todo) = viewModelScope.launch {
        _items.value = _items.value?.toMutableList()?.apply {
            remove(todo)
        }
        repository.delete(todo)
    }

    fun deleteAll() = viewModelScope.launch {
        repository.deleteAll()
    }
}

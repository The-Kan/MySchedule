package com.my.schedule.ui.data

import androidx.lifecycle.LiveData

class TodoRepository(private val todoDao: TodoDao) {

    suspend fun insert(todo: Todo) {
        todoDao.insert(todo)
    }

    suspend fun getAllTodos(): List<Todo> {
        return todoDao.getAllTodos()
    }

    suspend fun deleteAll() {
        todoDao.deleteAll()
    }
}
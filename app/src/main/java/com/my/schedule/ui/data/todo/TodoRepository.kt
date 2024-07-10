package com.my.schedule.ui.data.todo

import androidx.lifecycle.LiveData
import javax.inject.Inject

class TodoRepository @Inject constructor(private val todoDao: TodoDao) {

    suspend fun insert(todo: Todo) {
        todoDao.insert(todo)
    }

    fun getAllCompletedTodos(): LiveData<List<Todo>> {
        return todoDao.getAllCompletedTodos()
    }

    fun getAllTodos(): LiveData<List<Todo>> {
        return todoDao.getAllTodos()
    }

    suspend fun update(todo: Todo) {
        todoDao.update(todo)
    }

    suspend fun deleteAll() {
        todoDao.deleteAll()
    }

    suspend fun delete(todo: Todo) {
        todoDao.delete(todo)
    }
}
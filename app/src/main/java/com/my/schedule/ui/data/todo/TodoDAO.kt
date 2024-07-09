package com.my.schedule.ui.data.todo

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update

@Dao
interface TodoDao {
    @Insert
    suspend fun insert(todo: Todo)

    @Query("SELECT * FROM todo_table")
    fun getAllTodos(): LiveData<List<Todo>>

    @Update
    suspend fun update(todo: Todo)

    @Query("DELETE FROM todo_table")
    suspend fun deleteAll()

    @Delete
    suspend fun delete(todo: Todo)
}
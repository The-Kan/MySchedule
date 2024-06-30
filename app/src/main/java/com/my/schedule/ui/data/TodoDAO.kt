package com.my.schedule.ui.data

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query

@Dao
interface TodoDao {
    @Insert
    suspend fun insert(todo: Todo)

    @Query("SELECT * FROM todo_table")
    suspend fun getAllTodos(): List<Todo>

    @Query("DELETE FROM todo_table")
    suspend fun deleteAll()

    @Delete
    suspend fun delete(todo: Todo)
}
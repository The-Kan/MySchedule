package com.my.schedule.ui.data.todo

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "todo_table")
data class Todo(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    var todo: String = "",
    var date: String = "",
    var time: String = "",
    var notificationWorkId : String = "",
    var completed : Boolean = false
)

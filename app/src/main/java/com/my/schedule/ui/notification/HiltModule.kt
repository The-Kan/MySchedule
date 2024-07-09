package com.my.schedule.ui.notification

import android.content.Context
import com.my.schedule.ui.data.todo.TodoDao
import com.my.schedule.ui.data.todo.TodoDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
class TodoDatabaseModule {

    @Provides
    fun todoDao(db:TodoDatabase):TodoDao
            = db.todoDao()

    @Provides
    @Singleton
    fun provideDatabase(@ApplicationContext context: Context):TodoDatabase
            = TodoDatabase.getDatabase(context)
}
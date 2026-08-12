package com.example.todolist.di

import android.content.Context
import androidx.room.Room
import com.example.data.local.room.TaskDatabase
import com.example.data.local.dao.TaskDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {
    @Provides
    @Singleton
    fun provideTaskDatabase(@ApplicationContext context: Context): TaskDatabase =
        Room.databaseBuilder(context, TaskDatabase::class.java, "tasks.db").build()

    @Provides
    fun provideTaskDao(database: TaskDatabase): TaskDao = database.taskDao()
}
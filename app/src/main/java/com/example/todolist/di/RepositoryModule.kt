package com.example.todolist.di

import com.example.data.TaskRepositoryImpl
import com.example.data.local.TaskLocalDataSource
import com.example.data.local.room.RoomTaskDataSource
import com.example.data.remote.TaskRemoteDataSource
import com.example.data.remote.firebase.FirebaseTaskDataSource
import com.example.domain.repository.TaskRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {
    @Binds
    abstract fun bindTaskRepository(impl: TaskRepositoryImpl): TaskRepository

    @Binds
    abstract fun bindRemoteDataSource(impl: FirebaseTaskDataSource): TaskRemoteDataSource

    @Binds
    abstract fun bindLocalDataSource(impl: RoomTaskDataSource): TaskLocalDataSource
}
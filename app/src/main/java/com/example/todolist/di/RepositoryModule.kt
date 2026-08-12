package com.example.todolist.di

import com.example.data.remote.FirebaseTaskRepository
import com.example.domain.repository.TaskRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {
    @Binds
    abstract fun bindTaskRepository(impl: FirebaseTaskRepository): TaskRepository
}
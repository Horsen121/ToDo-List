package com.example.todolist.di

import com.example.data.remote.FirebaseTaskRepository
import com.example.domain.repository.TaskRepository
import com.example.domain.usecase.CompleteTaskUseCase
import com.example.domain.usecase.CreateTaskUseCase
import com.example.domain.usecase.DeleteTaskUseCase
import com.example.domain.usecase.ObserveTaskByIdUseCase
import com.example.domain.usecase.ObserveTasksUseCase
import com.example.domain.usecase.TakeInProgressUseCase
import com.example.domain.usecase.TaskUseCases
import com.google.firebase.Firebase
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.database

object AppModule {
    private val database: FirebaseDatabase by lazy { Firebase.database }
    private val tasksRef: DatabaseReference by lazy { database.getReference("tasks") }

    val taskRepository: TaskRepository by lazy { FirebaseTaskRepository(tasksRef) }

    val taskUseCases: TaskUseCases by lazy {
        TaskUseCases(
            observeTasks = ObserveTasksUseCase(taskRepository),
            observeTaskById = ObserveTaskByIdUseCase(taskRepository),
            createTask = CreateTaskUseCase(taskRepository),
            takeInProgress = TakeInProgressUseCase(taskRepository),
            complete = CompleteTaskUseCase(taskRepository),
            delete = DeleteTaskUseCase(taskRepository)
        )
    }
}
package com.example.todolist.di

import com.example.data.remote.TasksReference
import com.google.firebase.Firebase
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.database
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object FirebaseModule {

    @Provides
    @Singleton
    fun provideFirebaseDatabase(): FirebaseDatabase = Firebase.database

    @Provides
    @Singleton
    @TasksReference
    fun provideTasksRef(database: FirebaseDatabase): DatabaseReference =
        database.getReference("tasks")
}
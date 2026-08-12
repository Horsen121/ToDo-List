package com.example.data.remote.firebase

import com.example.data.remote.TaskRemoteDataSource
import com.example.data.remote.TasksReference
import com.example.data.remote.dto.TaskDto
import com.example.data.remote.mapper.toDomainTask
import com.example.domain.model.Task
import com.example.domain.model.TaskStatus
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ValueEventListener
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class FirebaseTaskDataSource @Inject constructor(
    @TasksReference private val tasksRef: DatabaseReference
) : TaskRemoteDataSource {

    override fun observeRemoteTasks(): Flow<List<Task>> = callbackFlow {
        val listener = object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                trySend(snapshot.children.mapNotNull { it.toDomainTask() })
            }
            override fun onCancelled(error: DatabaseError) { close(error.toException()) }
        }
        tasksRef.addValueEventListener(listener)
        awaitClose { tasksRef.removeEventListener(listener) }
    }

    override suspend fun addTask(shortDescription: String, fullDescription: String) {
        val key = tasksRef.push().key ?: throw IllegalStateException("Не удалось сгенерировать ключ")
        val dto = TaskDto(
            shortDescription,
            fullDescription,
            TaskStatus.NEW.name,
            System.currentTimeMillis()
        )
        tasksRef.child(key).setValue(dto).await()
    }

    override suspend fun updateStatus(taskId: String, newStatus: TaskStatus) {
        tasksRef.child(taskId).child("status").setValue(newStatus.name).await()
    }

    override suspend fun deleteTask(taskId: String) {
        tasksRef.child(taskId).removeValue().await()
    }
}
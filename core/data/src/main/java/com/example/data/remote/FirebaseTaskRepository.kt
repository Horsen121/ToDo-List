package com.example.data.remote

import com.example.domain.model.Task
import com.example.domain.model.TaskStatus
import com.example.domain.repository.TaskRepository
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ValueEventListener
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class FirebaseTaskRepository @Inject constructor(
    @TasksReference private val tasksRef: DatabaseReference
) : TaskRepository {

    override fun observeTasks(): Flow<List<Task>> = callbackFlow {
        val listener = object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val tasks = snapshot.children.mapNotNull { it.toDomainTask() }
                    .sortedByDescending { it.createdAt }
                trySend(tasks)
            }
            override fun onCancelled(error: DatabaseError) {
                close(error.toException())
            }
        }
        tasksRef.addValueEventListener(listener)
        awaitClose { tasksRef.removeEventListener(listener) }
    }

    override fun observeTaskById(taskId: String): Flow<Task?> = callbackFlow {
        val nodeRef = tasksRef.child(taskId)
        val listener = object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                trySend(snapshot.toDomainTask())
            }
            override fun onCancelled(error: DatabaseError) {
                close(error.toException())
            }
        }
        nodeRef.addValueEventListener(listener)
        awaitClose { nodeRef.removeEventListener(listener) }
    }

    override suspend fun addTask(shortDescription: String, fullDescription: String) {
        val key = tasksRef.push().key
            ?: throw IllegalStateException("Не удалось сгенерировать ключ задачи")
        val dto = TaskDto(
            shortDescription = shortDescription,
            fullDescription = fullDescription,
            status = TaskStatus.NEW.name,
            createdAt = System.currentTimeMillis()
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
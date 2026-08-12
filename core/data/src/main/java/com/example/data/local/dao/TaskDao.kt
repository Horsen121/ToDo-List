package com.example.data.local.dao

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Upsert
import com.example.data.local.entity.TaskEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface TaskDao {

    @Query("SELECT * FROM tasks ORDER BY createdAt DESC")
    fun observeAll(): Flow<List<TaskEntity>>

    @Query("SELECT * FROM tasks WHERE id = :taskId")
    fun observeById(taskId: String): Flow<TaskEntity?>

    @Upsert
    suspend fun upsertAll(tasks: List<TaskEntity>)

    @Query("DELETE FROM tasks WHERE id = :taskId")
    suspend fun deleteById(taskId: String)

    @Query("DELETE FROM tasks")
    suspend fun clearAll()
}
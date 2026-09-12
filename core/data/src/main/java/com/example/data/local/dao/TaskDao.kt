package com.example.data.local.dao

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Upsert
import com.example.data.local.entity.TaskEntity
import kotlinx.coroutines.flow.Flow

@Dao
abstract class TaskDao {

    @Query("SELECT * FROM tasks ORDER BY createdAt DESC")
    abstract fun observeAll(): Flow<List<TaskEntity>>

    @Query("SELECT * FROM tasks WHERE id = :taskId LIMIT 1")
    abstract fun observeById(taskId: String): Flow<TaskEntity?>

    @Upsert
    abstract suspend fun upsertAll(tasks: List<TaskEntity>)

    @Query("DELETE FROM tasks WHERE id = :taskId")
    abstract suspend fun deleteById(taskId: String)

    @Query("DELETE FROM tasks")
    abstract suspend fun clearAll()

    @Transaction
    open suspend fun replaceCache(tasks: List<TaskEntity>) {
        clearAll()
        upsertAll(tasks)
    }
}
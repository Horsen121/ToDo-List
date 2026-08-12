package com.example.data.local.room

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.data.local.dao.TaskDao
import com.example.data.local.entity.TaskEntity

@Database(
    entities = [TaskEntity::class],
    version = 1,
    exportSchema = true
)
abstract class TaskDatabase : RoomDatabase() {
    abstract fun taskDao(): TaskDao
}
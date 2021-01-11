package com.karlosprojects.todomvvmapp.data

import androidx.room.*
import kotlinx.coroutines.flow.Flow

@Dao
interface TaskDao {

    @Query("SELECT * FROM task WHERE name LIKE '%' || :searchQuery || '%' ORDER BY important DESC")
    fun getAllTasks(searchQuery : String): Flow<List<Task>> //Flow is an asynchronous stream of data, that's why it doesn't need to be a suspend function

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTask(task: Task)

    @Update
    suspend fun updateTask(task: Task)

    @Delete
    suspend fun deleteTask(task: Task)
}
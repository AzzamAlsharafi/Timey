package com.azzam.timey.data.repository

import androidx.lifecycle.LiveData
import com.azzam.timey.data.dao.TaskDao
import com.azzam.timey.data.entity.Task

class TaskRepository(private val taskDao: TaskDao) {
    val allTasks: LiveData<List<Task>> = taskDao.getAll()

    suspend fun insert(task: Task): Long = taskDao.insert(task)

    suspend fun delete(task: Task) = taskDao.delete(task)

    suspend fun update(task: Task) = taskDao.update(task)

    companion object {
        @Volatile
        private var INSTANCE: TaskRepository? = null

        fun getInstance(dao: TaskDao): TaskRepository{
            val tempInstance = INSTANCE
            if(tempInstance != null){
                return tempInstance
            }

            synchronized(this){
                val instance = TaskRepository(dao)
                INSTANCE = instance
                return instance
            }
        }
    }
}
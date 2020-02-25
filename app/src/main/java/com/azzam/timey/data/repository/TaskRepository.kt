package com.azzam.timey.data.repository

import androidx.lifecycle.LiveData
import com.azzam.timey.data.dao.TaskDao
import com.azzam.timey.data.entity.Task

class TaskRepository(private val taskDao: TaskDao) {
    val allTasks: LiveData<List<Task>> = taskDao.getAll()

    suspend fun insert(task: Task): Long = taskDao.insert(task)

    suspend fun delete(task: Task) = taskDao.delete(task)

    suspend fun update(task: Task) = taskDao.update(task)
}
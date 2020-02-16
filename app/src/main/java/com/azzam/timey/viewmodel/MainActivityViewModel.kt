package com.azzam.timey.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import com.azzam.timey.data.AppDatabase
import com.azzam.timey.data.entity.Event
import com.azzam.timey.data.entity.Habit
import com.azzam.timey.data.entity.Task
import com.azzam.timey.data.repository.EventRepository
import com.azzam.timey.data.repository.HabitRepository
import com.azzam.timey.data.repository.TaskRepository
import kotlinx.coroutines.launch

class MainActivityViewModel(application: Application) : AndroidViewModel(application) {

    private val database: AppDatabase

    private val eventRepository: EventRepository
    private val habitRepository: HabitRepository
    private val taskRepository: TaskRepository

    val allEvents: LiveData<List<Event>>
    val allHabits: LiveData<List<Habit>>
    val allTasks: LiveData<List<Task>>

    init {
        database = AppDatabase.getDatabase(application)

        eventRepository = EventRepository.getInstance(database.EventDao())
        habitRepository = HabitRepository.getInstance(database.HabitDao())
        taskRepository = TaskRepository.getInstance(database.TaskDao())

        allEvents = eventRepository.allEvents
        allHabits = habitRepository.allHabits
        allTasks = taskRepository.allTasks
    }

    fun insertEvent(event: Event) = viewModelScope.launch {
        eventRepository.insert(event)
    }

    fun insertHabit(habit: Habit) = viewModelScope.launch {
        habitRepository.insert(habit)
    }

    fun insertTask(task: Task) = viewModelScope.launch {
        taskRepository.insert(task)
    }
}
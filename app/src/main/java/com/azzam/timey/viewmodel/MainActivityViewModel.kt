package com.azzam.timey.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import com.azzam.timey.data.AppDatabase
import com.azzam.timey.data.OccurrencesGenerator
import com.azzam.timey.data.entity.Event
import com.azzam.timey.data.entity.Habit
import com.azzam.timey.data.entity.Occurrence
import com.azzam.timey.data.entity.Task
import com.azzam.timey.data.repository.EventRepository
import com.azzam.timey.data.repository.HabitRepository
import com.azzam.timey.data.repository.OccurrenceRepository
import com.azzam.timey.data.repository.TaskRepository
import kotlinx.coroutines.launch

class MainActivityViewModel(application: Application) : AndroidViewModel(application) {

    private val database: AppDatabase

    private val eventRepository: EventRepository
    private val habitRepository: HabitRepository
    private val taskRepository: TaskRepository
    private val occurrenceRepository: OccurrenceRepository

    val allEvents: LiveData<List<Event>>
    val allHabits: LiveData<List<Habit>>
    val allTasks: LiveData<List<Task>>

    init {
        database = AppDatabase.getDatabase(application)

        eventRepository = EventRepository(database.EventDao())
        habitRepository = HabitRepository(database.HabitDao())
        taskRepository = TaskRepository(database.TaskDao())
        occurrenceRepository = OccurrenceRepository(database.OccurrenceDao())

        allEvents = eventRepository.allEvents
        allHabits = habitRepository.allHabits
        allTasks = taskRepository.allTasks
    }

    fun insertEvent(event: Event) = viewModelScope.launch {
        val id = eventRepository.insert(event)
        event.id = id.toInt()
        occurrenceRepository.insert(OccurrencesGenerator.generateEventOccurrences(event))
    }

    fun insertHabit(habit: Habit) = viewModelScope.launch {
        val id = habitRepository.insert(habit)
        habit.id = id.toInt()
        occurrenceRepository.insert(OccurrencesGenerator.generateHabitOccurrences(habit))
    }

    fun insertTask(task: Task) = viewModelScope.launch {
        val id = taskRepository.insert(task)
        task.id = id.toInt()
        occurrenceRepository.insert(OccurrencesGenerator.generateTaskOccurrences(task))
    }
}
package com.azzam.timey.data.repository

import androidx.lifecycle.LiveData
import com.azzam.timey.data.dao.EventDao
import com.azzam.timey.data.entity.Event

class EventRepository private constructor(private val eventDao: EventDao) {
    val allEvents: LiveData<List<Event>> = eventDao.getAll()

    suspend fun insert(event: Event): Long = eventDao.insert(event)

    suspend fun delete(event: Event) = eventDao.delete(event)

    suspend fun update(event: Event) = eventDao.update(event)

    companion object {
        @Volatile
        private var INSTANCE: EventRepository? = null

        fun getInstance(dao: EventDao): EventRepository{
            val tempInstance = INSTANCE
            if(tempInstance != null){
                return tempInstance
            }

            synchronized(this){
                val instance = EventRepository(dao)
                INSTANCE = instance
                return instance
            }
        }
    }
}
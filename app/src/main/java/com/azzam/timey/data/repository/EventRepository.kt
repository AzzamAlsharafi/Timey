package com.azzam.timey.data.repository

import androidx.lifecycle.LiveData
import com.azzam.timey.data.dao.EventDao
import com.azzam.timey.data.entity.Event

class EventRepository (private val eventDao: EventDao) {
    val allEvents: LiveData<List<Event>> = eventDao.getAll()

    suspend fun getById(id: Int): Event = eventDao.getById(id)

    suspend fun getByIds(ids: List<Int>): List<Event> = eventDao.getByIds(ids)

    suspend fun insert(event: Event): Long = eventDao.insert(event)

    suspend fun delete(event: Event) = eventDao.delete(event)

    suspend fun update(event: Event) = eventDao.update(event)
}
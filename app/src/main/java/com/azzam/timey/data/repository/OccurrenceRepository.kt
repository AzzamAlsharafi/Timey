package com.azzam.timey.data.repository

import com.azzam.timey.data.dao.OccurrenceDao
import com.azzam.timey.data.entity.Occurrence

class OccurrenceRepository private constructor(private val occurrenceDao: OccurrenceDao){

    suspend fun insert(occurrences: List<Occurrence>): List<Long> = occurrenceDao.insert(occurrences)

    suspend fun deleteAll(parentId: Int) = occurrenceDao.deleteAll(parentId)

    suspend fun delete(occurrence: Occurrence) = occurrenceDao.delete(occurrence)

    companion object {

        // Singleton
        @Volatile
        private var INSTANCE: OccurrenceRepository? = null

        fun getInstance(dao: OccurrenceDao): OccurrenceRepository{
            val tempInstance = INSTANCE
            if(tempInstance != null){
                return tempInstance
            }

            synchronized(this){
                val instance = OccurrenceRepository(dao)
                INSTANCE = instance
                return instance
            }
        }
    }
}
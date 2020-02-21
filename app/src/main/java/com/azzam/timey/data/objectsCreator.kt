package com.azzam.timey.data

import android.content.Context
import android.util.Log
import com.azzam.timey.data.entity.*
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import java.util.*

class objectsCreator {
    private val repeatFirstSunday: Repeating = Repeating(Repeating.MONTHLY_BY_DAY_OF_WEEK, 1, "", Repeating.OCCURRENCES, 10)
    private val repeatYearly: Repeating = Repeating(Repeating.YEARLY, 1, "", Repeating.NEVER, 0)
    private val repeatYearlyEnding: Repeating = Repeating(Repeating.YEARLY, 1, "", Repeating.DATE, 1898888400000)
    private val repeatDaily: Repeating = Repeating(Repeating.DAILY, 1, "", Repeating.NEVER, 0)
    private val repeatTwoDaily: Repeating = Repeating(Repeating.DAILY, 2, "", Repeating.NEVER, 0)
    private val repeatWeekly: Repeating = Repeating(Repeating.WEEKLY, 1, "0000001", Repeating.NEVER, 0)
    private val repeatWeeklyWeekend: Repeating = Repeating(Repeating.WEEKLY, 1, "0000011", Repeating.DATE, 1614373200000)
    private val repeatWeeklyAll: Repeating = Repeating(Repeating.WEEKLY, 1, "1111111", Repeating.DATE, 1614373200000)

    private val oneTime: List<Date> = listOf(
        Date(68400000)
    )

    private val twoTimes: List<Date> = listOf(
        Date(14400000),
        Date(57600000)
    )

    val events: List<Event> = listOf(
        Event("Meeting", "Company's monthly meeting", Date(1583046000000), Date(1583049600000), false, repeatFirstSunday, Reminder(45, Reminder.MINUTES)),
        Event("Holidays Day", "Celebration of Holidays", Date(1583182800000), Date(1583182800000), true, repeatYearly, Reminder.NO_REMINDER),
        Event("Three Days", "Three Days Event which isn't three days long", Date(1583398800000), Date(1583416800000), false, repeatYearlyEnding, Reminder(5, Reminder.MINUTES))
    )

    val Tasks: List<Task> = listOf(
        Task("Read Task", "Read at least 50 pages of a book", Date(1582894800000), repeatTwoDaily, Reminder.NO_REMINDER),
        Task("Clean", "Clean the house", Date(1582956000000), repeatWeekly, Reminder.NO_REMINDER),
        Task("Develop Task", "Develop the app", Date(1582909200000), repeatDaily, Reminder.NO_REMINDER),
        Task("Hour of Code", "Compete in Hour of Code", Date(1583071200000), Repeating.NO_REPEATING, Reminder(10, Reminder.MINUTES))
    )

    val Habits: List<Habit> = listOf(
        Habit("Reading", "Reading 50 pages in the weekends at morning and evening", Date(1582837200000), twoTimes, repeatWeeklyWeekend, Reminder(5, Reminder.MINUTES)),
        Habit("Programming", "Programming daily for one hour", Date(1582837200000), oneTime, repeatWeeklyAll, Reminder.NO_REMINDER)
    )

    fun insertData(context: Context) = GlobalScope.launch{
        val database = AppDatabase.getDatabase(context)

        val eventDao = database.EventDao()
        val taskDao = database.TaskDao()
        val habitDao = database.HabitDao()

        for (e in events){
            Log.i("DATA-TAG", eventDao.insert(e).toString())
        }

        for (t in Tasks){
            Log.i("DATA-TAG", taskDao.insert(t).toString())
        }

        for (h in Habits){
            Log.i("DATA-TAG", habitDao.insert(h).toString())
        }
    }
}
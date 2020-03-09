package com.azzam.timey.data

import com.azzam.timey.data.entity.*
import org.threeten.bp.Duration
import org.threeten.bp.Instant
import org.threeten.bp.LocalDate
import org.threeten.bp.OffsetDateTime
import kotlin.math.ceil

object OccurrencesGenerator {

    // TODO: Add support for multiple daysOfWeek repeating, and multiple daysOfMonth.

    // Lambdas for checking if a day is valid when looping through the repeating range
    private val checkDaily =
        { _: LocalDate, _: LocalDate -> true } // Use this if repeating type is daily because every day is valid in daily repeating.

    private val checkDayOfWeek =
        { date: LocalDate, toCheck: LocalDate -> date.dayOfWeek == toCheck.dayOfWeek }

    private val checkDayOfMonth =
        { date: LocalDate, toCheck: LocalDate -> date.dayOfMonth == toCheck.dayOfMonth }

    // TODO: checkDayOfWeekOfMonth: support Fourth [dayOfWeek] AND Last [dayOfWeek] differently
    private val checkDayOfWeekOfMonth = { date: LocalDate, toCheck: LocalDate ->
        ceil(date.dayOfMonth.toFloat() / 7) == ceil(toCheck.dayOfMonth.toFloat() / 7) &&
                date.dayOfWeek == toCheck.dayOfWeek
    }

    private val checkDayOfYear =
        { date: LocalDate, toCheck: LocalDate -> date.month == toCheck.month && date.dayOfMonth == toCheck.dayOfMonth }

    fun generateEventOccurrences(event: Event): List<Occurrence> {
        if (event.repeating.repeatingValue == 0) { // None-repeating Events has one occurrence
            return listOf(
                Occurrence(
                    event.id,
                    Occurrence.PARENT_EVENT,
                    event.startDateTime,
                    event.endDateTime,
                    event.timezone,
                    event.reminder
                )
            )
        }

        return when (event.repeating.endType) {
            Repeating.DATE -> eventGenerateWithEndDate(event)
            Repeating.OCCURRENCES -> eventGenerateWithOccurrencesCount(event)
            else -> eventGenerateWithOccurrencesCount(event) // Repeating.NEVER
        }
    }

    private fun eventGenerateWithOccurrencesCount(event: Event): List<Occurrence> {
        val occurrences: MutableList<Occurrence> = mutableListOf()

        val r = event.repeating

        val count = if (r.endExtra > 0) {
            r.endExtra.toInt()
        } else {
            Occurrence.MAX_OCCURRENCES_COUNT
        }

        val start = Instant.ofEpochMilli(event.startDateTime).atZone(event.timezone)
        val end = Instant.ofEpochMilli(event.endDateTime).atZone(event.timezone)

        val duration = Duration.between(start, end)

        var date = start.toLocalDate()

        var matchingDaysCounts = 0

        val check = when (r.repeatingType) {
            Repeating.WEEKLY -> checkDayOfWeek
            Repeating.MONTHLY_BY_DAY -> checkDayOfMonth
            Repeating.MONTHLY_BY_DAY_OF_WEEK -> checkDayOfWeekOfMonth
            Repeating.YEARLY -> checkDayOfYear
            else -> checkDaily // Repeating.DAILY
        }

        while ((matchingDaysCounts / r.repeatingValue) < count) {
            if (check(date, start.toLocalDate())) {
                if (matchingDaysCounts % r.repeatingValue == 0) {
                    val occStart = start.with(date)
                    val occEnd = occStart.plus(duration)
                    val occurrence = Occurrence(
                        event.id,
                        Occurrence.PARENT_EVENT,
                        occStart.toInstant().toEpochMilli(),
                        occEnd.toInstant().toEpochMilli(),
                        event.timezone,
                        event.reminder
                    )
                    occurrences.add(occurrence)
                }
                matchingDaysCounts++
            }
            date = date.plusDays(1)
        }

        return occurrences.toList()
    }

    private fun eventGenerateWithEndDate(event: Event): List<Occurrence> {
        val occurrences: MutableList<Occurrence> = mutableListOf()

        val r = event.repeating

        val start = Instant.ofEpochMilli(event.startDateTime).atZone(event.timezone)
        val end = Instant.ofEpochMilli(event.endDateTime).atZone(event.timezone)

        val duration = Duration.between(start, end)

        var date = start.toLocalDate()
        val rEnd = Instant.ofEpochMilli(r.endExtra).atZone(event.timezone).toLocalDate().plusDays(1)

        var matchingDaysCounts = 0

        val check = when (r.repeatingType) {
            Repeating.WEEKLY -> checkDayOfWeek
            Repeating.MONTHLY_BY_DAY -> checkDayOfMonth
            Repeating.MONTHLY_BY_DAY_OF_WEEK -> checkDayOfWeekOfMonth
            Repeating.YEARLY -> checkDayOfYear
            else -> checkDaily // Repeating.DAILY
        }

        while (date.isBefore(rEnd)) {
            if (check(date, start.toLocalDate())) {
                if (matchingDaysCounts % r.repeatingValue == 0) {
                    val occStart = start.with(date)
                    val occEnd = occStart.plus(duration)
                    val occurrence = Occurrence(
                        event.id,
                        Occurrence.PARENT_EVENT,
                        occStart.toInstant().toEpochMilli(),
                        occEnd.toInstant().toEpochMilli(),
                        event.timezone,
                        event.reminder
                    )
                    occurrences.add(occurrence)
                }
                matchingDaysCounts++
            }
            date = date.plusDays(1)
        }

        return occurrences.toList()
    }

    fun generateTaskOccurrences(task: Task): List<Occurrence> {
        if (task.repeating.repeatingValue == 0) { // None-repeating Tasks has one occurrence
            return listOf(
                Occurrence(
                    task.id,
                    Occurrence.PARENT_TASK,
                    task.dateTime,
                    task.dateTime,
                    task.timezone,
                    task.reminder
                )
            )
        }

        return when (task.repeating.endType) {
            Repeating.DATE -> taskGenerateWithEndDate(task)
            Repeating.OCCURRENCES -> taskGenerateWithOccurrencesCount(task)
            else -> taskGenerateWithOccurrencesCount(task)// Repeating.NEVER
        }
    }

    private fun taskGenerateWithOccurrencesCount(task: Task): List<Occurrence> {
        val occurrences: MutableList<Occurrence> = mutableListOf()

        val r = task.repeating

        val count = if (r.endExtra > 0) {
            r.endExtra.toInt()
        } else {
            Occurrence.MAX_OCCURRENCES_COUNT
        }

        val start = Instant.ofEpochMilli(task.dateTime).atZone(task.timezone)

        var date = start.toLocalDate()

        var matchingDaysCounts = 0

        val check = when (r.repeatingType) {
            Repeating.WEEKLY -> checkDayOfWeek
            Repeating.MONTHLY_BY_DAY -> checkDayOfMonth
            Repeating.MONTHLY_BY_DAY_OF_WEEK -> checkDayOfWeekOfMonth
            Repeating.YEARLY -> checkDayOfYear
            else -> checkDaily // Repeating.DAILY
        }

        while ((matchingDaysCounts / r.repeatingValue) < count) {
            if (check(date, start.toLocalDate())) {
                if (matchingDaysCounts % r.repeatingValue == 0) {
                    val occStart = start.with(date).toInstant().toEpochMilli()
                    val occurrence = Occurrence(
                        task.id,
                        Occurrence.PARENT_TASK,
                        occStart,
                        occStart,
                        task.timezone,
                        task.reminder
                    )
                    occurrences.add(occurrence)
                }
                matchingDaysCounts++
            }
            date = date.plusDays(1)
        }

        return occurrences.toList()
    }

    private fun taskGenerateWithEndDate(task: Task): List<Occurrence> {
        val occurrences: MutableList<Occurrence> = mutableListOf()

        val r = task.repeating

        val start = Instant.ofEpochMilli(task.dateTime).atZone(task.timezone)

        var date = start.toLocalDate()
        val rEnd = Instant.ofEpochMilli(r.endExtra).atZone(task.timezone).toLocalDate().plusDays(1)

        var matchingDaysCounts = 0

        val check = when (r.repeatingType) {
            Repeating.WEEKLY -> checkDayOfWeek
            Repeating.MONTHLY_BY_DAY -> checkDayOfMonth
            Repeating.MONTHLY_BY_DAY_OF_WEEK -> checkDayOfWeekOfMonth
            Repeating.YEARLY -> checkDayOfYear
            else -> checkDaily // Repeating.DAILY
        }

        while (date.isBefore(rEnd)) {
            if (check(date, start.toLocalDate())) {
                if (matchingDaysCounts % r.repeatingValue == 0) {
                    val occStart = start.with(date).toInstant().toEpochMilli()
                    val occurrence = Occurrence(
                        task.id,
                        Occurrence.PARENT_TASK,
                        occStart,
                        occStart,
                        task.timezone,
                        task.reminder
                    )
                    occurrences.add(occurrence)
                }
                matchingDaysCounts++
            }
            date = date.plusDays(1)
        }

        return occurrences.toList()
    }

    fun generateHabitOccurrences(habit: Habit): List<Occurrence> {
        return when (habit.repeating.endType) {
            Repeating.DATE -> habitGenerateWithEndDate(habit)
            Repeating.OCCURRENCES -> habitGenerateWithOccurrencesCount(habit)
            else -> habitGenerateWithOccurrencesCount(habit) // Repeating.NEVER
        }
    }

    private fun habitGenerateWithOccurrencesCount(habit: Habit): List<Occurrence> {
        val occurrences: MutableList<Occurrence> = mutableListOf()

        val r = habit.repeating

        val count = if (r.endExtra > 0) {
            r.endExtra.toInt()
        } else {
            Occurrence.MAX_OCCURRENCES_COUNT
        }

        val start = Instant.ofEpochMilli(habit.startDate).atZone(habit.timezone)

        var date = start.toLocalDate()

        var matchingDaysCounts = 0

        val check = when (r.repeatingType) {
            Repeating.WEEKLY -> checkDayOfWeek
            Repeating.MONTHLY_BY_DAY -> checkDayOfMonth
            Repeating.MONTHLY_BY_DAY_OF_WEEK -> checkDayOfWeekOfMonth
            Repeating.YEARLY -> checkDayOfYear
            else -> checkDaily // Repeating.DAILY
        }

        while ((matchingDaysCounts / r.repeatingValue) < count) {
            if (check(date, start.toLocalDate())) {
                if (matchingDaysCounts % r.repeatingValue == 0) {
                    for (time in habit.times){
                        val occStart = start.with(date).with(time).toInstant().toEpochMilli()
                        val occurrence = Occurrence(
                            habit.id,
                            Occurrence.PARENT_HABIT,
                            occStart,
                            occStart,
                            habit.timezone,
                            habit.reminder
                        )
                        occurrences.add(occurrence)
                    }
                }
                matchingDaysCounts++
            }
            date = date.plusDays(1)
        }

        return occurrences.toList()
    }

    private fun habitGenerateWithEndDate(habit: Habit): List<Occurrence> {
        val occurrences: MutableList<Occurrence> = mutableListOf()

        val r = habit.repeating

        val start = Instant.ofEpochMilli(habit.startDate).atZone(habit.timezone)

        val rEnd = Instant.ofEpochMilli(r.endExtra).atZone(habit.timezone).toLocalDate().plusDays(1)

        var date = start.toLocalDate()

        var matchingDaysCounts = 0

        val check = when (r.repeatingType) {
            Repeating.WEEKLY -> checkDayOfWeek
            Repeating.MONTHLY_BY_DAY -> checkDayOfMonth
            Repeating.MONTHLY_BY_DAY_OF_WEEK -> checkDayOfWeekOfMonth
            Repeating.YEARLY -> checkDayOfYear
            else -> checkDaily // Repeating.DAILY
        }

        while (date.isBefore(rEnd)) {
            if (check(date, start.toLocalDate())) {
                if (matchingDaysCounts % r.repeatingValue == 0) {
                    for (time in habit.times){
                        val occStart = start.with(date).with(time).toInstant().toEpochMilli()
                        val occurrence = Occurrence(
                            habit.id,
                            Occurrence.PARENT_HABIT,
                            occStart,
                            occStart,
                            habit.timezone,
                            habit.reminder
                        )
                        occurrences.add(occurrence)
                    }
                }
                matchingDaysCounts++
            }
            date = date.plusDays(1)
        }

        return occurrences.toList()
    }
}
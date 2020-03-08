package com.azzam.timey.data

import com.azzam.timey.data.entity.*
import org.threeten.bp.Duration
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

        val count = if (r.endExtra.isNotEmpty()) {
            r.endExtra.toInt()
        } else {
            Occurrence.MAX_OCCURRENCES_COUNT
        }

        val start = event.startDateTime.atZoneSameInstant(event.timezone)
        val end = event.endDateTime.atZoneSameInstant(event.timezone)

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
                    val occStart: OffsetDateTime = start.with(date).toOffsetDateTime()
                    val occEnd: OffsetDateTime = occStart.plusSeconds(duration.seconds)
                    val occurrence = Occurrence(
                        event.id,
                        Occurrence.PARENT_EVENT,
                        occStart,
                        occEnd,
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

        val start = event.startDateTime.atZoneSameInstant(event.timezone)
        val end = event.endDateTime.atZoneSameInstant(event.timezone)

        val duration = Duration.between(start, end)

        var date = start.toLocalDate()
        val rEnd = LocalDate.parse(r.endExtra).plusDays(1)

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
                    val occStart: OffsetDateTime = start.with(date).toOffsetDateTime()
                    val occEnd: OffsetDateTime = occStart.plusSeconds(duration.seconds)
                    val occurrence = Occurrence(
                        event.id,
                        Occurrence.PARENT_EVENT,
                        occStart,
                        occEnd,
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

        val count = if (r.endExtra.isNotEmpty()) {
            r.endExtra.toInt()
        } else {
            Occurrence.MAX_OCCURRENCES_COUNT
        }

        val start = task.dateTime.atZoneSameInstant(task.timezone)

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
                    val occStart: OffsetDateTime = start.with(date).toOffsetDateTime()
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

        val start = task.dateTime.atZoneSameInstant(task.timezone)

        var date = start.toLocalDate()
        val rEnd = LocalDate.parse(r.endExtra).plusDays(1)

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
                    val occStart: OffsetDateTime = start.with(date).toOffsetDateTime()
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

        val count = if (r.endExtra.isNotEmpty()) {
            r.endExtra.toInt()
        } else {
            Occurrence.MAX_OCCURRENCES_COUNT
        }

        val start = habit.startDate.atZoneSameInstant(habit.timezone)

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
                    val occStart: OffsetDateTime = start.with(date).toOffsetDateTime()
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
                matchingDaysCounts++
            }
            date = date.plusDays(1)
        }

        return occurrences.toList()
    }

    private fun habitGenerateWithEndDate(habit: Habit): List<Occurrence> {
        val occurrences: MutableList<Occurrence> = mutableListOf()

        val r = habit.repeating

        val start = habit.startDate.atZoneSameInstant(habit.timezone)
        val end = habit.endDate.atZoneSameInstant(habit.timezone)

        val rEnd = end.toLocalDate().plusDays(1)

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
                    val occStart: OffsetDateTime = start.with(date).toOffsetDateTime()
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
                matchingDaysCounts++
            }
            date = date.plusDays(1)
        }

        return occurrences.toList()
    }
}
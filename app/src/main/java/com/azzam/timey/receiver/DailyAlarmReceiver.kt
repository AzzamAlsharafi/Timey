package com.azzam.timey.receiver

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.os.Build
import android.util.Log
import com.azzam.timey.data.AppDatabase
import com.azzam.timey.data.DataConverters
import com.azzam.timey.data.entity.*
import com.azzam.timey.data.repository.OccurrenceRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import org.threeten.bp.Instant
import org.threeten.bp.LocalDate
import org.threeten.bp.ZoneOffset
import org.threeten.bp.format.DateTimeFormatter
import org.threeten.bp.temporal.ChronoUnit

class DailyAlarmReceiver : BroadcastReceiver() {

    private lateinit var alarmManager: AlarmManager

    override fun onReceive(context: Context, intent: Intent) {
        Log.i("TAGGG", "DailyAlarm received")
        val todayDateTime = LocalDate.now(ZoneOffset.UTC).atStartOfDay(ZoneOffset.UTC)
        val tomorrowDateTime = todayDateTime.plusDays(1)

        val today = todayDateTime.toInstant().toEpochMilli()
        val tomorrow = tomorrowDateTime.toInstant().toEpochMilli()

        alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager

        val database = AppDatabase.getDatabase(context)
        val occurrenceRepository = OccurrenceRepository(database.OccurrenceDao())

        GlobalScope.launch(Dispatchers.IO) {
            val occurrences =
                occurrenceRepository.getAllInTimeRange(today, tomorrow, withReminder = true)
            setOccurrencesReminders(context, occurrences)
        }

        setTomorrowAlarm(context)
    }


    private fun setOccurrencesReminders(context: Context, occurrences: List<Occurrence>) {
        occurrences.forEach { setReminder(context, it) }
    }

    private fun setReminder(context: Context, occ: Occurrence) {
        Log.i("TAGGG", "SET ALARM FOR ${occ.id}")

        val intent = Intent(context, ReminderReceiver::class.java)
        intent.putExtra("id", occ.id)

        val pendingIntent = PendingIntent.getBroadcast(
            context,
            occ.id,
            intent,
            0
        )

        val unit = when (occ.reminder.unit) {
            Reminder.MINUTES -> ChronoUnit.MINUTES
            Reminder.HOURS -> ChronoUnit.HOURS
            else -> ChronoUnit.DAYS // Reminder.DAYS
        }

        val reminderTime = Instant.ofEpochMilli(occ.startDateTime).atZone(occ.timezone)
            .minus(occ.reminder.reminderValue.toLong(), unit)

        val triggerTime = reminderTime.toInstant().toEpochMilli()

        if (Build.VERSION.SDK_INT >= 23) {
            alarmManager.setExactAndAllowWhileIdle(
                AlarmManager.RTC_WAKEUP,
                triggerTime,
                pendingIntent
            )
        } else {
            alarmManager.setExact(AlarmManager.RTC_WAKEUP, triggerTime, pendingIntent)
        }
    }

    companion object {
        private const val DAILY_ALARM_REQUEST_CODE = 0

        fun setTomorrowAlarm(context: Context) {
            val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager

            val intent = Intent(context, DailyAlarmReceiver::class.java)
            val pendingIntent = PendingIntent.getBroadcast(
                context,
                DAILY_ALARM_REQUEST_CODE,
                intent,
                0
            )

            val tomorrowDate = LocalDate.now(ZoneOffset.UTC).plusDays(1)
                .atStartOfDay(ZoneOffset.UTC) // 00:00 in tomorrow's date UTC.
            val triggerTime = tomorrowDate.toInstant().toEpochMilli()

            Log.i(
                "TAGGG",
                "Set alarm at: ${tomorrowDate.format(DateTimeFormatter.ISO_ZONED_DATE_TIME)}"
            )

            if (Build.VERSION.SDK_INT >= 23) {
                alarmManager.setExactAndAllowWhileIdle(
                    AlarmManager.RTC_WAKEUP,
                    triggerTime,
                    pendingIntent
                )
            } else {
                alarmManager.setExact(AlarmManager.RTC_WAKEUP, triggerTime, pendingIntent)
            }
        }
    }
}

package com.azzam.timey

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.os.Build
import android.util.Log
import org.threeten.bp.LocalDate
import org.threeten.bp.ZoneOffset
import org.threeten.bp.format.DateTimeFormatter

class DailyAlarmReceiver : BroadcastReceiver() {

    override fun onReceive(context: Context, intent: Intent) {
        Log.i("TAGGG", "DailyAlarm received")
        setTomorrowAlarm(context)
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

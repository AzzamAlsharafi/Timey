package com.azzam.timey.receiver

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.util.Log

class ReminderReceiver : BroadcastReceiver() {

    override fun onReceive(context: Context, intent: Intent) {
        Log.i("TAGGG", "${intent.getIntExtra("id", -1)}   REMINDER ALARM")
        // TODO: Create notification for the reminder
    }
}

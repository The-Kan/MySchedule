package com.my.schedule.ui.notification

import android.content.Context
import android.content.SharedPreferences

class NotificationIDHelper(private val context: Context) {
    private val sharedPreferences: SharedPreferences by lazy {
        context.getSharedPreferences("notification_prefs", Context.MODE_PRIVATE)
    }

    var currentNotificationId: Int
        get() = sharedPreferences.getInt(KEY_LAST_NOTIFICATION_ID, 0)
        set(value) {
            sharedPreferences.edit().putInt(KEY_LAST_NOTIFICATION_ID, value).apply()
        }

    companion object {
        private const val KEY_LAST_NOTIFICATION_ID = "last_notification_id"
    }
}
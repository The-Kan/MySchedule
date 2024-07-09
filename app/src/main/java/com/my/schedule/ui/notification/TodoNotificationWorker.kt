package com.my.schedule.ui.notification

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context

import androidx.core.app.NotificationCompat
import androidx.work.Worker
import androidx.work.WorkerParameters
import com.my.schedule.R

class TodoNotificationWorker(context: Context, params: WorkerParameters) : Worker(context, params) {

    override fun doWork(): Result {
        val notificationManager =
            applicationContext.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        val channel = NotificationChannel(
            "1",
            "Todo",
            NotificationManager.IMPORTANCE_HIGH
        ).apply { description = "할 일에 대한 알림입니다." }
        notificationManager.createNotificationChannel(channel)

        val notification = NotificationCompat.Builder(applicationContext, "1")
            .setContentTitle("시간이 다가왔어요!")
            .setContentText(inputData.getString("todo"))
            .setSmallIcon(R.drawable.ic_launcher_foreground)
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .build()

        val notificationIDHelper = NotificationIDHelper(applicationContext)

        notificationManager.notify(notificationIDHelper.currentNotificationId++, notification)
        return Result.success()
    }
}
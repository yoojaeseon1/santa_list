package com.android.santa_list

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Context.NOTIFICATION_SERVICE
import android.content.Intent
import android.os.Build
import android.widget.Toast
import androidx.core.app.NotificationCompat

class AlarmReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context?, intent: Intent?) {



        if (intent != null) {
            val intent = Intent(context, ContactActivity::class.java).apply {
                flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            }
            val pendingIntent = PendingIntent.getActivity(
                context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT or
                        PendingIntent.FLAG_IMMUTABLE
            )
            val manager =
                context?.getSystemService(NOTIFICATION_SERVICE) as NotificationManager
            val builder: NotificationCompat.Builder
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                val channelId = "one-channel"
                val channelName = "My Channel One"
                val importance = NotificationManager.IMPORTANCE_DEFAULT
                val channel = NotificationChannel(
                    channelId,
                    channelName,
                    importance
                )
                manager.createNotificationChannel(channel)
                builder = NotificationCompat.Builder(context, channelId)
            } else {
                builder = NotificationCompat.Builder(context)
            }
            builder.run {
                setSmallIcon(R.drawable.ic_alert_on)
                setWhen(System.currentTimeMillis())
                setContentTitle("ddd")
                setContentText("ddd")
                setContentIntent(pendingIntent)
                setAutoCancel(true)
            }
            manager.notify(1, builder.build())
            Toast.makeText(context, "alarm", Toast.LENGTH_SHORT).show()
        }
    }


}
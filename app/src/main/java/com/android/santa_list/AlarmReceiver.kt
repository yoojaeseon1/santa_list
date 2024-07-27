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
import androidx.core.os.bundleOf
import androidx.fragment.app.setFragmentResult

class AlarmReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context?, intent: Intent?) {

        if (intent != null) {
            val activityIntent = Intent(context, ContactActivity::class.java).apply {
                flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            }
            val pendingIntent = PendingIntent.getActivity(
                context, 0, activityIntent, PendingIntent.FLAG_UPDATE_CURRENT or
                        PendingIntent.FLAG_IMMUTABLE
            )

            val manager =
                context?.getSystemService(NOTIFICATION_SERVICE) as NotificationManager
            val builder: NotificationCompat.Builder
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                val channelId = "one-channel"
                val channelName = "산타데이 알림"
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
                setContentTitle("산타로서 선물할 때가 됐어요!")
                setContentText("소중한 사람에게 마음을 전하러 가볼까요?")
                setContentIntent(pendingIntent)
                setAutoCancel(true)
            }
            manager.notify(1, builder.build())
            Toast.makeText(context, "산타로서 선물할 때가 됐어요!", Toast.LENGTH_SHORT).show()

        }
    }


}
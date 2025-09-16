import android.Manifest
import android.annotation.SuppressLint
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.content.pm.PackageManager
import android.util.Log
import androidx.compose.ui.platform.LocalContext
import androidx.core.app.ActivityCompat
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat


class SpeedAlertManager(private val context: Context) {

    private val NOTIFICATION_CHANNEL_ID = "speed_alert_channel"
    private val NOTIFICATION_ID = 101
    private val SPEED_LIMIT_KMH = 60// Example speed limit

    init {
        createNotificationChannel()

    }

    private fun createNotificationChannel() {
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            val name = "Speed Alerts"
            val descriptionText = "Notifications for exceeding speed limits"
            val importance = NotificationManager.IMPORTANCE_HIGH
            val channel = NotificationChannel(NOTIFICATION_CHANNEL_ID, name, importance).apply {
                description = descriptionText
            }
            val notificationManager: NotificationManager =
                context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(channel)
        }
    }

    private fun checkSpeedAndNotify(currentSpeedKmh: Float) {
        if (currentSpeedKmh > SPEED_LIMIT_KMH) {
            showSpeedAlertNotification(currentSpeedKmh)
        } else {
            // Optionally, dismiss the notification if no longer speeding
            NotificationManagerCompat.from(context).cancel(NOTIFICATION_ID)
        }
    }


    private fun showSpeedAlertNotification(currentSpeedKmh: Float) {
        val builder = NotificationCompat.Builder(context, NOTIFICATION_CHANNEL_ID)
            .setSmallIcon(android.R.drawable.ic_dialog_alert) // Replace with your icon
            .setContentTitle("Speeding Alert!")
            .setContentText("Current speed: ${String.format("%.1f", currentSpeedKmh)} km/h. Limit: $SPEED_LIMIT_KMH km/h.")
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setOngoing(true) // Makes the notification persistent

        with(NotificationManagerCompat.from(context)) {
            if (ActivityCompat.checkSelfPermission(
                    context,
                    Manifest.permission.POST_NOTIFICATIONS
                ) != PackageManager.PERMISSION_GRANTED
            ) {
            return
            }
            notify(NOTIFICATION_ID, builder.build())
        }
    }

    fun startSpeedTracking(currentSpeed: Float ) {

        Log.e("NotificationError",currentSpeed.toString()+" check string")
        checkSpeedAndNotify(currentSpeed)
    }


}
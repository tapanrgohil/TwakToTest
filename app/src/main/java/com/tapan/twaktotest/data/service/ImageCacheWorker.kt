package com.tapan.twaktotest.data.service

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.NotificationManager.IMPORTANCE_HIGH
import android.content.Context
import android.content.Context.NOTIFICATION_SERVICE
import android.os.Build.VERSION.SDK_INT
import android.os.Build.VERSION_CODES.O
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationCompat.PRIORITY_DEFAULT
import androidx.core.app.NotificationCompat.PRIORITY_MAX
import androidx.hilt.work.HiltWorker
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.tapan.twaktotest.R
import com.tapan.twaktotest.data.user.UserRepository
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject
import kotlin.random.Random

@HiltWorker
class ImageCacheWorker @AssistedInject constructor(
    @Assisted private val context: Context,
    @Assisted workerParams: WorkerParameters,
    private val userRepository: UserRepository
) : CoroutineWorker(context, workerParams) {

    val notificationId = 7894
    val NOTIFICATION_NAME = "GITHUB_USERS"
    val NOTIFICATION_CHANNEL = "GITHUB_USERS_IMAGE_CHACHE"
    val notificationManager =
        context.getSystemService(NOTIFICATION_SERVICE) as NotificationManager


    override suspend fun doWork(): Result {
        sendNotification(notificationId, false)
        return withContext(Dispatchers.IO) {
            userRepository.cacheImages()
            sendNotification(notificationId, isComplete = true)
            notificationManager.cancel(notificationId)
            Result.success()

        }

    }


    private fun sendNotification(id: Int, isComplete: Boolean) {


        val titleNotification = context.getString(R.string.notification_title)
        val subtitleNotification =
            if (!isComplete)
                context.getString(R.string.notification_subtitle)
            else {
                context.getString(R.string.notification_complete)
            }
        val notification = NotificationCompat
            .Builder(context, NOTIFICATION_CHANNEL)
            .setOngoing(true)
            .setContentTitle(titleNotification)
            .setSmallIcon(R.mipmap.ic_launcher)
            .setContentText(subtitleNotification)
            .setSilent(true)

        notification.priority = PRIORITY_DEFAULT

        if (SDK_INT >= O) {
            notification.setChannelId(NOTIFICATION_CHANNEL)
            val channel =
                NotificationChannel(NOTIFICATION_CHANNEL, NOTIFICATION_NAME, IMPORTANCE_HIGH)
            notificationManager.createNotificationChannel(channel)
        }

        notificationManager.notify(id, notification.build())
    }
}
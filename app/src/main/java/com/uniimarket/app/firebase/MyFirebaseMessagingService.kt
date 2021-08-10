package com.uniimarket.app.firebase

import android.annotation.SuppressLint
import android.app.*
import android.content.Context
import android.content.Intent
import android.media.RingtoneManager
import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.core.app.NotificationCompat
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import com.uniimarket.app.Dashboard.view.DashboardActivity
import com.uniimarket.app.MainActivity
import com.uniimarket.app.notification.utils.OreoNotification


class MyFirebaseMessagingService : FirebaseMessagingService() {

    var CHANNEL_ID = ""

    /**
     * Called when message is received.
     *
     * @param remoteMessage Object representing the message received from Firebase Cloud Messaging.
     */
    // [START receive_message]
    @RequiresApi(Build.VERSION_CODES.N)
    override fun onMessageReceived(remoteMessage: RemoteMessage?) {
        // [START_EXCLUDE]
        // There are two types of messages data messages and notification messages. Data messages are handled
        // here in onMessageReceived whether the app is in the foreground or background. Data messages are the type
        // traditionally used with GCM. Notification messages are only received here in onMessageReceived when the app
        // is in the foreground. When the app is in the background an automatically generated notification is displayed.
        // When the user taps on the notification they are returned to the app. Messages containing both notification
        // and data payloads are treated as notification messages. The Firebase console always sends notification
        // messages. For more see: https://firebase.google.com/docs/cloud-messaging/concept-options
        // [END_EXCLUDE]

        // TODO(developer): Handle FCM messages here.
        // Not getting messages here? See why this may be: https://goo.gl/39bRNJ
        Log.d(TAG, "From: ${remoteMessage?.from}")

        // Check if message contains a data payload.
        remoteMessage?.data?.isNotEmpty()?.let {
            Log.d(TAG, "Message data payload: " + remoteMessage.data)

            if (/* Check if data needs to be processed by long running job */ true) {
                // For long-running tasks (10 seconds or more) use WorkManager.
                scheduleJob()
            } else {
                // Handle message within 10 seconds
                handleNow()
            }
        }

        // Check if message contains a notification payload.
        remoteMessage?.notification?.let {
            Log.d(TAG, "Message Notification Body: ${it.body}")
        }

        // Also if you intend on generating your own notifications as a result of a received FCM
        // message, here is where that should be initiated. See sendNotification method below.

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            remoteMessage?.let { sendNotification1(it) }
        } else {
            remoteMessage?.let { sendNotification(it) }
        }


    }
    // [END receive_message]

    // [START on_new_token]
    /**
     * Called if InstanceID token is updated. This may occur if the security of
     * the previous token had been compromised. Note that this is called when the InstanceID token
     * is initially generated so this is where you would retrieve the token.
     */
    override fun onNewToken(token: String?) {
        Log.d(TAG, "Refreshed token: $token")

        // If you want to send messages to this application instance or
        // manage this apps subscriptions on the server side, send the
        // Instance ID token to your app server.
        sendRegistrationToServer(token)
    }
    // [END on_new_token]

    /**
     * Schedule async work using WorkManager.
     */
    private fun scheduleJob() {
        // [START dispatch_job]
//        val work = OneTimeWorkRequest.Builder(MyWorker::class.java).build()
//        WorkManager.getInstance().beginWith(work).enqueue()
        // [END dispatch_job]
    }

    /**
     * Handle time allotted to BroadcastReceivers.
     */
    private fun handleNow() {
        Log.d(TAG, "Short lived task is done.")
    }

    /**
     * Persist token to third-party servers.
     *
     * Modify this method to associate the user's FCM InstanceID token with any server-side account
     * maintained by your application.
     *
     * @param token The new token.
     */
    private fun sendRegistrationToServer(token: String?) {
        // TODO: Implement this method to send token to your app server.
    }

    /**
     * Create and show a simple notification containing the received FCM message.
     *
     * @param messageBody FCM message body received.
     */
    private fun sendNotification(messageBody: String) {
        val intent = Intent(this, MainActivity::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
        val pendingIntent = PendingIntent.getActivity(
            this, 0 /* Request code */, intent,
            PendingIntent.FLAG_ONE_SHOT
        )

        val channelId = getString(com.uniimarket.app.R.string.default_notification_channel_id)
        val defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION)
        val notificationBuilder = NotificationCompat.Builder(this, channelId)
            .setSmallIcon(com.uniimarket.app.R.mipmap.ic_launcher)
            .setContentTitle(getString(com.uniimarket.app.R.string.fcm_message))
            .setContentText(messageBody)
            .setAutoCancel(true)
            .setSound(defaultSoundUri)
            .setContentIntent(pendingIntent)

        val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        // Since android Oreo notification channel is needed.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                channelId,
                "Channel human readable title",
                NotificationManager.IMPORTANCE_DEFAULT
            )
            notificationManager.createNotificationChannel(channel)
        }

        notificationManager.notify(0 /* ID of notification */, notificationBuilder.build())
    }


    fun isAppIsInBackground(context: Context): Boolean {
        var isInBackground = true
        val am = context.getSystemService(Context.ACTIVITY_SERVICE) as ActivityManager
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.KITKAT_WATCH) {
            val runningProcesses = am.runningAppProcesses
            for (processInfo in runningProcesses) {
                if (processInfo.importance == ActivityManager.RunningAppProcessInfo.IMPORTANCE_FOREGROUND) {
                    for (activeProcess in processInfo.pkgList) {
                        if (activeProcess == context.packageName) {
                            isInBackground = false
                        }
                    }
                }
            }
        } else {

            val taskInfo = am.getRunningTasks(1)
            val componentInfo = taskInfo[0].topActivity
            if (componentInfo.packageName == context.packageName) {
                isInBackground = false
            }
        }

        return isInBackground
    }

    @SuppressLint("NewApi")
    private fun sendNotification1(remoteMessage: RemoteMessage) {
        if (!isAppIsInBackground(applicationContext)) {
            //foreground app
            Log.e("remoteMessage", remoteMessage.data.toString())
            Log.e("remoteMessage", remoteMessage.notification!!.body +" \n "+remoteMessage.data["data"])
            Log.e("remoteMessage data", remoteMessage.data.toString())
            val title = remoteMessage.notification!!.title
            val body = remoteMessage.notification!!.body
//            val data = remoteMessage.notification!!.data +"-- "+remoteMessage.data["CHAT"]
            val timestamp = ""
//            getTimeMilliSec(timestamp)
            val resultIntent = Intent(applicationContext, DashboardActivity::class.java)
            resultIntent.putExtra("chat", remoteMessage.data["data"])
            resultIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            val pendingIntent = PendingIntent.getActivity(
                applicationContext,
                0 /* Request code */, resultIntent,
                PendingIntent.FLAG_UPDATE_CURRENT
            )
            val defaultsound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION)

            val oreoNotification = OreoNotification(this)
            val builder = oreoNotification.getOreoNotification(
                title,
                body,
                timestamp,
                pendingIntent,
                defaultsound
            )

            val i = 0
            oreoNotification.manager.notify(i, builder.build())
        } else {
            Log.e("remoteMessage", remoteMessage.data.toString())
            Log.e("remoteMessage", remoteMessage.notification!!.body +" \n "+remoteMessage.data["CHAT"])
            val title = remoteMessage.data["title"]
            val body = remoteMessage.data["body"]
            val timestamp = ""
//            getTimeMilliSec(timestamp)
            val resultIntent = Intent(applicationContext, DashboardActivity::class.java)
            //   resultIntent.putExtra("Notification","Notification");
            resultIntent.putExtra("chat", remoteMessage.data["CHAT"])
            resultIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            val pendingIntent = PendingIntent.getActivity(
                applicationContext,
                0 /* Request code */, resultIntent,
                PendingIntent.FLAG_CANCEL_CURRENT
            )
            val defaultsound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION)

            val oreoNotification = OreoNotification(this)
            val builder = oreoNotification.getOreoNotification(
                title,
                body,
                timestamp,
                pendingIntent,
                defaultsound
            )

            val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            val i = 0
            oreoNotification.manager.notify(i, builder.build())
        }

    }

    @RequiresApi(Build.VERSION_CODES.N)
    @SuppressLint("LongLogTag")
    private fun sendNotification(remoteMessage: RemoteMessage) {
        if (!isAppIsInBackground(applicationContext)) {
            //foreground app
            Log.e("remoteMessage foreground", remoteMessage.data.toString())
            Log.e("remoteMessage", remoteMessage.notification!!.body +" \n "+remoteMessage.data["CHAT"])
            val title = remoteMessage.notification!!.title
            val body = remoteMessage.notification!!.body
            val timeStamp = ""
            val resultIntent = Intent(applicationContext, DashboardActivity::class.java)
            resultIntent.putExtra("chat", "CHAT")
            resultIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            val pendingIntent = PendingIntent.getActivity(
                applicationContext,
                0 /* Request code */, resultIntent,
                PendingIntent.FLAG_UPDATE_CURRENT
            )
            val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

            val notificationBuilder = NotificationCompat.Builder(applicationContext, CHANNEL_ID)
            notificationBuilder.setAutoCancel(true)
                .setDefaults(Notification.DEFAULT_ALL)
                .setWhen(System.currentTimeMillis())
                .setSmallIcon(com.uniimarket.app.R.drawable.logo_text)
                .setWhen(System.currentTimeMillis())
//                .setWhen(getTimeMilliSec(timeStamp))
                .setBadgeIconType(NotificationCompat.BADGE_ICON_SMALL)
                .setNumber(10)
                .setTicker("Bestmarts")
                .setContentTitle(title)
                .setContentText(body)
                .setContentInfo("Info")
            notificationManager.notify(1, notificationBuilder.build())
        } else {
            Log.e("remoteMessage background", remoteMessage.data.toString())
            val data = remoteMessage.data
            val title = data["title"] as String
            val body = data["body"] as String
            val timeStamp = ""
            val resultIntent = Intent(applicationContext, DashboardActivity::class.java)
            resultIntent.putExtra("chat", "data")
            resultIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            val pendingIntent = PendingIntent.getActivity(
                applicationContext,
                0 /* Request code */, resultIntent,
                PendingIntent.FLAG_CANCEL_CURRENT
            )
            val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

            val notificationBuilder = NotificationCompat.Builder(applicationContext, CHANNEL_ID)
            notificationBuilder.setAutoCancel(true)
                .setDefaults(Notification.DEFAULT_ALL)
                .setWhen(System.currentTimeMillis())
                .setSmallIcon(com.uniimarket.app.R.mipmap.ic_launcher)
                .setWhen(System.currentTimeMillis())
//                .setWhen(getTimeMilliSec(timeStamp))
                .setBadgeIconType(NotificationCompat.BADGE_ICON_SMALL)
                .setNumber(10)
                .setTicker("Bestmarts")
                .setContentTitle(title)
                .setContentText(body)
                .setContentInfo("Info")
            notificationManager.notify(1, notificationBuilder.build())
        }
    }

    companion object {

        private const val TAG = "MyFirebaseMsgService"
    }
}
package com.example.bceats20.service;
//
//import android.util.Log;
//import android.widget.Toast;
//
//import com.google.firebase.messaging.FirebaseMessagingService;
//import com.google.firebase.messaging.RemoteMessage;
//
//import androidx.annotation.NonNull;
//import androidx.work.OneTimeWorkRequest;
//import androidx.work.WorkManager;
//
//public class MyFirebaseMessagingService extends FirebaseMessagingService {
//    private static final String TAG = "MyFirebaseMsgService";
//
//    @Override
//    public void onMessageReceived(RemoteMessage remoteMessage) {
//        // There are two types of messages data messages and notification messages. Data messages
//        // are handled
//        // here in onMessageReceived whether the app is in the foreground or background. Data
//        // messages are the type
//        // traditionally used with GCM. Notification messages are only received here in
//        // onMessageReceived when the app
//        // is in the foreground. When the app is in the background an automatically generated
//        // notification is displayed.
//        // When the user taps on the notification they are returned to the app. Messages
//        // containing both notification
//        // and data payloads are treated as notification messages. The Firebase console always
//        // sends notification
//
//
//        // TODO(developer): Handle FCM messages here.
//        // Not getting messages here? See why this may be: https://goo.gl/39bRNJ
//        Log.d(TAG, "From: " + remoteMessage.getFrom());
//
//        // Check if message contains a data payload.
//        if (remoteMessage.getData().size() > 0) {
//            Log.d(TAG, "Message data payload: " + remoteMessage.getData());
//
//            if (/* Check if data needs to be processed by long running job */ true) {
//                // For long-running tasks (10 seconds or more) use WorkManager.
//                scheduleJob();
//            } else {
//                // Handle message within 10 seconds
//                handleNow();
//            }
//
//        }
//
//        // Check if message contains a notification payload.
//        if (remoteMessage.getNotification() != null) {
//            Log.d(TAG, "Message Notification Body: " + remoteMessage.getNotification().getBody());
//        }
//
//        // Also if you intend on generating your own notifications as a result of a received FCM
//        // message, here is where that should be initiated. See sendNotification method below.
//    }
//
//    @Override
//    public void onDeletedMessages() {
//        super.onDeletedMessages();
//    }
//
//
//
//
//
//
//    //fires whenever a new token is generated
//    @Override
//    public void onNewToken(@NonNull String token) {
//        super.onNewToken(token);
//        Log.d(TAG, "Refreshed token: " + token);
//
//        // If you want to send messages to this application instance or
//        // manage this apps subscriptions on the server side, send the
//        // Instance ID token to your app server.
//        sendRegistrationToServer(token);
//    }
//
//
//
//    private void scheduleJob() {
//        // [START dispatch_job]
//        OneTimeWorkRequest work = new OneTimeWorkRequest.Builder(MyWorker.class)
//                .build();
//        WorkManager.getInstance().beginWith(work).enqueue();
//        // [END dispatch_job]
//    }
//
//    private void handleNow() {
//        Log.d(TAG, "Short lived task is done.");
//    }
//
//
//    private void sendRegistrationToServer(String token) {
//        Toast.makeText(this, "SENDING REFRESH TOKEN TO SERVER" + token, Toast.LENGTH_SHORT).show();
//    }
////
////    private void sendNotification(String messageBody) {
////        Intent intent = new Intent(this, Application.class);
////        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
////        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0 /* Request code */, intent,
////                PendingIntent.FLAG_ONE_SHOT);
////
////        String channelId = getString(R.string.default_notification_channel_id);
////        Uri defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
////        NotificationCompat.Builder notificationBuilder =
////                new NotificationCompat.Builder(this, channelId)
////                        .setSmallIcon(R.drawable.ic_stat_ic_notification)
////                        .setContentTitle(getString(R.string.fcm_message))
////                        .setContentText(messageBody)
////                        .setAutoCancel(true)
////                        .setSound(defaultSoundUri)
////                        .setContentIntent(pendingIntent);
////
////        NotificationManager notificationManager =
////                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
////
////        // Since android Oreo notification channel is needed.
////        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
////            NotificationChannel channel = new NotificationChannel(channelId,
////                    "Channel human readable title",
////                    NotificationManager.IMPORTANCE_DEFAULT);
////            notificationManager.createNotificationChannel(channel);
////        }
////
////        notificationManager.notify(0 /* ID of notification */, notificationBuilder.build());
////    }
//
//
//}
//

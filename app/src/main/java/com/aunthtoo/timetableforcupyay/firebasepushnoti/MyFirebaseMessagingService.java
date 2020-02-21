package com.aunthtoo.timetableforcupyay.firebasepushnoti;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.support.v4.app.NotificationCompat;
import android.util.Log;
import android.widget.Toast;

import com.aunthtoo.timetableforcupyay.MainPage;
import com.aunthtoo.timetableforcupyay.R;
import com.aunthtoo.timetableforcupyay.dbhandler.TimeTableDBHandler;
import com.aunthtoo.timetableforcupyay.model.Student;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import me.myatminsoe.mdetect.MDetect;

/**
 * Created by Aunt Htoo on 12/10/2017.
 */

public class MyFirebaseMessagingService extends FirebaseMessagingService {

    private static final String TAG = "FCM Service";

    private TimeTableDBHandler dbHandler;

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        MDetect.INSTANCE.init(getApplicationContext());
        dbHandler = new TimeTableDBHandler(getApplicationContext());

        Uri uri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);

        Student student = dbHandler.getStudent();

       /* Log.d(TAG, "From: " + remoteMessage.getFrom());*/
        String body = remoteMessage.getNotification().getBody();

        Toast.makeText(getApplicationContext(), body, Toast.LENGTH_SHORT).show();

        String year = body.split("/")[0].toLowerCase();
        String title = body.split("/")[1];
        String notiBody = body.split("/")[2];


        if (student.getYear().toLowerCase().equals(year)) {

            sendNotification(notiBody, title);

        } else {
            Log.e("Push Noti", "It is not for you");
        }

    }

    private void sendNotification(String messageBody, String title) {
        Intent intent = new Intent(this, MainPage.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0 /* Request code */, intent,
                PendingIntent.FLAG_ONE_SHOT);

        Uri defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this)
                .setSmallIcon(R.mipmap.ic_launcher)/*Notification icon image*/
                .setContentTitle(title)
                .setContentText(messageBody)
                .setAutoCancel(true)
                .setOngoing(false)
                .setSound(defaultSoundUri)
                .setContentIntent(pendingIntent);

        NotificationManager notificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        notificationManager.notify(0 /* ID of notification */, notificationBuilder.build());
    }
}

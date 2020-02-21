package com.aunthtoo.timetableforcupyay.alarm;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.media.RingtoneManager;
import android.net.Uri;
import android.support.v4.app.NotificationCompat;

import com.aunthtoo.timetableforcupyay.MainPage;
import com.aunthtoo.timetableforcupyay.R;
import com.aunthtoo.timetableforcupyay.prefmanager.PrefManager;
import com.aunthtoo.timetableforcupyay.services.MyService;

import me.myatminsoe.mdetect.MDetect;

/**
 * Created by Aunt Htoo on 11/30/2017.
 */

public class AlarmReceiver extends BroadcastReceiver {

    Uri notiSound;
    private PrefManager prefManager;


    @Override
    public void onReceive(Context context, Intent intent) {

        prefManager = new PrefManager(context);

        if (intent.getStringExtra("alarm") != null && intent.getStringExtra("alarm").equals("setNoti")) {

            MDetect.INSTANCE.init(context);

            int id = Integer.parseInt(intent.getStringExtra("id"));
            //for content
            String bodyContent = intent.getStringExtra("bodyContent") + " " + context.getResources().getString(R.string.thistimegotit);

            NotificationManager alarmNoti =
                    (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);

            NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(context)
                    .setSmallIcon(R.mipmap.ic_launcher)
                    //example for large icon

                    .setContentTitle(MDetect.INSTANCE.getText(context.getString(R.string.mingalarpar)))
                    .setContentText(MDetect.INSTANCE.getText(bodyContent))
                    .setOngoing(false)
                    .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                    .setAutoCancel(true);

            Intent i = new Intent(context, MainPage.class);
            i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            PendingIntent pendingIntent =
                    PendingIntent.getActivity(
                            context,
                            0,
                            i,
                            PendingIntent.FLAG_ONE_SHOT
                    );

            // example for blinking LED
            // mBuilder.setLights(Color.RED, 3000, 3000);
            //for vibrate
            mBuilder.setVibrate(new long[]{1000, 1000, 1000, 1000, 1000});

            mBuilder.setSound(Uri.parse("android.resource://"
                    + context.getPackageName() + "/" + R.raw.alarm));
            mBuilder.setContentIntent(pendingIntent);


            alarmNoti.notify(0, mBuilder.build());

            setAlarm(intent.getStringExtra("id"), false);

        }
    }

    //for alarm
    public void setAlarm(String time, boolean value) {

        switch (time) {
            case "1":

                prefManager.setAlarmtime1(value);

                break;

            case "2":
                prefManager.setAlarmtime2(value);

                break;

            case "3":

                prefManager.setAlarmtime3(value);

                break;

            case "4":

                prefManager.setAlarmtime4(value);

                break;

            case "5":
                prefManager.setAlarmtime5(value);

                break;

            case "6":

                prefManager.setAlarmtime6(value);

                break;

            case "7":

                prefManager.setAlarmtime7(value);

                break;
        }
    }
}

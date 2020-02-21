package com.aunthtoo.timetableforcupyay.receive;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.Toast;

import com.aunthtoo.timetableforcupyay.services.MyService;

/**
 * Created by Aunt Htoo on 12/2/2017.
 */

public class ServiceReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {

        if (intent.getAction().equalsIgnoreCase(Intent.ACTION_BOOT_COMPLETED)) {
            //Toast.makeText(context, "Boot completed", Toast.LENGTH_SHORT).show();
            Log.d("ServiceReceiver", "Service is starting");

            Intent startServiceIntent = new Intent(context, MyService.class);
            context.startService(startServiceIntent);
        }


    }
}

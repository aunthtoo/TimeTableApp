package com.aunthtoo.timetableforcupyay;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import me.myatminsoe.mdetect.MDetect;

public class Profile extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        MDetect.INSTANCE.init(this);
    }
}

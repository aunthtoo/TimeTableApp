package com.aunthtoo.timetableforcupyay.splashscreen;

import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

import com.aunthtoo.timetableforcupyay.LogIn;
import com.aunthtoo.timetableforcupyay.MainPage;
import com.aunthtoo.timetableforcupyay.R;
import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;

public class SplashScreen extends AppCompatActivity {

    private ImageView logo;
    private TextView title, uniname;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);

        logo = findViewById(R.id.splogo);

        title = findViewById(R.id.sptitle);
        uniname = findViewById(R.id.uniname);

        Handler handler = new Handler();

        handler.postDelayed(new Runnable() {
            @Override
            public void run() {

                startActivity(new Intent(SplashScreen.this, LogIn.class));
                finish();
            }
        }, 3000);


    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        if (hasFocus) {
            YoYo.with(Techniques.DropOut)
                    .duration(2500)
                    .playOn(title);

            YoYo.with(Techniques.BounceIn)
                    .duration(2500)
                    .playOn(logo);

            YoYo.with(Techniques.SlideInUp)
                    .duration(2500)
                    .playOn(uniname);
        }
    }

    @Override
    public void onBackPressed() {

    }
}

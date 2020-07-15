package com.cookiefeeder.cookiepop;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;

import java.util.Timer;
import java.util.TimerTask;

public class LogoActivity extends AppCompatActivity
{
    private int timerCount;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_logo);
        final Timer mTimer = new Timer();
        TimerTask mTimerTask = new TimerTask()
        {
            @Override
            public void run()
            {
                mTimer.cancel();
                Intent intent = new Intent(getApplication(), MainActivity.class);
                startActivity(intent);
                finish();
            }
        };
        mTimer.schedule(mTimerTask, 2500);
    }
}

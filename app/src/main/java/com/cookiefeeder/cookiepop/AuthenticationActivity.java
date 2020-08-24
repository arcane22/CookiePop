package com.cookiefeeder.cookiepop;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Timer;
import java.util.TimerTask;

public class AuthenticationActivity extends AppCompatActivity implements View.OnClickListener
{
    /** **/
    public static final int REGISTRATION_ACTIVITY = 0;
    public static final int FINDUSERINFO_ACTIVITY = 1;

    private TextView tv_authentication_time;
    private EditText et_authentication_code;
    private Button btn_authentication;

    private String serverAuthCode;
    private int timer = 301;

    /** **/
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_authentication);
        initData();
    }

    @Override
    protected void onDestroy()
    {
        super.onDestroy();

    }

    private void initData()
    {
        tv_authentication_time = findViewById(R.id.tv_show_timer);
        et_authentication_code = findViewById(R.id.et_authentication_code);
        btn_authentication = findViewById(R.id.btn_authentication);

        btn_authentication.setOnClickListener(this);
        serverAuthCode = getIntent().getStringExtra("authCode");

        final Timer mTimer = new Timer();
        TimerTask mTimerTask = new TimerTask()
        {
            @Override
            public void run()
            {
                if(timer > 0)
                {
                    timer = timer - 1;
                    int minute = timer / 60;
                    int second = timer - 60 * minute;
                    tv_authentication_time.setText("유효시간 : " + minute + "분" + " " + second + "초");
                }
            }
        };
        mTimer.schedule(mTimerTask, 1000, 1000);
    }

    @Override
    public void onBackPressed()
    {
        super.onBackPressed();
        finish();
    }

    @Override
    public void onClick(View v)
    {
        switch(v.getId())
        {
            case R.id.btn_authentication:
                authentication(v, getIntent().getIntExtra("ActivityNum", 0));
                break;
        }
    }

    private void authentication(View v, int ActivityNum)
    {
        String inputCode = et_authentication_code.getText().toString();
        if(inputCode.equals(serverAuthCode) && timer > 0)
        {
            Toast.makeText(getApplicationContext(), "이메일 인증이 완료되었습니다.", Toast.LENGTH_SHORT).show();
            Intent intent = null;
            switch(ActivityNum)
            {
                case REGISTRATION_ACTIVITY:
                    intent = new Intent(getApplication(), RegistrationActivity.class);
                    break;
                case FINDUSERINFO_ACTIVITY:
                    intent = new Intent(getApplication(), FindUserInfoActivity.class);
                    break;
            }
            intent.putExtra("authResult", true);
            intent.putExtra("id", getIntent().getStringExtra("id"));
            startActivity(intent);
            finish();
        }
        else if(timer <= 0)
        {
            Toast.makeText(getApplicationContext(), "유효하지 않는 코드입니다.", Toast.LENGTH_SHORT).show();
        }
        else
        {
            Toast.makeText(getApplicationContext(), "잘못된 코드입니다.", Toast.LENGTH_SHORT).show();
        }
    }
}
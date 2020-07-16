package com.cookiefeeder.cookiepop;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.TextView;

public class LoginActivity extends AppCompatActivity
{
    private Button loginButton;
    private CheckBox cb_keepLogin;
    private TextView tv_findIDAndPW, tv_registration;

    private class OnClickListener implements View.OnClickListener
    {
        @Override
        public void onClick(View v)
        {
            Intent intent = null;
            switch(v.getId())
            {
                case R.id.button_login:
                    intent = new Intent(getApplication(), MainActivity.class);
                    break;
                case R.id.cb_keep_login:
                    break;
                case R.id.tv_find_id_and_pw:
                    intent = new Intent(getApplication(), FindIdAndPwActivity.class);
                    break;
                case R.id.tv_registration:
                    intent = new Intent(getApplication(), RegistrationActivity.class);
                    break;
            }
            if(intent != null)
            {
                if(v.getId() == R.id.tv_find_id_and_pw || v.getId() == R.id.tv_registration)
                    startActivity(intent);
                else
                {
                    startActivity(intent);
                    finish();
                }
            }
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        initData();
    }

    private void initData()
    {
        OnClickListener onClickListener = new OnClickListener();

        loginButton = findViewById(R.id.button_login);
        cb_keepLogin = findViewById(R.id.cb_keep_login);
        tv_findIDAndPW = findViewById(R.id.tv_find_id_and_pw);
        tv_registration = findViewById(R.id.tv_registration);

        loginButton.setOnClickListener(onClickListener);
        cb_keepLogin.setOnClickListener(onClickListener);
        tv_findIDAndPW.setOnClickListener(onClickListener);
        tv_registration.setOnClickListener(onClickListener);
    }
}

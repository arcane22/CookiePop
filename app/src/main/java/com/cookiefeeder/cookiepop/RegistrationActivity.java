package com.cookiefeeder.cookiepop;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import io.socket.client.IO;
import io.socket.client.Socket;

public class RegistrationActivity extends AppCompatActivity implements View.OnClickListener
{
    private EditText et_id, et_password, et_password_confirm, et_name;
    private Button btn_sendVerificationCode, btn_registration;
    private Socket mSocket;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);
        initData();
    }

    private void initData()
    {
        et_id = findViewById(R.id.et_registration_id);
        et_password = findViewById(R.id.et_registration_password);
        et_password_confirm = findViewById(R.id.et_registration_pwconfirm);
        et_name = findViewById(R.id.et_registration_name);

        btn_sendVerificationCode = findViewById(R.id.btn_sendVerificationCode);
        btn_registration = findViewById(R.id.btn_registration);
        btn_sendVerificationCode.setOnClickListener(this);
        btn_registration.setOnClickListener(this);
    }

    @Override
    public void onClick(View v)
    {
        switch(v.getId())
        {
            case R.id.btn_sendVerificationCode:
                break;
            case R.id.btn_registration:
                signUp(v);
                break;
        }
    }

    private void signUp(View v)
    {

    }
}

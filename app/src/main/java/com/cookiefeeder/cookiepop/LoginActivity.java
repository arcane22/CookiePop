package com.cookiefeeder.cookiepop;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;

import com.github.nkzawa.emitter.Emitter;
import com.github.nkzawa.engineio.client.transports.WebSocket;
import com.github.nkzawa.socketio.client.IO;
import com.github.nkzawa.socketio.client.Socket;

import java.net.URISyntaxException;


public class LoginActivity extends AppCompatActivity implements View.OnClickListener
{
    // implementation 'com.github.nkzawa:socket.io-client:0.6.0'
    // implementation ('io.socket:socket.io-client:1.0.0') {
    //        exclude group: 'org.json', module: 'json'
    //    }
    private TextView tv_findIDAndPW, tv_registration;
    private EditText et_login_email, et_login_password;
    private Button loginButton;
    private CheckBox cb_keepLogin;
    private Socket mSocket;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        initData();
        initSocket();
    }

    private void initData()
    {
        loginButton = findViewById(R.id.button_login);
        cb_keepLogin = findViewById(R.id.cb_keep_login);
        tv_findIDAndPW = findViewById(R.id.tv_find_id_and_pw);
        tv_registration = findViewById(R.id.tv_registration);

        loginButton.setOnClickListener(this);
        cb_keepLogin.setOnClickListener(this);
        tv_findIDAndPW.setOnClickListener(this);
        tv_registration.setOnClickListener(this);
    }

    private void initSocket()
    {
        try
        {
            IO.Options options = new IO.Options();
            options.transports = new String[] { WebSocket.NAME };
            mSocket = IO.socket("http://125.130.248.10:3001", options);
            mSocket.on(Socket.EVENT_CONNECT, onConnect);
            mSocket.on(Socket.EVENT_CONNECT_ERROR, onConnectError);
            mSocket.connect();
        }
        catch(URISyntaxException e)
        {
            e.printStackTrace();
        }
    }
    private Emitter.Listener onConnect = new Emitter.Listener()
    {
        @Override
        public void call(Object... args)
        {
            Log.d("socket", "connected");
        }
    };
    private Emitter.Listener onConnectError = new Emitter.Listener()
    {
        @Override
        public void call(Object... args)
        {
            Log.d("socket", "connection error");
        }
    };

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
                intent = new Intent(getApplication(), FindUserInfoActivity.class);
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

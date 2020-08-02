package com.cookiefeeder.cookiepop;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;


import com.google.android.material.snackbar.Snackbar;

import org.json.JSONException;
import org.json.JSONObject;

import java.net.URI;
import java.net.URISyntaxException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import io.socket.client.IO;
import io.socket.client.Socket;
import io.socket.emitter.Emitter;



public class LoginActivity extends AppCompatActivity implements View.OnClickListener
{
    private TextView tv_findIDAndPW, tv_registration;
    private EditText et_login_email, et_login_password;
    private Button loginButton;
    private CheckBox cb_keepLogin;

    private Socket mSocket;
    private URI uri;

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
        tv_findIDAndPW = findViewById(R.id.tv_find_id_and_pw);
        tv_registration = findViewById(R.id.tv_registration);
        et_login_email = findViewById(R.id.et_login_email);
        et_login_password = findViewById(R.id.et_login_password);
        loginButton = findViewById(R.id.button_login);
        cb_keepLogin = findViewById(R.id.cb_keep_login);

        loginButton.setOnClickListener(this);
        cb_keepLogin.setOnClickListener(this);
        tv_findIDAndPW.setOnClickListener(this);
        tv_registration.setOnClickListener(this);
    }

    private void initSocket()
    {
        try
        {
            uri = new URI("http://59.11.215.32:3001");
            mSocket = IO.socket(uri);
            mSocket.on(Socket.EVENT_CONNECT, onConnect);
            mSocket.on(Socket.EVENT_CONNECT_ERROR, onConnectError);
            mSocket.on(Socket.EVENT_CONNECT_TIMEOUT, onConnectError);
            mSocket.on(Socket.EVENT_DISCONNECT, onDisconnect);
            mSocket.on("signInResult", onSignIn);
        }
        catch(URISyntaxException e)
        {
            e.printStackTrace();
            Log.d("socket_log", "Server Connection Error");
        }
        mSocket.connect();
    }

    // Socket Event Listener
    private Emitter.Listener onConnect = new Emitter.Listener()
    {
        @Override
        public void call(Object... args)
        {
            runOnUiThread(new Runnable()
            {
                @Override
                public void run()
                {
                    Toast.makeText(getApplicationContext(), "Server Connected", Toast.LENGTH_SHORT).show();
                    Log.d("socket_log", "Connect");
                }
            });
        }
    };
    private Emitter.Listener onConnectError = new Emitter.Listener()
    {
        @Override
        public void call(Object... args)
        {
            runOnUiThread(new Runnable()
            {
                @Override
                public void run()
                {
                    Toast.makeText(getApplicationContext(), "Server Connection Error", Toast.LENGTH_SHORT).show();
                    Log.d("socket_log", "Connection Error");
                }
            });
        }
    };
    private Emitter.Listener onDisconnect = new Emitter.Listener()
    {
        @Override
        public void call(Object... args)
        {
            Log.d("socket_log", "disconnect");
        }
    };
    private Emitter.Listener onSignIn = new Emitter.Listener()
    {
        @Override
        public void call(final Object... args)
        {
            if((Boolean)args[0])
            {
                Intent intent = new Intent(getApplication(), MainActivity.class);
                startActivity(intent);
                finish();
            }
            else
            {
                runOnUiThread(new Runnable()
                {
                    @Override
                    public void run()
                    {
                        Toast.makeText(getApplicationContext(), "로그인 정보가 잘못되었거나,\n 존재하지 않는 계정입니다.", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        }
    };

    // Login Button Listener
    @Override
    public void onClick(View v)
    {
        Intent intent = null;
        switch(v.getId())
        {
            case R.id.button_login:
                signIn(v);
                break;
            case R.id.cb_keep_login:
                keepLogin();
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

    public void signIn(View v)
    {
        String id = et_login_email.getText().toString();
        String pw = et_login_password.getText().toString();
        String pw_hash = new Crypto().hashing(pw, "sha256");
        if(id.equals(""))
        {
            Snackbar snackbar = Snackbar.make(v, "아이디를 입력해주세요.", Snackbar.LENGTH_SHORT);
            snackbar.setBackgroundTint(Color.parseColor("#AED581"));
            snackbar.show();
            //Toast.makeText(this, "아이디를 입력해주세요.", Toast.LENGTH_SHORT).show();
        }
        else if(pw.equals(""))
        {
            Snackbar snackbar = Snackbar.make(v, "비밀번호를 입력해주세요.", Snackbar.LENGTH_SHORT);
            snackbar.setBackgroundTint(Color.parseColor("#AED581"));
            snackbar.show();
            //Toast.makeText(this, "비밀번호를 입력해주세요.", Toast.LENGTH_SHORT).show();
        }
        else
        {
            Snackbar snackbar = Snackbar.make(v, "로그인 시도중...", Snackbar.LENGTH_SHORT);
            snackbar.setBackgroundTint(Color.parseColor("#AED581"));
            snackbar.show();
            JSONObject jsonObject = new JSONObject();
            try
            {
                jsonObject.put("userID", id);
                jsonObject.put("userPW", pw_hash);
                mSocket.emit("signIn", jsonObject);
            }
            catch (JSONException e)
            {
                e.printStackTrace();
            }
        }
    }

    public void keepLogin()
    {

    }
}

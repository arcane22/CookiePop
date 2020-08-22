package com.cookiefeeder.cookiepop;

import androidx.appcompat.app.AppCompatActivity;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.graphics.Color;
import android.os.Bundle;
import android.os.IBinder;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.snackbar.Snackbar;

import org.json.JSONException;
import org.json.JSONObject;


public class LoginActivity extends AppCompatActivity implements View.OnClickListener
{
    /** Declaration of Login Activity class fields **/
    private final int SIGN_IN_SUCCESS = 0;
    private final int SIGN_IN_WRONG = 1;
    private final int SIGN_IN_NOT_EXIST = 2;
    private final int SIGN_IN_FAIL = 3;

    private TextView tv_findIDAndPW, tv_registration;
    private EditText et_login_email, et_login_password;
    private Button loginButton;
    private CheckBox cb_keepLogin;

    private NetworkService networkService;
    private boolean onNetworkServiceBound;

    /* bind service connection */
    private ServiceConnection mConnection = new ServiceConnection()
    {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service)
        {
            NetworkService.NetworkServiceBinder binder = (NetworkService.NetworkServiceBinder) service;
            networkService = binder.getService();
            onNetworkServiceBound = true;
        }

        @Override
        public void onServiceDisconnected(ComponentName name)
        {
            networkService = null;
            onNetworkServiceBound = false;
        }
    };

    /* Broadcast Receiver (Activity <- Service)*/
    private BroadcastReceiver broadcastReceiver = new BroadcastReceiver()
    {
        @Override
        public void onReceive(Context context, Intent intent)
        {
            int result = intent.getIntExtra("result", SIGN_IN_FAIL);
            switch(result)
            {
                case SIGN_IN_SUCCESS:
                    Toast.makeText(getApplicationContext(), "로그인에 성공하였습니다.", Toast.LENGTH_SHORT).show();
                    Intent mIntent = new Intent(getApplication(), MainActivity.class);
                    startActivity(mIntent);
                    finish();
                    break;
                case SIGN_IN_WRONG:
                    Toast.makeText(getApplicationContext(), "로그인정보가 잘못되었습니다.", Toast.LENGTH_SHORT).show();
                    break;
                case SIGN_IN_NOT_EXIST:
                    Toast.makeText(getApplicationContext(), "존재하지 않는 계정입니다.", Toast.LENGTH_SHORT).show();
                    break;
                case SIGN_IN_FAIL:
                    Toast.makeText(getApplicationContext(), "로그인에 실패하였습니다.", Toast.LENGTH_SHORT).show();
                    break;
            }
        }
    };

    /** Override method **/
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        initData();
    }

    @Override
    protected void onStart()
    {
        super.onStart();
        Intent intent = new Intent(this, NetworkService.class);
        bindService(intent, mConnection, Context.BIND_AUTO_CREATE);
    }

    @Override
    protected void onStop()
    {
        super.onStop();
        unbindService(mConnection);
    }

    @Override
    protected void onDestroy()
    {
        super.onDestroy();
        LocalBroadcastManager.getInstance(this).unregisterReceiver(broadcastReceiver);
    }


    /** Initialize login activity class fields **/
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

        LocalBroadcastManager.getInstance(this).registerReceiver(broadcastReceiver, new IntentFilter("signInResult"));
    }

    /** UI click Listener **/
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
                keepLogin(v);
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

    /** Sign In method (Send Data Activity -> Service) **/
    public void signIn(View v)
    {
        String id = et_login_email.getText().toString();
        String pw = et_login_password.getText().toString();
        String pw_hash = new Crypto().hashing(pw, "sha256");

        Snackbar snackbar = Snackbar.make(v, "", Snackbar.LENGTH_SHORT);
        snackbar.setBackgroundTint(Color.parseColor("#AED581"));

        if(id.equals(""))
        {
            snackbar.setText("아이디를 입력해주세요.");
            snackbar.show();
            //Toast.makeText(this, "아이디를 입력해주세요.", Toast.LENGTH_SHORT).show();
        }
        else if(pw.equals(""))
        {
            snackbar.setText("비밀번호를 입력해주세요.");
            snackbar.show();
            //Toast.makeText(this, "비밀번호를 입력해주세요.", Toast.LENGTH_SHORT).show();
        }
        else
        {
            if(onNetworkServiceBound)
            {
                snackbar.setText("로그인 시도중....");
                snackbar.show();
                JSONObject jsonObject = new JSONObject();
                try
                {
                    jsonObject.put("userID", id);
                    jsonObject.put("userPW", pw_hash);
                }
                catch (JSONException e)
                {
                    e.printStackTrace();
                }
                networkService.signIn(jsonObject);
            }
        }
    }

    public void keepLogin(View v)
    {

    }
}

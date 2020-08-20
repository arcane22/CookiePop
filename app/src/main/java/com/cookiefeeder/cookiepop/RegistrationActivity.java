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
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.material.snackbar.Snackbar;

import org.json.JSONException;
import org.json.JSONObject;


public class RegistrationActivity extends AppCompatActivity implements View.OnClickListener
{
    /** Declaration of Registration Activity class fields **/
    private final int SIGN_UP_SUCCESS = 0;
    private final int SIGN_UP_ALREADY_EXIST = 1;
    private final int SIGN_UP_FAIL = 2;

    private EditText et_id, et_password, et_password_confirm, et_name;
    private Button btn_sendVerificationCode, btn_registration;

    private NetworkService networkService;
    private boolean networkServiceBound;

    /* bind service connection */
    private ServiceConnection mConnection = new ServiceConnection()
    {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service)
        {
            NetworkService.NetworkServiceBinder binder = (NetworkService.NetworkServiceBinder) service;
            networkService = binder.getService();
            networkServiceBound = true;
        }

        @Override
        public void onServiceDisconnected(ComponentName name)
        {
            networkService = null;
            networkServiceBound= false;
        }
    };

    /* Broadcast Receiver (Activity <- Service)*/
    private BroadcastReceiver broadcastReceiver = new BroadcastReceiver()
    {
        @Override
        public void onReceive(Context context, Intent intent)
        {
            int result = intent.getIntExtra("result", SIGN_UP_FAIL);
            Log.d("signUp", "sign up result : " + result);
            switch(result)
            {
                case SIGN_UP_SUCCESS:
                    Toast.makeText(getApplicationContext(), "회원가입에 성공하였습니다.", Toast.LENGTH_SHORT).show();
                    Intent mIntent = new Intent(getApplication(), LoginActivity.class);
                    startActivity(mIntent);
                    finish();
                    break;
                case SIGN_UP_ALREADY_EXIST:
                    Toast.makeText(getApplicationContext(), "이미 존재하는 계정입니다.", Toast.LENGTH_SHORT).show();
                    break;
                case SIGN_UP_FAIL:
                    Toast.makeText(getApplicationContext(), "회원가입에 실패하였습니다.", Toast.LENGTH_SHORT).show();
                    break;
            }
        }
    };


    /** Override Methods **/
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);
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

    /** Initialize class fields **/
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

        LocalBroadcastManager.getInstance(this).registerReceiver(broadcastReceiver, new IntentFilter("signUpResult"));
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

    /** Sign Up method (Send Data Activity -> Service) **/
    private void signUp(View v)
    {
        String id = et_id.getText().toString();
        String pw = et_password.getText().toString();
        String pw_confirm = et_password_confirm.getText().toString();
        String name = et_name.getText().toString();

        Snackbar snackbar = Snackbar.make(v, "", Snackbar.LENGTH_SHORT);
        snackbar.setBackgroundTint(Color.parseColor("#AED581"));

        if(id.equals(""))
        {
            snackbar.setText("아이디를 입력해주세요.");
            snackbar.show();
        }
        else if(id.indexOf("@") == -1 || id.indexOf(".") == -1)
        {
            snackbar.setText("이메일형식으로 적어주세요");
            snackbar.show();
        }
        else if(pw.equals("") || pw_confirm.equals(""))
        {
            snackbar.setText("비밀번호를 입력해주세요.");
            snackbar.show();
        }
        else if(name.equals(""))
        {
            snackbar.setText("이름을 입력해주세요.");
            snackbar.show();
        }
        else if(!pw.equals(pw_confirm))
        {
            snackbar.setText("비밀번호를 확인해주세요.");
            snackbar.show();
        }
        else
        {
            if(networkServiceBound)
            {
                String pw_hash = new Crypto().hashing(pw, "sha256");
                JSONObject jsonObject = new JSONObject();
                try
                {
                    jsonObject.put("userID", id);
                    jsonObject.put("userPW", pw_hash);
                    jsonObject.put("userName", name);
                }
                catch(JSONException e)
                {
                    e.printStackTrace();
                }
                networkService.signUp(jsonObject);
            }
        }
    }
}

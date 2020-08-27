package com.cookiefeeder.cookiepop;

import androidx.appcompat.app.AppCompatActivity;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import android.app.Service;
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
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.snackbar.Snackbar;

import org.json.JSONException;
import org.json.JSONObject;

public class FindUserInfoActivity extends AppCompatActivity implements View.OnClickListener
{
    private EditText et_name, et_birthday, et_id, et_pw, et_pwConfirm;
    private Button btn_findId, btn_resetPassword, btn_findUserInfo_auth;

    private NetworkService networkService;
    private boolean onNetworkServiceBound;
    private boolean isPwAuthenticated;

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
            onNetworkServiceBound= false;
        }
    };

    /* Broadcast Receiver for find Id Result, Authentication code, Reset Password Result */
    private BroadcastReceiver findIdResultReceiver = new BroadcastReceiver()
    {
        @Override
        public void onReceive(Context context, Intent intent)
        {
            String userId = intent.getStringExtra("userId");

            if(userId.equals(""))
                Toast.makeText(getApplicationContext(), "존재하지 않는 계정입니다.", Toast.LENGTH_SHORT).show();
            else
                Toast.makeText(getApplicationContext(), "해당 사용자 아이디입니다.\n" + userId, Toast.LENGTH_LONG).show();
        }
    };
    private BroadcastReceiver authCodeReceiver = new BroadcastReceiver()
    {
        @Override
        public void onReceive(Context context, Intent intent)
        {
            String authCode = intent.getStringExtra("code");
            Intent mIntent = new Intent(getApplication(), AuthenticationActivity.class);

            mIntent.putExtra("authCode", authCode);
            mIntent.putExtra("ActivityNum", AuthenticationActivity.FINDUSERINFO_ACTIVITY);
            mIntent.putExtra("id", et_id.getText().toString());
            startActivity(mIntent);
            finish();
        }
    };
    private BroadcastReceiver resetPwResultReceiver = new BroadcastReceiver()
    {
        @Override
        public void onReceive(Context context, Intent intent)
        {
            boolean result = intent.getBooleanExtra("result", false);
            String toastText = "";

            if(result)
                toastText = "비밀번호를 변경하였습니다.";
            else
                toastText = "비밀번호 재설정에 실패하였습니다.\n(존재하지 않는 아이디 이거나 오류가 발생하였습니다.)";
            Toast.makeText(getApplicationContext(), toastText, Toast.LENGTH_SHORT).show();
            clearResetPWText();
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_find_userinfo);
        initData();
    }

    @Override
    protected void onDestroy()
    {
        super.onDestroy();
        unbindService(mConnection);
        LocalBroadcastManager.getInstance(this).unregisterReceiver(findIdResultReceiver);
        LocalBroadcastManager.getInstance(this).unregisterReceiver(authCodeReceiver);
        LocalBroadcastManager.getInstance(this).unregisterReceiver(resetPwResultReceiver);
    }

    @Override
    public void onBackPressed()
    {
        super.onBackPressed();
        finish();
    }

    private void initData()
    {
        et_name = findViewById(R.id.et_findUserInfo_name);
        et_birthday = findViewById(R.id.et_findUserInfo_birthday);
        et_id = findViewById(R.id.et_findUserInfo_id);
        et_pw = findViewById(R.id.et_findUserInfo_pw);
        et_pwConfirm = findViewById(R.id.et_findUserInfo_pwConfirm);

        btn_findId = findViewById(R.id.btn_findId);
        btn_resetPassword = findViewById(R.id.btn_resetPassword);
        btn_findUserInfo_auth = findViewById(R.id.btn_findUserInfo_auth);
        btn_findId.setOnClickListener(this);
        btn_resetPassword.setOnClickListener(this);
        btn_findUserInfo_auth.setOnClickListener(this);

        Intent intent = new Intent(getApplication(), NetworkService.class);
        bindService(intent, mConnection, Context.BIND_AUTO_CREATE);

        LocalBroadcastManager.getInstance(this).registerReceiver(findIdResultReceiver, new IntentFilter("findId"));
        LocalBroadcastManager.getInstance(this).registerReceiver(authCodeReceiver, new IntentFilter("authCode"));
        LocalBroadcastManager.getInstance(this).registerReceiver(resetPwResultReceiver, new IntentFilter("resetPassword"));

        isPwAuthenticated = getIntent().getBooleanExtra("authResult", false);
        et_id.setText(getIntent().getStringExtra("id"));
        if(isPwAuthenticated) {
            et_id.setEnabled(false);
        }
    }

    @Override
    public void onClick(View v)
    {
        switch(v.getId())
        {
            case R.id.btn_findId:
                findId(v);
                break;
            case R.id.btn_resetPassword:
                resetPassword(v);
                break;
            case R.id.btn_findUserInfo_auth:
                requestAuthCode(v);
                break;
        }
    }

    private void findId(View v)
    {
        String name = et_name.getText().toString();
        String birthday = et_birthday.getText().toString();

        Snackbar snackbar = Snackbar.make(v, "", Snackbar.LENGTH_SHORT);
        snackbar.setBackgroundTint(Color.parseColor("#AED581"));

        if(name.equals(""))
        {
            snackbar.setText("이름을 입력해주세요.");
            snackbar.show();
        }
        else if(birthday.equals(""))
        {
            snackbar.setText("생년월일을 입력해주세요.");
            snackbar.show();
        }
        else
        {
            if(onNetworkServiceBound)
            {
                JSONObject jsonObject = new JSONObject();
                try
                {
                    jsonObject.put("userName", name);
                    jsonObject.put("userBirthday", birthday);
                }
                catch(JSONException e)
                {
                    e.printStackTrace();
                }
                networkService.findIdFromServer(jsonObject);
            }
        }
    }

    private void resetPassword(View v)
    {
        String id = et_id.getText().toString();
        String pw = et_pw.getText().toString();
        String pw_confirm = et_pwConfirm.getText().toString();
        String pw_hash = new Crypto().hashing(pw, "sha256");

        Snackbar snackbar = Snackbar.make(v, "", Snackbar.LENGTH_SHORT);
        snackbar.setBackgroundTint(Color.parseColor("#AED581"));

        if(isPwAuthenticated)
        {
            snackbar.setText("인증 완료된 이메일 주소입니다.");
            snackbar.show();
        }
        else if(id.equals(""))
        {
            snackbar.setText("아이디를 입력해주세요.");
            snackbar.show();
        }
        else if(pw.equals("") || pw_confirm.equals("") || !pw.equals(pw_confirm))
        {
            snackbar.setText("비밀번호를 확인해주세요.");
            snackbar.show();
        }
        else if(!isPwAuthenticated)
        {
            snackbar.setText("이메일 인증을 해주세요.");
            snackbar.show();
        }
        else
        {
            if(onNetworkServiceBound)
            {
                JSONObject jsonObject = new JSONObject();
                try
                {
                    jsonObject.put("userID", id);
                    jsonObject.put("userPW", pw_hash);
                }
                catch(JSONException e)
                {
                    e.printStackTrace();
                }
                networkService.resetPassword(jsonObject);
            }
        }
    }

    private void requestAuthCode(View v)
    {
        String id = et_id.getText().toString();

        Snackbar snackbar = Snackbar.make(v, "", Snackbar.LENGTH_SHORT);
        snackbar.setBackgroundTint(Color.parseColor("#AED581"));

        if(id.equals(""))
        {
            snackbar.setText("아이디를 입력해주세요.");
            snackbar.show();
        }
        else
        {
            if(onNetworkServiceBound)
            {
                JSONObject jsonObject = new JSONObject();
                try
                {
                    jsonObject.put("userID", id);
                }
                catch(JSONException e)
                {
                    e.printStackTrace();
                }
                networkService.requestAuthCode(jsonObject);
            }
        }
    }

    private void clearResetPWText()
    {
        et_id.setText("");
        et_id.setEnabled(true);
        et_pw.setText("");
        et_pwConfirm.setText("");
        isPwAuthenticated = false;
    }
}
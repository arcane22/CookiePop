package com.cookiefeeder.cookiepop;

import android.app.Service;
import android.content.Intent;
import android.net.Network;
import android.os.Binder;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.Messenger;
import android.os.Parcelable;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import org.json.JSONException;
import org.json.JSONObject;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;

import io.socket.client.IO;
import io.socket.client.Socket;
import io.socket.emitter.Emitter;

public class NetworkService extends Service
{
    /** Declaration of NetworkService class fields **/
    private IBinder networkServiceBinder = new NetworkServiceBinder();
    private Socket mSocket;
    private URI uri;
    private Handler handler;
    private User user;


    /** Network Service binder class  **/
    public class NetworkServiceBinder extends Binder
    {
        public NetworkService getService()
        {
            return NetworkService.this;
        }
    }

    /** Network worker thread **/
    public class SocketThread extends Thread
    {
        public SocketThread() {
        }

        @Override
        public void run()
        {
            try
            {
                uri = new URI("http://61.79.202.134:3001");
                mSocket = IO.socket(uri);
                mSocket.on(Socket.EVENT_CONNECT, onConnect);
                mSocket.on(Socket.EVENT_CONNECT_ERROR, onConnectError);
                mSocket.on(Socket.EVENT_CONNECT_TIMEOUT, onConnectError);
                mSocket.on(Socket.EVENT_DISCONNECT, onDisconnect);
                mSocket.on("signInResult", onSignInResult);
                mSocket.on("signUpResult", onSignUpResult);
                mSocket.on("sendAuthCodeToClient", onReceiveAuthCode);
                mSocket.on("findUserIdResult", onFindUserId);
                mSocket.on("resetPasswordResult", onResetPassword);
            } catch (URISyntaxException e)
            {
                e.printStackTrace();
            }
            mSocket.connect();
        }
    }


    /** Constructor **/
    public NetworkService() {
    }

    /** Override Methods **/
    @Override
    public IBinder onBind(Intent intent)
    {
        return networkServiceBinder;
    }
    @Override
    public void onCreate()
    {
        super.onCreate();
        SocketThread socketThread = new SocketThread();
        socketThread.run();
        handler = new Handler();
    }
    @Override
    public int onStartCommand(Intent intent, int flags, int startId)
    {
        super.onStartCommand(intent, flags, startId);
        return START_STICKY;
    }
    @Override
    public void onDestroy()
    {
        super.onDestroy();
    }


    /** Defining to use runOnUiThread In Service **/
    private void runOnUiThread(Runnable runnable)
    {
        handler.post(runnable);
    }

    public User getUser()
    {
        return user;
    }
    public void setUser(User user) { this.user = user; }


    /** Sign in method for Login Activity (Send Data Activity <-> Service) **/
    public void signIn(JSONObject jsonObject)
    {
        if(mSocket.connected())
            mSocket.emit("signIn", jsonObject);
    }
    public void sendSignInResult(int result, JSONObject userInfo)
    {
        Intent intent = new Intent("signInResult");
        intent.putExtra("result", result);
        if(userInfo != null)
            user = new User(userInfo);
        LocalBroadcastManager.getInstance(this).sendBroadcast(intent);
    }


    /** Sign up method for Login Activity (Send Data Activity <-> Service) **/
    public void signUp(JSONObject jsonObject)
    {
        if(mSocket.connected())
            mSocket.emit("signUp", jsonObject);
    }
    public void sendSignUpResult(int result)
    {
        Intent intent = new Intent("signUpResult");
        intent.putExtra("result", result);
        LocalBroadcastManager.getInstance(this).sendBroadcast(intent);
    }

    /** Request Authentication Code to server & re**/
    public void requestAuthCode(JSONObject jsonObject)
    {
        if(mSocket.connected())
            mSocket.emit("authCode", jsonObject);
    }
    public void sendAuthCodeToActivity(String code)
    {
        Intent intent = new Intent("authCode");
        intent.putExtra("code", code);
        LocalBroadcastManager.getInstance(this).sendBroadcast(intent);
    }

    /** Find Id method for FindUser Activity (Send Data Activity <-> Service) **/
    public void findIdFromServer(JSONObject jsonObject)
    {
        if(mSocket.connected())
            mSocket.emit("findId", jsonObject);
    }
    public void sendFindIdResult(String userId)
    {
        Intent intent = new Intent("findId");
        intent.putExtra("userId", userId);
        LocalBroadcastManager.getInstance(this).sendBroadcast(intent);
    }

    /** Reset password method for FindUser Activity (Send Data Activity <-> Service) **/
    public void resetPassword(JSONObject jsonObject)
    {
        if(mSocket.connected())
            mSocket.emit("resetPassword", jsonObject);
    }
    public void sendResetPWResult(boolean result)
    {
        Intent intent = new Intent("resetPassword");
        intent.putExtra("result", result);
        LocalBroadcastManager.getInstance(this).sendBroadcast(intent);
    }

    /** **/
    public void setCookieTime(int cookieNum, String time)
    {
        if(mSocket.connected())
            mSocket.emit("setCookieTime", cookieNum, time, user.getUserID());
    }

    /** **/
    public void resetCookieRoom()
    {
        if(mSocket.connected())
            mSocket.emit("resetCookieRoom", user.getUserID());
    }

    /** **/
    public void executeCookie(int currCookieNum)
    {
        if(mSocket.connected())
            mSocket.emit("executeCookie", currCookieNum, user.getUserID());
    }

    /** socket.io connection event listener **/
    private Emitter.Listener onConnect = new Emitter.Listener()
    {
        @Override
        public void call(Object... args)
        {
            Log.d("socket_log", "connect");
            runOnUiThread(new Runnable()
            {
                @Override
                public void run()
                {
                    Toast.makeText(getApplicationContext(), "서버에 연결되었습니다.", Toast.LENGTH_SHORT).show();
                }
            });
        }
    };
    private Emitter.Listener onConnectError = new Emitter.Listener()
    {
        @Override
        public void call(Object... args)
        {
            Log.d("socket_log", "connection error");
        }
    };
    private Emitter.Listener onDisconnect = new Emitter.Listener()
    {
        @Override
        public void call(Object... args)
        {
            Log.d("socket_log", "disconnect");
            runOnUiThread(new Runnable()
            {
                @Override
                public void run()
                {
                    Toast.makeText(getApplicationContext(), "서버와의 연결이 종료되었습니다.", Toast.LENGTH_SHORT).show();
                }
            });
        }
    };


    /** socket.io server event listener (client <-> server) **/
    private Emitter.Listener onSignInResult = new Emitter.Listener()
    {
        @Override
        public void call(Object... args)
        {
            int result = (int) args[0];
            JSONObject userInfo = null;
            if(args.length == 2)
                userInfo = (JSONObject) args[1];
            sendSignInResult(result, userInfo);
        }
    };
    private Emitter.Listener onSignUpResult = new Emitter.Listener()
    {
        @Override
        public void call(Object... args)
        {
            int result = (int) args[0];
            sendSignUpResult(result);
        }
    };
    private Emitter.Listener onReceiveAuthCode = new Emitter.Listener()
    {
        @Override
        public void call(Object... args)
        {
            String code = (String) args[0];
            sendAuthCodeToActivity(code);
        }
    };
    private Emitter.Listener onFindUserId = new Emitter.Listener()
    {
        @Override
        public void call(Object... args)
        {
            String userId = (String) args[0];
            sendFindIdResult(userId);
        }
    };
    private Emitter.Listener onResetPassword = new Emitter.Listener()
    {
        @Override
        public void call(Object... args)
        {
            boolean result = (boolean) args[0];
            sendResetPWResult(result);
        }
    };
}

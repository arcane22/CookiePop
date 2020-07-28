package com.cookiefeeder.cookiepop;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.github.nkzawa.emitter.Emitter;
import com.github.nkzawa.engineio.client.transports.WebSocket;
import com.github.nkzawa.socketio.client.IO;
import com.github.nkzawa.socketio.client.Socket;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.net.URISyntaxException;


public class MainActivity extends AppCompatActivity
{
    private BottomNavigationView bottomNavigationView;
    private Socket mSocket;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initData();
        initBottomNavView();
    }

    private void initData()
    {
        try {
            IO.Options options = new IO.Options();
            options.transports = new String[] { WebSocket.NAME };
            mSocket = IO.socket("http://125.130.248.10:3001", options);
            mSocket.on(Socket.EVENT_CONNECT, new Emitter.Listener() {
                @Override
                public void call(Object... args) {
                    Log.d("socket", "connection");
                }
            });
        }
        catch(URISyntaxException e) {
            e.printStackTrace();
        }
        ((Button) findViewById(R.id.button_connect)).setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                mSocket.emit("message_from_client", "hi");
            }
        });
    }

    private void initSocket()
    {
    }

    private void initBottomNavView()
    {
        bottomNavigationView = findViewById(R.id.mainBottomNavView);
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener()
        {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item)
            {
                switch(item.getItemId())
                {
                    case R.id.bottom_menu_camera:
                        Toast.makeText(getApplicationContext(), "camera", Toast.LENGTH_SHORT).show();
                        return true;
                    case R.id.bottom_menu_home:
                        Toast.makeText(getApplicationContext(), "home", Toast.LENGTH_SHORT).show();
                        return true;
                    case R.id.bottom_menu_settings:
                        Toast.makeText(getApplicationContext(), "settings", Toast.LENGTH_SHORT).show();
                        return true;
                }
                return false;
            }
        });
    }
}

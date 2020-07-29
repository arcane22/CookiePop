package com.cookiefeeder.cookiepop;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.MenuItem;
import android.widget.Toast;

import com.google.android.material.bottomnavigation.BottomNavigationView;



public class MainActivity extends AppCompatActivity
{
    private BottomNavigationView bottomNavigationView;

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

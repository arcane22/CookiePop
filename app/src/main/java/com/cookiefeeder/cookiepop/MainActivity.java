package com.cookiefeeder.cookiepop;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.net.Network;
import android.os.Bundle;
import android.os.IBinder;
import android.provider.Settings;
import android.util.Log;
import android.view.MenuItem;
import android.widget.Toast;

import com.google.android.material.bottomnavigation.BottomNavigationView;



public class MainActivity extends AppCompatActivity
{
    private FragmentManager fragmentManager;
    private BottomNavigationView bottomNavigationView;
    private NetworkService networkService;
    private boolean onNetworkServiceBound;

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

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initData();
        initBottomNavView();
    }

    @Override
    protected void onDestroy()
    {
        super.onDestroy();
        unbindService(mConnection);
    }

    private void initData()
    {
        fragmentManager = getSupportFragmentManager();
        Intent intent = new Intent(getApplication(), NetworkService.class);
        bindService(intent, mConnection, Context.BIND_AUTO_CREATE);
    }

    private void initBottomNavView()
    {
        HomeFragment homeFragment = new HomeFragment();
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.replace(R.id.bottomMenuFrame, homeFragment);
        transaction.commit();

        bottomNavigationView = findViewById(R.id.mainBottomNavView);
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener()
        {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item)
            {
                FragmentTransaction transaction = fragmentManager.beginTransaction();

                switch(item.getItemId())
                {
                    case R.id.bottom_menu_camera:
                        CameraFragment cameraFragment = new CameraFragment();
                        transaction.replace(R.id.bottomMenuFrame, cameraFragment);
                        transaction.addToBackStack("camera");
                        fragmentManager.popBackStack("settings", FragmentManager.POP_BACK_STACK_INCLUSIVE);
                        fragmentManager.popBackStack("camera", FragmentManager.POP_BACK_STACK_INCLUSIVE);
                        break;
                    case R.id.bottom_menu_home:

                        HomeFragment homeFragment = new HomeFragment();
                        transaction.replace(R.id.bottomMenuFrame, homeFragment);
                        //transaction.addToBackStack("home");
                        //manager.popBackStack("home", FragmentManager.POP_BACK_STACK_INCLUSIVE);
                        fragmentManager.popBackStack("settings", FragmentManager.POP_BACK_STACK_INCLUSIVE);
                        fragmentManager.popBackStack("camera", FragmentManager.POP_BACK_STACK_INCLUSIVE);
                        break;
                    case R.id.bottom_menu_settings:

                        SettingsFragment settingsFragment = new SettingsFragment();
                        transaction.replace(R.id.bottomMenuFrame, settingsFragment);
                        transaction.addToBackStack("settings");

                        fragmentManager.popBackStack("settings", FragmentManager.POP_BACK_STACK_INCLUSIVE);
                        fragmentManager.popBackStack("camera", FragmentManager.POP_BACK_STACK_INCLUSIVE);
                        break;
                }
                transaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
                transaction.commit();
                transaction.isAddToBackStackAllowed();
                return true;
            }
        });
    }

    public User getUserFromService()
    {
        if(onNetworkServiceBound)
            return networkService.getUser();
        return null;
    }


    public void logOut()
    {
        if(onNetworkServiceBound)
        {
            Toast.makeText(getApplicationContext(), "로그아웃 하였습니다.", Toast.LENGTH_SHORT).show();
            networkService.setUser(null);
            Intent intent = new Intent(getApplication(), LoginActivity.class);
            startActivity(intent);
            finish();
        }
    }
}

package com.cookiefeeder.cookiepop;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.widget.Toast;

import com.google.android.material.bottomnavigation.BottomNavigationView;



public class MainActivity extends AppCompatActivity
{
    private BottomNavigationView bottomNavigationView;
    private FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
    private CameraFragment cameraFragment = new CameraFragment();
    private HomeFragment homeFragment = new HomeFragment();
    private SettingsFragment settingsFragment = new SettingsFragment();

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
        transaction.replace(R.id.bottomMenuFrame, homeFragment);
        transaction.commit();
        bottomNavigationView = findViewById(R.id.mainBottomNavView);
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener()
        {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item)
            {
                FragmentManager manager = getSupportFragmentManager();
                FragmentTransaction transaction = manager.beginTransaction();
                switch(item.getItemId())
                {
                    case R.id.bottom_menu_camera:
                        CameraFragment cameraFragment = new CameraFragment();
                        transaction.replace(R.id.bottomMenuFrame, cameraFragment);
                        transaction.addToBackStack("camera");
                        manager.popBackStack("settings", FragmentManager.POP_BACK_STACK_INCLUSIVE);
                        manager.popBackStack("camera", FragmentManager.POP_BACK_STACK_INCLUSIVE);
                        break;
                    case R.id.bottom_menu_home:

                        HomeFragment homeFragment = new HomeFragment();

                        transaction.replace(R.id.bottomMenuFrame, homeFragment);
                        //transaction.addToBackStack("home");

                        //manager.popBackStack("home", FragmentManager.POP_BACK_STACK_INCLUSIVE);
                        manager.popBackStack("settings", FragmentManager.POP_BACK_STACK_INCLUSIVE);
                        manager.popBackStack("camera", FragmentManager.POP_BACK_STACK_INCLUSIVE);
                        break;

                    case R.id.bottom_menu_settings:

                        SettingsFragment settingsFragment = new SettingsFragment();

                        transaction.replace(R.id.bottomMenuFrame, settingsFragment);
                        transaction.addToBackStack("settings");

                        manager.popBackStack("settings", FragmentManager.POP_BACK_STACK_INCLUSIVE);
                        manager.popBackStack("camera", FragmentManager.POP_BACK_STACK_INCLUSIVE);
                        break;
                }
                transaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
                transaction.commit();
                transaction.isAddToBackStackAllowed();
                return true;
            }
        });
    }
}

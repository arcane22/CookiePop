package com.cookiefeeder.cookiepop;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.TextView;

public class SettingsFragment extends Fragment implements View.OnClickListener
{
    private TextView  tv_userName, tv_healthInfo, tv_records, tv_introduction, tv_logOut;
    private Switch switch_topBarNotice, switch_popUpNotice;
    private ImageView userImg;
    private User user;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        View root = inflater.inflate(R.layout.fragment_settings, container, false);
        initData(root);
        return root;

    }

    private void initData(View root)
    {
        tv_userName = root.findViewById(R.id.tv_settings_userName);
        tv_healthInfo = root.findViewById(R.id.tv_settings_healthInfo);
        tv_records = root.findViewById(R.id.tv_settings_records);
        tv_introduction = root.findViewById(R.id.tv_settings_introdution);
        tv_logOut = root.findViewById(R.id.tv_settings_logOut);

        switch_topBarNotice = root.findViewById(R.id.switch_settings_topBarNotice);
        switch_popUpNotice = root.findViewById(R.id.switch_settings_popUpNotice);
        userImg = root.findViewById(R.id.iv_settings_profileImg);

        tv_healthInfo.setOnClickListener(this);
        tv_records.setOnClickListener(this);
        tv_introduction.setOnClickListener(this);
        tv_logOut.setOnClickListener(this);

        switch_topBarNotice.setOnClickListener(this);
        switch_popUpNotice.setOnClickListener(this);

        user = ((MainActivity) getActivity()).getUserFromService();
        if(user != null)
            tv_userName.setText(user.getUserName() + "님 프로필");
    }

    @Override
    public void onClick(View v)
    {
        switch (v.getId())
        {
            case R.id.tv_settings_healthInfo:
                healthInfo();
                break;
            case R.id.tv_settings_records:
                records();
                break;
            case R.id.tv_settings_introdution:
                introduction();
                break;
            case R.id.tv_settings_logOut:
                logOut();
                break;
            case R.id.switch_settings_topBarNotice:
                setTopBarNotice();
                break;
            case R.id.switch_settings_popUpNotice:
                setPopUpNotice();
                break;
        }
    }

    private void healthInfo()
    {

    }

    private void records()
    {

    }

    private void introduction()
    {

    }

    private void logOut()
    {
        ((MainActivity) getActivity()).logOut();
    }

    private void setTopBarNotice()
    {

    }

    private void setPopUpNotice()
    {

    }
}
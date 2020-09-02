package com.cookiefeeder.cookiepop;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class HomeFragment extends Fragment implements View.OnClickListener
{
    private Animation fab_open, fab_close, fab_small, fab_big;
    private Boolean isFabOpen = false;
    private FloatingActionButton mainFab;
    private Button btn_cookie1, btn_cookie2, btn_cookie3, btn_resetCookieRoom;
    private FrameLayout btn_layout;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        super.onCreateView(inflater, container, savedInstanceState);
        View root = (View) inflater.inflate(R.layout.fragment_home, container, false);
        initData(root);
        return root;
    }

    private void initData(View root)
    {
        fab_open = AnimationUtils.loadAnimation(getActivity(), R.anim.fab_open);
        fab_close = AnimationUtils.loadAnimation(getActivity(), R.anim.fab_close);
        fab_small = AnimationUtils.loadAnimation(getActivity(), R.anim.fab_small);
        fab_big = AnimationUtils.loadAnimation(getActivity(), R.anim.fab_big);

        btn_layout = root.findViewById(R.id.btn_layout);
        mainFab = root.findViewById(R.id.fab);
        mainFab.setOnClickListener(this);

        btn_cookie1 = root.findViewById(R.id.btn_cookie1);
        btn_cookie2 = root.findViewById(R.id.btn_cookie2);
        btn_cookie3 = root.findViewById(R.id.btn_cookie3);
        btn_resetCookieRoom = root.findViewById(R.id.btn_resetCookieRoom);

        btn_cookie1.setOnClickListener(this);
        btn_cookie2.setOnClickListener(this);
        btn_cookie3.setOnClickListener(this);
        btn_resetCookieRoom.setOnClickListener(this);
    }

    @Override
    public void onClick(View v)
    {
        Intent intent = new Intent(getActivity(), CookieActivity.class);
        switch(v.getId())
        {
            case R.id.btn_cookie1:
                intent.putExtra("cookieNum", CookieActivity.COOKIE_FIRST);
                break;
            case R.id.btn_cookie2:
                intent.putExtra("cookieNum", CookieActivity.COOKIE_SECOND);
                break;
            case R.id.btn_cookie3:
                intent.putExtra("cookieNum", CookieActivity.COOKIE_THIRD);
                break;
            case R.id.btn_resetCookieRoom:
                resetCookieRoom();
                break;
            case R.id.fab:
                btnAnimation();
                break;
        }
        if(v.getId() != R.id.fab && v.getId() != R.id.btn_resetCookieRoom)
            startActivity(intent);
    }

    public void btnAnimation()
    {
        if (isFabOpen)
        {
            btn_layout.startAnimation(fab_big);
            //mainFab.startAnimation(fab_big);
            btn_cookie1.startAnimation(fab_close);
            btn_cookie2.startAnimation(fab_close);
            btn_cookie3.startAnimation(fab_close);

            btn_cookie1.setClickable(false);
            btn_cookie2.setClickable(false);
            btn_cookie3.setClickable(false);

            isFabOpen = false;
        }
        else
        {
            btn_layout.startAnimation(fab_small);
            //mainFab.startAnimation(fab_small);
            btn_cookie1.startAnimation(fab_open);
            btn_cookie2.startAnimation(fab_open);
            btn_cookie3.startAnimation(fab_open);

            btn_cookie1.setClickable(true);
            btn_cookie2.setClickable(true);
            btn_cookie3.setClickable(true);

            isFabOpen = true;
        }
    }

    private void resetCookieRoom()
    {
        final MainActivity mainActivity = (MainActivity) getActivity();
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setTitle("쿠키함 첫번째로 이동");
        builder.setMessage("쿠키함을 첫번째로 이동시키고 모든 쿠키 시간을 초기화합니다.");
        builder.setIcon(android.R.drawable.ic_menu_revert);
        builder.setPositiveButton("예", new DialogInterface.OnClickListener()
        {
            public void onClick(DialogInterface dialog, int whichButton)
            {
                if(mainActivity.resetCookieRoom())
                    Toast.makeText(getContext(), "초기화되었습니다.", Toast.LENGTH_SHORT).show();
            }
        });
        builder.setNegativeButton("아니오", new DialogInterface.OnClickListener()
        {
            public void onClick(DialogInterface dialog, int whichButton)
            {
                Toast.makeText(getContext(), "취소하였습니다.", Toast.LENGTH_SHORT).show();
            }
        });
        builder.show();
    }
}
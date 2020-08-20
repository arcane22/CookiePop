package com.cookiefeeder.cookiepop;

import android.animation.ObjectAnimator;
import android.content.Intent;
import android.os.Bundle;

import androidx.constraintlayout.widget.ConstraintLayout;
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

public class HomeFragment extends Fragment implements View.OnClickListener {

    private Animation fab_open, fab_close, fab_small, fab_big;
    private Boolean isFabOpen = false;
    private FloatingActionButton mainFab;
    private Button button1,button2,button3,button4,button5,button6;
    private FrameLayout btn_layout;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        View root = (View)inflater.inflate(R.layout.fragment_home, container, false);
        View.OnClickListener listener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), CookieActivity.class);
                switch(v.getId())
                {
                    case R.id.cookie1:
                        intent.putExtra("순서","첫번째 쿠키");
                        break;
                    case R.id.cookie2:
                        intent.putExtra("순서","두번째 쿠키");
                        break;
                    case R.id.cookie3:
                        intent.putExtra("순서","세번째 쿠키");
                        break;
                    case R.id.cookie4:
                        intent.putExtra("순서","네번째 쿠키");
                        break;
                    case R.id.cookie5:
                        intent.putExtra("순서","다섯번째 쿠키");
                        break;
                    case R.id.cookie6:
                        intent.putExtra("순서","여섯번째 쿠키");
                        break;
                }
                startActivity(intent);
            }
        };
        fab_open = AnimationUtils.loadAnimation(getActivity(), R.anim.fab_open);
        fab_close = AnimationUtils.loadAnimation(getActivity(), R.anim.fab_close);
        fab_small = AnimationUtils.loadAnimation(getActivity(), R.anim.fab_small);
        fab_big = AnimationUtils.loadAnimation(getActivity(), R.anim.fab_big);
        btn_layout = root.findViewById(R.id.btn_layout);
        mainFab = (FloatingActionButton) root.findViewById(R.id.fab);
        mainFab.setOnClickListener(this);

        button1 = (Button) root.findViewById(R.id.cookie1);
        button2 = (Button) root.findViewById(R.id.cookie2);
        button3 = (Button) root.findViewById(R.id.cookie3);
        button4 = (Button) root.findViewById(R.id.cookie4);
        button5 = (Button) root.findViewById(R.id.cookie5);
        button6 = (Button) root.findViewById(R.id.cookie6);

        button1.setOnClickListener(listener);
        button2.setOnClickListener(listener);
        button3.setOnClickListener(listener);
        button4.setOnClickListener(listener);
        button5.setOnClickListener(listener);
        button6.setOnClickListener(listener);
        return root;
    }

    @Override
    public void onClick(View v) {
        anim();


    }
    public void anim() {

        if (isFabOpen) {
            btn_layout.startAnimation(fab_big);
            //mainFab.startAnimation(fab_big);
            button1.startAnimation(fab_close);
            button2.startAnimation(fab_close);
            button3.startAnimation(fab_close);
            button4.startAnimation(fab_close);
            button5.startAnimation(fab_close);
            button6.startAnimation(fab_close);
            button1.setClickable(false);
            button2.setClickable(false);
            button3.setClickable(false);
            button4.setClickable(false);
            button5.setClickable(false);
            button6.setClickable(false);
            isFabOpen = false;
        } else {
            btn_layout.startAnimation(fab_small);
            //mainFab.startAnimation(fab_small);
            button1.startAnimation(fab_open);
            button2.startAnimation(fab_open);
            button3.startAnimation(fab_open);
            button4.startAnimation(fab_open);
            button5.startAnimation(fab_open);
            button6.startAnimation(fab_open);
            button1.setClickable(true);
            button2.setClickable(true);
            button3.setClickable(true);
            button4.setClickable(true);
            button5.setClickable(true);
            button6.setClickable(true);
            isFabOpen = true;
        }
    }
}
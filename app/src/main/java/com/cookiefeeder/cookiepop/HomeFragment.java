package com.cookiefeeder.cookiepop;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

public class HomeFragment extends Fragment
{
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
        Button button1 = (Button) root.findViewById(R.id.cookie1);
        Button button2 = (Button) root.findViewById(R.id.cookie2);
        Button button3 = (Button) root.findViewById(R.id.cookie3);
        Button button4 = (Button) root.findViewById(R.id.cookie4);
        Button button5 = (Button) root.findViewById(R.id.cookie5);
        Button button6 = (Button) root.findViewById(R.id.cookie6);
        button1.setOnClickListener(listener);
        button2.setOnClickListener(listener);
        button3.setOnClickListener(listener);
        button4.setOnClickListener(listener);
        button5.setOnClickListener(listener);
        button6.setOnClickListener(listener);
        return root;
    }
}
package com.cookiefeeder.cookiepop;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.TextView;

public class TermsActivity extends AppCompatActivity
{
    public static final int TERMS_AGREEMENT1 = 0;
    public static final int TERMS_AGREEMENT2 = 1;
    public static final int TERMS_AGREEMENT3 = 2;

    private TextView tv_terms_title, tv_terms;

    private int agreementNum;
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_terms);
        initData();
    }

    private void initData()
    {
        agreementNum = getIntent().getIntExtra("agreementNum", 0);

        tv_terms_title = findViewById(R.id.tv_terms_title);
        tv_terms = findViewById(R.id.tv_terms);
        switch (agreementNum)
        {
            case TERMS_AGREEMENT1:
                tv_terms_title.setText("이용약관 동의");
                tv_terms.setText("이용약관 입니다.");
                break;
            case TERMS_AGREEMENT2:
                tv_terms_title.setText("개인정보 취급방침 동의");
                tv_terms.setText("개인정보 취금방침 입니다.");
                break;
            case TERMS_AGREEMENT3:
                tv_terms_title.setText("마케팅 정보 수신 동의");
                tv_terms.setText("마케팅 정보 수신입니다.");
                break;
        }
    }
}
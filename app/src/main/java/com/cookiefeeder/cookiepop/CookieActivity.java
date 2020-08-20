package com.cookiefeeder.cookiepop;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.NumberPicker;
import android.widget.TextView;
import android.widget.TimePicker;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class CookieActivity extends AppCompatActivity
{
    private TextView todayText, orderText, enterText;
    private NumberPicker datePicker;
    private TimePicker timePicker;
    private Button cancelBtn, enterBtn;
    private String enterDate;
    private int hour,minutes;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cookie);
        dateSet();
        orderSet();
        datePickerSet();
        timePickerSet();
        btnSet();
    }

    private void dateSet()
    {
        todayText = (TextView) findViewById(R.id.todayText);
        Date date = new Date();
        SimpleDateFormat dateFormat = new SimpleDateFormat("MM/dd");
        SimpleDateFormat eFormat = new SimpleDateFormat("E");
        String todayDate = dateFormat.format(date);
        String todayElements = eFormat.format(date);
        if(todayElements.equals("Mon") || todayElements.equals("월")) todayElements = "월";
        if(todayElements.equals("Tue") || todayElements.equals("화")) todayElements = "화";
        if(todayElements.equals("Wed") || todayElements.equals("수")) todayElements = "수";
        if(todayElements.equals("Thu") || todayElements.equals("목")) todayElements = "목";
        if(todayElements.equals("Fri") || todayElements.equals("금")) todayElements = "금";
        if(todayElements.equals("Sat") || todayElements.equals("토")) todayElements = "토";
        if(todayElements.equals("Sun") || todayElements.equals("일")) todayElements = "일";
        enterDate = todayDate;
        todayText.setText("Today "+todayDate+" ("+todayElements+")");
    }

    private void orderSet()
    {
        orderText = (TextView) findViewById(R.id.orderText);
        Intent intent = getIntent();
        String order = intent.getExtras().getString("순서");
        orderText.setText(order);
    }

    private void datePickerSet()
    {
        datePicker = (NumberPicker) findViewById(R.id.datePicker);
        datePicker.setMinValue(0);
        datePicker.setMaxValue(5);
        datePicker.setDisplayedValues(new String[]{"오늘", "1일뒤", "2일뒤", "3일뒤", "4일뒤", "5일뒤"});
        datePicker.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {
            @Override
            public void onValueChange(NumberPicker picker, int oldVal, int newVal) {
                Calendar cal = Calendar.getInstance();
                cal.add(Calendar.DATE, newVal);
                SimpleDateFormat dateFormat = new SimpleDateFormat("MM/dd");
                enterDate = dateFormat.format(cal.getTime());
            }
        });
    }

    private void timePickerSet()
    {
        timePicker = (TimePicker)findViewById(R.id.timePicker);
        hour = timePicker.getHour();
        minutes = timePicker.getMinute();
        timePicker.setOnTimeChangedListener(new TimePicker.OnTimeChangedListener() {
            @Override
            public void onTimeChanged(TimePicker view, int hourOfDay, int minute) {
                hour = hourOfDay;
                minutes = minute;
            }
        });
    }

    private void btnSet()
    {
        enterText = (TextView) findViewById(R.id.enterText);
        enterBtn = (Button) findViewById(R.id.cookieEnterBtn);
        cancelBtn = (Button) findViewById(R.id.cookieCancelBtn);
        enterBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                enterText.setText("선택된 날짜는 "+enterDate+" "+hour+"시 "+minutes+"분 입니다.");
            }
        });
        cancelBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                //startActivity(intent);
                finish();
            }
        });
    }

}

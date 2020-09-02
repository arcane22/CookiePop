package com.cookiefeeder.cookiepop;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.IBinder;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.NumberPicker;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class CookieActivity extends AppCompatActivity implements View.OnClickListener, EditText.OnEditorActionListener
{
    public static final int COOKIE_FIRST = 0, COOKIE_SECOND = 1, COOKIE_THIRD = 2;
    private int currentCookieNum;
    private String year, month, day, hour, minutes;

    private TextView tv_cookie_todayDate, tv_cookie_number, tv_cookie_showCookieTime;
    private EditText et_cookie_memo;
    private NumberPicker datePicker;
    private TimePicker timePicker;
    private Button btn_cancel, btn_confirm, btn_startCookie;

    private NetworkService networkService;
    private boolean onNetworkServiceBound;
    private User user;

    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;

    private ServiceConnection mConnection = new ServiceConnection()
    {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service)
        {
            NetworkService.NetworkServiceBinder binder = (NetworkService.NetworkServiceBinder) service;
            networkService = binder.getService();
            onNetworkServiceBound = true;
            String cookieStr = networkService.getUser().getCookieTimeList().get(currentCookieNum);
            if(cookieStr.equals("null")) cookieStr = "없음.";
            tv_cookie_showCookieTime.setText("설정된 쿠키시간 : " + cookieStr);
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
        setContentView(R.layout.activity_cookie);
        initData();
        setCookieOrder();
        setTodayDateText();
        setDatePicker();
        setTimePicker();
    }

    @Override
    protected void onDestroy()
    {
        super.onDestroy();
        unbindService(mConnection);
    }

    /** Initialize class fields **/
    private void initData()
    {
        Intent intent = new Intent(getApplication(), NetworkService.class);
        bindService(intent, mConnection, Context.BIND_AUTO_CREATE);
        currentCookieNum = getIntent().getIntExtra("cookieNum", 0);

        tv_cookie_todayDate = findViewById(R.id.tv_cookie_todayDate);
        tv_cookie_number = findViewById(R.id.tv_cookie_number);
        tv_cookie_showCookieTime = findViewById(R.id.tv_cookie_showCookieTime);

        et_cookie_memo = findViewById(R.id.et_cookie_memo);
        et_cookie_memo.setOnEditorActionListener(this);

        datePicker = findViewById(R.id.datePicker);
        timePicker = findViewById(R.id.timePicker);

        btn_cancel = findViewById(R.id.btn_cookie_cancel);
        btn_confirm = findViewById(R.id.btn_cookie_confirm);
        btn_startCookie = findViewById(R.id.btn_executeCookie);
        btn_cancel.setOnClickListener(this);
        btn_confirm.setOnClickListener(this);
        btn_startCookie.setOnClickListener(this);

        sharedPreferences = getSharedPreferences("cookieMemo", MODE_PRIVATE);
        editor = sharedPreferences.edit();

        String cookieMemo1 = sharedPreferences.getString("cookieMemo1", "");
        String cookieMemo2 = sharedPreferences.getString("cookieMemo2", "");
        String cookieMemo3 = sharedPreferences.getString("cookieMemo3", "");

        switch(currentCookieNum)
        {
            case COOKIE_FIRST:
                if(cookieMemo1.length() != 0)
                    et_cookie_memo.setText(cookieMemo1);
                break;
            case COOKIE_SECOND:
                if(cookieMemo2.length() != 0)
                    et_cookie_memo.setText(cookieMemo2);
                break;
            case COOKIE_THIRD:
                if(cookieMemo3.length() != 0)
                    et_cookie_memo.setText(cookieMemo3);
                break;
        }
    }

    /** Set cookie number(order) text view **/
    private void setCookieOrder()
    {
        currentCookieNum = getIntent().getIntExtra("cookieNum", 0);
        switch(currentCookieNum)
        {
            case COOKIE_FIRST:
                tv_cookie_number.setText("첫번째 쿠키");
                break;
            case COOKIE_SECOND:
                tv_cookie_number.setText("두번째 쿠키");
                break;
            case COOKIE_THIRD:
                tv_cookie_number.setText("세번째 쿠키");
                break;
        }
    }

    /** Set today's date text view **/
    private void setTodayDateText()
    {
        Date date = new Date();
        SimpleDateFormat yyyyMMddFormat = new SimpleDateFormat("yyyy/MM/dd");
        SimpleDateFormat dateFormat = new SimpleDateFormat("MM/dd");
        SimpleDateFormat eFormat = new SimpleDateFormat("E");

        String todayElements = eFormat.format(date);
        String currentDate = yyyyMMddFormat.format(date);

        if(todayElements.equals("Mon") || todayElements.equals("월")) todayElements = "월";
        if(todayElements.equals("Tue") || todayElements.equals("화")) todayElements = "화";
        if(todayElements.equals("Wed") || todayElements.equals("수")) todayElements = "수";
        if(todayElements.equals("Thu") || todayElements.equals("목")) todayElements = "목";
        if(todayElements.equals("Fri") || todayElements.equals("금")) todayElements = "금";
        if(todayElements.equals("Sat") || todayElements.equals("토")) todayElements = "토";
        if(todayElements.equals("Sun") || todayElements.equals("일")) todayElements = "일";
        tv_cookie_todayDate.setText("Today " + currentDate +" (" + todayElements + ")");
    }

    /** Set Date picker's attribute **/
    private void setDatePicker()
    {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd");
        year = dateFormat.format(new Date()).substring(0, 4);
        month = dateFormat.format(new Date()).substring(5, 7);
        day = dateFormat.format(new Date()).substring(8, 10);

        datePicker.setMinValue(0);
        datePicker.setMaxValue(7);
        datePicker.setDisplayedValues(new String[]{"오늘", "1일뒤", "2일뒤", "3일뒤", "4일뒤", "5일뒤", "6일뒤", "7일뒤"});
        datePicker.setOnValueChangedListener(new NumberPicker.OnValueChangeListener()
        {
            @Override
            public void onValueChange(NumberPicker picker, int oldVal, int newVal)
            {
                Calendar cal = Calendar.getInstance();
                cal.add(Calendar.DATE, newVal);
                SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd");
                year = dateFormat.format(cal.getTime()).substring(0, 4);
                month = dateFormat.format(cal.getTime()).substring(5, 7);
                day = dateFormat.format(cal.getTime()).substring(8, 10);
            }
        });
    }

    /** Set Time picker's attribute **/
    private void setTimePicker()
    {
        int initHour = timePicker.getHour();
        int initMinute = timePicker.getMinute();

        hour = initHour > 10 ? String.valueOf(initHour) : "0" + initHour;
        minutes = initMinute > 10 ? String.valueOf(initMinute) : "0" + initMinute;

        timePicker.setOnTimeChangedListener(new TimePicker.OnTimeChangedListener()
        {
            @Override
            public void onTimeChanged(TimePicker view, int hourOfDay, int minute)
            {
                hour = hourOfDay > 10 ? String.valueOf(hourOfDay) : "0" + hourOfDay;
                minutes = minute > 10 ? String.valueOf(minute) : "0" + minute;
            }
        });
    }

    @Override
    public void onClick(View v)
    {
        switch(v.getId())
        {
            case R.id.btn_cookie_cancel:
                finish();
                break;
            case R.id.btn_cookie_confirm:
                saveCookieTime(v);
                break;
            case R.id.btn_executeCookie:
                executeCookie();
                break;
        }
    }

    @Override
    public boolean onEditorAction(TextView v, int actionId, KeyEvent event)
    {
        final boolean isEnterEvent = event != null && event.getKeyCode() == KeyEvent.KEYCODE_ENTER;
        final boolean isEnterUpEvent = isEnterEvent && event.getAction() == KeyEvent.ACTION_UP;
        final boolean isEnterDownEvent = isEnterEvent && event.getAction() == KeyEvent.ACTION_DOWN;

        if(actionId == EditorInfo.IME_ACTION_DONE || isEnterEvent)
        {
            String data = v.getText().toString();
            switch(currentCookieNum)
            {
                case COOKIE_FIRST:
                    editor.putString("cookieMemo1", data);
                    break;
                case COOKIE_SECOND:
                    editor.putString("cookieMemo2", data);
                    break;
                case COOKIE_THIRD:
                    editor.putString("cookieMemo3", data);
                    break;
            }
            editor.commit();

            InputMethodManager inputMethodManager
                    = (InputMethodManager) getApplicationContext().getSystemService(Context.INPUT_METHOD_SERVICE);
            inputMethodManager.hideSoftInputFromWindow(v.getWindowToken(), 0);
            Toast.makeText(getApplicationContext(), "메모가 저장되었습니다.", Toast.LENGTH_SHORT).show();
        }
        return true;
    }

    private void saveCookieTime(View v)
    {
        String pickerDateStr = year + "-" + month + "-" + day + " " + hour + ":" + minutes + ":" + "00";
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date dbCookieDate = null;
        Date pickerDate = null;
        try
        {
            pickerDate = simpleDateFormat.parse(pickerDateStr);
        }
        catch(ParseException e)
        {
            e.printStackTrace();
        }
        if(pickerDate.getTime() > new Date().getTime())
        {
            networkService.setCookieTime(currentCookieNum, pickerDateStr);
            networkService.getUser().getCookieTimeList().set(currentCookieNum, pickerDateStr);
            Toast.makeText(getApplicationContext(), "시간이 설정되었습니다.", Toast.LENGTH_SHORT).show();
            tv_cookie_showCookieTime.setText("설정된 쿠키시간 : " + pickerDateStr);
        }
        else
        {
            Toast.makeText(getApplicationContext(), "현재시간 보다 이후의 시간을 설정해주세요.", Toast.LENGTH_SHORT).show();
        }
    }

    private void executeCookie()
    {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("쿠키 즉시 실행");
        builder.setMessage("쿠키함을 즉시 실행시키고 해당 쿠키 시간을 초기화합니다.");
        builder.setIcon(android.R.drawable.ic_menu_revert);
        builder.setPositiveButton("예", new DialogInterface.OnClickListener()
        {
            public void onClick(DialogInterface dialog, int whichButton)
            {
                if(onNetworkServiceBound)
                {
                    networkService.executeCookie(currentCookieNum);
                    tv_cookie_showCookieTime.setText("설정된 쿠키시간 : 없음.");
                    Toast.makeText(getApplicationContext(), "실행되었습니다.", Toast.LENGTH_SHORT).show();
                }
            }
        });
        builder.setNegativeButton("아니오", new DialogInterface.OnClickListener()
        {
            public void onClick(DialogInterface dialog, int whichButton)
            {
                Toast.makeText(getApplicationContext(), "취소하였습니다.", Toast.LENGTH_SHORT).show();
            }
        });
        builder.show();
    }
}

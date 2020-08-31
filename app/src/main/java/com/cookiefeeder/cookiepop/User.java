package com.cookiefeeder.cookiepop;

import android.os.Parcel;
import android.os.Parcelable;
import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Array;
import java.util.ArrayList;

public class User
{
    private String uid;
    private String userID;
    private String userName;
    private String userBirthday;
    private String userSignUpDate;
    private String userMachineId;
    private ArrayList<String> cookieTimeList;

    public User(JSONObject userInfo)
    {
        try
        {
            uid = userInfo.getString("uid");
            userID = userInfo.getString("user_id");
            userName = userInfo.getString("user_name");
            userBirthday = userInfo.getString("user_birthday");
            userSignUpDate = userInfo.getString("user_signupdate");
            userMachineId = userInfo.getString("user_machineId");
            cookieTimeList = new ArrayList<>();
            cookieTimeList.add(userInfo.getString("user_cookieTime1"));
            cookieTimeList.add(userInfo.getString("user_cookieTime2"));
            cookieTimeList.add(userInfo.getString("user_cookieTime3"));
            cookieTimeList.add(userInfo.getString("user_cookieTime4"));
            cookieTimeList.add(userInfo.getString("user_cookieTime5"));
            cookieTimeList.add(userInfo.getString("user_cookieTime6"));
        }
        catch(JSONException e)
        {
            e.printStackTrace();
        }
    }

    public User(String uid, String userID, String userName, String userBirthday, String userSignUpDate)
    {
        this.uid = uid;
        this.userID = userID;
        this.userName = userName;
        this.userBirthday = userBirthday;
        this.userSignUpDate = userSignUpDate;
    }

    /** Getter & Setter for class fields **/
    public String getUID() { return uid; }
    public void setUID(String uid) { this.uid = uid; }

    public String getUserID() { return userID; }
    public void setUserID(String userID) { this.userID = userID; }

    public String getUserName() { return userName; }
    public void setUserName(String userName) { this.userName = userName; }

    public String getUserBirthday() { return userBirthday; }
    public void setUserBirthday(String userBirthday) { this.userBirthday = userBirthday; }

    public String getUserSignUpDate() { return userSignUpDate; }
    public void setUserSignUpDate(String userSignUpDate) { this.userSignUpDate = userSignUpDate; }

    public String getUserMachineId() { return userMachineId; }
    public void setUserMachineId(String userMachineId) { this.userMachineId = userMachineId; }

    public ArrayList<String> getCookieTimeList() { return cookieTimeList; }
    public void setCookieTimeList(ArrayList<String> cookieTimeList) { this.cookieTimeList = cookieTimeList; }
}

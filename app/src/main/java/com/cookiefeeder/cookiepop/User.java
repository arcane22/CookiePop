package com.cookiefeeder.cookiepop;

import android.os.Parcel;
import android.os.Parcelable;

import org.json.JSONException;
import org.json.JSONObject;

public class User implements Parcelable
{
    private String uid;
    private String userID;
    private String userName;
    private String userBirthday;
    private String userSignUpDate;

    public User(JSONObject userInfo)
    {
        try
        {
            uid = userInfo.getString("uid");
            userID = userInfo.getString("user_id");
            userName = userInfo.getString("user_name");
            String birthdayStr = userInfo.getString("user_birthday");
            userBirthday = birthdayStr.substring(0, 4) + birthdayStr.substring(5, 7) + birthdayStr.substring(8, 10);
            String signUpStr = userInfo.getString("user_signupdate");
            userSignUpDate = signUpStr.substring(0, 4) + signUpStr.substring(5, 7) + signUpStr.substring(8, 10);
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

    protected User(Parcel in)
    {
        uid = in.readString();
        userID = in.readString();
        userName = in.readString();
        userBirthday = in.readString();
        userSignUpDate = in.readString();
    }

    public static final Creator<User> CREATOR = new Creator<User>()
    {
        @Override
        public User createFromParcel(Parcel in)
        {
            return new User(in);
        }
        @Override
        public User[] newArray(int size)
        {
            return new User[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags)
    {
        dest.writeString(uid);
        dest.writeString(userID);
        dest.writeString(userName);
        dest.writeString(userBirthday);
        dest.writeString(userSignUpDate);
    }

    /** Getter & Setter for class fields **/    public String getUID() { return uid; }
    public void setUID(String uid) { this.uid = uid; }

    public String getUserID() { return userID; }
    public void setUserID(String userID) { this.userID = userID; }

    public String getUserName() { return userName; }
    public void setUserName(String userName) { this.userName = userName; }

    public String getUserBirthday() { return userBirthday; }
    public void setUserBirthday(String userBirthday) { this.userBirthday = userBirthday; }

    public String getUserSignUpDate() { return userSignUpDate; }
    public void setUserSignUpDate(String userSignUpDate) { this.userSignUpDate = userSignUpDate; }
}

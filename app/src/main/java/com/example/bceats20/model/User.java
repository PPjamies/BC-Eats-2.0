package com.example.bceats20.model;

public class User {
    private String mUserName; //possibly not needed
    private String mPhone;
    private String mPassword;
    private String mSessionToken; //possibly not needed
    private String mPushToken; //possibly not needed

    //delete this constructor if username is not needed
    public User(String _mUserName, String _mPhone, String _mPassword){
        mUserName = _mUserName;
        mPhone = _mPhone;
        mPassword = _mPassword;
        mSessionToken = "";
        mPushToken = "";
    }

    public User(String _mPhone, String _mPassword){
        mPhone = _mPhone;
        mPassword = _mPassword;
        mSessionToken = "";
        mPushToken = "";
    }
    public String getUserName() {
        return mUserName;
    }

    public void setUserName(String userName) {
        mUserName = userName;
    }
    public String getPhone() {
        return mPhone;
    }

    public void setPhone(String phone) {
        mPhone = phone;
    }

    public String getPassword() {
        return mPassword;
    }

    public void setPassword(String password) {
        mPassword = password;
    }


    public String getSessionToken() {
        return mSessionToken;
    }

    public void setSessionToken(String sessionToken) {
        mSessionToken = sessionToken;
    }

    public String getPushToken() {
        return mPushToken;
    }

    public void setPushToken(String pushToken) {
        mPushToken = pushToken;
    }
}

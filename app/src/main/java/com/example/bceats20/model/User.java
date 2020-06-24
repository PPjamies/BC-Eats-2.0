package com.example.bceats20.model;

public class User {
    private String mPhone;
    private String mSessionToken; //possibly not needed
    private String mPushToken; //possibly not needed

    public User(){}

    //delete this constructor if username is not needed
    public User(String _mPhone){
        mPhone = _mPhone;
        mSessionToken = "";
        mPushToken = "";
    }

    public String getPhone() {
        return mPhone;
    }

    public void setPhone(String phone) {
        mPhone = phone;
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

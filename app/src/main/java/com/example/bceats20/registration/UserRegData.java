package com.example.bceats20.registration;

public class UserRegData {
    private String username;
    private String phoneNumber;
    private String code;

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public UserRegData(String mUsername, String mPhoneNumber){
        setUsername(mUsername);
        setPhoneNumber(mPhoneNumber);
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }
}

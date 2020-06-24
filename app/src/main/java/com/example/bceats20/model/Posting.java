package com.example.bceats20.model;

public class Posting {
    private String mTitle;
    private String mBuilding;
    private String mRoom;
    private String mTimeLimit;
    private String mDescription;
    private String mImageKey;
    private String mPhone;

    public Posting(){
        mTitle="";
        mBuilding="";
        mRoom="";
        mTimeLimit="";
        mDescription="";
        mImageKey="";
    }

    public Posting(String title, String building, String room, String timeLimit, String description, String imageKey) {
        mTitle = title;
        mBuilding = building;
        mRoom = room;
        mTimeLimit = timeLimit;
        mDescription = description;
        mImageKey = imageKey;
    }

    public String getPhone() {
        return mPhone;
    }

    public void setPhone(String phone) {
        mPhone = phone;
    }

    public String getTitle() {
        return mTitle;
    }

    public void setTitle(String title) {
        mTitle = title;
    }

    public String getBuilding() {
        return mBuilding;
    }

    public void setBuilding(String building) {
        mBuilding = building;
    }

    public String getRoom() {
        return mRoom;
    }

    public void setRoom(String room) {
        mRoom = room;
    }

    public String getTimeLimit() {
        return mTimeLimit;
    }

    public void setTimeLimit(String timeLimit) {
        mTimeLimit = timeLimit;
    }

    public String getDescription() {
        return mDescription;
    }

    public void setDescription(String description) {
        mDescription = description;
    }

    public String getImageKey() {
        return mImageKey;
    }

    public void setImageKey(String imageKey) {
        mImageKey = imageKey;
    }

}

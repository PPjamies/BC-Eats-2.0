package com.example.bceats20.registration;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.SavedStateHandle;
import androidx.lifecycle.ViewModel;

public class RegistrationViewModel extends ViewModel {
    private RegistrationRepository repository;

    private static final String TAG = "RegistrationViewModel";
    private static final String AREACODE_KEY = "areacode";
    private static final String PHONE_KEY = "phonenumber";
    private static final String USER_KEY = "username";
    private static final String CONFIRMATION_KEY = "confirmationCode";

    private LiveData<String> username;
    private LiveData<String> areacode;
    private LiveData<String> phonenumber;
    private LiveData<String> confirmationCode;

    private SavedStateHandle mState;

    public RegistrationViewModel(SavedStateHandle savedStateHandle){
        mState = savedStateHandle;
        repository = new RegistrationRepository();
    }


    //Getters and setters
    LiveData<String> getUsername(){
        return mState.getLiveData(USER_KEY);
    }
    public void setUsername(String mUsername){
        Log.d(TAG, "setUsername: ");
        mState.set(USER_KEY, mUsername);
    }
    LiveData<String> getAreacode() {
        return mState.getLiveData(AREACODE_KEY);
    }
    public void setAreacode(String areacode) {
        mState.set(AREACODE_KEY, areacode);
    }
    LiveData<String> getPhonenumber(){
        return mState.getLiveData(PHONE_KEY);
    }
    public void setPhonenumber(String mPhonenumber){
        Log.d(TAG, "setPhonenumber: ");
        mState.set(PHONE_KEY, mPhonenumber);
    }
    LiveData<String> getConfirmationCode(){
        return mState.getLiveData(CONFIRMATION_KEY);
    }
    public void setConfirmationCode(String confirmationCode) {
        mState.set(CONFIRMATION_KEY, confirmationCode);
    }



    public void phoneAuth(){

        Log.d(TAG, "phoneAuth: phoneAuth() method in ViewModel called");
//        repository.sendVerificationCode(getPhonenumber().getValue());
    }

    public void addUserToDatabase(){
        Log.d(TAG, "addUserToDatabase: ");
        repository.uploadUserToFirebaseDatabase("5555555555");
    }
}
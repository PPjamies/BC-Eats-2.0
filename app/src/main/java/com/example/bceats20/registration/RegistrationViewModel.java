package com.example.bceats20.registration;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.SavedStateHandle;
import androidx.lifecycle.ViewModel;

public class RegistrationViewModel extends AndroidViewModel {
    private RegistrationRepository repository;

    private static final String TAG = "RegistrationViewModel";
    private static final String AREACODE_KEY = "areacode";
    private static final String PHONE_KEY = "phonenumber";
    private static final String USER_KEY = "username";
    private static final String CONFIRMATION_KEY = "confirmationCode";
    private static final String COUNTRY_CODE = "+1";

    //LiveData fields that will be observable between activity and ViewModel
    private LiveData<String> username;
    private LiveData<String> areacode;
    private LiveData<String> phonenumber;
    private LiveData<String> confirmationCode;

    //Shared preferences local data storage
    private SharedPreferences sharedPref;
    private SharedPreferences.Editor editor;

    private SavedStateHandle mState;

    public RegistrationViewModel(Application application, SavedStateHandle savedStateHandle){
        super(application);
        mState = savedStateHandle;
        repository = new RegistrationRepository();

        //Local data storage setup
        sharedPref = getApplication().getSharedPreferences("user", Context.MODE_PRIVATE);
        editor = sharedPref.edit();
    }




    //Getters and setters
    LiveData<String> getUsername(){
        return mState.getLiveData(USER_KEY);
    }
    public void setUsername(String mUsername){
        Log.d(TAG, "setUsername: ");
        //Sets the saved state
        mState.set(USER_KEY, mUsername);
        //Sets the value in the SharedPreferences so that it can be used outside of this context
        editor.putString(USER_KEY,mUsername);
        editor.commit();
    }
    LiveData<String> getAreacode() {
        return mState.getLiveData(AREACODE_KEY);
    }
    public void setAreacode(String areacode) {
        mState.set(AREACODE_KEY, areacode);
        //Sets the value in the SharedPreferences so that it can be used outside of this context
        editor.putString(AREACODE_KEY,areacode);
        editor.commit();
    }
    LiveData<String> getPhonenumber(){
        return mState.getLiveData(PHONE_KEY);
    }
    public void setPhonenumber(String mPhonenumber){
        Log.d(TAG, "setPhonenumber: ");
        mState.set(PHONE_KEY, mPhonenumber);
        //Sets the value in the SharedPreferences so that it can be used outside of this context
        editor.putString(PHONE_KEY, mPhonenumber);
        editor.commit();
    }
    LiveData<String> getConfirmationCode(){
        return mState.getLiveData(CONFIRMATION_KEY);
    }
    public void setConfirmationCode(String confirmationCode) {
        mState.set(CONFIRMATION_KEY, confirmationCode);
    }



    public void phoneAuth(){

        Log.d(TAG, "phoneAuth: phoneAuth() method in ViewModel called");
        //Todo: must format the number in a way that Firebase will accept it (+15556666 format)
        repository.sendVerificationCode(COUNTRY_CODE
                                        .concat(getAreacode().getValue()
                                        .concat(getPhonenumber().getValue())));
    }

    public void addUserToDatabase(){
        Log.d(TAG, "addUserToDatabase: ");
        /*Todo: currently, this just adds the user as soon as they click the "Confirm" button in
           the activity. This should be called as soon as we get confirmation that they were verified
           by Firebase
         */
        repository.uploadUserToFirebaseDatabase(getAreacode().getValue().concat(getPhonenumber().getValue()));
    }
}
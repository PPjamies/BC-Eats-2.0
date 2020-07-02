package com.example.bceats20.ui.account;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import com.example.bceats20.R;
import com.example.bceats20.model.User;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class AccountViewModel extends AndroidViewModel {
    private final String TAG = "AccountViewModel";

    private AccountRepository mAccountRepository;
    private MutableLiveData<User> mUser;

    //SharedPreferences data save/retrieve
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;

    public AccountViewModel(Application application) {
        super(application);
        mAccountRepository = new AccountRepository();
        sharedPreferences = getApplication().getSharedPreferences(getApplication().getString(R.string.shared_preferences_file_name), Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();
    }

    public void setNewPhoneNumber(String oldPhoneNumber, String newPhoneNumber){
        if(mUser == null){
             mUser = mAccountRepository.GET_USER(oldPhoneNumber);
        }
        mAccountRepository.UPDATE_USER(newPhoneNumber, mUser);
    }

    public LiveData<User> getUser(){
        if(mUser == null){
            Log.d(TAG, "getUser: phonenumber "+getApplication().getString(R.string.shared_preferences_file_name));
            mUser = mAccountRepository.GET_USER(sharedPreferences.getString(getApplication().getString(R.string.shared_preferences_file_name),null));
        }
        return mUser;
    }

    public void setUser(User user){
        mUser.setValue(user);
    }
}
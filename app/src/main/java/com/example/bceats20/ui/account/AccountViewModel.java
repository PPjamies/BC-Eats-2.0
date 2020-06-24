package com.example.bceats20.ui.account;

import android.util.Log;

import com.example.bceats20.model.User;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class AccountViewModel extends ViewModel {
    private final String TAG = "AccountViewModel";

    private AccountRepository mAccountRepository;
    private MutableLiveData<User> mUser;

    public AccountViewModel() {
        mAccountRepository = new AccountRepository();
    }

    public void setNewPhoneNumber(String oldPhoneNumber, String newPhoneNumber){
        if(mUser == null){
            mUser = mAccountRepository.GET_USER(oldPhoneNumber);
        }
        mAccountRepository.UPDATE_USER(newPhoneNumber, mUser);
    }

    public LiveData<User> getUser(){
        if(mUser == null){
            mUser = mAccountRepository.GET_USER("12068888888");
        }
        return mUser;
    }

    public void setUser(User user){
        mUser.setValue(user);
    }
}
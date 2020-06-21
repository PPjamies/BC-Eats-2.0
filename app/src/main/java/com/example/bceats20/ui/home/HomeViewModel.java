package com.example.bceats20.ui.home;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class HomeViewModel extends ViewModel {

    private MutableLiveData<String> mDateText;
    private MutableLiveData<String> mHomeText;

    public HomeViewModel() {
        mDateText = new MutableLiveData<>();
        mHomeText = new MutableLiveData<>();
        mDateText.setValue(getDate());
    }

    private String getDate(){
        SimpleDateFormat dateFormat = new SimpleDateFormat("MMM dd, yyyy", Locale.US);
        Date date = new Date();
        return dateFormat.format(date);
    }

    public LiveData<String> getDateText() {
        return mDateText;
    }

    public LiveData<String> getHomeText(){
        return mHomeText;
    }
    public void setHomeText(String message){
        mHomeText.setValue(message);
    }

}
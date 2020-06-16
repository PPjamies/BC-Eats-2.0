package com.example.bceats20.ui.listings;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class ListingsViewModel extends ViewModel {

    private MutableLiveData<String> mText;

    public ListingsViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("This is my listings fragment");
    }

    public LiveData<String> getText() {
        return mText;
    }
}
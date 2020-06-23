package com.example.bceats20.ui.home;

import android.content.res.Resources;
import android.util.Log;

import com.example.bceats20.R;
import com.example.bceats20.post.PostRepository;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class HomeViewModel extends ViewModel {
    private static final String TAG = "HomeViewModel";

    private PostRepository mPostRepository;
    private MutableLiveData<String> mDateText;
    private MutableLiveData<String> mHomeText;

    public HomeViewModel() {
        mPostRepository = new PostRepository();
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
    public LiveData<Boolean> hasPosts(){
        MutableLiveData<Boolean> b = mPostRepository.HAS_ACTIVE_POSTINGS();
        Log.d(TAG, "hasPosts: " + b.getValue());
        return mPostRepository.HAS_ACTIVE_POSTINGS();
    }
    public LiveData<String> getHomeText(){
        return mHomeText;
    }
    public void setHomeText(String message){
        mHomeText.setValue(message);
    }

}
package com.example.bceats20.ui.listings;

import android.util.Log;

import com.example.bceats20.model.Posting;

import java.lang.reflect.Array;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class ListingsViewModel extends ViewModel {
    private static final String TAG = "ListingViewModel";

    private ListingsRepository mListingsRepository;
    private MutableLiveData<String> mDateText;
    private MutableLiveData<Boolean> mHasPosts;
    private MutableLiveData<ArrayList<Posting>> mListings;

    public ListingsViewModel() {
        mListingsRepository = new ListingsRepository();
        mDateText = new MutableLiveData<>();
        mListings = new MutableLiveData<>();

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

    //checks to see if user has any postings
    public LiveData<Boolean> hasPosts(String phoneNumber){
        mHasPosts = mListingsRepository.USER_HAS_ACTIVE_POSTINGS(phoneNumber);
        return mHasPosts;
    }

    //returns user postings
    public LiveData<ArrayList<Posting>> getListings(String phoneNumber){
        if(mListings == null){
            mListings = new MutableLiveData<>();
        }
        mListings = mListingsRepository.GET_USER_ACTIVE_POSTINGS(phoneNumber);
        return mListings;
    }
    public void setHasPosts(Boolean bool){
        mHasPosts.setValue(bool);
    }
    public void setList(ArrayList<Posting> list){
        mListings.setValue(list);
    }
}

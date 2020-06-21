package com.example.bceats20.post;

import android.graphics.Bitmap;
import android.net.Uri;

import com.example.bceats20.model.Posting;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class CreatePostViewModel extends ViewModel {
    private CreatePostRepository mCreatePostRepository; //manages data from external sources
    private MutableLiveData<Bitmap> mBitmap;
    private MutableLiveData<Uri> mUri;


    //Constructor
    public CreatePostViewModel() {
       mCreatePostRepository = new CreatePostRepository();
    }

    public void setPosting(String title, String building, String room, String timeLimit, String description){
        Posting posting = new Posting();
        posting.setTitle(title);
        posting.setBuilding(building);
        posting.setRoom(room);
        posting.setTimeLimit(timeLimit);
        posting.setDescription(description);

        String mImageKey = mCreatePostRepository.UPLOAD_NEW_POSTING(posting);
        mCreatePostRepository.UPLOAD_NEW_POSTING_IMAGE(mUri.getValue(),mImageKey);
    }

    public LiveData<Bitmap> getBitmap(){
        if (mBitmap == null) {
            mBitmap = new MutableLiveData<Bitmap>();
        }
        return mBitmap;
    }
    public void setBitmap(Bitmap bm){
        mBitmap.postValue(bm);
    }

    public LiveData<Uri> getUri(){
        if(mUri == null){
            mUri = new MutableLiveData<Uri>();
        }
        return mUri;
    }

    public void setUri(Uri uri){
        mUri.postValue(uri);
    }
}

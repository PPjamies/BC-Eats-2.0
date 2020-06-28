package com.example.bceats20.post;

import android.graphics.Bitmap;
import android.net.Uri;
import android.util.Log;

import com.example.bceats20.model.Posting;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class CreatePostViewModel extends ViewModel {
    private static final String TAG = "CreatePostingViewModel";

    private PostRepository mPostRepository; //manages data from external sources
    private MutableLiveData<Bitmap> mBitmap;
    private MutableLiveData<Uri> mUri;


    //Constructor
    public CreatePostViewModel() {
       mPostRepository = new PostRepository();
    }

    public void setPosting(String title, String building, String room, String timeLimit, String description){
        Posting posting = new Posting();
        posting.setTitle(title);
        posting.setBuilding(building);
        posting.setRoom(room);
        posting.setTimeLimit(timeLimit);
        posting.setDescription(description);

        String mImageKey = mPostRepository.UPLOAD_NEW_POSTING(posting);
        if(mUri.getValue() != null) {
            mPostRepository.UPLOAD_NEW_POSTING_IMAGE(mUri.getValue(), mImageKey);
        }else{
            Log.d(TAG, "setPosting: the uri is null wtf");
        }
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
        mUri.setValue(uri);
    }
}

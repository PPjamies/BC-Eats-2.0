package com.example.bceats20.post;

import android.graphics.Bitmap;
import android.net.Uri;
import android.service.voice.AlwaysOnHotwordDetector;
import android.util.Log;

import com.example.bceats20.model.Posting;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class EditPostViewModel extends ViewModel {
    private static final String TAG = "EditPostViewModel";

    private PostRepository mPostRepository;
    public MutableLiveData<Posting> mPosting;
    private MutableLiveData<Bitmap> mBitmap;
    private MutableLiveData<Uri> mUri;

    public EditPostViewModel() {
        mPostRepository = new PostRepository();
    }

    public void setPosting(String title, String building, String room, String timeLimit, String description){
        Posting posting = new Posting();
        posting.setTitle(title);
        posting.setBuilding(building);
        posting.setRoom(room);
        posting.setTimeLimit(timeLimit);
        posting.setDescription(description);
        posting.setPhone(mPosting.getValue().getPhone());
        posting.setImageKey(mPosting.getValue().getImageKey());
        mPosting.setValue(posting);

        mPostRepository.UPDATE_POSTING(mPosting.getValue(), mPosting.getValue().getImageKey());
        if (mUri.getValue() != null) {
            mPostRepository.UPLOAD_NEW_POSTING_IMAGE(mUri.getValue(), mPosting.getValue().getImageKey());
        }
    }

    public LiveData<Posting> getPosting(String postID){
        if(mPosting == null){
            mPosting = new MutableLiveData<>();
        }
        mPosting = mPostRepository.GET_POSTING(postID);
        return mPosting;
    }

    public LiveData<Bitmap> getBitmap(String postID){
        if(mBitmap == null){
            mBitmap = mPostRepository.GET_POSTING_IMAGE(postID);
        }
        return mBitmap;
    }

    public void setBitmap(Bitmap bm){
        mBitmap.postValue(bm);
    }

    public LiveData<Uri> getUri(){
        if(mUri == null){
            mUri = new MutableLiveData<>();
        }
        return mUri;
    }

    public void setUri(Uri uri){
        mUri.setValue(uri);
    }
}

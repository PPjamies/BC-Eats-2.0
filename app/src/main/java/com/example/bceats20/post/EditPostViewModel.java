package com.example.bceats20.post;

import android.graphics.Bitmap;
import android.net.Uri;
import android.util.Log;

import com.example.bceats20.model.Posting;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class EditPostViewModel extends ViewModel {
    private static final String TAG = "EditPostViewModel";

    private PostRepository mPostRepository;
    private MutableLiveData<Posting> mPosting;
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
        posting.setImageKey(mPosting.getValue().getImageKey());
        mPosting.setValue(posting);

        if(mUri.getValue() != null) {
            mPostRepository.UPDATE_POSTING(mPosting.getValue(), mPosting.getValue().getImageKey());
            mPostRepository.UPLOAD_NEW_POSTING_IMAGE(mUri.getValue(), mPosting.getValue().getImageKey());
        }
    }

    public LiveData<Posting> getPosting(){
        if(mPosting == null){
            Log.d(TAG, "getPosting: mPosting is null");
            mPosting = mPostRepository.GET_POSTING("-MAXPlVPc8YOtyR-DGx_");
            if(mPosting != null)
                Log.d(TAG, "getPosting: mPosting is not null anymore");
        }
        return mPosting;
    }

    public LiveData<Bitmap> getBitmap(){
        if(mBitmap == null){
            Log.d(TAG, "getBitmap: mBitmap is null");
            mBitmap = mPostRepository.GET_POSTING_IMAGE("-MAXPlVPc8YOtyR-DGx_");
        }
        return mBitmap;
    }

    public void setBitmap(Bitmap bm){
        mBitmap.postValue(bm);
    }

    public LiveData<Uri> getUri(){
        if(mUri == null){
            Log.d(TAG, "getUri: mUri is null");
            mUri = new MutableLiveData<Uri>();
        }
        return mUri;
    }

    public void setUri(Uri uri){
        mUri.postValue(uri);
    }
}

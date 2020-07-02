package com.example.bceats20.post;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.net.Uri;
import android.util.Log;

import com.example.bceats20.R;
import com.example.bceats20.model.Posting;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

public class CreatePostViewModel extends AndroidViewModel {
    private static final String TAG = "CreatePostingViewModel";

    private PostRepository mPostRepository;
    private MutableLiveData<Bitmap> mBitmap;
    private MutableLiveData<Uri> mUri;
    SharedPreferences sharedPreferences;

    //Constructor
    public CreatePostViewModel(Application application) {
        super(application);
        mPostRepository = new PostRepository();
        sharedPreferences = getApplication().getSharedPreferences(getApplication().getString(R.string.shared_preferences_file_name), Context.MODE_PRIVATE);
    }

    public void setPosting(String title, String building, String room, String timeLimit, String description, String phoneNumber){
        Posting posting = new Posting();
        posting.setTitle(title);
        posting.setBuilding(building);
        posting.setRoom(room);
        posting.setTimeLimit(timeLimit);
        posting.setDescription(description);
        posting.setPhone(phoneNumber);

        if(phoneNumber != null) {
            String mImageKey = mPostRepository.UPLOAD_NEW_POSTING(posting);
            if(mUri.getValue() != null) {
                mPostRepository.UPLOAD_NEW_POSTING_IMAGE(mUri.getValue(), mImageKey);
            }else{
                Log.d(TAG, "setPosting: the uri is null wtf");
            }
        }else{
            Log.d(TAG, "Phone number is null");
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

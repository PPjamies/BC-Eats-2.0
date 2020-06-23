package com.example.bceats20.model;

import android.graphics.Bitmap;
import android.net.Uri;

import androidx.lifecycle.MutableLiveData;

public class BitmapUriWrapper {
    private MutableLiveData<Bitmap> mBitmap;
    private MutableLiveData<Uri> mUri;

    public MutableLiveData<Bitmap> getBitmap() {
        return mBitmap;
    }

    public MutableLiveData<Uri> getUri() {
        return mUri;
    }

    public void setBitmap(MutableLiveData<Bitmap> bitmap) {
        mBitmap = bitmap;
    }

    public void setUri(MutableLiveData<Uri> uri) {
        mUri = uri;
    }

    public BitmapUriWrapper(MutableLiveData<Bitmap> bitmap, MutableLiveData<Uri> uri){
        mBitmap = bitmap;
        mUri = uri;
    }
    public BitmapUriWrapper(){}

}

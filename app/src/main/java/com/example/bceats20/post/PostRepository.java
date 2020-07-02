package com.example.bceats20.post;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.util.Log;

import com.example.bceats20.model.Posting;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;

public class PostRepository {
    private static final String TAG = "Create_Post_Repository";
    
    //Firebase db + storage
    private DatabaseReference myDatabaseRef;
    private StorageReference mStorageRef;

    //constructor
    public PostRepository() {
        myDatabaseRef = FirebaseDatabase.getInstance().getReference();
        mStorageRef = FirebaseStorage.getInstance().getReference();
    }


        private String getDate() {
            SimpleDateFormat dateFormat = new SimpleDateFormat("MMM dd, yyyy", Locale.getDefault());
            String date = dateFormat.format(new Date());
            return date.toString();
        }


    //checks if there are active postings for today's date
    private MutableLiveData<Boolean> isTrue = new MutableLiveData<>();
    public MutableLiveData<Boolean> HAS_ACTIVE_POSTINGS(){
        isTrue.setValue(true);
        myDatabaseRef
                .child("posts")
                .child(getDate())
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        if(!dataSnapshot.exists()){
                            isTrue.postValue(false);
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });

        return isTrue;
    }

    //uploads new posting to firebase
    public String UPLOAD_NEW_POSTING(final Posting posting) {
        String mImageKey = myDatabaseRef.child(getDate()).push().getKey(); //generate new key
        posting.setImageKey(mImageKey); //store key into posting object
        myDatabaseRef
                .child("posts")
                .child(getDate())
                .child(mImageKey)
                .setValue(posting); //save object to database
        return mImageKey;
    }

    //overwrites the current listing in the branch
    public void UPDATE_POSTING(final Posting mUpdatedPosting, @NonNull final String mKey) {
        Log.d(TAG, "UPDATE_POSTING: is this method being called?");
        myDatabaseRef
                .child("posts")
                .child(getDate())
                .child(mKey)
                .setValue(mUpdatedPosting);
    }

    //gets the posting of key
    public MutableLiveData<Posting> GET_POSTING(@NonNull final String mKey){
        MutableLiveData<Posting> posting = new MutableLiveData<>();
        myDatabaseRef
                .child("posts")
                .child(getDate())
                .child(mKey)
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        posting.postValue(dataSnapshot.getValue(Posting.class));
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {
                    }
                });
        return posting;
    }

    private MutableLiveData<Bitmap> mBitmap = new MutableLiveData<>();
    public MutableLiveData<Bitmap> GET_POSTING_IMAGE(@NonNull final String mImageKey){

        final String mStoragePathReference = new StringBuilder("images/").append(mImageKey).append(".jpg").toString();
        StorageReference ref = mStorageRef.child(mStoragePathReference);
        try {
            final File localFile = File.createTempFile("images", "jpg");
            ref.getFile(localFile).addOnSuccessListener(taskSnapshot ->
                    mBitmap.postValue(BitmapFactory.decodeFile(localFile.getAbsolutePath())))
                    .addOnFailureListener(e -> {
            });
        } catch (IOException e) {
            e.printStackTrace();
        }
        return mBitmap;
    }

    //upload new/updated posting image to firestore
    //returns the storage path reference
    public String UPLOAD_NEW_POSTING_IMAGE(@NonNull final Uri mUri, @NonNull final String mImageKey){

        final String mStoragePathReference = new StringBuilder("images/").append(mImageKey).append(".jpg").toString();

        StorageReference imgRef = mStorageRef.child(mStoragePathReference);
        imgRef.putFile(mUri)
                .addOnSuccessListener(taskSnapshot -> Log.d(TAG, "onSuccess: successfully uploaded new posting image"))
                .addOnFailureListener(exception -> Log.d(TAG, "onFailure: unable to upload new posting image"));
        return mStoragePathReference;
    }

}

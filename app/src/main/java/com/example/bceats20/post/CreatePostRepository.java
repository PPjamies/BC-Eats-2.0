package com.example.bceats20.post;

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
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import androidx.annotation.NonNull;

public class CreatePostRepository {
    private static final String TAG = "Create_Post_Repository";
    
    //Firebase db
    private DatabaseReference myDatabaseRef;

    //Firebase storage
    private StorageReference mStorageRef;

    //constructor
    public CreatePostRepository() {
        myDatabaseRef = FirebaseDatabase.getInstance().getReference();
        mStorageRef = FirebaseStorage.getInstance().getReference();
    }


        private String getDate() {
            SimpleDateFormat dateFormat = new SimpleDateFormat("MMM dd, yyyy", Locale.getDefault());
            String date = dateFormat.format(new Date());
            return date.toString();
        }


    //checks if there are active postings for today's date
    private int isTrue = 0;
    public boolean HAS_ACTIVE_POSTINGS(){
        myDatabaseRef
                .child(getDate())
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        if(!dataSnapshot.exists()){
                            isTrue = 1;
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
        if(isTrue == 0){
            return true;
        }
        return false;
    }

    //uploads new posting to firebase
    //returns the image key
    public String UPLOAD_NEW_POSTING(final Posting posting) {
        String mImageKey = myDatabaseRef.child(getDate()).push().getKey();
        posting.setImageKey(mImageKey);
        myDatabaseRef.child(getDate()).child(mImageKey).setValue(posting);
        return mImageKey;
    }

    //upload new posting image to firestore
    //returns the storage path reference
    public String UPLOAD_NEW_POSTING_IMAGE(@NonNull final Uri mUri, @NonNull final String mImageKey){

        final String mStoragePathReference = new StringBuilder("images/").append(mImageKey).append(".jpg").toString();

        StorageReference imgRef = mStorageRef.child(mStoragePathReference);
        imgRef.putFile(mUri)
                .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        //TBD
                        Log.d(TAG, "onSuccess: successfully uploaded new posting image");
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception exception) {
                        Log.d(TAG, "onFailure: unable to upload new posting image");
                    }
                });
        return mStoragePathReference;
    }


    //@TODO: Program this later
    public void GET_POSTING(){

    }

    public void GET_POSTING_IMAGE(){
        //StorageReference pathReference = mStorageRef.child("images/stars.jpg");
    }

}

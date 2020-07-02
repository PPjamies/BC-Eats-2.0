package com.example.bceats20.ui.listings;

import android.util.Log;

import com.example.bceats20.model.Posting;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;

public class ListingsRepository {
    private static final String TAG = "ListingRepository";

    //Firebase db + storage
    private DatabaseReference myDatabaseRef;
    private StorageReference mStorageRef;

    public ListingsRepository(){
        myDatabaseRef = FirebaseDatabase.getInstance().getReference();
        mStorageRef = FirebaseStorage.getInstance().getReference();
    }

    private String getDate(){
        SimpleDateFormat dateFormat = new SimpleDateFormat("MMM dd, yyyy", Locale.US);
        Date date = new Date();
        return dateFormat.format(date);
    }

    //checks if there are active postings for today's date
    private MutableLiveData<Boolean> isTrue = new MutableLiveData<>();
    public MutableLiveData<Boolean> USER_HAS_ACTIVE_POSTINGS(@NonNull final String phoneNumber){
        isTrue.setValue(false);
        myDatabaseRef
                .child("posts")
                .child(getDate())
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                            Posting posting = snapshot.getValue(Posting.class);
                            if (posting != null && posting.getPhone().equals(phoneNumber)) {
                                isTrue.postValue(true);
                            }
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
        return isTrue;
    }

    private MutableLiveData<ArrayList<Posting>> mListOfPostings = new MutableLiveData<>();
    public MutableLiveData<ArrayList<Posting>> GET_USER_ACTIVE_POSTINGS(@NonNull final String phoneNumber){
        myDatabaseRef
                .child("posts")
                .child(getDate())
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        ArrayList<Posting> temp = new ArrayList<>();
                        for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                            Posting post = snapshot.getValue(Posting.class);
                            if(post != null && post.getPhone().equals(phoneNumber))
                                temp.add(post);
                        }
                        mListOfPostings.postValue(temp);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
        return mListOfPostings;
    }

    public void DELETE_POSTING(@NonNull final String key){
        //removes from firebase
        myDatabaseRef
                .child("posts")
                .child(getDate())
                .child(key)
                .removeValue();

        //removes image from storage
        final String mStoragePathReference = new StringBuilder("images/").append(key).append(".jpg").toString();
        StorageReference imgRef = mStorageRef.child(mStoragePathReference);
        imgRef.delete().addOnSuccessListener(aVoid -> {
            Log.d(TAG, "onSuccess: deleted file");
        }).addOnFailureListener(exception -> {
            Log.d(TAG, "onFailure: did not delete file");
        });
    }
}

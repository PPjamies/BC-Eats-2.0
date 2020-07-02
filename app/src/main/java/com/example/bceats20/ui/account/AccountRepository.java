package com.example.bceats20.ui.account;

import android.util.Log;

import com.example.bceats20.model.Posting;
import com.example.bceats20.model.User;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;

public class AccountRepository {
    private static final String TAG = "AccountRepository";

    //Firebase db + storage
    private DatabaseReference myDatabaseRef;
    private StorageReference mStorageRef;

    //constructor
    public AccountRepository() {
        myDatabaseRef = FirebaseDatabase.getInstance().getReference();
        mStorageRef = FirebaseStorage.getInstance().getReference();
    }

    private String getDate() {
        SimpleDateFormat dateFormat = new SimpleDateFormat("MMM dd, yyyy", Locale.getDefault());
        String date = dateFormat.format(new Date());
        return date.toString();
    }

    //gets the user given phonenumber
    private MutableLiveData<User> user = new MutableLiveData<>();
    public MutableLiveData<User> GET_USER(@NonNull final String mPhoneNumber){
        myDatabaseRef
                .child("users")
                .child(mPhoneNumber)
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        Log.d(TAG, "onDataChange: success " + dataSnapshot.getValue(User.class));
                        user.postValue(dataSnapshot.getValue(User.class));
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {
                        Log.d(TAG, "onCancelled: unable to get user from db");
                    }
                });
        return user;
    }

    //updates phonenumber
    //overwrites the current listing in the branch
    private MutableLiveData<User> mUser = new MutableLiveData<>();
    private String mOldNumber = "";
    public void UPDATE_USER(@NonNull final String mNewNumber, @NonNull MutableLiveData<User> mUser) {

        User unpackUser = mUser.getValue();

        if(unpackUser.getPhone() != null) {
            mOldNumber = unpackUser.getPhone();
        }

        //set new number
        unpackUser.setPhone(mNewNumber);

        //push new number to new node
        myDatabaseRef.child("users").child(mNewNumber).setValue(unpackUser);

        //update listings with new phone
        myDatabaseRef
                .child("posts")
                .child(getDate())
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                            String key = snapshot.getKey();
                            Posting posting = snapshot.getValue(Posting.class);
                            if (posting.getPhone() != null && posting.getPhone().equals(mOldNumber)) {
                                posting.setPhone(mNewNumber);
                                myDatabaseRef.child(getDate()).child(key).setValue(posting);
                            }
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {
                    }
                });

        //delete old number
        myDatabaseRef
                .child("users")
                .child(mOldNumber)
                .removeValue();
    }
}

package com.example.bceats20.pj_login.authentication;

import android.util.Log;

import com.example.bceats20.model.User;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;

public class AuthRepository {
    private static final String TAG = "AuthRepository";
    private FirebaseAuth mAuth;
    private DatabaseReference mDatabaseRef;

    public AuthRepository(){
        mAuth = FirebaseAuth.getInstance();
        mDatabaseRef = FirebaseDatabase.getInstance().getReference();
    }

    MutableLiveData<User> firebaseSignInWithPhoneCredential(PhoneAuthCredential credential, @NonNull String phone) {
        MutableLiveData<User> authenticatedUserMutableLiveData = new MutableLiveData<>();
        mAuth.signInWithCredential(credential).addOnCompleteListener(authTask ->{
            if (authTask.isSuccessful()) {
                boolean isNewUser = authTask.getResult().getAdditionalUserInfo().isNewUser();

                //create new user
                User user = new User();
                user.setPhone(phone);
                user.isNewUser = isNewUser;

                //return new user with boolean value that states whether they are a new user or returning
                authenticatedUserMutableLiveData.setValue(user);

            } else {
                Log.d(TAG, "signInWithPhoneCredential: " + authTask.getException().getMessage());
            }
        });
        return authenticatedUserMutableLiveData;
    }

    MutableLiveData<User> createUserInFirebaseIfNotExists(User authenticatedUser) {
        MutableLiveData<User> newUserMutableLiveData = new MutableLiveData<>();
        mDatabaseRef
                .child("users")
                .child(authenticatedUser.getPhone())
                .setValue(authenticatedUser);

        authenticatedUser.isCreated = true;
        newUserMutableLiveData.setValue(authenticatedUser);
        return newUserMutableLiveData;
    }
}

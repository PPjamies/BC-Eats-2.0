package com.example.bceats20.pj_login.authentication;

import android.app.Application;
import android.util.Log;

import com.example.bceats20.model.User;
import com.google.firebase.auth.PhoneAuthCredential;

import java.util.Objects;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

public class AuthViewModel extends AndroidViewModel {
    private static final String TAG = "AuthViewModel";
    private AuthRepository authRepository;
    public LiveData<User> authenticatedUserLiveData;
    public LiveData<User> createdUserLiveData;

    public AuthViewModel(Application application) {
        super(application);
        authRepository = new AuthRepository();
    }

    public void SignInWithPhone(PhoneAuthCredential phoneAuthCredential, String phone){
        if(authenticatedUserLiveData == null){
            authenticatedUserLiveData = new MutableLiveData<>();
        }
        authenticatedUserLiveData = authRepository.firebaseSignInWithPhoneCredential(phoneAuthCredential, phone);
    }

    public LiveData<User> signInWithPhoneResponse(){
        return authenticatedUserLiveData;
    }

    public void createUser(User authenticatedUser) {
        if(createdUserLiveData == null){
            createdUserLiveData = new MutableLiveData<>();
        }
        createdUserLiveData = authRepository.createUserInFirebaseIfNotExists(authenticatedUser);
    }
}
package com.example.bceats20.registration;

import android.content.Intent;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.TaskExecutors;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.InstanceIdResult;

import java.util.concurrent.Executor;
import java.util.concurrent.TimeUnit;

public class RegistrationRepository {
    private LiveData<UserRegData> userRegData;
    private ProgressBar progressBar;

    //Finals
    private static final String TAG = "PhoneAuthActivity";

    //Firebase variables
    private FirebaseAuth mAuth;
    private DatabaseReference mDatabase;
    private String verificationID;
    private boolean mVerificationInProgress = false;
    private PhoneAuthProvider.ForceResendingToken mResendToken;


    public void sendVerificationCode(String number){
        Log.d(TAG, "sendVerificationCode: sendVerificationCode() called in RegistrationRepo");
//        progressBar.setVisibility(View.VISIBLE);
        PhoneAuthProvider.getInstance().verifyPhoneNumber(
//                number,
                //TEMPORARY testing number
                //Todo: change this before production
                "+1 650-555-3434",
                60,
                TimeUnit.SECONDS,
                TaskExecutors.MAIN_THREAD,
                mCallBack
        );
        mVerificationInProgress = true;
    }

    private PhoneAuthProvider.OnVerificationStateChangedCallbacks
            mCallBack = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
        @Override
        public void onCodeSent(String verificationId, PhoneAuthProvider.ForceResendingToken forceResendingToken) {
            Log.d(TAG, "onCodeSent: s="+verificationId);
            super.onCodeSent(verificationId, forceResendingToken);
            verificationID = verificationId;
            mResendToken = forceResendingToken;
        }

        @Override
        public void onVerificationCompleted(PhoneAuthCredential phoneAuthCredential) {
            Log.d(TAG, "onVerificationCompleted: " + phoneAuthCredential.getSmsCode());
            mVerificationInProgress = false;

            //TEMPORARY verification code
            //Todo: change this before production
            String code = "123456";
//            String code = phoneAuthCredential.getSmsCode();

            if(code != null) {
                verifyCode(code);
            }
            signInWithCredential(phoneAuthCredential);
        }

        @Override
        public void onVerificationFailed(FirebaseException e) {
            Log.d(TAG, "onVerificationFailed: "+e.toString());

            mVerificationInProgress = false;

            if(e instanceof FirebaseAuthInvalidCredentialsException){

            }
        }
    };

    private void verifyCode(String code){
        Log.d(TAG, "verifyCode: ");
        PhoneAuthCredential credential = PhoneAuthProvider.getCredential(verificationID, code);
        signInWithCredential(credential);
    }

    private void signInWithCredential(PhoneAuthCredential credential){
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener((Executor) RegistrationRepository.this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()){
                            startMain();
                            //Todo: add success function to move to main app
                        } else{
                            Log.d(TAG, "signInWithCredential failed ");
                            //TODO add error check
                        }
                    }
                });
    }

    private void startMain(){
        Log.d(TAG, "startMain: Success");
//        FirebaseInstanceId.getInstance().getInstanceId()
//                .addOnCompleteListener(new OnCompleteListener<InstanceIdResult>() {
//                    @Override
//                    public void onComplete(@NonNull Task<InstanceIdResult> task) {
//                        if (!task.isSuccessful()) {
//                            Log.w(TAG, "getInstanceId failed", task.getException());
//                            return;
//                        }
//
//                        // Get new Instance ID token
//                        String token = task.getResult().getToken();
//
//                        // Log and toast
//                        //Log.d(TAG, "Token: " + token );
//                        //Toast.makeText(PhoneAuthenticationActivity.this, "Token: " + token, Toast.LENGTH_SHORT).show();
//                    }
//                });
//
//        Intent intent = new Intent(RegistrationRepository.this, MainActivity.class);
//        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
//        startActivity(intent);
    }


    public void uploadUserToFirebaseDatabase(String phonenumber){
        Log.d(TAG, "uploadUserToFirebaseDatabase: ");
        mDatabase = FirebaseDatabase.getInstance().getReference();
        mDatabase.child("users").child(phonenumber).child("phone").setValue(phonenumber);

    }

}

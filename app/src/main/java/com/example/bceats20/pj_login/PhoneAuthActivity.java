package com.example.bceats20.pj_login;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.bceats20.MainActivity;
import com.example.bceats20.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.TaskExecutors;
import com.google.firebase.FirebaseException;
import com.google.firebase.FirebaseTooManyRequestsException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.InstanceIdResult;

import java.util.concurrent.TimeUnit;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

public class PhoneAuthActivity extends AppCompatActivity{
    private static final String TAG = "PhoneAuthActivity";

    //tracks whether verification is in process and number is valid
    private static final String KEY_VERIFY_IN_PROGRESS = "key_verify_in_progress";
    private boolean mVerificationInProgress = false;


    private static Context sContext;

    private FirebaseAuth mAuth;
    private String mPhone;
    private String mVerificationID;
    private PhoneAuthProvider.ForceResendingToken mResendToken;



    private ProgressBar mProgressBar;
    private EditText mEditText;
    private Button mButtonSignIn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.pj_activity_verify_phone);

        if (savedInstanceState != null) {
            onRestoreInstanceState(savedInstanceState);
        }

        mAuth = FirebaseAuth.getInstance();
        mPhone = getIntent().getStringExtra("mPhone");
        sContext = this;

        mProgressBar = (ProgressBar) findViewById(R.id.progressbar);
        mEditText = (EditText) findViewById(R.id.editTextCode);
        mButtonSignIn = (Button)findViewById(R.id.buttonSignIn);

        startPhoneVerification(mPhone);

        mButtonSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String code = mEditText.getText().toString().trim();

                if (code.isEmpty() || code.length() < 6) {
                    mEditText.setError("Enter code...");
                    mEditText.requestFocus();
                    return;
                }
                verifyPhoneNumberWithCode(code);
            }
        });
    }

/* Restores instance states to resume the phone number sign in process if your app closes before the user can sign in*/
    //if verification is already in progress call verifyPhoneNumber again.
    @Override
    protected void onStart() {
        super.onStart();
        if(FirebaseAuth.getInstance().getCurrentUser() != null){
            Log.d(TAG, "onStart: is already a user, proceed to main menu");
            startMain();
        }
        if (mVerificationInProgress) {
            startPhoneVerification(mPhone); //restarts the verification process
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putBoolean(KEY_VERIFY_IN_PROGRESS, mVerificationInProgress);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        mVerificationInProgress = savedInstanceState.getBoolean(KEY_VERIFY_IN_PROGRESS);
    }


/* Ask firebase to verify user's phone number */
    private void startPhoneVerification(String number) {
        mProgressBar.setVisibility(View.VISIBLE);
        PhoneAuthProvider.getInstance().verifyPhoneNumber(
                number,
                60,
                TimeUnit.SECONDS,
                TaskExecutors.MAIN_THREAD,
                mCallBack
        );
        mVerificationInProgress = true;
    }

/*callback function to the startPhoneVerification method above
 *these callbacks will let you know whether verification succeeded or failed
 */
    private PhoneAuthProvider.OnVerificationStateChangedCallbacks
            mCallBack = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {

        @Override
        public void onCodeSent(@NonNull String verificationId,
                               @NonNull PhoneAuthProvider.ForceResendingToken token) {
            // The SMS verification code has been sent to the provided phone number, we
            // now need to ask the user to enter the code and then construct a credential
            // by combining the code with a verification ID.
            mVerificationID = verificationId;
            mResendToken = token;
        }

        @Override
        public void onVerificationCompleted(PhoneAuthCredential credential) {
            // This callback will be invoked in two situations:
            // 1 - Instant verification. In some cases the phone number can be instantly
            //     verified without needing to send or enter a verification code.
            // 2 - Auto-retrieval. On some devices Google Play services can automatically
            //     detect the incoming verification SMS and perform verification without
            //     user action (automatic retrieval).
            mVerificationInProgress = false;
            signInWithCredential(credential);
        }

        @Override
        public void onVerificationFailed(FirebaseException e) {
            mVerificationInProgress = false;
            if (e instanceof FirebaseAuthInvalidCredentialsException) {
                Log.e(TAG,e.getMessage());
            } else if (e instanceof FirebaseTooManyRequestsException) {
                Log.e(TAG,e.getMessage());
            }
        }

    };

    //this method takes care of the situations in which
    //google play doesn't automatically handle your sms code (non-automatic retrieval)
    private void verifyPhoneNumberWithCode(String code) {
        PhoneAuthCredential credential = PhoneAuthProvider.getCredential(mVerificationID, code);
        signInWithCredential(credential); //we dont want to sign directly in
    }

    //This is the part that goes into repository for checking
    private void signInWithCredential(PhoneAuthCredential credential) {
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(PhoneAuthActivity.this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {

                            FirebaseUser user = task.getResult().getUser();

                            //launch next activity
                            startMain();
                        } else {
                            Log.d(TAG, task.getException().getMessage());
                            if (task.getException() instanceof FirebaseAuthInvalidCredentialsException) {
                                Log.d(TAG, task.getException().getMessage());
                            }
                        }
                    }
                });
    }



    protected void startMain(){
        // [START retrieve_current_token]
        FirebaseInstanceId.getInstance().getInstanceId()
                .addOnCompleteListener(new OnCompleteListener<InstanceIdResult>() {
                    @Override
                    public void onComplete(@NonNull Task<InstanceIdResult> task) {
                        if (!task.isSuccessful()) {
                            Log.w(TAG, "getInstanceId failed", task.getException());
                            return;
                        }

                        // Get new Instance ID token
                        String token = task.getResult().getToken();

                        // Log and toast
                        Log.d(TAG, "Token: " + token );
                        Toast.makeText(PhoneAuthActivity.this, "Token: " + token, Toast.LENGTH_SHORT).show();
                    }
                });
        // [END retrieve_current_token]

        Intent intent = new Intent(PhoneAuthActivity.this, MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
    }
}

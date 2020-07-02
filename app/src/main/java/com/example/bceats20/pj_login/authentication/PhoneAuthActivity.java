package com.example.bceats20.pj_login.authentication;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;

import com.example.bceats20.MainActivity;
import com.example.bceats20.R;
import com.google.android.gms.tasks.TaskExecutors;
import com.google.firebase.FirebaseException;
import com.google.firebase.FirebaseTooManyRequestsException;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;
import com.google.firebase.iid.FirebaseInstanceId;

import java.util.concurrent.TimeUnit;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProviders;

public class PhoneAuthActivity extends AppCompatActivity{
    private static final String TAG = "PhoneAuthActivity";
    private static Context sContext;

    //tracks whether verification is in process and number is valid
    private static final String KEY_VERIFY_IN_PROGRESS = "key_verify_in_progress";
    private boolean mVerificationInProgress = false;

    //view model
    private AuthViewModel  mAuthViewModel;

    //authentication
    private FirebaseAuth mAuth;
    private String mPhone;
    private String mVerificationID;
    private PhoneAuthProvider.ForceResendingToken mResendToken;

    //widgets
    private ProgressBar mProgressBar;
    private EditText mEditText;
    private Button mButtonSignIn;

    //SharedPreferences system saving
    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.pj_activity_verify_phone);
        sContext = this;

        if (savedInstanceState != null) {
            onRestoreInstanceState(savedInstanceState);
        }

        initialize();
        startPhoneVerification(mPhone);

        mButtonSignIn.setOnClickListener(v -> {
            String code = mEditText.getText().toString().trim();

            if (code.isEmpty() || code.length() < 6) {
                mEditText.setError("Enter code...");
                mEditText.requestFocus();
                return;
            }
            verifyPhoneNumberWithCode(code);
        });
    }

    private void initialize(){
        mAuthViewModel = ViewModelProviders.of(this).get(AuthViewModel.class);
        mAuth = FirebaseAuth.getInstance();
        mPhone = getIntent().getStringExtra("mPhone");
        mProgressBar = (ProgressBar) findViewById(R.id.progressbar);
        mEditText = (EditText) findViewById(R.id.editTextCode);
        mButtonSignIn = (Button)findViewById(R.id.buttonSignIn);
        sharedPreferences = this.getSharedPreferences("user", Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();
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
        signInWithCredential(credential);
    }

    //This is the part that goes into repository for checking
    private void signInWithCredential(PhoneAuthCredential credential) {
       mAuthViewModel.SignInWithPhone(credential, mPhone);
       mAuthViewModel.signInWithPhoneResponse().observe(this, user -> {
           if(user.isNewUser) {
               mAuthViewModel.createUser(user);
           }
           startMain();
       });
    }



    protected void startMain(){
        FirebaseInstanceId.getInstance().getInstanceId()
                .addOnCompleteListener(task -> {
                    if (!task.isSuccessful()) {
                        Log.w(TAG, "getInstanceId failed", task.getException());
                        return;
                    }

                    // Get new push token and save to db
                    String token = task.getResult().getToken();
                    mAuthViewModel.saveToken(token, mPhone);
                });


        //Save user phone number to sharedpreferences so other activities can use it
        editor.putString(getString(R.string.shared_preferences_file_name), mPhone);
        editor.commit();

        Intent intent = new Intent(PhoneAuthActivity.this, MainActivity.class);
        intent.putExtra("phonenumber",mPhone);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
    }
}

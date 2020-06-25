package com.example.bceats20.registration;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProviders;

import android.content.Context;
import android.os.Bundle;
import android.telephony.PhoneNumberUtils;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.bceats20.R;

public class RegistrationActivity extends AppCompatActivity {

    private final String TAG = "RegistrationActivity";
    private Context mContext;

    //ViewModel
    RegistrationViewModel mViewModel;

    //Widgets
    private EditText mUsername, mAreaCode, mPhonenumber, mConfirmEntry;
    private TextView mUsernameTag, mPhonenumberTag, mConfirmTag;
    private Button mSubmitBtn, mConfirmBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.d(TAG, "onCreate: Created RegistrationActivity");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);
        mContext = this;
        
        //Initialize widgets
        mUsername = (EditText) findViewById(R.id.registrationUsernameEntry);
        mAreaCode = (EditText) findViewById(R.id.registrationPhoneAreaCode);
        mPhonenumber = (EditText) findViewById(R.id.registrationPhoneNumber);
        mConfirmEntry = (EditText) findViewById(R.id.registrationConfirmEntry);

        //Add TextListeners
        mUsername.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                mViewModel.setUsername(s.toString());
                Log.d(TAG, "onTextChanged: LiveData value is: "+mViewModel.getUsername().getValue());
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        mAreaCode.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                mViewModel.setAreacode(s.toString());
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        mPhonenumber.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                mViewModel.setPhonenumber(s.toString());
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        mConfirmEntry.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                mViewModel.setConfirmationCode(s.toString());
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        mUsernameTag = (TextView) findViewById(R.id.registrationUsernameTag);
        mPhonenumberTag = (TextView) findViewById(R.id.registrationPhoneTag);
        mConfirmTag = (TextView) findViewById(R.id.registrationConfirmTag);
        
        mSubmitBtn = (Button) findViewById(R.id.registrationSubmitButton);
        mSubmitBtn.setOnClickListener((new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                Log.d(TAG, "onClick: Button clicked");
                loginUser();
            }
        }));
        
        mConfirmBtn = (Button) findViewById(R.id.registrationConfirmButton);
        mConfirmBtn.setOnClickListener((new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                confirmUser();
            }
        }));

        //ViewModel initialization
        mViewModel = ViewModelProviders.of(this).get(RegistrationViewModel.class);

    }

    private void loginUser() {

        Toast.makeText(this, "Trying to log in",Toast.LENGTH_LONG);

        String phonenumber = mAreaCode.getText().toString().trim().concat(mPhonenumber.getText().toString().trim());
        String username = mUsername.getText().toString().trim();

        Log.d(TAG, "loginUser: Phonenumber "+phonenumber+" Username "+username);

        //Check if entered number is valid
        if(!PhoneNumberUtils.isGlobalPhoneNumber(phonenumber)){
            Log.d(TAG, "loginUser: PhoneNumberEntry failed");
            Toast.makeText(this,"Please enter a valid phone number",Toast.LENGTH_LONG);
            return;
        }
        //Check for empty username field
        if (username.compareTo("")==0){
            Log.d(TAG, "loginUser: empty username");
            Toast.makeText(this,"Please enter a username",Toast.LENGTH_LONG);
            return;
        }

        Log.d(TAG, "loginUser: Succesful Number");
        mViewModel.setPhonenumber(phonenumber);
        mViewModel.setUsername(username);
        mViewModel.phoneAuth();
    }

    private void confirmUser() {
        Log.d(TAG, "confirmUser: Bottom button clicked");
        mViewModel.addUserToDatabase();
    }
}
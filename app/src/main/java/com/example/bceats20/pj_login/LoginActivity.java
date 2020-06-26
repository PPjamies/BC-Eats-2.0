package com.example.bceats20.pj_login;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.example.bceats20.R;

public class LoginActivity extends AppCompatActivity {
    private final static String TAG = "LoginActivity";
    public static Context mContext;

    private EditText mPhoneNumber_EditText;
    private Button mLoginButton;

    private AuthViewModel mAuthViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.pj_activity_login);
        mContext = this;

        initGetPhoneEditText();
        initSignInButtons();
        initAuthViewModel();
        //initGoogleSignInClient();
    }

    private void initGetPhoneEditText(){
        mPhoneNumber_EditText = (EditText) findViewById(R.id.phone_et);
    }

    private void initSignInButtons(){
        //Sign in by phone
        mLoginButton = (Button)findViewById(R.id.login_bt);
        mLoginButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                String phone = mPhoneNumber_EditText.getText().toString().trim();
                if(phone.isEmpty())
                {
                    mPhoneNumber_EditText.setError("please enter phone number");
                    mPhoneNumber_EditText.requestFocus();
                }
                else
                {
                    Intent intent = new Intent(mContext,PhoneAuthActivity.class);
                    intent.putExtra("mPhone", phone);
                    startActivity(intent);
                }
            }
        });

        //Sign in by Google

    }

    private void initAuthViewModel() {
        mAuthViewModel = new ViewModelProvider(this).get(AuthViewModel.class);
    }
}

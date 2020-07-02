package com.example.bceats20.pj_login;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;

import com.example.bceats20.R;
import com.example.bceats20.pj_login.authentication.PhoneAuthActivity;

public class LoginActivity extends AppCompatActivity{
    private final static String TAG = "LoginActivity";
    public static Context mContext;

    //widgets
    private EditText mPhoneNumber_EditText;
    private Button mLoginButton;

    //SharedPreferences saved data
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.pj_activity_login);
        mContext = this;
        initSharedPrefernces();

        initGetPhoneEditText();
        initSignInButtons();
    }

    private void initSharedPrefernces() {
        sharedPreferences = this.getSharedPreferences("user", Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();
    }

    private void initGetPhoneEditText(){
        mPhoneNumber_EditText = (EditText) findViewById(R.id.phone_et);
    }

    private void initSignInButtons(){
        //Sign in by phone
        mLoginButton = (Button)findViewById(R.id.login_bt);
        mLoginButton.setOnClickListener(v -> {
            String phone = mPhoneNumber_EditText.getText().toString().trim();
            if(phone.isEmpty())
            {
                mPhoneNumber_EditText.setError("please enter phone number");
                mPhoneNumber_EditText.requestFocus();
            }
            else
            {
                editor.putString(getString(R.string.shared_preferences_file_name), phone);
                Intent intent = new Intent(mContext, PhoneAuthActivity.class);
                intent.putExtra("mPhone", phone);
                startActivity(intent);
            }
        });
    }
}

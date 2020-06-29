package com.example.bceats20.ui.account;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.drawable.DrawableCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.example.bceats20.R;
import com.example.bceats20.model.User;

public class AccountFragment extends Fragment {
    private final String TAG = "AccountFragment";
    private Context mContext;

    private AccountViewModel mAccountViewModel;
    private LinearLayout mLinearLayout;
    private EditText mPhoneEditText;
    private ImageButton mEditButton;
    private ImageButton mDoneButton;
    private View mView;


    private void loadPhoneNumberFromViewModel(){
        mAccountViewModel.getUser().observe(getViewLifecycleOwner(), new Observer<User>() {
            @Override
            public void onChanged(@Nullable User user) {
                mPhoneEditText.setText(user.getPhone().toString());
            }
        });
    }

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_account, container, false);
        mContext = getActivity().getApplicationContext();

        SharedPreferences sharedPreferences = mContext.getSharedPreferences(getString(R.string.shared_preferences_file_name),Context.MODE_PRIVATE);

        //Initialize widgets
        mEditButton = (ImageButton) root.findViewById(R.id.edit_phone_btn_account);
        mDoneButton = (ImageButton) root.findViewById(R.id.edit_phone_done_btn_account);
        mLinearLayout = (LinearLayout) root.findViewById(R.id.account_linear_layout);
        mPhoneEditText = (EditText) root.findViewById(R.id.text_phone_account);
        mView = (View) root.findViewById(R.id.line_account);

        //Initialize view model and fetch data
        mAccountViewModel =
                ViewModelProviders.of(this).get(AccountViewModel.class);
        loadPhoneNumberFromViewModel();

        //Program edit button
        mEditButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //make text view editable
                mPhoneEditText.setFocusable(true);
                mPhoneEditText.setFocusableInTouchMode(true);
                mPhoneEditText.setClickable(true);

                //disable and hide edit button
                mEditButton.setEnabled(false);
                mEditButton.setVisibility(View.GONE);

                //enable and show done button
                mDoneButton.setEnabled(true);
                mDoneButton.setVisibility(View.VISIBLE);

                //show line
                mView.setVisibility(View.VISIBLE);
            }
        });

        //Program done button
        mDoneButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mPhoneEditText.setBackground(null);

                //disable and hide done button
                mDoneButton.setEnabled(false);
                mDoneButton.setVisibility(View.GONE);;

                //enable and show edit button
                mEditButton.setEnabled(true);;
                mEditButton.setVisibility(View.VISIBLE);

                //hide line
                //show line
                mView.setVisibility(View.GONE);

                //send
                String newNumber = mPhoneEditText.getText().toString().trim();
//                mAccountViewModel.setNewPhoneNumber("12068888888", newNumber);
//                mAccountViewModel.setUser(new User(newNumber));
            }
        });

        //Program linear layout to become clickable
        mLinearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //reset edit text hint with original phone number
                loadPhoneNumberFromViewModel();

                //make edit text non-editable
                mPhoneEditText.setFocusable(false);
                mPhoneEditText.setFocusableInTouchMode(false);
                mPhoneEditText.setClickable(false);

                //disable and hide done button
                mDoneButton.setEnabled(false);
                mDoneButton.setVisibility(View.GONE);;

                //enable and show edit button
                mEditButton.setEnabled(true);;
                mEditButton.setVisibility(View.VISIBLE);
            }
        });

        return root;
    }
}

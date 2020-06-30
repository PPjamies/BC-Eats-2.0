package com.example.bceats20.ui.listings;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.bceats20.R;
import com.example.bceats20.model.Posting;

import java.util.ArrayList;

public class ListingsFragment extends Fragment {
    private final static String TAG = "ListingsFragment";
    //TODO: Get rid of hard coded phone number and pull from SharedPreferences

    private  String phoneNumber;
    private Context mContext;

    //view model
    private ListingsViewModel mListingsViewModel;

    //recyclerview
    private RecyclerView mRecyclerView;
    private LinearLayoutManager mLinearLayoutManager;
    private Adapter mAdapter;
    private ArrayList<Posting> mList;

    //widgets
    private TextView mHasNoPostTextView;
    private TextView mDateText;

    private void loadTextFromViewModel(){
        mListingsViewModel.getDateText().observe(getViewLifecycleOwner(), s -> mDateText.setText(s));
    }


    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_listings, container, false);
        mContext = getActivity().getApplicationContext();

        //initialize widgets
        mHasNoPostTextView = (TextView)root.findViewById(R.id.listings_no_posts_text);
        mDateText = (TextView)root.findViewById(R.id.listings_date_text);

        //Get phone number
        SharedPreferences sharedPreferences = this.getActivity().getSharedPreferences(getString(R.string.shared_preferences_file_name),Context.MODE_PRIVATE);
        phoneNumber = sharedPreferences.getString(getString(R.string.shared_preferences_file_name),null);

        //initialize view model
        mListingsViewModel = ViewModelProviders.of(this).get(ListingsViewModel.class);
        loadTextFromViewModel();
        mListingsViewModel.hasPosts(phoneNumber).observe(getViewLifecycleOwner(), aBoolean -> {
            if(aBoolean){
                mHasNoPostTextView.setVisibility(TextView.GONE);
            }else{
                mHasNoPostTextView.setVisibility(TextView.VISIBLE);
            }
        });

        mList = new ArrayList<>();
        mListingsViewModel.getListings(phoneNumber).observe(getViewLifecycleOwner(), postings -> {
            mList = postings;
            mAdapter = new Adapter(getActivity(),mList);
            mRecyclerView.setAdapter(mAdapter);
        });


        //initialize recycler view
        mRecyclerView = (RecyclerView) root.findViewById(R.id.listings_recycler_view);
        mLinearLayoutManager = new LinearLayoutManager(mContext);
        mRecyclerView.setLayoutManager(mLinearLayoutManager);

        return root;
    }
}

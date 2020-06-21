package com.example.bceats20.ui.listings;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.example.bceats20.R;

public class ListingsFragment extends Fragment {

    private ListingsViewModel mListingsViewModel;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        mListingsViewModel =
                ViewModelProviders.of(this).get(ListingsViewModel.class);

        View root = inflater.inflate(R.layout.fragment_listings, container, false);

        final TextView textView = root.findViewById(R.id.text_listings);

        mListingsViewModel.getText().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
                textView.setText(s);
            }
        });
        return root;
    }
}

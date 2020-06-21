package com.example.bceats20.ui.home;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.bceats20.R;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import com.example.bceats20.glide.GlideApp;
import com.example.bceats20.model.Posting;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

public class HomeFragment extends Fragment {

    private HomeViewModel homeViewModel;

    //firebase database
    private FirebaseDatabase database;
    private DatabaseReference myRef;
    private Query query;
    private List<Posting> mPosts;

    //recyclerview
    private RecyclerView mRecyclerView;
    private LinearLayoutManager linearLayoutManager;
    private FirebaseRecyclerAdapter mFirebaseRecyclerAdapter;


    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        homeViewModel =
                ViewModelProviders.of(this).get(HomeViewModel.class);
        View root = inflater.inflate(R.layout.fragment_home, container, false);

        //initialize view models
        final TextView dateTextView = root.findViewById(R.id.home_date_text);
        homeViewModel.getDateText().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
                dateTextView.setText(s);
            }
        });

        //initialize firebase recycler view
        initializeRecyclerView();
        fetch();

        return root;
    }


    private void initializeRecyclerView(){
        mRecyclerView = (RecyclerView)getActivity().findViewById(R.id.home_recycler_view);
        linearLayoutManager = new LinearLayoutManager(getActivity().getApplicationContext());
        mRecyclerView.setLayoutManager(linearLayoutManager);
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        TextView mTitle;
        TextView mLocation;
        TextView mTimeLimit;
        TextView mDescription;
        ImageView mImageView;
        String mKey;

        public ViewHolder(View itemView){
            super(itemView);
            mTitle = (TextView) itemView.findViewById(R.id.list_item_title);
            mLocation = (TextView) itemView.findViewById(R.id.list_item_location);
            mTimeLimit = (TextView) itemView.findViewById(R.id.list_item_time_lmt);
            mDescription = (TextView) itemView.findViewById(R.id.list_item_desc);
            mImageView = (ImageView)itemView.findViewById(R.id.list_item_img);
        }

        public void setTxtTitle(String string){
            mTitle.setText(string);
        }
        public void setTxtLocation(String string){
            mLocation.setText(string);
        }
        public void setTxtTimeLimit(String string){
            mTimeLimit.setText(string);
        }
        public void setTxtDescription(String string){
            mDescription.setText(string);
        }
        public void setKey(String string){
            mKey = string;
        }
    }

    private void fetch(){
        //get current date
        SimpleDateFormat dateFormat = new SimpleDateFormat("MM dd, yyyy", Locale.getDefault());
        String date = dateFormat.format(new Date());

        //match current date with a branch in database
        query = FirebaseDatabase.getInstance().getReference("date").equalTo(date);
//                .orderByChild("date")
//                .equalTo(date);


        FirebaseRecyclerOptions<Posting> options =
                new FirebaseRecyclerOptions.Builder<Posting>()
                        .setQuery(query, Posting.class)
                        .build();

        mFirebaseRecyclerAdapter = new FirebaseRecyclerAdapter<Posting, ViewHolder>(options) {
            @Override
            public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.list_item, parent, false);

                return new ViewHolder(view);
            }

            @Override
            protected void onBindViewHolder(ViewHolder holder, int position, Posting model) {
                StringBuilder locationSB = new StringBuilder();
                String building = model.getBuilding();
                String room = model.getRoom();
                locationSB.append("In ").append(building).append(", room ").append(room);

                holder.setTxtTitle(model.getTitle());
                holder.setTxtLocation(locationSB.toString());
                holder.setTxtTimeLimit(model.getTimeLimit());
                holder.setTxtDescription(model.getDescription());
                holder.setKey(model.getImageKey());

                getImg(holder,position,model);
            }

            private void getImg(ViewHolder holder, int position, Posting model){
                String fileName = model.getImageKey() + ".jpg";
                StorageReference imgRef = FirebaseStorage.getInstance().getReference().child("images/" + fileName);

                GlideApp.with(getContext())
                        .load(imgRef)
                        .fitCenter()
                        .into(holder.mImageView);
            }
        };
        mRecyclerView.setAdapter(mFirebaseRecyclerAdapter);
    }
}

package com.example.bceats20.ui.home;

import android.content.Context;
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
import java.util.Locale;

import com.example.bceats20.glide.GlideApp;
import com.example.bceats20.model.Posting;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

public class HomeFragment extends Fragment {
    private static final String TAG = "HomeFragment";

    private HomeViewModel mHomeViewModel;
    private Context mContext;

    //firebase database
    private Query query;

    //recyclerview
    private RecyclerView mRecyclerView;
    private LinearLayoutManager linearLayoutManager;
    private FirebaseRecyclerAdapter mFirebaseRecyclerAdapter;

    //Widgets
    private TextView mDateTextView;
    private TextView mHomeTextView;


    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mContext = getActivity().getApplicationContext();
        mHomeViewModel =
                ViewModelProviders.of(this).get(HomeViewModel.class);
        View root = inflater.inflate(R.layout.fragment_home, container, false);

        //initialize view models
        mDateTextView = root.findViewById(R.id.home_date_text);
        mHomeViewModel.getDateText().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
                mDateTextView.setText(s);
            }
        });

        mHomeTextView = root.findViewById(R.id.home_no_posts_text);
        mHomeViewModel.hasPosts().observe(getViewLifecycleOwner(), new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean aBoolean) {
                if(aBoolean == true){
                    mHomeTextView.setVisibility(TextView.GONE);
                }else{
                    mHomeTextView.setVisibility(TextView.VISIBLE);
                }
            }
        });

        //initialize firebase recycler view
        mRecyclerView = (RecyclerView) root.findViewById(R.id.home_recycler_view);
        linearLayoutManager = new LinearLayoutManager(mContext);
        mRecyclerView.setLayoutManager(linearLayoutManager);
        fetch();

        return root;
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
        SimpleDateFormat dateFormat = new SimpleDateFormat("MMM dd, yyyy", Locale.US);
        String date = dateFormat.format(new Date());


        //match current date with a branch in database
        query = FirebaseDatabase.getInstance().getReference()
                .child("posts")
                .child(date);

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

                String timeLimit = "Available until: " + model.getTimeLimit();

                holder.setTxtTitle(model.getTitle());
                holder.setTxtLocation(locationSB.toString());
                holder.setTxtTimeLimit(timeLimit);
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
        mFirebaseRecyclerAdapter.notifyDataSetChanged();
        mRecyclerView.setAdapter(mFirebaseRecyclerAdapter);
    }

    @Override
    public void onStart(){
        super.onStart();
        mFirebaseRecyclerAdapter.startListening();
    }

    @Override
    public void onStop(){
        super.onStop();
        mFirebaseRecyclerAdapter.stopListening();
    }
}

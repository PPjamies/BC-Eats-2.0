package com.example.bceats20.ui.listings;

import android.content.Context;
import android.media.Image;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.bceats20.R;
import com.example.bceats20.glide.GlideApp;
import com.example.bceats20.model.Posting;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class Adapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>{
    private Context mContext;
    private List<Posting> mList;

    public Adapter(Context context, List<Posting> listOfPostings){
        this.mList = listOfPostings;
        this.mContext = context;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.list_item_swipe_menu, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        ViewHolder viewholder = (ViewHolder) holder;
        Posting posting;
        posting = mList.get(position);


        StringBuilder locationSB = new StringBuilder();
        String building = posting.getBuilding();
        String room = posting.getRoom();
        locationSB.append("In ").append(building).append(", room ").append(room);

        String timeLimit = "Available until: " + posting.getTimeLimit();

        viewholder.setTxtTitle(posting.getTitle());
        viewholder.setTxtLocation(locationSB.toString());
        viewholder.setTxtTimeLimit(timeLimit);
        viewholder.setTxtDescription(posting.getDescription());
        viewholder.setKey(posting.getImageKey());
        getImg(viewholder,position,posting);

        viewholder.mEditBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //go to edit posting activity
                Toast.makeText(v.getContext(), "EDIT CLICKED", Toast.LENGTH_SHORT).show();
            }
        });
        viewholder.mDeleteBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //open delete alert dialog
                Toast.makeText(v.getContext(), "DELETE CLICKED", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void getImg(ViewHolder holder, int position, Posting model){
        String fileName = model.getImageKey() + ".jpg";
        StorageReference imgRef = FirebaseStorage.getInstance().getReference().child("images/" + fileName);

        GlideApp.with(mContext)
                .load(imgRef)
                .fitCenter()
                .into(holder.mImageView);
    }

    @Override
    public int getItemCount() {
        return mList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder{
        protected TextView mTitle;
        protected TextView mLocation;
        protected TextView mTimeLimit;
        protected TextView mDescription;
        protected ImageView mImageView;
        protected String mKey;
        protected ImageButton mDeleteBtn;
        protected ImageButton mEditBtn;


        public ViewHolder(View itemView){
            super(itemView);
            mTitle = (TextView) itemView.findViewById(R.id.list_item_title);
            mLocation = (TextView) itemView.findViewById(R.id.list_item_location);
            mTimeLimit = (TextView) itemView.findViewById(R.id.list_item_time_lmt);
            mDescription = (TextView) itemView.findViewById(R.id.list_item_desc);
            mImageView = (ImageView)itemView.findViewById(R.id.list_item_img);
            mDeleteBtn = (ImageButton)itemView.findViewById(R.id.delete_button);
            mEditBtn = (ImageButton)itemView.findViewById(R.id.edit_button);
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
}

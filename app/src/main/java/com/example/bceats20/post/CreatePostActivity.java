package com.example.bceats20.post;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;

import com.example.bceats20.utility.ImageUtils;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.FileProvider;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TimePicker;
import android.widget.Toast;

import com.example.bceats20.R;

import java.io.File;
import java.io.IOException;

import static android.content.Intent.FLAG_GRANT_READ_URI_PERMISSION;
import static android.content.Intent.FLAG_GRANT_WRITE_URI_PERMISSION;

public class CreatePostActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {
    private final String TAG = "CreatePostActivity";
    private Context mContext;

    //view model
    CreatePostViewModel mViewModel;

    //widgets
    private Toolbar mToolbar;
    private Spinner mSpinner;
    private FloatingActionButton mFloatingActionButton;
    private Button mPostButton;
    private Button mCancelButton;
    private EditText mTitle, mRoom, mDescription;
    private ImageView mImage;
    private TimePicker mTimeLimit;

    //Camera/Gallery intent codes
    private static final int CAMERA_REQUEST = 0;
    private static final int GALLERY_REQUEST = 1;
    private static final int PERMISSIONS_REQUEST = 123;

    //Camera/Gallery
    private File captureMediaFile;
    private Uri selectedImage;
    private byte[] bytesDocumentsTypePicture = null;

    //Select Image Dialog
    private static final CharSequence[] items = { "Take Photo", "Choose from Library", "Cancel" };




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_post);
        mContext = this;

        //initialize widgets
        mTitle = (EditText) findViewById(R.id.post_title_et);
        mRoom = (EditText) findViewById(R.id.post_room_et);
        mDescription = (EditText) findViewById(R.id.post_desc_et);
        mImage = (ImageView) findViewById(R.id.post_food_backdrop);
        mTimeLimit = (TimePicker) findViewById(R.id.post_time_picker);

        //initialize and set toolbar
        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);

        //initialize and set spinner
        mSpinner = (Spinner) findViewById(R.id.post_spinner);
        ArrayAdapter<CharSequence> spinner_adapter = ArrayAdapter.createFromResource(
                this,
                R.array.buildings_array,
                android.R.layout.simple_spinner_item);
        spinner_adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mSpinner.setAdapter(spinner_adapter);
        mSpinner.setSelection(0);

        //restore state by loading data from view model
        mViewModel = ViewModelProviders.of(this).get(CreatePostViewModel.class);
        mViewModel.getBitmap().observe(this, new Observer<Bitmap>() {
            @Override
            public void onChanged(@Nullable  Bitmap bitmap) {
                mImage.setImageBitmap(bitmap);
            }
        });
        mViewModel.getUri().observe(this, new Observer<Uri>() {
            @Override
            public void onChanged(Uri uri) {
                selectedImage = uri;
            }
        });

        //initialize and set floating action button - open camera or gallery
        mFloatingActionButton = (FloatingActionButton) findViewById(R.id.fab);
        mFloatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                selectImageDialog();
            }
        });

        //initialize and set post button
        mPostButton = (Button) findViewById(R.id.post_btn);
        mPostButton.setOnClickListener((new View.OnClickListener(){
            @Override
            public void onClick(View view){
                uploadPosting();
            }
        }));

        //initialize and set cancel button
        mCancelButton = (Button) findViewById(R.id.cancel_btn);
        mCancelButton.setOnClickListener((new View.OnClickListener(){
            @Override
            public void onClick(View view){
                close();
            }
        }));
    }


    /* BOF override spinner */
    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {
        mSpinner.setSelection(pos);
    }

    public void onNothingSelected(AdapterView<?> parent) {
        // Another interface callback
    }
    /* EOF spinner */


    /* BOF gallery/camera intent */
    private void selectImageDialog(){
        AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
        builder.setTitle("Upload a Photo!");
        builder.setItems(items, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int item) {
                if (items[item].equals("Take Photo"))
                {
                    cameraIntent();
                }
                else if (items[item].equals("Choose from Library"))
                {
                    galleryIntent();
                }
                else if (items[item].equals("Cancel"))
                {
                    dialog.dismiss();
                }
            }
        });
        builder.show();
    }

        private void cameraIntent() {
            Log.d(TAG, "camera intent launching");
            captureMediaFile = ImageUtils.getOutputMediaFile(mContext);

            if (captureMediaFile != null) {
                Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

                if (Build.VERSION.SDK_INT < Build.VERSION_CODES.N) {
                    intent.addFlags(FLAG_GRANT_READ_URI_PERMISSION | FLAG_GRANT_WRITE_URI_PERMISSION);
                    intent.putExtra(MediaStore.EXTRA_OUTPUT, captureMediaFile);
                } else {
                    Uri photoUri = FileProvider.getUriForFile(mContext, mContext.getPackageName() + ".provider", captureMediaFile);
                    intent.addFlags(FLAG_GRANT_READ_URI_PERMISSION);
                    intent.putExtra(MediaStore.EXTRA_OUTPUT, photoUri);
                }

                if (intent.resolveActivity(getPackageManager()) != null) {
                    startActivityForResult(intent, CAMERA_REQUEST);
                } else {
                    Toast.makeText(mContext, R.string.camera_unavailable, Toast.LENGTH_LONG);
                }
            } else {
                Toast.makeText(mContext, R.string.file_save_error, Toast.LENGTH_LONG);
            }
        }


        private void galleryIntent(){
            Log.d(TAG, "gallery intent launching");
            Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            startActivityForResult(intent, GALLERY_REQUEST);
        }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK && requestCode == CAMERA_REQUEST) {
            Bitmap bitmap;
            if (Build.VERSION.SDK_INT < Build.VERSION_CODES.N) {
                if (data != null) {
                    selectedImage = data.getData();
                    try {
                        mViewModel.setUri(selectedImage);
                        bitmap = BitmapFactory.decodeStream(getContentResolver().openInputStream(selectedImage));
                        bytesDocumentsTypePicture = new ImageUtils().getBytesFromBitmapCamera(bitmap);
                        mViewModel.setBitmap(bitmap);
                        mImage.setImageBitmap(bitmap);
                    }catch (IOException ex){
                        Log.d(TAG,"unable to display camera image");
                    }
                } else {
                    Toast.makeText(getApplicationContext(), " some_error_while_uploading  ", Toast.LENGTH_SHORT).show();
                    Log.d(TAG, "onActivityResult: data is null");
                }
            } else {
                Log.d(TAG, "onActivityResult: build version is less that build version with codes N");
                bitmap = BitmapFactory.decodeFile(captureMediaFile.getAbsolutePath());
                bytesDocumentsTypePicture = new ImageUtils().getBytesFromBitmap(bitmap);
                String path = MediaStore.Images.Media.insertImage(getContentResolver(), bitmap, "Title", null);
                mViewModel.setUri(Uri.parse(path));
                mViewModel.setBitmap(bitmap);
                mImage.setImageBitmap(bitmap);
            }

        } else if (resultCode == RESULT_OK && requestCode == GALLERY_REQUEST) {
            if (data != null) {
                selectedImage = data.getData();
                try {
                    mViewModel.setUri(selectedImage);
                    Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), selectedImage);
                    bytesDocumentsTypePicture = new ImageUtils().getBytesFromBitmap(bitmap);
                    mViewModel.setBitmap(bitmap);
                    mImage.setImageBitmap(bitmap);
                }catch(IOException ex){
                    Log.d(TAG, "IO Exception: unable to create bitmap");
                }
            } else {
                Toast.makeText(getApplicationContext(), " some_error_while_uploading  ", Toast.LENGTH_SHORT).show();
            }
        } else {
            Toast.makeText(getApplicationContext(), " some_error_while_uploading  ", Toast.LENGTH_SHORT).show();
        }
    }
    /* EOF gallery/camera intent */


    /*BOF upload posting */
    private String getTimeLimit(){
        int hour, minute;
        String am_pm;

        if (Build.VERSION.SDK_INT >= 23 ){
            hour = mTimeLimit.getHour();
            minute = mTimeLimit.getMinute();
        }
        else{
            hour = mTimeLimit.getCurrentHour();
            minute = mTimeLimit.getCurrentMinute();
        }

        if(hour > 12) {
            am_pm = "PM";
        }else {
            am_pm="AM";
        }

        StringBuilder sb = new StringBuilder();
        sb.append(hour).append(":").append(minute).append(" ").append(am_pm);
        return sb.toString();
    }

    private void uploadPosting(){
        String title = mTitle.getText().toString().trim();
        String building = mSpinner.getSelectedItem().toString().trim();
        String room = mRoom.getText().toString().trim();
        String timeLimit = getTimeLimit();
        String description = mDescription.getText().toString().trim();

        if(TextUtils.isEmpty(title)){
            mTitle.setError("enter a title");
            mTitle.requestFocus();
        }else if(TextUtils.isEmpty(building)){
            mSpinner.requestFocus();
        }else if(TextUtils.isEmpty(room)) {
            mRoom.setError("enter a room number");
            mRoom.requestFocus();
        }else if(TextUtils.isEmpty(description)) {
            mDescription.setError("Add a little description of what's available");
            mDescription.requestFocus();
        }else{
            mViewModel.setPosting(title,building,room,timeLimit,description);
            close();
        }
    }
    /* EOF upload posting */


    /*BOF close*/
    public void close(){
        //closes out of activity
    }
    /*EOF close*/
}

package com.pascalso.inquire;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import com.parse.ParseFile;
import com.parse.ParseObject;
import com.parse.ParseUser;

import java.io.ByteArrayOutputStream;

/**
 * An example full-screen activity that shows and hides the system UI (i.e.
 * status bar and navigation/system bar) with user interaction.
 */
public class SelectedImage extends AppCompatActivity {
    /**
     * Whether or not the system UI should be auto-hidden after
     * {@link #AUTO_HIDE_DELAY_MILLIS} milliseconds.
     */
    private static final boolean AUTO_HIDE = true;

    /**
     * If {@link #AUTO_HIDE} is set, the number of milliseconds to wait after
     * user interaction before hiding the system UI.
     */
    private static final int AUTO_HIDE_DELAY_MILLIS = 3000;

    /**
     * Some older devices needs a small delay between UI widget updates
     * and a change of the status and navigation bar.
     */
    private static final int UI_ANIMATION_DELAY = 300;
    private final Handler mHideHandler = new Handler();
    private View mContentView;
    private final Runnable mHidePart2Runnable = new Runnable() {
        @SuppressLint("InlinedApi")
        @Override
        public void run() {
            // Delayed removal of status and navigation bar

            // Note that some of these constants are new as of API 16 (Jelly Bean)
            // and API 19 (KitKat). It is safe to use them, as they are inlined
            // at compile-time and do nothing on earlier devices.
            mContentView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LOW_PROFILE
                    | View.SYSTEM_UI_FLAG_FULLSCREEN
                    | View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                    | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
                    | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                    | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION);
        }
    };
    private View mControlsView;
    private final Runnable mShowPart2Runnable = new Runnable() {
        @Override
        public void run() {
            // Delayed display of UI elements
            ActionBar actionBar = getSupportActionBar();
            if (actionBar != null) {
                actionBar.show();
            }
            mControlsView.setVisibility(View.VISIBLE);
        }
    };
    private boolean mVisible;
    private final Runnable mHideRunnable = new Runnable() {
        @Override
        public void run() {
            hide();
        }
    };
    /**
     * Touch listener to use for in-layout UI controls to delay hiding the
     * system UI. This is to prevent the jarring behavior of controls going away
     * while interacting with activity UI.
     */
    private final View.OnTouchListener mDelayHideTouchListener = new View.OnTouchListener() {
        @Override
        public boolean onTouch(View view, MotionEvent motionEvent) {
            if (AUTO_HIDE) {
                delayedHide(AUTO_HIDE_DELAY_MILLIS);
            }
            return false;
        }
    };

    private Bitmap selectedimage;
    private String grade;
    private String username;
    private String curriculum;
    private ParseFile file;
    private String subject;
    private static String answercomment;
    private String userID;
    private static String[] subjects;
    private EditText editText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_selected_image);
        drawImage();

    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);

        // Trigger the initial hide() shortly after the activity has been
        // created, to briefly hint to the user that UI controls
        // are available.
    }

    private void drawImage(){
        ImageView imageView = (ImageView) findViewById(R.id.selectedimage);
        int callingActivity = getIntent().getIntExtra("calling-activity", 0);
        switch (callingActivity){
            case ActivityConstants.STUDENT_ACTIVITY:
                imageView.setScaleType(ImageView.ScaleType.FIT_XY);
                imageView.setImageBitmap(Camera.getImage());
                selectedimage = Camera.getImage();
                break;
            case ActivityConstants.GALLERY:
                Bitmap image = Gallery.getImage();
                if (image.getHeight() > image.getWidth()){
                    imageView.setScaleType(ImageView.ScaleType.FIT_XY);
                    imageView.setImageBitmap(image);
                }
                else {
                    Matrix matrix = new Matrix();
                    matrix.postRotate(90);
                    Bitmap scaledBitmap = Bitmap.createScaledBitmap(image, 1080, 1920, true);
                    Bitmap rotatedBitmap = Bitmap.createBitmap(scaledBitmap, 0, 0, scaledBitmap.getWidth(), scaledBitmap.getHeight(), matrix, true);
                    imageView.setScaleType(ImageView.ScaleType.FIT_XY);
                    imageView.setImageBitmap(rotatedBitmap);
                }
                //imageView.setImageBitmap(AccessGalleryActivity.getImage());
                selectedimage = Gallery.getImage();

                break;
        }
    }





    private void toggle() {
        if (mVisible) {
            hide();
        } else {
            show();
        }
    }

    private void hide() {
        // Hide UI first
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.hide();
        }
        //mControlsView.setVisibility(View.GONE);
        mVisible = false;

        // Schedule a runnable to remove the status and navigation bar after a delay
        mHideHandler.removeCallbacks(mShowPart2Runnable);
        mHideHandler.postDelayed(mHidePart2Runnable, UI_ANIMATION_DELAY);
    }

    @SuppressLint("InlinedApi")
    private void show() {
        // Show the system bar
        mContentView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION);
        mVisible = true;

        // Schedule a runnable to display UI elements after a delay
        mHideHandler.removeCallbacks(mHidePart2Runnable);
        mHideHandler.postDelayed(mShowPart2Runnable, UI_ANIMATION_DELAY);
    }

    /**
     * Schedules a call to hide() in [delay] milliseconds, canceling any
     * previously scheduled calls.
     */
    private void delayedHide(int delayMillis) {
        mHideHandler.removeCallbacks(mHideRunnable);
        mHideHandler.postDelayed(mHideRunnable, delayMillis);
    }



    public void sendClick(){
        ImageButton send = (ImageButton) findViewById(R.id.send);
        ParseUser user = ParseUser.getCurrentUser();
        if(user.get("usertype").equals("student")) {
            userID = ParseUser.getCurrentUser().getObjectId();
            send.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View arg0) {
                    subjects = Tutor.getSubjects();
                    LayoutInflater inflater = getLayoutInflater();
                    View dialogView = inflater.inflate(R.layout.activity_choosesubject, null);
                    AlertDialog.Builder alertDialog = new AlertDialog.Builder(SelectedImage.this);
                    alertDialog.setView(dialogView);
                    alertDialog.setTitle("Send Photo");
                    Spinner spinner = (Spinner)dialogView.findViewById(R.id.subjectspinner);
                    ArrayAdapter<String> adapter = new ArrayAdapter<>(SelectedImage.this, android.R.layout.simple_spinner_item, subjects);
                    spinner.setAdapter(adapter);
                    editText = (EditText)dialogView.findViewById(R.id.additionalcomment);
                    spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                        @Override
                        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                            Object item = parent.getItemAtPosition(position);
                            subject = item.toString();
                        }

                        @Override
                        public void onNothingSelected(AdapterView<?> parent) {

                        }
                    });
                    alertDialog.setPositiveButton("Send", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            answercomment = editText.getText().toString();
                            //sendPushToTutors();
                            saveImageToParse();
                        }
                    });
                    alertDialog.show();

                }
            });
        }
        else{
            send.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View arg0){
                    LayoutInflater inflater = getLayoutInflater();
                    View dialogView = inflater.inflate(R.layout.activity_addcomment, null);
                    AlertDialog.Builder alertDialog = new AlertDialog.Builder(SelectedImage.this);
                    alertDialog.setView(dialogView);
                    alertDialog.setTitle("Add Comment");
                    editText = (EditText)dialogView.findViewById(R.id.comment);
                    alertDialog.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            answercomment = editText.getText().toString();
                            Intent tutorActivity = new Intent(SelectedImage.this, Tutor.class);
                            tutorActivity.putExtra("calling-activity", ActivityConstants.SELECTED_IMAGE_FRAGMENT);
                            startActivity(tutorActivity);
                            finish();
                        }
                    });
                    alertDialog.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            startActivity(new Intent(SelectedImage.this, Student.class));
                            finish();
                        }
                    });
                    alertDialog.show();
                }
            });
        }
    }


    private void saveImageToParse(){
        ParseUser user = ParseUser.getCurrentUser();
        grade = user.get("grade").toString();
        username = user.get("username").toString();
        curriculum = user.get("curriculum").toString();
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        selectedimage.compress(Bitmap.CompressFormat.PNG, 100, stream);
        byte[] bytearray = stream.toByteArray();
        if (bytearray != null) {
            file = new ParseFile("picture.png", bytearray);
            file.saveInBackground();
        }
        ParseObject x = new ParseObject("Questions");
        x.put("mediatype", "image");
        x.put("username", username);
        x.put("subject", subject);
        x.put("grade", grade);
        x.put("curriculum", curriculum);
        x.put("comment", answercomment);
        x.put("ImageFile", file);
        x.put("studentID", userID);
        x.saveInBackground();
        Toast.makeText(getApplicationContext(), "Your Image Has Been Sent!",
                Toast.LENGTH_SHORT).show();
        Intent i = new Intent(SelectedImage.this, Student.class);
        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(i);
    }

}

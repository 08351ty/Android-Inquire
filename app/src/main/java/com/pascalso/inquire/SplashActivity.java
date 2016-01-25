package com.pascalso.inquire;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.os.Handler;
import android.view.MotionEvent;
import android.view.View;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;


import com.parse.CountCallback;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseObject;
import com.parse.ParsePush;
import com.parse.ParseQuery;
import com.parse.ParseUser;

/**
 * An example full-screen activity that shows and hides the system UI (i.e.
 * status bar and navigation/system bar) with user interaction.
 */
public class SplashActivity extends AppCompatActivity {

    private ArrayList<ParseObject> parseObjects = new ArrayList<ParseObject>();
    private static ArrayList<Date> dates = new ArrayList<Date>();
    private static ArrayList<String> timecreated = new ArrayList<String>();
    private static ArrayList<String> subjects = new ArrayList<String>();
    private static ArrayList<ParseFile> photos = new ArrayList<>();
    private static ParseQuery<ParseObject> query;
    private static ArrayList<String> comments = new ArrayList<>();
    private static ArrayList<String> curriculum = new ArrayList<>();
    private static ArrayList<String> grades = new ArrayList<>();
    private static ArrayList<String> usernames = new ArrayList<>();
    private static ArrayList<String> answers = new ArrayList<>();
    private static ArrayList<String> tutorIds = new ArrayList<>();
    private static ArrayList<ParseObject> biology = new ArrayList<>();
    private static ArrayList<ParseObject> chemistry = new ArrayList<>();
    private static ArrayList<ParseObject> compsci = new ArrayList<>();
    private static ArrayList<ParseObject> econ = new ArrayList<>();
    private static ArrayList<ParseObject> math = new ArrayList<>();
    private static ArrayList<ParseObject> physics = new ArrayList<>();
    private int day;
    private int month;
    private int hour;
    private int minute;
    private int year;
    private String username;
    private ParseFile photo;
    private String answered;
    private String comment;
    private String tutorId;
    private String answercomment;
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
            //hide();
        }
    };
    /**
     * Touch listener to use for in-layout UI controls to delay hiding the
     * system UI. This is to prevent the jarring behavior of controls going away
     * while interacting with activity UI.

    private final View.OnTouchListener mDelayHideTouchListener = new View.OnTouchListener() {
        @Override
        public boolean onTouch(View view, MotionEvent motionEvent) {
            if (AUTO_HIDE) {
                delayedHide(AUTO_HIDE_DELAY_MILLIS);
            }
            return false;
        }
    };
     */

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_splash);
        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();
        //ParseUser.logOut();

        getUserData();

        // Upon interacting with UI controls, delay any scheduled hide()
        // operations to prevent the jarring behavior of controls going away
        // while interacting with the UI.
    }

    /**
    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);

        // Trigger the initial hide() shortly after the activity has been
        // created, to briefly hint to the user that UI controls
        // are available.
        delayedHide(100);
    }
    */


    private void hide() {
        // Hide UI first
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.hide();
        }
        mControlsView.setVisibility(View.GONE);
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

    private void delayedHide(int delayMillis) {
        mHideHandler.removeCallbacks(mHideRunnable);
        mHideHandler.postDelayed(mHideRunnable, delayMillis);
    }
     */

    private void getUserData(){
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                ParseUser user = ParseUser.getCurrentUser();
                if (user == null) {
                    startActivity(new Intent(SplashActivity.this, UserVerification.class));
                    finish();
                } else {
                    if (user.get("usertype").toString().equals("student")) {
                        //Get data of student's own profile
                        query = ParseQuery.getQuery("Questions");
                        username = user.get("username").toString();
                        query.whereEqualTo("username", username);
                        query.orderByDescending("updatedAt");
                        query.setLimit(20);
                        try {
                            List<ParseObject> photos = query.find();
                            parseObjects.addAll(photos);
                        } catch (ParseException e) {
                        }

                        int x = 0;
                        while (x < parseObjects.size()) {
                            String subject = parseObjects.get(x).getString("subject");
                            Date date = parseObjects.get(x).getUpdatedAt();
                            if (parseObjects.get(x).has("Answer")) {
                                photo = parseObjects.get(x).getParseFile("Answer");
                                tutorId = parseObjects.get(x).getString("tutorId");
                                answered = "yes";
                            } else {
                                photo = parseObjects.get(x).getParseFile("ImageFile");
                                tutorId = null;
                                answered = "no";
                            }
                            if (parseObjects.get(x).has("TutorComment")) {
                                comment = parseObjects.get(x).getString("TutorComment");

                            } else {
                                comment = parseObjects.get(x).getString("comment");
                            }
                            tutorIds.add(tutorId);
                            answers.add(answered);
                            comments.add(comment);
                            subjects.add(subject);
                            dates.add(date);
                            photos.add(photo);
                            x++;
                        }
                        compareDate();
                        //ParsePush.subscribeInBackground(username);
                        startActivity(new Intent(SplashActivity.this, Student.class));
                        finish();
                    } else {
                        //Get data of all work
                        username = user.get("username").toString();
                        query = ParseQuery.getQuery("Questions");
                        query.whereDoesNotExist("Answer");
                        query.orderByAscending("createdAt");
                        query.setLimit(20);
                        try {
                            List<ParseObject> photos = query.find();
                            parseObjects.addAll(photos);
                        } catch (ParseException e) {
                        }

                        int x = 0;
                        while (x < parseObjects.size()) {
                            String subject = parseObjects.get(x).getString("subject");
                            switch (subject) {
                                case "Biology":
                                    biology.add(parseObjects.get(x));
                                    break;
                                case "Chemistry":
                                    chemistry.add(parseObjects.get(x));
                                    break;
                                case "Computer Science":
                                    compsci.add(parseObjects.get(x));
                                    break;
                                case "Economics":
                                    econ.add(parseObjects.get(x));
                                    break;
                                case "Maths":
                                    math.add(parseObjects.get(x));
                                    break;
                                case "Physics":
                                    physics.add(parseObjects.get(x));
                                    break;
                            }
                            x++;
                        }
                        //ParsePush.subscribeInBackground(username);
                        //ParsePush.subscribeInBackground("Tutor");
                        startActivity(new Intent(SplashActivity.this, Tutor.class));
                        finish();
                    }
                }
            }
        }, 1000);
    }
    private void compareDate(){
        Calendar calendar = Calendar.getInstance();
        minute = calendar.get(Calendar.MINUTE);
        hour = calendar.get(Calendar.HOUR_OF_DAY);
        day = calendar.get(Calendar.DAY_OF_MONTH);
        month = calendar.get(Calendar.MONTH);
        year = calendar.get(Calendar.YEAR);
        int x = 0;
        while (x < dates.size()){
            calendar.setTime(dates.get(x));
            if(calendar.get(Calendar.HOUR_OF_DAY) == hour && calendar.get(Calendar.DAY_OF_MONTH) == day){
                int b = minute - calendar.get(Calendar.MINUTE);
                if(b < 2){
                    timecreated.add("Just now");
                }
                else{
                    timecreated.add(b + " minutes ago");
                }
            }
            else {
                if(calendar.get(Calendar.YEAR) != year) {
                    int y = year - calendar.get(Calendar.YEAR);
                    if (y == 1) {
                        timecreated.add(y + " year ago");
                    }
                    else {
                        timecreated.add(y + " years ago");
                    }
                }
                else if(calendar.get(Calendar.MONTH) == month) {
                    if (calendar.get(Calendar.DAY_OF_MONTH) == day) {
                        int c = hour - calendar.get(Calendar.HOUR_OF_DAY);
                        if (c == 1) {
                            timecreated.add(c + " hour ago");
                        } else {
                            timecreated.add(c + " hours ago");
                        }
                    } else {
                        int d = day - calendar.get(Calendar.DAY_OF_MONTH);
                        if (d == 1) {
                            timecreated.add(d + " day ago");
                        } else {
                            timecreated.add(d + " days ago");
                        }
                    }
                }
                else {
                    int e = month - calendar.get(Calendar.MONTH);
                    if (e == 1) {
                        timecreated.add(e + " month ago");
                    }
                    else{
                        timecreated.add(e + " months ago");
                    }
                }
            }
            x++;
        }
    }

    public static ArrayList<String> getTimeCreated(){
        return timecreated;
    }

    public static ArrayList<String> getSubjects(){
        return subjects;
    }

    public static ArrayList<ParseFile> getPhotos(){
        return photos;
    }

    public static ArrayList<String> getComments(){
        return comments;
    }

    public static ArrayList<String> getAnswers(){
        return answers;
    }

    public static ArrayList<String> getTutorIds(){
        return tutorIds;
    }

    public static ArrayList<ParseObject> getBiology() {
        return biology;
    }

    public static ArrayList<ParseObject> getChemistry() {
        return chemistry;
    }

    public static ArrayList<ParseObject> getCompsci() {
        return compsci;
    }

    public static ArrayList<ParseObject> getEcon(){
        return econ;
    }

    public static ArrayList<ParseObject> getMath(){
        return math;
    }

    public static ArrayList<ParseObject> getPhysics(){
        return physics;
    }
}

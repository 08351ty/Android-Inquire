package com.pascalso.inquire;

import android.app.ActionBar;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.parse.ParseFile;
import com.parse.ParseObject;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;

public class SubjectQuestions extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private String chosensubject;
    private ArrayList<ParseObject> parseObjects = new ArrayList<>();
    private static ArrayList<Date> dates = new ArrayList<>();
    private ArrayList<String> grades = new ArrayList<>();
    private ArrayList<String> usernames = new ArrayList<>();
    private ArrayList<String> curriculum = new ArrayList<>();
    private ArrayList<String> comments = new ArrayList<>();
    private ArrayList<String> objectIDs = new ArrayList<>();
    private ArrayList<String> timecreated = new ArrayList<>();
    private ArrayList<ParseFile> photos = new ArrayList<>();
    private Integer[] imgid = { R.drawable.ic_sent, R.drawable.ic_replied};
    private int minute;
    private int hour;
    private int day;
    private int month;

    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_subject_questions);
        chosensubject = Tutor.getChosenSubject();
        /**
        ActionBar actionBar = getActionBar();
        actionBar.setTitle(chosensubject);
        getActionBar().setDisplayHomeAsUpEnabled(true);
         */
        getData();

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        mRecyclerView = (RecyclerView) findViewById(R.id.subject_recycler_view);

        // use this setting to improve performance if you know that changes
        // in content do not change the layout size of the RecyclerView
        mRecyclerView.setHasFixedSize(true);

        // use a linear layout manager
        mLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLayoutManager);

        // specify an adapter
        mAdapter = new SubjectQuestionListAdapter(this, usernames, grades, timecreated, imgid);
        mRecyclerView.setAdapter(mAdapter);

    }

    private void getData(){
        switch (chosensubject){
            case "Biology":
                parseObjects = SplashActivity.getBiology();
                break;
            case "Chemistry":
                parseObjects = SplashActivity.getChemistry();
                break;
            case "Computer Science":
                parseObjects = SplashActivity.getCompsci();
                break;
            case "Economics":
                parseObjects = SplashActivity.getEcon();
                break;
            case "Maths":
                parseObjects = SplashActivity.getMath();
                break;
            case "Physics":
                parseObjects = SplashActivity.getPhysics();
        }

        int x = 0;
        while (x < parseObjects.size()){
            Date date = parseObjects.get(x).getCreatedAt();
            ParseFile photo = parseObjects.get(x).getParseFile("ImageFile");
            String curr = parseObjects.get(x).getString("curriculum");
            String grade = parseObjects.get(x).getString("grade");
            String comment = parseObjects.get(x).getString("comment");
            String username = parseObjects.get(x).getString("username");
            String objectID = parseObjects.get(x).getObjectId();
            objectIDs.add(objectID);
            usernames.add(username);
            comments.add(comment);
            curriculum.add(curr);
            grades.add(grade);
            dates.add(date);
            photos.add(photo);
            x++;
        }
        Collections.reverse(objectIDs);
        Collections.reverse(usernames);
        Collections.reverse(comments);
        Collections.reverse(grades);
        Collections.reverse(curriculum);
        Collections.reverse(dates);
        Collections.reverse(photos);
        compareDate();

    }

    private void compareDate(){
        Calendar calendar = Calendar.getInstance();
        minute = calendar.get(Calendar.MINUTE);
        hour = calendar.get(Calendar.HOUR_OF_DAY);
        day = calendar.get(Calendar.DAY_OF_MONTH);
        month = calendar.get(Calendar.MONTH);
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
                if(calendar.get(Calendar.MONTH) == month) {
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
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.tutor, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_subjects) {
            // Handle the camera action
        } else if (id == R.id.nav_notifications) {

        } else if (id == R.id.nav_profile) {

        } else if (id == R.id.nav_pp) {

        } else if (id == R.id.nav_hf) {

        } else if (id == R.id.nav_logout) {

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}

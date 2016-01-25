package com.pascalso.inquire;

import android.os.Message;

import com.parse.CountCallback;
import com.parse.FindCallback;
import com.parse.Parse;
import com.parse.ParseException;
import com.parse.ParseFacebookUtils;
import com.parse.ParseFile;
import com.parse.ParseInstallation;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;

/**
 * Created by pso on 12/27/15.
 */
public class Application extends android.app.Application {
    public void onCreate() {
        super.onCreate();
        Parse.enableLocalDatastore(this);
        //ParseObject.registerSubclass(Message.class);
        Parse.initialize(this);
        //ParseInstallation.getCurrentInstallation().saveInBackground();
        ParseFacebookUtils.initialize(this);
    }
}

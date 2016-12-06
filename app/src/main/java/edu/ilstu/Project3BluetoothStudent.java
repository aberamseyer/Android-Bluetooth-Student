package edu.ilstu;

import android.app.Application;
import android.content.Context;

/**
 * Created by Abe on 12/3/2016.
 * the only purpose of this class is so we can get some context wherever is necessary
 */

public class Project3BluetoothStudent extends Application {

    private static Context context;

    public void onCreate() {
        super.onCreate();
        Project3BluetoothStudent.context = getApplicationContext();
    }

    public static Context getAppContext() {
        return Project3BluetoothStudent.context;
    }
}
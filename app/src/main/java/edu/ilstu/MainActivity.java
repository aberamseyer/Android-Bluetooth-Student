package edu.ilstu;

import android.Manifest;
import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.FileObserver;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.inputmethod.InputMethodManager;
import android.widget.Toast;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private static final int MY_PERMISSIONS_REQUEST_WRITE_EXTERNAL = 0;
    private FloatingActionButton fab;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.WRITE_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                    MY_PERMISSIONS_REQUEST_WRITE_EXTERNAL);
        }

        fab = (FloatingActionButton)findViewById(R.id.fab);

        FragmentManager fm = getSupportFragmentManager();
        Fragment fragment = fm.findFragmentById(R.id.fragmentContainer);

        fab.setOnClickListener(this);

        if (fragment == null) {
            fragment = new CardFragment();
            fm.beginTransaction()
                    .add(R.id.fragmentContainer, fragment)
                    .commit();

        }

    }

    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {
        super.onSaveInstanceState(savedInstanceState);
        // Save UI state changes to the savedInstanceState.
        // This bundle will be passed to onCreate if the process is
        // killed and restarted.
    }

    @Override
    public void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        // Restore UI state from the savedInstanceState.
        // This bundle has also been passed to onCreate.
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        Context staticContext = Project3BluetoothStudent.getAppContext();
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_WRITE_EXTERNAL: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                }
                else {
                    Toast.makeText(staticContext, R.string.file_permission_denied,
                            Toast.LENGTH_SHORT).show();
                }
                return;
            }

            // other 'case' lines to check for other
            // permissions this app might request
        }
    }

    public static void hideSoftKeyboard (Activity activity, View view)
    {
        InputMethodManager imm = (InputMethodManager)activity.getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(view.getApplicationWindowToken(), 0);
    }

    @Override
    public void onClick(View v) {

        hideSoftKeyboard(MainActivity.this, v); // MainActivity is the name of the class and v is the View parameter used in the button listener method onClick.

        switch (v.getId()) {
            case R.id.fab:
                Log.i("aramsey", "Big fab tapped");
                // Here, thisActivity is the current activity
                sendQuestions(v);
                break;
            default:
                Log.i("aramsey", "idk what you tapped");
                break;
        }
    }

    // Send the selected questions to a bluetooth devices
    private void sendQuestions(View v) {
        Log.i("blutooth", "bt button tapped");

        BluetoothAdapter btAdapter = BluetoothAdapter.getDefaultAdapter();
        Log.i("aramsey", btAdapter.toString());

        Context staticContext = Project3BluetoothStudent.getAppContext();
        CardFragment.printItemsToLog();
        String contentToSend = "";
        for(SAQuestion q : CardFragment.questions) {
            if(q.getSelected())
                contentToSend += q.getReturnString();
        }


        try {
            //Create a file and write the String to it
            BufferedWriter out;
            final String filePath = Environment.getExternalStorageDirectory().getPath() + "/answers.txt";
            FileWriter fileWriter = new FileWriter(filePath);
            out = new BufferedWriter(fileWriter);
            out.write(contentToSend);
            out.close();
            //Access the file and share it through the original intent
            File file = new File(filePath);
            Intent sendIntent = new Intent(Intent.ACTION_SEND);
            sendIntent.putExtra(Intent.EXTRA_STREAM, Uri.fromFile(file));
            sendIntent.setType("text/plain");
            //Create a file observer to monitor the access to the file
            FileObserver fobsv = new FileObserver(filePath) {
                @Override
                public void onEvent(int event, String path) {
                    if (event == FileObserver.CLOSE_NOWRITE) {
                        //The file was previously written to, now it's been sent and closed
                        //we can safely delete it.
                        File file = new File(filePath);
                        file.delete();
                    }
                }
            };
            fobsv.startWatching();

            final PackageManager pm = staticContext.getPackageManager();
            List<ResolveInfo> appsList = pm.queryIntentActivities(sendIntent, 0);

            if (appsList.size() > 0) {
                String packageName = null;
                String className = null;
                boolean found = false;

                for (int i = 0; i < appsList.size(); i++) {
                    // find bluetooth in the list of activities
                    packageName = appsList.get(i).activityInfo.packageName;
                    if (packageName.equals("com.android.bluetooth")) {
                        className = appsList.get(i).activityInfo.name;
                        found = true;
                        break;
                    }
                }
                if (!found) {
                    Toast.makeText(staticContext, R.string.blu_notfound_inlist,
                            Toast.LENGTH_SHORT).show();
                }

                sendIntent.setClassName(packageName, className);
                startActivity(sendIntent);
                Log.i("aramsey", "supposedly sent the file");
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

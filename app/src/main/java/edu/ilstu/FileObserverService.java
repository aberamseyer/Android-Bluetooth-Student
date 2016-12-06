package edu.ilstu;

import android.app.IntentService;
import android.content.Intent;
import android.os.Environment;
import android.os.FileObserver;
import android.os.Handler;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import java.io.File;

import static edu.ilstu.CardFragment.MyRecyclerView;
import static edu.ilstu.CardFragment.customAdapter;

/**
 * Created by Abe on 12/5/2016.
 */

public class FileObserverService extends IntentService {

    public static FileObserver fileObserver;
    private final String TAG = "debug";
    private final String pathToWatch = Environment.getExternalStorageDirectory().getPath() + "/bluetooth/";
    Handler handler;

    public FileObserverService() {
        super("FileObserverService");
        handler = new Handler();
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        fileObserver = new FileObserver(pathToWatch) { // set up a file observer to watch this directory on sd card
            long time = 0;
            long lastTime = 0;
            @Override
            public void onEvent(int event, String file) {
                time = System.currentTimeMillis();
                if(event == FileObserver.CREATE && file.equals("wadus.txt")) {
                    Log.i(TAG, "File created [" + pathToWatch + file + "]");
                }
                if(time - lastTime > 1000 && FileObserver.DELETE != event) {
                    Log.i(TAG, time + "    " + lastTime + ":    " + (time - lastTime));
                    Log.i(TAG, "close write");
                    if(true)//CardFragment.customAdapter != null)
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {

                                //stuff that updates ui
                                CardFragment.questions.clear();
                                CardFragment.initializeList();
                                if (CardFragment.questions.size() > 0 & CardFragment.MyRecyclerView != null) {
                                    //Create an integer representation of the question types
                                    CardFragment.dataSetTypes = new int[CardFragment.questions.size()];
                                    for (int i = 0; i < CardFragment.questions.size(); i++) {
                                        if (CardFragment.questions.get(i) instanceof MCQuestion) {
                                            CardFragment.dataSetTypes[i] = 1;
                                        }
                                    }
                                    customAdapter = new CustomAdapter(CardFragment.questions, CardFragment.dataSetTypes);
                                    CardFragment.MyRecyclerView.setAdapter(customAdapter);
                                    CardFragment.customAdapter.notifyDataSetChanged();
                                }
                            }
                        });
                }
                lastTime = time;
            }
        };
        fileObserver.startWatching(); //START OBSERVING
        Log.i(TAG, "started watching directory: "+ pathToWatch);
    }

    private void runOnUiThread(Runnable runnable) {
        handler.post(runnable);
    }
}

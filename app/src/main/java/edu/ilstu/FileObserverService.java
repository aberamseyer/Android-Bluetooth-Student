package edu.ilstu;

import android.app.IntentService;
import android.content.Intent;
import android.os.Environment;
import android.os.FileObserver;
import android.os.Handler;
import android.util.Log;

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
            public void onEvent(int event, String file) {   // Fires when the directory changes
                time = System.currentTimeMillis();                  // the code with system time here ensures that the view only updates once because receiving the file would
                if(event == FileObserver.CREATE && file.equals("wadus.txt")) { // trigger this method multiple times
                    Log.i(TAG, "File created [" + pathToWatch + file + "]");
                }
                if(time - lastTime > 1000 && FileObserver.DELETE != event) {
                    Log.i(TAG, time + "    " + lastTime + ":    " + (time - lastTime));
                    Log.i(TAG, "close write");
                    runOnUiThread(new Runnable() {      // stuff to deal with refreshing the UI has to happen on the main UI thread
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
        fileObserver.startWatching(); // start watching the directory
        Log.i(TAG, "started watching directory: " + pathToWatch);
    }

    private void runOnUiThread(Runnable runnable) {     // services don't inherently have access to the runOnUIThread() method, so pass it to the handler
        handler.post(runnable);
    }
}

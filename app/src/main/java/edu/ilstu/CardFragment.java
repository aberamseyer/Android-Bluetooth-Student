package edu.ilstu;

import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;


public class CardFragment extends Fragment {

    // please ignore all these global variables
    public static RecyclerView MyRecyclerView;
    public static int[] dataSetTypes;
    public static ArrayList<SAQuestion> questions = new ArrayList<SAQuestion>();
    public static CustomAdapter customAdapter;
    private static final String TAG = "debug";

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if(questions.isEmpty()) {
            Log.i(TAG, "called initialize list");
            initializeList();
        }
        getActivity().setTitle("Bluetooth Popup Quiz - Student");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_card, container, false);
        MyRecyclerView = (RecyclerView) view.findViewById(R.id.recycler);
        MyRecyclerView.setHasFixedSize(true);
        LinearLayoutManager MyLayoutManager = new LinearLayoutManager(getActivity());
        MyLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);

        if (questions.size() > 0 & MyRecyclerView != null) {
            //Create an integer representation of the question types
            dataSetTypes = new int[questions.size()];
            for(int i = 0; i < questions.size(); i++) {
                if (questions.get(i) instanceof MCQuestion) {
                    dataSetTypes[i] = 1;
                }
            }
            customAdapter = new CustomAdapter(questions, dataSetTypes);
            MyRecyclerView.setAdapter(customAdapter);
        }
        MyRecyclerView.setLayoutManager(MyLayoutManager);

        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }


    public static void initializeList() {
        ArrayList<String> questionStrings = new ArrayList<String>();
        String[] questionArray;
        int i = 0;

        // Read from the file that is in the bluetooth directory, delete it when we're done
        // to allow for receiving and reading a new set of questions
        try {
            BufferedReader in;
            final String filePath = Environment.getExternalStorageDirectory().getPath() + "/bluetooth/wadus.txt";
            FileReader fileReader = new FileReader(filePath);
            in = new BufferedReader(fileReader);
            questionStrings.add(in.readLine());
            while(questionStrings.get(i) != null) {
                i++;
                questionStrings.add(in.readLine());
            }
            questionStrings.remove(i);
            in.close();
            Log.i(TAG, "file read");
            File file = new File(filePath);
            file.delete();
        }
        catch (FileNotFoundException e) {
        }
        catch (IOException e) {
            e.printStackTrace();
        }
        for(String questionString : questionStrings) {
            questionArray = questionString.split(",");
            switch (Integer.parseInt(questionArray[0])) {
                case 0:
                    questions.add(new SAQuestion(questionArray[1]));
                    break;
                case 1:
                    questions.add(new SAQuestion(questionArray[1],questionArray[2]));
                    break;
                case 2:
                    questions.add(new MCQuestion(questionArray[1],questionArray[2],questionArray[3],
                            questionArray[4],questionArray[5]));
                    break;
                case 3:
                    questions.add(new MCQuestion(questionArray[1],questionArray[2]));
                    break;
                default:
                    break;
            }
        }
    }

    public static void printItemsToLog() {
        for (int i = 0; i < questions.size(); i++) {
            if (questions.get(i).getSelected())
                Log.i(TAG, questions.get(i).getReturnString());
        }
    }
}

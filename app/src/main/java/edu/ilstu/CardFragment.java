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
import android.widget.CheckBox;
import android.widget.TextView;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;


public class CardFragment extends Fragment {

    RecyclerView MyRecyclerView;
    private int[] dataSetTypes;
    public static ArrayList<SAQuestion> questions = new ArrayList<SAQuestion>();

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if(questions.isEmpty()) {
            Log.i("aramsey", "called initialize list");
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
            MyRecyclerView.setAdapter(new CustomAdapter(questions, dataSetTypes));
        }
        MyRecyclerView.setLayoutManager(MyLayoutManager);

        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

//    public void initializeList() {
//        questions.clear();
//        // Predefined multiple-choice questions + any others added
//        questions.add(new MCQuestion("What grade do you plan to get in this class?", "A", "B", "C", "D", "a"));
//        questions.add(new MCQuestion("What is your favorite color", "Red", "Green", "Blue", "none of these", "c"));
//        questions.add(new MCQuestion("how old are you", "18", "19", "20", "21", "b"));
//        questions.add(new MCQuestion("What is your major?", "Computer Science", "IS", "Something else", "not sure yet", "a"));
//        questions.add(new MCQuestion("What kind of housing do you live in?", "Dorm", "Apartment", "House", "I'm Homeless", "a"));
//        questions.add(new SAQuestion("What is your name?"));
//    }

    public void initializeList() {
        ArrayList<String> questionStrings = new ArrayList<String>();
        String[] questionArray;
        int i = 0;
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
            Log.i("aramsey", "file read");
            File file = new File(filePath);
            file.delete();
        }
        catch (FileNotFoundException e) {
            e.printStackTrace();
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
                Log.i("aramsey", questions.get(i).getReturnString());
        }
    }
}

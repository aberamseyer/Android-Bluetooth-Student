package edu.ilstu;

import android.app.Activity;
import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by Michael McHugh on 12/4/2016.
 */

public class CustomAdapter extends RecyclerView.Adapter<CustomAdapter.ViewHolder> {
    private static final String TAG = "CustomAdapter";

    private ArrayList<SAQuestion> questions = new ArrayList<SAQuestion>();
    private int[] mDataSetTypes;

    public static final int SA_QUESTION = 0;
    public static final int MC_QUESTION = 1;

    public CustomAdapter(ArrayList<SAQuestion> data, int[] dataSetTypes) {
        questions = data;
        mDataSetTypes = dataSetTypes;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public ViewHolder(View v) {
            super(v);
        }
    }

    public class SAViewHolder extends ViewHolder {
        TextView question;
        EditText shortAnswer;
        public SAViewHolder(View v) {
            super(v);
            this.question = (TextView) v.findViewById(R.id.question);
            this.shortAnswer = (EditText) v.findViewById(R.id.answer_text);
        }
    }

    public class MCViewHolder extends ViewHolder {
        TextView question;
        RadioGroup options;
        RadioButton optionA;
        RadioButton optionB;
        RadioButton optionC;
        RadioButton optionD;
        public MCViewHolder(View v) {
            super(v);
            this.question = (TextView) v.findViewById(R.id.question);
            this.options = (RadioGroup) v.findViewById(R.id.options);
            this.optionA = (RadioButton) options.findViewById(R.id.optionA);
            this.optionB = (RadioButton) options.findViewById(R.id.optionB);
            this.optionC = (RadioButton) options.findViewById(R.id.optionC);
            this.optionD = (RadioButton) options.findViewById(R.id.optionD);
        }
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        View v;
        Log.i("mmc", "View type: " + viewType);
        if (viewType == SA_QUESTION) {
            v = LayoutInflater.from(viewGroup.getContext())
                    .inflate(R.layout.sa_card, viewGroup, false);
            return new SAViewHolder(v);
        }
        else {
            v = LayoutInflater.from(viewGroup.getContext())
                    .inflate(R.layout.mc_card, viewGroup, false);
            return new MCViewHolder(v);
        }
    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, final int position) {
        if (viewHolder.getItemViewType() == SA_QUESTION) {
           final  SAViewHolder holder = (SAViewHolder) viewHolder;
            questions.get(position).formatQuestion();
            holder.question.setText(questions.get(position).getQuestion() + "?");
            holder.shortAnswer.setText(questions.get(position).getAns());

            TextWatcher watcher= new TextWatcher() {
                public void afterTextChanged(Editable s) {
                    questions.get(position).setAns(holder.shortAnswer.getText().toString());
                }
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                    //Do something or nothing.
                }
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                    //Do something or nothing
                }
            };

            holder.shortAnswer.addTextChangedListener(watcher);
        }
        else if (viewHolder.getItemViewType() == MC_QUESTION) {
            MCQuestion question = (MCQuestion) questions.get(position);
            final MCViewHolder holder = (MCViewHolder) viewHolder;
            question.formatQuestion();
            holder.question.setText(question.getQuestion() + "?");
            holder.optionA.setText("A: " + question.getA());
            holder.optionB.setText("B: " + question.getB());
            holder.optionC.setText("C: " + question.getC());
            holder.optionD.setText("D: " + question.getD());
            if (questions.get(position).getAns() != null) {
                switch (questions.get(position).getAns()) {
                    case "a":
                        holder.options.check(R.id.optionA);
                        break;
                    case "b":
                        holder.options.check(R.id.optionB);
                        break;
                    case "c":
                        holder.options.check(R.id.optionC);
                        break;
                    case "d":
                        holder.options.check(R.id.optionD);
                        break;
                    default:
                        break;
                }
            }

            holder.options.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(RadioGroup group, int checkedId) {
                    switch(checkedId) {
                        case R.id.optionA:
                            ((MCQuestion) questions.get(position)).setAns("a");
                            Log.i("mmc", "A Selected");
                            break;
                        case R.id.optionB:
                            ((MCQuestion) questions.get(position)).setAns("b");
                            Log.i("mmc", "B Selected");
                            break;
                        case R.id.optionC:
                            ((MCQuestion) questions.get(position)).setAns("c");
                            Log.i("mmc", "C Selected");
                            break;
                        case R.id.optionD:
                            ((MCQuestion) questions.get(position)).setAns("d");
                            Log.i("mmc", "D Selected");
                            break;
                        default:
                            break;
                    }
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return questions.size();
    }

    @Override
    public int getItemViewType(int position) {
        return mDataSetTypes[position];
    }
}
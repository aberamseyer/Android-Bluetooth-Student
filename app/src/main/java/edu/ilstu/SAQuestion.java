package edu.ilstu;

/**
 * Created by Abe on 12/3/2016.
 */

public class SAQuestion {
    private String question;
    protected String ans;
    private boolean selected;

    public SAQuestion(String question) {
        this.question = question;
        selected = true;
        ans = null;
    }

    public SAQuestion(String question, String ans) {
        this.question = question;
        this.ans = ans;
        selected = true;
    }

    public String getQuestion() {
        return question;
    }

    public void setQuestion(String question) {
        this.question = question;
    }

    public String getAns() {
        return ans;
    }

    public void setAns(String ans) {
        this.ans = ans;
    }

    public void formatQuestion() {
        if (question.lastIndexOf("?") == question.length()-1) { // remove last question mark if there is one
            question = question.substring(0, question.length()-1);
        }
        question = question.substring(0, 1).toUpperCase() + question.substring(1); // capitalize the first letter
    }

    public void toggleSelected() {
        selected = !selected;
    }

    public boolean getSelected() {
        return selected;
    }


    // We have a few different toString() methods for the various ways the questions/answers need to be formatted
    @Override
    public String toString() {
        return "" + question + "?\n";
    }

    public String getSendString() {
        return "0," + getQuestion() + "\n";
    }

    public String getReturnString() {
        return "1," + getQuestion() + "," + ans + "\n";
    }
}

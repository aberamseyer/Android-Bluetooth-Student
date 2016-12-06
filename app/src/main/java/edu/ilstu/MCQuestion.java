package edu.ilstu;

import android.widget.CheckBox;

/**
 * Created by anonymous on 1/15/16.
 */
public class MCQuestion extends SAQuestion {

    private String a, b, c, d;

    public MCQuestion(String question, String ans) {
        super(question,ans);
        a = null;
        b = null;
        c = null;
        d = null;
    }

    public MCQuestion(String question, String a, String b, String c, String d) {
        super(question);
        this.a = a;
        this.b = b;
        this.c = c;
        this.d = d;
    }

    public MCQuestion(String question, String a, String b, String c, String d, String ans) {
        this(question,a,b,c,d);
        this.ans = ans;
    }

    public void setA(String a) {
        this.a = a;
    }

    public void setB(String b) {
        this.b = b;
    }

    public void setC(String c) {
        this.c = c;
    }

    public void setD(String d) {
        this.d = d;
    }

    public String getA() {
        return a;
    }

    public String getB() {
        return b;
    }

    public String getC() {
        return c;
    }

    public String getD() {
        return d;
    }


    // a few different toStrings() for the different outputs we need
    @Override
    public String toString() {
        super.formatQuestion();
        return "" + super.getQuestion() + "?\n" + "\tA: " + a + "\n\tB: " + b + "\n\tC: " + c + "\n\tD: " + d + "\n" + ans + "\n";
    }

    public String getSendString() {
        return "2," + super.getQuestion() + "," + a + "," + b + "," + c + "," + d + "\n";
    }

    public String getReturnString() {
        return "3," + super.getQuestion() + "," + ans + "\n";
    }
}





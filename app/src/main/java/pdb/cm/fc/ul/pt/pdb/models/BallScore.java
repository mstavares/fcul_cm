package pdb.cm.fc.ul.pt.pdb.models;

import java.io.Serializable;

import pdb.cm.fc.ul.pt.pdb.utilities.Utilities;


public class BallScore implements Serializable {

    private String mDate;
    private int mScore, mTime;

    public BallScore() {
    }

    public BallScore(int score, int time) {
        mScore = score;
        mTime = time;
        mDate = Utilities.getTimestamp();
    }

    public String getDate() {
        return mDate;
    }

    public int getScore() {
        return mScore;
    }

    public void setScore(int score) {
        mScore = score;
    }

    public int getTime() {
        return mTime;
    }

    public void setTime(int time) {
        mTime = time;
    }
}

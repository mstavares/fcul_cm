package pdb.cm.fc.ul.pt.pdb.models;

import java.io.Serializable;

import pdb.cm.fc.ul.pt.pdb.utilities.Utilities;

public class WordScore implements Serializable {


    private String mDate;
    private int mScore, mTime, mFaults;

    public WordScore() {}

    public WordScore(int time, int score, int faults) {
        mTime = time;
        mScore = score;
        mFaults = faults;
        mDate = Utilities.getTimestamp();
    }

    public String getDate() {
        return mDate;
    }

    public int getScore() {
        return mScore;
    }

    public int getTime() {
        return mTime;
    }

    public int getFaults() {
        return mFaults;
    }

}

package pdb.cm.fc.ul.pt.pdb.models;

import java.io.Serializable;

import pdb.cm.fc.ul.pt.pdb.utilities.Utilities;

public class WordScore implements Serializable {


    private String date;
    private int score, time, faults;

    public WordScore() {}

    public WordScore(int time, int score, int faults) {
        this.time = time;
        this.score = score;
        this.faults = faults;
        date = Utilities.getTimestamp();
    }

    public String getDate() {
        return date;
    }

    public int getScore() {
        return score;
    }

    public int getTime() {
        return time;
    }

    public int getFaults() {
        return faults;
    }

}

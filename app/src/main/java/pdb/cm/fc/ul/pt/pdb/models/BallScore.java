package pdb.cm.fc.ul.pt.pdb.models;

import java.io.Serializable;

import pdb.cm.fc.ul.pt.pdb.utilities.Utilities;


public class BallScore implements Serializable {

    private String date;
    private int score, time;

    public BallScore() {
    }

    public BallScore(int score, int time) {
        this.score = score;
        this.time = time;
        date = Utilities.getTimestamp();
    }

    public String getDate() {
        return date;
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }

    public int getTime() {
        return time;
    }

    public void setTime(int time) {
        this.time = time;
    }
}

package pdb.cm.fc.ul.pt.pdb.models;

import java.io.Serializable;

/**
 * Created by nunonelas on 08/12/17.
 */

public class WordScore implements Serializable {

    private String date;
    private String score;
    private String time;
    private String faults;
    private String word;

    public WordScore() {
    }

    public WordScore(String date, String score, String time, String faults, String word) {
        this.date = date;
        this.score = score;
        this.time = time;
        this.faults = faults;
        this.word = word;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getScore() {
        return score;
    }

    public void setScore(String score) {
        this.score = score;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getFaults() {
        return faults;
    }

    public void setFaults(String faults) {
        this.faults = faults;
    }

    public String getWord() {
        return word;
    }

    public void setWord(String word) {
        this.word = word;
    }
}

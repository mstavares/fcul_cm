package pdb.cm.fc.ul.pt.pdb.models;

import java.io.Serializable;

/**
 * Created by nunonelas on 08/12/17.
 */

public class BallScore implements Serializable {

    private String score;
    private String time;

    public BallScore() {
    }

    public BallScore(String score, String time) {
        this.score = score;
        this.time = time;
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
}

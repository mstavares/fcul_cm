package pdb.cm.fc.ul.pt.pdb.models;

import java.io.Serializable;

/**
 * Created by nunonelas on 22/11/17.
 */

public class Note implements Serializable {

    private String note;
    private String date;

    public Note () {

    }

    public Note(String note, String date) {
        this.note = note;
        this.date = date;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }
}

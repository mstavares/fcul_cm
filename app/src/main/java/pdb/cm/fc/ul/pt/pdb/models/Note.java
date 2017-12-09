package pdb.cm.fc.ul.pt.pdb.models;

import java.io.Serializable;


public class Note implements Serializable {

    private String note;
    private String date;

    public Note(String note, String date) {
        this.note = note;
        this.date = date;
    }

    public String getNote() {
        return note;
    }

    public String getDate() {
        return date;
    }

}

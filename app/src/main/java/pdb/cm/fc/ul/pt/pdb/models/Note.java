package pdb.cm.fc.ul.pt.pdb.models;

import java.io.Serializable;


public class Note implements Serializable {

    private String id;
    private String note;
    private String date;

    public Note () {

    }

    public Note(String id, String note, String date) {
        this.id = id;
        this.note = note;
        this.date = date;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
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

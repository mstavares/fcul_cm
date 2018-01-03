package pdb.cm.fc.ul.pt.pdb.models;


import pdb.cm.fc.ul.pt.pdb.utilities.Utilities;

public class Shake {

    private String date;
    private double shake;

    public Shake() {}

    public Shake(double shake) {
        this.shake = shake;
        date = Utilities.getTimestamp();
    }

    public double getShake() {
        return shake;
    }

    public String getDate() {
        return date;
    }

}

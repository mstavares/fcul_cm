package pdb.cm.fc.ul.pt.pdb.models;


import pdb.cm.fc.ul.pt.pdb.utilities.Utilities;

public class Shake {

    private String mDate;
    private double mShake;

    public Shake() {}

    public Shake(double shake) {
        mShake = shake;
        mDate = Utilities.getTimestamp();
    }

    public double getShake() {
        return mShake;
    }

    public String getDate() {
        return mDate;
    }

}

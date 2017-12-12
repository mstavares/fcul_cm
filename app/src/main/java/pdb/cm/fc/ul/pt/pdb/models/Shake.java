package pdb.cm.fc.ul.pt.pdb.models;


import java.sql.Timestamp;

import pdb.cm.fc.ul.pt.pdb.utilities.Utilities;

public class Shake {

    private String mDate;
    private double mXvalue, mYvalue, mZvalue;

    public Shake() {}

    public Shake(double xValue, double yValue, double zValue) {
        mXvalue = xValue;
        mYvalue = yValue;
        mZvalue = zValue;
        mDate = Utilities.getTimestamp();
    }

    public double getXvalue() {
        return mXvalue;
    }

    public double getYvalue() {
        return mYvalue;
    }

    public double getZvalue() {
        return mZvalue;
    }

    public String getDate() {
        return mDate;
    }

}

package pdb.cm.fc.ul.pt.pdb.utilities;


import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public abstract class Utilities {

    private static final String TIMESTAMP_FORMAT = "dd/M/yyyy HH:mm:ss";

    public static String getTimestamp() {
        return new SimpleDateFormat(TIMESTAMP_FORMAT).format(new Date());
    }

    public static double computeAverage(ArrayList<Integer> data) {
        double sum = 0.0;
        for(double value : data)
            sum += value;
        return sum / data.size();
    }
}

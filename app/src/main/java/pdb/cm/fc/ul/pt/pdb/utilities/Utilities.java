package pdb.cm.fc.ul.pt.pdb.utilities;

import android.util.Log;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public abstract class Utilities {

    private static final String TIMESTAMP_FORMAT = "dd/M/yyyy HH:mm:ss";

    public static String getTimestamp() {
        return new SimpleDateFormat(TIMESTAMP_FORMAT).format(new Date());
    }

    public static double computeAverage(ArrayList<Double> data) {
        double sum = 0.0;
        for(double value : data)
            sum += value;
        return sum / data.size();
    }

    public static String getFirstDayOfCurrentWeek(){
        Calendar c = Calendar.getInstance(Locale.US);
        c.set(Calendar.DAY_OF_WEEK,Calendar.SUNDAY);
        c.set(Calendar.HOUR_OF_DAY,0);
        c.set(Calendar.MINUTE,0);
        c.set(Calendar.SECOND,0);
        return new SimpleDateFormat(TIMESTAMP_FORMAT).format(c.getTime());
    }

    public static String getLastDay(){
        Calendar c = Calendar.getInstance();
        c.add(Calendar.DATE, -1);
        return new SimpleDateFormat(TIMESTAMP_FORMAT).format(c.getTime());
    }

    public static float getHourRightNow(){
        Calendar c = Calendar.getInstance();
        return c.get(Calendar.HOUR_OF_DAY);
    }

}

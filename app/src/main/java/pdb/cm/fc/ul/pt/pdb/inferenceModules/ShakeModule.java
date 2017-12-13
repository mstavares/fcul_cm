package pdb.cm.fc.ul.pt.pdb.inferenceModules;


import android.content.Context;
import android.util.Log;

import java.util.ArrayList;

import pdb.cm.fc.ul.pt.pdb.models.Shake;
import pdb.cm.fc.ul.pt.pdb.preferences.UserPreferences;
import pdb.cm.fc.ul.pt.pdb.sensors.accelerometer.AccelerometerListener;
import pdb.cm.fc.ul.pt.pdb.sensors.accelerometer.AccelerometerManager;
import pdb.cm.fc.ul.pt.pdb.sensors.screen.ScreenListener;
import pdb.cm.fc.ul.pt.pdb.sensors.screen.ScreenManager;
import pdb.cm.fc.ul.pt.pdb.services.firebase.FirebaseDoente;
import pdb.cm.fc.ul.pt.pdb.utilities.Utilities;

public class ShakeModule implements ScreenListener, AccelerometerListener.onSensorChanged {

    private static final String TAG = ShakeModule.class.getSimpleName();
    private static final int SAMPLING_EVALUATION = 50;
    private ArrayList<Integer> xData = new ArrayList<>();
    private ArrayList<Integer> yData = new ArrayList<>();
    private ArrayList<Integer> zData = new ArrayList<>();
    private boolean isRegistered = false;
    private String mUserPaciente;

    public ShakeModule(Context context) {
        ScreenManager.registerListener(this);
        mUserPaciente = UserPreferences.getUser(context);
        AccelerometerManager.registerListener(this);
    }

    @Override
    public void screenIsOn() {
        Log.i(TAG, "screenIsOn");
        registerListener();
    }

    @Override
    public void screenIsOff() {
        Log.i(TAG, "screenIsOff");
        unregisterListener();
    }

    @Override
    public void onSensorChanged(float xAccel, float yAccel, float zAccel) {
        xData.add((int) xAccel); yData.add((int) yAccel); zData.add((int) zAccel);
        evaluateData();

    }

    private synchronized void registerListener() {
        if(!isRegistered) {
            Log.i(TAG, "Register listener");
            isRegistered = true;
            AccelerometerManager.registerListener(this);
        }

    }

    private synchronized void unregisterListener() {
        if(isRegistered) {
            Log.i(TAG, "Unregister listener");
            isRegistered = false;
            AccelerometerManager.unregisterListener(this);
        }

    }

    private synchronized void evaluateData() {
        if(xData.size() == SAMPLING_EVALUATION) {
            double xAverage = Utilities.computeAverage(xData);
            double yAverage = Utilities.computeAverage(yData);
            double zAverage = Utilities.computeAverage(zData);
            Log.i(TAG, "xAverage = " + xAverage + " yAverage = " + yAverage + " zAverage = " + zAverage);
            xData.clear(); yData.clear(); zData.clear();
            FirebaseDoente.sendShakeData(mUserPaciente, new Shake(xAverage, yAverage, zAverage));
        }
    }

    public void close() {
        ScreenManager.unregisterListener(this);
        AccelerometerManager.unregisterListener(this);
    }

}

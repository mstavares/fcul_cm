package pdb.cm.fc.ul.pt.pdb.inferenceModules;


import android.content.Context;
import android.os.Handler;
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

public class ShakeModule implements Runnable, ScreenListener, AccelerometerListener.onSensorChanged {

    private static final String TAG = ShakeModule.class.getSimpleName();
    private static final int SAMPLING_EVALUATION = 5 * 1000;
    private ArrayList<Double> mData = new ArrayList<>();
    private Handler mHandler = new Handler();
    private boolean isRegistered = false;
    private boolean isEvaluating = false;
    private String mUser;

    public ShakeModule(Context context) {
        ScreenManager.registerListener(this);
        mUser = UserPreferences.getUser(context);
        mHandler.postDelayed(this, SAMPLING_EVALUATION);
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
        if(!isEvaluating)
            mData.add(Math.sqrt(xAccel * xAccel + yAccel * yAccel + zAccel * zAccel));
    }

    @Override
    public void run() {
        if(mData.size() > 0) {
            isEvaluating = true;
            double average = Utilities.computeAverage(mData);
            FirebaseDoente.sendShakeData(mUser, new Shake(average));
            mHandler.postDelayed(this, SAMPLING_EVALUATION);
            isEvaluating = false;
        }
    }

    private synchronized void registerListener() {
        if(!isRegistered) {
            Log.i(TAG, "Register listener");
            isRegistered = true;
            AccelerometerManager.registerListener(this);
            mHandler.postDelayed(this, SAMPLING_EVALUATION);
        }
    }

    private synchronized void unregisterListener() {
        if(isRegistered) {
            Log.i(TAG, "Unregister listener");
            isRegistered = false;
            AccelerometerManager.unregisterListener(this);
        }
    }

    public void close() {
        mHandler.removeCallbacks(this);
        ScreenManager.unregisterListener(this);
        AccelerometerManager.unregisterListener(this);
    }

}

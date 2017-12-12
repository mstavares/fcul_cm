package pdb.cm.fc.ul.pt.pdb.sensors.proximity;


import android.content.Context;
import android.content.pm.PackageManager;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.util.Log;

import pdb.cm.fc.ul.pt.pdb.R;
import pdb.cm.fc.ul.pt.pdb.exceptions.SensorNotFoundException;

public class Proximity implements SensorEventListener {

    private static final String TAG = Proximity.class.getSimpleName();
    private static final int SENSOR_SENSITIVITY = 4;
    private ProximityListener mProximityListener;
    private SensorManager mSensorManager;
    private boolean mIsRunning;


    public Proximity(Context context, ProximityListener proximityListener) throws SensorNotFoundException {
        checkIfAccelerometerIsAvailable(context);
        mProximityListener = proximityListener;
        start(context);
    }

    private void checkIfAccelerometerIsAvailable(Context context) throws SensorNotFoundException {
        PackageManager pm = context.getPackageManager();
        if(!pm.hasSystemFeature(PackageManager.FEATURE_SENSOR_LIGHT))
            throw new SensorNotFoundException(context.getString(R.string.sensor_not_found_message, context.getString(R.string.Light)));
    }

    private void start(Context context) {
        Log.i(TAG, "start was invoked");
        mSensorManager = (SensorManager) context.getSystemService(Context.SENSOR_SERVICE);
        mIsRunning = mSensorManager.registerListener(this, mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER), SensorManager.SENSOR_DELAY_GAME);
    }

    @Override
    public void onSensorChanged(SensorEvent sensorEvent) {
        if (sensorEvent.values[0] >= -SENSOR_SENSITIVITY && sensorEvent.values[0] <= SENSOR_SENSITIVITY) {
            Log.i(TAG, "Is near.");
            mProximityListener.onNear();
        } else {
            Log.i(TAG, "Is far.");
            mProximityListener.onFar();
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int i) { }

    private void stopListening() {
        Log.i(TAG, "stopListening was invoked");
        mSensorManager.unregisterListener(this);
        mIsRunning = false;
    }

    void close(){
        Log.i(TAG, "close was invoked");
        if(mIsRunning){
            stopListening();
        }
    }
}

package pdb.cm.fc.ul.pt.pdb.sensors.accelerometer;

import android.content.Context;
import android.content.pm.PackageManager;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.util.Log;

import pdb.cm.fc.ul.pt.pdb.R;
import pdb.cm.fc.ul.pt.pdb.exceptions.SensorNotFoundException;

public class Accelerometer implements SensorEventListener {


    /** This variable is used to debug MotionManager class */
    private static final String TAG = Accelerometer.class.getSimpleName();

    private AccelerometerListener.onSensorChanged mListener;

    /** Indicates whether or not Accelerometer Sensor is running */
    private boolean mIsRunning = false;

    /** This class is to access functionality of Sensor Manager */
    private SensorManager mSensorManager;

    /**
     * This method is the constructor of MotionManager class
     * @param context application context
     */
    Accelerometer(Context context, AccelerometerListener.onSensorChanged listener) throws SensorNotFoundException {
        Log.i(TAG, "Accelerometer constructor was invoked");
        checkIfAccelerometerIsAvailable(context);
        mListener = listener;
        start(context);
    }

    private void checkIfAccelerometerIsAvailable(Context context) throws SensorNotFoundException {
        PackageManager pm = context.getPackageManager();
        if(!pm.hasSystemFeature(PackageManager.FEATURE_SENSOR_ACCELEROMETER))
            throw new SensorNotFoundException(context.getString(R.string.sensor_not_found_message, context.getString(R.string.Accelerometer)));
    }

    /**
     * This method register the accelerometer and put it running
     * @param context application context
     */
    private void start(Context context) {
        Log.i(TAG, "start was invoked");
        mSensorManager = (SensorManager) context.getSystemService(Context.SENSOR_SERVICE);
        mIsRunning = mSensorManager.registerListener(this, mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER), SensorManager.SENSOR_DELAY_GAME);
    }

    /**
     * This method provides the accelerometer data
     * @param event accelerometer data
     */
    @Override
    public void onSensorChanged(final SensorEvent event) {
        mListener.onSensorChanged(event.values[0], event.values[1], event.values[2]);
    }

    /**
     * This method is triggered when the accelerometer's accuracy changes
     * @param sensor sensor which changed it's accuracy
     * @param accuracy new accuracy
     */
    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {
        Log.i(TAG, "The accelerometer accuracy has been changed");
    }

    /**
     * This method stops and unregisters the accelerometer
     */
    private void stopListening() {
        Log.i(TAG, "stopListening was invoked");
        mSensorManager.unregisterListener(this);
        mIsRunning = false;
    }

    /**
     * This method checks if the accelerometer is running, if it's running stops it
     */
    void close(){
        Log.i(TAG, "close was invoked");
        if(mIsRunning){
            stopListening();
        }
    }

}
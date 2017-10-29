package pdb.cm.fc.ul.pt.pdb.sensors.accelerometer;


import android.content.Context;

import java.util.ArrayList;

import pdb.cm.fc.ul.pt.pdb.exceptions.SensorNotFoundException;

public class AccelerometerManager implements AccelerometerListener.onSensorChanged {

    private static final float THRESHOLD = 15.0f;
    private static final int INTERVAL = 400;
    private static ArrayList<AccelerometerListener> sListeners = new ArrayList<>();
    private float mLastXaccel, mLastYaccel, mLastZaccel, mForce;
    private long mLastUpdate, mLastShake, mNow;
    private Accelerometer mAccelerometer;

    public AccelerometerManager (Context context) throws SensorNotFoundException {
        mAccelerometer = new Accelerometer(context, this);
    }

    public static void registerListener(AccelerometerListener listener) {
        sListeners.add(listener);
    }

    public static void unRegisterListener(AccelerometerListener listener) {
        sListeners.remove(listener);
    }

    @Override
    public void onSensorChanged(float xAccel, float yAccel, float zAccel) {
        mNow = System.currentTimeMillis();
        if(mLastUpdate == 0)
            mLastShake = mNow;
        else
            evaluateShakeMovement(xAccel, yAccel, zAccel);
        updateSensorData(xAccel, yAccel, zAccel);
        notifySensorChanged(xAccel, yAccel, zAccel);
    }

    private void updateSensorData(float xAccel, float yAccel, float zAccel) {
        mLastUpdate = mNow;
        mLastXaccel = xAccel;
        mLastYaccel = yAccel;
        mLastZaccel = zAccel;
    }

    private void evaluateShakeMovement(float xAccel, float yAccel, float zAccel) {
        long timeDiff = mNow - mLastUpdate;
        if(timeDiff > 0) {
            mForce = Math.abs(xAccel + yAccel + zAccel - mLastXaccel - mLastYaccel - mLastZaccel);
            if(Float.compare(mForce, THRESHOLD) > 0) {
                if(mNow - mLastShake >= INTERVAL) {
                    mLastShake = mNow;
                    notifyShakeMovement();
                }
            }
        }
    }

    private void notifySensorChanged(float xAccel, float yAccel, float zAccel) {
        for(AccelerometerListener listener : sListeners) {
            if(listener instanceof AccelerometerListener.onSensorChanged) {
                ((onSensorChanged) listener).onSensorChanged(xAccel, yAccel, zAccel);
            }
        }
    }

    private void notifyShakeMovement() {
        for(AccelerometerListener listener : sListeners) {
            if(listener instanceof AccelerometerListener.onShake) {
                ((onShake) listener).onShake();
            }
        }
    }

    public void close() {
        if (mAccelerometer != null) {
            mAccelerometer.close();
            mAccelerometer = null;
        }
    }

}

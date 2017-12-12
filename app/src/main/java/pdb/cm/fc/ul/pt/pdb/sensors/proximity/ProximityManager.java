package pdb.cm.fc.ul.pt.pdb.sensors.proximity;


import android.content.Context;
import android.util.Log;

import java.util.ArrayList;

import pdb.cm.fc.ul.pt.pdb.exceptions.SensorNotFoundException;
import pdb.cm.fc.ul.pt.pdb.sensors.accelerometer.AccelerometerListener;

public class ProximityManager implements ProximityListener {

    private static final String TAG = ProximityManager.class.getSimpleName();
    private static ArrayList<ProximityListener> sListeners = new ArrayList<>();
    private Proximity mProximity;

    public ProximityManager(Context context) throws SensorNotFoundException {
        mProximity = new Proximity(context, this);
    }

    public static void registerListener(ProximityListener listener) {
        sListeners.add(listener);
        Log.i(TAG, "Listener registered");
    }

    public static void unregisterListener(ProximityListener listener) {
        sListeners.remove(listener);
        Log.i(TAG, "Listener unregistered");
    }

    @Override
    public void onNear() {
        Log.i(TAG, "Received NEAR from sensor");
        for(ProximityListener listener : sListeners) {
            listener.onNear();
        }
    }

    @Override
    public void onFar() {
        Log.i(TAG, "Received FAR from sensor");
        for(ProximityListener listener : sListeners) {
            listener.onFar();
        }
    }

    public void close() {
        mProximity.close();
    }
}

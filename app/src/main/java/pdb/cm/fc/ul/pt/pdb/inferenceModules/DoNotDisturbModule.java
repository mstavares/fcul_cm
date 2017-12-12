package pdb.cm.fc.ul.pt.pdb.inferenceModules;


import android.util.Log;

import java.util.ArrayList;

import pdb.cm.fc.ul.pt.pdb.sensors.proximity.ProximityListener;
import pdb.cm.fc.ul.pt.pdb.sensors.proximity.ProximityManager;

public class DoNotDisturbModule implements ProximityListener {

    private static final String TAG = DoNotDisturbModule.class.getSimpleName();
    private static ArrayList<DoNotDisturbListener> sListeners = new ArrayList<>();
    private static final int THRESHOLD = 10;
    private int mNear, mFar;

    public DoNotDisturbModule() {
        ProximityManager.registerListener(this);
    }

    public static void registerListener(DoNotDisturbListener listener) {
        sListeners.add(listener);
    }

    public static void unregisterListener(DoNotDisturbListener listener) {
        sListeners.remove(listener);
    }

    @Override
    public void onNear() {
        Log.i(TAG, "NEAR received");
        mFar = 0;
        if(++mNear > THRESHOLD) {
            for (DoNotDisturbListener listener : sListeners) {
                listener.onDoNotDisturb();
            }
        }
    }

    @Override
    public void onFar() {
        Log.i(TAG, "NEAR received");
        mNear = 0;
        if(++mFar > THRESHOLD) {
            for (DoNotDisturbListener listener : sListeners) {
                listener.onRemoveDoNotDisturb();
            }
        }
    }

    public void close() {
        ProximityManager.unregisterListener(this);
    }

}

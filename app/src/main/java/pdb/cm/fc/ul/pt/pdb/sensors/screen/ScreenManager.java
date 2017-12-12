package pdb.cm.fc.ul.pt.pdb.sensors.screen;


import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.util.Log;

import java.util.ArrayList;

public class ScreenManager implements ScreenListener {

    private static String TAG = ScreenManager.class.getSimpleName();
    private static ArrayList<ScreenListener> sListeners = new ArrayList<>();
    private ScreenReceiver mScreenReceiver;
    private Context mContext;

    public ScreenManager(Context context) {
        mContext = context;
        mScreenReceiver = new ScreenReceiver(this);
        IntentFilter filter = new IntentFilter(Intent.ACTION_SCREEN_ON);
        filter.addAction(Intent.ACTION_SCREEN_OFF);
        context.registerReceiver(mScreenReceiver, filter);
    }

    public static void registerListener(ScreenListener listener) {
        sListeners.add(listener);
        Log.i(TAG, "Listener registered");
    }

    public static void unregisterListener(ScreenListener listener) {
        sListeners.remove(listener);
        Log.i(TAG, "Listener unregistered");
    }

    @Override
    public void screenIsOn() {
        Log.i(TAG, "Screen is on");
        for(ScreenListener listener : sListeners) {
            listener.screenIsOn();
        }
    }

    @Override
    public void screenIsOff() {
        Log.i(TAG, "Screen is off");
        for(ScreenListener listener : sListeners) {
            listener.screenIsOff();
        }
    }

    public void close() {
        mContext.unregisterReceiver(mScreenReceiver);
    }

}

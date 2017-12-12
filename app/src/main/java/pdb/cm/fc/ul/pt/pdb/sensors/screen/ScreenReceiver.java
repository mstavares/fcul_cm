package pdb.cm.fc.ul.pt.pdb.sensors.screen;


import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public class ScreenReceiver extends BroadcastReceiver {

    private ScreenListener mListener;

    public ScreenReceiver(ScreenListener listener) {
        mListener = listener;
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        if (intent.getAction().equals(Intent.ACTION_SCREEN_ON)) {
            mListener.screenIsOn();
        } else if (intent.getAction().equals(Intent.ACTION_SCREEN_OFF))  {
            mListener.screenIsOff();
        }
    }
}

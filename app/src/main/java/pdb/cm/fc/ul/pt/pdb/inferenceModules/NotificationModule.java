package pdb.cm.fc.ul.pt.pdb.inferenceModules;


import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.os.Handler;
import android.util.Log;

import pdb.cm.fc.ul.pt.pdb.R;
import pdb.cm.fc.ul.pt.pdb.activities.LoginActivity;
import pdb.cm.fc.ul.pt.pdb.sensors.screen.ScreenListener;

public class NotificationModule implements Runnable, ScreenListener {

    private static final String TAG = NotificationModule.class.getSimpleName();
    private static final int NOTIFICATION_TIME_WINDOW = 120 * 1000;
    private Handler mHander = new Handler();
    private Context mContext;

    public NotificationModule(Context context) {
        mContext = context;
    }

    @Override
    public void screenIsOn() {
        mHander.removeCallbacks(this);
    }

    @Override
    public void screenIsOff() {
        mHander.postDelayed(this, NOTIFICATION_TIME_WINDOW);
    }

    @Override
    public void run() {
        createNotification(new Intent(mContext, LoginActivity.class));
        mHander.postDelayed(this, NOTIFICATION_TIME_WINDOW);
    }

    private void createNotification(Intent intent) {
        Log.i(TAG, "Creating notification");

        Notification.Builder builder = new Notification.Builder(mContext);
        builder.setTicker("NSense");
        builder.setContentTitle(mContext.getString(R.string.title));
        builder.setContentText(mContext.getString(R.string.message));
        builder.setSmallIcon(R.mipmap.ic_launcher);
        builder.setContentIntent(PendingIntent.getActivity(mContext, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT));
        builder.setLargeIcon(BitmapFactory.decodeResource(mContext.getResources(), R.mipmap.ic_launcher));
        builder.setAutoCancel(true);
        builder.setVibrate(new long[] { 1000, 1000, 1000, 1000, 1000 });

        NotificationManager notificationManager = (NotificationManager) mContext.getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.notify(R.mipmap.ic_launcher, builder.build());
    }

    public void close() {
        mHander.removeCallbacks(this);
    }

}

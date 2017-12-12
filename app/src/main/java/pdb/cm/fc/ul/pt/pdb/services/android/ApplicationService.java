package pdb.cm.fc.ul.pt.pdb.services.android;

import android.app.Notification;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.support.v4.app.NotificationCompat;
import android.util.Log;
import android.view.View;

import pdb.cm.fc.ul.pt.pdb.R;
import pdb.cm.fc.ul.pt.pdb.activities.SplashActivity;
import pdb.cm.fc.ul.pt.pdb.exceptions.SensorNotFoundException;
import pdb.cm.fc.ul.pt.pdb.inferenceModules.NotificationModule;
import pdb.cm.fc.ul.pt.pdb.inferenceModules.ShakeModule;
import pdb.cm.fc.ul.pt.pdb.sensors.accelerometer.AccelerometerManager;
import pdb.cm.fc.ul.pt.pdb.sensors.proximity.Proximity;
import pdb.cm.fc.ul.pt.pdb.sensors.proximity.ProximityManager;
import pdb.cm.fc.ul.pt.pdb.sensors.screen.ScreenManager;


public class ApplicationService extends Service {

    private static final String TAG = ApplicationService.class.getSimpleName();
    private static final int SERVICE_ID = 112;

    private AccelerometerManager mAccelerometerManager;
    private ProximityManager mProximityManager;
    private ScreenManager mScreenManager;
    private NotificationModule mNotificationModule;
    private ShakeModule mShakeModule;

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int i, int j) {
        Log.i(TAG, "onStartCommand was invoked");
        try {
			/* Enable the notification icon */
            runAsForeground();
			/* Initializing the sensors */
            mAccelerometerManager = new AccelerometerManager(this);
            //mProximityManager = new ProximityManager(this);
            mScreenManager = new ScreenManager(this);
            mShakeModule = new ShakeModule(this);
            mNotificationModule = new NotificationModule(this);
        } catch (SensorNotFoundException e) {
            e.showDialogError(this);
        }
        return START_STICKY;
    }

    /**
     * This method provides the functionality to run the service as Foreground
     */
    public void runAsForeground(){
        Log.i(TAG, "runAsForeground was invoked");
        Intent notificationIntent = new Intent(this, SplashActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0,
                notificationIntent, PendingIntent.FLAG_UPDATE_CURRENT);

        Notification notification = new NotificationCompat.Builder(this)
                .setSmallIcon(R.drawable.ic_app_logo)
                .setLargeIcon(BitmapFactory.decodeResource(getResources(), R.mipmap.ic_launcher))
                .setContentTitle(getString(R.string.app_name))
                .setContentText(getString(R.string.service_name))
                .setContentIntent(pendingIntent)
                .build();

        int smallIconId = getResources().getIdentifier("right_icon", "id", android.R.class.getPackage().getName());
        if (smallIconId != 0) {
            if (notification.contentView!=null)
                notification.contentView.setViewVisibility(smallIconId, View.INVISIBLE);
        }

        startForeground(SERVICE_ID, notification);
    }

    public void close() {
        if(mAccelerometerManager != null)
            mAccelerometerManager.close();
        if(mProximityManager != null)
            mScreenManager.close();
        if(mScreenManager != null)
            mScreenManager.close();
        if(mShakeModule != null)
            mShakeModule.close();
        if(mNotificationModule != null)
            mNotificationModule.close();
    }

    @Override
    public void onDestroy() {
        Log.i(TAG, "onDestroy was invoked");
        close();
        super.onDestroy();
    }

}

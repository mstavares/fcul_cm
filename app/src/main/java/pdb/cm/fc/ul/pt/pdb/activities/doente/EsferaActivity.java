package pdb.cm.fc.ul.pt.pdb.activities.doente;

import android.content.Intent;
import android.graphics.Point;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Display;

import pdb.cm.fc.ul.pt.pdb.R;
import pdb.cm.fc.ul.pt.pdb.sensors.accelerometer.Accelerometer;
import pdb.cm.fc.ul.pt.pdb.sensors.accelerometer.AccelerometerListener;
import pdb.cm.fc.ul.pt.pdb.sensors.accelerometer.AccelerometerManager;
import pdb.cm.fc.ul.pt.pdb.views.BallView;

public class EsferaActivity extends AppCompatActivity implements AccelerometerListener.onSensorChanged {

    private static final String TAG = EsferaActivity.class.getSimpleName();
    private BallView mBallView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.activity_esfera);
        setup();
        setContentView(mBallView);
    }

    private void setup() {
        mBallView = new BallView(this);
        AccelerometerManager.registerListener(this);
    }

    @Override
    public void onSensorChanged(float xAccel, float yAccel, float zAccel) {
        Log.i(TAG, "xAccel: " + xAccel + " yAccel: " + yAccel);
        mBallView.updateBall(xAccel, -yAccel);
    }

    @Override
    public void onBackPressed() {
        startActivity(new Intent(this, DoenteMainActivity.class));
        finish();
    }

    @Override
    public void onDestroy() {
        AccelerometerManager.unRegisterListener(this);
        super.onDestroy();
    }

}

package pdb.cm.fc.ul.pt.pdb.activities.doente;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import pdb.cm.fc.ul.pt.pdb.sensors.accelerometer.AccelerometerListener;
import pdb.cm.fc.ul.pt.pdb.sensors.accelerometer.AccelerometerManager;
import pdb.cm.fc.ul.pt.pdb.views.PitchView;

public class EsferaActivity extends AppCompatActivity implements AccelerometerListener.onSensorChanged {

    private static final String TAG = EsferaActivity.class.getSimpleName();

    private PitchView mPitchView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setup();
        setContentView(mPitchView);
    }

    private void setup() {
        mPitchView = new PitchView(this);
        AccelerometerManager.registerListener(this);
    }

    @Override
    public void onSensorChanged(float xAccel, float yAccel, float zAccel) {
        Log.i(TAG, "xAccel: " + xAccel + " yAccel: " + yAccel);
        mPitchView.updateBall(xAccel, -yAccel);
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

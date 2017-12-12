package pdb.cm.fc.ul.pt.pdb.activities.doente;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.TextView;

import butterknife.BindView;
import pdb.cm.fc.ul.pt.pdb.R;
import pdb.cm.fc.ul.pt.pdb.interfaces.doente.Esfera;
import pdb.cm.fc.ul.pt.pdb.preferences.UserPreferences;
import pdb.cm.fc.ul.pt.pdb.sensors.accelerometer.AccelerometerListener;
import pdb.cm.fc.ul.pt.pdb.sensors.accelerometer.AccelerometerManager;
import pdb.cm.fc.ul.pt.pdb.views.PitchView;

public class EsferaActivity extends AppCompatActivity implements AccelerometerListener.onSensorChanged, Esfera.View {

    private static final String TAG = EsferaActivity.class.getSimpleName();

    @BindView(R.id.score) TextView mScoreView;
    @BindView(R.id.time) TextView mTimeView;

    private PitchView mPitchView;
    private String mEmail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setup();
        setContentView(mPitchView);
    }

    private void setup() {
        mPitchView = new PitchView(this);
        AccelerometerManager.registerListener(this);
        mEmail = UserPreferences.getEmail(this);
    }

    @Override
    public void onChangeTime(int time) {
        mTimeView.setText(getString(R.string.time, time));
    }

    @Override
    public void onChangeScore(int score) {
        mScoreView.setText(getString(R.string.score, score));
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
        AccelerometerManager.unregisterListener(this);
        super.onDestroy();
    }

}

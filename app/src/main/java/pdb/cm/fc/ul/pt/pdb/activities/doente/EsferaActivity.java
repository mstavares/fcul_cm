package pdb.cm.fc.ul.pt.pdb.activities.doente;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;
import pdb.cm.fc.ul.pt.pdb.R;
import pdb.cm.fc.ul.pt.pdb.activities.dialog.MessageDialogActivity;
import pdb.cm.fc.ul.pt.pdb.interfaces.doente.Esfera;
import pdb.cm.fc.ul.pt.pdb.preferences.UserPreferences;
import pdb.cm.fc.ul.pt.pdb.presenters.doente.EsferaPresenter;
import pdb.cm.fc.ul.pt.pdb.sensors.accelerometer.AccelerometerListener;
import pdb.cm.fc.ul.pt.pdb.sensors.accelerometer.AccelerometerManager;
import pdb.cm.fc.ul.pt.pdb.views.PitchView;
import pdb.cm.fc.ul.pt.pdb.views.PitchViewListener;

public class EsferaActivity extends AppCompatActivity implements AccelerometerListener.onSensorChanged,
        Esfera.View, PitchViewListener {

    private static final String TAG = EsferaActivity.class.getSimpleName();

    @BindView(R.id.score) TextView mScoreView;
    @BindView(R.id.time) TextView mTimeView;

    private Esfera.Presenter mPresenter;
    private PitchView mPitchView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_esfera);
        setup();
    }

    private void setup() {
        ButterKnife.bind(this);
        mPitchView = findViewById(R.id.pitch);
        mPitchView.setOnPitchViewListener(this);
        mPresenter = new EsferaPresenter(this, this);
        AccelerometerManager.registerListener(this);
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
    public void onGoal() {
        mPresenter.onGoal();
    }

    @Override
    public void onTimeUp(int score, int time) {
        startActivity(new Intent(this, MessageDialogActivity.class)
                .putExtra(MessageDialogActivity.TITLE, getString(R.string.win_title))
                .putExtra(MessageDialogActivity.MESSAGE, getString(R.string.win_message, score))
                .putExtra(MessageDialogActivity.TIME, getString(R.string.win_time, time))
                .addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        );
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

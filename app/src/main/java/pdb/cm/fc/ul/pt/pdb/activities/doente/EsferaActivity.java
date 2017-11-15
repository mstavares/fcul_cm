package pdb.cm.fc.ul.pt.pdb.activities.doente;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import pdb.cm.fc.ul.pt.pdb.Maze;
import pdb.cm.fc.ul.pt.pdb.MazeCreator;
import pdb.cm.fc.ul.pt.pdb.views.MazeView;
import pdb.cm.fc.ul.pt.pdb.sensors.accelerometer.AccelerometerListener;
import pdb.cm.fc.ul.pt.pdb.sensors.accelerometer.AccelerometerManager;

public class EsferaActivity extends AppCompatActivity implements AccelerometerListener.onSensorChanged {

    private static final String TAG = EsferaActivity.class.getSimpleName();
    private MazeView mMazeView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.activity_esfera);
        setup();
        setContentView(mMazeView);
    }

    private void setup() {
        int mazeNum = 1 + (int)(Math.random() * ((3 - 1) + 1));
        Maze maze = MazeCreator.getMaze(mazeNum);
        mMazeView = new MazeView(this,maze);
        //mBallView = new BallView(this);
        AccelerometerManager.registerListener(this);
    }

    @Override
    public void onSensorChanged(float xAccel, float yAccel, float zAccel) {
        Log.i(TAG, "xAccel: " + xAccel + " yAccel: " + yAccel);
        mMazeView.updateBall(xAccel, -yAccel);
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

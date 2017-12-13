package pdb.cm.fc.ul.pt.pdb.activities;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import pdb.cm.fc.ul.pt.pdb.R;

public class SplashActivity extends AppCompatActivity implements Runnable {

    private static final String TAG = SplashActivity.class.getSimpleName();

    private static final int SPLASH_TIMER = 2 * 1000;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        Log.i(TAG, "onCreate");
        setup();
    }

    private void setup() {
        new Handler().postDelayed(this, SPLASH_TIMER);
    }

    @Override
    public void run() {
        startActivity(new Intent(this, LoginActivity.class));
        finish();
    }

}

package pdb.cm.fc.ul.pt.pdb.activities;

import android.content.Intent;
import android.os.Handler;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import pdb.cm.fc.ul.pt.pdb.R;
import pdb.cm.fc.ul.pt.pdb.services.android.ApplicationService;

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
        startService();
        new Handler().postDelayed(this, SPLASH_TIMER);
    }

    private void startService() {
        Log.i(TAG, "startService was invoked");
        startService(new Intent(this, ApplicationService.class));
    }

    @Override
    public void run() {
        startActivity(new Intent(this, LoginActivity.class));
        finish();
    }

}

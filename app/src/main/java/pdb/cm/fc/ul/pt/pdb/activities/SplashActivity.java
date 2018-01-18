package pdb.cm.fc.ul.pt.pdb.activities;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import pdb.cm.fc.ul.pt.pdb.R;
import pdb.cm.fc.ul.pt.pdb.activities.doente.DoenteMainActivity;
import pdb.cm.fc.ul.pt.pdb.activities.medico.MedicoMainActivity;

public class SplashActivity extends AppCompatActivity implements Runnable {

    private static final String TAG = SplashActivity.class.getSimpleName();

    private static final int SPLASH_TIMER = 2 * 1000;

    private FirebaseAuth mAuth;

    private FirebaseUser currentUser;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        mAuth = FirebaseAuth.getInstance();
        currentUser = mAuth.getCurrentUser();
        Log.i(TAG, "onCreate");
        setup();
    }

    private void setup() {
        new Handler().postDelayed(this, SPLASH_TIMER);
    }

    @Override
    public void run() {
        if(currentUser != null){
            if(currentUser.getEmail().contains("paciente")){
                startActivity(new Intent(this, DoenteMainActivity.class));
            } else {
                startActivity(new Intent(this, MedicoMainActivity.class));
            }
        }
        else {
            startActivity(new Intent(this, LoginActivity.class));
        }
        startActivity(new Intent(this, LoginActivity.class));
        finish();
    }

}

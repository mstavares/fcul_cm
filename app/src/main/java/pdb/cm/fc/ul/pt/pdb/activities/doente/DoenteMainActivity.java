package pdb.cm.fc.ul.pt.pdb.activities.doente;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;

import com.google.firebase.auth.FirebaseAuth;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import pdb.cm.fc.ul.pt.pdb.R;
import pdb.cm.fc.ul.pt.pdb.activities.LoginActivity;
import pdb.cm.fc.ul.pt.pdb.models.Doente;
import pdb.cm.fc.ul.pt.pdb.services.android.ApplicationService;

public class DoenteMainActivity extends AppCompatActivity {

    private static final String TAG = DoenteMainActivity.class.getSimpleName();
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_doente_main);
        ButterKnife.bind(this);
        mAuth = FirebaseAuth.getInstance();
        startService();
    }

    private void startService() {
        Log.i(TAG, "startService was invoked");
        startService(new Intent(this, ApplicationService.class));
    }

    @OnClick(R.id.button1)
    public void button1() {
        startActivity(new Intent(this, TecladoActivity.class));
        finish();
    }

    @OnClick(R.id.button2)
    public void button2() {
        startActivity(new Intent(this, EsferaActivity.class));
        finish();
    }

    @OnClick(R.id.button3)
    public void button3() {
        mAuth.signOut();
        stopService(new Intent(this, ApplicationService.class));
        startActivity(new Intent(this, LoginActivity.class));
        finish();
    }

}

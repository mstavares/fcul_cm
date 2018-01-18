package pdb.cm.fc.ul.pt.pdb.activities.doente;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import pdb.cm.fc.ul.pt.pdb.R;
import pdb.cm.fc.ul.pt.pdb.activities.LoginActivity;
import pdb.cm.fc.ul.pt.pdb.preferences.UserPreferences;
import pdb.cm.fc.ul.pt.pdb.services.android.ApplicationService;

public class DoenteMainActivity extends AppCompatActivity {

    private static final String TAG = DoenteMainActivity.class.getSimpleName();
    private FirebaseAuth mAuth;

    @BindView(R.id.welcome) TextView mWelcomeView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_doente_main);
        setup();
    }

    private void setup() {
        ButterKnife.bind(this);
        mAuth = FirebaseAuth.getInstance();
        mWelcomeView.setText(getString(R.string.welcome, UserPreferences.getUser(this)));
    }

    @OnClick(R.id.keyboard_btn)
    public void openKeyboardGame() {
        startActivity(new Intent(this, TecladoActivity.class));
        finish();
    }

    @OnClick(R.id.sphere_btn)
    public void openSphereGame() {
        startActivity(new Intent(this, EsferaActivity.class));
        finish();
    }

    @OnClick(R.id.scores_btn)
    public void openScores() {
        startActivity(new Intent(this, PontuacoesActivity.class));
        finish();
    }

    @OnClick(R.id.signout_btn)
    public void signOut() {
        mAuth.signOut();
        stopService(new Intent(this, ApplicationService.class));
        startActivity(new Intent(this, LoginActivity.class));
        finish();
    }

}

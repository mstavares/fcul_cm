package pdb.cm.fc.ul.pt.pdb.activities.doente;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Button;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import pdb.cm.fc.ul.pt.pdb.R;

public class DoenteMainActivity extends AppCompatActivity {

    private static final String EXTRA_EMAIL = "email";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_doente_main);
        ButterKnife.bind(this);
    }

    @OnClick(R.id.button1)
    public void button1() {
        startActivity(new Intent(this, TecladoActivity.class).putExtra("email", loadEmailFromExtras()));
        finish();
    }

    @OnClick(R.id.button2)
    public void button2() {
        startActivity(new Intent(this, EsferaActivity.class).putExtra("email", loadEmailFromExtras()));
        finish();
    }

    private String loadEmailFromExtras() {
        return getIntent().getExtras().getString(EXTRA_EMAIL);
    }

}

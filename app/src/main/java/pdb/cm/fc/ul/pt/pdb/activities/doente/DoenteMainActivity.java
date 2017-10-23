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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_doente_main);
        ButterKnife.bind(this);
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

}

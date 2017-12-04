package pdb.cm.fc.ul.pt.pdb.activities.medico;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import pdb.cm.fc.ul.pt.pdb.R;
import pdb.cm.fc.ul.pt.pdb.adapters.DoentesAdapter;
import pdb.cm.fc.ul.pt.pdb.interfaces.medico.MedicoMain;
import pdb.cm.fc.ul.pt.pdb.models.Doente;
import pdb.cm.fc.ul.pt.pdb.presenters.medico.MedicoMainActivityPresenter;

public class MedicoMainActivity extends AppCompatActivity implements MedicoMain.View,
        AdapterView.OnItemClickListener {

    private static final String EXTRA_EMAIL = "email";
    @BindView(R.id.doentes) ListView mDoentes;
    private MedicoMain.Presenter mPresenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_doentes);
        setup();
    }

    private void setup() {
        ButterKnife.bind(this);
        mDoentes.setOnItemClickListener(this);
        mPresenter = new MedicoMainActivityPresenter(this);
        defineWelcomeMessage();
    }

    private String loadEmailFromExtras() {
        return getIntent().getExtras().getString(EXTRA_EMAIL);
    }

    @Override
    public void onResume() {
        mPresenter.fetchDoentes(loadEmailFromExtras());
        super.onResume();
    }

    private void defineWelcomeMessage() {
        ((TextView) findViewById(R.id.welcome)).setText(getString(R.string.welcome_doctor, loadEmailFromExtras()));
    }

    @Override
    public void loadDoentes(ArrayList<Doente> doentes) {
        mDoentes.setAdapter(new DoentesAdapter(this, doentes));
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
        String emailDoente = ((TextView) view.findViewById(R.id.email)).getText().toString();
        startActivity(new Intent(this, DoenteDetalhesActivity.class).putExtra("emailDoente", emailDoente));
    }

}
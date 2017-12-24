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
import pdb.cm.fc.ul.pt.pdb.models.Medico;
import pdb.cm.fc.ul.pt.pdb.preferences.UserPreferences;
import pdb.cm.fc.ul.pt.pdb.presenters.medico.MedicoMainActivityPresenter;

import static pdb.cm.fc.ul.pt.pdb.activities.medico.MedicoDashboardMainActivity.EXTRA_DOENTE;
import static pdb.cm.fc.ul.pt.pdb.activities.medico.MedicoDashboardMainActivity.EXTRA_MEDICO;

public class MedicoMainActivity extends AppCompatActivity implements MedicoMain.View,
        AdapterView.OnItemClickListener {

    @BindView(R.id.doentes) ListView mDoentesListView;
    private MedicoMain.Presenter mPresenter;
    private String mEmail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_medico_doentes);
        setup();
    }

    private void setup() {
        ButterKnife.bind(this);
        mDoentesListView.setOnItemClickListener(this);
        mPresenter = new MedicoMainActivityPresenter(this);
        mEmail = UserPreferences.getEmail(this);
        defineWelcomeMessage();
    }

    @Override
    public void onResume() {
        mPresenter.fetchDoentes(mEmail);
        mPresenter.fetchMedico(mEmail);
        super.onResume();
    }

    private void defineWelcomeMessage() {
        ((TextView) findViewById(R.id.welcome)).setText(getString(R.string.welcome_doctor, mEmail));
    }

    @Override
    public void DoentesLoaded(ArrayList<Doente> doentes) {
        mDoentesListView.setAdapter(new DoentesAdapter(this, doentes));
    }

    @Override
    public void DoenteLoaded(Doente doente, Medico medico) {
        startActivity(new Intent(this, MedicoDashboardMainActivity.class).putExtra(EXTRA_DOENTE, doente).putExtra(EXTRA_MEDICO, medico));
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
        mPresenter.fetchDoente(position);
    }

}
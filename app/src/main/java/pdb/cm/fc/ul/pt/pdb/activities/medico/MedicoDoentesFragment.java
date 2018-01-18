package pdb.cm.fc.ul.pt.pdb.activities.medico;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;

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

/**
 * Created by nunonelas on 27/12/17.
 */

public class MedicoDoentesFragment extends Fragment implements MedicoMain.View,
        AdapterView.OnItemClickListener {

    private ListView mDoentesListView;
    private MedicoMain.Presenter mPresenter;
    private String mEmail;

    public static MedicoDoentesFragment newInstance() {
        MedicoDoentesFragment fragment = new MedicoDoentesFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_medico_doentes, container, false);
        mDoentesListView = (ListView) view.findViewById(R.id.doentes);
        setup(view);
        return view;
    }

    private void setup(View view) {
        ButterKnife.bind(getActivity());
        mDoentesListView.setOnItemClickListener(this);
        mPresenter = new MedicoMainActivityPresenter(this);
        mEmail = UserPreferences.getEmail(getContext());
        defineWelcomeMessage(view);
    }

    @Override
    public void onResume() {
        mPresenter.fetchDoentes(mEmail);
        mPresenter.fetchMedico(mEmail);
        super.onResume();
    }

    private void defineWelcomeMessage(View view) {
        ((TextView) view.findViewById(R.id.welcome)).setText(getString(R.string.welcome, mEmail));
    }

    @Override
    public void DoentesLoaded(ArrayList<Doente> doentes) {
        (getView().findViewById(R.id.login_progress)).setVisibility(View.GONE);
        (getView().findViewById(R.id.wait_message)).setVisibility(View.GONE);
        mDoentesListView.setAdapter(new DoentesAdapter(getActivity(), doentes));
    }

    @Override
    public void DoenteLoaded(Doente doente, Medico medico) {
        startActivity(new Intent(getActivity(), MedicoDashboardMainActivity.class).putExtra(EXTRA_DOENTE, doente).putExtra(EXTRA_MEDICO, medico));
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
        mPresenter.fetchDoente(position);
    }
}

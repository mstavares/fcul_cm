package pdb.cm.fc.ul.pt.pdb.activities.medico.dashboard;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import pdb.cm.fc.ul.pt.pdb.R;
import pdb.cm.fc.ul.pt.pdb.models.Doente;
import pdb.cm.fc.ul.pt.pdb.services.firebase.FirebaseDoente;

import static pdb.cm.fc.ul.pt.pdb.activities.medico.MedicoDashboardMainActivity.EXTRA_DOENTE;

/**
 * Created by nunonelas on 24/12/17.
 */

public class MedicoPatientSettingsFragment extends Fragment {

    private Doente doente;
    private EditText ballTime;
    private EditText wordsTime;

    private TextView currentBallTime;
    private TextView currentWordsTime;

    public static MedicoPatientSettingsFragment newInstance() {
        MedicoPatientSettingsFragment fragment = new MedicoPatientSettingsFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_medico_patientsettings, container, false);

        doente = (Doente) getActivity().getIntent().getExtras().getSerializable(EXTRA_DOENTE);

        ballTime = (EditText) view.findViewById(R.id.str_change_time_ball);
        wordsTime = (EditText) view.findViewById(R.id.str_change_time_words);

        currentBallTime = (TextView) view.findViewById(R.id.str_current_balltime);
        currentWordsTime = (TextView) view.findViewById(R.id.str_current_wordstime);

        currentBallTime.setText(getString(R.string.patient_settings_ballgame_current_time, doente.getTimeBall()));
        currentWordsTime.setText(getString(R.string.patient_settings_wordsgame_current_time, doente.getTimeWords()));

        Button btnSave = (Button) view.findViewById(R.id.btn_save);
        btnSave.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                FirebaseDoente.sendTimeGameData(doente.getId(), ballTime.getText().toString(), wordsTime.getText().toString());
                Toast.makeText(getContext(), "Content Saved!", Toast.LENGTH_SHORT).show();
                refreshContent();
            }
        });

        return view;
    }

    public void refreshContent(){
        currentBallTime.setText(getString(R.string.patient_settings_ballgame_current_time, doente.getTimeBall()));
        currentWordsTime.setText(getString(R.string.patient_settings_wordsgame_current_time, doente.getTimeWords()));
    }
}

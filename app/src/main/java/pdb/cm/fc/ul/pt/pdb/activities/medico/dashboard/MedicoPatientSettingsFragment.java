package pdb.cm.fc.ul.pt.pdb.activities.medico.dashboard;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import pdb.cm.fc.ul.pt.pdb.R;
import pdb.cm.fc.ul.pt.pdb.models.Doente;
import pdb.cm.fc.ul.pt.pdb.services.firebase.FirebaseDoente;

import static pdb.cm.fc.ul.pt.pdb.activities.medico.MedicoDashboardMainActivity.EXTRA_DOENTE;

/**
 * Created by nunonelas on 24/12/17.
 */

public class MedicoPatientSettingsFragment extends Fragment {

    private static final int MIN_WORDSGAME_TIME = 10;
    private static final int MIN_BALLGAME_TIME = 10;

    private Doente doente;

    @BindView(R.id.str_change_time_ball) EditText ballTime;
    @BindView(R.id.str_change_time_words) EditText wordsTime;

    @BindView(R.id.str_current_balltime) TextView currentBallTime;
    @BindView(R.id.str_current_wordstime) TextView currentWordsTime;

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

        ButterKnife.bind(this, view);

        setup();

        return view;
    }

    private void setup(){
        currentBallTime.setText(getString(R.string.patient_settings_ballgame_current_time, doente.getTimeBall()));
        currentWordsTime.setText(getString(R.string.patient_settings_wordsgame_current_time, doente.getTimeWords()));
    }

    @OnClick(R.id.btn_save)
    public void onClickSave() {
        if (!wordsTime.getText().toString().trim().equals("") &&
                !ballTime.getText().toString().trim().equals("")){
            if(Integer.parseInt(wordsTime.getText().toString()) >= MIN_WORDSGAME_TIME &&
                    Integer.parseInt(ballTime.getText().toString()) >= MIN_BALLGAME_TIME) {
                FirebaseDoente.sendTimeGameWordsData(doente.getId(), wordsTime.getText().toString());
                FirebaseDoente.sendTimeGameBallData(doente.getId(), ballTime.getText().toString());
                Toast.makeText(getContext(), "Both words game and ball game time successfuly changed to " + wordsTime.getText().toString() + " and " + ballTime.getText().toString() + "!", Toast.LENGTH_SHORT).show();
                refreshContent();
            } else if (Integer.parseInt(wordsTime.getText().toString()) < MIN_WORDSGAME_TIME &&
                    Integer.parseInt(ballTime.getText().toString()) < MIN_BALLGAME_TIME){
                Toast.makeText(getContext(), "Minimum time required for both keyboard game and ball game is " + String.valueOf(MIN_WORDSGAME_TIME) + " and " + String.valueOf(MIN_BALLGAME_TIME) + "!", Toast.LENGTH_SHORT).show();
            }
        } else if(!wordsTime.getText().toString().trim().equals("")) {
            if(Integer.parseInt(wordsTime.getText().toString()) >= MIN_WORDSGAME_TIME) {
                FirebaseDoente.sendTimeGameWordsData(doente.getId(), wordsTime.getText().toString());
                Toast.makeText(getContext(), "Words game time successfuly changed to " + wordsTime.getText().toString() + "!", Toast.LENGTH_SHORT).show();
                refreshContent();
            } else if (Integer.parseInt(wordsTime.getText().toString()) < MIN_WORDSGAME_TIME){
                Toast.makeText(getContext(), "Minimum time required for keyboard game is " + String.valueOf(MIN_WORDSGAME_TIME)+"!", Toast.LENGTH_SHORT).show();
            }

        } else if (!ballTime.getText().toString().trim().equals("")) {
            if(Integer.parseInt(ballTime.getText().toString()) >= MIN_BALLGAME_TIME) {
                FirebaseDoente.sendTimeGameBallData(doente.getId(), ballTime.getText().toString());
                Toast.makeText(getContext(), "Ball game time successfuly changed to " + ballTime.getText().toString() + "!", Toast.LENGTH_SHORT).show();
                refreshContent();
            } else if (Integer.parseInt(ballTime.getText().toString()) < MIN_BALLGAME_TIME){
                Toast.makeText(getContext(), "Minimum time required for ball game is " + String.valueOf(MIN_BALLGAME_TIME)+"!", Toast.LENGTH_SHORT).show();

            }
        }
    }

    private void refreshContent(){
        if (!wordsTime.getText().toString().trim().equals("") &&
                !ballTime.getText().toString().trim().equals("")){
            currentWordsTime.setText(getString(R.string.patient_settings_wordsgame_current_time, wordsTime.getText().toString()));
            currentBallTime.setText(getString(R.string.patient_settings_ballgame_current_time, ballTime.getText().toString()));

        } else if(!wordsTime.getText().toString().trim().equals("")){
            currentWordsTime.setText(getString(R.string.patient_settings_wordsgame_current_time, wordsTime.getText().toString()));

        } else if (!ballTime.getText().toString().trim().equals("")){
            currentBallTime.setText(getString(R.string.patient_settings_ballgame_current_time, ballTime.getText().toString()));

        } else {
            currentBallTime.setText(getString(R.string.patient_settings_ballgame_current_time, doente.getTimeBall()));
            currentWordsTime.setText(getString(R.string.patient_settings_wordsgame_current_time, doente.getTimeWords()));
        }
    }
}

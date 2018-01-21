package pdb.cm.fc.ul.pt.pdb.activities.medico.dashboard;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.NestedScrollView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.HashMap;

import butterknife.BindView;
import butterknife.ButterKnife;
import pdb.cm.fc.ul.pt.pdb.R;
import pdb.cm.fc.ul.pt.pdb.adapters.RawDataBallAdapter;
import pdb.cm.fc.ul.pt.pdb.adapters.RawDataShakeAdapter;
import pdb.cm.fc.ul.pt.pdb.adapters.RawDataWordsAdapter;
import pdb.cm.fc.ul.pt.pdb.models.BallScore;
import pdb.cm.fc.ul.pt.pdb.models.Doente;
import pdb.cm.fc.ul.pt.pdb.models.Shake;
import pdb.cm.fc.ul.pt.pdb.models.WordScore;
import pdb.cm.fc.ul.pt.pdb.services.firebase.Firebase;
import pdb.cm.fc.ul.pt.pdb.services.firebase.FirebaseMedico;

import static pdb.cm.fc.ul.pt.pdb.activities.medico.MedicoDashboardMainActivity.EXTRA_DOENTE;
import static pdb.cm.fc.ul.pt.pdb.models.Constants.rawDataDate;
import static pdb.cm.fc.ul.pt.pdb.models.Constants.rawDataFaults;
import static pdb.cm.fc.ul.pt.pdb.models.Constants.rawDataScore;
import static pdb.cm.fc.ul.pt.pdb.models.Constants.rawDataShake;
import static pdb.cm.fc.ul.pt.pdb.models.Constants.rawDataTime;

/**
 * Created by nunonelas on 24/12/17.
 */

public class MedicoRawDataFragment extends Fragment implements Firebase.LoadRawData {

    @BindView(R.id.rawdata_word) ListView wordList;
    @BindView(R.id.rawdata_ball) ListView ballList;
    @BindView(R.id.rawdata_shake) ListView shakeList;

    @BindView(R.id.scrollview_wordsgame) NestedScrollView scrollViewWords;
    @BindView(R.id.scrollview_ballgame) NestedScrollView scrollViewBall;
    @BindView(R.id.scrollview_shake) NestedScrollView scrollViewShake;

    @BindView(R.id.empty_wordsgame) TextView emptyWords;
    @BindView(R.id.empty_ballgame) TextView emptyBall;
    @BindView(R.id.empty_shake) TextView emptyShake;

    private ArrayList<HashMap<String, String>> listWords;
    private ArrayList<HashMap<String, String>> listBall;
    private ArrayList<HashMap<String, String>> listShake;

    private Doente doente;

    public static MedicoRawDataFragment newInstance() {
        MedicoRawDataFragment fragment = new MedicoRawDataFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_medico_rawdata, container, false);

        doente = (Doente) getActivity().getIntent().getExtras().getSerializable(EXTRA_DOENTE);
        ((TextView) view.findViewById(R.id.name)).setText(doente.getName());
        ((TextView) view.findViewById(R.id.age)).setText(doente.getAge());

        ButterKnife.bind(this, view);
        setup();

        return view;
    }

    private void setup(){
        listWords = new ArrayList<HashMap<String,String>>();
        listBall = new ArrayList<HashMap<String,String>>();
        listShake = new ArrayList<HashMap<String,String>>();

        FirebaseMedico.getRawDataWords(this, doente);
        FirebaseMedico.getRawDataBall(this, doente);
        FirebaseMedico.getRawDataShake(this, doente);
    }

    @Override
    public void loadRawDataWords(ArrayList<HashMap<String, String>> listWords){
        this.listWords = listWords;
        checkRawDataWords();
    }

    @Override
    public void loadRawDataBall(ArrayList<HashMap<String, String>> listBall){
        this.listBall = listBall;
        checkRawDataBall();
    }

    @Override
    public void loadRawDataShake(ArrayList<HashMap<String, String>> listShake){
        this.listShake = listShake;
        checkRawDataShake();
    }

    private void checkRawDataWords(){
        if(listWords.size() == 0){
            scrollViewWords.setVisibility(View.GONE);
            emptyWords.setVisibility(View.VISIBLE);
        } else {
            scrollViewWords.setVisibility(View.VISIBLE);
            emptyWords.setVisibility(View.GONE);

            wordList.setAdapter(new RawDataWordsAdapter(getActivity(), listWords));
        }
    }

    private void checkRawDataBall(){
        if(listBall.size() == 0){
            scrollViewBall.setVisibility(View.GONE);
            emptyBall.setVisibility(View.VISIBLE);
        } else {
            scrollViewBall.setVisibility(View.VISIBLE);
            emptyBall.setVisibility(View.GONE);

            ballList.setAdapter(new RawDataBallAdapter(getActivity(), listBall));
        }
    }

    private void checkRawDataShake(){
        if(listShake.size() == 0){
            scrollViewShake.setVisibility(View.GONE);
            emptyShake.setVisibility(View.VISIBLE);
        } else {
            scrollViewShake.setVisibility(View.VISIBLE);
            emptyShake.setVisibility(View.GONE);

            shakeList.setAdapter(new RawDataShakeAdapter(getActivity(), listShake));
        }
    }
}

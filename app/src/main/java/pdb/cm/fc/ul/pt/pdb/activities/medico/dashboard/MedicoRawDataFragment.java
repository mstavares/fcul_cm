package pdb.cm.fc.ul.pt.pdb.activities.medico.dashboard;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;

import pdb.cm.fc.ul.pt.pdb.R;
import pdb.cm.fc.ul.pt.pdb.adapters.RawDataBallAdapter;
import pdb.cm.fc.ul.pt.pdb.adapters.RawDataShakeAdapter;
import pdb.cm.fc.ul.pt.pdb.adapters.RawDataWordsAdapter;
import pdb.cm.fc.ul.pt.pdb.models.BallScore;
import pdb.cm.fc.ul.pt.pdb.models.Doente;
import pdb.cm.fc.ul.pt.pdb.models.Shake;
import pdb.cm.fc.ul.pt.pdb.models.WordScore;

import static pdb.cm.fc.ul.pt.pdb.activities.medico.MedicoDashboardMainActivity.EXTRA_DOENTE;
import static pdb.cm.fc.ul.pt.pdb.models.Constants.rawDataDate;
import static pdb.cm.fc.ul.pt.pdb.models.Constants.rawDataFaults;
import static pdb.cm.fc.ul.pt.pdb.models.Constants.rawDataScore;
import static pdb.cm.fc.ul.pt.pdb.models.Constants.rawDataShake;
import static pdb.cm.fc.ul.pt.pdb.models.Constants.rawDataTime;

/**
 * Created by nunonelas on 24/12/17.
 */

public class MedicoRawDataFragment extends Fragment {

    private ListView wordList;
    private ListView ballList;
    private ListView shakeList;

    private static final String TBL_WORDSCORES = "wordscores";
    private static final String TBL_BALLSCORES = "ballscores";
    private static final String TBL_SHAKESCORES = "shake";

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

        wordList = (ListView) view.findViewById(R.id.rawdata_word);
        ballList = (ListView) view.findViewById(R.id.rawdata_ball);
        shakeList = (ListView) view.findViewById(R.id.rawdata_shake);

        listWords = new ArrayList<HashMap<String,String>>();
        listBall = new ArrayList<HashMap<String,String>>();
        listShake = new ArrayList<HashMap<String,String>>();

        getRawDataWords(doente.getId());
        getRawDataBall(doente.getId());
        getRawDataShake(doente.getId());

        return view;
    }

    public void getRawDataWords(String id){
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference(TBL_WORDSCORES+"/"+id);

        Query query = reference.orderByChild("date");
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    for (DataSnapshot scoresSnapshot : dataSnapshot.getChildren()) {
                        WordScore scores = scoresSnapshot.getValue(WordScore.class);

                        HashMap<String,String> temp = new HashMap<String, String>();
                        temp.put(rawDataScore, Integer.toString(scores.getScore()));
                        temp.put(rawDataFaults, Integer.toString(scores.getFaults()));
                        temp.put(rawDataTime, Integer.toString(scores.getTime()));
                        temp.put(rawDataDate, scores.getDate());
                        listWords.add(temp);
                    }

                    RawDataWordsAdapter adapter = new RawDataWordsAdapter(getActivity(), listWords);
                    wordList.setAdapter(adapter);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    public void getRawDataBall(String id){
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference(TBL_BALLSCORES+"/"+id);

        Query query = reference.orderByChild("date");
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    for (DataSnapshot scoresSnapshot : dataSnapshot.getChildren()) {
                        BallScore scores = scoresSnapshot.getValue(BallScore.class);

                        HashMap<String,String> temp = new HashMap<String, String>();
                        temp.put(rawDataScore, Integer.toString(scores.getScore()));
                        temp.put(rawDataTime, Integer.toString(scores.getTime()));
                        temp.put(rawDataDate, scores.getDate());
                        listBall.add(temp);
                    }

                    RawDataBallAdapter adapter = new RawDataBallAdapter(getActivity(), listBall);
                    ballList.setAdapter(adapter);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    public void getRawDataShake(String id){
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference(TBL_SHAKESCORES+"/"+id);

        Query query = reference.orderByChild("date");
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    for (DataSnapshot shakesSnapshot : dataSnapshot.getChildren()) {
                        Shake shakes = shakesSnapshot.getValue(Shake.class);

                        HashMap<String,String> temp = new HashMap<String, String>();
                        temp.put(rawDataShake, Double.toString(shakes.getShake()));
                        temp.put(rawDataDate, shakes.getDate());
                        listShake.add(temp);
                    }

                    RawDataShakeAdapter adapter = new RawDataShakeAdapter(getActivity(), listShake);
                    shakeList.setAdapter(adapter);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }
}

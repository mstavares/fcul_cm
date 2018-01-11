package pdb.cm.fc.ul.pt.pdb.activities.doente;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
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
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

import pdb.cm.fc.ul.pt.pdb.R;
import pdb.cm.fc.ul.pt.pdb.adapters.PontuacoesListAdapter;
import pdb.cm.fc.ul.pt.pdb.models.BallScore;
import pdb.cm.fc.ul.pt.pdb.models.Doente;

import static pdb.cm.fc.ul.pt.pdb.models.Constants.name;
import static pdb.cm.fc.ul.pt.pdb.models.Constants.pos;
import static pdb.cm.fc.ul.pt.pdb.models.Constants.score;

/**
 * Created by nunonelas on 19/12/17.
 */

public class PontuacoesBallGameFragment extends Fragment {

    private static final String TAG = PontuacoesBallGameFragment.class.getSimpleName();

    private static final int MAX_LENGHT_SCORES = 10;

    private static final String TBL_BALLSCORES = "ballscores";
    private static final String TBL_DOENTES = "doentes";
    private ArrayList<HashMap<String, String>> list;
    private ListView listView;

    private ArrayList<String> listNames;
    private ArrayList<String> listScores;

    private int mNumProccessedPatients = 0;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        ViewGroup rootView = (ViewGroup) inflater.inflate(
                R.layout.fragment_doente_pontuacoes_ballgame, container, false);

        listView=(ListView) rootView.findViewById(R.id.listViewScoresBall);

        getAllUsers();

        return rootView;
    }

    private void configureListView(){
        PontuacoesListAdapter adapter = new PontuacoesListAdapter(getActivity(), list);
        listView.setAdapter(adapter);
    }

    private void getAllUsers(){
        listNames = new ArrayList<>();
        listScores = new ArrayList<>();

        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference(TBL_DOENTES);
        databaseReference.addValueEventListener(new ValueEventListener() {

            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                ArrayList<String> doenteId = new ArrayList<>();
                ArrayList<String> doenteName = new ArrayList<>();

                for (DataSnapshot registosSnapshot : dataSnapshot.getChildren()) {
                    Doente doente = registosSnapshot.getValue(Doente.class);
                    doenteId.add(doente.getId());
                    doenteName.add(doente.getName());
                }

                for(int i = 0; i < doenteId.size(); i++){
                    getScore(doenteId.get(i), doenteName.get(i), doenteId.size());
                }
            }

            @Override
            public void onCancelled(DatabaseError error) {
                Log.w(TAG, "Failed to read value. ", error.toException());
            }
        });
    }

    private void getScore(String id, final String mName, final int mNumOfPatients){
        mNumProccessedPatients++;
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference(TBL_BALLSCORES+"/"+id);

        Query query = reference.orderByChild("score").limitToLast(10);
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    for (DataSnapshot scoresSnapshot : dataSnapshot.getChildren()) {
                        BallScore scores = scoresSnapshot.getValue(BallScore.class);
                        listNames.add(mName);
                        listScores.add(Integer.toString(scores.getScore()));
                    }

                    if(mNumOfPatients == mNumProccessedPatients) {
                        orderList();
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    private void orderList(){
        Collections.reverse(listNames);
        Collections.reverse(listScores);

        list = new ArrayList<HashMap<String,String>>();

        HashMap<String,String> temp = new HashMap<String, String>();
        temp.put(pos, "Position");
        temp.put(name, "Name");
        temp.put(score, "Score");
        list.add(temp);

        for (int i = 0; i < listNames.size(); i++){
            HashMap<String,String> data = new HashMap<String, String>();
            data.put(pos, Integer.toString(i+1));
            data.put(name, listNames.get(i));
            data.put(score, listScores.get(i));
            list.add(data);
        }

        for (int i = listNames.size(); i < MAX_LENGHT_SCORES; i++) {
            HashMap<String,String> data = new HashMap<String, String>();
            data.put(pos, Integer.toString(i+1));
            data.put(name, "Empty");
            data.put(score, "0");
            list.add(data);
        }

        configureListView();
    }
}

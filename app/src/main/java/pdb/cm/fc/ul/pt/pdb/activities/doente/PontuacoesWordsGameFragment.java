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
import java.util.HashMap;

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

public class PontuacoesWordsGameFragment extends Fragment {

    private static final String TBL_WORDSSCORES = "wordscores";

    private static final String TAG = PontuacoesBallGameFragment.class.getSimpleName();

    private static final String TBL_DOENTES = "doentes";
    private ArrayList<HashMap<String, String>> list;
    private ListView listView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        ViewGroup rootView = (ViewGroup) inflater.inflate(
                R.layout.fragment_doente_pontuacoes_wordsgame, container, false);

        listView=(ListView) rootView.findViewById(R.id.listViewScoresWords);

        list = new ArrayList<HashMap<String,String>>();

        HashMap<String,String> temp = new HashMap<String, String>();
        temp.put(pos, "Position");
        temp.put(name, "Name");
        temp.put(score, "Score");
        list.add(temp);

        getAllUsers();

        return rootView;
    }

    public void getAllUsers(){
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference(TBL_DOENTES);
        databaseReference.addValueEventListener(new ValueEventListener() {

            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot registosSnapshot : dataSnapshot.getChildren()) {
                    Doente doente = registosSnapshot.getValue(Doente.class);
                    getScore(doente.getId(), doente.getName());
                }
            }

            @Override
            public void onCancelled(DatabaseError error) {
                Log.w(TAG, "Failed to read value. ", error.toException());
            }
        });
    }

    public void getScore(String id, final String mName){
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference(TBL_WORDSSCORES+"/"+id);

        Query query = reference.orderByChild("score").limitToLast(10);
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    int i = 1;
                    for (DataSnapshot scoresSnapshot : dataSnapshot.getChildren()) {
                        BallScore scores = scoresSnapshot.getValue(BallScore.class);

                        HashMap<String,String> temp = new HashMap<String, String>();
                        temp.put(pos, Integer.toString(i));
                        temp.put(name, mName);
                        temp.put(score, Integer.toString(scores.getScore()));
                        list.add(temp);
                        i++;
                    }

                    PontuacoesListAdapter adapter = new PontuacoesListAdapter(getActivity(), list);
                    listView.setAdapter(adapter);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }
}

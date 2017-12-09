package pdb.cm.fc.ul.pt.pdb.services.firebase;


import android.util.Log;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import pdb.cm.fc.ul.pt.pdb.models.BallScore;
import pdb.cm.fc.ul.pt.pdb.models.Doente;
import pdb.cm.fc.ul.pt.pdb.models.WordScore;

import static pdb.cm.fc.ul.pt.pdb.utilities.Utilities.getTimestamp;

public abstract class FirebaseDoente {

    private static final String TAG = FirebaseDoente.class.getSimpleName();
    private static final String ATR_LAST_LOGIN = "lastLogin";
    private static final String TBL_DOENTES = "doentes";
    private static final String TBL_WORDSSCORES = "wordscores";
    private static final String TBL_BALLSCORES = "ballscores";

    public static void setLastLogin(final String email) {
        final DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference(TBL_DOENTES);
        databaseReference.addValueEventListener(new ValueEventListener() {

            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot registosSnapshot : dataSnapshot.getChildren()) {
                    Doente doente = registosSnapshot.getValue(Doente.class);
                    if (doente.getEmail().equals(email)) {
                        databaseReference.child(doente.getId()).child(ATR_LAST_LOGIN).setValue(getTimestamp());
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError error) {
                Log.w(TAG, "Failed to read value. ", error.toException());
            }
        });
    }

    public static void setWordsScore(String doente, final WordScore wordScore) {
        doente = checkUserID(doente);
        final DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference(TBL_WORDSSCORES + "/" + doente);
        databaseReference.push().setValue(wordScore);
    }

    public static void setBallScore(String doente, final BallScore ballScore) {
        doente = checkUserID(doente);
        final DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference(TBL_BALLSCORES + "/" + doente);
        databaseReference.push().setValue(ballScore);
    }

    public static String checkUserID(String doente){
        if(doente.contains("@")){
            String[] output = doente.split("@");
            doente = output[0];
        }
        return doente;
    }

}

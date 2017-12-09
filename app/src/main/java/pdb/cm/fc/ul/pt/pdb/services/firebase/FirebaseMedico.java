package pdb.cm.fc.ul.pt.pdb.services.firebase;


import android.util.Log;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import pdb.cm.fc.ul.pt.pdb.models.Doente;
import pdb.cm.fc.ul.pt.pdb.models.Note;

import static pdb.cm.fc.ul.pt.pdb.utilities.Utilities.getTimestamp;

public abstract class FirebaseMedico {

    private static final String TAG = FirebaseMedico.class.getSimpleName();
    private static final String TBL_DOENTES = "doentes";
    private static final String TBL_NOTES = "notes";

    public static void fetchAllDoentes(final Firebase.LoadDoentes callback, final String medico) {
        final DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference(TBL_DOENTES);
        databaseReference.addValueEventListener(new ValueEventListener() {

            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                ArrayList<Doente> doentes = new ArrayList<>();
                for (DataSnapshot doentesSnapshot: dataSnapshot.getChildren()) {
                    Doente doente = doentesSnapshot.getValue(Doente.class);
                    if (doente.getMedicoAssign().equals(medico)) {
                        doentes.add(doente);
                    }
                }
                callback.loadDoentes(doentes);
            }

            @Override
            public void onCancelled(DatabaseError error) {
                Log.e(TAG, "Failed to read value. ", error.toException());
            }
        });
    }

    public static void fetchDoente(final Firebase.LoadDoente callback, final String emailDoente) {
        final DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference(TBL_DOENTES);
        databaseReference.addValueEventListener(new ValueEventListener() {

            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot doentesSnapshot : dataSnapshot.getChildren()) {
                    Doente doente = doentesSnapshot.getValue(Doente.class);
                    if (doente.getEmail().equals(emailDoente)) {
                        callback.loadDoente(doente);
                        break;
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError error) {
                Log.e(TAG, "Failed to read value. ", error.toException());
            }
        });
    }

    public static void fetchAllNotes(final Firebase.LoadNotes callback, final String nomeDoente) {
        final DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference(TBL_DOENTES + "/" + nomeDoente);
        databaseReference.addValueEventListener(new ValueEventListener() {

            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                ArrayList<Note> notes = new ArrayList<>();
                for (DataSnapshot notesSnapshot: dataSnapshot.getChildren()) {
                    notes.add(notesSnapshot.getValue(Note.class));
                }
                callback.loadNotes(notes);
            }

            @Override
            public void onCancelled(DatabaseError error) {
                Log.e(TAG, "Failed to read value. ", error.toException());
            }
        });
    }

}
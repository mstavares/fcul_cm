package pdb.cm.fc.ul.pt.pdb.services.firebase;

import android.util.Log;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import pdb.cm.fc.ul.pt.pdb.models.Doente;
import pdb.cm.fc.ul.pt.pdb.models.Medico;
import pdb.cm.fc.ul.pt.pdb.models.Note;

public abstract class FirebaseMedico {

    private static final String TAG = FirebaseMedico.class.getSimpleName();
    private static final String TBL_DOENTES = "doentes";
    private static final String TBL_MEDICOS = "medicos";
    private static final String TBL_NOTES = "notes";
    private static final String TBL_WORDS = "words";

    private static final String ATR_NOTE = "note";

    public static void fetchMedico(final Firebase.LoadMedico callback, final String emailMedico){
        final DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference(TBL_MEDICOS);
        databaseReference.addValueEventListener(new ValueEventListener() {

            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot medicosSnapshot : dataSnapshot.getChildren()) {
                    Medico medico = medicosSnapshot.getValue(Medico.class);
                    if (medico.getEmail().equals(emailMedico)) {
                        callback.loadMedico(medico);
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

    public static void fetchAllNotes(final Firebase.LoadNotes callback, final String doenteID) {
        final DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference(TBL_NOTES + "/" + doenteID);
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

    public static void sendNewWord(final String word) {
        final DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference(TBL_WORDS);
        databaseReference.push().setValue(word);
    }

    public static void removeWord(final String word) {
        final DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference(TBL_WORDS);
        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {

            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot wordsSnapshot: dataSnapshot.getChildren()) {
                    if(wordsSnapshot.getValue().equals(word)) {
                        databaseReference.child(wordsSnapshot.getKey()).removeValue();
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError error) {
                Log.e(TAG, "Failed to read value. ", error.toException());
            }
        });
    }

    public static void addUserToTable(Doente doente){
        final DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference(TBL_DOENTES);
        databaseReference.child(doente.getId()).setValue(doente);
    }

    public static void insertNote(Note note, Doente doente) {
        final DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference(TBL_NOTES+"/"+doente.getId());
        databaseReference.push().setValue(note);
    }

    public static void removeNote(Note note, Doente doente){
        final DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference(TBL_NOTES+"/"+doente.getId());
        Query deleteQuery = databaseReference.orderByChild("date").equalTo(note.getDate());
        deleteQuery.addListenerForSingleValueEvent(new ValueEventListener() {

            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot notesSnapshot: dataSnapshot.getChildren()) {
                    databaseReference.child(notesSnapshot.getKey()).removeValue();
                }
            }

            @Override
            public void onCancelled(DatabaseError error) {
                Log.e(TAG, "Failed to read value. ", error.toException());
            }
        });
    }

    public static void updateNote(final Note newNote, Note oldNote, Doente doente){
        final DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference(TBL_NOTES+"/"+doente.getId());
        Query deleteQuery = databaseReference.orderByChild("date").equalTo(oldNote.getDate());
        deleteQuery.addListenerForSingleValueEvent(new ValueEventListener() {

            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot notesSnapshot: dataSnapshot.getChildren()) {
                    databaseReference.child(notesSnapshot.getKey()).child(ATR_NOTE).setValue(newNote.getNote());
                }
            }

            @Override
            public void onCancelled(DatabaseError error) {
                Log.e(TAG, "Failed to read value. ", error.toException());
            }
        });
    }
}
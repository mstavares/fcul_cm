package pdb.cm.fc.ul.pt.pdb.services.firebase;

import android.util.Log;

import com.github.mikephil.charting.data.Entry;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;

import pdb.cm.fc.ul.pt.pdb.models.BallScore;
import pdb.cm.fc.ul.pt.pdb.models.Doente;
import pdb.cm.fc.ul.pt.pdb.models.Medico;
import pdb.cm.fc.ul.pt.pdb.models.Note;
import pdb.cm.fc.ul.pt.pdb.models.Shake;
import pdb.cm.fc.ul.pt.pdb.models.WordScore;
import pdb.cm.fc.ul.pt.pdb.utilities.Utilities;

import static pdb.cm.fc.ul.pt.pdb.models.Constants.rawDataDate;
import static pdb.cm.fc.ul.pt.pdb.models.Constants.rawDataFaults;
import static pdb.cm.fc.ul.pt.pdb.models.Constants.rawDataScore;
import static pdb.cm.fc.ul.pt.pdb.models.Constants.rawDataShake;
import static pdb.cm.fc.ul.pt.pdb.models.Constants.rawDataTime;

public abstract class FirebaseMedico {

    private static final String TAG = FirebaseMedico.class.getSimpleName();

    private static final String TBL_DOENTES = "doentes";
    private static final String TBL_MEDICOS = "medicos";
    private static final String TBL_NOTES = "notes";
    private static final String TBL_WORDS = "words";
    private static final String TBL_SHAKES = "shake";
    private static final String TBL_WORDSSCORES = "wordscores";
    private static final String TBL_BALLSCORES = "ballscores";

    private static final String ATR_NOTE = "note";
    private static final String ATR_DATE = "date";

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

    public static void getWordsFromFirebase(final Firebase.LoadWords callback) {
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference(TBL_WORDS);
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                ArrayList<String> wordsList = new ArrayList<String>();
                for (DataSnapshot wordsSnapshot : dataSnapshot.getChildren()) {
                    wordsList.add(wordsSnapshot.getValue().toString());
                }

                callback.loadWords(wordsList);
            }

            @Override
            public void onCancelled(DatabaseError error) {
                Log.w(TAG, "Failed to read value. ", error.toException());
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

    public static void fetchAllWordsGameData(final Firebase.LoadDashboardData callback, Doente doente) {
        final DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference(TBL_WORDSSCORES + "/" + doente.getId());
        Query query = databaseReference.orderByChild(ATR_DATE).startAt(Utilities.getFirstDayOfCurrentWeek());
        query.addValueEventListener(new ValueEventListener() {

            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                ArrayList<Entry> scoreWords = new ArrayList<>();
                ArrayList<Entry> timeWords = new ArrayList<>();
                ArrayList<Entry> faultsWords = new ArrayList<>();
                for (DataSnapshot scoresSnapshot: dataSnapshot.getChildren()) {
                    WordScore wordScore = scoresSnapshot.getValue(WordScore.class);
                    try {
                        Calendar c = Calendar.getInstance();
                        c.setTime(new SimpleDateFormat("dd/M/yyyy HH:mm:ss").parse(wordScore.getDate()));
                        int dayOfWeek = c.get(Calendar.DAY_OF_WEEK);
                        int hours = c.get(Calendar.HOUR_OF_DAY);
                        int minutes = c.get(Calendar.MINUTE);

                        float time = Utilities.convertDate(dayOfWeek, hours, minutes);

                        scoreWords.add(new Entry(time, (float) wordScore.getScore()));
                        timeWords.add(new Entry(time, (float) wordScore.getTime()));
                        faultsWords.add(new Entry(time, (float) wordScore.getFaults()));

                    } catch (java.text.ParseException e){

                    }
                    callback.loadWordsGameData(scoreWords, timeWords, faultsWords);
                }
            }

            @Override
            public void onCancelled(DatabaseError error) {
                Log.e(TAG, "Failed to read value. ", error.toException());
            }
        });
    }

    public static void fetchAllBallGameData(final Firebase.LoadDashboardData callback, Doente doente) {
        final DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference(TBL_BALLSCORES + "/" + doente.getId());
        Query query = databaseReference.orderByChild(ATR_DATE).startAt(Utilities.getFirstDayOfCurrentWeek());
        query.addValueEventListener(new ValueEventListener() {

            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                ArrayList<Entry> scoreBall = new ArrayList<>();
                ArrayList<Entry> timeBall = new ArrayList<>();
                for (DataSnapshot scoresSnapshot: dataSnapshot.getChildren()) {
                    BallScore ballScore = scoresSnapshot.getValue(BallScore.class);
                    try {
                        Calendar c = Calendar.getInstance();
                        c.setTime(new SimpleDateFormat("dd/M/yyyy HH:mm:ss").parse(ballScore.getDate()));
                        int dayOfWeek = c.get(Calendar.DAY_OF_WEEK);
                        int hours = c.get(Calendar.HOUR_OF_DAY);
                        int minutes = c.get(Calendar.MINUTE);

                        float time = Utilities.convertDate(dayOfWeek, hours, minutes);

                        scoreBall.add(new Entry(time, (float) ballScore.getScore()));
                        timeBall.add(new Entry(time, (float) ballScore.getTime()));

                    } catch (java.text.ParseException e){

                    }
                    callback.loadBallGameData(scoreBall, timeBall);
                }
            }

            @Override
            public void onCancelled(DatabaseError error) {
                Log.e(TAG, "Failed to read value. ", error.toException());
            }
        });
    }

    public static void fetchAllShakeData(final Firebase.LoadDashboardData callback, Doente doente) {
        final DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference(TBL_SHAKES + "/" + doente.getId());
        Query query = databaseReference.orderByChild(ATR_DATE).startAt(Utilities.getLastDay());
        query.addValueEventListener(new ValueEventListener() {

            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                ArrayList<Entry> shakes = new ArrayList<>();
                for (DataSnapshot shakesSnapshot: dataSnapshot.getChildren()) {
                    Shake shake = shakesSnapshot.getValue(Shake.class);
                    try {
                        Calendar c = Calendar.getInstance();
                        c.setTime(new SimpleDateFormat("dd/M/yyyy HH:mm:ss").parse(shake.getDate()));
                        int hours = c.get(Calendar.HOUR_OF_DAY);
                        int minutes = c.get(Calendar.MINUTE);

                        String hold = String.format("%02d.%02d", hours, minutes);

                        float time = Float.valueOf(hold);

                        shakes.add(new Entry(time, (float) shake.getShake()));
                    } catch (java.text.ParseException e){

                    }
                    callback.loadShakeData(shakes);
                }
            }

            @Override
            public void onCancelled(DatabaseError error) {
                Log.e(TAG, "Failed to read value. ", error.toException());
            }
        });
    }

    public static void getRawDataWords(final Firebase.LoadRawData callback, Doente doente){
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference(TBL_WORDSSCORES+"/"+doente.getId());

        Query query = reference.orderByChild("date");
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                ArrayList<HashMap<String, String>> listWords = new ArrayList<HashMap<String,String>>();
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

                    callback.loadRawDataWords(listWords);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    public static void getRawDataBall(final Firebase.LoadRawData callback, Doente doente){
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference(TBL_BALLSCORES+"/"+doente.getId());

        Query query = reference.orderByChild("date");
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                ArrayList<HashMap<String, String>> listBall = new ArrayList<HashMap<String,String>>();
                if (dataSnapshot.exists()) {
                    for (DataSnapshot scoresSnapshot : dataSnapshot.getChildren()) {
                        BallScore scores = scoresSnapshot.getValue(BallScore.class);

                        HashMap<String,String> temp = new HashMap<String, String>();
                        temp.put(rawDataScore, Integer.toString(scores.getScore()));
                        temp.put(rawDataTime, Integer.toString(scores.getTime()));
                        temp.put(rawDataDate, scores.getDate());
                        listBall.add(temp);
                    }

                    callback.loadRawDataBall(listBall);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    public static void getRawDataShake(final Firebase.LoadRawData callback, Doente doente){
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference(TBL_SHAKES+"/"+doente.getId());

        Query query = reference.orderByChild("date");
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                ArrayList<HashMap<String, String>> listShake = new ArrayList<HashMap<String,String>>();
                if (dataSnapshot.exists()) {
                    for (DataSnapshot shakesSnapshot : dataSnapshot.getChildren()) {
                        Shake shakes = shakesSnapshot.getValue(Shake.class);

                        HashMap<String,String> temp = new HashMap<String, String>();
                        temp.put(rawDataShake, Double.toString(shakes.getShake()));
                        temp.put(rawDataDate, shakes.getDate());
                        listShake.add(temp);
                    }

                    callback.loadRawDataShake(listShake);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    public static void fetchLastPatientID(final Firebase.LoadLastPatient callback){
        final DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference(TBL_DOENTES);
        Query deleteQuery = databaseReference.orderByChild("id").limitToLast(1);
        deleteQuery.addListenerForSingleValueEvent(new ValueEventListener() {

            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot doentesSnapshot: dataSnapshot.getChildren()) {
                    Doente lastDoenteOnFirebase = doentesSnapshot.getValue(Doente.class);
                    callback.loadLastPatient(lastDoenteOnFirebase);
                }
            }

            @Override
            public void onCancelled(DatabaseError error) {
                Log.e(TAG, "Failed to read value. ", error.toException());
            }
        });
    }
}
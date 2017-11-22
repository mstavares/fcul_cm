package pdb.cm.fc.ul.pt.pdb.activities.medico;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;

import pdb.cm.fc.ul.pt.pdb.R;
import pdb.cm.fc.ul.pt.pdb.models.Doente;
import pdb.cm.fc.ul.pt.pdb.models.Note;
import pdb.cm.fc.ul.pt.pdb.views.ListViewAdapter;
import pdb.cm.fc.ul.pt.pdb.views.NotesViewAdapter;

import static android.content.ContentValues.TAG;
import static pdb.cm.fc.ul.pt.pdb.models.Constants.noteDate;
import static pdb.cm.fc.ul.pt.pdb.models.Constants.noteName;

/**
 * Created by nunonelas on 22/11/17.
 */

public class NotesMedicoFragment extends Fragment {

    private ArrayList<HashMap<String, String>> list;

    private String mEmailDoente;
    private ListView notesView;
    private TextView nameTV;
    private TextView ageTV;

    private DatabaseReference mDatabaseNotes;
    private DatabaseReference mDatabaseDoentes;

    private Note note;
    private Doente doente;
    private String userID;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_notes, container, false);

        notesView = (ListView) rootView.findViewById(R.id.listViewNotes);

        list=new ArrayList<HashMap<String,String>>();

        nameTV = (TextView) rootView.findViewById(R.id.name);
        ageTV = (TextView) rootView.findViewById(R.id.age);

        if (getArguments() != null) {
            mEmailDoente = getArguments().getString("emailDoente");
            checkUserID();
            mDatabaseDoentes = FirebaseDatabase.getInstance().getReference("doentes");
            mDatabaseNotes = FirebaseDatabase.getInstance().getReference("notes/"+userID);
            getUserData();
            getNotesFirebase();
        }

        return rootView;
    }

    public void getNotesFirebase(){
        mDatabaseNotes.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                list=new ArrayList<HashMap<String,String>>();

                for (DataSnapshot notesSnapshot: dataSnapshot.getChildren()) {
                    note = notesSnapshot.getValue(Note.class);
                    insertIntoHashMap(note);
                }
                NotesViewAdapter adapter=new NotesViewAdapter(getActivity(), list);
                notesView.setAdapter(adapter);
            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
                Log.w(TAG, "Failed to read value.", error.toException());
            }
        });
    }

    public void getUserData(){
        mDatabaseDoentes.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot doentesSnapshot: dataSnapshot.getChildren()) {
                    doente = doentesSnapshot.getValue(Doente.class);
                    if (doente.getEmail().equals(mEmailDoente)) {
                        nameTV.setText(doente.getName());
                        ageTV.setText(doente.getAge());
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
                Log.w(TAG, "Failed to read value.", error.toException());
            }
        });
    }

    public void insertIntoHashMap(Note note){
        HashMap<String,String> temp=new HashMap<String, String>();
        temp.put(noteName, note.getNote());
        temp.put(noteDate, note.getDate());
        list.add(temp);
    }

    public void checkUserID(){
        String[] output = mEmailDoente.split("@");
        userID = output[0];
    }
}

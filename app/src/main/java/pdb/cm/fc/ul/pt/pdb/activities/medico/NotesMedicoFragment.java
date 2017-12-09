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
import pdb.cm.fc.ul.pt.pdb.adapters.NotesViewAdapter;
import pdb.cm.fc.ul.pt.pdb.services.firebase.Firebase;
import pdb.cm.fc.ul.pt.pdb.services.firebase.FirebaseMedico;

import static android.content.ContentValues.TAG;
import static pdb.cm.fc.ul.pt.pdb.activities.medico.DoenteDetalhesActivity.EXTRA_DOENTE;
import static pdb.cm.fc.ul.pt.pdb.models.Constants.noteDate;
import static pdb.cm.fc.ul.pt.pdb.models.Constants.noteName;


public class NotesMedicoFragment extends Fragment implements Firebase.LoadNotes {


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_notes, container, false);
        (rootView.findViewById(R.id.login_progress)).setVisibility(View.VISIBLE);
        Doente doente = (Doente) getArguments().getSerializable(EXTRA_DOENTE);
        FirebaseMedico.fetchAllNotes(this, doente.getName());
        ((TextView) rootView.findViewById(R.id.name)).setText(doente.getName());
        ((TextView) rootView.findViewById(R.id.age)).setText(doente.getAge());
        return rootView;
    }


    @Override
    public void loadNotes(ArrayList<Note> notes) {
        (getView().findViewById(R.id.login_progress)).setVisibility(View.GONE);
        ((ListView) getView().findViewById(R.id.listViewNotes)).setAdapter(new NotesViewAdapter(getActivity(), notes));
    }

}

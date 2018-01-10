package pdb.cm.fc.ul.pt.pdb.activities.medico.dashboard;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

import pdb.cm.fc.ul.pt.pdb.R;
import pdb.cm.fc.ul.pt.pdb.adapters.NotesViewAdapter;
import pdb.cm.fc.ul.pt.pdb.models.Doente;
import pdb.cm.fc.ul.pt.pdb.models.Note;
import pdb.cm.fc.ul.pt.pdb.services.firebase.Firebase;
import pdb.cm.fc.ul.pt.pdb.services.firebase.FirebaseMedico;
import pdb.cm.fc.ul.pt.pdb.utilities.Utilities;

import static pdb.cm.fc.ul.pt.pdb.activities.medico.MedicoDashboardMainActivity.EXTRA_DOENTE;

/**
 * Created by nunonelas on 24/12/17.
 */

public class MedicoNotesFragment extends Fragment implements Firebase.LoadNotes {

    private Doente doente;

    private ListView listNotes;

    public static MedicoNotesFragment newInstance() {
        MedicoNotesFragment fragment = new MedicoNotesFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_medico_notes, container, false);
        doente = (Doente) getActivity().getIntent().getExtras().getSerializable(EXTRA_DOENTE);
        FirebaseMedico.fetchAllNotes(this, doente.getId());
        ((TextView) view.findViewById(R.id.name)).setText(doente.getName());
        ((TextView) view.findViewById(R.id.age)).setText(doente.getAge());

        listNotes = (ListView) view.findViewById(R.id.listViewNotes);
        listNotes.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int position, long arg3) {
                // Get the selected item text from ListView
                Note note = (Note) arg0.getItemAtPosition(position);
                openPopUpOpenNote(note);

            }
        });
        listNotes.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {

            @Override
            public boolean onItemLongClick(AdapterView<?> arg0, View arg1, int position, long arg3) {
                // Get the selected item text from ListView
                Note note = (Note) arg0.getItemAtPosition(position);
                openPopUpRemoveNote(note);

                return true;
            }
        });

        FloatingActionButton fab = (FloatingActionButton) view.findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openPopUpInsertNewNote();
            }
        });

        return view;
    }

    @Override
    public void loadNotes(ArrayList<Note> notes) {
        (getView().findViewById(R.id.login_progress)).setVisibility(View.GONE);
        (getView().findViewById(R.id.wait_message)).setVisibility(View.GONE);
        ((ListView) getView().findViewById(R.id.listViewNotes)).setAdapter(new NotesViewAdapter(getContext(), notes));
    }

    private void openPopUpInsertNewNote(){
        final EditText input = new EditText(getContext());

        new AlertDialog.Builder(getActivity())
                .setTitle("Insert new note")
                .setView(input)
                .setPositiveButton("Insert", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        createNote(input.getText().toString());
                    }
                }).setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                // Do nothing.
            }
        }).show();
    }

    private void createNote(String textNote){
        Note note = new Note(textNote, Utilities.getTimestamp());

        FirebaseMedico.insertNote(note, doente);
        Toast.makeText(getContext(),
                "Note successfully added to database!",
                Toast.LENGTH_SHORT).show();
    }

    private void openPopUpRemoveNote(final Note note){
        new AlertDialog.Builder(getActivity())
                .setTitle("Do you really want to remove this note?")
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        FirebaseMedico.removeNote(note, doente);
                        Toast.makeText(getContext(), "Note successfully removed from database", Toast.LENGTH_SHORT).show();
                    }
                }).setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                // Do nothing.
            }
        }).show();
    }

    private void openPopUpOpenNote(final Note note){
        new AlertDialog.Builder(getActivity())
                .setTitle("Note")
                .setMessage("Note: "+note.getNote()+"\n\n"+"Date: "+note.getDate())
                .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                    }
                }).show();
    }
}

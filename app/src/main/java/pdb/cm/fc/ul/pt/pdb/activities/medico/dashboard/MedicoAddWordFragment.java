package pdb.cm.fc.ul.pt.pdb.activities.medico.dashboard;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.text.Editable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import pdb.cm.fc.ul.pt.pdb.R;
import pdb.cm.fc.ul.pt.pdb.services.firebase.FirebaseMedico;

import static android.content.ContentValues.TAG;

/**
 * Created by nunonelas on 24/12/17.
 */

public class MedicoAddWordFragment extends Fragment {

    private ListView listView;
    private List<String> wordsList;

    private static final String TBL_WORDS = "words";

    public static MedicoAddWordFragment newInstance() {
        MedicoAddWordFragment fragment = new MedicoAddWordFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_medico_addwords, container, false);

        FloatingActionButton fab = (FloatingActionButton) view.findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openPopUpInsertNewWord();
            }
        });

        listView = (ListView) view.findViewById(R.id.listWords);

        getWordsFromFirebase();

        return view;
    }

    private void openPopUpInsertNewWord(){
        final EditText input = new EditText(getContext());

        new AlertDialog.Builder(getActivity())
                .setTitle("Insert new word")
                .setView(input)
                .setPositiveButton("Insert", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        FirebaseMedico.sendNewWord(input.getText().toString());
                        Toast.makeText(getContext(),
                                "Word "+input.getText().toString()+" was added successfully to database!",
                                Toast.LENGTH_SHORT).show();
                    }
                }).setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                // Do nothing.
            }
        }).show();
    }

    private void refreshList(){
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(
                getContext(),
                android.R.layout.simple_list_item_1,
                wordsList);

        listView.setAdapter(arrayAdapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int position, long arg3) {
                // Get the selected item text from ListView
                String selectedItem = (String) arg0.getItemAtPosition(position);
                openPopUpRemoveWord(selectedItem);

            }
        });
    }

    private void openPopUpRemoveWord(final String word){
        new AlertDialog.Builder(getActivity())
                .setTitle("Do you really want to remove "+word+"?")
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        FirebaseMedico.removeWord(word);
                        Toast.makeText(getContext(),
                                "Word "+word+" was successfully removed successfully from database!",
                                Toast.LENGTH_SHORT).show();
                    }
                }).setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                // Do nothing.
            }
        }).show();
    }

    private void getWordsFromFirebase() {
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference(TBL_WORDS);
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                wordsList = new ArrayList<String>();
                for (DataSnapshot wordsSnapshot : dataSnapshot.getChildren()) {
                    wordsList.add(wordsSnapshot.getValue().toString());
                }

                refreshList();
            }

            @Override
            public void onCancelled(DatabaseError error) {
                Log.w(TAG, "Failed to read value. ", error.toException());
            }
        });
    }
}

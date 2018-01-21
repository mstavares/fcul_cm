package pdb.cm.fc.ul.pt.pdb.activities.medico.dashboard;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import pdb.cm.fc.ul.pt.pdb.R;
import pdb.cm.fc.ul.pt.pdb.services.firebase.Firebase;
import pdb.cm.fc.ul.pt.pdb.services.firebase.FirebaseMedico;

/**
 * Created by nunonelas on 24/12/17.
 */

public class MedicoAddWordFragment extends Fragment implements Firebase.LoadWords {

    private ArrayList<String> wordsList;

    @BindView(R.id.listWords) ListView listView;

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
        ButterKnife.bind(this, view);
        setup();
        return view;
    }

    private void setup(){
        FirebaseMedico.getWordsFromFirebase(this);
    }

    @Override
    public void loadWords(ArrayList<String> wordsList){
        this.wordsList = wordsList;
        refreshList();
    }

    @OnClick(R.id.fab)
    public void openPopUpInsertNewWord(){
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
}

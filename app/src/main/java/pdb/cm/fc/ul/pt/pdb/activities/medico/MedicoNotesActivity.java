package pdb.cm.fc.ul.pt.pdb.activities.medico;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;

import pdb.cm.fc.ul.pt.pdb.R;
import pdb.cm.fc.ul.pt.pdb.models.Doente;
import pdb.cm.fc.ul.pt.pdb.models.Note;
import pdb.cm.fc.ul.pt.pdb.adapters.NotesViewAdapter;
import pdb.cm.fc.ul.pt.pdb.services.firebase.Firebase;
import pdb.cm.fc.ul.pt.pdb.services.firebase.FirebaseMedico;

import static pdb.cm.fc.ul.pt.pdb.activities.medico.MedicoDashboardActivity.EXTRA_DOENTE;

public class MedicoNotesActivity extends AppCompatActivity implements Firebase.LoadNotes {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_medico_notes);
        (findViewById(R.id.login_progress)).setVisibility(View.VISIBLE);
        Doente doente = (Doente) getIntent().getExtras().getSerializable(EXTRA_DOENTE);
        FirebaseMedico.fetchAllNotes(this, doente.getName());
        ((TextView) findViewById(R.id.name)).setText(doente.getName());
        ((TextView) findViewById(R.id.age)).setText(doente.getAge());

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

    }

    @Override
    public void loadNotes(ArrayList<Note> notes) {
        (findViewById(R.id.login_progress)).setVisibility(View.GONE);
        ((ListView) findViewById(R.id.listViewNotes)).setAdapter(new NotesViewAdapter(getApplicationContext(), notes));
    }

}

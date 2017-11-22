package pdb.cm.fc.ul.pt.pdb.activities.medico;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;

import pdb.cm.fc.ul.pt.pdb.R;
import pdb.cm.fc.ul.pt.pdb.models.Doente;
import pdb.cm.fc.ul.pt.pdb.views.ListViewAdapter;

import static pdb.cm.fc.ul.pt.pdb.models.Constants.email;
import static pdb.cm.fc.ul.pt.pdb.models.Constants.lastLogin;
import static pdb.cm.fc.ul.pt.pdb.models.Constants.name;
import static pdb.cm.fc.ul.pt.pdb.models.Constants.photo;

import static android.content.ContentValues.TAG;

public class MedicoMainActivity extends AppCompatActivity {

    private ArrayList<HashMap<String, String>> list;

    private ListView listView;
    private TextView welcomeTV;
    private String emailTV;

    private DatabaseReference mDatabaseDoentes;

    private Doente doente;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Bundle bundle = getIntent().getExtras();
        emailTV = bundle.getString("email");

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_doentes);

        welcomeTV = (TextView) findViewById(R.id.welcome);
        listView = (ListView)findViewById(R.id.listViewDoentes);

        list=new ArrayList<HashMap<String,String>>();

        setWelcomeMessage();

        mDatabaseDoentes = FirebaseDatabase.getInstance().getReference("doentes");

        getDoentesFirebase();

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener()
        {
            @Override
            public void onItemClick(AdapterView<?> parent, final View view, int position, long id)
            {
                String emailDoente = ((TextView) view.findViewById(R.id.email)).getText().toString();
                startActivity(new Intent(getApplicationContext(), DoenteDetalhesActivity.class).putExtra("emailDoente", emailDoente));

                //Toast toast=Toast.makeText(getApplicationContext(), emailDoente, Toast.LENGTH_SHORT);
                //toast.show();
            }

        });

    }

    public void setWelcomeMessage(){
        welcomeTV.setText(getString(R.string.welcome_doctor, emailTV));
    }

    public void getDoentesFirebase(){
        mDatabaseDoentes.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                list=new ArrayList<HashMap<String,String>>();

                for (DataSnapshot doentesSnapshot: dataSnapshot.getChildren()) {
                    doente = doentesSnapshot.getValue(Doente.class);
                    if (doente.getMedicoAssign().equals("m1")) {
                        insertIntoHashMap(doente);
                    }
                }
                ListViewAdapter adapter=new ListViewAdapter(MedicoMainActivity.this, list);
                listView.setAdapter(adapter);
            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
                Log.w(TAG, "Failed to read value.", error.toException());
            }
        });
    }

    public void insertIntoHashMap(Doente doente){
        HashMap<String,String> temp=new HashMap<String, String>();
        temp.put(photo, doente.getPhoto());
        temp.put(name, doente.getName());
        temp.put(email, doente.getEmail());
        temp.put(lastLogin, doente.getLastLogin());
        list.add(temp);
    }
}
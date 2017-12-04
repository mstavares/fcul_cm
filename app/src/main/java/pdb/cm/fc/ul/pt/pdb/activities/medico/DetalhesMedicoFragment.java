package pdb.cm.fc.ul.pt.pdb.activities.medico;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import pdb.cm.fc.ul.pt.pdb.R;
import pdb.cm.fc.ul.pt.pdb.models.Doente;

import static android.content.ContentValues.TAG;

public class DetalhesMedicoFragment extends Fragment {

    private String mEmailDoente;
    private TextView nameTV;
    private TextView ageTV;
    private Doente doente;
    private DatabaseReference mDatabaseDoentes;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_detalhes, container, false);

        nameTV = (TextView) rootView.findViewById(R.id.name);
        ageTV = (TextView) rootView.findViewById(R.id.age);

        if (getArguments() != null) {
            mEmailDoente = getArguments().getString("emailDoente");
            mDatabaseDoentes = FirebaseDatabase.getInstance().getReference("doentes");
            getUserData();
        }

        return rootView;
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
}

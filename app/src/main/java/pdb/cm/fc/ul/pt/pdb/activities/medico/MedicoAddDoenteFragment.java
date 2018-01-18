package pdb.cm.fc.ul.pt.pdb.activities.medico;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import pdb.cm.fc.ul.pt.pdb.R;
import pdb.cm.fc.ul.pt.pdb.activities.LoginActivity;
import pdb.cm.fc.ul.pt.pdb.activities.SplashActivity;
import pdb.cm.fc.ul.pt.pdb.models.Doente;
import pdb.cm.fc.ul.pt.pdb.preferences.UserPreferences;
import pdb.cm.fc.ul.pt.pdb.services.firebase.FirebaseMedico;

/**
 * Created by nunonelas on 27/12/17.
 */

public class MedicoAddDoenteFragment extends Fragment {

    private static final String TAG = MedicoAddDoenteFragment.class.getSimpleName();

    private FirebaseAuth mAuth1;
    private FirebaseAuth mAuth2;

    private static final String DOMAIN = "@paciente.pdb.pt";
    private static final int DEFAULT_TIMEBALL = 90;
    private static final int DEFAULT_TIMEWORDS = 90;
    private static final String TBL_DOENTES = "doentes";
    private static final String DATABASE_URL = "https://fcul-cm.firebaseio.com/";
    private static final String PROJECT_ID = "fcul-cm";
    private static final String WEB_API_KEY = "AIzaSyBesiKVzx8qlAF-udUvLIwgWQm6JbC5PLg";
    private static final String APP_NAME = "Secondary";

    private String mMedicoEmail;

    private EditText mName;
    private EditText mPassword;
    private EditText mAge;

    private TextView mEmail;
    private String mID;

    private Button mAddUser;

    public static MedicoAddDoenteFragment newInstance() {
        MedicoAddDoenteFragment fragment = new MedicoAddDoenteFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_medico_add_doentes, container, false);
        mAuth1 = FirebaseAuth.getInstance();

        FirebaseOptions firebaseOptions = new FirebaseOptions.Builder()
                .setDatabaseUrl(DATABASE_URL)
                .setApiKey(WEB_API_KEY)
                .setApplicationId(PROJECT_ID).build();

        FirebaseApp myApp = FirebaseApp.initializeApp(getActivity().getApplicationContext(), firebaseOptions,
                APP_NAME);

        mAuth2 = FirebaseAuth.getInstance(myApp);

        fetchLastPatientID();
        mMedicoEmail = UserPreferences.getEmail(getContext());

        mName = (EditText) view.findViewById(R.id.str_name);
        mPassword = (EditText) view.findViewById(R.id.str_password);
        mAge = (EditText) view.findViewById(R.id.str_age);

        mEmail = (TextView) view.findViewById(R.id.str_email);

        mAddUser = (Button) view.findViewById(R.id.btn_add_user);
        mAddUser.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                addPatient(createEmail(mID), mPassword.getText().toString());
            }
        });

        return view;
    }

    private void addPatient(final String email, String password){
        mAuth2.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(getActivity(), new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            mAuth2.signOut();
                            Log.d(TAG, "createUserWithEmail:success");
                            Toast.makeText(getContext(), "createUserWithEmail:success", Toast.LENGTH_LONG).show();
                            addPatientToTable(mID,
                                    mName.getText().toString(),
                                    mAge.getText().toString(),
                                    email,
                                    mMedicoEmail, Integer.toString(DEFAULT_TIMEBALL), Integer.toString(DEFAULT_TIMEWORDS));
                            startActivity(new Intent(getActivity(), MedicoMainActivity.class));
                        } else {
                            Log.w(TAG, "createUserWithEmail:failure", task.getException());
                            Toast.makeText(getContext(), task.getException().toString(), Toast.LENGTH_LONG).show();
                        }
                    }
                });
    }

    private void addPatientToTable(String id, String name, String age, String email, String medicoAssign, String timeBall, String timeWords){
        Doente doente = new Doente(id, name, age, email, medicoAssign, timeBall, timeWords);
        FirebaseMedico.addUserToTable(doente);
    }

    private String createEmail(String id){
        return id.concat(DOMAIN);
    }

    private void fetchLastPatientID(){
        final DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference(TBL_DOENTES);
        Query deleteQuery = databaseReference.orderByChild("id").limitToLast(1);
        deleteQuery.addListenerForSingleValueEvent(new ValueEventListener() {

            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot doentesSnapshot: dataSnapshot.getChildren()) {
                    Doente lastDoenteOnFirebase = doentesSnapshot.getValue(Doente.class);
                    getNewID(lastDoenteOnFirebase);
                }
            }

            @Override
            public void onCancelled(DatabaseError error) {
                Log.e(TAG, "Failed to read value. ", error.toException());
            }
        });
    }

    private void getNewID(Doente lastDoenteOnFirebase){
        String lastID = lastDoenteOnFirebase.getId().substring(1);
        int newID = Integer.valueOf(lastID) + 1;
        mID = "p"+String.valueOf(newID);
        mEmail.setText(createEmail(mID));
    }
}

package pdb.cm.fc.ul.pt.pdb.activities.medico;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
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

import butterknife.BindView;
import pdb.cm.fc.ul.pt.pdb.R;
import pdb.cm.fc.ul.pt.pdb.models.Doente;
import pdb.cm.fc.ul.pt.pdb.preferences.UserPreferences;
import pdb.cm.fc.ul.pt.pdb.services.firebase.Firebase;
import pdb.cm.fc.ul.pt.pdb.services.firebase.FirebaseMedico;

/**
 * Created by nunonelas on 27/12/17.
 */

public class MedicoAddDoenteFragment extends Fragment implements Firebase.LoadLastPatient {

    private static final String TAG = MedicoAddDoenteFragment.class.getSimpleName();

    private FirebaseAuth mAuth1;
    private FirebaseAuth mAuth2;

    private static final String DOMAIN = "@paciente.pdb.pt";

    private static final int DEFAULT_TIMEBALL = 90;
    private static final int DEFAULT_TIMEWORDS = 90;

    private static final String DATABASE_URL = "https://fcul-cm.firebaseio.com/";
    private static final String PROJECT_ID = "fcul-cm";
    private static final String WEB_API_KEY = "AIzaSyBesiKVzx8qlAF-udUvLIwgWQm6JbC5PLg";
    private static final String APP_NAME = "Secondary";

    @BindView(R.id.str_name) EditText mName;
    @BindView(R.id.str_password) EditText mPassword;
    @BindView(R.id.str_age) EditText mAge;
    @BindView(R.id.str_email) TextView mEmail;

    private String mID;
    private String mMedicoEmail;

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

        mMedicoEmail = UserPreferences.getEmail(getContext());

        setup();

        Button mAddUser = (Button) view.findViewById(R.id.btn_add_user);
        mAddUser.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                addPatient(createEmail(mID), mPassword.getText().toString());
            }
        });

        return view;
    }

    private void setup () {
        createNewInstanceOfFirebase();
        FirebaseMedico.fetchLastPatientID(this);
    }

    @Override
    public void loadLastPatient(Doente lastDoenteOnFirebase){
        getNewID(lastDoenteOnFirebase);
    }

    private void createNewInstanceOfFirebase(){
        mAuth1 = FirebaseAuth.getInstance();

        FirebaseOptions firebaseOptions = new FirebaseOptions.Builder()
                .setDatabaseUrl(DATABASE_URL)
                .setApiKey(WEB_API_KEY)
                .setApplicationId(PROJECT_ID).build();

        FirebaseApp myApp = FirebaseApp.initializeApp(getActivity().getApplicationContext(), firebaseOptions,
                APP_NAME);

        mAuth2 = FirebaseAuth.getInstance(myApp);
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


    private void getNewID(Doente lastDoenteOnFirebase){
        String lastID = lastDoenteOnFirebase.getId().substring(1);
        int newID = Integer.valueOf(lastID) + 1;
        mID = "p"+String.valueOf(newID);
        mEmail.setText(createEmail(mID));
    }
}

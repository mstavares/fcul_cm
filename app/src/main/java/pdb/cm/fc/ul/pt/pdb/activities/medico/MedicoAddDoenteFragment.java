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
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

import pdb.cm.fc.ul.pt.pdb.R;
import pdb.cm.fc.ul.pt.pdb.activities.LoginActivity;
import pdb.cm.fc.ul.pt.pdb.models.Doente;
import pdb.cm.fc.ul.pt.pdb.preferences.UserPreferences;
import pdb.cm.fc.ul.pt.pdb.services.firebase.FirebaseMedico;

/**
 * Created by nunonelas on 27/12/17.
 */

public class MedicoAddDoenteFragment extends Fragment {

    private static final String TAG = MedicoAddDoenteFragment.class.getSimpleName();

    private FirebaseAuth mAuth;

    private static final String DOMAIN = "@paciente.pdb.pt";
    private static final int DEFAULT_TIMEBALL = 90;
    private static final int DEFAULT_TIMEWORDS = 90;

    private String mMedicoEmail;

    private EditText mName;
    private EditText mID;
    private EditText mPassword;
    private EditText mAge;

    private TextView mEmail;

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
        mAuth = FirebaseAuth.getInstance();

        mMedicoEmail = UserPreferences.getEmail(getContext());

        mName = (EditText) view.findViewById(R.id.str_name);
        mID = (EditText) view.findViewById(R.id.str_id);
        mPassword = (EditText) view.findViewById(R.id.str_password);
        mAge = (EditText) view.findViewById(R.id.str_age);

        mEmail = (TextView) view.findViewById(R.id.str_email);

        mID.addTextChangedListener(new TextWatcher() {
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            public void onTextChanged(CharSequence s, int start, int before, int count) {
                //This sets a textview to the current length
                mEmail.setText(String.valueOf(s).concat(DOMAIN));
            }

            public void afterTextChanged(Editable s) {
            }
        });

        mAddUser = (Button) view.findViewById(R.id.btn_add_user);
        mAddUser.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                addPatient(createEmail(mID.getText().toString()), mPassword.getText().toString());
            }
        });

        return view;
    }

    public void addPatient(final String email, String password){
        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(getActivity(), new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            Log.d(TAG, "createUserWithEmail:success");
                            addPatientToTable(mID.getText().toString(),
                                            mName.getText().toString(),
                                            mAge.getText().toString(),
                                            email,
                                            mMedicoEmail, Integer.toString(DEFAULT_TIMEBALL), Integer.toString(DEFAULT_TIMEWORDS));
                            mAuth.signOut();
                            startActivity(new Intent(getActivity(), LoginActivity.class));
                        } else {
                            Log.w(TAG, "createUserWithEmail:failure", task.getException());
                            Toast.makeText(getContext(), task.getException().toString(), Toast.LENGTH_LONG).show();
                        }
                    }
                });
    }

    public void addPatientToTable(String id, String name, String age, String email, String medicoAssign, String timeBall, String timeWords){
        Doente doente = new Doente(id, name, age, email, medicoAssign, timeBall, timeWords);
        FirebaseMedico.addUserToTable(doente);
    }

    public String createEmail(String id){
        return id.concat(DOMAIN);
    }
}

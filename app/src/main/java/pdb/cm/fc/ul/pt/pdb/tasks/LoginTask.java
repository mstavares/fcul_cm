package pdb.cm.fc.ul.pt.pdb.tasks;


import android.support.annotation.NonNull;
import android.util.Log;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;

import pdb.cm.fc.ul.pt.pdb.activities.medico.MedicoMainActivity;
import pdb.cm.fc.ul.pt.pdb.interfaces.Login;
import pdb.cm.fc.ul.pt.pdb.models.Doente;
import pdb.cm.fc.ul.pt.pdb.models.LoginCredentials;
import pdb.cm.fc.ul.pt.pdb.views.ListViewAdapter;

import static android.content.ContentValues.TAG;

public class LoginTask implements OnCompleteListener {

    private static final String TAG = LoginTask.class.getSimpleName();

    private FirebaseAuth mFirebaseAuth;
    private Login.Presenter mPresenter;
    private LoginCredentials mLoginCredentials;


    public LoginTask(Login.Presenter presenter, LoginCredentials loginCredentials) {
        mPresenter = presenter;
        mLoginCredentials = loginCredentials;
        mFirebaseAuth = FirebaseAuth.getInstance();
    }

    public void executeLogin() {
        mFirebaseAuth.signInWithEmailAndPassword(mLoginCredentials.getEmail(), mLoginCredentials.getPassword())
                .addOnCompleteListener(this);
    }

    @Override
    public void onComplete(@NonNull Task task) {
        if(task.isSuccessful()) {
            Log.i(TAG, "Authentication successful");
            mPresenter.onLoginOk();
        } else {
            Log.i(TAG, "Authentication without success");
            mPresenter.onLoginError();
        }
    }
}

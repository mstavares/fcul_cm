package pdb.cm.fc.ul.pt.pdb.tasks;


import android.support.annotation.NonNull;
import android.util.Log;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;

import pdb.cm.fc.ul.pt.pdb.activities.doente.DoenteMainActivity;
import pdb.cm.fc.ul.pt.pdb.activities.medico.MedicoMainActivity;
import pdb.cm.fc.ul.pt.pdb.interfaces.Login;
import pdb.cm.fc.ul.pt.pdb.models.LoginCredentials;
import pdb.cm.fc.ul.pt.pdb.services.firebase.FirebaseDoente;

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

    private Class getMainActivity() {
        if(mLoginCredentials.isPaciente()) {
            FirebaseDoente.setLastLogin(mLoginCredentials.getEmail());
            return DoenteMainActivity.class;
        }
        return MedicoMainActivity.class;
    }

    @Override
    public void onComplete(@NonNull Task task) {
        if(task.isSuccessful()) {
            Log.i(TAG, "Authentication successful");
            mPresenter.onLoginOk(getMainActivity());
        } else {
            Log.i(TAG, "Authentication without success");
            mPresenter.onLoginError();
        }
    }
}

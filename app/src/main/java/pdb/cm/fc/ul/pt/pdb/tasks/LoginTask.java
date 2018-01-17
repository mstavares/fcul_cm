package pdb.cm.fc.ul.pt.pdb.tasks;


import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.util.Log;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;

import pdb.cm.fc.ul.pt.pdb.activities.doente.DoenteMainActivity;
import pdb.cm.fc.ul.pt.pdb.activities.medico.MedicoMainActivity;
import pdb.cm.fc.ul.pt.pdb.interfaces.Login;
import pdb.cm.fc.ul.pt.pdb.models.LoginCredential;
import pdb.cm.fc.ul.pt.pdb.services.android.ApplicationService;
import pdb.cm.fc.ul.pt.pdb.services.firebase.FirebaseDoente;

public class LoginTask implements OnCompleteListener {

    private static final String TAG = LoginTask.class.getSimpleName();

    private Context mContext;
    private FirebaseAuth mFirebaseAuth;
    private Login.Presenter mPresenter;
    private LoginCredential mLoginCredential;


    public LoginTask(Login.Presenter presenter, Context context, LoginCredential loginCredential) {
        mContext = context;
        mPresenter = presenter;
        mLoginCredential = loginCredential;
        mFirebaseAuth = FirebaseAuth.getInstance();
    }

    public void executeLogin() {
        mFirebaseAuth.signInWithEmailAndPassword(mLoginCredential.getEmail(), mLoginCredential.getPassword())
                .addOnCompleteListener(this);
    }

    private Class getMainActivity() {
        if(mLoginCredential.isPaciente()) {
            FirebaseDoente.sendLastLogin(mLoginCredential.getEmail());


            mContext.startService(new Intent(mContext, ApplicationService.class));


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

package pdb.cm.fc.ul.pt.pdb.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.Date;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import pdb.cm.fc.ul.pt.pdb.R;
import pdb.cm.fc.ul.pt.pdb.activities.doente.DoenteMainActivity;
import pdb.cm.fc.ul.pt.pdb.activities.medico.MedicoMainActivity;
import pdb.cm.fc.ul.pt.pdb.interfaces.Login;
import pdb.cm.fc.ul.pt.pdb.models.Doente;
import pdb.cm.fc.ul.pt.pdb.presenters.LoginPresenter;

/**
 * A login screen that offers login via email/password.
 */
public class LoginActivity extends AppCompatActivity implements Login.View {

    /** This variable is used to debug this class */
    private static final String TAG = LoginActivity.class.getSimpleName();

    /** UI references. */
    @BindView(R.id.email) EditText mEmailView;
    @BindView(R.id.password) EditText mPasswordView;
    @BindView(R.id.login_progress) View mProgressView;
    @BindView(R.id.login_form) View mLoginFormView;
    @BindView(R.id.wait_message) TextView mWaitMessageView;

    /** MVP Presenter */
    private Login.Presenter mPresenter;

    private DatabaseReference mDatabase;
    private Doente doente;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        setup();
    }

    private void setup() {
        ButterKnife.bind(this);
        mPresenter = new LoginPresenter(this);
        mPasswordView.setOnEditorActionListener(this);
    }

    @OnClick(R.id.email_sign_in_button)
    public void submit() {
        /** Reset errors. */
        resetErrors();
        /** Dismiss the keyboard */
        dismissKeyboard();
        /** Show loading */
        onShowProgressBar();
        /** Store values at the time of the login attempt. */
        String email = mEmailView.getText().toString();
        String password = mPasswordView.getText().toString();
        mPresenter.onAttemptLogin(email, password);
    }

    private void resetErrors() {
        mEmailView.setError(null);
        mPasswordView.setError(null);
    }

    private void dismissKeyboard() {
        InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
    }

    private void showProgress(boolean show) {
        mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
        mWaitMessageView.setVisibility(show ? View.VISIBLE : View.GONE);
        mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
    }

    @Override
    public boolean onEditorAction(TextView textView, int id, KeyEvent keyEvent) {
        if (id == R.id.login || id == EditorInfo.IME_NULL) {
            submit();
            return true;
        }
        return false;
    }

    @Override
    public void onShowProgressBar() {
        showProgress(true);
    }

    @Override
    public void onLoginError() {
        showProgress(false);
        Toast.makeText(this, getString(R.string.error_incorrect_password), Toast.LENGTH_LONG).show();
        mEmailView.requestFocus();
    }

    @Override
    public void onLoginOk() {
        String email = mEmailView.getText().toString();
        if(email.contains("medico")){
            startActivity(new Intent(this, MedicoMainActivity.class).putExtra("email", email));
        } else if (email.contains("paciente")) {
            setLastLogin(email);
            startActivity(new Intent(this, DoenteMainActivity.class));
        }
        finish();
    }

    public void setLastLogin(final String email){
        SimpleDateFormat sdf = new SimpleDateFormat("dd/M/yyyy HH:mm:ss");
        final String lastLogin = sdf.format(new Date());
        mDatabase = FirebaseDatabase.getInstance().getReference("doentes");
        mDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                for (DataSnapshot registosSnapshot: dataSnapshot.getChildren()) {
                    doente = registosSnapshot.getValue(Doente.class);
                    if (doente.getEmail().equals(email)) {
                        mDatabase.child(doente.getId()).child("lastLogin").setValue(lastLogin);
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

    @Override
    public void onEmptyEmail() {
        showProgress(false);
        mEmailView.setError(getString(R.string.error_field_required));
        mEmailView.requestFocus();
    }

    @Override
    public void onInvalidEmail() {
        showProgress(false);
        mEmailView.setError(getString(R.string.error_invalid_email));
        mEmailView.requestFocus();
    }

    @Override
    public void onEmptyPassword() {
        showProgress(false);
        mPasswordView.setError(getString(R.string.error_field_required));
        mPasswordView.requestFocus();
    }

    @Override
    public void onInvalidPassword() {
        showProgress(false);
        mPasswordView.setError(getString(R.string.error_invalid_password));
        mPasswordView.requestFocus();
    }

}


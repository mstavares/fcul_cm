package pdb.cm.fc.ul.pt.pdb.tasks;


import android.os.AsyncTask;

import pdb.cm.fc.ul.pt.pdb.interfaces.Login;
import pdb.cm.fc.ul.pt.pdb.models.LoginCredentials;

public class LoginTask extends AsyncTask<Void, Void, LoginTask.Results> {

    private static final String[] DUMMY_CREDENTIALS = new String[]{
            "foo@example.com:hello", "bar@example.com:world"
    };

    enum Results {AUTHENTICATED, REGISTERED, FAILED};
    private Login.Presenter mPresenter;
    private LoginCredentials mLoginCredentials;


    public LoginTask(Login.Presenter presenter, LoginCredentials loginCredentials) {
        mPresenter = presenter;
        mLoginCredentials = loginCredentials;
    }

    @Override
    protected Results doInBackground(Void... params) {
        // TODO: attempt authentication against a network service.

        try {
            // Simulate network access.
            Thread.sleep(4000);
        } catch (InterruptedException e) {
            return Results.FAILED;
        }

        for (String credential : DUMMY_CREDENTIALS) {
            String[] pieces = credential.split(":");
            if (pieces[0].equals(mLoginCredentials.getEmail())) {
                if(pieces[1].equals(mLoginCredentials.getPassword())) {
                    return Results.AUTHENTICATED;
                }
            }
        }

        // TODO: register the new account here.
        return Results.REGISTERED;
    }

    @Override
    protected void onPostExecute(final Results result) {
        if (result == Results.AUTHENTICATED) {
            mPresenter.onLoginOk();
        } else if (result == Results.REGISTERED) {
            mPresenter.onUserRegistered();
        } else {
            mPresenter.onLoginError();
        }
    }

    @Override
    protected void onCancelled() {
        mPresenter.onLoginCancelled();
    }
}

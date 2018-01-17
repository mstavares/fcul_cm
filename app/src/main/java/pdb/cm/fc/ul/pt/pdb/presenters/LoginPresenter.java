package pdb.cm.fc.ul.pt.pdb.presenters;


import android.content.Context;

import pdb.cm.fc.ul.pt.pdb.exceptions.EmptyEmailException;
import pdb.cm.fc.ul.pt.pdb.exceptions.EmptyPasswordException;
import pdb.cm.fc.ul.pt.pdb.exceptions.InvalidEmailException;
import pdb.cm.fc.ul.pt.pdb.exceptions.InvalidPasswordException;
import pdb.cm.fc.ul.pt.pdb.interfaces.Login;
import pdb.cm.fc.ul.pt.pdb.models.LoginCredential;
import pdb.cm.fc.ul.pt.pdb.preferences.UserPreferences;
import pdb.cm.fc.ul.pt.pdb.tasks.LoginTask;

public class LoginPresenter implements Login.Presenter {

    private LoginCredential mCredential;
    private Login.View mView;
    private Context mContext;

    public LoginPresenter(Context context, Login.View view) {
        mContext = context;
        mView = view;
    }

    @Override
    public void onAttemptLogin(String email, String password) {
        try {
            mCredential = new LoginCredential(email, password);
            new LoginTask(this, mContext, mCredential).executeLogin();
        } catch (EmptyEmailException e) {
            mView.onEmptyEmail();
        } catch (EmptyPasswordException e) {
            mView.onEmptyPassword();
        } catch (InvalidEmailException e) {
            mView.onInvalidEmail();
        } catch (InvalidPasswordException e) {
            mView.onInvalidPassword();
        }
    }

    @Override
    public void onLoginError() {
        mView.onLoginError();
    }

    @Override
    public void onLoginOk(Class activity) {
        UserPreferences.pushLogin(mContext, mCredential.getEmail(), mCredential.getUser());
        mView.onLoginOk(activity);
    }

}

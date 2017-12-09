package pdb.cm.fc.ul.pt.pdb.presenters;


import pdb.cm.fc.ul.pt.pdb.exceptions.EmptyEmailException;
import pdb.cm.fc.ul.pt.pdb.exceptions.EmptyPasswordException;
import pdb.cm.fc.ul.pt.pdb.exceptions.InvalidEmailException;
import pdb.cm.fc.ul.pt.pdb.exceptions.InvalidPasswordException;
import pdb.cm.fc.ul.pt.pdb.interfaces.Login;
import pdb.cm.fc.ul.pt.pdb.models.LoginCredential;
import pdb.cm.fc.ul.pt.pdb.tasks.LoginTask;

public class LoginPresenter implements Login.Presenter {

    private Login.View mView;
    private LoginTask mLoginTask;

    public LoginPresenter(Login.View view) {
        mView = view;
    }

    @Override
    public void onAttemptLogin(String email, String password) {
        try {
            LoginCredential credentials = new LoginCredential(email, password);
            mLoginTask = new LoginTask(this, credentials);
            mLoginTask.executeLogin();
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
        mView.onLoginOk(activity);
    }

}

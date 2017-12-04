package pdb.cm.fc.ul.pt.pdb.interfaces;


import android.widget.TextView;

public interface Login {

    interface View extends TextView.OnEditorActionListener {
        void onLoginError();
        void onLoginOk(Class activity);
        void onEmptyEmail();
        void onInvalidEmail();
        void onEmptyPassword();
        void onInvalidPassword();
        void onShowProgressBar();
    }

    interface Presenter {
        void onLoginError();
        void onLoginOk(Class activity);
        void onAttemptLogin(String email, String password);
    }

}

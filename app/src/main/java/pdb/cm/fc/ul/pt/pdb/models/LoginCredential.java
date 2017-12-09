package pdb.cm.fc.ul.pt.pdb.models;


import pdb.cm.fc.ul.pt.pdb.exceptions.EmptyEmailException;
import pdb.cm.fc.ul.pt.pdb.exceptions.EmptyPasswordException;
import pdb.cm.fc.ul.pt.pdb.exceptions.InvalidEmailException;
import pdb.cm.fc.ul.pt.pdb.exceptions.InvalidPasswordException;

public class LoginCredential {

    private static final String PACIENT_LABEL = "paciente";
    private static final int MIN_PASSWORD_LEN = 4;
    private String mEmail, mPassword;

    public LoginCredential(String email, String password) throws EmptyEmailException,
            EmptyPasswordException, InvalidEmailException, InvalidPasswordException {
        validateCredentials(email, password);
        mEmail = email;
        mPassword = password;
    }

    private void validateCredentials(String email, String password) throws EmptyEmailException,
            EmptyPasswordException, InvalidEmailException, InvalidPasswordException {
        isEmailEmpty(email);
        isEmailValid(email);
        isPasswordEmpty(password);
        isPasswordValid(password);
    }

    private void isEmailEmpty(String email) throws EmptyEmailException {
        if(email.isEmpty())
            throw new EmptyEmailException();
    }

    private void isEmailValid(String email) throws InvalidEmailException {
        if(!email.contains("@") || !email.contains("."))
            throw new InvalidEmailException();
    }

    private void isPasswordEmpty(String password) throws EmptyPasswordException {
        if(password.isEmpty())
            throw new EmptyPasswordException();
    }

    private void isPasswordValid(String password) throws InvalidPasswordException {
        if(password.length() < MIN_PASSWORD_LEN)
            throw new InvalidPasswordException();
    }

    public String getEmail() {
        return mEmail;
    }

    public String getPassword() {
        return mPassword;
    }

    public boolean isPaciente() {
        return mEmail.contains(PACIENT_LABEL);
    }

}

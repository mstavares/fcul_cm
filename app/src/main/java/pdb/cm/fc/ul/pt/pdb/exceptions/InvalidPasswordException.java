package pdb.cm.fc.ul.pt.pdb.exceptions;


public class InvalidPasswordException extends Exception {

    public InvalidPasswordException() {
        super("The password is invalid < 4 digits");
    }
}

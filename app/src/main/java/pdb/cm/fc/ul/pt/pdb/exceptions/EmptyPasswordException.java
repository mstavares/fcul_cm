package pdb.cm.fc.ul.pt.pdb.exceptions;


public class EmptyPasswordException extends Exception {

    public EmptyPasswordException() {
        super("The field password is empty");
    }
}

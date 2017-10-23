package pdb.cm.fc.ul.pt.pdb.exceptions;


public class EmptyEmailException extends Exception {

    public EmptyEmailException() {
        super("The field email is empty");
    }
}

package pdb.cm.fc.ul.pt.pdb.exceptions;


public class InvalidEmailException extends Exception {

    public InvalidEmailException() {
        super("Invalid email, doesn't contains @ or .");
    }
}

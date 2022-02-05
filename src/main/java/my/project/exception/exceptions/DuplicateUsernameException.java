package my.project.exception.exceptions;

public class DuplicateUsernameException extends RuntimeException {

    public DuplicateUsernameException() {
        super("Found Duplicate Username in Database");
    }

    public DuplicateUsernameException(String msg) {
        super(msg);
    }

}

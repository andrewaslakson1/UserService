package my.project.util.exception.exceptions;

public class UserNotFoundException extends RuntimeException {

    public UserNotFoundException() {
        super("Could not locate user in database");
    }

    public UserNotFoundException(String msg) {
        super(msg);
    }

}

package dataaccess;

public class UnavailableException extends DataAccessException {
    public UnavailableException() {
        super("Error: already taken");
    }
}

package dataaccess;

public class UnauthorizedException extends DataAccessException {
    public UnauthorizedException() {
        super("Error: unauthorized");
    }
}

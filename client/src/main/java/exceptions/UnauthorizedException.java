package exceptions;

public class UnauthorizedException extends ServerException {
    public UnauthorizedException() {
        super("unauthorized");
    }
}

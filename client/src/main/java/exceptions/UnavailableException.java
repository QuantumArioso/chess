package exceptions;

public class UnavailableException extends ServerException {
    public UnavailableException() {
        super("403: that name is already taken");
    }
}

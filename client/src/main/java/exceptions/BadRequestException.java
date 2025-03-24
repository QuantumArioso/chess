package exceptions;

public class BadRequestException extends ServerException {
    public BadRequestException() {
        super("400: There is some missing/misformatted input");
    }
}

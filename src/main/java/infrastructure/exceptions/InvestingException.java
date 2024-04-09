package infrastructure.exceptions;

public class InvestingException extends RuntimeException {

    public InvestingException(String message) {
        super(message);
    }

    public InvestingException(String message, Throwable cause) {
        super(message, cause);
    }
}

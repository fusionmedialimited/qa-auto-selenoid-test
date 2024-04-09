package infrastructure.exceptions;

public class DriverCreationFailedRuntimeException extends RuntimeException {
    public DriverCreationFailedRuntimeException(String message) {
        super("Failed to start driver: ".concat(message));
    }
}

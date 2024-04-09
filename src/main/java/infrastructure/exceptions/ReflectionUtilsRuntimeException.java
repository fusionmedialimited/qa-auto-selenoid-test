package infrastructure.exceptions;

public class ReflectionUtilsRuntimeException extends RuntimeException {
    public ReflectionUtilsRuntimeException() {
        this(null, null);
    }

    public ReflectionUtilsRuntimeException(String message) {
        this(message, null);
    }

    public ReflectionUtilsRuntimeException(Throwable cause) {
        this(cause != null ? cause.getMessage() : null, cause);
    }

    public ReflectionUtilsRuntimeException(String message, Throwable cause) {
        super(message);
        if (cause != null) super.initCause(cause);
    }
}

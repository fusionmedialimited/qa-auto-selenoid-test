package infrastructure.exceptions;

public class InvalidSystemConfigException extends RuntimeException {

    public InvalidSystemConfigException(String systemConfigField) {
        throw new RuntimeException(String.format("System config field \"%s\" is not defined! " +
                "Please check pom.xml or maven command line parameters\n", systemConfigField));
    }
}

package infrastructure.allure;

import infrastructure.exceptions.InvestingException;
import infrastructure.functional.PredicateDescribed;
import infrastructure.utilities.ReflectionUtils;
import org.jetbrains.annotations.NotNull;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Arrays;

import static io.qameta.allure.Allure.addAttachment;

public class AllureUtils {

    /**
     * This method logs text message and attaches it to Allure report
     *
     * @param loggerClass class, which implements Log4j2 Logger instance
     * @param attachName  the name for the attached block with the text in the report
     * @param message     text to log
     */
    public static void logMessageAndAttachToAllure(Class<?> loggerClass, String attachName, String message) {
        String logMessage =
                "[Allure]["
                        .concat(attachName)
                        .concat("] ")
                        .concat(message).concat("\n");

        PredicateDescribed<Method> predicate =
                definePredicateToExtractLoggingMethod(attachName);

        Method loggingMethod =
                ReflectionUtils.getMethod(loggerClass, predicate);

        try {
            loggingMethod.invoke(null, logMessage);
        } catch (IllegalAccessException | InvocationTargetException cause) {
            throw new InvestingException("Couldn't invoke method from the Logger class " + loggerClass.getName(), cause);
        }

        addAttachment(attachName, message);
    }

    @NotNull
    private static PredicateDescribed<Method> definePredicateToExtractLoggingMethod(String attachName) {
        String logMethodName = switch (attachName.toUpperCase()) {
            case "ERROR" -> "error";
            case "WARN" -> "warn";
            default -> "info";
        };

        return new PredicateDescribed<>(
                "Method with name \"" + logMethodName + "\" and single argument of String type from the Logger class",
                method -> method.getName().equals(logMethodName)
                        && Arrays.equals(method.getParameterTypes(), new Class[]{ String.class })
        );
    }
}

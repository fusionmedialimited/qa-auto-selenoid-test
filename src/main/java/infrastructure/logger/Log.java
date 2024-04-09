package infrastructure.logger;

import infrastructure.exceptions.InvestingException;
import infrastructure.threadlocals.ThreadLocalScenario;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.ThreadContext;

import java.io.File;
import java.util.regex.Pattern;

public class Log {

    private static final Logger LOGGER = LogManager.getLogger();

    private static final String TEST_ID_KEY = "testId";
    private static final String LINE_NUMBER_KEY = "lineNumber";
    private static final String TEST_NAME_KEY = "testName";
    private static final String TRY_KEY = "tryNumber";

    private static final ThreadLocal<Integer> tryCounter = ThreadLocal.withInitial(() -> 0);


    public static void incTryCounter() {
        tryCounter.set(tryCounter.get() + 1);
    }

    public static void resetTryCounter() {
        tryCounter.set(0);
    }


    private static void putIntoThreadContext(String key, String value) {
        try {
            ThreadContext.put(key, value);
        } catch (Exception cause) {
            throw new InvestingException(
                    String.format("Couldn't put \"%s\" into logging thread context!", key),
                    cause
            );
        }
    }

    public static void prepareContext() {
        putIntoThreadContext(TEST_NAME_KEY, ThreadLocalScenario.getName());
        putIntoThreadContext(TEST_ID_KEY, ThreadLocalScenario.getTestId());
        putIntoThreadContext(LINE_NUMBER_KEY, String.valueOf(ThreadLocalScenario.getLine()));
        putIntoThreadContext(TRY_KEY, String.valueOf(tryCounter.get()));


        info(
                String.format(
                        "\n***********\nTest started: %s \nID: %s, Line: %s, Try: %s\n***********\n",
                        ThreadContext.get(TEST_NAME_KEY), ThreadContext.get(TEST_ID_KEY),
                        ThreadContext.get(LINE_NUMBER_KEY), ThreadContext.get(TRY_KEY)
                )
        );
    }

    public static void info(String message) {
        LOGGER.info(message);
    }

    public static void warn(String message) {
        LOGGER.warn(message);
    }

    public static void error(String message) {
        LOGGER.error(message);
    }

    public static void fatal(String message) {
        LOGGER.fatal(message);
    }

    public static void debug(String message) {
        LOGGER.debug(message);
    }
}

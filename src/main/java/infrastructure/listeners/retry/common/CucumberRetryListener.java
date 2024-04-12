package infrastructure.listeners.retry.common;

import infrastructure.constants.WebEnvParams;
import infrastructure.logger.Log;
import infrastructure.threadlocals.ThreadLocalScenario;
import io.cucumber.java.Scenario;
import org.testng.IRetryAnalyzer;
import org.testng.ITestResult;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * This Listener reruns the Cucumber scenario by defined retry count
 */
public class CucumberRetryListener implements IRetryAnalyzer {

    protected final AtomicInteger maxRetryNumber = new AtomicInteger(WebEnvParams.getRetriesNumberParam());
    protected final ConcurrentMap<String, AtomicInteger> map = new ConcurrentHashMap<>();

    protected enum Status {
        SUCCESS,
        FAILURE,
        SKIP
    }

    protected Status getStatus(ITestResult result) {
        return switch (result.getStatus()) {
            case ITestResult.SUCCESS -> Status.SUCCESS;
            case ITestResult.FAILURE -> Status.FAILURE;
            case ITestResult.SKIP -> Status.SKIP;
            default -> null;
        };
    }

    /**
     * The {@link Scenario#getId()} is not stable across multiple executions of Cucumber.
     * So the uri + line number is used to obtain a stable identifier of a scenario
     */
    protected String getScenario() {
        return String.format("%s (line %d)",
                ThreadLocalScenario.getName(),
                ThreadLocalScenario.getLine()
        );
    }

    protected AtomicInteger getRetryNumber() {
        int triesCompleted = map.getOrDefault(getScenario(), new AtomicInteger(0)).intValue();
        map.put(getScenario(), new AtomicInteger(++triesCompleted));

        return map.get(getScenario());
    }

    @Override
    public boolean retry(ITestResult result) {
        AtomicInteger retryNumber = getRetryNumber();

        try {
            if (retryNumber.intValue() < maxRetryNumber.intValue()) {
                Log.info(String.format(
                        "Retrying test \"%s\" with status %s for the %d time",
                        getScenario(),
                        getStatus(result),
                        retryNumber.intValue()
                ));

                return true;
            }
        } catch (Exception ignore) {}

        return false;
    }
}

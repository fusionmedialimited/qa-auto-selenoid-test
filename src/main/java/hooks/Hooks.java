package hooks;

import infrastructure.Investing;
import infrastructure.logger.Log;
import infrastructure.threadlocals.*;
import io.cucumber.java.After;
import io.cucumber.java.Before;
import io.cucumber.java.Scenario;

import static infrastructure.allure.AllureAttachments.allureAttachText;

public class Hooks {

    @Before(order = 1)
    public void beforePrepareThreadLocalVariables(Scenario scenario) {
        ThreadLocalScenario.put(scenario);
//        Log.prepareContext();
        clearThreadLocalData();
    }

    @Before(order = 2)
    public void beforeInitWebDriver() {
        ThreadLocalDriver.put(new Investing());
    }


    @After(order = 1)
    public void afterSteps(Scenario scenario) {
        clearThreadLocalData();
    }

    /**
     * Clear all thread local data, excepting Cucumber Scenario.
     * Scenario should be provided further into Retry listener
     */
    private void clearThreadLocalData() {
        try {
            ThreadLocalEdition.clear();
            ThreadLocalSesId.clear();
            ThreadLocalPopups.clear();
            ThreadLocalCookies.clear();
        } catch (Exception cause) {
            allureAttachText("error", "Couldn't clear thread local data: " + cause);
        }
    }

}

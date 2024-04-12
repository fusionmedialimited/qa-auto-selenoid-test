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
    }

    @Before(order = 2)
    public void beforeInitWebDriver() {
        ThreadLocalDriver.put(new Investing());
    }

    @After
    public void disposeDriver() {
        ThreadLocalDriver.get().dispose();
    }

}

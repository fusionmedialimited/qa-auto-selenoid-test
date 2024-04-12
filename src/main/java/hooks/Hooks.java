package hooks;

import infrastructure.Investing;
import infrastructure.threadlocals.ThreadLocalDriver;
import io.cucumber.java.After;
import io.cucumber.java.Before;

public class Hooks {

    @Before()
    public void beforeInitDriver() {
        ThreadLocalDriver.put(new Investing());
    }

    @After
    public void afterDisposeDriver() {
        ThreadLocalDriver.get().dispose();
    }

}

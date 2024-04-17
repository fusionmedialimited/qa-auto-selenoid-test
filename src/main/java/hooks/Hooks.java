package hooks;

import infrastructure.DriverUtilities;
import io.cucumber.java.After;
import io.cucumber.java.Before;

public class Hooks {

    @Before()
    public void beforeInitDriver() {
        DriverUtilities.initDriver();
    }

    @After
    public void afterDisposeDriver() {
        DriverUtilities.disposeDriver();
    }

}

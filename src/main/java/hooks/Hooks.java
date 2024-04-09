package hooks;

import infrastructure.Investing;
import infrastructure.allure.AllureUtils;
import infrastructure.constants.WebEnvParams;
import infrastructure.exceptions.InvestingException;
import infrastructure.logger.Log;
import infrastructure.threadlocals.*;
import io.cucumber.java.After;
import io.cucumber.java.Before;
import io.cucumber.java.Scenario;
import org.apache.commons.io.IOUtils;

import java.util.Objects;

import static infrastructure.ReportAttachments.*;
import static infrastructure.allure.AllureAttachments.allureAttachText;

public class Hooks {

    @Before(order = 1)
    public void beforePrepareThreadLocalVariables(Scenario scenario) {
        ThreadLocalScenario.put(scenario);
        Log.prepareContext();
        clearThreadLocalData();
    }


    @After(order = 1)
    public void afterSteps(Scenario scenario) {
//        if (scenario.isFailed() && !ThreadLocalScenario.containsTag("@API")) {
//            attachLogsAndFiles(scenario);
//        } else if (scenario.getStatus().equals(Status.SKIPPED) && ThreadLocalDriver.exists()) {
//            try {
//                cucumberAttachScreenshot("skipped step screenshot");
//            } catch (Exception e) {
//                allureAttachText("error", "Couldn't attach screenshot: " + e);
//            }
//        }

        clearThreadLocalData();
    }

    private void attachLogsAndFiles(Scenario scenario) {
        Investing investing = ThreadLocalDriver.get();

        try {
            cucumberAttachScreenshot("failed step screenshot");
        } catch (Exception e) {
            allureAttachText("error", "Couldn't attach screenshot: " + e);
        }

        if (WebEnvParams.getRunParam().equals("cloud")) {
            try {
                String sessionId = ThreadLocalDriver.getRemoteSessionId();
                try {
                    investing.quit();
                } catch (Exception e) {
                    allureAttachText("error", "Couldn't quit driver: " + e);
                }

                cucumberAttachVideo(scenario, "Recording for " + scenario.getName(),
                        IOUtils.toByteArray(
                                Objects.requireNonNull(
                                        getVideoFromServer(AllureUtils.getVideoUrl(sessionId))
                                )
                        )
                );
            } catch (Exception cause) {
                allureAttachText("error", "Couldn't attach video to allure report!");
                throw new InvestingException("Couldn't attach video to allure report!", cause);
            }
            finally {
                if (ThreadLocalDriver.exists())
                    investing.quit();
            }
        }
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

package infrastructure;


import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.stream.JsonReader;
import infrastructure.enums.LogLevel;
import infrastructure.logger.Log;
import infrastructure.threadlocals.ThreadLocalDriver;
import infrastructure.threadlocals.ThreadLocalScenario;
import io.cucumber.java.Scenario;
import org.apache.commons.io.FileUtils;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.logging.LogType;

import java.io.*;
import java.net.URL;

import static infrastructure.enums.LogLevel.ERROR;
import static org.testng.Assert.assertNotNull;

public class ReportAttachments {
// TODO: implement the getVideoFromFolder() the way it will work:

    /**
     * This method gets the video as an Input Stream directly from the Selenoid server from the specified URL
     *
     * @param url of the video on the Selenoid server
     */
    public static InputStream getVideoFromServer(URL url) {
        assertNotNull(url, "The specified URL is null");

        int lastSize = 0;
        int exit = 2;
        for (int i = 0; i < 20; i++) {
            try {
                int size = Integer.parseInt(url.openConnection().getHeaderField("Content-Length"));
                Log.info("Content-Length s: " + size);
                if (size > lastSize) {
                    lastSize = size;
                    Thread.sleep(1500);
                } else if (size == lastSize) {
                    Log.info("Content-Length is: " + size);
                    exit--;
                    Thread.sleep(1000);
                }
                if (exit < 0) {
                    Log.info("the video is ready!");
                    return url.openStream();
                }
            } catch (Exception | AssertionError e) {
                Log.error("There is a problem with getting video from the URL: " + url + ", see the stack traces: " + e);
            }
        }
        return null;
    }

    // TODO: update logger
    //  to generate logs for the each test in the separated file
    //  or re-write the existent file for each thread
    /**
     * This method attaches Log4j2 file from the 'log4j2' folder to the Cucumber JSON report
     *
     * @param scenario a current Cucumber scenario
     */
    public static void cucumberAttachLog4j(Scenario scenario) {
        try {
            byte[] fileData = FileUtils.readFileToByteArray(new File("log4j2/log4j2-test-automation.log"));
            scenario.attach(fileData, "text/plain", "Log4j logs");
        } catch (IOException e) {
            Log.error("The Log4j log file was not found in the 'log4j2' folder");
        }
    }

    /**
     * This method attaches screenshot of the current browser screen to the Cucumber JSON report
     *
     * @param attachName the name for the attached block with the screenshot in the report
     */
    public static void cucumberAttachScreenshot(String attachName) {
        byte[] data = ((TakesScreenshot) ThreadLocalDriver.get().getDriver()).getScreenshotAs(OutputType.BYTES);
        ThreadLocalScenario.get().attach(data, "image/png", attachName);
    }

    /**
     * This method accepts a byte array and attaches it to the Cucumber JSON report with the specified name
     *
     * @param attachName a name of the attachment
     * @param data       a byte array with video
     */
    public static void cucumberAttachVideo(Scenario scenario, String attachName, byte[] data) {
        if (data != null) {
            scenario.attach(data, "video/mp4", attachName);
        } else Log.error("Can't attache video file, specified byte array is null");
    }

    /**
     * This method attaches current browser logs to the Cucumber JSON report (currently supported: Chrome, FireFox, Edge)
     */
    public static void cucumberAttachBrowserLog(Scenario scenario, Investing investing) {
        scenario.attach(String.valueOf(investing
                .getDriver()
                .manage()
                .logs()
                .get(LogType.BROWSER).getAll()), "text/plain", "Browser console log:");
    }

    /**
     * This method simple copies the specified string value to the logs and return it back.
     * Main purpose of this method is to be used with Allure step methods to reflect the steps names
     * in the log4j logs as well.
     *
     * @param logLevel the level of logging from LogLevel enum
     * @param text     the text value
     * @return the same text value
     */
    public static String textWithCopyToLog(LogLevel logLevel, String text) {
        switch (logLevel) {
            case ERROR -> Log.error("Allure Step: " + text);
            case FATAL -> Log.fatal("Allure Step: " + text);
            case DEBUG -> Log.debug("Allure Step: " + text);
            default -> Log.info("Allure Step: " + text);
        }
        return text;
    }

    /**
     * This method checks the size of the Cucumber.json file and if it exceeds the specified value,
     * removes the attachments (all types) from the test scenarios until the size of the file will be < than
     * the specified value.
     *
     * @param reportSizeLimitMb the size limit for the Cucumber.json file (currently, it should be < 100 Mb)
     */
    public static void cucumberReportCheckSize(int reportSizeLimitMb) {
        Log.info("Checking Cucumber Report size...");
        try {
            File reportFile = new File("reports/Cucumber.json");

            if (reportFile.length() / (1024 * 1024) > reportSizeLimitMb) {
                JsonReader report = new JsonReader(new FileReader(reportFile));
                Gson gson = new GsonBuilder().setPrettyPrinting().create();

                JsonArray rootArray = gson.fromJson(report, JsonArray.class);

                int scenarioCounter = 1, testCounter;

                // the root array represents the feature level e.i. each element is a feature file
                outerLoop:
                for (JsonElement feature : rootArray) {
                    Log.info("feature: " + scenarioCounter++);
                    testCounter = 1;
                    // the 'elements' array represents the test scenarios
                    for (JsonElement scenario : feature.getAsJsonObject().get("elements").getAsJsonArray()) {
                        Log.info("scenario: " + testCounter++);
                        // in some cases the scenario doesn't have the 'after' section where the attachments reside
                        try {
                            for (JsonElement afterItem : scenario.getAsJsonObject().get("after").getAsJsonArray()) {
                                if (afterItem
                                        .getAsJsonObject()
                                        .has("embeddings")) {
                                    Log.info("attachments found on the scenario level, removing...");
                                    afterItem
                                            .getAsJsonObject()
                                            .remove("embeddings");
                                }
                            }
                        } catch (NullPointerException e) {
                            Log.info("scenario: " + testCounter + " has no 'after' section");
                        }
                        // if the attachment is a screenshot it will be in the 'steps' section too
                        for (JsonElement stepItem : scenario.getAsJsonObject().get("steps").getAsJsonArray()) {
                            for (JsonElement stepAfterItem : stepItem.getAsJsonObject().get("after").getAsJsonArray()) {
                                if (stepAfterItem
                                        .getAsJsonObject()
                                        .has("embeddings")) {
                                    Log.info("attachments found on the step level, removing...");
                                    stepAfterItem
                                            .getAsJsonObject()
                                            .remove("embeddings");
                                }
                            }
                        }
                        Writer writer = new FileWriter(reportFile);
                        gson.toJson(rootArray, writer);
                        writer.close();

                        if (reportFile.length() / (1024 * 1024) < reportSizeLimitMb)
                            break outerLoop;
                    }
                }
            } else Log.info("The Cucumber report doesn't exceed " + reportSizeLimitMb + "Mb");
        } catch (IOException e) {
            Log.error("There's a problem with processing the report file: " + e);
        }
    }

}

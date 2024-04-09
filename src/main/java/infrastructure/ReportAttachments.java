package infrastructure;


import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.stream.JsonReader;
import infrastructure.enums.LogLevel;
import infrastructure.logger.Log;

import java.io.*;

public class ReportAttachments {

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

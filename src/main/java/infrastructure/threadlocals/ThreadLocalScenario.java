package infrastructure.threadlocals;

import infrastructure.enums.JiraProject;
import infrastructure.logger.Log;
import infrastructure.threadlocals.abstracts.ThreadLocalAbstract;
import io.cucumber.java.Scenario;
import org.apache.commons.lang3.StringUtils;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Handles Cucumber Scenario instance
 */
public class ThreadLocalScenario {
    private static final ThreadLocalAbstract<Scenario> scenario = new ThreadLocalAbstract<>("Cucumber Scenario") {
        @Override
        public String toString() {
            Scenario value = getThreadLocal().get();

            String valueStr =
                    value == null
                            ? "undefined"
                            : "Scenario: " + value.getName() + ". Line: " + value.getLine();

            return String.format("%s as \"%s\"", this.getDescription().toUpperCase(), valueStr) ;
        }
    };

    public static void put(Scenario scenario) {
        ThreadLocalScenario.scenario.put(scenario);
    }

    public static Scenario get() {
        return scenario.get();
    }

    public static void clear() { scenario.clear(); }

    public static Collection<String> getTags() {
        return get().getSourceTagNames();
    }

    /**
     * @param tag tag to check, starts with '@' char
     * @return TRUE if Cucumber Scenario contains provided tag
     */
    public static boolean containsTag(String tag) {
        return getTags().contains(tag);
    }

    /**
     * @return ids of Jira tickets with bugs, provided in the Cucumber Scenario tags
     */
    public static List<String> getIssues() {
        String prefix = "@issue=";

        return getTags().stream()
                .filter(tag -> tag.startsWith(prefix))
                .map(tag -> StringUtils.removeStart(tag, prefix))
                .collect(Collectors.toList());
    }

    /**
     * @return the test ID from the corresponding tag (like '@CT-xxxx' or '@tmsLink=CT-xxxx').
     *         if such tag isn't exist this will return NULL
     */
    public static String getTestId() {
        List<String> patterns = Arrays.stream(JiraProject.values())
                .flatMap(jiraEnumValue -> Stream.of(
                        String.format("^@%s-\\d+$", jiraEnumValue),         // e.g. ^@CT-\d+$
                        String.format("^@tmsLink=%s-\\d+$", jiraEnumValue)  // e.g. ^@tmsLink=CT-\d+$
                ))
                .collect(Collectors.toList());

        String testId = getTags().stream()
                .filter(tag -> patterns.stream().anyMatch(tag::matches))
                .findAny()
                .map(tag -> StringUtils.removeStart(tag, "@"))
                .orElse(null);

        if (testId == null)
            Log.error("Couldn't detect Jira ticket ID for the test!");
        else
            testId = StringUtils.removeStart(testId, "tmsLink=");


        return testId;
    }

    /**
     * @return the name of the Scenario
     */
    public static String getName() {
        return get().getName();
    }

    /**
     * @return the line in the feature file of the Scenario. If this is a
     *         Scenario from Scenario Outlines this will return the line of the
     *         example row in the Scenario Outline.
     */
    public static int getLine() {
        return get().getLine();
    }
}
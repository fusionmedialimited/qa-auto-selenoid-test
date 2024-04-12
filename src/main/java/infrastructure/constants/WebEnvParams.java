package infrastructure.constants;

import java.util.Locale;

public class WebEnvParams {

    // TODO Need to remove it from infrastructure module, this class should be generic.
    private static final String tagValue = "latest";
    private static final String runValue = "local";
    private static final String browserValue = "chrome";
    private static final String noCashValue = "0";

    public static String getTagParam() {
        String tag;
        if (System.getProperty("tag") != null)
            tag = !System.getProperty("tag").isEmpty() ? System.getProperty("tag") : tagValue;
        else
            tag = tagValue;

        return tag;
    }

    public static String getRunParam() {
        String run;
        if (System.getProperty("run") != null)
            run = !System.getProperty("run").isEmpty() ? System.getProperty("run").toLowerCase(Locale.ROOT) : runValue;
        else
            run = runValue;

        return run;
    }

    public static String getBrowserParam() {
        String browser;
        if (System.getProperty("browser") != null)
            browser = !System.getProperty("browser").isEmpty() ? System.getProperty("browser").toLowerCase(Locale.ROOT)
                    : browserValue;
        else
            browser = browserValue;

        return browser;
    }

    public static String getNoCashParam() {
        String noCash;
        if (System.getProperty("no_cash") != null)
            noCash = !System.getProperty("no_cash").isEmpty() ? System.getProperty("no_cash") : noCashValue;
        else
            noCash = noCashValue;

        return noCash;
    }

    public static boolean getHeadlessParam() {
        String headless = System.getProperty("headless");
        return Boolean.parseBoolean(headless);
    }

}

package infrastructure.constants;

import infrastructure.enums.Edition;
import infrastructure.threadlocals.ThreadLocalEdition;
import org.apache.commons.lang3.EnumUtils;

import java.util.Locale;

import static infrastructure.constants.ConstantProvider.WebConstant.Page.CANARY_SUB_DOMAIN;

public class WebEnvParams {

    // TODO Need to remove it from infrastructure module, this class should be generic.
    private static final String tagValue = "latest";
    private static final String runValue = "local";
    private static final String browserValue = "chrome";
    private static final String editionValue = "www";
    private static final String urlValue = "investing.com";
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

    public static String getEditionParam() {
        String edition;
        if (System.getProperty("edition") != null)
            edition = !System.getProperty("edition").isEmpty() ? System.getProperty("edition").toLowerCase(Locale.ROOT)
                    : editionValue;
        else
            edition = editionValue;

        return edition;
    }

    public static Edition getEditionParamAsEnum() {
        return EnumUtils.getEnumIgnoreCase(Edition.class, getEditionParam());
    }

    public static String getUrlParam() {
        String url;
        if (System.getProperty("url") != null)
            url = !System.getProperty("url").isEmpty() ? System.getProperty("url").toLowerCase(Locale.ROOT) : urlValue;
        else
            url = urlValue;

        return url.endsWith("/") ? url.substring(0, url.length() - 1) : url ;
    }

    public static boolean isOnCanary() {
        return getUrlParam().contains(CANARY_SUB_DOMAIN);
    }

    public static String getHomeURL() {
        String homeURL = "https://";
        if (getBrowserParam().equalsIgnoreCase("chromemobile")) {
            homeURL += "m";
            if (!ThreadLocalEdition.get().equals(Edition.WWW))
                homeURL += "." + ThreadLocalEdition.get().toStringLowerCased();
        } else
            homeURL += ThreadLocalEdition.get().toStringLowerCased();

        homeURL += "." + WebEnvParams.getUrlParam();
        return homeURL;
    }

    public static String getNoCashParam() {
        String noCash;
        if (System.getProperty("no_cash") != null)
            noCash = !System.getProperty("no_cash").isEmpty() ? System.getProperty("no_cash") : noCashValue;
        else
            noCash = noCashValue;

        return noCash;
    }

    public static int getRetriesNumberParam() {
        String tries = System.getProperty("tries.count");
        return tries == null
                ? 1
                : Integer.parseInt(tries);
    }

    public static boolean getSelenoidLocalParam() {
        String selenoidLocal = System.getProperty("selenoid");
        return Boolean.parseBoolean(selenoidLocal);
    }

    public static boolean getHeadlessParam() {
        String headless = System.getProperty("headless");
        return Boolean.parseBoolean(headless);
    }

}

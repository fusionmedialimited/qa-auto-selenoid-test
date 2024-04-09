package infrastructure.utilities;

import infrastructure.Investing;
import infrastructure.constants.ConstantProvider;
import infrastructure.constants.WebEnvParams;
import infrastructure.enums.Edition;
import infrastructure.exceptions.InvestingException;
import infrastructure.threadlocals.ThreadLocalDriver;
import infrastructure.threadlocals.ThreadLocalEdition;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.apache.commons.lang3.StringUtils;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.TimeoutException;

import java.lang.reflect.Field;
import java.util.ArrayList;

import static infrastructure.ReportAttachments.textWithCopyToLog;
import static infrastructure.allure.AllureAttachments.allureAttachText;
import static infrastructure.constants.ConstantProvider.WebConstant.Page.HOME_NO_EDITION_URL;
import static infrastructure.constants.ConstantProvider.WebConstant.TimeoutDuration.*;
import static infrastructure.constants.WebEnvParams.getNoCashParam;
import static infrastructure.enums.LogLevel.INFO;
import static infrastructure.utilities.WaitUtilities.customWait;
import static infrastructure.utilities.WaitUtilities.waitUntil;
import static io.qameta.allure.Allure.step;

public class NavigationUtilities {

    /**
     * Checks if page is constant from Pages interface.
     * Returns value of corresponding constant if it is so.
     * Else returns the same page
     */
    public static String extractPageFromConst(String text) {
        if (text.isEmpty())
            return text;

        try {
            Field field = ReflectionUtils.getStaticFields( ConstantProvider.WebConstant.Page.class, String.class).stream()
                    .filter(f -> f.getName().equals(text))
                    .findFirst()
                    .orElse(null);

            return field == null
                    ? text
                    : (String) field.get(null);
        } catch (Exception cause) {
            throw new InvestingException("Couldn't get page from constant!", cause);
        }
    }

    /**
     * Open given url
     *
     * @param driver : driver object
     * @param url    : any valid URL
     */
    public static void goToURL(Investing driver, String url) {
        step(textWithCopyToLog(INFO, "Navigating to the " + url + " url"), () -> {
            try {
                if (getNoCashParam().equals("0"))
                    driver.get(url);
                else
                    driver.get(url.concat(NO_CASH_PARAM.concat(getNoCashParam())));
            } catch (Exception cause) {
                throw new InvestingException("Couldn't open the " + url + " url!", cause);
            }
        });
    }

    /**
     * Open given page
     *
     * @param driver          driver object
     * @param pageOrConstName page or constant from Pages interface
     */
    public static void goToPage(Investing driver, String pageOrConstName) {
        String page = extractPageFromConst(pageOrConstName);
        String url = WebEnvParams.getHomeURL().concat(page);

        step(textWithCopyToLog(INFO, "Navigating to the ".concat(page).concat(" page")), () -> goToURL(driver, url));
    }

    /**
     * Navigates to the specific page uri + desired edition
     *
     * @param driver  : driver object
     * @param edition : language edition
     * @param pageOrConstName page or constant from Pages interface
     */
    public static void goToPage(Investing driver, String pageOrConstName, Edition edition) {
        String page = extractPageFromConst(pageOrConstName);
        String url = "https://"
                .concat(edition.toString().toLowerCase())
                .concat(".")
                .concat(HOME_NO_EDITION_URL).concat(page);

        ThreadLocalEdition.set(edition);
        goToURL(driver, url);
    }

    /**
     * Navigates to the specific page uri + desired edition
     *
     * @param driver  : driver object
     * @param edition : language edition
     * @param pageOrConstName page or constant from Pages interface
     */
    public static void goToPage(Investing driver, String pageOrConstName, Edition edition, boolean noDynamicContent) {
        String page = extractPageFromConst(pageOrConstName);
        String noDynamicContentFlag = noDynamicContent ? "?disable_realtime=true" : "";

        String url = "https://"
                .concat(edition.toString().toLowerCase())
                .concat(".")
                .concat(HOME_NO_EDITION_URL).concat(page)
                .concat(noDynamicContentFlag);

        ThreadLocalEdition.set(edition);
        goToURL(driver, url);
    }

    public static void goToMainPage(Investing driver) {
        goToPage(driver, "");
    }

    /** This method opens the specified page and returns its load time in seconds
     *
     * @param driver      web driver
     * @param pageOrConst page or constant from Pages interface
     * @return page load time as long
     */
    public static long goToPageLoadingCount(Investing driver, String pageOrConst) {
        String page = extractPageFromConst(pageOrConst);
        String url = WebEnvParams.getHomeURL().concat(page);

        try {
            driver.get(url);

            long loadEventEnd = getPageLoadEventValue(driver, PerformanceEvent.LOAD_EVENT_END);
            long navigationStart = getPageLoadEventValue(driver, PerformanceEvent.NAVIGATION_START);

            return (loadEventEnd - navigationStart) / 1000;

        } catch (Exception e) {
            allureAttachText("error", String.format("Couldn't get selected URL %s", url));
            throw new InvestingException(String.format("[ERROR] Couldn't get selected URL %s", url), e);
        }
    }

    public static boolean isPagePro(String page) {
        return StringUtils.startsWith(extractPageFromConst(page),PRO_PAGE + "/");
    }

    /**
     * Extract page without domain from the provided URL
     */
    public static String getPageFromUrl(String url) {
        if (!url.contains(HOME_NO_EDITION_URL)) {
            throw new IllegalArgumentException(
                    String.format("Couldn't get page, because URL \"%s\" doesn't contain domain \"%s\"!", url, HOME_NO_EDITION_URL));
        }

        String currentUrl = StringUtils.removeEnd(url, "/");
        String domainPattern =  "^.*".concat(HOME_NO_EDITION_URL.replace(".", "\\."));

        return  currentUrl.replaceAll(domainPattern,"");
    }

    /**
     * Get current page without domain
     */
    public static String getCurrentPage() {
        String url = ThreadLocalDriver.get().getCurrentUrl();
        return getPageFromUrl(url);
    }

    /**
     * Switching between tabs
     *
     * @param tabNumber tab number
     */
    public static void switchTab(int tabNumber) {
        Investing investing = ThreadLocalDriver.get();

        try {
            waitUntil(webDriver ->
                    webDriver.getWindowHandles().size() > tabNumber);

            ArrayList<String> tabs = new ArrayList<>(investing.getWindowHandles());
            investing.switchTo().window(tabs.get(tabNumber));

            Utilities.sleep(GO_TO_URL_DURATION);
        } catch (Exception cause) {
            throw new InvestingException("[ERROR] Couldn't switch tab!", cause);
        }
    }

    /**
     * Switching to the latest opened tab
     */
    public static void switchTabToNew() {
        Investing investing = ThreadLocalDriver.get();
        int tabsNumber = investing.getWindowHandles().size();

        try {
            waitUntil(LONG_CLICK_DURATION, webDriver ->
                    webDriver.getWindowHandles().size() == tabsNumber + 1);
        } catch (TimeoutException ignore) {}

        int index = investing.getWindowHandles().size() - 1;
        switchTab(index);
    }

    /** This method returns a current page rendering time in milliseconds
     *
     * @param driver web driver
     * @return rendering time in milliseconds
     */
    private static long getRenderingTime(Investing driver) {
        JavascriptExecutor js = (JavascriptExecutor) driver.getDriver();

        long start = System.currentTimeMillis();
        js.executeScript("window.performance.timing.loadEventEnd");
        long end = System.currentTimeMillis();

        return end - start;
    }


    @AllArgsConstructor @Getter
    public enum PerformanceEvent {
        LOAD_EVENT_END ("loadEventEnd"),
        NAVIGATION_START ("navigationStart");
        private final String event;
    }

    /** This method returns time values for the PerformanceEvents acquired via JS code execution
     *
     * @param driver web driver
     * @param event PerformanceEvent enum value
     * @return time value as Long
     */
    public static long getPageLoadEventValue(Investing driver, PerformanceEvent event) {
        JavascriptExecutor js = (JavascriptExecutor) driver.getDriver();
        Object value = js.executeScript(String.format("return window.performance.timing.%s;", event.getEvent()));

        if (value instanceof Long)
            return (Long) value;
        else throw new InvestingException("The 'return window.performance.timing.".concat(event.getEvent())
                .concat("' script return an unexpected value of not a Long type"));
    }

    public static void goToPreviousPage() {
        try {
          ThreadLocalDriver.get().navigate().back();
          customWait(GO_TO_URL_DURATION);
        } catch (Exception cause) {
            throw new InvestingException("Unable to go to the previous page, see traces: ", cause);
        }
    }

}

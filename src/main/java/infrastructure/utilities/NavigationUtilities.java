package infrastructure.utilities;

import infrastructure.Investing;
import infrastructure.constants.ConstantProvider;
import infrastructure.enums.Edition;
import infrastructure.exceptions.InvestingException;

import java.lang.reflect.Field;

import static infrastructure.ReportAttachments.textWithCopyToLog;
import static infrastructure.constants.ConstantProvider.WebConstant.Page.HOME_NO_EDITION_URL;
import static infrastructure.enums.LogLevel.INFO;
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
                    driver.get(url);
            } catch (Exception cause) {
                throw new InvestingException("Couldn't open the " + url + " url!", cause);
            }
        });
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

        goToURL(driver, url);
    }


}

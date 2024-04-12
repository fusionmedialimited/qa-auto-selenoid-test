package infrastructure.utilities;

import infrastructure.Investing;
import infrastructure.enums.Edition;
import infrastructure.exceptions.InvestingException;

import static infrastructure.ReportAttachments.textWithCopyToLog;
import static infrastructure.constants.ConstantProvider.WebConstant.Page.HOME_NO_EDITION_URL;
import static infrastructure.enums.LogLevel.INFO;
import static io.qameta.allure.Allure.step;

public class NavigationUtilities {

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
     * @param page    : page
     */
    public static void goToPage(Investing driver, String page, Edition edition) {
        String url = "https://"
                .concat(edition.toString().toLowerCase())
                .concat(".")
                .concat(HOME_NO_EDITION_URL).concat(page);

        goToURL(driver, url);
    }


}

package infrastructure.listeners.webriver;

import infrastructure.listeners.webriver.helpers.WDListenerBaseHelper;
import infrastructure.listeners.webriver.helpers.WDListenerCookieHelper;
import infrastructure.listeners.webriver.helpers.WDListenerPopupHelper;
import infrastructure.logger.Log;
import infrastructure.threadlocals.ThreadLocalDriver;
import infrastructure.threadlocals.ThreadLocalPopups;
import infrastructure.utilities.NavigationUtilities;
import infrastructure.utilities.Utilities;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Sequence;
import org.openqa.selenium.support.events.WebDriverListener;

import java.util.Collection;

import static infrastructure.allure.AllureAttachments.allureAttachText;
import static infrastructure.constants.ConstantProvider.WebConstant.TimeoutDuration.GO_TO_URL_DURATION;
import static infrastructure.utilities.NavigationUtilities.isPagePro;

/**
 * Expected that current common listener will be stable and used for each regular test run.
 * This listener is used by default for each test run now
 */
public class CommonWebDriveListener implements WebDriverListener {

    @Override
    public void afterGet(WebDriver driver, String url) {
        Log.info("afterGet event detected");

        try {
            Utilities.sleep(GO_TO_URL_DURATION);
            String page = NavigationUtilities.getPageFromUrl(url);

            if (isPagePro(page))
                return;
            else {
                // Privacy / CCPA popup processing
                WDListenerPopupHelper.detectAndClosePrivacyCcpaPopup(url);
                // prevent InvestingPro promo banner
//                WDListenerCookieHelper.modifyProPromoPopupCookie();
                // prevent InvestingPro Sale banner
//                WDListenerCookieHelper.modifyProSalePromoPopupCookies(page);
                // prevent ProPicks banner
//                WDListenerCookieHelper.modifyProPicksPromoPopupCookies(page);
                // prevent ProTips banner
//                WDListenerCookieHelper.addPromoBannerAutoCookie();

//                if (WDListenerCookieHelper.cookiesModifiedFlag.get()) {
                    // refresh page if any cookie for any banner was modified
//                    ThreadLocalDriver.get().navigate().refresh();
//                    WDListenerCookieHelper.cookiesModifiedFlag.set(false);
//                }
            }
        } catch (Exception cause) {
            allureAttachText("error","Couldn't complete afterGet steps with WebDriver listener:\n" + cause);
        }
    }

    @Override
    public void afterRefresh(WebDriver.Navigation navigation) {
        Log.info("afterRefresh event detected");
        Utilities.sleep(GO_TO_URL_DURATION);

        // Privacy / CCPA popup processing
        if (!ThreadLocalPopups.getPrivacyCcpaPopupShownFlag()) {
            String url = ThreadLocalDriver.get().getCurrentUrl();
            WDListenerPopupHelper.detectAndClosePrivacyCcpaPopup(url);
        }
    }

    public void afterPerform(WebDriver driver, Collection<Sequence> actions) {
        Log.info("afterPerform event detected");
        WDListenerBaseHelper.closeBannersAfterScrollOrClick();
    }

    public void afterExecuteScript(WebDriver driver, String script, Object[] args, Object result) {
        Log.info("afterExecuteScript event detected");
        WDListenerBaseHelper.closeBannersAfterScrollOrClick();
    }

    public void afterClick(WebElement element) {
        Log.info("afterClick event detected");
        WDListenerBaseHelper.closeBannersAfterScrollOrClick();
    }


    @Override
    public void afterQuit(WebDriver driver) {
        Log.info("afterQuit event detected");

        try {
            ThreadLocalDriver.clear();
        } catch (Exception cause) {
            allureAttachText("error","Couldn't complete afterQuit steps with WebDriver listener:\n" + cause);
        }
    }
}

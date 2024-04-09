package infrastructure.listeners.webriver.helpers;

import infrastructure.constants.ConstantProvider;
import infrastructure.threadlocals.ThreadLocalCookies;
import infrastructure.threadlocals.ThreadLocalDriver;
import infrastructure.utilities.BaseUtilities;
import org.openqa.selenium.Cookie;

import java.util.Arrays;
import java.util.stream.Collectors;

import static infrastructure.allure.AllureAttachments.allureAttachText;
import static infrastructure.constants.ConstantProvider.WebConstant.Cookies.*;
import static infrastructure.listeners.webriver.helpers.WDListenerBaseHelper.isPageRelevantForProPicksBanner;
import static infrastructure.listeners.webriver.helpers.WDListenerBaseHelper.isPageRelevantForProSaleBanner;
import static io.qameta.allure.Allure.step;

public class WDListenerCookieHelper {

    public static final ThreadLocal<Boolean> cookiesModifiedFlag = ThreadLocal.withInitial(() -> false);

    private static void modifyCookies(Cookie... cookies) {
        Arrays.stream(cookies)
                        .forEach(cookie -> step("Cookies are modified!", () -> {
                            ThreadLocalDriver.get().manage().deleteCookieNamed(cookie.getName());
                            ThreadLocalDriver.get().manage().addCookie(cookie);
                            allureAttachText(cookie.getName() + " cookie modified", "Value changed to " + cookie.getValue());
                        }));

        cookiesModifiedFlag.set(true);
    }

    public static void logCookies() {
        String cookiesOutput = ThreadLocalDriver.get()
                .manage()
                .getCookies()
                .stream()
                .map((cookie) -> cookie.getName() + ": " + cookie.getValue())
                .collect(Collectors.toList())
                .toString()
                .replace(",", ",\n");

        allureAttachText("Current cookies values", cookiesOutput);
    }

    /**
     * <b>invpro_promote_variant</b> cookie should be set as <b>0</b>
     * to prevent Pro Promo popup displaying
     */
    public static synchronized void modifyProPromoPopupCookie() {
        if (!ThreadLocalCookies.getInvProPromoteVariantCookieModifiedFlag())
            if (BaseUtilities.isCookiePresented(invProPromoteVariantCookie)) {
                modifyCookies(invProPromoteVariantCookie);
                ThreadLocalCookies.putInvProPromoteVariantCookieModifiedFlag();
            }
    }

    /**
     * The following logic and cookies controls to prevent popup appearance. <br>
     *
     * Every 2 days, limited to 3 times in total if user dismissed it.
     * Could be controlled by the following cookies:
     * <ul>
     *     <li>
     *         event_popup_counter: (number): Increased by 1 each time the user see the popup
     *     </li>
     *     <li>
     *         event_popup_did_user_dismissed: (0,1): By default is 0, Changed to 1 if the user dismissed the popup
     *     </li>
     *     <li>
     *         event_popup_last_show: (date): When the last time the user saw the popup
     *     </li>
     * </ul>
     *
     * For new promotion this logic will be restarted.
     * Dismissal is either when a user click on the X button OR clicks on the site background.
     */
    public static synchronized void modifyProSalePromoPopupCookies(String page) {
        if (!ThreadLocalCookies.getEventPopupCookiesModifiedFlag())
            if (isPageRelevantForProSaleBanner.test(page))
                if (BaseUtilities.isCookiePresented(eventPopupCounterCookie)) {
                    modifyCookies(eventPopupCounterCookie, eventPopupDidUserDismissedCookie, eventPopupLastShownCookie);
                    ThreadLocalCookies.putEventPopupCookiesModifiedFlag();
                }
    }

    /**
     * The following logic and cookies controls to prevent popup appearance. <br>
     *
     * Every 4 days, limited to 3 times in total if user dismissed it.
     * Could be controlled by the following cookies:
     * <ul>
     *     <li>
     *         propicks_popup_counter: (number): Increased by 1 each time the user see the popup
     *     </li>
     *     <li>
     *         propicks_popup_did_user_dismissed: (0,1): By default is 0, Changed to 1 if the user dismissed the popup
     *     </li>
     *     <li>
     *         propicks_popup_last_shows: (date): When the last time the user saw the popup
     *     </li>
     *     <li>
     *         propicks_popup_user_clicked (0,1): By default is 0, Changed to 1 if the user clicked on the popup
     *     </li>
     * </ul>
     *
     * For new promotion this logic will be restarted.
     * Dismissal is either when a user click on the X button OR clicks on the site background.
     */
    public static synchronized void modifyProPicksPromoPopupCookies(String page) {
        if (!ThreadLocalCookies.getProPicksPopupCookiesModifiedFlag()) {
            if (isPageRelevantForProPicksBanner.test(page))
                if (BaseUtilities.isCookiePresented(propicksPopupCounterCookie)) {
                    modifyCookies(propicksPopupCounterCookie, propicksPopupDidUserDismissedCookie, propicksPopupLastShownCookie, propicksPopupUserClickedCookie);
                    ThreadLocalCookies.putProPicksPopupCookiesModifiedFlag();
                }
        }
    }

    /**
     * Expected, that presence of {@link ConstantProvider.WebConstant.Cookies#promoBannerAutoCookie} cookie
     * prevents all new promo banners. <br><br>
     *
     * Now it works for the ProTips Promo popup
     */
    public static synchronized void addPromoBannerAutoCookie() {
        if (!ThreadLocalCookies.getPromoBannerAutoCookieAddedFlag()) {
            ThreadLocalDriver.get().manage().addCookie(promoBannerAutoCookie);
            allureAttachText(promoBannerAutoCookie.getName() + " cookie", "Cookie added!");
            ThreadLocalCookies.putPromoBannerAutoCookieAddedFlag();
            cookiesModifiedFlag.set(true);
        }
    }
}

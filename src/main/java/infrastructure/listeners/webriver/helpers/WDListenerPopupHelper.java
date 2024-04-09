package infrastructure.listeners.webriver.helpers;

import infrastructure.constants.WebEnvParams;
import infrastructure.threadlocals.ThreadLocalPopups;
import infrastructure.utilities.NavigationUtilities;
import org.openqa.selenium.TimeoutException;

import static infrastructure.constants.ConstantProvider.WebConstant.TimeoutDuration.MINIMAL_WAITING_DURATION;
import static infrastructure.constants.ConstantProvider.WebConstant.TimeoutDuration.POPUP_WAITING_DURATION;
import static infrastructure.constants.GlobalLocators.WebGlobalLocators.PopupLocators.ccpaPopUp;
import static infrastructure.constants.GlobalLocators.WebGlobalLocators.PopupLocators.privacyPopUp;
import static infrastructure.listeners.webriver.helpers.WDListenerBaseHelper.*;
import static infrastructure.utilities.PopupUtilities.*;
import static infrastructure.utilities.WaitUtilities.waitUntil;
import static org.openqa.selenium.support.ui.ExpectedConditions.or;
import static org.openqa.selenium.support.ui.ExpectedConditions.visibilityOfElementLocated;

public class WDListenerPopupHelper {

    /**
     * Privacy and CCPA have the same goal. <br>
     * <ul>
     *     <li>
     *         If user's location is in Europe - then Privacy popup should be displayed.
     *     </li>
     *     <li>
     *         If user's location is Brazil or US (BR, US_NV, US_CA, US_VA, US_CT) - then CCPA popup should be displayed.
     *     </li>
     * </ul>
     *
     * Expected, that one of these popups should be displayed one the 1st opened Investing.com page.
     * If this condition wasn't completed, then current user's location doesn't require such popup.
     */
    public static synchronized void detectAndClosePrivacyCcpaPopup(String url) {
        if (!ThreadLocalPopups.getPrivacyCcpaPopupShownFlag())
            if (isPageRelevantForPrivacyOrCcpaPopup.test(url))
                try {
                    waitUntil(
                            POPUP_WAITING_DURATION,
                            or(
                                    visibilityOfElementLocated(privacyPopUp),
                                    visibilityOfElementLocated(ccpaPopUp)
                            ));
                    if (isPopupDisplayed(MINIMAL_WAITING_DURATION, privacyPopUp))
                        closePrivacyPopUp();
                    else
                        closeCCPAPopUp();
                } catch (TimeoutException ignore) {
                } finally {
                    ThreadLocalPopups.putPrivacyCcpaPopupClosedFlag();
                }
    }

    /**
     * Sign Up promo popup appears when user’s mouse cursor escape from the page focus
     */
    public static synchronized void detectAndCloseSignUpPromoPopup() {
        if (!ThreadLocalPopups.getSignUpPromoPopupShownFlag())
            if (isPageRelevantForSignUpPopup.get())
                    if (closePromotionSignUp())
                        ThreadLocalPopups.putSignUpPromoPopupShownFlag();
    }

    public static synchronized void detectAndCloseProPromoPopup() {
        if (!ThreadLocalPopups.getProPromoPopupShownFlag())
            if (closeProPromoPopup())
                ThreadLocalPopups.putSignUpPromoPopupShownFlag();
    }

    public static synchronized void detectAndCloseSalePromoPopup(String page) {
        if (!ThreadLocalPopups.getProSalePromoPopupShownFlag())
            if (isPageRelevantForProSaleBanner.test(page))
                    if (closeProPromoPopup())
                        ThreadLocalPopups.putProSalePromoPopupShownFlag();
    }

    public static synchronized void detectAndCloseProPicksPromoPopup(String page) {
        if (!ThreadLocalPopups.getProPicksPromoPopupShownFlag()) {
            if (isPageRelevantForProPicksBanner.test(page))
                if (closeProPromoPopup())
                    ThreadLocalPopups.putProPicksPromoPopupShownFlag();
        }
    }

    public static synchronized void detectAndCloseProPicksPromoPopupOnCanary() {
        // cookies for the ProPicks banner can't be loaded on Canary
        if (WebEnvParams.isOnCanary())
            if (!ThreadLocalPopups.getProPicksPromoPopupShownFlag()) {
                String page = NavigationUtilities.getCurrentPage();
                // ProPicks banner processing
                WDListenerPopupHelper.detectAndCloseProPicksPromoPopup(page);
        }
    }

    public static synchronized void detectAndCloseProTipsPromoPopup(String page) {
        if (!ThreadLocalPopups.getProTipsPromoPopupShownFlag()) {
            if (isPageRelevantForProTipsBanner.test(page)) {
                if (closePopupWithIframe())
                    ThreadLocalPopups.putProTipsPromoPopupShownFlag();
            }
        }
    }

    public static synchronized void detectAndCloseProMarchSalePromoPopup() {
        if (!ThreadLocalPopups.getProMarchSalePromoPopupShownFlag()) {
            String page = NavigationUtilities.getCurrentPage();
            if (isPageRelevantForProMarchSaleBanner.test(page))
                if (closePopupWithIframe())
                    ThreadLocalPopups.putProMarchSalePromoPopupShownFlag();
        }
    }
}

package infrastructure.utilities;

import infrastructure.exceptions.InvestingException;
import infrastructure.threadlocals.ThreadLocalDriver;
import org.apache.commons.lang3.StringUtils;
import org.openqa.selenium.*;
import org.openqa.selenium.support.ui.ExpectedConditions;

import java.time.Duration;

import static infrastructure.ReportAttachments.textWithCopyToLog;
import static infrastructure.allure.AllureAttachments.allureAttachText;
import static infrastructure.constants.ConstantProvider.WebConstant.TimeoutDuration.*;
import static infrastructure.constants.GlobalLocators.WebGlobalLocators.PopupLocators.*;
import static infrastructure.constants.GlobalLocators.WebGlobalLocators.WorkstationLocators.watchlistOnboardingPanel;
import static infrastructure.enums.LogLevel.INFO;
import static infrastructure.utilities.BaseUtilities.*;
import static infrastructure.utilities.WaitUtilities.*;
import static io.qameta.allure.Allure.step;

public class PopupUtilities {

    public static boolean isPopupDisplayed(Duration duration, By popup) {
        try {
            waitForVisibility(duration, popup);
            return true;
        } catch (TimeoutException | NoSuchElementException exception) {
            return false;
        }
    }

    /**
     * close any Pop-up on page init
     *
     * @param popup       expected popup
     * @param closeButton button, which closes expected popup
     *
     * @return true means popup was closed
     */
    public static boolean closePopup(Duration duration, By popup, By closeButton) {
        try {
            if (isPopupDisplayed(duration, popup)) {
                BaseUtilities.removeElementFromDOM(watchlistOnboardingPanel, MINIMAL_WAITING_DURATION);
                clickOnVisibleElement(closeButton);

                waitForInvisibility(ELEMENT_WAITING_DURATION_SMALL, popup);

                allureAttachText("info", "Pop-up was closed: " + popup.toString());
                return true;
            } else {
                allureAttachText("info", "Pop-up was not displayed: " + popup.toString());
            }
        } catch (InvestingException | TimeoutException | NoSuchElementException | StaleElementReferenceException ignore) {
        }

        return false;
    }

    public static boolean closePopup(Duration duration, By popup, By checkbox, By closeButton) {
        try {
            waitForVisibility(duration, popup);

            clickOnVisibleElement(checkbox);

            Utilities.sleep(LONG_CLICK_DURATION);

            clickOnElement(
                    waitUntil(LONG_CLICK_DURATION, ExpectedConditions.elementToBeClickable(closeButton))
            );

            waitForInvisibility(LONG_CLICK_DURATION, popup);

            allureAttachText("info", "Pop-up was closed: " + popup.toString());
            return true;
        } catch (InvestingException | TimeoutException | NoSuchElementException ignore) {
            allureAttachText("info", "Pop-up was not displayed: " + popup.toString());
        }

        return false;
    }

    /**
     * close the sign-up promotion on page init
     */
    public static boolean closePromotionSignUp() {
        return step(textWithCopyToLog(INFO, "Closing SignUp pop-up"), () ->
                closePopup(ELEMENT_WAITING_DURATION_SMALL, promotionPopUp, promotionPopUpCloseBtn)
        );
    }

    /**
     * close the Privacy Pop-up on page init (happens only for European IPs)
     */
    public static void closePrivacyPopUp() {
        step(textWithCopyToLog(INFO, "Closing Privacy pop-up"), () ->
                closePopup(POPUP_WAITING_DURATION, privacyPopUp, privacyPopUpAcceptBtn)
        );
    }

    /**
     * close the CCPA Pop-up on page init (happens only for American IPs)
     */
    public static void closeCCPAPopUp() {
        step(textWithCopyToLog(INFO, "Closing CCPA pop-up"), () ->
            closePopup(POPUP_WAITING_DURATION, ccpaPopUp, ccpaPopUpCloseBtn)
        );
    }

    /**
     * close the notification request permission Pop-up on page init
     */
    public static void closeNotificationPopup() {
        step(textWithCopyToLog(INFO, "Closing Notification pop-up"), () ->
                closePopup(POPUP_WAITING_DURATION, notificationPopUp, notificationPopUpBtn)
        );
    }

    public static boolean isProPromoPopupDisplayed() {
        if (isPopupDisplayed(ELEMENT_WAITING_DURATION_SMALL, proPromoPopup)) {
            String text = BaseUtilities.getElement(proPromoPopup).getText();
            return StringUtils.containsIgnoreCase(text, "Start my free trial");
        }
        return false;
    }

    public static boolean isProSalePromoPopupDisplayed() {
        if (isPopupDisplayed(ELEMENT_WAITING_DURATION_SMALL, proPromoPopup)) {
            String text = BaseUtilities.getElement(proPromoPopup).getText();
            return StringUtils.containsIgnoreCase(text, "InvestingPro")
                    && StringUtils.containsIgnoreCase(text, "Sale");
        }
        return false;
    }

    public static boolean isProPicksPopupDisplayed() {
        if (isPopupDisplayed(ELEMENT_WAITING_DURATION_SMALL, proPromoPopup)) {
            try {
                String text = BaseUtilities.getChildElement(BaseUtilities.getElement(proPromoPopup), By.tagName("img")).getAttribute("src");
                return !text.isBlank()
                        && StringUtils.containsIgnoreCase(text, "propicks");
            } catch (NoSuchElementException ignore) {}
        }

        return false;
    }

    /**
     * close one of the InvestingPro Promo Pop-ups:
     * - Black Friday Sale
     * - ProPicks
     */
    public static boolean closeProPromoPopup() {
        return step(textWithCopyToLog(INFO, "Closing InvestingPro Promo Pop-up"), () ->
                closePopup(ELEMENT_WAITING_DURATION_SMALL, proPromoPopup, proPromoPopupCloseBtn)
        );
    }

    /**
     * close Promo Pop-up with iframe inside
     */
    public static boolean closePopupWithIframe() {
        return step(textWithCopyToLog(INFO, "Closing Pop-up with iframe inside"), () -> {
            WebElement popup;
            try {
                try {
                    popup = waitForVisibility(ELEMENT_WAITING_DURATION_MEDIUM, popupWithIframe);
                } catch (TimeoutException e) {
                    allureAttachText("Pop-up", "Pop-up with iframe inside was not displayed");
                    return false;
                }

                WebElement iframe = BaseUtilities.getChildElement(popup, iframeSubElement);
                ThreadLocalDriver.get().switchTo().frame(iframe);

                clickOnVisibleElement(popupWithIframeCloseBtn);
                ThreadLocalDriver.get().switchTo().defaultContent();

                waitForInvisibility(ELEMENT_WAITING_DURATION_SMALL, popupWithIframe);
                allureAttachText("info", "ProTips Promo Pop-up was closed");

                return true;
            } catch (Exception cause) {
                throw new InvestingException("Couldn't close Pop-up with iframe@", cause);
            }
        });
    }

    /**
     * This method closes One Trusted banner. Required for Canary environment with EU IPs only
     */
    public static void closeOneTrustedBanner() {
        step(textWithCopyToLog(INFO, "Closing one trusted banner"), () ->
                closePopup(POPUP_WAITING_DURATION, oneTrustBanner, oneTrustBannerAcceptBtn)
        );
    }
}

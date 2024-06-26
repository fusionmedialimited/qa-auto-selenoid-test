package infrastructure.utilities;

import infrastructure.exceptions.InvestingException;
import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.StaleElementReferenceException;
import org.openqa.selenium.TimeoutException;

import java.time.Duration;

import static infrastructure.ReportAttachments.textWithCopyToLog;
import static infrastructure.allure.AllureAttachments.allureAttachText;
import static infrastructure.constants.ConstantProvider.WebConstant.TimeoutDuration.*;
import static infrastructure.constants.GlobalLocators.WebGlobalLocators.PopupLocators.*;
import static infrastructure.constants.GlobalLocators.WebGlobalLocators.WorkstationLocators.watchlistOnboardingPanel;
import static infrastructure.enums.LogLevel.INFO;
import static infrastructure.utilities.BaseUtilities.clickOnVisibleElement;
import static infrastructure.utilities.WaitUtilities.waitForInvisibility;
import static infrastructure.utilities.WaitUtilities.waitForVisibility;
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
     * close one of the InvestingPro Promo Pop-ups:
     * - Black Friday Sale
     * - ProPicks
     */
    public static boolean closeProPromoPopup() {
        return step(textWithCopyToLog(INFO, "Closing InvestingPro Promo Pop-up"), () ->
                closePopup(ELEMENT_WAITING_DURATION_SMALL, proPromoPopup, proPromoPopupCloseBtn)
        );
    }
}

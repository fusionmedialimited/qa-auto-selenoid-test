package infrastructure.constants;

import org.openqa.selenium.By;
import org.openqa.selenium.support.pagefactory.ByAll;
import org.openqa.selenium.support.pagefactory.ByChained;

import static infrastructure.constants.IconsProvider.xCommon2;

public class GlobalLocators {

    public static final class WebGlobalLocators {

        /**
         * Private Constructor
         */
        private WebGlobalLocators() {
            throw new AssertionError();
        }

        public static final class PopupLocators {
            public static final By promotionPopUp = By.id("PromoteSignUpPopUp");
            public static final By promotionPopUpCloseBtn = By.cssSelector("i.popupCloseIcon");
            public static final By privacyPopUp = By.id("onetrust-banner-sdk");
            public static final By ccpaPopUp = By.id("onetrust-close-btn-container");
            public static final By ccpaPopUpCloseBtn = By.cssSelector("button.ot-close-icon");
            public static final By privacyPopUpAcceptBtn = By.id("onetrust-accept-btn-handler");
            public static final By notificationPopUp = By.cssSelector("div.allow-notifications-popup");
            public static final By notificationPopUpBtn = By.cssSelector("button.allow-notifications-popup-close-button");
            public static final By oneTrustBanner = By.cssSelector("#onetrust-banner-sdk");
            public static final By oneTrustBannerAcceptBtn = By.cssSelector("#onetrust-accept-btn-handler");
            public static final By proPromoPopup = By.cssSelector("[class *= \"invProPromote\"]");
            public static final By proPromoPopupCloseBtn = xCommon2.buildLocator(false, false);
            public static final By popupWithIframe = By.cssSelector("div[role = \"dialog\"][aria-label = \"Modal Overlay Box\"]");
            public static final By iframeSubElement = By.cssSelector("iframe[aria-label = \"Modal Overlay Box Frame\"]");
            public static final By popupWithIframeCloseBtn = By.cssSelector("button[aria-label = \"Close Modal\"]");  // is located inside iframe!
        }

        public static final class WorkstationLocators {
            public static final By watchlistOnboardingPanel = By.xpath("//div[@class = \"relative\" and .//*[@data-test = \"watchlist-onboarding-drawer\"]]");
        }
    }
}
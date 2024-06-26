package infrastructure.constants;

import org.openqa.selenium.By;

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
            public static final By privacyPopUp = By.id("onetrust-banner-sdk");
            public static final By ccpaPopUp = By.id("onetrust-close-btn-container");
            public static final By ccpaPopUpCloseBtn = By.cssSelector("button.ot-close-icon");
            public static final By privacyPopUpAcceptBtn = By.id("onetrust-accept-btn-handler");
            public static final By proPromoPopup = By.cssSelector("[class *= \"invProPromote\"]");
            public static final By proPromoPopupCloseBtn = xCommon2.buildLocator(false, false);
        }

        public static final class WorkstationLocators {
            public static final By watchlistOnboardingPanel = By.xpath("//div[@class = \"relative\" and .//*[@data-test = \"watchlist-onboarding-drawer\"]]");
        }
    }
}
package infrastructure.constants;

import infrastructure.utilities.DateTimeUtilities;
import org.openqa.selenium.Cookie;

import java.time.Duration;

public class ConstantProvider {

    /**
     * Web constants provider class
     */
    public static final class WebConstant {

        /**
         * Private Constructor
         */
        private WebConstant() {
            throw new AssertionError();
        }

        public static final class Page {
            public static final String CANARY_SUB_DOMAIN = "de-canary-gcp";
            public static final String EQUITIES_PAGE = "/equities";
            public static final String HOME_NO_EDITION_URL = "investing.com";
            public static final String INDICES_PAGE = "/indices";
            public static final String INDICES_FUTURES_PAGE = INDICES_PAGE + "/indices-futures";
            public static final String INDICES_MAJOR_PAGE = INDICES_PAGE + "/major-indices";
            public static final String PRO_PAGE = "/pro";

            public static final String NO_CASH_PARAM = "?nocache=";


        }

        public static final class TimeoutDuration {
            public static final Duration ELEMENT_WAITING_DURATION_FULL = Duration.ofSeconds(10);
            public static final Duration ELEMENT_WAITING_DURATION_SMALL = ELEMENT_WAITING_DURATION_FULL.dividedBy(3);
            public static final Duration LONG_CLICK_DURATION = Duration.ofMillis(3000);
            public static final Duration MINIMAL_WAITING_DURATION = Duration.ofMillis(500);
            public static final Duration GO_TO_URL_DURATION = Duration.ofSeconds(5);
            public static final Duration DYNAMIC_CONTNET_LOADING_DURATION = Duration.ofSeconds(5);
            public static final Duration POPUP_WAITING_DURATION = DYNAMIC_CONTNET_LOADING_DURATION;
        }

        public static final class Cookies {
            public static final Cookie invProPromoteVariantCookie = new Cookie("invpro_promote_variant", "0");

            public static final Cookie eventPopupCounterCookie = new Cookie("event_popup_counter", "3");
            public static final Cookie eventPopupDidUserDismissedCookie = new Cookie("event_popup_did_user_dismissed", "1");
            public static final Cookie eventPopupLastShownCookie = new Cookie(
                    "event_popup_last_shown",
                    DateTimeUtilities.getCurrentDate("yyyy-MM-dd") + "T00%0000%0000.000Z"
            );

            public static final Cookie propicksPopupCounterCookie = new Cookie("propicks_popup_counter", "3");
            public static final Cookie propicksPopupDidUserDismissedCookie = new Cookie("propicks_popup_did_user_dismissed", "1");
            public static final Cookie propicksPopupUserClickedCookie = new Cookie("propicks_popup_user_clicked", "0");
            public static final Cookie propicksPopupLastShownCookie = new Cookie(
                    "propicks_popup_last_shown",
                    DateTimeUtilities.getCurrentDate("yyyy-MM-dd") + "T00%0000%0000.000Z"
            );

            public static final Cookie promoBannerAutoCookie = new Cookie("promo_banner_auto", "");

        }
    }
}
package infrastructure.constants;

import infrastructure.utilities.DateTimeUtilities;
import org.openqa.selenium.Cookie;

import java.time.Duration;

import static infrastructure.constants.ConstantProvider.WebConstant.Timeout.CLICK;
import static infrastructure.constants.ConstantProvider.WebConstant.Timeout.LONG_CLICK;

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

        public static final class Offset {
            public static final int HEADER_OFFSET = 120;
        }

        public static final class Page {
            public static final String CANARY_SUB_DOMAIN = "de-canary-gcp";

            public static final String CERTIFICATES_PAGE = "/certificates";

            public static final String COMMODITIES_PAGE = "/commodities";
            public static final String COMMODITIES_GOLD_PAGE = COMMODITIES_PAGE + "/gold";

            public static final String CRYPTO_PAGE = "/crypto";
            public static final String CRYPTO_TETHER_NEWS_PAGE = CRYPTO_PAGE + "/tether/news/";

            public static final String CURRENCIES_PAGE = "/currencies";

            public static final String EQUITIES_PAGE = "/equities";
            public static final String EQUITIES_APPLE_PAGE = EQUITIES_PAGE + "/apple-computer-inc";
            public static final String EQUITIES_TESLA_PAGE = EQUITIES_PAGE + "/tesla-motors";
            public static final String EQUITIES_ANADOLU_EFES_PAGE = EQUITIES_PAGE + "/anadolu-efes";
            public static final String EQUITIES_PRE_MARKET_PAGE = EQUITIES_PAGE + "/pre-market";
            public static final String EQUITIES_SENTIMENTS_PAGE = "/members-admin/sentiments" + EQUITIES_PAGE;

            public static final String HEADLINES_PAGE = "/news/headlines";

            public static final String HOME_NO_EDITION_URL = "investing.com";

            public static final String INDICES_PAGE = "/indices";
            public static final String INDICES_FUTURES_PAGE = INDICES_PAGE + "/indices-futures";
            public static final String INDICES_MAJOR_PAGE = INDICES_PAGE + "/major-indices";

            public static final String MAIN_PAGE = "/";

            public static final String NEWS_PAGE = "/news";
            public static final String NEWS_LATEST_PAGE = NEWS_PAGE + "/latest-news";
            public static final String NEWS_ARTICLE_795074_PAGE = NEWS_PAGE + "/economy/big-tech-disappoints-jobs-report-adani-woes--whats-moving-markets-795074";
            public static final String NEWS_ARTICLE_304104_PAGE = NEWS_PAGE + "/commodities-news/nymex-crude-prices-gain-in-asia-in-rebound-trade,-api-data-awaited-304104";
            public static final String NEWS_MOST_POPULAR_PAGE = NEWS_PAGE + "/most-popular-news"; // Only Shown on M.Site

            public static final String PORTFOLIO_PAGE = "/portfolio";

            public static final String TERMS_AND_CONDITIONS_PAGE = "/about-us/terms-and-conditions";
            public static final String WEBINARS_PAGE = "/education/webinars";
            public static final String WARREN_AI_PAGE = "/warrenai";
        }

        public static final class Timeout {
            public static final int CLICK = 2000;
            public static final int LONG_CLICK = 3000;
        }

        public static final class TimeoutDuration {
            public static final Duration ELEMENT_WAITING_DURATION_FULL = Duration.ofSeconds(10);
            public static final Duration ELEMENT_WAITING_DURATION_MEDIUM = ELEMENT_WAITING_DURATION_FULL.dividedBy(2);
            public static final Duration ELEMENT_WAITING_DURATION_SMALL = ELEMENT_WAITING_DURATION_FULL.dividedBy(3);
            public static final Duration ADS_LOADING_DURATION = Duration.ofSeconds(10);
            public static final Duration CLICK_DURATION = Duration.ofMillis(CLICK);
            public static final Duration LONG_CLICK_DURATION = Duration.ofMillis(LONG_CLICK);
            public static final Duration MINIMAL_WAITING_DURATION = Duration.ofMillis(500);
            public static final Duration DATA_UPDATE_DURATION = Duration.ofMinutes(1);
            public static final Duration GO_TO_URL_DURATION = Duration.ofSeconds(5);
            public static final Duration PRO_DATA_LOADING_DURATION = Duration.ofSeconds(12);
            public static final Duration PRO_SUBSCRIPTION_STATUS_CHANGING_DURATION = Duration.ofSeconds(25);
            public static final Duration PAYMENT_VALIDATION_DURATION = Duration.ofSeconds(15);
            public static final Duration SIGN_OUT_DURATION = Duration.ofSeconds(6);
            public static final Duration PRICE_UPDATE_DURATION = Duration.ofSeconds(15);
            public static final Duration COMMENT_POSTING_DURATION = Duration.ofMinutes(1);
            public static final Duration DYNAMIC_CONTNET_LOADING_DURATION = Duration.ofSeconds(5);
            public static final Duration POPUP_WAITING_DURATION = DYNAMIC_CONTNET_LOADING_DURATION;
            public static final Duration SCROLL_WAITING_DURATION = DYNAMIC_CONTNET_LOADING_DURATION;
            public static final Duration WAIT_TO_DISAPPEAR_DURATION = DYNAMIC_CONTNET_LOADING_DURATION;
            public static final Duration CHART_UPDATE_DURATION = DYNAMIC_CONTNET_LOADING_DURATION;
            public static final Duration MARKETS_TABLE_DATA_LOAD = Duration.ofSeconds(30);
            public static final Duration MARKETS_TABLE_DATA_LOAD_LIMIT = Duration.ofSeconds(10);
            public static final String PRO_PAGE = "/pro";

            public static final String NO_CASH_PARAM = "?nocache=";
        }

        public static final class Cookies {
            public static final String newDesignCookieName = "ab_test_equity_v2";
            public static final String geolocationCookieName = "geoC";

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
package infrastructure.constants;

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
            public static final String HOME_NO_EDITION_URL = "investing.com";
            public static final String PRO_PAGE = "/pro";
            public static final String NO_CASH_PARAM = "?nocache=";
        }

        public static final class TimeoutDuration {
            public static final Duration ELEMENT_WAITING_DURATION_FULL = Duration.ofSeconds(10);
            public static final Duration ELEMENT_WAITING_DURATION_SMALL = ELEMENT_WAITING_DURATION_FULL.dividedBy(3);
            public static final Duration LONG_CLICK_DURATION = Duration.ofMillis(3000);
            public static final Duration MINIMAL_WAITING_DURATION = Duration.ofMillis(500);
            public static final Duration DYNAMIC_CONTNET_LOADING_DURATION = Duration.ofSeconds(5);
            public static final Duration POPUP_WAITING_DURATION = DYNAMIC_CONTNET_LOADING_DURATION;
        }
    }
}
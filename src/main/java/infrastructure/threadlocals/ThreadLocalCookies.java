package infrastructure.threadlocals;

import infrastructure.threadlocals.abstracts.ThreadLocalAbstract;

/**
 * Handles flags for cookies, which have to be defined before each test.
 * Expected that value for such cookies should be set only 1 time for each session.
 * After the 1st opening of the web-site needed cookie should be updated and corresponding thread local value set to true.
 * It allows not to check for the value of needed cookie after opening every web-page
 */
public class ThreadLocalCookies {

    private static final ThreadLocalAbstract<Boolean> invProPromoteVariantCookie = new ThreadLocalAbstract<>("Pro Promo Popup displaying cookie flag", false) {};
    private static final ThreadLocalAbstract<Boolean> eventPopupCookies = new ThreadLocalAbstract<>("Pro Sale Promo Popup displaying cookies flag", false) {};
    private static final ThreadLocalAbstract<Boolean> proPicksPopupCookies = new ThreadLocalAbstract<>("Pro Sale Promo Popup displaying cookies flag", false) {};
    public static final ThreadLocalAbstract<Boolean> promoBannerAutoCookieAddedFlag = new ThreadLocalAbstract<>("promo_banner_auto cookie flag", false) {};

    //* Pro Promo Popup displaying cookie *//

    /**
     * Set flag for InvestingPro Promo popup displaying cookie as TRUE.
     * Means that cookie had been already modified with the needed value
     */
    public static synchronized void putInvProPromoteVariantCookieModifiedFlag() {
        invProPromoteVariantCookie.put(true);
    }

    public static synchronized boolean getInvProPromoteVariantCookieModifiedFlag() {
        return invProPromoteVariantCookie.get();
    }

    public static void clearInvProPromoteVariantCookieModifiedFlag() {
        invProPromoteVariantCookie.clear();
    }


    //* Pro Sale Promo Popup displaying cookies *//

    /**
     * Set flag for Pro Sale Promo popup displaying cookies as TRUE.
     * Means that cookies had been already modified with the needed value
     */
    public static synchronized void putEventPopupCookiesModifiedFlag() {
        eventPopupCookies.put(true);
    }

    public static synchronized boolean getEventPopupCookiesModifiedFlag() {
        return eventPopupCookies.get();
    }

    public static void clearEventPopupCookiesModifiedFlag() {
        eventPopupCookies.clear();
    }


    //* ProPicks Promo Popup displaying cookies *//

    /**
     * Set flag for ProPicks Promo popup displaying cookies as TRUE.
     * Means that cookies had been already modified with the needed value
     */
    public static synchronized void putProPicksPopupCookiesModifiedFlag() {
        proPicksPopupCookies.put(true);
    }

    public static synchronized boolean getProPicksPopupCookiesModifiedFlag() {
        return proPicksPopupCookies.get();
    }

    public static void clearProPicksPopupCookiesModifiedFlag() {
        proPicksPopupCookies.clear();
    }


    //* promo_banner_auto cookie *//

    /**
     * Set flag for promo_banner_auto cookie adding as TRUE.
     * Means that cookie had been already added
     */
    public static synchronized void putPromoBannerAutoCookieAddedFlag() {
        promoBannerAutoCookieAddedFlag.put(true);
    }

    public static synchronized boolean getPromoBannerAutoCookieAddedFlag() {
        return promoBannerAutoCookieAddedFlag.get();
    }

    public static void clearPromoBannerAutoCookieAddedFlag() {
        promoBannerAutoCookieAddedFlag.clear();
    }

    //* * * * * * General * * * * * *//

    public static void clear() {
        clearInvProPromoteVariantCookieModifiedFlag();
        clearEventPopupCookiesModifiedFlag();
        clearProPicksPopupCookiesModifiedFlag();
        clearPromoBannerAutoCookieAddedFlag();
    }
}

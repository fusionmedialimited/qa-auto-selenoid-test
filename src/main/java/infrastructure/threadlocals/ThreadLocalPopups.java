package infrastructure.threadlocals;

import infrastructure.threadlocals.abstracts.ThreadLocalAbstract;

/**
 * Handles flags for promo and security popups.
 * Expected that such popups should be displayed only 1 time for each session.
 * After detecting and closing such popup corresponding thread local value set to true.
 * It allows not to wait for displayed popup after opening every web-page
 */
public class ThreadLocalPopups {

    private static final ThreadLocalAbstract<Boolean> privacyCcpaPopup = new ThreadLocalAbstract<>("Privacy Popup flag", false) {};
    private static final ThreadLocalAbstract<Boolean> proPicksPromoPopup = new ThreadLocalAbstract<>("Pro Picks Promo Popup flag", false) {};


    //* * * * * Privacy and CCPA Popups * * * * *//

    /**
     * Set flag for Privacy or CCPA popup as TRUE.
     * Means that Privacy or CCPA popup have been already closed
     */
    public static synchronized void putPrivacyCcpaPopupClosedFlag() {
        privacyCcpaPopup.put(true);
    }

    public static synchronized boolean getPrivacyCcpaPopupShownFlag() {
        return privacyCcpaPopup.get();
    }

    public static void clearPrivacyCcpaPopupFlag() {
        privacyCcpaPopup.clear();
    }


    //* ProPicks Popup *//

    /**
     * Set flag for ProPicks Promo popup as TRUE.
     * Means that ProPicks Promo popup had been already shown
     */
    public static synchronized void putProPicksPromoPopupShownFlag() {
        proPicksPromoPopup.put(true);
    }

    public static synchronized boolean getProPicksPromoPopupShownFlag() {
        return proPicksPromoPopup.get();
    }

    public static void clearProPicksPromoPopupFlag() {
        proPicksPromoPopup.clear();
    }


    //* * * * * * General * * * * * *//

    public static void clear() {
        clearPrivacyCcpaPopupFlag();
        clearProPicksPromoPopupFlag();
    }
}

package infrastructure.threadlocals;

import infrastructure.threadlocals.abstracts.ThreadLocalAbstract;

/**
 * Handles flags for promo and security popups.
 * Expected that such popups should be displayed only 1 time for each session.
 * After detecting and closing such popup corresponding thread local value set to true.
 * It allows not to wait for displayed popup after opening every web-page
 */
public class ThreadLocalPopups {

    private static final ThreadLocalAbstract<Boolean> promoPopup = new ThreadLocalAbstract<>("Promo Popup flag", false) {};
    private static final ThreadLocalAbstract<Boolean> privacyCcpaPopup = new ThreadLocalAbstract<>("Privacy Popup flag", false) {};
    private static final ThreadLocalAbstract<Boolean> proIntroPopup = new ThreadLocalAbstract<>("Pro Intro Popup flag", false) {};
    private static final ThreadLocalAbstract<Boolean> proPromoPopup = new ThreadLocalAbstract<>("Pro Picks Promo Popup flag", false) {};

    private static final ThreadLocalAbstract<Boolean> proSalePromoPopup = new ThreadLocalAbstract<>("Pro Black Friday Promo Popup flag", false) {};
    private static final ThreadLocalAbstract<Boolean> proPicksPromoPopup = new ThreadLocalAbstract<>("Pro Picks Promo Popup flag", false) {};
    private static final ThreadLocalAbstract<Boolean> proTipsPromoPopup = new ThreadLocalAbstract<>("Pro Tips Promo Popup flag", false) {};
    private static final ThreadLocalAbstract<Boolean> proMarchSalePromoPopup = new ThreadLocalAbstract<>("Pro March Sale Promo Popup flag", false) {};

    //* * * * * Sign Up Promo Popup * * * * *//

    /**
     * Set flag for Sign Up Promo popup as TRUE.
     * Means that Sign Up Promo popup have been already shown
     */
    public static synchronized void putSignUpPromoPopupShownFlag() {
        promoPopup.put(true);
    }

    public static synchronized boolean getSignUpPromoPopupShownFlag() {
        return promoPopup.get();
    }

    public static void clearSignUpPromoPopupFlag() {
        promoPopup.clear();
    }

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


    //* * * InvestingPro Intro Popup * * *//

    /**
     * Set flag for InvestingPro Intro popup as TRUE.
     * Means that InvestingPro Intro popup had been already shown
     */
    public static synchronized void putProIntroPopupShownFlag() {
        proIntroPopup.put(true);
    }

    public static synchronized boolean getProIntroPopupShownFlag() {
        return proIntroPopup.get();
    }

    public static void clearProIntroPopupFlag() {
        proIntroPopup.clear();
    }

    //* Pro Promo Popup *//

    /**
     * Set flag for InvestingPro Promo popup as TRUE.
     * Means that InvestingPro Promo popup had been already shown
     */
    public static synchronized void putProPromoPopupShownFlag() {
        proPromoPopup.put(true);
    }

    public static synchronized boolean getProPromoPopupShownFlag() {
        return proPromoPopup.get();
    }

    public static void clearProPromoPopupFlag() {
        proPromoPopup.clear();
    }

    //* InvestingPro Black Friday Promo Popup *//

    /**
     * Set flag for InvestingPro Sale Promo popup as TRUE.
     * Means that InvestingPro Black Friday Promo popup had been already shown
     */
    public static synchronized void putProSalePromoPopupShownFlag() {
        proSalePromoPopup.put(true);
    }

    public static synchronized boolean getProSalePromoPopupShownFlag() {
        return proSalePromoPopup.get();
    }

    public static void clearProSalePromoPopupFlag() {
        proSalePromoPopup.clear();
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

    //* ProTips Popup *//

    /**
     * Set flag for ProTips Promo popup as TRUE.
     * Means that ProTips Promo popup had been already shown
     */
    public static synchronized void putProTipsPromoPopupShownFlag() {
        proTipsPromoPopup.put(true);
    }

    public static synchronized boolean getProTipsPromoPopupShownFlag() {
        return proTipsPromoPopup.get();
    }

    public static void clearProTipsPromoPopupFlag() {
        proTipsPromoPopup.clear();
    }

    //* Pro March Sale Popup *//

    /**
     * Set flag for Pro March Sale Promo popup as TRUE.
     * Means that Pro March Sale Promo popup had been already shown
     */
    public static synchronized void putProMarchSalePromoPopupShownFlag() {
        proMarchSalePromoPopup.put(true);
    }

    public static synchronized boolean getProMarchSalePromoPopupShownFlag() {
        return proMarchSalePromoPopup.get();
    }

    public static void clearProMarchSalePromoPopupFlag() {
        proMarchSalePromoPopup.clear();
    }

    //* * * * * * General * * * * * *//

    public static void clear() {
        clearSignUpPromoPopupFlag();
        clearPrivacyCcpaPopupFlag();
        clearProIntroPopupFlag();
        clearProPromoPopupFlag();
        clearProSalePromoPopupFlag();
        clearProPicksPromoPopupFlag();
        clearProTipsPromoPopupFlag();
        clearProMarchSalePromoPopupFlag();
    }
}

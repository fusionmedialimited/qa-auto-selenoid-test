package infrastructure.listeners.webriver.helpers;

import infrastructure.utilities.InvestingUserUtilities;
import infrastructure.utilities.PageDesignUtilities;

import java.util.function.Predicate;
import java.util.function.Supplier;

import static infrastructure.constants.ConstantProvider.WebConstant.Page.*;

public class WDListenerBaseHelper {

    /**
     * Privacy or CCPA popup can be displayed on any Investing.com page
     */
    public static Predicate<String> isPageRelevantForPrivacyOrCcpaPopup = url ->
            url.contains(HOME_NO_EDITION_URL);

    /**
     * SignUp popup can be displayed for signed out users on the Legacy pages
     */
    public static Supplier<Boolean> isPageRelevantForSignUpPopup = () ->
            !(InvestingUserUtilities.isUserSignedIn() || PageDesignUtilities.isNewDesign());

    /**
     * <b>Sale popup displaying conditions:</b>
     * <ul>
     *     <li>Editions: All</li>
     *     <li>Triggering after opening equities overview pages only</li>
     * </ul>
     */
    public static Predicate<String> isPageRelevantForProSaleBanner = page ->
            page.startsWith(EQUITIES_PAGE);

    /**
     * <b>ProPicks popup displaying conditions:</b>
     * <ul>
     *     <li>Editions: All EN editions, IT,ES,TR,DE,FR,MX,TH,BR,KR,PL,SA,RU,CN.VN,IN,JP,NK,PT,HK,GR,IL</li>
     *     <li>Triggered after scroll or click</li>
     *     <li>Relevant pages:
     *         <ul>
     *             <li>/indices/indices-futures</li>
     *             <li>/indices/major-indices</li>
     *             <li>/indices/us-spx-500-futures</li>
     *             <li>/indices/us-spx-500</li>
     *         </ul>
     *     </li>
     * </ul>
     */
    public static Predicate<String> isPageRelevantForProPicksBanner = page ->
            page.contains(INDICES_FUTURES_PAGE)
                    || page.contains(INDICES_MAJOR_PAGE)
                    || page.contains("us-spx-500-futures")
                    || page.contains("us-spx-500");

    /**
     * <b>ProTips popup displaying conditions:</b> page starts with '/equities/'
     */
    public static Predicate<String> isPageRelevantForProTipsBanner = page ->
            page.startsWith(EQUITIES_PAGE + "/");


    //TODO: define conditions for the Pro March Sale popup
    /**
     * <b>Pro March Sale popup displaying conditions:</b> page starts with '/indices/' or '/news/'
     */
    public static Predicate<String> isPageRelevantForProMarchSaleBanner = page ->
            page.startsWith(INDICES_PAGE + "/") || page.startsWith(NEWS_PAGE + "/");

    public static void closeBannersAfterScrollOrClick() {
        // ProPicks banner processing if env is Canary
        WDListenerPopupHelper.detectAndCloseProPicksPromoPopupOnCanary();
        // Pro March Sale banner processing
        WDListenerPopupHelper.detectAndCloseProMarchSalePromoPopup();
    }
}

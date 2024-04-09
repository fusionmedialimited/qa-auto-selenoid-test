package infrastructure.listeners.webriver.helpers;

import java.util.function.Predicate;

import static infrastructure.constants.ConstantProvider.WebConstant.Page.*;

public class WDListenerBaseHelper {

    /**
     * Privacy or CCPA popup can be displayed on any Investing.com page
     */
    public static Predicate<String> isPageRelevantForPrivacyOrCcpaPopup = url ->
            url.contains(HOME_NO_EDITION_URL);

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

    public static void closeBannersAfterScrollOrClick() {
        // ProPicks banner processing if env is Canary
        WDListenerPopupHelper.detectAndCloseProPicksPromoPopupOnCanary();
    }
}

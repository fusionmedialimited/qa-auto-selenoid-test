package infrastructure.enums;

public enum PageType {
    LEGACY,
    LEGACY_MOBILE_VIEW,
    REFACTORED,
    REFACTORED_MOBILE_VIEW,
    REFACTORED_FULL_WIDTH;

    public static boolean isRefactored(PageType pageType) {
        return pageType.equals(REFACTORED) || pageType.equals(REFACTORED_MOBILE_VIEW);
    }
}

package infrastructure.enums;

import org.apache.commons.lang3.StringUtils;
import org.openqa.selenium.By;

public enum BySelectorType {
    xpath,
    name,
    className,
    linkText,
    partialLinkText,
    id,
    tagName,
    cssSelector;

    public By buildLocator(String value) {
        return switch (this) {
            case xpath -> By.xpath(value);
            case name -> By.name(value);
            case className -> By.className(value);
            case linkText -> By.linkText(value);
            case partialLinkText -> By.partialLinkText(value);
            case id -> By.id(value);
            case tagName -> By.tagName(value);
            case cssSelector -> By.cssSelector(value);
        };
    }

    public static BySelectorType fromString(String text) {
        try {
            String textNormalized = StringUtils.removeStart(text, "By.");
            return valueOf(textNormalized);
        } catch (IllegalArgumentException e) {
            return switch (text) {
                case "tag name" -> tagName;
                case "css selector" -> cssSelector;
                case "class name" -> className;
                default -> throw new IllegalArgumentException("Couldn't defined type of selector for \"" + text + "\"!");
            };
        }
    }
}

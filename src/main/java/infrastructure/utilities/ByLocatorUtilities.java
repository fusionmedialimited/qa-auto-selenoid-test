package infrastructure.utilities;

import infrastructure.enums.BySelectorType;
import infrastructure.exceptions.InvestingException;
import org.apache.commons.lang3.RegExUtils;
import org.apache.commons.lang3.StringUtils;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.pagefactory.ByAll;
import org.openqa.selenium.support.pagefactory.ByChained;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Stream;

public class ByLocatorUtilities {

    /**
     * This method gets the selector value out of the specified web element
     *
     * @param element from which selector will be extracted
     * @return the selector value as string
     */
    public static String getSelectorFromWebElement(WebElement element) {
        if (element.toString().contains("->")) {
            String[] arr = element.toString().split("->");
            StringBuilder sb = new StringBuilder();
            for (int i = 1; i < arr.length; i++) {
                String item = arr[i].trim();
                int openingSquareBracketsNumber = StringUtils.countMatches(item, '[');

                while (openingSquareBracketsNumber < StringUtils.countMatches(item, ']'))
                    item = item.substring(0, item.lastIndexOf(']'));

                sb.append(StringUtils.removeEnd(item, "}"));

                if (i < arr.length - 1)
                    sb.append(" -> ");
            }

            return sb.toString();
        }
        else return StringUtils.removeEnd(
                element.toString().contains("Proxy element")
                        ? element.toString().substring(element.toString().indexOf("'") + 1, element.toString().lastIndexOf("'"))
                        : "[locator not found]",
                "}");
    }

    /**
     * This method gets the locator value out of the specified web element
     *
     * @param element from which locator will be extracted
     * @return the locator value as {@link By} instance
     */
    public static By getLocatorFromElement(WebElement element) {
        String[] selectors = getSelectorFromWebElement(element).split("\\s*->\\s*");

        By[] locators = Arrays.stream(selectors)
                .map(ByLocatorUtilities::getLocatorFromSelector)
                .toArray(By[]::new);

        return new ByChained(locators);
    }

    /**
     * This method builds locator using string selector
     *
     * @param selector selector which is used for building locator
     * @return locator as {@link By} or {@link ByAll} instance
     */
    public static By getLocatorFromSelector(String selector) {
        // e.g.: By.all({By.tagName: label,By.className: inv-checkbox-text})
        if (StringUtils.startsWith(selector, "By.all({")) {
            return compositeSelectorToLocator(selector, ByAll.class);
        } else {
            return simpleSelectorToLocator(selector);
        }
    }

    /**
     * This method builds locator using string selector. <br>
     * Works only with simple selectors from {@link By}:
     * <ul>
     *     <li>id</li>
     *     <li>linkText</li>
     *     <li>partialLinkText</li>
     *     <li>name</li>
     *     <li>tagName</li>
     *     <li>xpath</li>
     *     <li>className</li>
     *     <li>cssSelector</li>
     * </ul>
     *
     * @param selector selector which is used for building locator
     * @return locator as {@link By} instance
     */
    private static By simpleSelectorToLocator(String selector) {
        int indexOfSeparator = selector.indexOf(":");

        String selectorType = selector.substring(0, indexOfSeparator).trim();
        String value = selector.substring(indexOfSeparator + 1).trim();

        return BySelectorType.fromString(selectorType).buildLocator(value);
    }

    /**
     * This method builds locator using string selector. <br>
     * Works only with composite selectors:
     * <ul>
     *     <li>{@link ByAll}</li>
     *     <li>{@link ByChained}</li>
     * </ul>
     *
     * @param selector selector which is used for building locator
     * @param clazz type of composite selector
     * @return locator as {@link By} instance
     */
    private static By compositeSelectorToLocator(String selector, Class<?> clazz) {
        // locator type validation
        if (!(clazz == ByAll.class || clazz == ByChained.class))
            throw new IllegalArgumentException("Locator from composite selector could be build only for ByAll or ByChained types! But provided type is: " + clazz.getSimpleName());

        By[] subLocators = streamOfLocatorsFromCompositeSelector(selector)
                .toArray(By[]::new);

        try {
            return (By) clazz.getConstructor(By[].class).newInstance(new Object[] { subLocators });
        } catch (InvocationTargetException | InstantiationException | IllegalAccessException | NoSuchMethodException cause) {
            throw new InvestingException("Couldn't build locator from composite selector!", cause);
        }
    }

    private static Stream<By> streamOfLocatorsFromCompositeSelector(String selector) {

        selector = RegExUtils.removePattern(selector, "^By\\.\\w+\\(\\{");
        selector = RegExUtils.removePattern(selector, "\\}\\)$");

        List<String> subSelectors = new ArrayList<>();

        // By.all({By.tagName: ...,By.className: ...,...})
        // By.chained({By.tagName: ...,By.className: ...,...})
        Matcher compositeMatcher = Pattern.compile("^By\\.(all|chained)\\(\\{.*\\}\\)").matcher(selector);

        while (StringUtils.countMatches(selector, "By.") > 0) {
            String subSelector = (compositeMatcher.find())
                    ? compositeMatcher.group()
                    : selector.split(",?By\\.")[1];

            subSelectors.add(subSelector);

            selector = RegExUtils.replaceFirst(selector, "^,?By\\.", "")
                    .trim();

            subSelector = RegExUtils.replaceFirst(subSelector, "^,?By\\.", "")
                    .trim();

            selector = StringUtils.removeStart(selector, subSelector);
        }

        // convert array of sub-selectors like [{By.tagName: ...}, {By.className: ...}, ...]
        // to stream of locators, built from each sub-selector
        return subSelectors.stream()
                .filter(subSelector -> !subSelector.isEmpty())
                .map(subSelector -> {
                    try {
                        return simpleSelectorToLocator(subSelector);
                    } catch (IllegalArgumentException e) {
                        Class<?> type = StringUtils.startsWith(subSelector, "By.all({")
                                ? ByAll.class
                                : ByChained.class;
                        return compositeSelectorToLocator(subSelector, type);
                    }
                });
    }
}

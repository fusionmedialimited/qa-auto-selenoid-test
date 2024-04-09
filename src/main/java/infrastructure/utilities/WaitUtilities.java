package infrastructure.utilities;

import infrastructure.Investing;
import infrastructure.constants.ConstantProvider;
import infrastructure.exceptions.InvestingException;
import infrastructure.logger.Log;
import infrastructure.threadlocals.ThreadLocalDriver;
import org.openqa.selenium.*;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.FluentWait;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;
import java.util.List;
import java.util.stream.Collectors;

import static infrastructure.allure.AllureAttachments.allureAttachText;
import static infrastructure.constants.ConstantProvider.WebConstant.TimeoutDuration.*;
import static org.openqa.selenium.support.ui.ExpectedConditions.*;

public class WaitUtilities {

    /**
     * waits for the specified amount of time in millis
     *
     * @param driver:       driver object
     * @param timeInMillis: to wait
     */
    public static FluentWait<WebDriver> customWait(Investing driver, int timeInMillis) {
        WebDriver webDriver = driver.getDriver();
        return new WebDriverWait(webDriver, Duration.ofMillis(timeInMillis));
    }

    /**
     * waits for the specified amount of time
     *
     * @param duration: to wait
     */
    public static FluentWait<WebDriver> customWait(Duration duration) {
        Log.info("Waiting for " + duration.toMillis() + " ms");
        return new WebDriverWait(ThreadLocalDriver.get(), duration);
    }

    /**
     * waits for the duration, provide in constant:
     * {@link ConstantProvider.WebConstant.TimeoutDuration#ELEMENT_WAITING_DURATION_FULL}
     */
    public static FluentWait<WebDriver> customWait() {
        return customWait(ELEMENT_WAITING_DURATION_FULL);
    }

    /**
     * Wait until provided condition will be completed for provided duration
     *
     * @param duration duration to wait
     * @param isTrue   condition to wait
     */
    public static <T> T waitUntil(Duration duration, ExpectedCondition<T> isTrue) {
        Log.info("Expected condition to wait: " + isTrue.toString());
        return customWait(duration).until(isTrue);
    }

    /**
     * Wait until provided condition will be completed. <br>
     * Duration of waiting is provided in constant:
     * {@link ConstantProvider.WebConstant.TimeoutDuration#ELEMENT_WAITING_DURATION_FULL}
     *
     * @param isTrue condition to wait
     */
    public static <T> T waitUntil(ExpectedCondition<T> isTrue) {
        return customWait().until(isTrue);
    }

    /**
     * Implicitly waits for presence of passed web element for a passed duration
     *
     * @param elementAttr element or locator to wait for
     * @param duration    duration to wait
     */
    public static <T> WebElement waitForPresence(Duration duration, T elementAttr) {
        try {
            BaseUtilities.validateElementOrLocator(elementAttr);

            if (elementAttr instanceof By)
                return waitUntil(duration, presenceOfElementLocated((By) elementAttr));
            else {
                WebElement element = (WebElement) elementAttr;
                waitUntil(MINIMAL_WAITING_DURATION, not(stalenessOf(element)));
                return element;
            }
        } catch (TimeoutException cause) {
            throw new NoSuchElementException("Element \"" + elementAttr + "\" did not show up within given time '" + duration.toMillis() + "' ms!", cause);
        } catch (Exception cause) {
            throw  new InvestingException("Waiting for element presence failed: " + elementAttr, cause);
        }
    }

    /**
     * waits for presence of passed web element.
     * Duration of waiting is provided from {@link ConstantProvider.WebConstant.TimeoutDuration#ELEMENT_WAITING_DURATION_FULL} const
     *
     * @param elementAttr element or locator to wait for
     */
    public static <T> WebElement waitForPresence(T elementAttr) {
        return waitForPresence(ELEMENT_WAITING_DURATION_FULL, elementAttr);
    }
    /**
     * waits for visibility of existing Web element
     *
     * @param elementAttr element or locator to wait for
     * @param duration    duration to wait
     */
    public static <T> WebElement waitForVisibility(Duration duration, T elementAttr) {
        try {
            WebElement element = waitForPresence(duration, elementAttr);
            return waitUntil(duration, visibilityOf(element));
        } catch (TimeoutException | NoSuchElementException cause) {
            throw new TimeoutException("Time for waiting for visibility of " + elementAttr + " element finished, but it is not visible!", cause);
        } catch (Exception cause) {
            throw new InvestingException("Waiting for visibility of " + elementAttr + " element failed!", cause);
        }
    }

    /**
     * waits for visibility of existing Web element. <br>
     * Duration of waiting is provided from {@link ConstantProvider.WebConstant.TimeoutDuration#ELEMENT_WAITING_DURATION_FULL} const
     *
     * @param elementAttr element or locator to wait for
     */
    public static <T> WebElement waitForVisibility(T elementAttr) {
        return waitForVisibility(ELEMENT_WAITING_DURATION_FULL, elementAttr);
    }

    /**
     * waits for visibility of all provided elements
     *
     * @param elementAttrs element(s) or locator(s) to wait for
     * @param duration     duration to wait
     */
    public static <T> List<WebElement> waitForVisibilityAll(Duration duration, List<T> elementAttrs) {
        try {
            return elementAttrs.stream()
                            .map(elementAttr -> waitForVisibility(duration, elementAttr))
                            .collect(Collectors.toList());
        } catch (TimeoutException | NoSuchElementException cause) {
                throw new TimeoutException("Time for waiting for visibility of several elements finished, but some of them is not visible!", cause);
        } catch (Exception cause) {
                throw new InvestingException("Waiting for visibility of several elements failed!", cause);
        }
    }

    /**
     * waits for visibility of all provided elements
     *
     * @param elementAttrs element(s) or locator(s) to wait for
     */
    public static <T> List<WebElement> waitForVisibilityAll(List<T> elementAttrs) {
        try {
            return elementAttrs.stream()
                            .map(WaitUtilities::waitForVisibility)
                            .collect(Collectors.toList());
        } catch (TimeoutException | NoSuchElementException cause) {
                throw new TimeoutException("Time for waiting for visibility of several elements finished, but some of them is not visible!", cause);
        } catch (Exception cause) {
                throw new InvestingException("Waiting for visibility of several elements failed!", cause);
        }
    }

    /**
     * Waits for an element to be invisible or not present on the DOM.
     *
     * @param elementAttr element to disappear
     * @param duration    duration to wait
     */
    public static <T> void waitForInvisibility(Duration duration, T elementAttr) {
        try {
            BaseUtilities.validateElementOrLocator(elementAttr);

            if (elementAttr instanceof By)
                waitUntil(duration, invisibilityOfElementLocated((By) elementAttr));
            else
                waitUntil(duration, invisibilityOf((WebElement) elementAttr));
        } catch (TimeoutException cause) {
            throw new TimeoutException("Element \"" + elementAttr + "\" wasn't hidden after " + duration.toMillis() + " ms!", cause);
        } catch (Exception cause) {
            throw new InvestingException("Waiting for invisibility of element \"" + elementAttr + "\" failed!", cause);
        }
    }

    /**
     * Waits for an element to be invisible or not present on the DOM.
     * Duration of waiting is provided from {@link ConstantProvider.WebConstant.TimeoutDuration#ELEMENT_WAITING_DURATION_FULL} const
     *
     * @param elementAttr element to disappear
     */
    public static <T> void waitForInvisibility(T elementAttr) {
        waitForInvisibility(ELEMENT_WAITING_DURATION_FULL, elementAttr);
    }

    /**
     * Wait for loader appearing and further disappearing, if it was displayed
     *
     * @param loaderLocator locator of the loader, which should be displayed and then hidden
     * @param timeoutForPresence  time to wait for loader will be displayed
     * @param timeoutForDisappearing time to wait for loader will be hidden
     */
    public static void waitForLoading(By loaderLocator, Duration timeoutForPresence, Duration timeoutForDisappearing) {
        // wait for loader presence
        try {
            waitUntil(timeoutForPresence, visibilityOfElementLocated(loaderLocator));
        } catch (TimeoutException timeoutException) {
            return;
        }

        // wait for loader disappearing
        try {
            waitUntil(timeoutForDisappearing, invisibilityOfElementLocated(loaderLocator));
        } catch (TimeoutException timeoutException) {
            allureAttachText("warn", "Time was up, but loader have been still displayed!");
        }

        Log.info("Loader was displayed and then hidden");
    }
}

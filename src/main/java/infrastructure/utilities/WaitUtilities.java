package infrastructure.utilities;

import infrastructure.constants.ConstantProvider;
import infrastructure.exceptions.InvestingException;
import infrastructure.logger.Log;
import infrastructure.threadlocals.ThreadLocalDriver;
import org.openqa.selenium.*;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.FluentWait;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

import static infrastructure.constants.ConstantProvider.WebConstant.TimeoutDuration.ELEMENT_WAITING_DURATION_FULL;
import static infrastructure.constants.ConstantProvider.WebConstant.TimeoutDuration.MINIMAL_WAITING_DURATION;
import static org.openqa.selenium.support.ui.ExpectedConditions.*;

public class WaitUtilities {


    /**
     * waits for the specified amount of time
     *
     * @param duration: to wait
     */
    public static FluentWait<WebDriver> customWait(Duration duration) {
        Log.info("Waiting for " + duration.toMillis() + " ms");
        return new WebDriverWait(ThreadLocalDriver.get().getDriver(), duration);
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

}

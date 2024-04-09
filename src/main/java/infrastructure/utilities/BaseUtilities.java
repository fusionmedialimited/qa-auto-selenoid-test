package infrastructure.utilities;

import infrastructure.exceptions.InvestingException;
import infrastructure.logger.Log;
import infrastructure.threadlocals.ThreadLocalDriver;
import org.apache.commons.lang.IllegalClassException;
import org.openqa.selenium.*;

import java.time.Duration;

import static infrastructure.constants.ConstantProvider.WebConstant.TimeoutDuration.LONG_CLICK_DURATION;
import static infrastructure.constants.ConstantProvider.WebConstant.TimeoutDuration.MINIMAL_WAITING_DURATION;
import static infrastructure.utilities.WaitUtilities.waitForVisibility;
import static infrastructure.utilities.WaitUtilities.waitUntil;
import static io.qameta.allure.Allure.step;
import static org.openqa.selenium.support.ui.ExpectedConditions.elementToBeClickable;
import static org.openqa.selenium.support.ui.ExpectedConditions.presenceOfElementLocated;

public class BaseUtilities {

    /**
     * Validate that {@link By} or {@link WebElement} instance is provided
     *
     * @param elementAttr checking object
     * @param <T> {@link By} or {@link WebElement} type
     */
    public static <T> void validateElementOrLocator(T elementAttr) {
        if (elementAttr == null)
            throw new NoSuchElementException("Nothing to validate, because elementAttr is NULL!");

        Class<?> clazz = elementAttr.getClass();
        if (!(By.class.isAssignableFrom(clazz) || WebElement.class.isAssignableFrom(clazz)))
            throw new IllegalClassException("Expected instance of By locator or WebElement, but found: " + clazz.getSimpleName());
    }

    /**
     * returns TRUE, if element is displayed. Else returns FALSE
     *
     * @param elements: to check
     */
    public static boolean isElementDisplayed(WebElement... elements) {
        try {
            for (WebElement el : elements) {
                if (!el.isDisplayed()) {
                    return false;
                }
            }
            return true;
        } catch (NoSuchElementException noSuchElementException) {
            return false;
        }
    }

    /**
     * Clicking on the given element and waits for element to be present, handling exceptions if needed.
     *
     * @param elementAttr element or locator to click
     */
    public static <T> void clickOnVisibleElement(T elementAttr) {
        try {
            waitUntil(
                    LONG_CLICK_DURATION,
                    elementToBeClickable(waitForVisibility(elementAttr))
            ).click();
        } catch (Exception cause) {
            throw new InvestingException("Couldn't click on " + elementAttr + " element!", cause);
        }
    }

    public static boolean isCookiePresented(String name) {
        Log.info(String.format("Check if cookie with name \"%s\" is presented in the browser storage", name));

        try {
            return ThreadLocalDriver.get().manage().getCookieNamed(name) != null;
        } catch (Exception cause) {
            throw new InvestingException (String.format("Couldn't check if cookie with name \"%s\" is presented in the browser storage!", name), cause);
        }
    }

    public static boolean isCookiePresented(Cookie cookie, Duration duration) {
        try {
            waitUntil(duration, drv -> isCookiePresented(cookie.getName()));
            return true;
        } catch (TimeoutException ignore) {}

        return false;
    }

    public static boolean isCookiePresented(Cookie cookie) {
        return isCookiePresented(cookie, MINIMAL_WAITING_DURATION);
    }

    /**
     * This method closes prompt pop-up if present
     *
     * @param element WebElement object
     */
    public static void removeElementFromDOM(WebElement element) {
        try {
            if (isElementDisplayed(element)) {
                step("Removing element from DOM: " + element, () -> {
                    JavascriptExecutor js = (JavascriptExecutor) ThreadLocalDriver.get().getDriver();
                    js.executeScript("arguments[0].remove();", element);
                });
            }
        } catch (TimeoutException | NoSuchElementException ignore) {
        } catch (Exception cause) {
            throw new InvestingException("Couldn't remove element from DOM: " + element, cause);
        }
    }

    public static void removeElementFromDOM(By locator, Duration duration) {
        try {
            WebElement element = waitUntil(duration, drv ->
                    presenceOfElementLocated(locator)).apply(ThreadLocalDriver.get().getDriver());

            removeElementFromDOM(element);
        } catch (TimeoutException | NoSuchElementException ignore) {}
    }

}


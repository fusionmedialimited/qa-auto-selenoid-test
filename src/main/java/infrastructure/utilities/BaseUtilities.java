package infrastructure.utilities;

import infrastructure.Investing;
import infrastructure.exceptions.InvestingException;
import infrastructure.logger.Log;
import infrastructure.threadlocals.ThreadLocalDriver;
import org.apache.commons.lang.IllegalClassException;
import org.apache.commons.lang3.RandomStringUtils;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.*;
import org.openqa.selenium.interactions.Actions;

import java.net.HttpCookie;
import java.time.Duration;
import java.time.Instant;
import java.util.*;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.Collectors;

import static infrastructure.allure.AllureAttachments.allureAttachText;
import static infrastructure.constants.ConstantProvider.WebConstant.Timeout.CLICK;
import static infrastructure.constants.ConstantProvider.WebConstant.TimeoutDuration.*;
import static infrastructure.utilities.WaitUtilities.waitForVisibility;
import static infrastructure.utilities.WaitUtilities.waitUntil;
import static io.qameta.allure.Allure.step;
import static org.openqa.selenium.support.ui.ExpectedConditions.elementToBeClickable;
import static org.openqa.selenium.support.ui.ExpectedConditions.presenceOfElementLocated;

public class BaseUtilities {

    /**
     * Allows to avoid exceptions like <br> <font color = "red">
     *     class {instance of {@link Number}} cannot be cast to class {another instance of {@link Number}} <color/>
     */
    private static final Function<Object, Integer> numberToIntFn = obj-> ((Number) obj).intValue();

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

    public static boolean isElementDisplayed(Investing driver, By element) {
        try {
            return isElementDisplayed(driver.findElement(element));
        } catch (NoSuchElementException noSuchElementException) {
            return false;
        }
    }

    public static boolean isChildElementDisplayed(Duration duration, WebElement parent, By... children) {
        try {
            for (By child : children) {
                waitForVisibility(duration, getChildElement(parent, child));
            }
        } catch (NoSuchElementException | TimeoutException e) {
            allureAttachText("error", String.format("Nested element visibility check failed: %s", e.getMessage()));
            return false;
        }

        return true;
    }

    public static boolean isChildElementDisplayed(WebElement parent, By... children) {
        try {
            for (By child : children) {
                if (!isElementDisplayed(getChildElement(parent, child)))
                    throw new InvalidElementStateException(String.format("Child element with locator %s found but isn't displayed", child));
            }
        } catch (NoSuchElementException | InvalidElementStateException exception) {
            return false;
        }

        return true;
    }

    /**
     * check if element is displayed + handle exception
     *
     * @param element: to check
     */
    public static void checkIfElementDisplayed(WebElement element) {
        String elementLocator = ByLocatorUtilities.getSelectorFromWebElement(element);

        try {
            element.isDisplayed();
            allureAttachText("info", "The '" + elementLocator + "' element is Displayed");
        } catch (NoSuchElementException cause) {
            allureAttachText("error", "The '" + elementLocator + "' element is not displayed");
            throw new NoSuchElementException("[ERROR] Element is Not Displayed: ", cause);
        }
    }

    /**
     * Verifies passed element presence and visibility within a given duration
     * Returns false if element is not present or visible
     *
     * @param duration    duration to wait
     * @param elementAttr element or locator
     */
    public static <T> boolean isElementPresentAndVisible(Duration duration, T elementAttr) {
        try {
            waitForVisibility(duration, elementAttr);
            return true;
        } catch (TimeoutException ignore) {}

        return false;
    }

    /**
     * Get height of the viewport for the current browser window
     */
    public static int getViewportHeight(Investing driver) {
        JavascriptExecutor js = (JavascriptExecutor) driver.getDriver();
        return numberToIntFn.apply(
                js.executeScript("return window.innerHeight;")
        );
    }

    /**
     * Get height of the viewport for the current browser window
     */
    public static int getViewportWidth(Investing driver) {
        JavascriptExecutor js = (JavascriptExecutor) driver.getDriver();
        return numberToIntFn.apply(
                js.executeScript("return window.innerWidth;")
        );
    }

    /**
     * Check if specified element is fully displayed in the viewport
     */
    public static boolean isElementFullyInViewport(Investing driver, WebElement element) {
        Log.info(String.format(
                "Check if element \"%s\" is fully displayed in viewport", ByLocatorUtilities.getSelectorFromWebElement(element))
        );

        try {
            JavascriptExecutor js = (JavascriptExecutor) driver.getDriver();

            int elementTop = numberToIntFn.apply(
                    js.executeScript("return arguments[0].getBoundingClientRect().top;", element));
            int elementBottom = numberToIntFn.apply(
                    js.executeScript("return arguments[0].getBoundingClientRect().bottom;", element));
            int elementLeft = numberToIntFn.apply(
                    js.executeScript("return arguments[0].getBoundingClientRect().left;", element));
            int elementRight = numberToIntFn.apply(
                    js.executeScript("return arguments[0].getBoundingClientRect().right;", element));
            int viewportHeight = getViewportHeight(driver);
            int viewportWidth = getViewportWidth(driver);

            return elementTop >= 0
                    && elementBottom <= viewportHeight
                    && elementLeft >= 0
                    && elementRight <= viewportWidth;
        } catch (Exception cause) {
            allureAttachText("error", "Couldn't check if element is fully in viewport: '".concat(cause.getMessage()));
            throw new InvestingException("Couldn't check if element is fully in viewport!", cause);
        }
    }

    /**
     * Clicking on the given element and handling exceptions if needed.
     *
     * @param element: to click
     */
    public static void clickOnElement(WebElement element) {
        String elementLocator = ByLocatorUtilities.getSelectorFromWebElement(element);
        try {
            Thread.sleep(CLICK);
            element.click();
        } catch (Exception cause) {
            allureAttachText("error", "Couldn't click on " + elementLocator + " element: " + cause);
            throw new InvestingException("[ERROR] Couldn't click on " + elementLocator + " element!", cause);
        }
    }

    /**
     * Clicking on the given element and handling exceptions if needed.
     *
     * @param element: to click
     * @param customMessage: a custom message to specify a name of the element which will be clicked
     */
    public static void clickOnElement(WebElement element, String customMessage) {
        String elementLocator = ByLocatorUtilities.getSelectorFromWebElement(element);
        try {
            allureAttachText("info", "Clicking on ".concat(customMessage));
            Thread.sleep(CLICK);
            element.click();
        } catch (Exception cause) {
            allureAttachText("error", "Couldn't click on ".concat(customMessage).concat(" (".concat(elementLocator).concat(") selector")) + cause);
            throw new InvestingException("[ERROR] Couldn't click on ".concat(customMessage).concat(" (".concat(elementLocator).concat(") selector")), cause);
        }
    }

    /**
     * Double-clicking on the given element and handling exceptions if needed.
     *
     * @param element: to double-click
     */
    public static void doubleClickOnElement(Investing driver, WebElement element) {
        String elementLocator = ByLocatorUtilities.getSelectorFromWebElement(element);
        try {
            new Actions(driver.getDriver())
                    .doubleClick(element)
                    .build()
                    .perform();

        } catch (Exception cause) {
            allureAttachText("error", "Couldn't double-click on " + elementLocator + " element: " + cause);
            throw new InvestingException("[ERROR] Couldn't double-click on " + elementLocator + " element!", cause);
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

    /**
     * Clicking on the given element and waits for element to be present, handling exceptions if needed.
     *
     * @param elementAttr: to click
     * @param customMessage: a custom massage to specify a name of the element which will be clicked
     */
    public static <T> void clickOnVisibleElement(T elementAttr, String customMessage) {
        try {
            allureAttachText("info", "Clicking on ".concat(customMessage));
            WebElement visibleElement = waitForVisibility(CLICK_DURATION, elementAttr);
            visibleElement.click();
        } catch (Exception cause) {
            throw new InvestingException("[ERROR] Couldn't click on ".concat(customMessage)
                    .concat(" (".concat(elementAttr.toString()).concat(") selector")), cause);
        }
    }

    /**
     * Clicking on the given element while it stops to be fenced or timeout finishes
     *
     * @param element  to click
     * @param duration maximum time for waiting
     */
    public static void clickOnNotFencedElement(WebElement element, Duration duration) {
        Supplier<Boolean> isExceptionCaught = () -> {
            try {
                element.click();
                return false;
            } catch (ElementClickInterceptedException e) {
                allureAttachText("warn", "Click on the fenced element: ".concat(e.getMessage()));
            }

            return true;
        };

        boolean isFenced = isExceptionCaught.get();

        while (isFenced && !duration.minus(MINIMAL_WAITING_DURATION).isNegative()) {
            Utilities.sleep(MINIMAL_WAITING_DURATION);
            isFenced = isExceptionCaught.get();
        }

        if (isFenced)
            try {
                element.click();
            } catch (ElementClickInterceptedException cause) {
                throw new InvestingException("Click on the fenced element!", cause);
            }
    }

    /**
     * Perform click by provided coordinates
     *
     * @param driver driver object
     * @param point  point with coordinates to click
     */
    public static void clickByCoordinates(Investing driver, Point point) {
        try {
            Actions action = new Actions(driver.getDriver());
            action.moveByOffset(point.getX(), point.getY())
                    .click()
                    .perform();
        } catch (Exception cause) {
            throw new InvestingException("Couldn't perform click by coordinates!", cause);
        }
    }

    /**
     * Types a given text into a test field, clears previous text if existed
     *
     * @param elementAttr: text field locator or element
     * @param text:        text to type
     */
    public static <T> void type(T elementAttr, String text) {
        try {
            WebElement element = waitForVisibility(elementAttr);

            element.click();
            element.clear();
            element.sendKeys(text);
        } catch (Exception cause) {
            throw new InvestingException("Couldn't send keys into element " + elementAttr, cause);
        }
    }

    /**
     * This method returns the WebElement that was found by the specified By selector(s) including
     * the chained selectors case or the null if the element was not found
     *
     * @param driver          WebDriver object
     * @param elementsLocator one or several selectors
     * @return found WebElement or null
     */
    public static WebElement getElementOrNull(Investing driver, By... elementsLocator) {
        int counter = 0;
        try {
            if (elementsLocator.length > 1) {
                WebElement extElement = driver.findElement(elementsLocator[0]);

                for (int i = 1; i < elementsLocator.length; i++) {
                    counter = i;
                    extElement = extElement.findElement(elementsLocator[i]);
                }
                return extElement;

            } else return driver.findElement(elementsLocator[0]);

        } catch (NoSuchElementException e) {
            StringBuilder selectorsStr = new StringBuilder(elementsLocator[0].toString());

            for (int i = 1; i < counter + 1; i++)
                selectorsStr.append(" -> ").append(elementsLocator[i]);

            allureAttachText("error", "The '" + selectorsStr + "' Element was not found");
            return null;
        }
    }

    /**
     * This method returns the WebElement that was found by the text inside it
     *
     * @param driver WebDriver object
     * @param text   text, which the text inside element
     * @return       found WebElement
     * */
    public static WebElement getElementByText(Investing driver, String text) {
        Log.info( "Check if element with needed text is displayed");

        By elementLocator = By.xpath(String.format("//*[text() = \"%s\"]", text));
        return getElement(driver, elementLocator);
    }

    /**
     * This method returns the WebElement that was found by the specified By selector(s) including
     * the chained selectors case or throws an exception if nothing was found
     *
     * @param driver          WebDriver object
     * @param elementsLocator one or several selectors
     * @return found WebElement
     */
    public static WebElement getElement(Investing driver, By... elementsLocator) {
        int counter = 0;
        try {
            if (elementsLocator.length > 1) {
                WebElement extElement = driver.findElement(elementsLocator[0]);

                for (int i = 1; i < elementsLocator.length; i++) {
                    counter = i;
                    extElement = extElement.findElement(elementsLocator[i]);
                }
                return extElement;

            } else return driver.findElement(elementsLocator[0]);

        } catch (NoSuchElementException cause) {
            StringBuilder selectorsStr = new StringBuilder(elementsLocator[0].toString());

            for (int i = 1; i < counter + 1; i++)
                selectorsStr.append(" -> ").append(elementsLocator[i]);

            allureAttachText("error", "Element not found: '" + selectorsStr + "'");
            throw new NoSuchElementException("[ERROR]: Element not found: '" + selectorsStr + "'", cause);
        }
    }

    public static WebElement getElement(By... elementsLocator) {
        return getElement(ThreadLocalDriver.get(), elementsLocator);
    }

    /**
     * This method returns the list of WebElement that were found by the specified By selector(s) including
     * the chained selectors case or throws an exception if nothing was found
     *
     * @param driver          WebDriver object
     * @param elementsLocator one or several selectors
     * @return list of found web elements
     */
    public static List<WebElement> getElements(Investing driver, By... elementsLocator) {
        int counter = 0;
        List<WebElement> Elements = new ArrayList<>();
        try {
            if (elementsLocator.length > 1) {
                WebElement extElement = driver.findElement(elementsLocator[0]);

                for (int i = 1; i < elementsLocator.length; i++) {
                    counter = i;
                    Elements = extElement.findElements(elementsLocator[i]);
                }
                return Elements;

            } else return driver.findElements(elementsLocator[0]);

        } catch (NoSuchElementException e) {
            StringBuilder selectorsStr = new StringBuilder(elementsLocator[0].toString());

            for (int i = 1; i < counter + 1; i++)
                selectorsStr.append(" -> ").append(elementsLocator[i]);

            allureAttachText("error", "The '" + selectorsStr + "' Elements were not found");
            throw new NoSuchElementException("[ERROR]: The '" + selectorsStr + "' Elements were not found");
        }
    }

    /**
     * Moves the mouse to the middle of the element.
     * The element is scrolled into view if it hasn't been previously
     *
     * @param element to move on
     */
    public static void moveMouseToCenterOfElement(WebElement element) {
        try {
            new Actions(ThreadLocalDriver.get().getDriver()).moveToElement(element).perform();
        } catch (Exception cause) {
            throw new InvestingException("[ERROR] Couldn't move mouse to the center of the element:\n" + element.toString(), cause);
        }
    }

    /**
     * Get child element by locator <br><br>
     * <p>
     * When using xpath, use ".//" to limit your search to the children of this WebElement. <br>
     * More see {@link WebElement#findElement(By)}
     *
     * @param parent element, which contains sought-for
     * @param child  locator of sought-for element. <p>
     * @return child element
     */
    public static WebElement getChildElement(WebElement parent, By child) {
        try {
            return parent.findElement(child);
        } catch (NoSuchElementException cause) {
            throw new NoSuchElementException("[ERROR]: Child element [" + child.toString() + "] from parent [" + ByLocatorUtilities.getLocatorFromElement(parent) +
                    "] wasn't found!", cause);
        }
    }

    /**
     * Get list of child elements by locator<br><br>
     * <p>
     * When using xpath, use ".//" to limit your search to the children of this WebElement. <br>
     * More see {@link WebElement#findElements(By)}
     *
     * @param parent element, which contains sought-for
     * @param child  locator of sought-for elements. <p>
     * @return list of child elements
     */
    public static List<WebElement> getChildElements(WebElement parent, By child) {
        try {
            return parent.findElements(child);
        } catch (Exception cause) {
            throw new InvestingException("[Couldn't get child elements with locator " + child + " from parent " + ByLocatorUtilities.getLocatorFromElement(parent), cause);
        }
    }

    /**
     * Get text from child element by locator <br><br>
     *
     * When using xpath, use ".//" to limit your search to the children of this WebElement. <br>
     * More see {@link WebElement#findElement(By)}
     *
     * @param parent element, which contains sought-for
     * @param child  locator of sought-for element
     * @return text from child element
     */
    public static String getTextFromChildElement(WebElement parent, By child) {
        return getChildElement(parent, child).getText();
    }

    /**
     * Get text from child elements by locator <br><br>
     *
     * When using xpath, use ".//" to limit your search to the children of this WebElement. <br>
     * More see {@link WebElement#findElement(By)}
     *
     * @param parent element, which contains sought-for
     * @param child  locator of sought-for element
     * @return list of texts from child element
     */
    public static List<String> getTextFromChildElements(WebElement parent, By child) {
        List<String> result = new ArrayList<>();

        getChildElements(parent, child).forEach(childElement ->
                result.add(childElement.getText())
        );

        return result;
    }

    /**
     * This method simple returns a random int value withing the specified range
     *
     * @param min value of the range (inclusive)
     * @param max value of the range (exclusive)
     * @return random int within range
     */
    public static int getRandomIndex(int min, int max) {
        return new Random().nextInt(max - min) + min;
    }

    /**
     * Checks if element has a child with given locator
     *
     * @param parent element, which should contains sought-for
     * @param child  locator of sought-for element. <p>
     * @return true, if child element was found
     */
    public static boolean elementContainsChild(WebElement parent, By child) {
        try {
            getChildElement(parent, child);
            return true;
        } catch (NoSuchElementException noSuchElementException) {
            return false;
        }
    }

    /**
     * Finds and clicks button by text
     *
     * @param driver driver object
     * @param text   name of the button in the scenario and on web page
     */
    public static void clickButtonByText(Investing driver, String text) {
        allureAttachText("info", "Click on button with text: " + text);

        By buttonLocator = By.xpath(String.format("//*[normalize-space(.) = \"%s\"]", text));

        try {
            clickOnVisibleElement(buttonLocator);
        } catch (Exception cause) {
            allureAttachText("error", "Couldn't click button, which contains needed text: " + cause);
            throw new InvestingException("Couldn't click button, which contains needed text!", cause);
        }
    }

    /**
     * Returns a random string of letters
     *
     * @param size String size in characters
     */
    public static String getRandomString(int size) {
        return RandomStringUtils.random(size, true, false);
    }


    /**
     * This method performs the click via JS with the specified delay and without Selenium's 'clickable' condition verification
     *
     * @param driver: web driver
     * @param element: web element to click on
     * @param delay: delay before the click and after it in ms
     * @throws InvestingException: any caused by JS code execution
     */
    public static void javaScriptClickWithDelay(Investing driver, WebElement element, int delay) {
        try {
            JavascriptExecutor executor = ((JavascriptExecutor) driver.getDriver());
            Thread.sleep(delay);
            executor.executeScript("arguments[0].click();", element);
            Thread.sleep(delay);

        } catch (Exception cause) {
            allureAttachText("error", "Couldn't click button via JavaScript " + cause);
            throw new InvestingException("Couldn't click button via JavaScript!", cause);
        }
    }

    /**
     * Gets WebElement attribute by attribute name
     *
     * @param element       parent element to get attribute form
     * @param attributeName attribute name
     */
    public static String getElementsAttribute(WebElement element, String attributeName) {
        try {
            return element.getAttribute(attributeName);
        } catch (Exception cause) {
            allureAttachText("error", "Couldn't get element's attribute '" + attributeName + "'" + cause);
            throw new InvestingException("Couldn't get element's attribute '" + attributeName + "'!", cause);
        }
    }

    public static String getCssValue(WebElement element, String propertyName) {
        try {
            return element.getCssValue(propertyName);
        } catch (Exception cause) {
            allureAttachText("error",
                    String.format("Couldn't get element's attribute '%s': %s", propertyName, cause));
            throw new InvestingException(String.format("Couldn't get element's attribute '%s'!", propertyName), cause);
        }
    }

    /**
     * Get cookie from browser by its name
     */
    public static Cookie getCookie(String name) {
        Log.info(String.format("Get cookie with name \"%s\" from browser", name));

        try {
            return ThreadLocalDriver.get().manage().getCookieNamed(name);
        } catch (NullPointerException e) {
            throw new NoSuchCookieException (String.format("Cookie named \"%s\" not found!", name));
        }
    }

    /**
     * Convert cookie from {@link HttpCookie} to {@link Cookie} and set them to WebDriver
     */
    public static void setCookies(HttpCookie... httpCookies) {
        Cookie[] cookies = Arrays.stream(httpCookies)
                .map(httpCookie -> new Cookie(
                        httpCookie.getName(),
                        httpCookie.getValue(),
                        httpCookie.getDomain(),
                        httpCookie.getPath(),
                        Date.from(Instant.now().plusSeconds(httpCookie.getMaxAge())),
                        httpCookie.getSecure(),
                        httpCookie.isHttpOnly()))
                .toArray(Cookie[]::new);

        setCookies(cookies);
    }

    /**
     * Set provided cookies to WebDriver
     */
    public static void setCookies(Cookie... cookies) {
        for (Cookie cookie : cookies)
            step("Setting " + cookie.getName() + " as \"" + cookie.getValue() + "\"", () ->
                    ThreadLocalDriver.get().manage().addCookie(cookie)
            );

        step("Refreshing page", () -> ThreadLocalDriver.get().navigate().refresh());
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

    /** This method performs cursor hovering operation over the specified web element
     *
     * @param driver: web driver
     * @param element: web element over which you want to hover the cursor
     */
    public static void hoverOverElement(Investing driver, WebElement element, String customMessage) {
        String elementLocator = ByLocatorUtilities.getSelectorFromWebElement(element);
        try {
            allureAttachText("info", "Hovering over ".concat(customMessage));
            Actions actions = new Actions(driver.getDriver());
            actions.moveToElement(element).perform();

        } catch (Exception cause) {
            allureAttachText("error", "Couldn't hover over ".concat(customMessage).concat(" (".concat(elementLocator).concat(") selector")) + cause);
            throw new InvestingException("[ERROR] Couldn't hover over ".concat(customMessage).concat(" (".concat(elementLocator).concat(") selector")), cause);
        }
    }

    /**
     * Get value of <code>href</code> attribute from element.
     * If element doesn't contain <code>href</code> attribute, then search for it in nested elements.
     * Exception will be thrown, if more than 1 nested element have <code>href</code> attribute to avoid ambiguity.
     *
     * @param element element to search <code>href</code> attribute nk
     */
    public static String getLinkFromElementOrChild(WebElement element) {
        try {
            String result = getElementsAttribute(element, "href");
            if (result != null)
                return result;
            else {
                List<String> childrenLinks = element.findElements(By.xpath(".//*")).stream()
                        .map(childEl -> getElementsAttribute(childEl, "href"))
                        .filter(Objects::nonNull)
                        .collect(Collectors.toList());

                if (childrenLinks.size() == 1)
                    return childrenLinks.get(0);

                if (childrenLinks.isEmpty())
                    throw new InvalidElementStateException("Element and its nested elements don't have href attribute!");
                else
                    throw new InvalidElementStateException("Element doesn't have href attribute but more than 1 of nested elements have it!");
            }

        } catch (Exception cause) {
            throw new InvestingException("[ERROR] Couldn't get link from " + element.toString() + " element or its nested elements!", cause);
        }
    }

    /** This method checks if the specified element is in the middle of the viewport on the X-axis with respect to the delta value
     *
     * @param element Web element to check
     * @param delta the accepted difference between element's starting point (X-axis) and the calculated point for placing the element in the middle of viewport
     * @return true, if element's starting point minus calculated point is equal or less the delta value, otherwise - false
     */
    public static boolean isElementInTheMiddleOfTheScreen(WebElement element, int delta) {
        try {
            int elementStringPointX = element.getLocation().getX();
            int elementWidth = element.getRect().getWidth();
            int viewportWidth = getViewportWidth(ThreadLocalDriver.get());
            int middleScreenAlignment = (viewportWidth - elementWidth) / 2;

            allureAttachText("info", "Element's starting X point: " + elementStringPointX
                    + "\nElement's width: " + elementWidth + "\nViewport width: " + viewportWidth
                    + "\nMiddle screen alignment X point: " + middleScreenAlignment);

            return Math.abs(elementStringPointX - middleScreenAlignment) <= delta;
        } catch (Exception cause) {
            throw new InvestingException("Unable to check if the element is in the middle of the screen!", cause);
        }
    }

    /** This method checks if the 1st specified element is lower on the screen than the 2nd one
     *
     * @param firstElement web element
     * @param secondElement web element
     * @return true if 1st element's Y coordinate of the upper point is >= 2nd one's
     */
    public static boolean isFirstElementUnderTheSecondElement(WebElement firstElement, WebElement secondElement) {
        try {
            int firstElementUpperYPoint = firstElement.getLocation().getY();
            int secondElementUpperYPoint = secondElement.getLocation().getY() + secondElement.getRect().getHeight();

            allureAttachText("info", "1st Element's upper Y point: " + firstElementUpperYPoint
                    + "\n2nd Element's upper Y point: " + secondElementUpperYPoint);

            return firstElementUpperYPoint >= secondElementUpperYPoint;
        } catch (Exception cause) {
            throw  new InvestingException("Unable to check if the 1st element is lower than the 2nd one!", cause);
        }
    }

    /** This method checks if the 1st element is located within the 2nd one
     *
     * @param firstElement 1st element to check
     * @param secondElement 2nd element to check
     * @return true if starting point (top-left corner) of the 1st element has X and Y values > than X and Y of the 2nd one,
     * and the height and width added to the starting pont of the 1st element are < than the same values of the 2nd one.
     */
    public static boolean isFirstElementWithinTheSecondOne(WebElement firstElement, WebElement secondElement) {
        Point firstElementStartingPoint = firstElement.getLocation(),
                secondElementStartingPoint = secondElement.getLocation();

        Rectangle firstElementRect = firstElement.getRect(),
                secondElementRect = secondElement.getRect();

        return secondElementStartingPoint.getX() < firstElementStartingPoint.getX()
                && secondElementStartingPoint.getY() < firstElementStartingPoint.getY()
                && (firstElementStartingPoint.getX() + firstElementRect.getWidth()
                <= secondElementStartingPoint.getX() + secondElementRect.getWidth())
                && (firstElementStartingPoint.getY() + firstElementRect.getHeight()
                <= secondElementStartingPoint.getY() + secondElementRect.getHeight());
    }

}


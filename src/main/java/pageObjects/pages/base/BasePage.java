package pageObjects.pages.base;

import infrastructure.Investing;
import infrastructure.exceptions.InvestingException;
import infrastructure.threadlocals.ThreadLocalDriver;
import lombok.NonNull;
import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;
import java.util.List;
import java.util.Objects;

import static infrastructure.constants.ConstantProvider.WebConstant.TimeoutDuration.ELEMENT_WAITING_DURATION_FULL;

public class BasePage {
    protected final Investing driver;

    public BasePage() {
        this.driver = ThreadLocalDriver.get();
    }

    public BasePage(Investing driver) {
        this.driver = driver;
        ThreadLocalDriver.put(driver);
    }

    public <T> void clickOn(Investing driver, T elementAttr) {
        try {
            getAndWaitForElement(driver, elementAttr).click();
        } catch (Exception cause) {
            throw new InvestingException("Couldn't click on " + elementAttr, cause);
        }
    }

    public <T> void sendText(Investing driver, T elementAttr, String text) {
        try {
            getAndWaitForElement(driver, elementAttr).sendKeys(text);
        } catch (Exception cause) {
            throw new InvestingException("Couldn't send text into " + elementAttr, cause);
        }
    }

    public <T> String getText(Investing driver, T elementAttr) {
        return getAndWaitForElement(driver, elementAttr).getText();
    }

    public <T> boolean isDisplayed(Investing driver, T elementAttr) {
        try {
            return getAndWaitForElement(driver, elementAttr).isDisplayed();
        } catch (TimeoutException | NoSuchElementException | InvestingException ignore) {}

        return false;
    }

    public <T> boolean isDisplayed(Investing driver, T elementAttr, int timeout) {
        try {
            return getAndWaitForElement(driver, elementAttr, timeout).isDisplayed();
        } catch (TimeoutException | NoSuchElementException | InvestingException ignore) {}

        return false;
    }

    public <T> boolean isDisplayed(Investing driver, T elementAttr, Duration duration) {
        try {
            return getAndWaitForElement(driver, elementAttr, duration).isDisplayed();
        } catch (TimeoutException | NoSuchElementException | InvestingException ignore) {}

        return false;
    }

    protected <T> WebElement getAndWaitForElement(Investing driver, @NonNull T elementAttr, int timeout) {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofMillis(timeout));

        if (elementAttr instanceof By) {
            return Objects.requireNonNull(wait.until(ExpectedConditions.visibilityOfElementLocated((By) elementAttr)));
        } else {
            return Objects.requireNonNull(wait.until(ExpectedConditions.visibilityOf((WebElement) elementAttr)));
        }
    }

    protected <T> WebElement getAndWaitForElement(Investing driver, @NonNull T elementAttr, Duration duration) {
        try {
            WebDriverWait wait = new WebDriverWait(driver, duration);

            if (elementAttr instanceof By) {
                return Objects.requireNonNull(wait.until(ExpectedConditions.visibilityOfElementLocated((By) elementAttr)));
            } else {
                return Objects.requireNonNull(wait.until(ExpectedConditions.visibilityOf((WebElement) elementAttr)));
            }
        } catch (Exception cause) {
            throw new InvestingException("Couldn't get element: " + elementAttr, cause);
        }
    }

    protected <T> WebElement getAndWaitForElement(Investing driver, @NonNull T elementAttr) {
        return getAndWaitForElement(driver, elementAttr, ELEMENT_WAITING_DURATION_FULL);
    }

    protected <T> List<WebElement> getAndWaitForElements(Investing driver, @NonNull T elementAttr, int timeout) {
        try {
            WebDriverWait wait = new WebDriverWait(driver, Duration.ofMillis(timeout));

            if (elementAttr instanceof By) {
                return wait.until(ExpectedConditions.visibilityOfAllElementsLocatedBy((By) elementAttr));
            } else {
                return wait.until(ExpectedConditions.visibilityOfAllElements((WebElement) elementAttr));
            }
        } catch (Exception cause) {
            throw new InvestingException("Couldn't get elements: " + elementAttr, cause);
        }
    }
}

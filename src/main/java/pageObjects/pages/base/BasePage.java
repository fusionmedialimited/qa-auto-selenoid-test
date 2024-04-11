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
import java.util.Objects;

import static infrastructure.constants.ConstantProvider.WebConstant.TimeoutDuration.ELEMENT_WAITING_DURATION_FULL;

public class BasePage {
    protected final Investing driver;

    public BasePage(Investing driver) {
        this.driver = driver;
        ThreadLocalDriver.put(driver);
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

    protected <T> WebElement getAndWaitForElement(Investing driver, @NonNull T elementAttr, Duration duration) {
        try {
            WebDriverWait wait = new WebDriverWait(driver.getDelegate(), duration);

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

}

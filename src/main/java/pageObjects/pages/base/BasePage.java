package pageObjects.pages.base;

import infrastructure.exceptions.InvestingException;
import lombok.NonNull;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;
import java.util.Objects;

import static infrastructure.constants.ConstantProvider.WebConstant.TimeoutDuration.ELEMENT_WAITING_DURATION_FULL;

public class BasePage {
    protected final WebDriver driver;

    public BasePage(WebDriver driver) {
        this.driver = driver;
    }

    public <T> String getText(WebDriver driver, T elementAttr) {
        return getAndWaitForElement(driver, elementAttr).getText();
    }

    protected <T> WebElement getAndWaitForElement(WebDriver driver, @NonNull T elementAttr, Duration duration) {
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

    protected <T> WebElement getAndWaitForElement(WebDriver driver, @NonNull T elementAttr) {
        return getAndWaitForElement(driver, elementAttr, ELEMENT_WAITING_DURATION_FULL);
    }

}

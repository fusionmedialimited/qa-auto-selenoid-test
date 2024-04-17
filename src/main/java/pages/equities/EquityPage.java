
package pages.equities;

import infrastructure.DriverUtilities;
import org.openqa.selenium.By;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.pagefactory.AjaxElementLocatorFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.FluentWait;
import org.openqa.selenium.support.ui.WebDriverWait;
import pages.base.BasePage;

import java.time.Duration;

public class EquityPage extends BasePage {

    @FindBy(tagName = "h1")
    private WebElement pageTitle;

    public EquityPage(WebDriver driver) {
        super(driver);
        PageFactory.initElements(new AjaxElementLocatorFactory(driver, 10), this);
    }

    public String getTitle() {
        return pageTitle.getText();
    }

    public void closePrivacyPopup() {
        FluentWait<WebDriver> wait = new WebDriverWait(this.driver, Duration.ofSeconds(5));
        WebElement popup;

        // wait for popup
        try {
            popup = wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("onetrust-banner-sdk")));
        } catch (TimeoutException ignore) {
            return;
        }

        // click Accept button
        popup.findElement(By.id("onetrust-accept-btn-handler")).click();

        // wait for popup disappearing
        wait.until(ExpectedConditions.invisibilityOf(popup));
    }
}


package pageObjects.pages.equities;

import infrastructure.Investing;
import io.qameta.allure.Allure;
import org.openqa.selenium.*;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.pagefactory.AjaxElementLocatorFactory;
import pageObjects.pages.base.BasePage;

public class BaseInstrumentPage extends BasePage {

    @FindBy(tagName = "h1")
    protected WebElement pageTitle;

    public BaseInstrumentPage(Investing driver) {
        super(driver);
        PageFactory.initElements(new AjaxElementLocatorFactory(driver, 10), this);
    }

    public String getTitle() {
        Allure.addAttachment("Instrument page title", "Get text");
        return getText(this.driver, getAndWaitForElement(driver, pageTitle));
    }
}

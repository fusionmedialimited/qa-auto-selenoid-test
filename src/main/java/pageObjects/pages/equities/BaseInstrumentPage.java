
package pageObjects.pages.equities;

import infrastructure.Investing;
import org.openqa.selenium.*;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.pagefactory.AjaxElementLocatorFactory;
import pageObjects.interfaces.equities.BaseInstrumentMethods;
import pageObjects.pages.base.BasePage;

import static infrastructure.allure.AllureAttachments.allureAttachText;

public class BaseInstrumentPage extends BasePage implements BaseInstrumentMethods {

    @FindBy(tagName = "h1")
    protected WebElement pageTitle;

    public BaseInstrumentPage(Investing driver) {
        super(driver);
        PageFactory.initElements(new AjaxElementLocatorFactory(driver, 10), this);
    }

    public boolean isTitleDisplayed() {
        allureAttachText("Instrument page title", "Check visibility");
        return isDisplayed(driver, pageTitle);
    }

    public String getTitle() {
        allureAttachText("Instrument page title", "Get text");
        return getText(this.driver, getAndWaitForElement(driver, pageTitle));
    }
}

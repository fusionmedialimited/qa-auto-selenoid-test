package stepDefinitions.equities;

import infrastructure.DriverUtilities;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.testng.Assert;
import pages.equities.EquityPage;

public class ACPEquitiesSteps {

    @Given("user on the {} page in {} edition")
    public void aUserOnThePageInEdition(String page, String edition) {
        String url = "https://" + edition + "." + page;
        DriverUtilities.getDriver().get(url);
    }

    @When("user closes Privacy popup")
    public void userClosesPrivacyPopup() {
        EquityPage equityInstrumentPage = new EquityPage(DriverUtilities.getDriver());
        equityInstrumentPage.closePrivacyPopup();
    }

    @Then("company title should be {string} and not stock")
    public void getAndCompareCompanyHeadline(String equityTitle) {
        EquityPage equityInstrumentPage = new EquityPage(DriverUtilities.getDriver());
        Assert.assertEquals(equityInstrumentPage.getTitle(), equityTitle, "Equity titles do not match");
    }
}

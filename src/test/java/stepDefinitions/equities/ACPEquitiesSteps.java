package stepDefinitions.equities;

import infrastructure.DriverUtilities;
import infrastructure.enums.Edition;
import infrastructure.enums.UserStatus;
import io.cucumber.java.ParameterType;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.apache.commons.lang3.EnumUtils;
import org.testng.Assert;
import pages.equities.EquityPage;

import static infrastructure.ConstantProvider.HOME_NO_EDITION_URL;

public class ACPEquitiesSteps {

    @ParameterType("SIGNED_IN|SIGNED_OUT")
    public UserStatus userStatus(String text){
        return UserStatus.valueOf(text);
    }

    @ParameterType(".+")
    public Edition edition(String text) {
        Edition edition = EnumUtils.getEnumIgnoreCase(Edition.class, text);

        if (edition == null)
            throw new IllegalArgumentException("Edition \"" + text + "\" not defined in enum class!");

        return edition;
    }

    @Given("a {userStatus} user on the {} page in {edition} edition")
    public void aUserOnThePageInEdition(UserStatus userStatus, String page, Edition edition) {
        switch (userStatus) {
            case SIGNED_OUT -> {
                String url = "https://"
                        .concat(edition.toString().toLowerCase())
                        .concat(".")
                        .concat(HOME_NO_EDITION_URL).concat(page);
                DriverUtilities.getDriver().get(url);
            }
            default -> throw new RuntimeException("Unexpected value for the user state: ".concat(userStatus.name()));
        }
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

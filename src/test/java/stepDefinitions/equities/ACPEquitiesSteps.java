package stepDefinitions.equities;

import infrastructure.Investing;
import infrastructure.enums.Edition;
import infrastructure.enums.UserStatus;
import infrastructure.exceptions.InvestingException;
import io.cucumber.java.ParameterType;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import org.apache.commons.lang3.EnumUtils;
import org.picocontainer.annotations.Inject;
import org.testng.Assert;
import pageObjects.pages.equities.BaseInstrumentPage;

import static infrastructure.utilities.NavigationUtilities.goToPage;
import static io.qameta.allure.Allure.step;

public class ACPEquitiesSteps {

    @Inject
    Investing investing;

    @Inject
    BaseInstrumentPage equityInstrumentPage;

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

    private void aLoggedOutUser() {
        step("User not logged in");
    }

    @Given("a {userStatus} user on the {} page in {edition} edition")
    public void aUserOnThePageInEdition(UserStatus userStatus, String page, Edition edition) {
        switch (userStatus) {
            case SIGNED_OUT -> {
                aLoggedOutUser();
                goToPage(investing, page, edition);
            }
            default -> throw new InvestingException("Unexpected value for the user state: ".concat(userStatus.name()));
        }
    }

    @Then("company title should be {string} and not stock")
    public void getAndCompareCompanyHeadline(String equityTitle) {
        Assert.assertEquals(equityInstrumentPage.getTitle(), equityTitle, "Equity titles do not match");
    }
}

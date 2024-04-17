package testRuns;

import io.cucumber.testng.AbstractTestNGCucumberTests;
import io.cucumber.testng.CucumberOptions;
import org.testng.annotations.DataProvider;

@CucumberOptions(
        features = {
                "src/test/resources/features.equities"}, //Insert your feature file folder here - example is already inserted
        glue = {"stepDefinitions", "hooks"},
        monochrome = true,
        plugin = {"io.qameta.allure.cucumber6jvm.AllureCucumber6Jvm", "json:reports/Cucumber.json"},
        tags = "@CoreRegression" // insert your specific tag here - example is already inserted
)


public class CoreRegressionTestRun extends AbstractTestNGCucumberTests {

    @Override
    @DataProvider(parallel = true)
    public Object[][] scenarios() {
        return super.scenarios();
    }
}
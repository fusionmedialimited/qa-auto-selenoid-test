package infrastructure;

import infrastructure.constants.WebEnvParams;
import infrastructure.exceptions.InvestingException;
import infrastructure.logger.Log;
import io.github.bonigarcia.wdm.WebDriverManager;
import lombok.Getter;
import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.picocontainer.Disposable;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;
import java.util.Set;

import static infrastructure.CapabilitiesProvider.getCapability;


public class Investing implements WebDriver, Disposable {

    public WebDriver delegate;

    @Getter
    private String remoteSessionId;

    public Investing() {
        String run = WebEnvParams.getRunParam();
        String browser = WebEnvParams.getBrowserParam();
        String version = WebEnvParams.getTagParam();


        switch (run) {
            case "local" -> {
                if (browser.equals("chrome")) {
                    WebDriverManager.chromedriver().setup();
                    delegate = new ChromeDriver((ChromeOptions) getCapability(browser, version, run));
                } else {
                    throw new InvestingException("Couldn't initialize WebDriver instance because of unexpected value in the 'browser' property: " + run);
                }
            }
            case "cloud" -> {
                MutableCapabilities options = getCapability(browser, version, run);
                Log.info("Provided browser options are: " + options.toJson().toString());

                try {
                    delegate = new RemoteWebDriver(new URL("http://selenoid:4444/wd/hub"), options);
                    // session id is parsed right after WebDriver initialization
                    // to avoid errors while attaching video after the test
                    // in case when WebDriver instance isn't acceptable
                    setRemoteSessionId();
                } catch (MalformedURLException e) {
                    Log.error("A malformed URL issue occurred: " + e);
                }
            }

            default -> throw new InvestingException("Couldn't initialize WebDriver instance because of unexpected value in the 'run' property: " + run);
        }
        
        delegate.manage().window().maximize();
    }

    public synchronized WebDriver getDriver() {
        if (delegate == null)
            throw new InvestingException("Couldn't get WebDriver instance: Investing instance is initialized, but WebDriver instance not!");

        return delegate;
    }
    
    private void setRemoteSessionId() {
        try {
            this.remoteSessionId = ((RemoteWebDriver) this.delegate).getSessionId().toString();
        } catch (Exception cause) {
            throw new InvestingException("Couldn't get ID of the Remote WebDriver session!", cause);
        }
    }

    @Override
    public void get(String url) {
        getDriver().get(url);
    }

    @Override
    public String getCurrentUrl() {
        return getDriver().getCurrentUrl();
    }

    @Override
    public String getTitle() {
        return getDriver().getTitle();
    }

    @Override
    public List<WebElement> findElements(By by) {
        return getDriver().findElements(by);
    }

    @Override
    public WebElement findElement(By by) {
        return getDriver().findElement(by);
    }

    @Override
    public String getPageSource() {
        return getDriver().getPageSource();
    }

    @Override
    public void close() {
        getDriver().close();
    }

    @Override
    public void quit() {
        getDriver().quit();
        this.delegate = null;
    }

    @Override
    public Set<String> getWindowHandles() {
        return getDriver().getWindowHandles();
    }

    @Override
    public String getWindowHandle() {
        return getDriver().getWindowHandle();
    }

    @Override
    public TargetLocator switchTo() {
        return getDriver().switchTo();
    }

    @Override
    public Navigation navigate() {
        return getDriver().navigate();
    }

    @Override
    public Options manage() {
        return getDriver().manage();
    }

    @Override
    public void dispose() {
        Log.info("Driver TearDown");

        if (delegate != null)
            delegate.quit();
    }
}
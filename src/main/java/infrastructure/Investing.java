package infrastructure;

import infrastructure.constants.WebEnvParams;
import infrastructure.exceptions.InvestingException;
import infrastructure.listeners.webriver.CommonWebDriveListener;
import infrastructure.logger.Log;
import infrastructure.threadlocals.ThreadLocalDriver;
import infrastructure.threadlocals.ThreadLocalScenario;
import io.github.bonigarcia.wdm.WebDriverManager;
import lombok.Getter;
import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.edge.EdgeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.safari.SafariDriver;
import org.openqa.selenium.safari.SafariOptions;
import org.openqa.selenium.support.events.EventFiringDecorator;
import org.openqa.selenium.support.events.WebDriverListener;
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

        String browser = ThreadLocalScenario.containsTag("@MobileSite")
                ? "chromemobile"
                : WebEnvParams.getBrowserParam();

        String version = ThreadLocalScenario.containsTag("@Profile")
                ? "profile"
                : WebEnvParams.getTagParam();


        switch (run) {
            case "local" -> {
                if (WebEnvParams.getSelenoidLocalParam())
                {
                    MutableCapabilities options = getCapability(browser, version, run);
                    Log.info("Provided browser options are: " + options.toJson().toString());

                    try {
                        delegate = new RemoteWebDriver(new URL("http://localhost:4444/wd/hub"), options);
                    } catch (Exception cause) {
                        throw new InvestingException("Couldn't init remote driver local!", cause);
                    }
                } else switch (browser) {
                    case "chrome", "chromemobile" -> {
                        WebDriverManager.chromedriver().setup();
                        delegate = new ChromeDriver((ChromeOptions) getCapability(browser, version, run));
                    }
                    case "firefox" -> {
                        WebDriverManager.firefoxdriver().setup();
                        delegate = new FirefoxDriver((FirefoxOptions) getCapability(browser, version, run));
                    }
                    case "safari" -> {
                        WebDriverManager.safaridriver().setup();
                        delegate = new SafariDriver((SafariOptions) getCapability(browser, version, run));
                    }
                    case "edge" -> {
                        WebDriverManager.edgedriver().setup();
                        delegate = new EdgeDriver((EdgeOptions) getCapability(browser, version, run));
                    }
                    default -> throw new InvestingException("Couldn't initialize WebDriver instance because of unexpected value in the 'browser' property: " + run);
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

        if (browser.equals("chromemobile"))
            delegate.manage().window().setSize(new Dimension(600, 1000));
        else
            delegate.manage().window().maximize();
    }

    public synchronized WebDriver getDelegate() {
        if (delegate == null)
            throw new InvestingException("Couldn't get WebDriver instance: Investing instance is initialized, but WebDriver instance not!");

        return delegate;
    }

    /**
     * Wrapping is happened using {@link EventFiringDecorator} decorator and provided implementation of {@link WebDriverListener}.
     * Using this wrapped delegate means, that provided listener will catch all events and complete all needed before/after actions.
     *
     * @return wrapped driver instance
     */
    private WebDriver getWrappedDelegate() {
        WebDriverListener listener = new CommonWebDriveListener();
        return new EventFiringDecorator<>(listener).decorate(getDelegate());
    }

    public WebDriver getDriver() {
        return getWrappedDelegate();
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
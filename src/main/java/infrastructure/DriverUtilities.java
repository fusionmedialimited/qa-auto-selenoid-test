package infrastructure;

import io.github.bonigarcia.wdm.WebDriverManager;
import org.openqa.selenium.MutableCapabilities;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.remote.RemoteWebDriver;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.Locale;

import static infrastructure.CapabilitiesProvider.getCapability;

public class DriverUtilities {

    private static final ThreadLocal<WebDriver> driverThreadLocal = new ThreadLocal<>();

    public static void initDriver() {
        String run = (System.getProperty("run") == null || System.getProperty("run").isEmpty())
                ? "local"
                : System.getProperty("run").toLowerCase(Locale.ROOT);

        WebDriver driver = switch (run) {
            case "local" -> {
                WebDriverManager.chromedriver().setup();
                yield new ChromeDriver((ChromeOptions) getCapability(run));
            }
            case "cloud" -> {
                try {
                    MutableCapabilities options = getCapability(run);
                    yield new RemoteWebDriver(new URL("http://selenoid:4444/wd/hub"), options);
                } catch (MalformedURLException e) {
                    throw new RuntimeException(e);
                }
            }
            default -> null;
        };

        driverThreadLocal.set(driver);
        driverThreadLocal.get().manage().window().maximize();
    }

    public static WebDriver getDriver() {
        return driverThreadLocal.get();
    }

    public static void disposeDriver() {
        driverThreadLocal.get().quit();
        driverThreadLocal.remove();
    }
}

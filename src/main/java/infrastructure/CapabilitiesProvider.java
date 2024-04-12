package infrastructure;

import infrastructure.threadlocals.ThreadLocalScenario;
import org.openqa.selenium.PageLoadStrategy;
import org.openqa.selenium.Platform;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.edge.EdgeOptions;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.openqa.selenium.remote.AbstractDriverOptions;
import org.openqa.selenium.remote.CapabilityType;
import org.openqa.selenium.safari.SafariOptions;

import java.util.HashMap;
import java.util.Map;

import static infrastructure.constants.WebEnvParams.getHeadlessParam;

public class CapabilitiesProvider {

    public static AbstractDriverOptions<?> getCapability(String browser, String version, String mode) {
        if (browser.equals("chrome")) {
            ChromeOptions chromeOptions = new ChromeOptions();
            chromeOptions.setCapability(CapabilityType.PLATFORM_NAME, Platform.ANY);
            chromeOptions.setPageLoadStrategy(PageLoadStrategy.EAGER);

            switch (mode) {
                case "local" -> {
                    chromeOptions.addArguments(
                            "--disable-gpu",
                            "--window-size=1920,1200",
                            "--ignore-certificate-errors");

                    if (getHeadlessParam())
                        chromeOptions.addArguments("--headless");

                    chromeOptions.addArguments("--no-sandbox");
                    chromeOptions.addArguments("--disable-dev-shm-usage");
                    chromeOptions.addArguments("--remote-allow-origins=*");
                    chromeOptions.addArguments("--verbose");
                }
                case "cloud" -> {
                    chromeOptions.addArguments("--no-sandbox");
                    chromeOptions.addArguments("--disable-dev-shm-usage");
                    chromeOptions.addArguments("--ignore-certificate-errors");
                    chromeOptions.addArguments("--window-size=1920,1200");
                    chromeOptions.addArguments("--remote-allow-origins=*");
                    chromeOptions.addArguments("--verbose");

                    chromeOptions.setCapability("browserName", browser);
                    chromeOptions.setCapability("browserVersion", version);

                    chromeOptions.setCapability("selenoid:options", new HashMap<String, Object>() {{
                        put("name", ThreadLocalScenario.getName());
                        put("enableLog", true);
                        put("enableVideo", true);
                        put("enableVNC", true);
                    }});
                }
            }

            return chromeOptions;
        }
        throw new IllegalArgumentException("[ERROR] Can't return capabilities for the specified " + browser + " browser " +
                "or/and " + mode + "mode. make sure they are supported");
    }
}
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
        switch (browser) {

            case "firefox": {
                FirefoxOptions firefoxOptions = new FirefoxOptions();
                firefoxOptions.setCapability(CapabilityType.PLATFORM_NAME, Platform.ANY);
                firefoxOptions.setAcceptInsecureCerts(true);

                switch (mode) {
                    case "local" -> {
                        //TODO: add desired capabilities for FireFox in the local mode
                        if (getHeadlessParam())
                            firefoxOptions.addArguments("--headless");

                        return firefoxOptions;
                    }
                    case "cloud" -> {
                        firefoxOptions.setCapability("browserName", browser);
                        firefoxOptions.setCapability("version", version);
                        firefoxOptions.setCapability("enableVNC", true);
                        firefoxOptions.setCapability("enableVideo", true);

                        return firefoxOptions;
                    }
                }
            }
            case "safari": {
                SafariOptions safariOptions = new SafariOptions();
                safariOptions.setPageLoadStrategy(PageLoadStrategy.EAGER);

                switch (mode) {
                    case "local" ->
                            safariOptions.setCapability(CapabilityType.PLATFORM_NAME, Platform.MAC);
                    case "cloud" -> {
                        safariOptions.setCapability(CapabilityType.PLATFORM_NAME, Platform.ANY);
                        safariOptions.setCapability("browserName", browser);
                        safariOptions.setCapability("version", version.equals("latest") ? "15.0" : version);
                        safariOptions.setCapability("enableVNC", true);
                        safariOptions.setCapability("enableVideo", true);
                    }
                }

                return safariOptions;
            }
            case "chrome": {
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
            case "chromemobile": {
                ChromeOptions chromeOptions = new ChromeOptions();
                chromeOptions.setCapability(CapabilityType.PLATFORM_NAME, Platform.ANY);

                Map<String, String> mobileEmulation = new HashMap<>();
                mobileEmulation.put("deviceName", "Samsung Galaxy S20 Ultra");

                chromeOptions.setExperimentalOption("mobileEmulation", mobileEmulation);

                switch (mode) {
                    case "local" -> {
                        chromeOptions.addArguments("--disable-gpu");
                        chromeOptions.addArguments("--ignore-certificate-errors");
                        chromeOptions.addArguments("--no-sandbox");
                        chromeOptions.addArguments("--disable-dev-shm-usage");
                        chromeOptions.addArguments("--remote-allow-origins=*");

                        return chromeOptions;
                    }
                    case "cloud" -> {
                        chromeOptions.addArguments("--no-sandbox");
                        chromeOptions.addArguments("--disable-dev-shm-usage");
                        chromeOptions.addArguments("--ignore-certificate-errors");
                        chromeOptions.addArguments("--remote-allow-origins=*");

                        chromeOptions.setCapability("browserName", "chrome");
                        chromeOptions.setCapability("browserVersion", version);

                        chromeOptions.setCapability("selenoid:options", new HashMap<String, Object>() {{
                            put("name", ThreadLocalScenario.getName());
                            put("enableLog", true);
                            put("enableVideo", true);
                            put("enableVNC", true);
                        }});

                        return chromeOptions;
                    }
                }
            }
            case "edge": {
                EdgeOptions edgeOptions = new EdgeOptions();
                edgeOptions.setCapability(CapabilityType.PLATFORM_NAME, Platform.ANY);

                switch (mode) {
                    case "local" -> {
                        //TODO: add desired capabilities for Edge in the local mode
                        if (getHeadlessParam())
                            edgeOptions.addArguments("--headless");

                        return edgeOptions;
                    }
                    case "cloud" -> {
                        edgeOptions.setCapability("browserName", "MicrosoftEdge");
                        edgeOptions.setCapability("version", version);
                        edgeOptions.setCapability("enableVNC", true);
                        edgeOptions.setCapability("enableVideo", true);

                        return edgeOptions;
                    }
                }
            }
        }
        throw new IllegalArgumentException("[ERROR] Can't return capabilities for the specified " + browser + " browser " +
                "or/and " + mode + "mode. make sure they are supported");
    }
}
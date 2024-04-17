package infrastructure;

import org.openqa.selenium.PageLoadStrategy;
import org.openqa.selenium.Platform;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.remote.AbstractDriverOptions;
import org.openqa.selenium.remote.CapabilityType;

import java.util.HashMap;

public class CapabilitiesProvider {

    public static AbstractDriverOptions<?> getCapability(String mode) {

            ChromeOptions chromeOptions = new ChromeOptions();
            chromeOptions.setCapability(CapabilityType.PLATFORM_NAME, Platform.ANY);
            chromeOptions.setPageLoadStrategy(PageLoadStrategy.EAGER);

            switch (mode) {
                case "local" -> {
                    chromeOptions.addArguments(
                            "--disable-gpu",
                            "--window-size=1920,1200",
                            "--ignore-certificate-errors");

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

                    chromeOptions.setCapability("browserName", "chrome");
                    chromeOptions.setCapability("browserVersion", "latest");

                    chromeOptions.setCapability("selenoid:options", new HashMap<String, Object>() {{
                        put("enableLog", true);
                        put("enableVideo", true);
                        put("enableVNC", true);
                    }});
                }
            }

            return chromeOptions;
    }
}
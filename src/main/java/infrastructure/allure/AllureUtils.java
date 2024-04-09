package infrastructure.allure;

import infrastructure.enums.Module;
import infrastructure.exceptions.InvestingException;
import infrastructure.functional.PredicateDescribed;
import infrastructure.logger.Log;
import infrastructure.utilities.ReflectionUtils;
import okhttp3.Request;
import okhttp3.ResponseBody;
import org.apache.commons.configuration.ConfigurationException;
import org.apache.commons.configuration.PropertiesConfiguration;
import org.apache.commons.lang3.StringUtils;
import org.jetbrains.annotations.NotNull;
import org.openqa.selenium.Capabilities;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.remote.RemoteWebDriver;
import retrofit2.Response;

import java.io.*;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;

import static infrastructure.constants.WebEnvParams.*;
import static infrastructure.enums.Module.WEB;
import static io.qameta.allure.Allure.addAttachment;

public class AllureUtils {

    /** This method creates the environment.properties file in the 'allure-results' folder for the Web or Data-Web modules
     * and writes key-value pairs that will be shown on the allure report widget
     *
     * @param module the Module where you call this method and for which you want to create the environment.properties file
     * @param driver WebDriver object (needed only for Web and Data-Web modules)
     */
    public static void updateAllureEnvParams(Module module, WebDriver driver) {
        final String path = getRunParam().equals("local")
                ? "../" + module.getModule() + "/allure-results/environment.properties"
                : "./allure-results/environment.properties";

        try (FileOutputStream out = new FileOutputStream(path)) {

            PropertiesConfiguration config = new PropertiesConfiguration(path);

            switch (module) {
                case WEB, DATA_WEB -> {
                    Capabilities caps = ((RemoteWebDriver) driver).getCapabilities();

                    config.setProperty("Browser", StringUtils.capitalize(caps.getBrowserName())
                            + " ver. "
                            + caps.getBrowserVersion());
                    config.setProperty("Suite or Tests", module.equals(WEB)
                            ? getWebRunningSuiteOrTests() : "RegistrationFunnelWebTest");
                }
                default -> throw new IllegalStateException("This method supports only Web and Data-Web" +
                        " but the specified module is: " + module);
            }

            config.setProperty("Edition", getEditionParam().toUpperCase());
            config.setProperty("Url", getUrlParam().replaceAll("/", ""));

            config.save();

            Log.info("allure properties file saved");

        } catch (ConfigurationException | IOException e) {
            Log.error("Failure with allure environment file: " + e);
        }
    }

    /** This method creates the environment.properties file in the 'allure-results' folder for the API or Mobile modules
     * and writes key-value pairs that will be shown on the allure report widget
     *
     * @param module the Module where you call this method and for which you want to create the environment.properties file
     */
    public static void updateAllureEnvParams(Module module) {
        final String path = getRunParam().equals("local")
                ? "../" + module.getModule() + "/allure-results/environment.properties"
                : "./allure-results/environment.properties";

        try (FileOutputStream out = new FileOutputStream(path)) {

            PropertiesConfiguration config = new PropertiesConfiguration(path);

            switch (module) {
                case API, MOBILE -> {
                    /* please consider that keys-values you need in the environment.properties except the URL and EDITION */
                }
                default -> throw new IllegalStateException("This method supports only API and Mobile" +
                        " but the specified module is: " + module);
            }

            config.setProperty("Edition", getEditionParam().toUpperCase());
            config.setProperty("Url", getUrlParam().replaceAll("/", ""));

            config.save();

            Log.info("allure properties file saved");

        } catch (ConfigurationException | IOException e) {
            Log.error("Failure with allure environment file: " + e);
        }
    }

    /** This method gets the current run mode for the Web module
     *
     * @return string with the name of the executed suite or test
     */
    public static String getWebRunningSuiteOrTests() {
        if (getSuiteParam().equalsIgnoreCase("run_dev")) {

            String line, values;

            try (BufferedReader br =
                         new BufferedReader(
                                 new InputStreamReader(
                                         new FileInputStream("src/test/java/testRuns/DevTestRunner.java"),
                                         StandardCharsets.UTF_8))) {

                while ((line = br.readLine()) != null) {
                    if (line.contains("tags")) {

                        values = line.trim();
                        values = values.substring(values.indexOf("@"));
                        values = values.substring(0, values.length() - 1);

                        return values;
                    }
                }
            } catch (IOException e) {
                Log.error("Failed to read test tags: " + e);
            }
        }
        return getSuiteParam();
    }

    /**
     * This method returns the URL of the video for the specified session ID
     *
     * @param sessionId ID of the session
     * @return URL of the video
     */
    public static URL getVideoUrl(String sessionId) {
        String videoUrl = "http://selenoid:4444/video/" + sessionId + ".mp4";

        try {
            return new URL(videoUrl);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * This method logs text message and attaches it to Allure report
     *
     * @param loggerClass class, which implements Log4j2 Logger instance
     * @param attachName  the name for the attached block with the text in the report
     * @param message     text to log
     */
    public static void logMessageAndAttachToAllure(Class<?> loggerClass, String attachName, String message) {
        String logMessage =
                "[Allure]["
                        .concat(attachName)
                        .concat("] ")
                        .concat(message).concat("\n");

        PredicateDescribed<Method> predicate =
                definePredicateToExtractLoggingMethod(attachName);

        Method loggingMethod =
                ReflectionUtils.getMethod(loggerClass, predicate);

        try {
            loggingMethod.invoke(null, logMessage);
        } catch (IllegalAccessException | InvocationTargetException cause) {
            throw new InvestingException("Couldn't invoke method from the Logger class " + loggerClass.getName(), cause);
        }

        addAttachment(attachName, message);
    }

    @NotNull
    private static PredicateDescribed<Method> definePredicateToExtractLoggingMethod(String attachName) {
        String logMethodName = switch (attachName.toUpperCase()) {
            case "ERROR" -> "error";
            case "WARN" -> "warn";
            default -> "info";
        };

        return new PredicateDescribed<>(
                "Method with name \"" + logMethodName + "\" and single argument of String type from the Logger class",
                method -> method.getName().equals(logMethodName)
                        && Arrays.equals(method.getParameterTypes(), new Class[]{ String.class })
        );
    }

    /**
     * This method logs API request and attaches it to Allure report
     *
     * @param loggerClass class, which implements Log4j2 Logger instance
     * @param request     API request to log
     */
    public static void logRequestAndAttachToAllure(Class<?> loggerClass, Request request) {
        logMessageAndAttachToAllure(loggerClass, "Sent Request", request.toString());
    }

    /**
     * This method logs initial API response and attaches it to Allure report.
     * <br><br>
     * This method should be used in cases, when you need to work with the raw body from response not only for logging
     * (e.g. together with logging you need to deserialize it)
     *
     * @param loggerClass class, which implements Log4j2 Logger instance
     * @param response    initial API response
     * @param rawBody     parsed body, using {@link Response#body()} or {@link Response#errorBody()} methods
     */
    public static void logResponseAndAttachToAllure(Class<?> loggerClass, Response<ResponseBody> response, String rawBody) {
        String message = response.toString()
                .concat("\n\n")
                .concat("---")
                .concat("\n\n")
                .concat("Body:")
                .concat("\n")
                .concat(rawBody);

        logMessageAndAttachToAllure(loggerClass, "Received Response", message);
    }

    /**
     * This method logs instance of any type using corresponding ToString() method and attaches it to Allure report
     *
     * @param loggerClass class, which implements Log4j2 Logger instance
     * @param instance    instance of any type
     */
    public static <T> void logInstanceAndAttachToAllure(Class<?> loggerClass, T instance) {
        String attachName = ReflectionUtils.getSimpleNamePretty(instance.getClass());
        logMessageAndAttachToAllure(loggerClass, attachName, instance.toString());
    }
}

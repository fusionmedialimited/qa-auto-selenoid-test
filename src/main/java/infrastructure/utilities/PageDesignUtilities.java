package infrastructure.utilities;

import infrastructure.threadlocals.ThreadLocalDriver;
import org.openqa.selenium.JavascriptExecutor;

public class PageDesignUtilities {

    /**
     * This method returns true if the page has new UI design
     *
     */
    public static boolean isNewDesign() {
        JavascriptExecutor js = (JavascriptExecutor) ThreadLocalDriver.get().getDriver();

        try {
            String variableValue = (String) js.executeScript("return page_design;");
            return variableValue.equalsIgnoreCase("refactoring_full_width");

        } catch (Exception e) {
            return false;
        }
    }
}

package infrastructure.allure;

import infrastructure.logger.Log;

public class AllureAttachments {

    /** @see AllureUtils#logMessageAndAttachToAllure */
    public static void allureAttachText(String attachName, String message) {
        AllureUtils.logMessageAndAttachToAllure(Log.class, attachName, message);
    }

    /** @see AllureUtils#logMessageAndAttachToAllure */
    public static void allureAttachText(String message) {
        allureAttachText("info", message);
    }
}

package infrastructure.allure;

import infrastructure.logger.Log;
import okhttp3.Request;
import okhttp3.ResponseBody;
import retrofit2.Response;

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

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

    /** @see AllureUtils#logRequestAndAttachToAllure */
    public static void allureAttachRequest(Request request) {
        AllureUtils.logRequestAndAttachToAllure(Log.class, request);
    }

    /** @see AllureUtils#logResponseAndAttachToAllure */
    public static void allureAttachResponse(Response<ResponseBody> response, String rawBody) {
        AllureUtils.logResponseAndAttachToAllure(Log.class, response, rawBody);
    }

    /** @see AllureUtils#logResponseAndAttachToAllure */
//    public static void allureAttachResponse(Response<ResponseBody> response) {
//        allureAttachResponse(response, GsonDeserializer.getRawBody(response));
//    }

    /** @see AllureUtils#logInstanceAndAttachToAllure */
    public static <T> void allureAttach(T instance) {
        AllureUtils.logInstanceAndAttachToAllure(Log.class, instance);
    }
}

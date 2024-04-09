package infrastructure.threadlocals;

import infrastructure.threadlocals.abstracts.ThreadLocalAbstract;

/**
 * Handles ses_id value
 */
public class ThreadLocalSesId {

    public static final String cookieName = "ses_id";

    private static final ThreadLocalAbstract<String> sesId = new ThreadLocalAbstract<>(cookieName) {};

    public static void put(String value) {
        sesId.put(value);
    }

    public static String get() {
        return sesId.get();
    }

    public static void clear() {
        sesId.clear();
    }
}

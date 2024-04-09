package infrastructure.threadlocals;

import infrastructure.Investing;
import infrastructure.threadlocals.abstracts.ThreadLocalAbstract;

import java.time.Duration;

public class ThreadLocalDriver {
    private static final ThreadLocalAbstract<Investing> driver = new ThreadLocalAbstract<>("Driver instance") {};

    public static synchronized void put(Investing driver) {
        ThreadLocalDriver.driver.put(driver);
    }

    public static synchronized Investing get() {
        return driver.get();
    }

    public static void clear() {
        driver.clear();
    }

    public static Duration getImplicitWait() {
        return get().manage().timeouts().getImplicitWaitTimeout();
    }

    public static void setImplicitWait(Duration duration) {
        get().manage().timeouts().implicitlyWait(duration);
    }

    public static boolean exists() {
        return get() != null;
    }

    public static String getRemoteSessionId() {
        return get().getRemoteSessionId();
    }
}

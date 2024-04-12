package infrastructure.threadlocals;

import infrastructure.Investing;
import infrastructure.threadlocals.abstracts.ThreadLocalAbstract;

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
}

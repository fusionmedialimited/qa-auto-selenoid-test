package infrastructure.threadlocals;

import infrastructure.constants.WebEnvParams;
import infrastructure.enums.Edition;
import infrastructure.threadlocals.abstracts.ThreadLocalAbstract;

/**
 * Handles Edition value
 */
public class ThreadLocalEdition {

    private static final ThreadLocalAbstract<Edition> edition = new ThreadLocalAbstract<>("Edition", WebEnvParams.getEditionParamAsEnum()) {};

    public static Edition get() {
        return edition.get();
    }

    public static void set(Edition desiredEdition) {
        edition.put(desiredEdition);
    }

    public static void clear() {
        edition.clear();
    }


}

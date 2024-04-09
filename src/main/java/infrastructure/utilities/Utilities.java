package infrastructure.utilities;

import infrastructure.exceptions.InvestingException;
import infrastructure.logger.Log;
import lombok.extern.log4j.Log4j2;

import java.time.Duration;

/**
 * Utilities has different general utilities methods that could be used in each squad.
 *
 * @author Nael Marwan
 */
@Log4j2
public class Utilities {

    /**
     * Sleep by given duration
     */
    public static void sleep(Duration duration) {
        Log.info(String.format("Sleeping for %d seconds", duration.getSeconds()));
        try {
            Thread.sleep(duration.toMillis());
        } catch (InterruptedException cause) {
            throw new InvestingException("Couldn't sleep for " + duration.getSeconds() + " seconds!", cause);
        }
    }

}
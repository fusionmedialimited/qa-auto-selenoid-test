package infrastructure.utilities;

import infrastructure.exceptions.InvestingException;
import infrastructure.logger.Log;
import lombok.extern.log4j.Log4j2;
import okhttp3.ResponseBody;
import org.apache.commons.codec.digest.DigestUtils;
import org.eclipse.jetty.util.HttpCookieStore;
import retrofit2.Response;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.CookieStore;
import java.net.HttpCookie;
import java.net.URL;
import java.security.SecureRandom;
import java.time.Duration;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.TimeUnit;

/**
 * Utilities has different general utilities methods that could be used in each squad.
 *
 * @author Nael Marwan
 */
@Log4j2
public class Utilities {

    /**
     * Get random number
     *
     * @param min
     * @param max
     * @return An int Number between min and max
     */
    public static int getRandomNumber(int min, int max) {
        return ThreadLocalRandom.current().nextInt((max - min) + 1) + min;
    }

    /**
     * Get random number
     *
     * @param min
     * @param max
     * @param exceptValues list of values, which should be regenerated
     * @return An int Number between min and max
     */
    public static int getRandomNumber(int min, int max, List<Integer> exceptValues) {
        int result;

        do result = getRandomNumber(min, max);
        while (exceptValues.contains(result));

        return result;
    }

    /**
     * Get random string
     *
     * @param len
     * @return A String by given length
     */
    public static String getRandomString(int len) {
        return getRandomStringOfSymbols(len, "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz");
    }

    /**
     * Get random string with numbers
     *
     * @param len
     * @return A String by given length
     */
    public static String getRandomStringWithNumbers(int len) {
        return getRandomStringOfSymbols(len, "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789");
    }

    /**
     * Get random string with cyrillic symbols
     *
     * @param len Expected length
     * @return A String by given length
     */
    public static String getRandomCyrillic(int len) {
        return getRandomStringOfSymbols(len, "АаБбВвГгДдЕеЁёЖжЗзИиЙйКкЛлМмНнОоПпРрСсТтУуФфХхЦцЧчШшЩщЪъЫыЬьЭэЮюЯя");
    }

    /**
     * Get random string with cyrillic symbols
     *
     * @param len Expected length
     * @return A String by given length
     */
    public static String getRandomHieroglyphs(int len) {
        return getRandomStringOfSymbols(len, "성수末本채민응걸현물健파조김");
    }

    /**
     * Get random string based on symbols
     *
     * @param len     Expected length
     * @param symbols String like "ABCD123" etc.
     * @return A String by given length
     */
    public static String getRandomStringOfSymbols(int len, String symbols) {
        SecureRandom rnd = new SecureRandom();

        StringBuilder sb = new StringBuilder(len);

        for (int i = 0; i < len; i++)
            sb.append(symbols.charAt(rnd.nextInt(symbols.length())));

        return sb.toString();
    }

    /**
     * Get random long from range
     *
     * @param min Minimum value of a range
     * @param max Maximum value of a range
     * @return Random Long value
     */
    public static Long getRandomLong(Long min, Long max) {
        return ThreadLocalRandom.current().nextLong(min, max);
    }

    /**
     * Get random hash without dashes
     *
     * @return A String looked like hash
     */
    public static String getRandomHash() {
        return UUID.randomUUID().toString().replace("-", "");
    }

    /**
     * Create valid (non-exists) gmail address with exact length of random string
     *
     * @param lengthOfRandomPart Length of required string including domain and 'qa.' prefix
     * @return Email string
     */
    public static String generateEmail(int lengthOfRandomPart) {
        String str;
        if (lengthOfRandomPart > 0) {
            str = getRandomString(lengthOfRandomPart);
        } else {
            str = getRandomString(4);
            log.warn("Default length of random part (4) of mail has been used. Check input argument");
        }
        return "qa.".concat(str).concat("@investing.com");
    }

    /**
     * Create valid (non-exists) gmail address with exact length of random string and 2nd prefix
     *
     * @param lengthOfRandomPart Length of the email's part, which will be generated randomly
     * @param prefix part of address, located between "qa." prefix and random part
     * @return Email string
     */
    public static String generateEmail(int lengthOfRandomPart, String prefix) {
        String randomStr = getRandomString(lengthOfRandomPart);
        return "qa.".concat(prefix).concat(randomStr).concat("@investing.com");
    }

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

    /**
     * Sleep by given unit
     */
    public static void sleep(int time, TimeUnit unit) {
        sleep(Duration.of(time, unit.toChronoUnit()));
    }

    /**
     * Get external IP
     *
     * @return IP as a String
     */
    public static String getExternalIP() {
        try {
            URL whatismyip = new URL("http://checkip.amazonaws.com");
            BufferedReader in = new BufferedReader(new InputStreamReader(whatismyip.openStream()));
            return in.readLine(); //return the IP as a String
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * MD5 is a widely used cryptographic hash function, which produces a hash of 128 bit.
     * Return a hash for a string.
     *
     * @param stringToHash
     * @return Hashed String
     */
    public static String getMD5HashByString(String stringToHash) {
        return DigestUtils.md5Hex(stringToHash).toUpperCase();
    }

    /**
     * Parse cookies from response
     *
     * @param response response with cookies
     * @return storage with all parsed cookies
     */
    public static HttpCookie grabCookie(Response<ResponseBody> response, String cookieName) {
        CookieStore cookieStore = new HttpCookieStore();
        response.headers().values("set-cookie").forEach(header ->
                cookieStore.add(null, HttpCookie.parse(header).get(0))
        );

        return cookieStore.getCookies().stream()
                .filter(c -> (c.getName().equals(cookieName) && !c.getValue().isEmpty()))
                .findAny()
                .orElseThrow(() -> new InvestingException(
                        String.format("\"%s\" cookie not found or has empty value!", cookieName)));


    }
}
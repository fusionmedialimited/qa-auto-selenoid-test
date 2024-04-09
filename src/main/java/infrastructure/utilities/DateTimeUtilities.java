package infrastructure.utilities;

import org.joda.time.DateTime;

import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.Clock;
import java.time.Instant;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeFormatterBuilder;
import java.time.temporal.ChronoUnit;
import java.util.Calendar;
import java.util.Date;

public class DateTimeUtilities {

    /** This method returns Unix Time Stamp value with an offset, if specified
     *
     * @param offsetValue the offset value
     * @param offsetTimeUnit ChronoUnits (months, days, hours, minutes...)
     * @return Unix Time Stamp value - offset
     */
    public static String getUnixTimeStampWithOffset(int offsetValue, ChronoUnit offsetTimeUnit) {
        Instant instant = Instant.now(Clock.systemUTC()).minus(offsetValue, offsetTimeUnit);
        return String.valueOf(instant.getEpochSecond());
    }

    /**
     * Get date with given format
     *
     * @param pattern     pattern for format of returning date
     * @param offsetDays  number of days from today, e.g.:
     *                     <ul>
     *                         <il><b>0</b> for current day</il><br>
     *                         <il><b>1</b> for tomorrow</il><br>
     *                         <il><b>-1</b> for yesterday</il><br>
     *                         <il>etc.<il>
     *                     </ul>
     * @param offsetMonth number of months from current month
     * @param offsetYears number of years from current year
     * @return date with given format
     */
    public static String getDate(String pattern, int offsetDays, int offsetMonth, int offsetYears) {
        DateFormat dateFormat = new SimpleDateFormat(pattern);
        Calendar cal = Calendar.getInstance();
        if (offsetDays != 0) {
            cal.add(Calendar.DATE, offsetDays);
        }
        if (offsetMonth != 0) {
            cal.add(Calendar.MONTH, offsetMonth);
        }
        if (offsetYears != 0) {
            cal.add(Calendar.YEAR, offsetYears);
        }
        return dateFormat.format(cal.getTime());
    }

    /**
     * Get today's date with given format
     *
     * @param pattern          pattern for format of returning date
     * @return date with given format
     */
    public static String getCurrentDate(String pattern) {
        return getDate(pattern, 0, 0 ,0);
    }

    /**
     * Get current timestamp
     *
     * @return Rounded for millis current time as java.sql.Timestamp object
     */
    public static Timestamp getCurrentTimestamp() {
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.MILLISECOND, 0);
        return new Timestamp(cal.getTime().getTime());
    }


    /**
     * Parses the given date string using the specified date format and returns a LocalDate object.
     *
     * @param dateString the date string to parse
     * @param pattern    the date format string, which should be in the pattern expected by the DateTimeFormatter class
     * @return a LocalDate object representing the parsed date string
     */
    public static LocalDate parseDate(String dateString, String pattern) {
        DateTimeFormatter formatter = new DateTimeFormatterBuilder().parseCaseInsensitive().appendPattern(pattern).toFormatter();
        return LocalDate.parse(dateString, formatter);
    }

    /**
     * Returns the current system date as a LocalDate object.
     *
     * @return The LocalDate object that represents the current system date.
     */
    public static LocalDate getCurrentLocalDate() {
        return LocalDate.now();
    }

    public static DateTime parseDateTimeFromString(String strToParse, org.joda.time.format.DateTimeFormatter dateTimeFormatter) {
        return DateTime.parse(strToParse, dateTimeFormatter);
    }

    public static String formatTimestampToHHmmss(Date timestamp) {
        SimpleDateFormat formatter = new SimpleDateFormat("HH:mm:ss");
        return formatter.format(timestamp);
    }
}

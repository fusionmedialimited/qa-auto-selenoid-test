package infrastructure.utilities;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;

public class DateTimeUtilities {

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

}

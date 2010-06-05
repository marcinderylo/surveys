package org.adaptiveplatform.surveys.utils;

import java.util.Date;

import org.joda.time.DateTime;

/**
 *
 * @author Marcin Deryło
 */
public final class DateTimeUtils {

    private DateTimeUtils() {
    }

    /**
     * @param date conversion target
     * @return DateTime equivalent of input parameter or {@code null}, if null
     * has been passed in.
     */
    public static DateTime asDateTime(Date date) {
        DateTime dateTime = null;
        if (date != null) {
            dateTime = new DateTime(date.getTime());
        }
        return dateTime;
    }
}

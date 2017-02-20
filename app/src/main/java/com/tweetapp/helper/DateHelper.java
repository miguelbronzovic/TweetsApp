package com.tweetapp.helper;

import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
import org.joda.time.Period;

/**
 * Created by Miguel Bronzovic.
 */
public final class DateHelper {
    private DateHelper() {}

    /**
     * Shows the elapsed time between input DateTime and now. </br>
     *
     * @param inputDateTime
     * @param now
     * @return String difference in days, hours, minutes or seconds
     */
    public static String calculateElapsedTimeString(DateTime inputDateTime, DateTime now) {
        final Period p = new Period(inputDateTime.toDateTime(DateTimeZone.getDefault()).getMillis(), now.toDateTime(DateTimeZone.getDefault()).getMillis());
        final int days = p.getDays();
        final int hours = p.getHours();
        final int minutes = p.getMinutes();

        if (days > 1) {
            return String.format("%sd", days);
        } else if ((hours <= 24) && (hours > 1)) {
            return String.format("%sh", hours);
        } else if ((minutes <= 60) && (minutes > 1)) {
            return String.format("%sm", minutes);
        } else {
            return "0s";
        }
    }

}

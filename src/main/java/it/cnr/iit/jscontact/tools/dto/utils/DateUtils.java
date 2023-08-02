package it.cnr.iit.jscontact.tools.dto.utils;

import ezvcard.util.VCardDateFormat;
import it.cnr.iit.jscontact.tools.dto.PartialDate;
import org.apache.commons.lang3.StringUtils;

import java.time.*;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;
import java.util.Date;

/**
 * Utility class for handling date time formats.
 *
 * @author Mario Loffredo
 */
public class DateUtils {

    public enum DateTimeType {
      UTC_TIME,
      LOCAL_TIME,
      NON_ZERO_TIME
    }


    /**
     * Returns the text conversion of a date according to the ISO-8601 instant format such as '2011-12-03T10:15:30Z'.
     * @param date the date represented as java.util.Date object
     * @return the text conversion
     */
    public static String toString(Date date) {

        Calendar cal = Calendar.getInstance();
        cal.setTime(date);

        LocalDateTime ldt = LocalDateTime.ofInstant(date.toInstant(), ZoneId.of(cal.getTimeZone().getID()));
        ZonedDateTime zdt = ZonedDateTime.of(ldt,ZoneId.of(cal.getTimeZone().getID()));
        return DateTimeFormatter.ISO_INSTANT.format(zdt);

    }

    private static boolean hasTime(Calendar cal) {

        return (cal.get(Calendar.HOUR_OF_DAY) > 0) ||
               (cal.get(Calendar.MINUTE) > 0) ||
               (cal.get(Calendar.SECOND) > 0) ||
               (cal.get(Calendar.MILLISECOND) > 0);
    }

    private static boolean hasUTCTime(Calendar cal) {

        return hasTime(cal) && cal.getTimeZone()!=null && (cal.getTimeZone().getID().equals("GMT") || cal.getTimeZone().getID().equals("UTC"));
    }

    private static ZoneOffset getZoneOffset(Calendar cal) {

        int gmtOffset = cal.get(Calendar.ZONE_OFFSET);
        int hours = gmtOffset / (60 * 60 * 1000);
        int minutes = Math.abs((gmtOffset % hours) / (60 * 1000));
        return ZoneOffset.ofHoursMinutes(hours, minutes);
    }


    /**
     * Converts a java.util.Calendar object into text according to the NON_ZERO_TIME date time type.
     * @param calendar the Calendar object
     * @return the text representing the Calendar object
     */
    public static String toString(Calendar calendar) {
        if (hasUTCTime(calendar))
            return toString(calendar, DateTimeType.UTC_TIME);
        else
            return toString(calendar, DateTimeType.NON_ZERO_TIME);
    }

    /**
     * Converts a java.util.Calendar object into text according to a given date time type.
     * @param calendar the Calendar object
     * @param dateTimeType the date time type (i.e. UTC, LOCAL_TIME, NON_ZERO_TIME)
     * @return the text representing the Calendar object
     */
    public static String toString(Calendar calendar, DateTimeType dateTimeType) {

        if (calendar == null)
            return null;

        if (dateTimeType!=DateTimeType.NON_ZERO_TIME || hasTime(calendar)) {
            int offset = calendar.get(Calendar.ZONE_OFFSET);
            if (offset == 0) {
                return LocalDateTime.of(calendar.get(Calendar.YEAR),
                        calendar.get(Calendar.MONTH) + 1,
                        calendar.get(Calendar.DAY_OF_MONTH),
                        calendar.get(Calendar.HOUR_OF_DAY),
                        calendar.get(Calendar.MINUTE),
                        calendar.get(Calendar.SECOND)
                ).format(DateTimeFormatter.ISO_LOCAL_DATE_TIME)+((dateTimeType==DateTimeType.UTC_TIME) ? "Z" : StringUtils.EMPTY);
            } else {
                OffsetDateTime offsetDateTime = OffsetDateTime.of(calendar.get(Calendar.YEAR),
                        calendar.get(Calendar.MONTH) + 1,
                        calendar.get(Calendar.DAY_OF_MONTH),
                        calendar.get(Calendar.HOUR_OF_DAY),
                        calendar.get(Calendar.MINUTE),
                        calendar.get(Calendar.SECOND),
                        0,
                        getZoneOffset(calendar)
                );
                switch (dateTimeType) {
                    case UTC_TIME:
                        return LocalDateTime.ofInstant(offsetDateTime.toInstant(), ZoneOffset.UTC).format(DateTimeFormatter.ISO_LOCAL_DATE_TIME) + "Z";
                    case LOCAL_TIME:
                        return offsetDateTime.format(DateTimeFormatter.ISO_LOCAL_DATE_TIME);
                    default:
                        return offsetDateTime.format(DateTimeFormatter.ISO_OFFSET_DATE_TIME);
                }
            }
        } else {
            return LocalDate.of(calendar.get(Calendar.YEAR),
                    calendar.get(Calendar.MONTH) + 1,
                    calendar.get(Calendar.DAY_OF_MONTH)
            ).format(DateTimeFormatter.ISO_LOCAL_DATE);
        }
    }

    /**
     * Converts an Ezvcard partial date into a JSContact partial date text format.
     * @param partialDate the partial date as an Ezvcard PartialDate object
     * @return the Ezvcard partial date in text format
     */
    public static String toJSContactPartialDateText(PartialDate partialDate) {

        String year = (partialDate.getYear() != null) ? String.format("%04d",partialDate.getYear()) : "0000";
        String month = (partialDate.getMonth() != null) ? String.format("%02d",partialDate.getMonth()) : "00";
        String day = (partialDate.getDay() != null) ? String.format("%02d",partialDate.getDay()) : "00";
        return String.format("%s-%s-%s", year, month, day);
    }

    /**
     * Converts date in text format into a java.util.Calendar object.
     * @param date a date in text format
     * @return the Calendar object
     */
    public static Calendar toCalendar(String date) {
        return VCardDateFormat.parseAsCalendar(date);
    }

    /**
     * Converts the timestamp as a Calendar object in the VCard timestamp string.
     * @param timestamp a timestamp as a Calendar object
     * @return VCard timestamp string
     */
    public static String toVCardTimestamp(Calendar timestamp) {

        if (timestamp == null)
            return null;

        String result = "";
        String timestampAsString = toString(timestamp);
        String items[] = timestampAsString.split("T");
        result = result + items[0].replace("-", StringUtils.EMPTY) + "T";
        result = result + items[1].replace(":", StringUtils.EMPTY);

        return result;
    }

}

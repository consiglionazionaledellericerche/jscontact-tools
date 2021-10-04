package it.cnr.iit.jscontact.tools.dto.utils;

import ezvcard.util.PartialDate;
import ezvcard.util.VCardDateFormat;

import java.time.*;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;
import java.util.Date;

public class DateUtils {

    public enum DateTimeType {
      UTC,
      LOCAL,
      NON_ZERO_TIME
    };


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

    private static ZoneOffset getZoneOffset(Calendar cal) {

        int gmtOffset = cal.get(Calendar.ZONE_OFFSET);
        int hours = gmtOffset / (60 * 60 * 1000);
        int minutes = Math.abs((gmtOffset % hours) / (60 * 1000));
        return ZoneOffset.ofHoursMinutes(hours, minutes);
    }

    public static String toString(Calendar cal) {
        return toString(cal, DateTimeType.NON_ZERO_TIME);
    }

    public static String toString(Calendar cal, DateTimeType dateTimeType) {

        if (cal == null)
            return null;

        if (dateTimeType!=DateTimeType.NON_ZERO_TIME || hasTime(cal)) {
            int offset = cal.get(Calendar.ZONE_OFFSET);
            if (offset == 0) {
                return LocalDateTime.of(cal.get(Calendar.YEAR),
                        cal.get(Calendar.MONTH) + 1,
                        cal.get(Calendar.DAY_OF_MONTH),
                        cal.get(Calendar.HOUR_OF_DAY),
                        cal.get(Calendar.MINUTE),
                        cal.get(Calendar.SECOND)
                ).format(DateTimeFormatter.ISO_LOCAL_DATE_TIME)+((dateTimeType==DateTimeType.UTC) ? "Z" : "");
            } else {
                OffsetDateTime offsetDateTime = OffsetDateTime.of(cal.get(Calendar.YEAR),
                        cal.get(Calendar.MONTH) + 1,
                        cal.get(Calendar.DAY_OF_MONTH),
                        cal.get(Calendar.HOUR_OF_DAY),
                        cal.get(Calendar.MINUTE),
                        cal.get(Calendar.SECOND),
                        0,
                        getZoneOffset(cal)
                );
                switch (dateTimeType) {
                    case UTC:
                        return LocalDateTime.ofInstant(offsetDateTime.toInstant(), ZoneOffset.UTC).format(DateTimeFormatter.ISO_LOCAL_DATE_TIME) + "Z";
                    case LOCAL:
                        return offsetDateTime.format(DateTimeFormatter.ISO_LOCAL_DATE_TIME);
                    default:
                        return offsetDateTime.format(DateTimeFormatter.ISO_OFFSET_DATE_TIME);
                }
            }
        } else {
            return LocalDate.of(cal.get(Calendar.YEAR),
                    cal.get(Calendar.MONTH) + 1,
                    cal.get(Calendar.DAY_OF_MONTH)
            ).format(DateTimeFormatter.ISO_LOCAL_DATE);
        }
    }


    public static String toVCardPartialDateText(String jsContactPartialDateText) {

        String year = jsContactPartialDateText.substring(0,4);
        String month = jsContactPartialDateText.substring(5,7);
        String day = jsContactPartialDateText.substring(9);
        return String.format("%s%s%s", (year.equals("0000") ? "--" : year),
                                       (month.equals("00") ? "-" : month),
                                       (day.equals("00") ? "-" : day));
    }

    public static String toJSContactPartialDateText(PartialDate partialDate) {

        String year = (partialDate.getYear() != null) ? String.format("%04d",partialDate.getYear()) : "0000";
        String month = (partialDate.getMonth() != null) ? String.format("%02d",partialDate.getMonth()) : "00";
        String day = (partialDate.getDate() != null) ? String.format("%02d",partialDate.getDate()) : "00";
        return String.format("%s-%s-%s", year, month, day);
    }


    public static Calendar toCalendar(String date) {
        return VCardDateFormat.parseAsCalendar(date);
    }
}

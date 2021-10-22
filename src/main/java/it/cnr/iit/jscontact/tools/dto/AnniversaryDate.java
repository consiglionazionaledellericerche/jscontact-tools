package it.cnr.iit.jscontact.tools.dto;

import ezvcard.util.PartialDate;
import it.cnr.iit.jscontact.tools.dto.utils.DateUtils;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Calendar;

/**
 * Class mapping the "date" property values of the Anniversary type as defined in section 2.6.1 of [draft-ietf-jmap-jscontact].
 *
 * @see <a href="https://datatracker.ietf.org/doc/draft-ietf-jmap-jscontact#section-2.6.1">draft-ietf-jmap-jscontact</a>
 * @author Mario Loffredo
 */
@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class AnniversaryDate implements Serializable {

    Calendar date;
    PartialDate partialDate;

    /**
     * Tests if this annivesary date is equal to the given date in text format.
     *
     * @param text the date in text format
     * @return true if this anniversary date is equal to the given date in text format, false otherwise
     * @see <a href="https://datatracker.ietf.org/doc/draft-ietf-jmap-jscontact#section-2.6.1">draft-ietf-jmap-jscontact</a>
     */
    public boolean isEqual(String text) {

        if (this.date != null)
          return (this.date.compareTo(DateUtils.toCalendar(text))==0);
        else
          return DateUtils.toJSContactPartialDateText(partialDate).equals(text);
    }

    /**
     * Return the annivesary date corresponding to the given date in text format.
     *
     * @param text the date in text format
     * @return the anniversary date corresponding to the given date in text format
     * @see <a href="https://datatracker.ietf.org/doc/draft-ietf-jmap-jscontact#section-2.6.1">draft-ietf-jmap-jscontact</a>
     */
    public static AnniversaryDate parse(String text) {

        try {
            Calendar date = DateUtils.toCalendar(text);
            return AnniversaryDate.builder().date(date).build();
        } catch (Exception e) {
            try {
                PartialDate partialDate = PartialDate.parse(DateUtils.toVCardPartialDateText(text));
                return AnniversaryDate.builder().partialDate(partialDate).build();
            } catch (Exception e1) {
                return null;
            }
        }

    }
}

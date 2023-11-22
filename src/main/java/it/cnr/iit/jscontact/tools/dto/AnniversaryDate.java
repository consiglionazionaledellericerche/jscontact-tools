package it.cnr.iit.jscontact.tools.dto;

import it.cnr.iit.jscontact.tools.constraints.NotNullAnyConstraint;
import it.cnr.iit.jscontact.tools.dto.utils.DateUtils;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.Valid;
import java.io.Serializable;

/**
 * Class mapping the "date" property values of the Anniversary type as defined in section 2.8.1 of [draft-ietf-calext-jscontact].
 *
 * @see <a href="https://datatracker.ietf.org/doc/draft-ietf-calext-jscontact#section-2.8.1">draft-ietf-calext-jscontact</a>
 * @author Mario Loffredo
 */
@NotNullAnyConstraint(fieldNames = {"date", "partialDate"}, message = "at least one not null member between date and partialDate is required in AnniversaryDate")
@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class AnniversaryDate implements Serializable {

    @Valid
    Timestamp date;
    @Valid
    PartialDate partialDate;

    /**
     * Tests if this annivesary date is equal to the given date in text format.
     *
     * @param text the date in text format
     * @return true if this anniversary date is equal to the given date in text format, false otherwise
     * @see <a href="https://datatracker.ietf.org/doc/draft-ietf-calext-jscontact#section-2.8.1">draft-ietf-calext-jscontact</a>
     */
    public boolean isEqual(String text) {

        if (this.date != null)
          return (this.date.getUtc().compareTo(DateUtils.toCalendar(text))==0);
        else
          return DateUtils.toJSContactPartialDateText(partialDate).equals(text);
    }

}

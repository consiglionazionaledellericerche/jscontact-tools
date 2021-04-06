package it.cnr.iit.jscontact.tools.dto;

import ezvcard.util.PartialDate;
import ezvcard.util.VCardDateFormat;
import it.cnr.iit.jscontact.tools.dto.utils.DateUtils;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Calendar;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class AnniversaryDate {

    Calendar date;
    PartialDate partialDate;

    private static boolean isEqual(PartialDate pd1, PartialDate pd2) {

        if ((pd1 == null && pd2 != null) || (pd1 != null && pd2 == null))
            return false;

        if (pd1 == null && pd2 == null)
            return true;

        return (pd1.getYear() == pd2.getYear()) &&
                (pd1.getMonth() == pd2.getMonth()) &&
                (pd1.getDate() == pd2.getDate());
    }

    public boolean isEqual(String text) {

        if (date != null)
          return (date.compareTo(VCardDateFormat.parseAsCalendar(text))==0);
        else
          return DateUtils.toJSContactPartialDateText(partialDate).equals(text);
    }

}

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

    public boolean isEqual(String text) {

        if (date != null)
          return (date.compareTo(VCardDateFormat.parseAsCalendar(text))==0);
        else
          return DateUtils.toJSContactPartialDateText(partialDate).equals(text);
    }

}

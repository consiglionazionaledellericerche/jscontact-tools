package it.cnr.iit.jscontact.tools.dto;

import ezvcard.util.PartialDate;
import it.cnr.iit.jscontact.tools.dto.utils.DateUtils;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Calendar;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class AnniversaryDate implements Serializable {

    Calendar date;
    PartialDate partialDate;

    public boolean isEqual(String text) {

        if (date != null)
          return (date.compareTo(DateUtils.toCalendar(text))==0);
        else
          return DateUtils.toJSContactPartialDateText(partialDate).equals(text);
    }
}

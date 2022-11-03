package it.cnr.iit.jscontact.tools.dto;

import lombok.*;

import java.util.Calendar;

/**
 * Class mapping the TimeZoneRule type as defined in section 4.7.2 of [RFC8984].
 *
 * @see <a href="https://datatracker.ietf.org/doc/rfc8984#section-4.7.2">RFC8984</a>
 * @author Mario Loffredo
 */
@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class TimeZoneRule {

    Calendar start;

    String offsetFrom;

    String offsetTo;

}

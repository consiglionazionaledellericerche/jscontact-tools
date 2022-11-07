package it.cnr.iit.jscontact.tools.dto;

import lombok.*;

import java.util.Calendar;
import java.util.List;

/**
 * Class mapping the TimeZone type as defined in section 4.7.2 of [RFC8984].
 *
 * @see <a href="https://datatracker.ietf.org/doc/rfc8984#section-4.7.2">RFC8984</a>
 * @author Mario Loffredo
 */
@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class TimeZone {

    String tzId;

    Calendar updated;

    @Singular(value = "standardItem", ignoreNullCollections = true)
    List<TimeZoneRule> standard;

}

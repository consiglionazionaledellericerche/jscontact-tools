/*
 *    Copyright (C) 2020  Consiglio Nazionale delle Ricerche
 *     This program is free software: you can redistribute it and/or modify
 *     it under the terms of the GNU Affero General Public License as
 *     published by the Free Software Foundation, either version 3 of the
 *     License, or (at your option) any later version.
 *
 *     This program is distributed in the hope that it will be useful,
 *     but WITHOUT ANY WARRANTY; without even the implied warranty of
 *     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *     GNU Affero General Public License for more details.
 *
 *     You should have received a copy of the GNU Affero General Public License
 *     along with this program.  If not, see <https://www.gnu.org/licenses/>.
 */
package it.cnr.iit.jscontact.tools.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.SuperBuilder;

import java.io.Serializable;

/**
 * Class mapping the values of the "kind" property of the Calendar type as defined in section 2.4.1 of [draft-ietf-calext-jscontact].
 *
 * @see <a href="https://datatracker.ietf.org/doc/draft-ietf-calext-jscontact#section-2.4.1">draft-ietf-calext-jscontact</a>
 * @author Mario Loffredo
 */
@Getter
@Setter
@ToString(callSuper = true)
@NoArgsConstructor
@SuperBuilder
public class CalendarResourceKind extends ExtensibleEnumType<CalendarResourceEnum> implements Serializable {

    /**
     * Tests if this calendar resource type is "calendar".
     *
     * @return true if calendar resource type is "calendar", false otherwise
     */
    @JsonIgnore
    public boolean isCalendar() { return isRfc(CalendarResourceEnum.CALENDAR); }

    /**
     * Tests if this calendar resource type is "freeBusy".
     *
     * @return true if calendar resource type is "freeBusy", false otherwise
     */
    @JsonIgnore
    public boolean isFreeBusy() { return isRfc(CalendarResourceEnum.FREEBUSY); }


    private static CalendarResourceKind rfc(CalendarResourceEnum rfcValue) { return CalendarResourceKind.builder().rfcValue(rfcValue).build(); }

    /**
     * Returns a "calendar" calendar resource type.
     *
     * @return a "calendar" calendar resource type
     */
    public static CalendarResourceKind calendar() { return rfc(CalendarResourceEnum.CALENDAR);}

    /**
     * Returns a "freeBusy" calendar resource type.
     *
     * @return a "freeBusy" calendar resource type
     */
    public static CalendarResourceKind freeBusy() { return rfc(CalendarResourceEnum.FREEBUSY);}

    /**
     * Returns a custom calendar resource type.
     *
     * @return a custom calendar resource type
     */
    private static CalendarResourceKind ext(String extValue) { return CalendarResourceKind.builder().extValue(V_Extension.toV_Extension(extValue)).build(); }
}

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
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import it.cnr.iit.jscontact.tools.constraints.ResourceConstraint;
import it.cnr.iit.jscontact.tools.dto.deserializers.CalendarResourceTypeDeserializer;
import it.cnr.iit.jscontact.tools.dto.interfaces.HasKind;
import lombok.*;
import lombok.experimental.SuperBuilder;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;


/**
 * Class mapping the CalendarResource type as defined in section 2.4.1 of [draft-ietf-calext-jscontact].
 *
 * @see <a href="https://datatracker.ietf.org/doc/draft-ietf-calext-jscontact#section-2.4.1">draft-ietf-calext-jscontact</a>
 * @author Mario Loffredo
 */
@ResourceConstraint
@JsonPropertyOrder({"@type","uri","kind","mediaType","contexts","pref","label"})
@JsonInclude(JsonInclude.Include.NON_NULL)
@SuperBuilder
@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class CalendarResource extends Resource implements HasKind {

    @NotNull
    @Pattern(regexp = "CalendarResource", message="invalid @type value in CalendarResource")
    @JsonProperty("@type")
    @Builder.Default
    String _type = "CalendarResource";

    @JsonDeserialize(using = CalendarResourceTypeDeserializer.class)
    CalendarResourceKind kind;

    @JsonIgnore
    private boolean isCalendarResource(CalendarResourceKind type) { return this.kind.equals(type); }

    /**
     * Tests if this calendar resource is a calendar.
     *
     * @return true if this calendar resource is a calendar, false otherwise
     */
    @JsonIgnore
    public boolean isCalendar() { return isCalendarResource(CalendarResourceKind.calendar()); }

    /**
     * Tests if this calendar resource is a free busy calendar.
     *
     * @return true if this calendar resource is a free busy calendar, false otherwise
     */
    @JsonIgnore
    public boolean isFreeBusy() { return isCalendarResource(CalendarResourceKind.freeBusy()); }

    private static CalendarResource resource(CalendarResourceKind type, String uri) {
        return CalendarResource.builder()
                       .uri(uri)
                       .kind(type)
                       .build();
    }

    /**
     * Returns a calendar
     *
     * @param uri calendar uri
     * @return the calendar
     */
    public static CalendarResource calendar(String uri) { return resource(CalendarResourceKind.calendar(), uri);}

    /**
     * Returns a free busy calendar
     *
     * @param uri entry uri
     * @return the entry
     */
    public static CalendarResource freeBusy(String uri) { return resource(CalendarResourceKind.freeBusy(), uri);}

}

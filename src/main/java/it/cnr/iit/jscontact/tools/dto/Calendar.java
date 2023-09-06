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
import it.cnr.iit.jscontact.tools.dto.annotations.ContainsExtensibleEnum;
import it.cnr.iit.jscontact.tools.dto.deserializers.CalendarKindDeserializer;
import it.cnr.iit.jscontact.tools.dto.interfaces.HasKind;
import it.cnr.iit.jscontact.tools.dto.interfaces.IsIANAType;
import lombok.*;
import lombok.experimental.SuperBuilder;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

/**
 * Class mapping the Calendar type as defined in section 2.4.1 of [draft-ietf-calext-jscontact].
 *
 * @see <a href="https://datatracker.ietf.org/doc/draft-ietf-calext-jscontact#section-2.4.1">draft-ietf-calext-jscontact</a>
 * @author Mario Loffredo
 */
@JsonPropertyOrder({"@type","uri","kind","mediaType","contexts","pref","label"})
@JsonInclude(JsonInclude.Include.NON_NULL)
@SuperBuilder
@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class Calendar extends Resource implements HasKind, IsIANAType {

    @Pattern(regexp = "Calendar", message="invalid @type value in Calendar")
    @JsonProperty("@type")
    @Builder.Default
    String _type = "Calendar";

    @NonNull
    @NotNull
    @JsonDeserialize(using = CalendarKindDeserializer.class)
    @ContainsExtensibleEnum(enumClass = CalendarEnum.class, getMethod = "getKind")
    CalendarKind kind;

    @JsonIgnore
    private boolean isCalendarResource(CalendarKind type) { return this.kind.equals(type); }

    /**
     * Tests if this calendar resource is a calendar.
     *
     * @return true if this calendar resource is a calendar, false otherwise
     */
    @JsonIgnore
    public boolean isCalendar() { return isCalendarResource(CalendarKind.calendar()); }

    /**
     * Tests if this calendar resource is a free busy calendar.
     *
     * @return true if this calendar resource is a free busy calendar, false otherwise
     */
    @JsonIgnore
    public boolean isFreeBusy() { return isCalendarResource(CalendarKind.freeBusy()); }

    private static Calendar resource(CalendarKind type, String uri) {
        return Calendar.builder()
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
    public static Calendar calendar(String uri) { return resource(CalendarKind.calendar(), uri);}

    /**
     * Returns a free busy calendar
     *
     * @param uri entry uri
     * @return the entry
     */
    public static Calendar freeBusy(String uri) { return resource(CalendarKind.freeBusy(), uri);}


    @JsonIgnore
    public String getRegisteredIANATypeName() {
        return "Calendar";
    }

}

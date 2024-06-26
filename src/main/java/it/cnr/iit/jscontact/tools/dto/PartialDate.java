package it.cnr.iit.jscontact.tools.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import it.cnr.iit.jscontact.tools.constraints.NotNullAnyConstraint;
import it.cnr.iit.jscontact.tools.constraints.NotNullDependencyConstraint;
import it.cnr.iit.jscontact.tools.dto.interfaces.IsIANAType;
import lombok.*;
import lombok.experimental.SuperBuilder;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.Pattern;

/**
 * Class mapping the PartialDate type as defined in section 2.8.1 of [RFC9553].
 *
 * @see <a href="https://datatracker.ietf.org/doc/RFC9553#section-2.8.1">Section 2.8.1 of RFC9553</a>
 * @author Mario Loffredo
 */
@NotNullAnyConstraint(fieldNames = {"year", "month"}, message = "at least one not null member between year and month is required in PartialDate")
@NotNullDependencyConstraint(fieldName="month", dependingFieldNames = {"day"})
@JsonPropertyOrder({"@type", "year", "month", "day", "calendarScale"})
@JsonInclude(JsonInclude.Include.NON_NULL)
@SuperBuilder
@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper = false)
public class PartialDate extends AbstractJSContactType implements IsIANAType {

    @Pattern(regexp = "PartialDate", message = "invalid @type value in PartialDate")
    @JsonProperty("@type")
    @Builder.Default
    String _type = "PartialDate";

    @Min(value = 1, message = "invalid year in PartialDate - value must be greater or equal than 1")
    Integer year;

    @Min(value = 1, message = "invalid month in PartialDate - value must be greater or equal than 1")
    @Max(value = 12, message = "invalid month in PartialDate - value must be less or equal than 12")
    Integer month;

    @Min(value = 1, message = "invalid day in PartialDate - value must be greater or equal than 1")
    @Max(value = 31, message = "invalid day in PartialDate - value must be less or equal than 31")
    Integer day;

    String calendarScale;

    /**
     * Returns the representation of this partial date according to the Ez-vcard PartialDate class.
     *
     * @return the conversion of this partial date into the Ez-vcard PartialDate class.
     */
    public ezvcard.util.PartialDate toVCardPartialDate() {
        return ezvcard.util.PartialDate.builder().year(year).month(month).date(day).build();
    }

}

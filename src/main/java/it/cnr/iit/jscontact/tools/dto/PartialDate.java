package it.cnr.iit.jscontact.tools.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import it.cnr.iit.jscontact.tools.constraints.NotNullAnyConstraint;
import lombok.*;
import lombok.experimental.SuperBuilder;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

/**
 * Class mapping the PartialDate type as defined in section 2.8.1 of [draft-ietf-calext-jscontact].
 *
 * @see <a href="https://datatracker.ietf.org/doc/draft-ietf-calext-jscontact#section-2.8.1">draft-ietf-calext-jscontact</a>
 * @author Mario Loffredo
 */
@NotNullAnyConstraint(fieldNames={"year","month","day"}, message = "at least one not null member is missing in VCardParam")
@JsonPropertyOrder({"@type","year","month","day","calscale"})
@JsonInclude(JsonInclude.Include.NON_NULL)
@SuperBuilder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class PartialDate extends AbstractJSContactType {

    @NotNull
    @Pattern(regexp = "PartialDate", message="invalid @type value in PartialDate")
    @JsonProperty("@type")
    @Builder.Default
    String _type = "PartialDate";

    Integer year;

    Integer month;

    Integer day;

    String calendarScale;

    public ezvcard.util.PartialDate toVCardPartialDate() {
        return ezvcard.util.PartialDate.builder().year(year).month(month).date(day).build();
    }

}

package it.cnr.iit.jscontact.tools.dto;

import it.cnr.iit.jscontact.tools.constraints.NotNullAnyConstraint;
import lombok.*;

/**
 * Class mapping the VCard parameters as defined in section 2.15.2 of [RFC9555].
 *
 * @author Mario Loffredo
 * @see <a href="https://datatracker.ietf.org/doc/RFC9553#section-2.15.2">Section 2.15.2 of RFC9555</a>
 */
@NotNullAnyConstraint(fieldNames={"value","values"}, message = "at least one not null member between value and values is required in VCardParam")
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Data
public class VCardParam {

    String value;

    String[] values;

}

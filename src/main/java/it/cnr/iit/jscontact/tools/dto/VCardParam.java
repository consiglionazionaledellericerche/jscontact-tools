package it.cnr.iit.jscontact.tools.dto;

import it.cnr.iit.jscontact.tools.constraints.NotNullAnyConstraint;
import lombok.*;

/**
 * Class mapping the VCard parameters as defined in section 2.16.2 of [draft-ietf-calext-jscontact-vcard].
 *
 * @author Mario Loffredo
 * @see <a href="https://datatracker.ietf.org/doc/draft-ietf-calext-jscontact#section-2.16.2">draft-ietf-calext-jscontact-vcard</a>
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

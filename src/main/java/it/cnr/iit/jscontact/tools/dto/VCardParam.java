package it.cnr.iit.jscontact.tools.dto;

import it.cnr.iit.jscontact.tools.constraints.NotNullAnyConstraint;
import lombok.*;

/**
 * Class mapping the VCard parameters as defined in section 2.15.2 of [draft-ietf-calext-jscontact-vcard].
 *
 * @see <a href="https://datatracker.ietf.org/doc/draft-ietf-calext-jscontact#section-2.15.2">draft-ietf-calext-jscontact-vcard</a>
 * @author Mario Loffredo
 */
@NotNullAnyConstraint(fieldNames={"value","values"}, message = "at least one not null member is missing in VCardParam")
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Data
public class VCardParam {

    String value;

    String[] values;

}

package it.cnr.iit.jscontact.tools.dto;

import ezvcard.VCardDataType;
import ezvcard.parameter.VCardParameters;
import it.cnr.iit.jscontact.tools.constraints.NotNullAnyConstraint;
import lombok.*;

import java.util.HashMap;
import java.util.Map;

/**
 * Class mapping the JCard parameters as defined in section 2.15.2 of [draft-ietf-calext-jscontact].
 *
 * @see <a href="https://datatracker.ietf.org/doc/draft-ietf-calext-jscontact#section-2.15.2">draft-ietf-calext-jscontact</a>
 * @author Mario Loffredo
 */
@NotNullAnyConstraint(fieldNames={"value","values"}, message = "at least one not null member is missing in JCardParam")
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Data
public class JCardParam {

    String value;

    String[] values;

}

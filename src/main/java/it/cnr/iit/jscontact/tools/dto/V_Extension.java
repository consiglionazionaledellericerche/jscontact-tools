package it.cnr.iit.jscontact.tools.dto;

import it.cnr.iit.jscontact.tools.dto.utils.DelimiterUtils;
import lombok.*;

import javax.validation.constraints.NotNull;
import java.util.Arrays;

/**
 * Class mapping the vendor-extension values as defined in section 1.8 of [RFC9553].
 *
 * @author Mario Loffredo
 * @see <a href="https://datatracker.ietf.org/doc/RFC9553#section-1.8">RFC9553</a>
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class V_Extension {

    private String v_prefix;

    @NonNull
    @NotNull(message = "v_value is missing in V_Extension")
    private String v_value;

    public static V_Extension toV_Extension(String extValue) {

        if (extValue == null) return null;

        String[] items = extValue.split(DelimiterUtils.COLON_DELIMITER);
        if (items.length == 1) //Unknown Extension
            return V_Extension.builder().v_value(extValue).build();

        // Vendor Extension
        return V_Extension.builder().v_prefix(items[0]).v_value(String.join(DelimiterUtils.COLON_DELIMITER, Arrays.copyOfRange(items, 1, items.length))).build();
    }

    @Override
    public String toString() {
        if (v_prefix == null)
            return v_value;
        return v_prefix + ":" + v_value;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null)
            return false;

        if (!(obj instanceof V_Extension))
            return false;

        return this.toString().equalsIgnoreCase(obj.toString());
    }

}

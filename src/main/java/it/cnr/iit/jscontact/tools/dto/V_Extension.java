package it.cnr.iit.jscontact.tools.dto;

import lombok.*;

import javax.validation.constraints.NotNull;

/**
 * Class mapping the vendor-extension values as defined in section 1.7 of [draft-ietf-calext-jscontact].
 *
 * @see <a href="https://datatracker.ietf.org/doc/draft-ietf-calext-jscontact#section-1.7">draft-ietf-calext-jscontact</a>
 * @author Mario Loffredo
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class V_Extension {


    private String v_prefix;

    @NonNull
    @NotNull
    private String v_value;

    public static V_Extension toV_Extension(String extValue) {
        if (extValue == null) return null;

        if (extValue.toLowerCase().startsWith("x-")) //Unknown Extension
            return V_Extension.builder().v_value(extValue.toLowerCase()).build();

        String[] items = extValue.split(":");
        if (items.length != 2) //Unknown Extension
            return V_Extension.builder().v_value(extValue.toLowerCase()).build();

        // Vendor Extension
        return V_Extension.builder().v_prefix(items[0]).v_value(items[1]).build();
    }

    @Override
    public String toString() {
        if (v_prefix == null)
            return v_value;
        return v_prefix + ":" + v_value;
    }

}

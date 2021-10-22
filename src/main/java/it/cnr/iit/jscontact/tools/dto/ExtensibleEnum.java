package it.cnr.iit.jscontact.tools.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import it.cnr.iit.jscontact.tools.dto.interfaces.IsExtensible;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.io.Serializable;

/**
 * Abstract class mapping extensible enumerated types.
 *
 * @author Mario Loffredo
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
@NoArgsConstructor
@Getter
@Setter
@ToString
@SuperBuilder
public abstract class ExtensibleEnum<T extends IsExtensible> implements Serializable {

    T rfcValue;
    String extValue;

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        ExtensibleEnum type = (ExtensibleEnum) obj;
        if (type.rfcValue != null )
            return type.rfcValue == rfcValue;

        return extValue != null && extValue.equals(type.extValue);
    }


    @Override
    public int hashCode() {

        if (rfcValue != null)
            return rfcValue.getValue().hashCode();

        return extValue.hashCode();
    }

    /**
     * Tests if this is a pre-defined value of an extensible enumerated type.
     *
     * @return true if this is a pre-defined value of an extensible enumerated type, false otherwise
     */
    @JsonIgnore
    public boolean isRfcValue() { return rfcValue != null; }
    @JsonIgnore
    protected boolean isExtValue() { return extValue != null; }
}

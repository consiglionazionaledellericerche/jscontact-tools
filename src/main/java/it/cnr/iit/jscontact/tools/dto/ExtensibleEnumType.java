package it.cnr.iit.jscontact.tools.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonValue;
import it.cnr.iit.jscontact.tools.dto.interfaces.IsExtensibleEnum;
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
public abstract class ExtensibleEnumType<T extends IsExtensibleEnum> implements Serializable {

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
        ExtensibleEnumType type = (ExtensibleEnumType) obj;
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

    /**
     * Tests if this is an extension of an extensible enumerated type.
     *
     * @return true if this is an extension of an extensible enumerated type, false otherwise
     */
    @JsonIgnore
    public boolean isExtValue() { return extValue != null; }

    @JsonIgnore
    public boolean isRfc(T value) { return isRfcValue() && rfcValue == value; }

    @JsonValue
    public String toJson() {
        return (isRfcValue()) ? getRfcValue().getValue() : getExtValue();
    }

}

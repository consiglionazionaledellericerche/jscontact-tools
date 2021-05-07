package it.cnr.iit.jscontact.tools.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import it.cnr.iit.jscontact.tools.dto.interfaces.IsExtensible;
import lombok.*;
import lombok.experimental.SuperBuilder;

@JsonInclude(JsonInclude.Include.NON_NULL)
@NoArgsConstructor
@Getter
@Setter
@ToString
@SuperBuilder
public abstract class ExtensibleType<T extends IsExtensible> {

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
        ExtensibleType type = (ExtensibleType) obj;
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

}

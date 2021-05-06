/*
 *    Copyright (C) 2020  Consiglio Nazionale delle Ricerche
 *     This program is free software: you can redistribute it and/or modify
 *     it under the terms of the GNU Affero General Public License as
 *     published by the Free Software Foundation, either version 3 of the
 *     License, or (at your option) any later version.
 *
 *     This program is distributed in the hope that it will be useful,
 *     but WITHOUT ANY WARRANTY; without even the implied warranty of
 *     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *     GNU Affero General Public License for more details.
 *
 *     You should have received a copy of the GNU Affero General Public License
 *     along with this program.  If not, see <https://www.gnu.org/licenses/>.
 */
package it.cnr.iit.jscontact.tools.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import it.cnr.iit.jscontact.tools.dto.deserializers.NameComponentTypeDeserializer;
import it.cnr.iit.jscontact.tools.dto.serializers.NameComponentTypeSerializer;
import lombok.*;

import javax.validation.constraints.NotNull;
import java.io.Serializable;

@JsonInclude(JsonInclude.Include.NON_NULL)
@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class NameComponent extends GroupableObject implements Serializable {

    @NotNull(message = "type is missing in NameComponent")
    @NonNull
    @JsonSerialize(using = NameComponentTypeSerializer.class)
    @JsonDeserialize(using = NameComponentTypeDeserializer.class)
    NameComponentType type;

    @NotNull(message = "value is missing in NameComponent")
    @NonNull
    String value;

    private boolean isRfcNameComponent(NameComponentEnum value) { return (type.getRfcValue()!= null && type.getRfcValue() == value);};
    @JsonIgnore
    public boolean isPrefix() { return isRfcNameComponent(NameComponentEnum.PREFIX); }
    @JsonIgnore
    public boolean isPersonal() { return isRfcNameComponent(NameComponentEnum.PERSONAL); }
    @JsonIgnore
    public boolean isSurname() { return isRfcNameComponent(NameComponentEnum.SURNAME); }
    @JsonIgnore
    public boolean isAdditional() { return isRfcNameComponent(NameComponentEnum.ADDITIONAL); }
    @JsonIgnore
    public boolean isSuffix() { return isRfcNameComponent(NameComponentEnum.SUFFIX); }
    @JsonIgnore
    public boolean isSeparator() { return isRfcNameComponent(NameComponentEnum.SEPARATOR); }
    @JsonIgnore
    public boolean isExtNameComponent() { return type.getExtValue() != null; }

    private static NameComponent nameComponent(NameComponentEnum rfcEnum, String value) {
        return NameComponent.builder()
                .value(value)
                .type(NameComponentType.builder().rfcValue(rfcEnum).build())
                .build();
    }
    public static NameComponent prefix(String value) {return nameComponent(NameComponentEnum.PREFIX, value);}
    public static NameComponent personal(String value) {return nameComponent(NameComponentEnum.PERSONAL, value);}
    public static NameComponent surname(String value) {return nameComponent(NameComponentEnum.SURNAME, value);}
    public static NameComponent additional(String value) {return nameComponent(NameComponentEnum.ADDITIONAL, value);}
    public static NameComponent suffix(String value) {return nameComponent(NameComponentEnum.SUFFIX, value);}
    public static NameComponent separator(String value) {return nameComponent(NameComponentEnum.SEPARATOR, value);}
    public static NameComponent extNameComponent(String extEnum, String value) {
        return NameComponent.builder()
                .value(value)
                .type(NameComponentType.builder().extValue(extEnum).build())
                .build();
    }


}

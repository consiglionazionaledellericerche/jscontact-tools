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
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.io.Serializable;

@Getter
@Setter
@ToString(callSuper = true)
@NoArgsConstructor
@SuperBuilder
public class KindType extends ExtensibleType<KindEnum> implements Serializable {

    private boolean isRfcKind(KindEnum value) { return rfcValue != null && rfcValue == value; }
    @JsonIgnore
    public boolean isIndividual() { return isRfcKind(KindEnum.INDIVIDUAL); }
    @JsonIgnore
    public boolean isGroup() { return isRfcKind(KindEnum.GROUP); }
    @JsonIgnore
    public boolean isOrg() { return isRfcKind(KindEnum.ORG); }
    @JsonIgnore
    public boolean isDevice() { return isRfcKind(KindEnum.DEVICE); }
    @JsonIgnore
    public boolean isApplication() { return isRfcKind(KindEnum.APPLICATION); }
    @JsonIgnore
    public boolean isLocation() { return isRfcKind(KindEnum.LOCATION); }
    @JsonIgnore
    public boolean isExtKind() { return extValue != null; }

    private static KindType rfcKindType(KindEnum rfcValue) { return KindType.builder().rfcValue(rfcValue).build(); }
    public static KindType individual() { return rfcKindType(KindEnum.INDIVIDUAL);}
    public static KindType group() { return rfcKindType(KindEnum.GROUP);}
    public static KindType org() { return rfcKindType(KindEnum.ORG);}
    public static KindType device() { return rfcKindType(KindEnum.DEVICE);}
    public static KindType location() { return rfcKindType(KindEnum.LOCATION);}
    public static KindType application() { return rfcKindType(KindEnum.APPLICATION);}
}

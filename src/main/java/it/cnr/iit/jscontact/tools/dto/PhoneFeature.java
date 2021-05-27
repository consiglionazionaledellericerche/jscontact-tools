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
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.SuperBuilder;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Getter
@Setter
@ToString(callSuper = true)
@NoArgsConstructor
@SuperBuilder
public class PhoneFeature extends ExtensibleEnum<PhoneFeatureEnum> implements Serializable {

    private boolean isRfc(PhoneFeatureEnum value) { return isRfcValue() && rfcValue == value; }
    @JsonIgnore
    public boolean isVoice() { return isRfc(PhoneFeatureEnum.VOICE); }
    @JsonIgnore
    public boolean isFax() { return isRfc(PhoneFeatureEnum.FAX); }
    @JsonIgnore
    public boolean isCell() { return isRfc(PhoneFeatureEnum.CELL); }
    @JsonIgnore
    public boolean isPager() { return isRfc(PhoneFeatureEnum.PAGER); }
    @JsonIgnore
    public boolean isVideo() { return isRfc(PhoneFeatureEnum.VIDEO); }
    @JsonIgnore
    public boolean isText() { return isRfc(PhoneFeatureEnum.TEXT); }
    @JsonIgnore
    public boolean isTextphone() { return isRfc(PhoneFeatureEnum.TEXTPHONE); }
    @JsonIgnore
    public boolean isOther() { return isRfc(PhoneFeatureEnum.OTHER); }

    public static PhoneFeature rfc(PhoneFeatureEnum rfcValue) { return PhoneFeature.builder().rfcValue(rfcValue).build();}
    public static PhoneFeature voice() { return rfc(PhoneFeatureEnum.VOICE);}
    public static PhoneFeature fax() { return rfc(PhoneFeatureEnum.FAX);}
    public static PhoneFeature pager() { return rfc(PhoneFeatureEnum.PAGER);}
    public static PhoneFeature cell() { return rfc(PhoneFeatureEnum.CELL);}
    public static PhoneFeature video() { return rfc(PhoneFeatureEnum.VIDEO);}
    public static PhoneFeature text() { return rfc(PhoneFeatureEnum.TEXT);}
    public static PhoneFeature textphone() { return rfc(PhoneFeatureEnum.TEXTPHONE);}
    public static PhoneFeature other() { return rfc(PhoneFeatureEnum.OTHER);}
    public static PhoneFeature ext(String extValue) { return PhoneFeature.builder().extValue(extValue).build(); }

    public static List<PhoneFeatureEnum> getFeatureEnumValues(Collection<PhoneFeature> features) {

        if (features == null)
            return null;

        List<PhoneFeatureEnum> enumValues = new ArrayList<>();
        for (PhoneFeature feature : features) {
            if (feature.rfcValue != null)
                enumValues.add(feature.getRfcValue());
        }

        return enumValues;
    }

}

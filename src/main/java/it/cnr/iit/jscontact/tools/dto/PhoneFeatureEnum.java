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

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonValue;
import it.cnr.iit.jscontact.tools.dto.interfaces.IsExtensible;
import it.cnr.iit.jscontact.tools.dto.interfaces.VCardTypeDerivedEnum;
import it.cnr.iit.jscontact.tools.dto.utils.EnumUtils;
import lombok.AllArgsConstructor;

import java.util.Arrays;
import java.util.List;

@AllArgsConstructor
public enum PhoneFeatureEnum implements IsExtensible,VCardTypeDerivedEnum {

    VOICE("voice"),
    FAX("fax"),
    PAGER("pager"),
    TEXT("text"),
    CELL("cell"),
    VIDEO("video"),
    TEXTPHONE("textphone"),
    OTHER("other");

    private String value;

    @JsonIgnore
    private static final List<String> otherVCardTypes = Arrays.asList("textphone", "video", "cell");

    @JsonValue
    public String getValue() {
        return value;
    }

    @JsonCreator
    public static PhoneFeatureEnum getEnum(String value) throws IllegalArgumentException {
        return EnumUtils.getEnum(PhoneFeatureEnum.class, value);
    }

    @Override
    public String toString() {
        return value;
    }

    @JsonIgnore
    public static String getVCardType(PhoneFeatureEnum type) {

        if (type == OTHER)
            return null;

        return type.getValue();
    }

    @JsonIgnore
    public static String getVCardType(String label) {

        try {
            PhoneFeatureEnum rc = getEnum(label);
            return getVCardType(rc);
        }
        catch(Exception e) {

            if (otherVCardTypes.contains(label))
                return label;

            return null;
        }
    }


}


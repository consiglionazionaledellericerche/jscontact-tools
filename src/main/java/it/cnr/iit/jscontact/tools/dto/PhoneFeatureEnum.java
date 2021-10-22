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

/**
 * Enum class mapping the "features" map keys of the Phone type as defined in section 2.3.2 of [draft-ietf-jmap-jscontact].
 *
 * @see <a href="https://datatracker.ietf.org/doc/draft-ietf-jmap-jscontact#section-2.3.2">draft-ietf-jmap-jscontact</a>
 * @author Mario Loffredo
 */
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

    /**
     * Returns the vCard 4.0 [RFC6350] TYPE parameter corresponding to the enum value representing the phone feature.
     *
     * @param phoneFeature the phone feature
     * @return the vCard 4.0 TYPE parameter value
     * @see <a href="https://datatracker.ietf.org/doc/html/rfc6350#section-5.6">RFC6350</a>
     */
    @JsonIgnore
    public static String toVCardType(PhoneFeatureEnum phoneFeature) {

        if (phoneFeature == OTHER)
            return null;

        return phoneFeature.getValue();
    }


}


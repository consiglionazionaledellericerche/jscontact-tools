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

/**
 * Class mapping the keys of "features" map of the Phone type as defined in section 2.4.1 of [draft-ietf-jmap-jscontact].
 *
 * @see <a href="https://datatracker.ietf.org/doc/draft-ietf-jmap-jscontact#section-2.4.1">draft-ietf-jmap-jscontact</a>
 * @author Mario Loffredo
 */
@Getter
@Setter
@ToString(callSuper = true)
@NoArgsConstructor
@SuperBuilder
public class PhoneFeature extends ExtensibleEnum<PhoneFeatureEnum> implements Serializable {

    private boolean isRfc(PhoneFeatureEnum value) { return isRfcValue() && rfcValue == value; }
    /**
     * Tests if this is a "voice" phone feature.
     *
     * @return true if this is a "voice" phone feature, false otherwise
     */
    @JsonIgnore
    public boolean isVoice() { return isRfc(PhoneFeatureEnum.VOICE); }
    /**
     * Tests if this is a "fax" phone feature.
     *
     * @return true if this is a "fax" phone feature, false otherwise
     */
    @JsonIgnore
    public boolean isFax() { return isRfc(PhoneFeatureEnum.FAX); }
    /**
     * Tests if this is a "cell" phone feature.
     *
     * @return true if this is a "cell" phone feature, false otherwise
     */
    @JsonIgnore
    public boolean isCell() { return isRfc(PhoneFeatureEnum.CELL); }
    /**
     * Tests if this is a "pager" phone feature.
     *
     * @return true if this is a "pager" phone feature, false otherwise
     */
    @JsonIgnore
    public boolean isPager() { return isRfc(PhoneFeatureEnum.PAGER); }
    /**
     * Tests if this is a "video" phone feature.
     *
     * @return true if this is a "video" phone feature, false otherwise
     */
    @JsonIgnore
    public boolean isVideo() { return isRfc(PhoneFeatureEnum.VIDEO); }
    /**
     * Tests if this is a "text" phone feature.
     *
     * @return true if this is a "text" phone feature, false otherwise
     */
    @JsonIgnore
    public boolean isText() { return isRfc(PhoneFeatureEnum.TEXT); }
    /**
     * Tests if this is a "textphone" phone feature.
     *
     * @return true if this is a "textphone" phone feature, false otherwise
     */
    @JsonIgnore
    public boolean isTextphone() { return isRfc(PhoneFeatureEnum.TEXTPHONE); }
    /**
     * Tests if this is a phone feature other than those known.
     *
     * @return true if this is a "other" phone feature, false otherwise
     */
    @JsonIgnore
    public boolean isOther() { return isRfc(PhoneFeatureEnum.OTHER); }

    public static PhoneFeature rfc(PhoneFeatureEnum rfcValue) { return PhoneFeature.builder().rfcValue(rfcValue).build();}
    /**
     * Creates a "voice" phone feature.
     *
     * @return an object representing a "voice" phone feature
     */
    public static PhoneFeature voice() { return rfc(PhoneFeatureEnum.VOICE);}
    /**
     * Creates a "fax" phone feature.
     *
     * @return an object representing a "fax" phone feature
     */
    public static PhoneFeature fax() { return rfc(PhoneFeatureEnum.FAX);}
    /**
     * Creates a "pager" phone feature.
     *
     * @return an object representing a "pager" phone feature
     */
    public static PhoneFeature pager() { return rfc(PhoneFeatureEnum.PAGER);}
    /**
     * Creates a "cell" phone feature.
     *
     * @return an object representing a "cell" phone feature
     */
    public static PhoneFeature cell() { return rfc(PhoneFeatureEnum.CELL);}
    /**
     * Creates a "video" phone feature.
     *
     * @return an object representing a "video" phone feature
     */
    public static PhoneFeature video() { return rfc(PhoneFeatureEnum.VIDEO);}
    /**
     * Creates a "text" phone feature.
     *
     * @return an object representing a "text" phone feature
     */
    public static PhoneFeature text() { return rfc(PhoneFeatureEnum.TEXT);}
    /**
     * Creates a "textphone" phone feature.
     *
     * @return an object representing a "textphone" phone feature
     */
    public static PhoneFeature textphone() { return rfc(PhoneFeatureEnum.TEXTPHONE);}
    /**
     * Creates a phone feature other than those known.
     *
     * @return an object representing an "other" phone feature
     */
    public static PhoneFeature other() { return rfc(PhoneFeatureEnum.OTHER);}
    /**
     * Creates a custom phone feature.
     *
     * @param extValue the custom phone feature as text
     * @return an object representing a custom phone feature
     */
    public static PhoneFeature ext(String extValue) { return PhoneFeature.builder().extValue(extValue).build(); }

    /**
     * Returns the list of enum values corresponding to those ones whose type is known in a given collection of phone features.
     *
     * @param features the list of phone features
     * @return list of enum values corresponding to those features whose type is known
     */
    public static List<PhoneFeatureEnum> toEnumValues(Collection<PhoneFeature> features) {

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

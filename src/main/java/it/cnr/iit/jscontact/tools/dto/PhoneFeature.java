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
 * Class mapping the keys of "features" map of the Phone type as defined in section 2.3.2 of [draft-ietf-calext-jscontact].
 *
 * @see <a href="https://datatracker.ietf.org/doc/draft-ietf-calext-jscontact#section-2.3.2">draft-ietf-calext-jscontact</a>
 * @author Mario Loffredo
 */
@Getter
@Setter
@ToString(callSuper = true)
@NoArgsConstructor
@SuperBuilder
public class PhoneFeature extends ExtensibleEnumType<PhoneFeatureEnum> implements Serializable {

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
     * Returns a phone feature whose enum value is pre-defined.
     *
     * @param rfcValue the pre-defined feature
     * @return a pre-defined phone feature
     */
    public static PhoneFeature rfc(PhoneFeatureEnum rfcValue) { return PhoneFeature.builder().rfcValue(rfcValue).build();}
    /**
     * Returns a "voice" phone feature.
     *
     * @return a "voice" phone feature
     */
    public static PhoneFeature voice() { return rfc(PhoneFeatureEnum.VOICE);}
    /**
     * Returns a "fax" phone feature.
     *
     * @return a "fax" phone feature
     */
    public static PhoneFeature fax() { return rfc(PhoneFeatureEnum.FAX);}
    /**
     * Returns a "pager" phone feature.
     *
     * @return a "pager" phone feature
     */
    public static PhoneFeature pager() { return rfc(PhoneFeatureEnum.PAGER);}
    /**
     * Returns a "cell" phone feature.
     *
     * @return a "cell" phone feature
     */
    public static PhoneFeature cell() { return rfc(PhoneFeatureEnum.CELL);}
    /**
     * Returns a "video" phone feature.
     *
     * @return a "video" phone feature
     */
    public static PhoneFeature video() { return rfc(PhoneFeatureEnum.VIDEO);}
    /**
     * Returns a "text" phone feature.
     *
     * @return a "text" phone feature
     */
    public static PhoneFeature text() { return rfc(PhoneFeatureEnum.TEXT);}
    /**
     * Returns a "textphone" phone feature.
     *
     * @return a "textphone" phone feature
     */
    public static PhoneFeature textphone() { return rfc(PhoneFeatureEnum.TEXTPHONE);}
    /**
     * Returns a custom phone feature.
     *
     * @param extValue the custom phone feature in text format
     * @return a custom phone feature
     */
    public static PhoneFeature ext(String extValue) { return PhoneFeature.builder().extValue(V_Extension.toV_Extension(extValue)).build(); }

    /**
     * Returns the list of enum values corresponding to those whose type is known in a given collection of phone features.
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

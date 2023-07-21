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

import it.cnr.iit.jscontact.tools.dto.utils.EnumUtils;
import lombok.AllArgsConstructor;

/**
 * Enum class mapping the VCard parameters as defined in section 5 of [RFC6350] and section 3 of [draft-ietf-calext-vcard-jscontact-extensions] .
 *
 * @see <a href="https://datatracker.ietf.org/doc/draft-ietf-calext-jscontact#section-5">https://www.rfc-editor.org/rfc/rfc6350</a>
 * @see <a href="https://datatracker.ietf.org/doc/draft-ietf-calext-vcard-jscontact-extensions">draft-ietf-calext-vcard-jscontact-extensions</a>
 * @author Mario Loffredo
 */
@AllArgsConstructor
public enum VCardParamEnum  {

    TZ("TZ"),
    GEO("GEO"),
    ALTID("ALTID"),
    INDEX("INDEX"),
    PID("PID"),
    LEVEL("LEVEL"),
    CC("CC"),
    ISO_3166_1_ALPHA_2 ("ISO-3166-1-alpha-2"),
    GROUP("GROUP"),
    TYPE("TYPE"),
    PREF("PREF"),
    CALSCALE("CALSCALE"),
    LANGUAGE("LANGUAGE"),
    MEDIATYPE("MEDIATYPE"),
    SORT_AS("SORT-AS"),
    LABEL("LABEL"),
    PROP_ID("PROP-ID"),
    DERIVED("DERIVED"),
    AUTHOR("AUTHOR"),
    AUTHOR_NAME("AUTHOR-NAME"),
    CREATED("CREATED"),
    JSPTR("JSPTR"),
    JSCOMPS("JSCOMPS"),
    SERVICE_TYPE("SERVICE-TYPE"),
    USERNAME("USERNAME"),
    VALUE("VALUE");

    private final String value;

    public String getValue() {
        return value;
    }

    public static VCardParamEnum getEnum(String value) throws IllegalArgumentException {
        return (value == null) ? null : EnumUtils.getEnum(VCardParamEnum.class, value);
    }

    @Override
    public String toString() {
        return value;
    }


}


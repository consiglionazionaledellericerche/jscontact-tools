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
 * Enum class mapping some VCard properties as defined in section 6 of [RFC6350] and section 3 of [draft-ietf-calext-vcard-jscontact-extensions] .
 *
 * @author Mario Loffredo
 * @see <a href="https://datatracker.ietf.org/doc/draft-ietf-calext-jscontact#section-5">https://www.rfc-editor.org/rfc/rfc6350</a>
 * @see <a href="https://datatracker.ietf.org/doc/draft-ietf-calext-vcard-jscontact-extensions">draft-ietf-calext-vcard-jscontact-extensions</a>
 */
@AllArgsConstructor
public enum VCardPropEnum {

    VERSION("VERSION"),
    TZ("TZ"),
    GEO("GEO"),
    XML("XML"),
    SOCIALSERVICE("SOCIALSERVICE"),
    PRONOUNS("PRONOUNS"),
    CONTACT_BY("CONTACT-BY"),
    CONTACT_URI("CONTACT-URI"),
    DEFLANGUAGE("DEFLANGUAGE"),
    CLIENTPIDMAP("CLIENTPIDMAP"),
    CREATED("CREATED"),
    GENDER("GENDER"),
    GRAMGENDER("GRAMGENDER"),
    JSCONTACT_PROP("JSCONTACT-PROP"),
    X_ABLABEL("X-ABLabel");

    private final String value;

    public String getValue() {
        return value;
    }

    public static VCardPropEnum getEnum(String value) throws IllegalArgumentException {
        return (value == null) ? null : EnumUtils.getEnum(VCardPropEnum.class, value);
    }

    @Override
    public String toString() {
        return value;
    }


}


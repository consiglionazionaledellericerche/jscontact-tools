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
import lombok.experimental.SuperBuilder;

import java.io.Serializable;

/**
 * Class mapping the "contacrtBy" map keys as defined in section 2.3.4 of [draft-ietf-calext-jscontact].
 *
 * @see <a href="https://datatracker.ietf.org/doc/draft-ietf-calext-jscontact#section-2.3.4">draft-ietf-calext-jscontact</a>
 * @author Mario Loffredo
 */
@Getter
@Setter
@NoArgsConstructor
@SuperBuilder
public class ContactByType extends ExtensibleEnumType<ContactByEnum> implements Serializable, Comparable<ContactByType> {

    /**
     * Tests if this contact channel type  is "addresses".
     *
     * @return true if this contact channel type is "addresses", false otherwise
     */
    @JsonIgnore
    public boolean isAddresses() { return isRfc(ContactByEnum.ADDRESSES); }

    /**
     * Tests if this contact channel type  is "emails".
     *
     * @return true if this contact channel type is "emails", false otherwise
     */
    @JsonIgnore
    public boolean isEmails() { return isRfc(ContactByEnum.EMAILS); }

    /**
     * Tests if this contact channel type  is "onlineServices".
     *
     * @return true if this contact channel type is "onlineServices", false otherwise
     */
    @JsonIgnore
    public boolean isOnlineServices() { return isRfc(ContactByEnum.ONLINE_SERVICES); }

    /**
     * Tests if this contact channel type  is "phones".
     *
     * @return true if this contact channel type is "phones", false otherwise
     */
    @JsonIgnore
    public boolean isPhones() { return isRfc(ContactByEnum.PHONES); }

    public static ContactByType rfc(ContactByEnum rfcValue) { return ContactByType.builder().rfcValue(rfcValue).build(); }

    /**
     * Returns an "addresses" contact channel type.
     *
     * @return an "addresses" contact channel type.
     */
    public static ContactByType addresses() { return rfc(ContactByEnum.ADDRESSES);}

    /**
     * Returns an "emails" contact channel type.
     *
     * @return an "emails" contact channel type.
     */
    public static ContactByType emails() { return rfc(ContactByEnum.EMAILS);}

    /**
     * Returns an "onlineServices" contact channel type.
     *
     * @return an "onlineServices" contact channel type.
     */
    public static ContactByType onlineServices() { return rfc(ContactByEnum.ONLINE_SERVICES);}

    /**
     * Returns a "phones" contact channel type.
     *
     * @return a "phones" contact channel type.
     */
    public static ContactByType phones() { return rfc(ContactByEnum.PHONES);}

    /**
     * Returns a custom contact channel type.
     *
     * @param extValue the custom contact channel type in text format
     * @return a custom contact channel type
     */
    public static ContactByType ext(String extValue) { return ContactByType.builder().extValue(V_Extension.toV_Extension(extValue)).build(); }

    @Override
    public int compareTo(ContactByType o) {

        return this.toString().compareTo(o.toString());
    }

    @Override
    public String toString() {
        return (this.isRfcValue()) ? this.getRfcValue().toString() : this.getExtValue().toString();
    }
}

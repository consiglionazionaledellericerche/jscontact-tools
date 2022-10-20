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

/**
 * Class mapping the "preferredContactChannels" map keys as defined in section 2.3.4 of [draft-ietf-calext-jscontact].
 *
 * @see <a href="https://datatracker.ietf.org/doc/draft-ietf-calext-jscontact#section-2.3.4">draft-ietf-calext-jscontact</a>
 * @author Mario Loffredo
 */
@Getter
@Setter
@ToString(callSuper = true)
@NoArgsConstructor
@SuperBuilder
public class ChannelType extends ExtensibleEnum<ChannelEnum> implements Serializable, Comparable<ChannelType> {

    public boolean isRfc(ChannelEnum value) { return isRfcValue() && rfcValue == value; }

    /**
     * Tests if this contact channel type  is "addresses".
     *
     * @return true if this contact channel type is "addresses", false otherwise
     */
    @JsonIgnore
    public boolean isAddresses() { return isRfc(ChannelEnum.ADDRESSES); }

    /**
     * Tests if this contact channel type  is "emails".
     *
     * @return true if this contact channel type is "emails", false otherwise
     */
    @JsonIgnore
    public boolean isEmails() { return isRfc(ChannelEnum.EMAILS); }

    /**
     * Tests if this contact channel type  is "onlineServices".
     *
     * @return true if this contact channel type is "onlineServices", false otherwise
     */
    @JsonIgnore
    public boolean isOnlineServices() { return isRfc(ChannelEnum.ONLINE_SERVICES); }

    /**
     * Tests if this contact channel type  is "phones".
     *
     * @return true if this contact channel type is "phones", false otherwise
     */
    @JsonIgnore
    public boolean isPhones() { return isRfc(ChannelEnum.PHONES); }

    /**
     * Tests if this is a custom contact channel type.
     *
     * @return true if this is a custom contact channel type, false otherwise
     */
    @JsonIgnore
    public boolean isExt() { return isExtValue(); }

    public static ChannelType rfc(ChannelEnum rfcValue) { return ChannelType.builder().rfcValue(rfcValue).build(); }

    /**
     * Returns an "addresses" contact channel type.
     *
     * @return an "addresses" contact channel type.
     */
    public static ChannelType addresses() { return rfc(ChannelEnum.ADDRESSES);}

    /**
     * Returns an "emails" contact channel type.
     *
     * @return an "emails" contact channel type.
     */
    public static ChannelType emails() { return rfc(ChannelEnum.EMAILS);}

    /**
     * Returns an "onlineServices" contact channel type.
     *
     * @return an "onlineServices" contact channel type.
     */
    public static ChannelType onlineServices() { return rfc(ChannelEnum.ONLINE_SERVICES);}

    /**
     * Returns an "phones" contact channel type.
     *
     * @return an "phones" contact channel type.
     */
    public static ChannelType phones() { return rfc(ChannelEnum.PHONES);}

    /**
     * Returns a custom contact channel type.
     *
     * @return a custom contact channel type
     */
    public static ChannelType ext(String extValue) { return ChannelType.builder().extValue(extValue).build(); }

    @Override
    public int compareTo(ChannelType o) {

        return this.toString().compareTo(o.toString());
    }

    @Override
    public String toString() {
        return (this.isRfcValue()) ? this.getRfcValue().toString() : this.getExtValue();
    }
}

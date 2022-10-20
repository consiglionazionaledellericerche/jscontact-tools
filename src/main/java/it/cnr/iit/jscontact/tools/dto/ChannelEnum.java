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
import it.cnr.iit.jscontact.tools.dto.utils.EnumUtils;
import lombok.AllArgsConstructor;

import java.util.HashMap;
import java.util.Map;

/**
 * Enum class mapping the "preferredContactChannels" map keys as defined in section 2.3.4 of [draft-ietf-calext-jscontact].
 *
 * @see <a href="https://datatracker.ietf.org/doc/draft-ietf-calext-jscontact#section-2.3.4">draft-ietf-calext-jscontact</a>
 * @author Mario Loffredo
 */
@AllArgsConstructor
public enum ChannelEnum implements IsExtensible {

    ADDRESSES("addresses"),
    EMAILS("emails"),
    ONLINE_SERVICES("onlineServices"),
    PHONES("phones");

    private static final Map<String, ChannelEnum> aliases = new HashMap<String, ChannelEnum>()
    {{
        put("tel", PHONES);
        put("impp", ONLINE_SERVICES);
        put("email", EMAILS);
        put("adr", ADDRESSES);
    }};

    private final String value;

    @JsonValue
    public String getValue() {
        return value;
    }

    @JsonCreator
    public static ChannelEnum getEnum(String value) throws IllegalArgumentException {
        return (value == null) ? null : EnumUtils.getEnum(ChannelEnum.class, value, aliases);
    }

    @Override
    public String toString() {
        return value;
    }

    /**
     * Returns the vCard 4.0 CONTACT-CHANNEL-PREF value corresponding to the enum value representing the channel type.
     *
     * @param channel the channel type
     * @return the vCard 4.0 CONTACT-CHANNEL-PREF value
     * @see <a href="https://datatracker.ietf.org/doc/draft-ietf-calext-vcard-jscontact-extensions">draft-ietf-calext-vcard-jscontact-extensions</a>
     */
    @JsonIgnore
    public static String toVCardChannelType(ChannelEnum channel) {

        if (channel == null)
            return null;

        for (Map.Entry<String,ChannelEnum> entry : aliases.entrySet()) {
            if (entry.getValue() == channel)
                return entry.getKey().toUpperCase();
        }

        return null;
    }

}


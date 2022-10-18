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
package it.cnr.iit.jscontact.tools.dto.deserializers;

import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.KeyDeserializer;
import it.cnr.iit.jscontact.tools.dto.*;
import lombok.NoArgsConstructor;

import java.io.IOException;
import java.util.*;

/**
 * Custom JSON deserializer for the "preferredContactChannels" map.
 *
 * @author Mario Loffredo
 */
@NoArgsConstructor
public class ContactChannelsKeyDeserializer extends KeyDeserializer {

    @Override
    public ChannelType deserializeKey(String key, DeserializationContext ctxt) {
        //Use the string key here to return a real map key object
        ChannelType channelType;
        try {
            channelType = ChannelType.builder().rfcValue(ChannelEnum.getEnum(key)).build();
        } catch (IllegalArgumentException e) {
            channelType = ChannelType.builder().extValue(key).build();
        }
        return channelType;
    }
}

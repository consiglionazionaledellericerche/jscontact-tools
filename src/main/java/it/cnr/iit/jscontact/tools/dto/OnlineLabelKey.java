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

import it.cnr.iit.jscontact.tools.dto.utils.LabelUtils;
import lombok.AllArgsConstructor;

import java.util.Arrays;
import java.util.List;

/**
 * Enum class mapping the values included in the "label" property of the Resource type that is used to represent an online resource as defined in section 2.3.3 of [draft-ietf-jmap-jscontact].
 * The values are those corresponding to vCard 4.0 [RFC6350] properties that are not directly mapped to a JSContact property.
 *
 * @see <a href="https://datatracker.ietf.org/doc/draft-ietf-jmap-jscontact#section-2.4.1">draft-ietf-jmap-jscontact</a>
 * @see <a href="https://datatracker.ietf.org/doc/rfc6350">RFC6350</a>
 * @author Mario Loffredo
 */
@AllArgsConstructor
public enum OnlineLabelKey {

    KEY("key"),
    LOGO("logo"),
    SOURCE("source"),
    SOUND("sound"),
    URL("url"),
    CALURI("caluri"),
    CALADRURI("caladruri"),
    FBURL("fburl"),
    ORG_DIRECTORY("org-directory"),
    CONTACT_URI("contact-uri"),
    IMPP("XMPP");

    private String value;

    public String getValue() {
        return value;
    }

    /**
     * Returns a key furtherly specifying the online resource type. The online key can be found among the comma separated items included in the "label" property of the Resource type.
     *
     * @param label a text including a comma separated list of items detailing an online resource
     * @return the online resource key found in the label, null if no online resource key is found
     */
    public static OnlineLabelKey getLabelKey(String label) {

        List<String> labelItems = Arrays.asList(label.split(LabelUtils.LABEL_DELIMITER));
        for (OnlineLabelKey key : OnlineLabelKey.values())
            if (labelItems.contains(key.getValue()))
                return key;

         return null;
    }
}

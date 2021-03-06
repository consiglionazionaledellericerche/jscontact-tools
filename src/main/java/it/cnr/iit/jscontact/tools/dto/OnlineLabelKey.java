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

    public static OnlineLabelKey getLabelKey(String label) {

        List<String> labelItems = Arrays.asList(label.split(LabelUtils.LABEL_DELIMITER));
        for (OnlineLabelKey key : OnlineLabelKey.values())
            if (labelItems.contains(key.getValue()))
                return key;

         return null;
    }
}

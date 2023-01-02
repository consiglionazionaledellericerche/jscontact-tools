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
 * Class mapping the values of the "type" property of the DirectoryResource type as defined in section 2.6.2 of [draft-ietf-calext-jscontact].
 *
 * @see <a href="https://datatracker.ietf.org/doc/draft-ietf-calext-jscontact#section-2.6.2">draft-ietf-calext-jscontact</a>
 * @author Mario Loffredo
 */
@Getter
@Setter
@ToString(callSuper = true)
@NoArgsConstructor
@SuperBuilder
public class DirectoryResourceType extends ExtensibleEnumType<DirectoryResourceEnum> implements Serializable {

    /**
     * Tests if this directory resource type is "directory".
     *
     * @return true if directory resource type is "directory", false otherwise
     */
    @JsonIgnore
    public boolean isDirectory() { return isRfc(DirectoryResourceEnum.DIRECTORY); }

    /**
     * Tests if this directory resource type is "entry".
     *
     * @return true if directory resource type is "entry", false otherwise
     */
    @JsonIgnore
    public boolean isEntry() { return isRfc(DirectoryResourceEnum.ENTRY); }


    private static DirectoryResourceType rfc(DirectoryResourceEnum rfcValue) { return DirectoryResourceType.builder().rfcValue(rfcValue).build(); }

    /**
     * Returns a "directory" directory resource type.
     *
     * @return a "directory" directory resource type
     */
    public static DirectoryResourceType directory() { return rfc(DirectoryResourceEnum.DIRECTORY);}

    /**
     * Returns an "entry" directory resource type.
     *
     * @return an "entry" directory resource type
     */
    public static DirectoryResourceType entry() { return rfc(DirectoryResourceEnum.ENTRY);}

    /**
     * Returns a custom directory resource type.
     *
     * @param extValue a custom directory source type in text format
     * @return a custom directory resource type
     */
    public static DirectoryResourceType ext(String extValue) { return DirectoryResourceType.builder().extValue(V_Extension.toV_Extension(extValue)).build(); }
}

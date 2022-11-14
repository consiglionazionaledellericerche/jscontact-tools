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

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import it.cnr.iit.jscontact.tools.constraints.CardGroupKindConstraint;
import lombok.*;
import lombok.experimental.SuperBuilder;
import org.apache.commons.lang3.SerializationUtils;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.util.LinkedHashMap;
import java.util.Map;


/**
 * Class mapping the CardGroup object as defined in section 3 of [draft-ietf-calext-jscontact].
 *
 * @see <a href="https://datatracker.ietf.org/doc/draft-ietf-calext-jscontact#section-3">draft-ietf-calext-jscontact</a>
 * @author Mario Loffredo
 */
@JsonPropertyOrder({"@type","uid","members","card","ietf.org:rfc0000:props"})
@CardGroupKindConstraint
@JsonInclude(JsonInclude.Include.NON_NULL)
@NoArgsConstructor
@ToString(callSuper = true)
@Getter
@Setter
@SuperBuilder
public class CardGroup extends JSContact implements Serializable {

    @NotNull
    @Pattern(regexp = "CardGroup", message="invalid @type value in CardGroup")
    @JsonProperty("@type")
    @Builder.Default
    String _type = "CardGroup";
    
    @NotNull
    @Size(min=1)
    @JsonProperty(required = true)
    Map<String,Boolean> members;

    @Valid
    Card card;

    /**
     * Adds a member to this object.
     *
     * @param member the uid value of the object representing a group member
     */
    public void addMember(String member) {

        if(members == null)
            members = new LinkedHashMap<>();

        members.putIfAbsent(member,Boolean.TRUE);
    }

    /**
     * Clones this object.
     *
     * @return the clone of this CardGroup object
     */
    @Override
    public CardGroup clone() {
        return SerializationUtils.clone(this);
    }

}

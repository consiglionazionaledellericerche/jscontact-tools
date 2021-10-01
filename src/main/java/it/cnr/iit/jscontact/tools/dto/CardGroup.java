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

    String name;

    @Valid
    Card card;

    public void addMember(String member) {

        if(members == null)
            members = new LinkedHashMap<>();

        members.putIfAbsent(member,Boolean.TRUE);
    }

    public CardGroup clone() {
        return SerializationUtils.clone(this);
    }

}

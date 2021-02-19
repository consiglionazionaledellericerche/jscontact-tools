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
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import it.cnr.iit.jscontact.tools.constraints.JSCardGroupConstraint;
import it.cnr.iit.jscontact.tools.dto.interfaces.JSContact;
import lombok.*;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.LinkedHashMap;
import java.util.Map;

@JSCardGroupConstraint
@JsonInclude(JsonInclude.Include.NON_NULL)
@Data
@AllArgsConstructor
@NoArgsConstructor
public class JSCardGroup extends JSCard implements JSContact {

    @Builder(builderMethodName = "jsCardGroupBuilder")
    public JSCardGroup(String uid) {
        super();
        this.setUid(uid);
        this.setKind(KindType.builder().rfcValue(it.cnr.iit.jscontact.tools.dto.Kind.GROUP).build());
    }

    @NotNull
    @Size(min=1)
    Map<String,Boolean> members;

    public void addMember(String member) {

        if(members == null)
            members = new LinkedHashMap<String,Boolean>();

        if (!members.containsKey(member))
            members.put(member,Boolean.TRUE);
    }


}

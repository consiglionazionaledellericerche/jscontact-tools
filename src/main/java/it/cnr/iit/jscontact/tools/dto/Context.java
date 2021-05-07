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
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Getter
@Setter
@ToString(callSuper = true)
@NoArgsConstructor
@SuperBuilder
public class Context extends ExtensibleType<ContextEnum> implements Serializable {

    private boolean isRfc(ContextEnum value) { return isRfcValue() && rfcValue == value; }
    @JsonIgnore
    public boolean isPrivate() { return isRfc(ContextEnum.PRIVATE); }
    @JsonIgnore
    public boolean isWork() { return isRfc(ContextEnum.WORK); }
    @JsonIgnore
    public boolean isOther() { return isRfc(ContextEnum.OTHER); }

    public static Context rfc(ContextEnum rfcValue) { return Context.builder().rfcValue(rfcValue).build();}
    public static Context private_() { return rfc(ContextEnum.PRIVATE);}
    public static Context work() { return rfc(ContextEnum.WORK);}
    public static Context other() { return rfc(ContextEnum.OTHER);}
    public static Context ext(String extValue) { return Context.builder().extValue(extValue).build(); }

    public static List<ContextEnum> getContextEnumValues(Collection<Context> contexts) {

        if (contexts == null)
            return null;

        List<ContextEnum> enumValues = new ArrayList<>();
        for (Context context : contexts) {
            if (context.rfcValue != null)
                enumValues.add(context.getRfcValue());
        }

        return enumValues;
    }

}

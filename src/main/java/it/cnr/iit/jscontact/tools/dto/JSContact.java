
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

import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.fasterxml.jackson.annotation.JsonAnySetter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.deser.std.DateDeserializers;
import com.fasterxml.jackson.databind.ser.std.CalendarSerializer;
import it.cnr.iit.jscontact.tools.constraints.*;
import it.cnr.iit.jscontact.tools.dto.deserializers.KindTypeDeserializer;
import it.cnr.iit.jscontact.tools.dto.serializers.KindTypeSerializer;
import it.cnr.iit.jscontact.tools.dto.utils.LabelUtils;
import it.cnr.iit.jscontact.tools.dto.utils.NoteUtils;
import lombok.*;
import lombok.experimental.SuperBuilder;
import org.apache.commons.lang3.ArrayUtils;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.*;


@JsonInclude(JsonInclude.Include.NON_NULL)
@NoArgsConstructor
@Getter
@Setter
@ToString
@EqualsAndHashCode(of={"uid"})
@SuperBuilder
public abstract class JSContact extends ValidableObject implements Serializable {

    //Metadata properties
    @NotNull(message = "uid is missing in JSContact")
    @NonNull
    String uid;


}

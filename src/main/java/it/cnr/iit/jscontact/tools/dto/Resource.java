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
import com.fasterxml.jackson.annotation.JsonInclude;
import it.cnr.iit.jscontact.tools.constraints.BooleanMapConstraint;
import it.cnr.iit.jscontact.tools.constraints.UriResourceConstraint;
import it.cnr.iit.jscontact.tools.dto.interfaces.HasIndex;
import it.cnr.iit.jscontact.tools.dto.interfaces.IdMapValue;
import it.cnr.iit.jscontact.tools.dto.interfaces.hasContext;
import it.cnr.iit.jscontact.tools.dto.utils.HasIndexUtils;
import it.cnr.iit.jscontact.tools.dto.utils.LabelUtils;
import lombok.*;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.Map;

@UriResourceConstraint
@JsonInclude(JsonInclude.Include.NON_NULL)
@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Resource extends GroupableObject implements HasIndex, Comparable<Resource>, IdMapValue, Serializable, hasContext {

    @NotNull(message = "resource is missing in Resource")
    @NonNull
    String resource;

    @Builder.Default
    ResourceType type = ResourceType.OTHER;

    String mediaType;

    @BooleanMapConstraint(message = "invalid Map<Context,Boolean> contexts in Resource - Only Boolean.TRUE allowed")
    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    @Singular(ignoreNullCollections = true)
    Map<Context,Boolean> contexts;

    String label;

    @Min(value=1, message = "invalid pref in Resource - value must be greater or equal than 1")
    @Max(value=100, message = "invalid pref in Resource - value must be less or equal than 100")
    Integer pref;

    @JsonIgnore
    Integer index;

    //to compare VCard ORG-DIRECTORY instances based on index
    @Override
    public int compareTo(Resource o) {

        return HasIndexUtils.compareTo(this, o);
    }

    @JsonIgnore
    public boolean isUri() { return type == ResourceType.URI; }
    @JsonIgnore
    public boolean isUsername() { return type == ResourceType.USERNAME; }
    @JsonIgnore
    public boolean isOtherResource() { return type == ResourceType.OTHER; }

    public boolean asCaladruri() { return LabelUtils.labelIncludesItem(label,OnlineLabelKey.CALADRURI.getValue()); }
    public boolean asCaluri() { return LabelUtils.labelIncludesItem(label,OnlineLabelKey.CALURI.getValue()); }
    public boolean asContactUri() { return LabelUtils.labelIncludesItem(label,OnlineLabelKey.CONTACT_URI.getValue()); }
    public boolean asFburl() { return LabelUtils.labelIncludesItem(label,OnlineLabelKey.FBURL.getValue()); }
    public boolean asKey() { return LabelUtils.labelIncludesItem(label,OnlineLabelKey.KEY.getValue()); }
    public boolean asImpp() { return LabelUtils.labelIncludesItem(label,OnlineLabelKey.IMPP.getValue()); }
    public boolean asLogo() { return LabelUtils.labelIncludesItem(label,OnlineLabelKey.LOGO.getValue()); }
    public boolean asOrgDirectory() { return LabelUtils.labelIncludesItem(label,OnlineLabelKey.ORG_DIRECTORY.getValue()); }
    public boolean asSound() { return LabelUtils.labelIncludesItem(label,OnlineLabelKey.SOUND.getValue()); }
    public boolean asSource() { return LabelUtils.labelIncludesItem(label,OnlineLabelKey.SOURCE.getValue()); }
    public boolean asUrl() { return LabelUtils.labelIncludesItem(label,OnlineLabelKey.URL.getValue()); }

}

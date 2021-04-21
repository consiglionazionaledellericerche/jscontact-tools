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
import it.cnr.iit.jscontact.tools.dto.interfaces.HasContext;
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
public class Resource extends GroupableObject implements HasIndex, Comparable<Resource>, IdMapValue, Serializable, HasContext {

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

    private boolean asResource(OnlineLabelKey labelKey) { return LabelUtils.labelIncludesItem(label,labelKey.getValue()); }
    public boolean asCaladruri() { return asResource(OnlineLabelKey.CALADRURI); }
    public boolean asCaluri() { return asResource(OnlineLabelKey.CALURI); }
    public boolean asContactUri() { return asResource(OnlineLabelKey.CONTACT_URI); }
    public boolean asFburl() { return asResource(OnlineLabelKey.FBURL); }
    public boolean asKey() { return asResource(OnlineLabelKey.KEY); }
    public boolean asImpp() { return asResource(OnlineLabelKey.IMPP); }
    public boolean asLogo() { return asResource(OnlineLabelKey.LOGO); }
    public boolean asOrgDirectory() { return asResource(OnlineLabelKey.ORG_DIRECTORY); }
    public boolean asSound() { return asResource(OnlineLabelKey.SOUND); }
    public boolean asSource() { return asResource(OnlineLabelKey.SOURCE); }
    public boolean asUrl() { return asResource(OnlineLabelKey.URL); }

}

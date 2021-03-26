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

import com.fasterxml.jackson.annotation.*;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import it.cnr.iit.jscontact.tools.constraints.EmailsConstraint;
import it.cnr.iit.jscontact.tools.constraints.JSContactMapsConstraint;
import it.cnr.iit.jscontact.tools.constraints.OnlineConstraint;
import it.cnr.iit.jscontact.tools.constraints.PhonesConstraint;
import it.cnr.iit.jscontact.tools.dto.deserializers.KindDeserializer;
import it.cnr.iit.jscontact.tools.dto.serializers.KindSerializer;
import lombok.*;
import lombok.experimental.SuperBuilder;
import org.apache.commons.lang3.ArrayUtils;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.*;


@JSContactMapsConstraint
@JsonInclude(JsonInclude.Include.NON_NULL)
@NoArgsConstructor
@Getter
@Setter
@ToString
@EqualsAndHashCode(of={"uid"})
@SuperBuilder
public abstract class JSContact extends ValidableObject {

    @NotNull(message = "uid is missing in JSCard")
    @NonNull
    String uid;

    String prodId;

    String updated;

    String created;

    @JsonSerialize(using = KindSerializer.class)
    @JsonDeserialize(using = KindDeserializer.class)
    KindType kind;

    Map<String,Relation> relatedTo;

    @Valid
    LocalizedString fullName;

    @Valid
    NameComponent[] name;

    @Valid
    LocalizedString[] nickNames;

    @Valid
    LocalizedString[] organizations;

    @Valid
    LocalizedString[] jobTitles;

    @Valid
    LocalizedString[] roles;

    @Valid
    @EmailsConstraint(message = "invalid email Resource in JSCard")
    Resource[] emails;

    @Valid
    @PhonesConstraint(message = "invalid phone Resource in JSCard")
    Resource[] phones;

    @Valid
    @OnlineConstraint(message = "invalid online Resource in JSCard")
    Resource[] online;

    @Valid
    File[] photos;

    PreferredContactMethodType preferredContactMethod;

    Map<String, ContactLanguage[]> preferredContactLanguages;

    @Valid
    Address[] addresses;

    @Valid
    Anniversary[] anniversaries;

    @Valid
    PersonalInformation[] personalInfo;

    @Valid
    LocalizedString[] notes;

    Map<String,Boolean> categories;

    Map<String,String> extensions;

    @JsonAnyGetter
    public Map<String, String> getExtensions() {
        return extensions;
    }

    @JsonAnySetter
    public void setExtension(String name, String value) {

        if (extensions == null)
            extensions = new HashMap<String,String>();

        extensions.put(name, value);
    }

    public void addName(NameComponent nc) {
        name = ArrayUtils.add(name, nc);
    }

    public void addNickName(LocalizedString nick) {
        nickNames = ArrayUtils.add(nickNames, nick);
    }

    public void addPhone(Resource phone) {
        phones = ArrayUtils.add(phones, phone);
    }

    public void addEmail(Resource email) {
        emails = ArrayUtils.add(emails, email);
    }

    public void addOnline(Resource ol) {
        online = ArrayUtils.add(online, ol);
    }

    public void addPhoto(File f) {
        photos = ArrayUtils.add(photos, f);
    }

    public void addOrganization(LocalizedString org) {
        organizations = ArrayUtils.add(organizations, org);
    }

    public void addTitle(LocalizedString title) {
        jobTitles = ArrayUtils.add(jobTitles, title);
    }

    public void addRole(LocalizedString rl) {
        roles = ArrayUtils.add(roles, rl);
    }

    public void addNote(LocalizedString note) {
        notes = ArrayUtils.add(notes, note);
    }

    public void addPersonalInfo(PersonalInformation pi) {
        personalInfo = ArrayUtils.add(personalInfo, pi);
    }

    public void addAnniversary(Anniversary anniversary) {
        anniversaries = ArrayUtils.add(anniversaries, anniversary);
    }

    public void addAddress(Address address) {
        addresses = ArrayUtils.add(addresses, address);
    }

    private void addCategory(String category) {

        if(categories == null)
            categories = new LinkedHashMap<String,Boolean>();

        if (!categories.containsKey(category))
            categories.put(category,Boolean.TRUE);
    }

    public void addCategories(String[] categories) {
        if (categories==null)
            return;

        for (String category: categories)
            addCategory(category);
    }

    public void addRelation(String key, RelationType relType) {

        if (relatedTo == null)
            relatedTo = new HashMap<String, Relation>();

        Relation relationPerKey = relatedTo.get(key);
        if (relationPerKey == null) {
            if (relType == null)
                relatedTo.put(key, Relation.builder().build());
            else
                relatedTo.put(key, Relation.builder()
                        .relation(new HashMap<String, Boolean>() {{
                            put(relType.getValue(), Boolean.TRUE);
                        }})
                        .build());
        }
        else {
            Map<String, Boolean> map = relationPerKey.getRelation();
            map.put(relType.getValue(), Boolean.TRUE);
            relatedTo.replace(key, Relation.builder()
                    .relation(map)
                    .build());
        }
    }

    public void addContactLanguage(String key, ContactLanguage contactLanguage) {

        if (preferredContactLanguages == null)
            preferredContactLanguages = new HashMap<String, ContactLanguage[]>();

        ContactLanguage[] languagesPerKey = preferredContactLanguages.get(key);
        if (languagesPerKey == null)
            preferredContactLanguages.put(key, new ContactLanguage[] {contactLanguage});
        else
            preferredContactLanguages.put(key, ArrayUtils.add(languagesPerKey, contactLanguage));
    }

    public void addFullName(String fn, String language) {
        if (fullName == null)
            fullName = LocalizedString.builder()
                    .value(fn)
                    .language(language)
                    .build();
        else
            fullName.addLocalization(language, fn);
    }

    public void addExtension(String key, String value) {
        if(extensions == null)
            extensions = new HashMap<String,String>();

        if (!extensions.containsKey(key))
            extensions.put(key,value);
    }

    @JsonIgnore
    public Resource[] getOnline(String label) {

        List<Resource> ols = new ArrayList<Resource>();
        for (Resource ol : online) {
            if (ol.getLabels().keySet().contains(label))
                ols.add(ol);
        }
        if (ols.size()==0)
            return null;

        return ols.toArray(new Resource[ols.size()]);
    }

    @JsonIgnore
    public Resource[] getOnline(LabelKey labelKey) {
        return getOnline(labelKey.getValue());
    }

    @JsonIgnore
    public Resource[] getOnlineKey() {
        return getOnline(LabelKey.KEY);
    }

    @JsonIgnore
    public Resource[] getOnlineUrl() {
        return getOnline(LabelKey.URL);
    }

    @JsonIgnore
    public Resource[] getOnlineSource() {
        return getOnline(LabelKey.SOURCE);
    }

    @JsonIgnore
    public Resource[] getOnlineLogo() {
        return getOnline(LabelKey.LOGO);
    }

    @JsonIgnore
    public Resource[] getOnlineSound() {
        return getOnline(LabelKey.SOUND);
    }

    @JsonIgnore
    public Resource[] getOnlineFburl() {
        return getOnline(LabelKey.FBURL);
    }

    @JsonIgnore
    public Resource[] getOnlineCaluri() {
        return getOnline(LabelKey.CALURI);
    }

    @JsonIgnore
    public Resource[] getOnlineCaladruri() {
        return getOnline(LabelKey.CALADRURI);
    }

    @JsonIgnore
    public Resource[] getOnlineOrgDirectory() {
        return getOnline(LabelKey.ORG_DIRECTORY);
    }

    @JsonIgnore
    public Resource[] getOnlineImpp() {
        return getOnline(LabelKey.IMPP);
    }

    @JsonIgnore
    public Resource[] getOnlineContactUri() {
        return getOnline(LabelKey.CONTACT_URI);
    }


}

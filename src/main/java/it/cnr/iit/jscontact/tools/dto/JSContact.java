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
import com.fasterxml.jackson.databind.deser.std.DateDeserializers;
import com.fasterxml.jackson.databind.ser.std.CalendarSerializer;
import it.cnr.iit.jscontact.tools.constraints.*;
import it.cnr.iit.jscontact.tools.dto.deserializers.KindDeserializer;
import it.cnr.iit.jscontact.tools.dto.serializers.KindSerializer;
import it.cnr.iit.jscontact.tools.dto.utils.NoteUtils;
import lombok.*;
import lombok.experimental.SuperBuilder;
import org.apache.commons.lang3.ArrayUtils;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.*;


@TitleOrganizationConstraint
@JsonInclude(JsonInclude.Include.NON_NULL)
@NoArgsConstructor
@Getter
@Setter
@ToString
@EqualsAndHashCode(of={"uid"})
@SuperBuilder
public abstract class JSContact extends ValidableObject {

    @NotNull(message = "uid is missing in JSContact")
    @NonNull
    String uid;

    String prodId;

    @JsonSerialize(using = CalendarSerializer.class)
    @JsonDeserialize(using = DateDeserializers.CalendarDeserializer.class)
    Calendar updated;

    @JsonSerialize(using = CalendarSerializer.class)
    @JsonDeserialize(using = DateDeserializers.CalendarDeserializer.class)
    Calendar created;

    @JsonSerialize(using = KindSerializer.class)
    @JsonDeserialize(using = KindDeserializer.class)
    KindType kind;

    @RelatedToConstraint
    Map<String,Relation> relatedTo;

    @Valid
    LocalizedString fullName;

    @Valid
    NameComponent[] name;

    @Valid
    LocalizedString[] nickNames;

    @Valid
    @IdMapConstraint(message = "invalid Id in Map<Id,Organization>")
    Map<String,Organization> organizations;

    @Valid
    @IdMapConstraint(message = "invalid Id in Map<Id,Title>")
    Map<String,Title> jobTitles;

    @Valid
    LocalizedString[] roles;

    //    @IdMapConstraint(message = "invalid Id in Map<Id,Email>")
    @Valid
    @EmailsConstraint(message = "invalid email Resource in JSContact")
    Resource[] emails;

    //    @IdMapConstraint(message = "invalid Id in Map<Id,Phone>")
    @Valid
    @PhonesConstraint(message = "invalid phone Resource in JSContact")
    Resource[] phones;

    //    @IdMapConstraint(message = "invalid Id in Map<Id,Resource>")
    @Valid
    @OnlineConstraint(message = "invalid online Resource in JSContact")
    Resource[] online;

    @Valid
    @IdMapConstraint(message = "invalid Id in Map<Id,File>")
    Map<String,File> photos;

    PreferredContactMethodType preferredContactMethod;

    @PreferredContactLanguagesConstraint
    Map<String, ContactLanguage[]> preferredContactLanguages;

    @Valid
    @IdMapConstraint(message = "invalid Id in Map<Id,Address>")
    Map<String,Address> addresses;

    @Valid
    Anniversary[] anniversaries;

    @Valid
    PersonalInformation[] personalInfo;

    @Valid
    LocalizedString notes;

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

    public void addPhoto(String id, File f) {

        if (photos == null)
            photos = new HashMap<String,File>();

        photos.put(id, f);
    }

    public void addOrganization(String id, LocalizedString name, LocalizedString[] units) {

        if(organizations == null)
            organizations = new HashMap<String,Organization>();

        if (!organizations.containsKey(id))
            organizations.put(id,Organization.builder().name(name).units(units).build());
    }

    public void addOrganization(String id, Organization organization) {
        if(organizations == null)
            organizations = new HashMap<String,Organization>();

        organizations.put(id,organization);
    }

    public void addOrganization(String id, LocalizedString name) {
        addOrganization(id, name, null);
    }

    public void addTitle(String id, LocalizedString title, String organization) {

        if(jobTitles == null)
            jobTitles = new HashMap<String,Title>();

        if (!jobTitles.containsKey(id))
            jobTitles.put(id,Title.builder().title(title).organization(organization).build());
    }

    public void addTitle(String id, LocalizedString title) {
        addTitle(id, title, null);
    }

    public void addRole(LocalizedString rl) {
        roles = ArrayUtils.add(roles, rl);
    }

    public void addNote(String note) { addNote(note, null); }

    public void addNote(String note, String language) {

        if (notes == null) {
            notes = LocalizedString.builder().value(note).language(language).build();
            return;
        }

        if (language == notes.getLanguage()) {
            notes.setValue(String.format("%s%s%s", notes.getValue(), NoteUtils.NOTE_DELIMITER, note));
            return;
        }

        if (notes.getLanguage() != null && language == null) {
            notes.addLocalization(notes.getLanguage(), notes.getValue());
            notes.setValue(note);
            notes.setLanguage(null);
        } else {
            if (notes.getLocalizations()!= null && notes.getLocalizations().containsKey(language))
                notes.getLocalizations().replace(language,String.format("%s%s%s", notes.getLocalizations().get(language), NoteUtils.NOTE_DELIMITER, note));
            else
                notes.addLocalization(language, note);
        }

    }

    public void addPersonalInfo(PersonalInformation pi) {
        personalInfo = ArrayUtils.add(personalInfo, pi);
    }

    public void addAnniversary(Anniversary anniversary) {
        anniversaries = ArrayUtils.add(anniversaries, anniversary);
    }

    public void addAddress(String id, Address address) {

        if(addresses == null)
            addresses = new HashMap<String,Address>();

        if (!addresses.containsKey(id))
            addresses.put(id,address);
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

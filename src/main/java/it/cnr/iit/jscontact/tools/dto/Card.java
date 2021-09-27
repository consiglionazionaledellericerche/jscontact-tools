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
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.deser.std.DateDeserializers;
import it.cnr.iit.jscontact.tools.constraints.*;
import it.cnr.iit.jscontact.tools.constraints.groups.CardConstraintsGroup;
import it.cnr.iit.jscontact.tools.dto.deserializers.KindTypeDeserializer;
import it.cnr.iit.jscontact.tools.dto.serializers.KindTypeSerializer;
import it.cnr.iit.jscontact.tools.dto.serializers.UTCDateTimeSerializer;
import it.cnr.iit.jscontact.tools.dto.utils.LabelUtils;
import it.cnr.iit.jscontact.tools.dto.utils.NoteUtils;
import lombok.*;
import lombok.experimental.SuperBuilder;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.SerializationUtils;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import java.io.Serializable;
import java.util.Calendar;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

@TitleOrganizationConstraint
@CardKindConstraint(groups = CardConstraintsGroup.class)
@NoArgsConstructor
@Getter
@Setter
@ToString(callSuper = true)
@SuperBuilder
public class Card extends JSContact implements Serializable {

    @NotNull
    @Pattern(regexp = "Card", message="Invalid @type value in Card")
    @JsonProperty("@type")
    @Builder.Default
    String _type = "Card";

    String prodId;

    @JsonSerialize(using = UTCDateTimeSerializer.class)
    @JsonDeserialize(using = DateDeserializers.CalendarDeserializer.class)
    Calendar created;

    @JsonSerialize(using = UTCDateTimeSerializer.class)
    @JsonDeserialize(using = DateDeserializers.CalendarDeserializer.class)
    Calendar updated;

    @JsonSerialize(using = KindTypeSerializer.class)
    @JsonDeserialize(using = KindTypeDeserializer.class)
    KindType kind;

    @RelatedToConstraint
    Map<String,Relation> relatedTo;

    //Name and Organization properties
    @Valid
    NameComponent[] name;

    @Valid
    LocalizedString fullName;

    @Valid
    LocalizedString[] nickNames;

    @Valid
    @IdMapConstraint(message = "invalid Id in Map<Id,Organization>")
    Map<String,Organization> organizations;

    @Valid
    @IdMapConstraint(message = "invalid Id in Map<Id,Title>")
    Map<String,Title> titles;

    //Contact and Resource properties
    @Valid
    @IdMapConstraint(message = "invalid Id in Map<Id,Email>")
    Map<String,EmailAddress> emails;

    @Valid
    @IdMapConstraint(message = "invalid Id in Map<Id,Phone>")
    Map<String,Phone> phones;

    @Valid
    @IdMapConstraint(message = "invalid Id in Map<Id,Resource>")
    Map<String,Resource> online;

    @Valid
    @IdMapConstraint(message = "invalid Id in Map<Id,File>")
    Map<String,File> photos;

    PreferredContactMethodType preferredContactMethod;

    @PreferredContactLanguagesConstraint
    Map<String, ContactLanguage[]> preferredContactLanguages;

    //Address and Location properties
    @Valid
    @IdMapConstraint(message = "invalid Id in Map<Id,Address>")
    Map<String,Address> addresses;

    //Additional properties
    @Valid
    @IdMapConstraint(message = "invalid Id in Map<Id,Anniversary>")
    Map<String,Anniversary> anniversaries;

    @Valid
    @IdMapConstraint(message = "invalid Id in Map<Id,PersonalInformation>")
    Map<String,PersonalInformation> personalInfo;

    @Valid
    LocalizedString notes;

    @BooleanMapConstraint(message = "invalid Map<String,Boolean> categories in JSContact - Only Boolean.TRUE allowed")
    Map<String,Boolean> categories;

    @Valid
    Map<String,TimeZone> timeZones;

    Map<String,String> extensions;


    private boolean isContactByMethodPreferred(PreferredContactMethodType method) {return preferredContactMethod != null && preferredContactMethod == method; }
    @JsonIgnore
    public boolean isContactByEmailsPreferred() {return isContactByMethodPreferred(PreferredContactMethodType.EMAILS); }
    @JsonIgnore
    public boolean isContactByPhonesPreferred() {return isContactByMethodPreferred(PreferredContactMethodType.PHONES); }
    @JsonIgnore
    public boolean isContactByOnlinePreferred() {return isContactByMethodPreferred(PreferredContactMethodType.ONLINE); }

//Methods for adding items to a mutable collection

    public void addRelation(String key, RelationType relType) {

        if (relatedTo == null)
            relatedTo = new HashMap<>();

        Relation relationPerKey = relatedTo.get(key);
        if (relationPerKey == null) {
            if (relType == null)
                relatedTo.put(key, Relation.builder().build());
            else
                relatedTo.put(key, Relation.builder()
                        .relation(new HashMap<RelationType, Boolean>() {{
                            put(relType, Boolean.TRUE);
                        }})
                        .build());
        }
        else {
            Map<RelationType, Boolean> map = relationPerKey.getRelation();
            map.put(relType, Boolean.TRUE);
            relatedTo.replace(key, Relation.builder()
                    .relation(map)
                    .build());
        }
    }

    public void addName(NameComponent nc) {
        name = ArrayUtils.add(name, nc);
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

    public void addNickName(LocalizedString nick) {
        nickNames = ArrayUtils.add(nickNames, nick);
    }

    public void addOrganization(String id, Organization organization) {
        if(organizations == null)
            organizations = new HashMap<>();

        organizations.put(id,organization);
    }

    private void addTitle(String id, LocalizedString title, String organization) {

        if(titles == null)
            titles = new HashMap<>();

        if (!titles.containsKey(id))
            titles.put(id,Title.builder().title(title).organization(organization).build());
    }

    public void addTitle(String id, LocalizedString title) {
        addTitle(id, title, null);
    }

    public void addEmail(String id, EmailAddress email) {

        if (emails == null)
            emails = new HashMap<>();

        emails.put(id, email);
    }

    public void addPhone(String id, Phone phone) {

        if (phones == null)
            phones = new HashMap<>();

        phones.put(id, phone);
    }

    public void addOnline(String id, Resource ol) {

        if (online == null)
            online = new HashMap<>();

        online.put(id, ol);
    }

    @JsonIgnore
    private Map<String,Resource> getOnline(String label) {

        Map<String,Resource> ols = new HashMap<>();
        for (Map.Entry<String,Resource> ol : online.entrySet()) {
            if (LabelUtils.labelIncludesItem(ol.getValue().getLabel(), label))
                ols.put(ol.getKey(),ol.getValue());
        }
        if (ols.size()==0)
            return null;

        return ols;
    }

    @JsonIgnore
    private Map<String,Resource> getOnline(OnlineLabelKey labelKey) {
        return getOnline(labelKey.getValue());
    }

    @JsonIgnore
    public Map<String,Resource> getOnlineKey() {
        return getOnline(OnlineLabelKey.KEY);
    }

    @JsonIgnore
    public Map<String,Resource> getOnlineUrl() {
        return getOnline(OnlineLabelKey.URL);
    }

    @JsonIgnore
    public Map<String,Resource> getOnlineSource() {
        return getOnline(OnlineLabelKey.SOURCE);
    }

    @JsonIgnore
    public Map<String,Resource> getOnlineLogo() {
        return getOnline(OnlineLabelKey.LOGO);
    }

    @JsonIgnore
    public Map<String,Resource> getOnlineSound() {
        return getOnline(OnlineLabelKey.SOUND);
    }

    @JsonIgnore
    public Map<String,Resource> getOnlineFburl() {
        return getOnline(OnlineLabelKey.FBURL);
    }

    @JsonIgnore
    public Map<String,Resource> getOnlineCaluri() {
        return getOnline(OnlineLabelKey.CALURI);
    }

    @JsonIgnore
    public Map<String,Resource> getOnlineCaladruri() {
        return getOnline(OnlineLabelKey.CALADRURI);
    }

    @JsonIgnore
    public Map<String,Resource> getOnlineOrgDirectory() {
        return getOnline(OnlineLabelKey.ORG_DIRECTORY);
    }

    @JsonIgnore
    public Map<String,Resource> getOnlineImpp() {
        return getOnline(OnlineLabelKey.IMPP);
    }

    @JsonIgnore
    public Map<String,Resource> getOnlineContactUri() {
        return getOnline(OnlineLabelKey.CONTACT_URI);
    }

    public void addPhoto(String id, File f) {

        if (photos == null)
            photos = new HashMap<>();

        photos.put(id, f);
    }

    public void addContactLanguage(String key, ContactLanguage contactLanguage) {

        if (preferredContactLanguages == null)
            preferredContactLanguages = new HashMap<>();

        ContactLanguage[] languagesPerKey = preferredContactLanguages.get(key);
        if (languagesPerKey == null)
            preferredContactLanguages.put(key, new ContactLanguage[] {contactLanguage});
        else
            preferredContactLanguages.put(key, ArrayUtils.add(languagesPerKey, contactLanguage));
    }

    public void addAddress(String id, Address address) {

        if(addresses == null)
            addresses = new HashMap<>();

        if (!addresses.containsKey(id))
            addresses.put(id,address);
    }

    public void addAnniversary(String id, Anniversary anniversary) {

        if(anniversaries == null)
            anniversaries = new HashMap<>();

        if (!anniversaries.containsKey(id))
            anniversaries.put(id,anniversary);
    }

    public void addPersonalInfo(String id, PersonalInformation pi) {

        if(personalInfo == null)
            personalInfo = new HashMap<>();

        if (!personalInfo.containsKey(id))
            personalInfo.put(id,pi);
    }

    public void addNote(String note, String language) {

        if (notes == null) {
            notes = LocalizedString.builder().value(note).language(language).build();
            return;
        }

        if ((language == null && notes.getLanguage() == null) ||
                (language != null && notes.getLanguage() != null && language.equals(notes.getLanguage()))) {
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

    private void addCategory(String category) {

        if(categories == null)
            categories = new LinkedHashMap<>();

        if (!categories.containsKey(category))
            categories.put(category,Boolean.TRUE);
    }

    public void addCategories(String[] categories) {
        if (categories==null)
            return;

        for (String category: categories)
            addCategory(category);
    }

    @JsonAnyGetter
    public Map<String, String> getExtensions() {
        return extensions;
    }

    @JsonAnySetter
    public void setExtension(String name, String value) {

        if (extensions == null)
            extensions = new HashMap<>();

        extensions.put(name, value);
    }

    public void addExtension(String key, String value) {
        if(extensions == null)
            extensions = new HashMap<>();

        if (!extensions.containsKey(key))
            extensions.put(key,value);
    }

    public Card clone() {
        return SerializationUtils.clone(this);
    }

}

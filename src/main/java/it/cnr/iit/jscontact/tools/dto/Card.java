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
import com.fasterxml.jackson.core.JsonPointer;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.deser.std.DateDeserializers;
import com.fasterxml.jackson.databind.node.ObjectNode;
import it.cnr.iit.jscontact.tools.constraints.*;
import it.cnr.iit.jscontact.tools.constraints.groups.CardConstraintsGroup;
import it.cnr.iit.jscontact.tools.dto.deserializers.KindTypeDeserializer;
import it.cnr.iit.jscontact.tools.dto.serializers.KindTypeSerializer;
import it.cnr.iit.jscontact.tools.dto.serializers.UTCDateTimeSerializer;
import it.cnr.iit.jscontact.tools.dto.utils.JsonPointerUtils;
import it.cnr.iit.jscontact.tools.dto.utils.LabelUtils;
import it.cnr.iit.jscontact.tools.dto.utils.DelimiterUtils;
import lombok.*;
import lombok.experimental.SuperBuilder;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.SerializationUtils;
import org.apache.commons.lang3.StringUtils;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import java.io.Serializable;
import java.util.*;

/**
 * Class mapping the Card object as defined in section 2 of [draft-ietf-jmap-jscontact].
 *
 * @see <a href="https://datatracker.ietf.org/doc/draft-ietf-jmap-jscontact#section-2">draft-ietf-jmap-jscontact</a>
 * @author Mario Loffredo
 */
@JsonPropertyOrder({
        "@type","uid","prodId","created","updated","kind","relatedTo","language",
        "name","fullName","nickNames","organizations","titles",
        "emails","online","photos","preferredContactMethod","preferredContactLanguages",
        "addresses","localizations",
        "anniversaries","personalInfo","notes","categpories","timeZones",
        "extensions"})
@TitleOrganizationConstraint
@CardKindConstraint(groups = CardConstraintsGroup.class)
@LocalizationsConstraint
@NoArgsConstructor
@Getter
@Setter
@ToString(callSuper = true)
@SuperBuilder
public class Card extends JSContact implements Serializable {

    //Metadata properties
    @NotNull
    @Pattern(regexp = "Card", message="invalid @type value in Card")
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

    @JsonPropertyOrder(alphabetic = true)
    @RelatedToConstraint
    Map<String,Relation> relatedTo;

    String language;

    //Name and Organization properties
    @Valid
    NameComponent[] name;

    String fullName;

    String[] nickNames;

    @JsonPropertyOrder(alphabetic = true)
    @Valid
    @IdMapConstraint(message = "invalid Id in Map<Id,Organization>")
    Map<String,Organization> organizations;

    @JsonPropertyOrder(alphabetic = true)
    @Valid
    @IdMapConstraint(message = "invalid Id in Map<Id,Title>")
    Map<String,Title> titles;

    //Contact and Resource properties
    @JsonPropertyOrder(alphabetic = true)
    @Valid
    @IdMapConstraint(message = "invalid Id in Map<Id,Email>")
    Map<String,EmailAddress> emails;

    @JsonPropertyOrder(alphabetic = true)
    @Valid
    @IdMapConstraint(message = "invalid Id in Map<Id,Phone>")
    Map<String,Phone> phones;

    @JsonPropertyOrder(alphabetic = true)
    @Valid
    @IdMapConstraint(message = "invalid Id in Map<Id,Resource>")
    Map<String,Resource> online;

    @JsonPropertyOrder(alphabetic = true)
    @Valid
    @IdMapConstraint(message = "invalid Id in Map<Id,File>")
    Map<String,File> photos;

    PreferredContactMethodType preferredContactMethod;

    @JsonPropertyOrder(alphabetic = true)
    @PreferredContactLanguagesConstraint
    Map<String, ContactLanguage[]> preferredContactLanguages;

    //Address and Location properties
    @JsonPropertyOrder(alphabetic = true)
    @Valid
    @IdMapConstraint(message = "invalid Id in Map<Id,Address>")
    Map<String,Address> addresses;

    @JsonPropertyOrder(alphabetic = true)
    Map<String,Map<String,JsonNode>> localizations;

    //Additional properties
    @JsonPropertyOrder(alphabetic = true)
    @Valid
    @IdMapConstraint(message = "invalid Id in Map<Id,Anniversary>")
    Map<String,Anniversary> anniversaries;

    @JsonPropertyOrder(alphabetic = true)
    @Valid
    @IdMapConstraint(message = "invalid Id in Map<Id,PersonalInformation>")
    Map<String,PersonalInformation> personalInfo;

    String notes;

    @JsonPropertyOrder(alphabetic = true)
    @BooleanMapConstraint(message = "invalid Map<String,Boolean> categories in JSContact - Only Boolean.TRUE allowed")
    Map<String,Boolean> categories;

    @JsonPropertyOrder(alphabetic = true)
    @Valid
    Map<String,TimeZone> timeZones;

    @JsonPropertyOrder(alphabetic = true)
    Map<String,String> extensions;

    private boolean isContactByMethodPreferred(PreferredContactMethodType method) {return preferredContactMethod != null && preferredContactMethod == method; }

    /**
     * Tests if the preferred contact method is by emails.
     *
     * @return true if the preferred contact method is by emails, false otherwise
     */
    @JsonIgnore
    public boolean isContactByEmailsPreferred() {return isContactByMethodPreferred(PreferredContactMethodType.EMAILS); }

    /**
     * Tests if the preferred contact method is by phones.
     *
     * @return true if the preferred contact method is by phones, false otherwise
     */
    @JsonIgnore
    public boolean isContactByPhonesPreferred() {return isContactByMethodPreferred(PreferredContactMethodType.PHONES); }

    /**
     * Tests if the preferred contact method is by online service.
     *
     * @return true if the preferred contact method is by online service, false otherwise
     */
    @JsonIgnore
    public boolean isContactByOnlinePreferred() {return isContactByMethodPreferred(PreferredContactMethodType.ONLINE); }

//Methods for adding items to a mutable collection

    /**
     * Adds a relation between this object and another Card object.
     *
     * @param key the uid value of the related Card object
     * @param relType one of the RELATED property [RFC6350] type parameter values, or an IANA-registered value, or a vendor-specific value
     * @see <a href="https://tools.ietf.org/html/rfc6350">RFC6350</a>
     */
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

    /**
     * Adds a name component to this object.
     *
     * @param nc the name component
     */
    public void addName(NameComponent nc) {
        name = ArrayUtils.add(name, nc);
    }

    /**
     * Adds a nickname to this object.
     *
     * @param nickName the nickname
     */
    public void addNickName(String nickName) {
        nickNames = ArrayUtils.add(nickNames, nickName);
    }

    /**
     * Adds an organization to this object.
     *
     * @param id the organization identifier
     * @param organization the object representing the organization
     */
    public void addOrganization(String id, Organization organization) {

        if(organizations == null)
            organizations = new HashMap<>();

        organizations.putIfAbsent(id,organization);
    }

    /**
     * Adds a title to this object.
     *
     * @param id the title identifier
     * @param title the object representing the title
     */
    public void addTitle(String id, Title title) {

        if(titles == null)
            titles = new HashMap<>();

        titles.putIfAbsent(id,title);
    }

    /**
     * Adds an email address to this object.
     *
     * @param id the email identifier
     * @param email the object representing the email address
     */
    public void addEmail(String id, EmailAddress email) {

        if (emails == null)
            emails = new HashMap<>();

        emails.putIfAbsent(id, email);
    }

    /**
     * Adds a phone number to this object.
     *
     * @param id the phone number identifier
     * @param phone the object representing the phone number
     */
    public void addPhone(String id, Phone phone) {

        if (phones == null)
            phones = new HashMap<>();

        phones.putIfAbsent(id, phone);
    }

    /**
     * Adds an online resource to this object.
     *
     * @param id the resource identifier
     * @param resource the object representing the online resource
     */
    public void addOnline(String id, Resource resource) {

        if (online == null)
            online = new HashMap<>();

        online.putIfAbsent(id, resource);
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

    /**
     * Returns all the online resources associated to this object mapping the vCard 4.0 KEY property as defined in section 6.8.1 of [RFC6350].
     *
     * @return all the resources found, null otherwise
     * @see <a href="https://datatracker.ietf.org/doc/html/rfc6350#section-6.8.1">RFC6350</a>
     */
    @JsonIgnore
    public Map<String,Resource> getOnlineKey() {
        return getOnline(OnlineLabelKey.KEY);
    }

    /**
     * Returns all the online resources associated to this object corresponding to vCard 4.0 URL property as defined in section 6.7.8 of [RFC6350].
     *
     * @return all the resources found, null otherwise
     * @see <a href="https://datatracker.ietf.org/doc/html/rfc6350#section-6.7.8">RFC6350</a>
     */
    @JsonIgnore
    public Map<String,Resource> getOnlineUrl() {
        return getOnline(OnlineLabelKey.URL);
    }

    /**
     * Returns all the online resources associated to this object corresponding to vCard 4.0 SOURCE property as defined in section 6.1.3 of [RFC6350].
     *
     * @return all the resources found, null otherwise
     * @see <a href="https://datatracker.ietf.org/doc/html/rfc6350#section-6.1.3">RFC6350</a>
     */
    @JsonIgnore
    public Map<String,Resource> getOnlineSource() {
        return getOnline(OnlineLabelKey.SOURCE);
    }

    /**
     * Returns all the online resources associated to this object corresponding to vCard 4.0 LOGO property as defined in section 6.6.3 of [RFC6350].
     *
     * @return all the resources found, null otherwise
     * @see <a href="https://datatracker.ietf.org/doc/html/rfc6350#section-6.6.3">RFC6350</a>
     */
    @JsonIgnore
    public Map<String,Resource> getOnlineLogo() {
        return getOnline(OnlineLabelKey.LOGO);
    }

    /**
     * Returns all the online resources associated to this object corresponding to vCard 4.0 SOUND property as defined in section 6.7.5 of [RFC6350].
     *
     * @return all the resources found, null otherwise
     * @see <a href="https://datatracker.ietf.org/doc/html/rfc6350#section-6.7.5">RFC6350</a>
     */
    @JsonIgnore
    public Map<String,Resource> getOnlineSound() {
        return getOnline(OnlineLabelKey.SOUND);
    }

    /**
     * Returns all the online resources associated to this object corresponding to vCard 4.0 FBURL property as defined in section 6.9.1 of [RFC6350].
     *
     * @return all the resources found, null otherwise
     * @see <a href="https://datatracker.ietf.org/doc/html/rfc6350#section-6.9.1">RFC6350</a>
     */
    @JsonIgnore
    public Map<String,Resource> getOnlineFburl() {
        return getOnline(OnlineLabelKey.FBURL);
    }

    /**
     * Returns all the online resources associated to this object corresponding to vCard 4.0 CALURI property as defined in section 6.9.3 of [RFC6350].
     *
     * @return all the resources found, null otherwise
     * @see <a href="https://datatracker.ietf.org/doc/html/rfc6350#section-6.9.3">RFC6350</a>
     */
    @JsonIgnore
    public Map<String,Resource> getOnlineCaluri() {
        return getOnline(OnlineLabelKey.CALURI);
    }

    /**
     * Returns all the online resources associated to this object corresponding to vCard 4.0 CALADRURI property as defined in section 6.9.2 of [RFC6350].
     *
     * @return all the resources found, null otherwise
     * @see <a href="https://datatracker.ietf.org/doc/html/rfc6350#section-6.9.2">RFC6350</a>
     */
    @JsonIgnore
    public Map<String,Resource> getOnlineCaladruri() {
        return getOnline(OnlineLabelKey.CALADRURI);
    }

    /**
     * Returns all the online resources associated to this object corresponding to vCard 4.0 ORG-DIRECTORY property as defined in section 2.4 of [RFC6715].
     *
     * @return all the resources found, null otherwise
     * @see <a href="https://datatracker.ietf.org/doc/html/rfc6715.html#section-2.4">RFC6715</a>
     */
    @JsonIgnore
    public Map<String,Resource> getOnlineOrgDirectory() {
        return getOnline(OnlineLabelKey.ORG_DIRECTORY);
    }

    /**
     * Returns all the online resources associated to this object corresponding to vCard 4.0 IMPP property as defined in section 6.4.3 of [RFC6350].
     *
     * @return all the resources found, null otherwise
     * @see <a href="https://datatracker.ietf.org/doc/html/rfc6350#section-6.4.3">RFC6350</a>
     */
    @JsonIgnore
    public Map<String,Resource> getOnlineImpp() {
        return getOnline(OnlineLabelKey.IMPP);
    }

    /**
     * Returns all the online resources associated to this object corresponding to vCard 4.0 CONTACT-URI property as defined in section 21 of [RFC8605].
     *
     * @return all the resources found, null otherwise
     * @see <a href="https://datatracker.ietf.org/doc/html/rfc8605#section-2.1">RFC8605</a>
     */
    @JsonIgnore
    public Map<String,Resource> getOnlineContactUri() {
        return getOnline(OnlineLabelKey.CONTACT_URI);
    }

    /**
     * Adds a photo to this object.
     *
     * @param id the photo identifier
     * @param file the object representing the photo
     */
    public void addPhoto(String id, File file) {

        if (photos == null)
            photos = new HashMap<>();

        photos.putIfAbsent(id, file);
    }

    /**
     * Adds a contact language to this object.
     *
     * @param id the contact language identifier
     * @param contactLanguage the object representing the contact language
     */
    public void addContactLanguage(String id, ContactLanguage contactLanguage) {

        if (preferredContactLanguages == null)
            preferredContactLanguages = new HashMap<>();

        ContactLanguage[] languagesPerId = preferredContactLanguages.get(id);
        if (languagesPerId == null)
            preferredContactLanguages.put(id, new ContactLanguage[] {contactLanguage});
        else
            preferredContactLanguages.put(id, ArrayUtils.add(languagesPerId, contactLanguage));
    }

    /**
     * Adds a postal address to this object.
     *
     * @param id the postal address identifier
     * @param address the object representing the postal address
     */
    public void addAddress(String id, Address address) {

        if(addresses == null)
            addresses = new HashMap<>();

        addresses.putIfAbsent(id,address);
    }

    /**
     * Adds an anniversary to this object.
     *
     * @param id the anniversary identifier
     * @param anniversary the object representing the anniversary
     */
    public void addAnniversary(String id, Anniversary anniversary) {

        if(anniversaries == null)
            anniversaries = new HashMap<>();

        anniversaries.putIfAbsent(id,anniversary);
    }

    /**
     * Adds a personal information to this object.
     *
     * @param id the personal information identifier
     * @param personalInformation the object representing the personal information
     */
    public void addPersonalInfo(String id, PersonalInformation personalInformation) {

        if(personalInfo == null)
            personalInfo = new HashMap<>();

        personalInfo.putIfAbsent(id,personalInformation);
    }

    /**
     * Adds a note to this object.
     *
     * @param note the note
     */
    public void addNote(String note) {

        if (notes == null)
            notes = note;
        else
            notes = String.format("%s%s%s", notes, DelimiterUtils.NEWLINE_DELIMITER, note);
    }

    private void addCategory(String category) {

        if(categories == null)
            categories = new LinkedHashMap<>();

        categories.putIfAbsent(category,Boolean.TRUE);
    }

    /**
     * Adds a collection of categories to this object.
     *
     * @param categories the categories
     */
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

        extensions.putIfAbsent(name, value);
    }

    /**
     * Adds an extension to this object.
     *
     * @param key the extension identifier
     * @param value the extension as a text value
     */
    public void addExtension(String key, String value) {
        if(extensions == null)
            extensions = new HashMap<>();

        extensions.putIfAbsent(key,value);
    }

    /**
     * Adds a localization to a property of this object.
     *
     * @param language the localization language tag [RFC5646]
     * @param path the JSON pointer [RFC6901] to the property
     * @param object the Jackson library JsonNode object representing the localization for the property
     * @see <a href="https://tools.ietf.org/html/rfc5646">RFC5646</a>
     * @see <a href="https://tools.ietf.org/html/rfc6901">RFC6901</a>
     * @see <a href="https://github.com/FasterXML/jackson">Jackson Project Home</a>
     */
    public void addLocalization(String language, String path, JsonNode object) {

        if (language == null || path == null || object == null)
            return;

        if (localizations == null)
            localizations = new HashMap<>();

        Map<String,JsonNode> localizationsPerLanguage;
        if (localizations.containsKey(language))
            localizationsPerLanguage = localizations.get(language);
        else
            localizationsPerLanguage = new HashMap<>();

        if (localizationsPerLanguage.containsKey(path))
            return;

        localizationsPerLanguage.put(path, object);

        if (localizations.containsKey(language))
            localizations.replace(language, localizationsPerLanguage);
        else
            localizations.put(language, localizationsPerLanguage);
    }

    /**
     * Returns all the localizations of this object for a given property.
     *
     * @param path the JSON pointer [RFC6901] to the property
     * @return a map of language tags [RFC5646] to Jackson library JsonNode objects representing the localizations for the property
     * @see <a href="https://tools.ietf.org/html/rfc5646">RFC5646</a>
     * @see <a href="https://tools.ietf.org/html/rfc6901">RFC6901</a>
     * @see <a href="https://github.com/FasterXML/jackson">Jackson Project Home</a>
     */
    public Map<String,JsonNode> getLocalizationsPerPath(String path) {

        if (localizations == null)
            return null;

        Map<String, JsonNode> localizationsPerPath = new HashMap<>();

        for (Map.Entry<String,Map<String,JsonNode>> localizationsPerLang : localizations.entrySet()) {

            if (localizationsPerLang.getValue().containsKey(path))
                localizationsPerPath.put(localizationsPerLang.getKey(), localizationsPerLang.getValue().get(path));

        }

        return (localizationsPerPath.size() != 0) ? localizationsPerPath : null;
    }

    /**
     * Returns all the localizations of this object for a given property.
     *
     * @param language the localization language tag [RFC5646]
     * @return a map of JSON pointers [RFC6901] to Jackson library JsonNode objects representing the localizations for the property
     * @see <a href="https://tools.ietf.org/html/rfc5646">RFC5646</a>
     * @see <a href="https://tools.ietf.org/html/rfc6901">RFC6901</a>
     * @see <a href="https://github.com/FasterXML/jackson">Jackson Project Home</a>
     */
    public Map<String,JsonNode> getLocalizationsPerLanguage(String language) {

        if (localizations == null)
            return null;

        return localizations.get(language);
    }

    /**
     * Returns the localization to a property of this object for given language and path.
     *
     * @param language the localization language tag [RFC5646]
     * @param path the JSON pointer [RFC6901] to the property
     * @return the Jackson library JsonNode object representing the localization for the property
     * @see <a href="https://tools.ietf.org/html/rfc5646">RFC5646</a>
     * @see <a href="https://tools.ietf.org/html/rfc6901">RFC6901</a>
     * @see <a href="https://github.com/FasterXML/jackson">Jackson Project Home</a>
     */
    public JsonNode getLocalization(String language, String path) {

        if (localizations == null)
            return null;

        Map<String, JsonNode> localizationsPerPath = getLocalizationsPerPath(path);

        if (localizationsPerPath == null)
            return null;

        return localizationsPerPath.get(language);
    }


    /**
     * Returns the localized version of this object.
     *
     * @param language the localization language tag [RFC5646]
     * @return the localization of this object for the given language
     * @see <a href="https://tools.ietf.org/html/rfc5646">RFC5646</a>
     */
    public Card getLocalizedVersion(String language) {

        if (localizations == null)
            return null;

        Map<String,JsonNode> localizationsPerLanguage = getLocalizationsPerLanguage(language);

        if (localizationsPerLanguage == null)
            return null;

        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode root = objectMapper.valueToTree(this);

        for (Map.Entry<String,JsonNode> localization : localizationsPerLanguage.entrySet()) {
            JsonPointer jsonPointer = JsonPointer.compile(JsonPointerUtils.toAbsolute(localization.getKey()));
            JsonNode localizedNode = localization.getValue();
            JsonNode parentNode = root.at(jsonPointer.head());

            if (!parentNode.isMissingNode() && parentNode.isObject()) {
                ObjectNode parentObjectNode = (ObjectNode) parentNode;
                String fieldName = jsonPointer.last().toString();
                fieldName = fieldName.replace(Character.toString(JsonPointer.SEPARATOR), StringUtils.EMPTY);
                if (parentObjectNode.get(fieldName) != null)
                    parentObjectNode.set(fieldName, localizedNode);
            }
        }

        Card localizedCard = objectMapper.convertValue(root, Card.class);
        localizedCard.setLanguage(language);
        localizedCard.setLocalizations(null);

        return localizedCard;
    }

    /**
     * Clones this object.
     *
     * @return the clone of this Card object
     */
    @Override
    public Card clone() {
        return SerializationUtils.clone(this);
    }

}

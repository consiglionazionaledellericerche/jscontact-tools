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
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.deser.std.DateDeserializers;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.fasterxml.jackson.databind.node.ObjectNode;
import it.cnr.iit.jscontact.tools.constraints.*;
import it.cnr.iit.jscontact.tools.dto.annotations.JSContactCollection;
import it.cnr.iit.jscontact.tools.dto.deserializers.ContactChannelsKeyDeserializer;
import it.cnr.iit.jscontact.tools.dto.deserializers.JCardPropsDeserializer;
import it.cnr.iit.jscontact.tools.dto.deserializers.KindTypeDeserializer;
import it.cnr.iit.jscontact.tools.dto.serializers.ContactChannelsKeySerializer;
import it.cnr.iit.jscontact.tools.dto.serializers.JCardPropsSerializer;
import it.cnr.iit.jscontact.tools.dto.serializers.UTCDateTimeSerializer;
import it.cnr.iit.jscontact.tools.dto.utils.JsonPointerUtils;
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
 * Class mapping the Card object as defined in section 2 of [draft-ietf-calext-jscontact].
 *
 * @see <a href="https://datatracker.ietf.org/doc/draft-ietf-calext-jscontact#section-2">draft-ietf-calext-jscontact</a>
 * @author Mario Loffredo
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
        "@type","uid","prodId","created","updated","kind","members","relatedTo","locale",
        "name","fullName","nickNames","organizations","titles","speakToAs",
        "emails","onlineServices","phones","preferredContactChannels","preferredLanguages",
        "calendars","schedulingAddresses",
        "addresses",
        "cryptoKeys","directories","links","media",
        "localizations",
        "anniversaries","personalInfo","notes","keywords","ietf.org:rfc0000:props"})
@TitleOrganizationConstraint
@LocalizationsConstraint
@NoArgsConstructor
@Getter
@Setter
@ToString(callSuper = true)
@EqualsAndHashCode(of={"uid"}, callSuper = false)
@SuperBuilder
public class Card extends ValidableObject implements Serializable {

    private static ObjectMapper mapper = new ObjectMapper();

    /*
    Metadata properties
     */
    @NotNull
    @Pattern(regexp = "Card", message="invalid @type value in Card")
    @JsonProperty("@type")
    @Builder.Default
    String _type = "Card";

    @NotNull(message = "uid is missing in Card")
    @NonNull
    String uid;

    String prodId;

    @JsonSerialize(using = UTCDateTimeSerializer.class)
    @JsonDeserialize(using = DateDeserializers.CalendarDeserializer.class)
    Calendar created;

    @JsonSerialize(using = UTCDateTimeSerializer.class)
    @JsonDeserialize(using = DateDeserializers.CalendarDeserializer.class)
    Calendar updated;

    @JsonDeserialize(using = KindTypeDeserializer.class)
    KindType kind;

    @BooleanMapConstraint(message = "invalid Map<String,Boolean> members in JSContact - Only Boolean.TRUE allowed")
    Map<String,Boolean> members;

    @JSContactCollection(addMethod = "addRelation")
    @JsonPropertyOrder(alphabetic = true)
    @RelatedToConstraint
    Map<String,Relation> relatedTo;

    @LanguageTagConstraint
    String locale;


    /*
    Name and Organization properties
     */
    @Valid
    Name name;

    String fullName;

    @JSContactCollection(addMethod = "addNickName")
    @JsonPropertyOrder(alphabetic = true)
    @Valid
    @IdMapConstraint(message = "invalid Id in Map<Id,NickName>")
    Map<String,NickName> nickNames;

    @JSContactCollection(addMethod = "addOrganization")
    @JsonPropertyOrder(alphabetic = true)
    @Valid
    @IdMapConstraint(message = "invalid Id in Map<Id,Organization>")
    Map<String,Organization> organizations;

    @JSContactCollection(addMethod = "addTitle")
    @JsonPropertyOrder(alphabetic = true)
    @Valid
    @IdMapConstraint(message = "invalid Id in Map<Id,Title>")
    Map<String,Title> titles;

    @Valid
    SpeakToAs speakToAs;


    /*
    Contact and Resource properties
     */
    @JSContactCollection(addMethod = "addEmail")
    @JsonPropertyOrder(alphabetic = true)
    @Valid
    @IdMapConstraint(message = "invalid Id in Map<Id,Email>")
    Map<String,EmailAddress> emails;

    @JSContactCollection(addMethod = "addOnlineService")
    @JsonPropertyOrder(alphabetic = true)
    @Valid
    @IdMapConstraint(message = "invalid Id in Map<Id,OnlineService>")
    Map<String,OnlineService> onlineServices;

    @JSContactCollection(addMethod = "addPhone")
    @JsonPropertyOrder(alphabetic = true)
    @Valid
    @IdMapConstraint(message = "invalid Id in Map<Id,Phone>")
    Map<String,Phone> phones;

    @JSContactCollection(addMethod = "addContactChannelPreference")
    @JsonPropertyOrder(alphabetic = true)
    @JsonSerialize(keyUsing = ContactChannelsKeySerializer.class)
    @JsonDeserialize(keyUsing = ContactChannelsKeyDeserializer.class)
    @PreferredContactChannelsConstraint
    Map<ChannelType,ContactChannelPreference[]> preferredContactChannels;

    @JSContactCollection(addMethod = "addLanguagePreference")
    @JsonPropertyOrder(alphabetic = true)
    @PreferredLanguagesConstraint
    Map<String, LanguagePreference[]> preferredLanguages;


    /*
     Calendaring and Scheduling properties
     */
    @JSContactCollection(addMethod = "addCalendar")
    @JsonPropertyOrder(alphabetic = true)
    @Valid
    @IdMapConstraint(message = "invalid Id in Map<Id,CalendarResource>")
    Map<String,CalendarResource> calendars;

    @JSContactCollection(addMethod = "addSchedulingAddress")
    @JsonPropertyOrder(alphabetic = true)
    @Valid
    @IdMapConstraint(message = "invalid Id in Map<Id,SchedulingAddress>")
    Map<String, SchedulingAddress> schedulingAddresses;


    /*
    Address and Location properties
     */
    @JSContactCollection(addMethod = "addAddress")
    @JsonPropertyOrder(alphabetic = true)
    @Valid
    @IdMapConstraint(message = "invalid Id in Map<Id,Address>")
    Map<String,Address> addresses;

    /*
    Resource properties
     */
    @JSContactCollection(addMethod = "addCryptoResource")
    @JsonPropertyOrder(alphabetic = true)
    @Valid
    @IdMapConstraint(message = "invalid Id in Map<Id,CryptoResource>")
    Map<String,CryptoResource> cryptoKeys;

    @JSContactCollection(addMethod = "addDirectoryResource")
    @JsonPropertyOrder(alphabetic = true)
    @Valid
    @IdMapConstraint(message = "invalid Id in Map<Id,DirectoryResource>")
    Map<String,DirectoryResource> directories;

    @JSContactCollection(addMethod = "addLinkResource")
    @JsonPropertyOrder(alphabetic = true)
    @Valid
    @IdMapConstraint(message = "invalid Id in Map<Id,LinkResource>")
    Map<String,LinkResource> links;

    @JSContactCollection(addMethod = "addMediaResource")
    @JsonPropertyOrder(alphabetic = true)
    @Valid
    @IdMapConstraint(message = "invalid Id in Map<Id,MediaResource>")
    Map<String,MediaResource> media;


    /*
    Multilingual properties
     */
    @JsonPropertyOrder(alphabetic = true)
    Map<String,Map<String,JsonNode>> localizations;


    /*
    Additional properties
     */
    @JSContactCollection(addMethod = "addAnniversary")
    @JsonPropertyOrder(alphabetic = true)
    @Valid
    @IdMapConstraint(message = "invalid Id in Map<Id,Anniversary>")
    Map<String,Anniversary> anniversaries;

    @JSContactCollection(addMethod = "addPersonalInfo")
    @JsonPropertyOrder(alphabetic = true)
    @Valid
    @IdMapConstraint(message = "invalid Id in Map<Id,PersonalInformation>")
    Map<String,PersonalInformation> personalInfo;

    Note[] notes;

    @BooleanMapConstraint(message = "invalid Map<String,Boolean> keywords in JSContact - Only Boolean.TRUE allowed")
    Map<String,Boolean> keywords;

    @JsonProperty("ietf.org:rfc0000:props")
    @JsonSerialize(using = JCardPropsSerializer.class)
    @JsonDeserialize(using = JCardPropsDeserializer.class)
    @Valid
    JCardProp[] jCardExtensions;

    @JsonIgnore
    Map<String,TimeZone> customTimeZones;

//Methods for adding items to a mutable collection


    /**
     * Adds a member to this object.
     *
     * @param member the uid value of the object representing a group member
     */
    public void addMember(String member) {

        if(members == null)
            members = new LinkedHashMap<>();

        members.putIfAbsent(member,Boolean.TRUE);
    }

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
            if (relType == null)
                relatedTo.put(key, Relation.builder().build());
            else {
                Map<RelationType, Boolean> map = new HashMap<>();
                map.putAll(relationPerKey.getRelation());
                map.put(relType, Boolean.TRUE);
                relatedTo.replace(key, Relation.builder()
                        .relation(map)
                        .build());
            }
        }
    }

    /**
     * Adds a nickname to this object.
     *
     * @param id the nickname identifier
     * @param nickName the object representing the nickname
     */
    public void addNickName(String id, NickName nickName) {

        if(nickNames == null)
            nickNames = new HashMap<>();

        nickNames.putIfAbsent(id,nickName);
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
     * Adds a scheduling address to this object.
     *
     * @param id the scheduling address identifier
     * @param scheduling the object representing the scheduling address
     */
    public void addSchedulingAddress(String id, SchedulingAddress scheduling) {

        if (this.schedulingAddresses == null)
            this.schedulingAddresses = new HashMap<>();

        this.schedulingAddresses.putIfAbsent(id, scheduling);
    }

    /**
     * Adds an online service to this object.
     *
     * @param id the online service identifier
     * @param onlineService the object representing the online service
     */
    public void addOnlineService(String id, OnlineService onlineService) {

        if (this.onlineServices == null)
            this.onlineServices = new HashMap<>();

        this.onlineServices.putIfAbsent(id, onlineService);
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
     * Adds a directory resource to this object.
     *
     * @param id the directory resource identifier
     * @param resource the object representing the directory resource
     */
    public void addDirectoryResource(String id, DirectoryResource resource) {

        if (directories == null)
            directories = new HashMap<>();

        directories.putIfAbsent(id, resource);
    }

    /**
     * Adds a crypto resource to this object.
     *
     * @param id the crypto resource identifier
     * @param resource the object representing the crypto resource
     */
    public void addCryptoResource(String id, CryptoResource resource) {

        if (cryptoKeys == null)
            cryptoKeys = new HashMap<>();

        cryptoKeys.putIfAbsent(id, resource);
    }


    /**
     * Adds a calendar resource to this object.
     *
     * @param id the calendar resource identifier
     * @param resource the object representing the calendar resource
     */
    public void addCalendarResource(String id, CalendarResource resource) {

        if (calendars == null)
            calendars = new HashMap<>();

        calendars.putIfAbsent(id, resource);
    }

    /**
     * Adds a link resource to this object.
     *
     * @param id the link resource identifier
     * @param resource the object representing the link resource
     */
    public void addLinkResource(String id, LinkResource resource) {

        if (links == null)
            links = new HashMap<>();

        links.putIfAbsent(id, resource);
    }


    /**
     * Adds a media resource to this object.
     *
     * @param id the media resource identifier
     * @param resource the object representing the media resource
     */
    public void addMediaResource(String id, MediaResource resource) {

        if (media == null)
            media = new HashMap<>();

        media.putIfAbsent(id, resource);
    }

    /**
     * Adds a language preference to this object.
     *
     * @param id the contact language identifier
     * @param languagePreference the object representing the contact language
     */
    public void addLanguagePreference(String id, LanguagePreference languagePreference) {

        if (preferredLanguages == null)
            preferredLanguages = new HashMap<>();

        LanguagePreference[] languagesPerId = preferredLanguages.get(id);
        LanguagePreference[] languages;
        if (languagePreference == null)
            languages = new LanguagePreference[]{};
        else
            languages = new LanguagePreference[] {languagePreference};
        if (languagesPerId == null)
            preferredLanguages.put(id, languages);
        else
            preferredLanguages.put(id, ArrayUtils.addAll(languagesPerId, languages));
    }

    /**
     * Adds a contact channel preference to this object.
     *
     * @param id the contact channel preference identifier
     * @param contactChannelPreference the object representing the contact channel preference
     */
    public void addContactChannelPreference(ChannelType id, ContactChannelPreference contactChannelPreference) {

        if (preferredContactChannels == null)
            preferredContactChannels = new HashMap<>();

        ContactChannelPreference[] contactChannelsPerId = preferredContactChannels.get(id);

        ContactChannelPreference[] channels;
        if (contactChannelPreference == null)
            channels = new ContactChannelPreference[]{};
        else
            channels = new ContactChannelPreference[] {contactChannelPreference};
        if (contactChannelsPerId == null)
            preferredContactChannels.put(id, channels);
        else
            preferredContactChannels.put(id, ArrayUtils.addAll(contactChannelsPerId, channels));
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
     * Adds personal information to this object.
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
     * @param note the note object
     */
    public void addNote(Note note) {

        notes = ArrayUtils.add(notes, note);
    }

    private void addKeyword(String keyword) {

        if(keywords == null)
            keywords = new LinkedHashMap<>();

        keywords.putIfAbsent(keyword,Boolean.TRUE);
    }

    /**
     * Adds a collection of keywords to this object.
     *
     * @param keywords the keywords
     */
    public void addKeywords(String[] keywords) {
        if (keywords==null)
            return;

        for (String keyword: keywords)
            addKeyword(keyword);
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
    @JsonIgnore
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
    @JsonIgnore
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
    @JsonIgnore
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
    @JsonIgnore
    public Card getLocalizedVersion(String language) {

        if (localizations == null)
            return null;

        Map<String,JsonNode> localizationsPerLanguage = getLocalizationsPerLanguage(language);

        if (localizationsPerLanguage == null)
            return null;

        JsonNode root = mapper.valueToTree(this);

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

        Card localizedCard = mapper.convertValue(root, Card.class);
        localizedCard.setLocale(language);
        localizedCard.setLocalizations(null);

        return localizedCard;
    }

    /**
     * Returns the localization languages of this object.
     *
     * @return the array of localization languages [RFC5646]
     * @see <a href="https://tools.ietf.org/html/rfc5646">RFC5646</a>
     */
    @JsonIgnore
    public String[] getLocalizationsLanguages() {

        if (localizations == null)
            return null;

        return localizations.keySet().toArray(new String[localizations.size()]);
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


    public static Card toJSCard(String json) throws JsonProcessingException {

        return mapper.readValue(json, Card.class);

    }

    public static String toJson(Card jsCard) throws JsonProcessingException {

        return mapper.writeValueAsString(jsCard);

    }

    /**
     * Deserialize a single Card object or an array of Card objects
     *
     * @param json the single Card object or the array of Card objects in JSON
     * @return an array of Card objects
     * @throws JsonProcessingException never thrown
     */
    public static Card[] toJSCards(String json) throws JsonProcessingException {

        SimpleModule module = new SimpleModule();
        mapper.registerModule(module);
        try {
            return mapper.readValue(json, Card[].class);
        } catch(Exception e) {
            return new Card[]{mapper.readValue(json, Card.class)};
        }
    }

    /**
     * Serialize an array of Card objects
     *
     * @param jsCards the array of Card objects
     * @return the array of Card objects in JSON
     * @throws JsonProcessingException never thrown
     */
    public static String toJson(Card[] jsCards) throws JsonProcessingException {

        return mapper.writeValueAsString(jsCards);
    }

    /**
     * Adds a JCardProp object to this object.
     *
     * @param o the JCardProp object
     */
    public void addJCardProp(JCardProp o) {

        jCardExtensions = ArrayUtils.add(jCardExtensions, o);
    }


    /**
     * Convert the jCardExtensions array into a map
     * where the keys are the extnsion names and
     * the values are the extension values in text format
     *
     * @return jCardExtensions array converted into a map
     */
    @JsonIgnore
    public Map<String,String> getJCardExtensionsAsMap() {

        Map<String,String> map = new HashMap<>();
        if (this.getJCardExtensions() == null)
            return map;

        for (JCardProp jCardExtension : this.getJCardExtensions())
            map.put(jCardExtension.getName().toString(),jCardExtension.getValue().toString());

        return map;
    }
}

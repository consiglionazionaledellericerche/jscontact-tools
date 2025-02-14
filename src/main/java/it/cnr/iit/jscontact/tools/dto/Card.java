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
import it.cnr.iit.jscontact.tools.constraints.validators.builder.ValidatorBuilder;
import it.cnr.iit.jscontact.tools.dto.annotations.ContainsExtensibleEnum;
import it.cnr.iit.jscontact.tools.dto.annotations.JSContactCollection;
import it.cnr.iit.jscontact.tools.dto.deserializers.VCardPropsDeserializer;
import it.cnr.iit.jscontact.tools.dto.deserializers.CardKindDeserializer;
import it.cnr.iit.jscontact.tools.dto.interfaces.IsIANAType;
import it.cnr.iit.jscontact.tools.dto.serializers.VCardPropsSerializer;
import it.cnr.iit.jscontact.tools.dto.serializers.UTCDateTimeSerializer;
import it.cnr.iit.jscontact.tools.dto.utils.JsonPointerUtils;
import it.cnr.iit.jscontact.tools.dto.utils.VersionUtils;
import lombok.*;
import lombok.experimental.SuperBuilder;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.SerializationUtils;
import org.apache.commons.lang3.StringUtils;

import javax.validation.ConstraintViolation;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import java.io.Serializable;
import java.util.*;

/**
 * Class mapping the Card object as defined in section 2 of [RFC9553] and section 2.15.1 of [RFC9555].
 *
 * @see <a href="https://datatracker.ietf.org/doc/RFC9553#section-2">Section 2 of RFC9553</a>
 * @see <a href="https://datatracker.ietf.org/doc/RFC9555#section-2.15.1">RFC9555</a>
 * @author Mario Loffredo
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
        "@type", "version", "created", "kind", "language", "members", "prodId", "relatedTo", "uid", "updated",
        "name", "nicknames", "organizations", "speakToAs", "titles",
        "emails", "onlineServices", "phones", "preferredLanguages",
        "calendars", "schedulingAddresses",
        "addresses",
        "cryptoKeys", "directories", "links", "media",
        "localizations",
        "anniversaries", "keywords", "notes", "personalInfo",
        "vCardProps"})
@TitleOrganizationConstraint
@MembersVsCardKindValueConstraint
@LocalizationsConstraint
@NoArgsConstructor
@Getter
@Setter
@ToString(callSuper = true)
@EqualsAndHashCode(of = {"uid"}, callSuper = false)
@SuperBuilder
public class Card extends AbstractExtensibleJSContactType implements IsIANAType, Serializable {

    private static ObjectMapper mapper = new ObjectMapper();

    /*
    Metadata Properties
     */

    /**
     * @see <a href="https://datatracker.ietf.org/doc/RFC9553#section-2.1.1">Section 2.1.1 of RFC9553</a>
     */
    @NotNull
    @Pattern(regexp = "Card", message = "invalid @type value in Card")
    @JsonProperty("@type")
    @Builder.Default
    String _type = "Card";

    /**
     * @see <a href="https://datatracker.ietf.org/doc/RFC9553#section-2.1.2">Section 2.1.2 of RFC9553</a>
     */
    @NotNull
    @VersionValueConstraint
    String version = VersionUtils.getDefaultVersion();

    /**
     * @see <a href="https://datatracker.ietf.org/doc/RFC9553#section-2.1.3">Section 2.1.3 of RFC9553</a>
     */
    @JsonSerialize(using = UTCDateTimeSerializer.class)
    @JsonDeserialize(using = DateDeserializers.CalendarDeserializer.class)
    java.util.Calendar created;

    /**
     * @see <a href="https://datatracker.ietf.org/doc/RFC9553#section-2.1.4">Section 2.1.4 of RFC9553</a>
     */
    @JsonDeserialize(using = CardKindDeserializer.class)
    @ContainsExtensibleEnum(enumClass = KindEnum.class, getMethod = "getKind")
    KindType kind;

    /**
     * @see <a href="https://datatracker.ietf.org/doc/RFC9553#section-2.1.5">Section 2.1.5 of RFC9553</a>
     */
    @LanguageTagConstraint
    String language;

    /**
     * @see <a href="https://datatracker.ietf.org/doc/RFC9553#section-2.1.6">Section 2.1.6 of RFC9553</a>
     */
    @BooleanMapConstraint(message = "invalid Map<String,Boolean> members in JSContact - Only Boolean.TRUE allowed")
    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    Map<String, Boolean> members;

    /**
     * @see <a href="https://datatracker.ietf.org/doc/RFC9553#section-2.1.7">Section 2.1.7 of RFC9553</a>
     */
    String prodId;

    /**
     * @see <a href="https://datatracker.ietf.org/doc/RFC9553#section-2.1.8">Section 2.1.8 of RFC9553</a>
     */
    @JSContactCollection(addMethod = "addRelation", itemClass = Relation.class)
    @JsonPropertyOrder(alphabetic = true)
    @RelatedToConstraint
    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    Map<String, Relation> relatedTo;

    /**
     * @see <a href="https://datatracker.ietf.org/doc/RFC9553#section-2.1.9">Section 2.1.9 of RFC9553</a>
     */
    @NotNull(message = "uid is missing in Card")
    @NonNull
    String uid;

    /**
     * @see <a href="https://datatracker.ietf.org/doc/RFC9553#section-2.1.10">Section 2.1.10 of RFC9553</a>
     */
    @JsonSerialize(using = UTCDateTimeSerializer.class)
    @JsonDeserialize(using = DateDeserializers.CalendarDeserializer.class)
    java.util.Calendar updated;

    /*
    Name and Organization properties
     */

    /**
     * @see <a href="https://datatracker.ietf.org/doc/RFC9553#section-2.2.1">Section 2.2.1 of RFC9553</a>
     */
    @Valid
    Name name;

    /**
     * @see <a href="https://datatracker.ietf.org/doc/RFC9553#section-2.2.2">Section 2.2.2 of RFC9553</a>
     */
    @JSContactCollection(addMethod = "addNickName", itemClass = Nickname.class)
    @JsonPropertyOrder(alphabetic = true)
    @Valid
    @IdMapConstraint(message = "invalid Id in Map<Id,Nickname>")
    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    Map<String, Nickname> nicknames;

    /**
     * @see <a href="https://datatracker.ietf.org/doc/RFC9553#section-2.2.3">Section 2.2.3 of RFC9553</a>
     */
    @JSContactCollection(addMethod = "addOrganization", itemClass = Organization.class)
    @JsonPropertyOrder(alphabetic = true)
    @Valid
    @IdMapConstraint(message = "invalid Id in Map<Id,Organization>")
    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    Map<String, Organization> organizations;

    /**
     * @see <a href="https://datatracker.ietf.org/doc/RFC9553#section-2.2.4">Section 2.2.4 of RFC9553</a>
     */
    @Valid
    SpeakToAs speakToAs;

    /**
     * @see <a href="https://datatracker.ietf.org/doc/RFC9553#section-2.2.5">Section 2.2.5 of RFC9553</a>
     */
    @JSContactCollection(addMethod = "addTitle", itemClass = Title.class)
    @JsonPropertyOrder(alphabetic = true)
    @Valid
    @IdMapConstraint(message = "invalid Id in Map<Id,Title>")
    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    Map<String, Title> titles;

    /*
    Contact Properties
     */

    /**
     * @see <a href="https://datatracker.ietf.org/doc/RFC9553#section-2.3.1">Section 2.3.1 of RFC9553</a>
     */
    @JSContactCollection(addMethod = "addEmailAddress", itemClass = EmailAddress.class)
    @JsonPropertyOrder(alphabetic = true)
    @Valid
    @IdMapConstraint(message = "invalid Id in Map<Id,Email>")
    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    Map<String, EmailAddress> emails;

    /**
     * @see <a href="https://datatracker.ietf.org/doc/RFC9553#section-2.3.2">Section 2.3.2 of RFC9553</a>
     */
    @JSContactCollection(addMethod = "addOnlineService", itemClass = OnlineService.class)
    @JsonPropertyOrder(alphabetic = true)
    @Valid
    @IdMapConstraint(message = "invalid Id in Map<Id,OnlineService>")
    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    Map<String,OnlineService> onlineServices;

    /**
     * @see <a href="https://datatracker.ietf.org/doc/RFC9553#section-2.3.3">Section 2.3.3 of RFC9553</a>
     */
    @JSContactCollection(addMethod = "addPhone", itemClass = Phone.class)
    @JsonPropertyOrder(alphabetic = true)
    @Valid
    @IdMapConstraint(message = "invalid Id in Map<Id,Phone>")
    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    Map<String,Phone> phones;

    /**
     * @see <a href="https://datatracker.ietf.org/doc/RFC9553#section-2.3.4">Section 2.3.4 of RFC9553</a>
     */
    @JSContactCollection(addMethod = "addLanguagePref", itemClass = LanguagePref.class)
    @JsonPropertyOrder(alphabetic = true)
    @Valid
    @IdMapConstraint(message = "invalid Id in Map<Id,LanguagePref>")
    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    Map<String, LanguagePref> preferredLanguages;

    /*
     Calendaring and Scheduling properties
     */

    /**
     * @see <a href="https://datatracker.ietf.org/doc/RFC9553#section-2.4.1">Section 2.4.1 of RFC9553</a>
     */
    @JSContactCollection(addMethod = "addCalendar", itemClass = Calendar.class)
    @JsonPropertyOrder(alphabetic = true)
    @Valid
    @IdMapConstraint(message = "invalid Id in Map<Id,Calendar>")
    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    Map<String, Calendar> calendars;

    /**
     * @see <a href="https://datatracker.ietf.org/doc/RFC9553#section-2.4.2">Section 2.4.2 of RFC9553</a>
     */
    @JSContactCollection(addMethod = "addSchedulingAddress", itemClass = SchedulingAddress.class)
    @JsonPropertyOrder(alphabetic = true)
    @Valid
    @IdMapConstraint(message = "invalid Id in Map<Id,SchedulingAddress>")
    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    Map<String, SchedulingAddress> schedulingAddresses;

    /*
    Address and Location properties
     */

    /**
     * @see <a href="https://datatracker.ietf.org/doc/RFC9553#section-2.5.1">Section 2.5.1 of RFC9553</a>
     */
    @JSContactCollection(addMethod = "addAddress", itemClass = Address.class)
    @JsonPropertyOrder(alphabetic = true)
    @Valid
    @IdMapConstraint(message = "invalid Id in Map<Id,Address>")
    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    Map<String, Address> addresses;

    /*
    Resource properties
     */

    /**
     * @see <a href="https://datatracker.ietf.org/doc/RFC9553#section-2.6.1">Section 2.6.1 of RFC9553</a>
     */
    @JSContactCollection(addMethod = "addCryptoResource", itemClass = CryptoKey.class)
    @JsonPropertyOrder(alphabetic = true)
    @Valid
    @IdMapConstraint(message = "invalid Id in Map<Id,CryptoKey>")
    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    Map<String, CryptoKey> cryptoKeys;

    /**
     * @see <a href="https://datatracker.ietf.org/doc/RFC9553#section-2.6.2">Section 2.6.2 of RFC9553</a>
     */
    @JSContactCollection(addMethod = "addDirectoryResource", itemClass = Directory.class)
    @JsonPropertyOrder(alphabetic = true)
    @Valid
    @IdMapConstraint(message = "invalid Id in Map<Id,Directory>")
    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    Map<String, Directory> directories;

    /**
     * @see <a href="https://datatracker.ietf.org/doc/RFC9553#section-2.6.3">Section 2.6.3 of RFC9553</a>
     */
    @JSContactCollection(addMethod = "addLinkResource", itemClass = Link.class)
    @JsonPropertyOrder(alphabetic = true)
    @Valid
    @IdMapConstraint(message = "invalid Id in Map<Id,Link>")
    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    Map<String, Link> links;

    /**
     * @see <a href="https://datatracker.ietf.org/doc/RFC9553#section-2.6.4">Section 2.6.4 of RFC9553</a>
     */
    @JSContactCollection(addMethod = "addMediaResource", itemClass = Media.class)
    @JsonPropertyOrder(alphabetic = true)
    @Valid
    @IdMapConstraint(message = "invalid Id in Map<Id,Media>")
    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    Map<String, Media> media;

    /*
    Multilingual properties
     */

    /**
     * @see <a href="https://datatracker.ietf.org/doc/RFC9553#section-2.7.1">Section 2.7.1 of RFC9553</a>
     */
    @JsonPropertyOrder(alphabetic = true)
    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    Map<String, Map<String, JsonNode>> localizations;

    /*
    Additional properties
     */

    /**
     * @see <a href="https://datatracker.ietf.org/doc/RFC9553#section-2.8.1">Section 2.8.1 of RFC9553</a>
     */
    @JSContactCollection(addMethod = "addAnniversary", itemClass = Anniversary.class)
    @JsonPropertyOrder(alphabetic = true)
    @Valid
    @IdMapConstraint(message = "invalid Id in Map<Id,Anniversary>")
    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    Map<String, Anniversary> anniversaries;

    /**
     * @see <a href="https://datatracker.ietf.org/doc/RFC9553#section-2.8.2">Section 2.8.2 of RFC9553</a>
     */
    @BooleanMapConstraint(message = "invalid Map<String,Boolean> keywords in JSContact - Only Boolean.TRUE allowed")
    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    Map<String, Boolean> keywords;

    /**
     * @see <a href="https://datatracker.ietf.org/doc/RFC9553#section-2.8.3">Section 2.8.3 of RFC9553</a>
     */
    @IdMapConstraint(message = "invalid Id in Map<Id,Note>")
    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    Map<String, Note> notes;

    /**
     * @see <a href="https://datatracker.ietf.org/doc/RFC9553#section-2.8.4">Section 2.8.4 of RFC9553</a>
     */
    @JSContactCollection(addMethod = "addPersonalInfo", itemClass = PersonalInfo.class)
    @JsonPropertyOrder(alphabetic = true)
    @Valid
    @IdMapConstraint(message = "invalid Id in Map<Id,PersonalInfo>")
    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    Map<String, PersonalInfo> personalInfo;

    /*
     New JSContact Properties
    */

    /**
     * @see <a href="https://datatracker.ietf.org/doc/RFC9555#section-2.15.1">Section 2.15.1 of RFC9555</a>
     */
    @JsonProperty("vCardProps")
    @JsonSerialize(using = VCardPropsSerializer.class)
    @JsonDeserialize(using = VCardPropsDeserializer.class)
    @Valid
    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    VCardProp[] vCardProps;

    @JsonIgnore
    Map<String,TimeZone> customTimeZones;

    @JsonIgnore
    @Getter
    private List<String> validationMessages;


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
                Map<RelationType, Boolean> map = new HashMap<>(relationPerKey.getRelation());
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
     * @param nickname the object representing the nickname
     */
    public void addNickName(String id, Nickname nickname) {

        if(nicknames == null)
            nicknames = new HashMap<>();

        nicknames.putIfAbsent(id,nickname);
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
    public void addEmailAddress(String id, EmailAddress email) {

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
    public void addDirectoryResource(String id, Directory resource) {

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
    public void addCryptoResource(String id, CryptoKey resource) {

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
    public void addCalendarResource(String id, Calendar resource) {

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
    public void addLinkResource(String id, Link resource) {

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
    public void addMediaResource(String id, Media resource) {

        if (media == null)
            media = new HashMap<>();

        media.putIfAbsent(id, resource);
    }

    /**
     * Adds a language preference to this object.
     *
     * @param id the contact language identifier
     * @param languagePref the object representing the contact language
     */
    public void addLanguagePref(String id, LanguagePref languagePref) {

        if (preferredLanguages == null)
            preferredLanguages = new HashMap<>();

        preferredLanguages.putIfAbsent(id, languagePref);
    }

    /**
     * Adds a delivery address to this object.
     *
     * @param id      the delivery address identifier
     * @param address the object representing the delivery address
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
     * @param id           the personal information identifier
     * @param personalInfo the object representing the personal information
     */
    public void addPersonalInfo(String id, PersonalInfo personalInfo) {

        if (this.personalInfo == null)
            this.personalInfo = new HashMap<>();

        this.personalInfo.putIfAbsent(id, personalInfo);
    }

    /**
     * Adds a note to this object.
     *
     * @param id the note identifier
     * @param note the note object
     */
    public void addNote(String id, Note note) {

        if(notes == null)
            notes = new HashMap<>();

        notes.putIfAbsent(id,note);
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
     * @param object the Jackson library JsonNode object [Jackson Project] representing the localization for the property
     * @see <a href="https://tools.ietf.org/html/rfc5646">RFC5646</a>
     * @see <a href="https://tools.ietf.org/html/rfc6901">RFC6901</a>
     * @see <a href="https://github.com/FasterXML/jackson">Jackson Project</a>
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
            localizationsPerLanguage.replace(path, object);
        else
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
     * @return a map of language tags [RFC5646] to Jackson library JsonNode objects [Jackson Project] representing the localizations for the property
     * @see <a href="https://tools.ietf.org/html/rfc5646">RFC5646</a>
     * @see <a href="https://tools.ietf.org/html/rfc6901">RFC6901</a>
     * @see <a href="https://github.com/FasterXML/jackson">Jackson Project</a>
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
     * @return a map of JSON pointers [RFC6901] to Jackson library JsonNode objects [Jackson Project] representing the localizations for the property
     * @see <a href="https://tools.ietf.org/html/rfc5646">RFC5646</a>
     * @see <a href="https://tools.ietf.org/html/rfc6901">RFC6901</a>
     * @see <a href="https://github.com/FasterXML/jackson">Jackson Project</a>
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
     * @return the Jackson library JsonNode object [Jackson Project] representing the localization for the property
     * @see <a href="https://tools.ietf.org/html/rfc5646">RFC5646</a>
     * @see <a href="https://tools.ietf.org/html/rfc6901">RFC6901</a>
     * @see <a href="https://github.com/FasterXML/jackson">Jackson Project</a>
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
        localizedCard.setLanguage(language);
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

        return localizations.keySet().toArray(new String[0]);
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
     * Adds a VCardProp object to this object.
     *
     * @param o the VCardProp object
     */
    public void addVCardProp(VCardProp o) {

        vCardProps = ArrayUtils.add(vCardProps, o);
    }


    /**
     * Convert the vCardProps array into a map
     * where the keys are the extnsion names and
     * the values are the extension values in text format
     *
     * @return vCardProps array converted into a map
     */
    @JsonIgnore
    public Map<String,String> getVCardPropsAsMap() {

        Map<String,String> map = new HashMap<>();
        if (this.getVCardProps() == null)
            return map;

        for (VCardProp jCardExtension : this.getVCardProps())
            map.put(jCardExtension.getName().toString(),jCardExtension.getValue().toString());

        return map;
    }


    /**
     * Tests if a JSContact Card is valid.
     *
     * @return true if the validation check ends successfully, false otherwise
     */
    @JsonIgnore
    public boolean isValid() {

        validationMessages = new ArrayList<>();

        Set<ConstraintViolation<Card>> constraintViolations;
        constraintViolations = ValidatorBuilder.getValidator().validate(this);
        if (constraintViolations.size() > 0) {
            for (ConstraintViolation<Card> constraintViolation : constraintViolations)
                validationMessages.add(constraintViolation.getMessage());
            return false;
        }

        return true;
    }

    /**
     * Returns the error message when the validation check ends unsuccessfully.
     *
     * @return the validation message as a text
     */
    @JsonIgnore
    public String getValidationMessage() {

        if (validationMessages == null)
            return null;

        return String.join("\n", validationMessages);
    }

}

package it.cnr.iit.jscontact.tools.vcard.converters.jscontact2ezvcard;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.module.SimpleModule;
import ezvcard.VCard;
import ezvcard.VCardDataType;
import ezvcard.VCardVersion;
import ezvcard.parameter.*;
import ezvcard.property.*;
import ezvcard.util.*;
import ezvcard.util.PartialDate;
import it.cnr.iit.jscontact.tools.dto.*;
import it.cnr.iit.jscontact.tools.dto.Address;
import it.cnr.iit.jscontact.tools.dto.Anniversary;
import it.cnr.iit.jscontact.tools.dto.Note;
import it.cnr.iit.jscontact.tools.dto.Organization;
import it.cnr.iit.jscontact.tools.dto.TimeZone;
import it.cnr.iit.jscontact.tools.dto.Title;
import it.cnr.iit.jscontact.tools.dto.deserializers.JSContactListDeserializer;
import it.cnr.iit.jscontact.tools.dto.interfaces.HasContext;
import it.cnr.iit.jscontact.tools.dto.interfaces.VCardTypeDerivedEnum;
import it.cnr.iit.jscontact.tools.dto.utils.DelimiterUtils;
import it.cnr.iit.jscontact.tools.dto.utils.JsonNodeUtils;
import it.cnr.iit.jscontact.tools.dto.utils.VCardUtils;
import it.cnr.iit.jscontact.tools.dto.utils.X_RFC0000_JSPROP_Utils;
import it.cnr.iit.jscontact.tools.exceptions.CardException;
import it.cnr.iit.jscontact.tools.exceptions.InternalErrorException;
import it.cnr.iit.jscontact.tools.vcard.converters.AbstractConverter;
import it.cnr.iit.jscontact.tools.vcard.converters.config.JSContact2VCardConfig;
import lombok.NoArgsConstructor;
import org.apache.commons.lang3.StringUtils;

import java.lang.reflect.Constructor;
import java.net.URI;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Utility class for converting a JSContact object into a vCard 4.0 [RFC6350] instance represented as an Ezvcard VCard object.
 *
 * @see <a href="https://tools.ietf.org/html/rfc6350">RFC6350</a>
 * @author Mario Loffredo
 */
@NoArgsConstructor
public class JSContact2EZVCard extends AbstractConverter {

    private static final Map<String,String> ezclassesPerPropertiesMap = new HashMap<String,String>() {{
        put("Address", "ADR");
        put("Birthday","BDAY");
        put("CalendarUri","CALURI");
        put("CalendarRequestUri","CALADRURI");
        put("FormattedName", "FN");
        put("FreeBusyUrl","FBURL");
        put("Language","LANG");
        put("Organization","ORG");
        put("OrgDirectories","ORG-DIRECTORY");
        put("ProductId","PRODID");
        put("Revision","REV");
        put("StructuredName", "N");
        put("Telephone","TEL");
        put("Timezone","TZ");
    }};

    protected JSContact2VCardConfig config;

    private static String getVCardType(HasContext o) {

        if (o.hasNoContext())
            return null;

        List<String> vCardTypeValues = toVCardTypeValues(ContextEnum.class, Context.toEnumValues(o.getContexts().keySet()));

        if (vCardTypeValues.isEmpty())
            return null;

        return String.join(DelimiterUtils.COMMA_ARRAY_DELIMITER, vCardTypeValues);
    }


    private void addPropId (VCardProperty property, String propId) {

        if (propId != null && config.isSetPropIdParam())
           property.addParameter(VCardUtils.VCARD_PROP_ID_PARAM_TAG, propId);
    }

    private static Kind getKind(KindType kind) {

        if (kind == null)
            return null;

        if (kind.getExtValue() != null)
            return new Kind(kind.getExtValue().toString());

        return new Kind(kind.getRfcValue().getValue());
    }

    private static Uid getUid(String uid) {

        if (uid == null)
            return null;

        return new Uid(uid);
    }

    private static Revision getRevision(Calendar update) {

        if (update == null)
            return null;

        return new Revision(update);
    }

    private static void fillMembers(VCard vcard, CardGroup jsCardGroup) {

        if (jsCardGroup.getMembers() == null)
            return;

        for (String key : jsCardGroup.getMembers().keySet())
            vcard.addMember(new Member(key));

    }


    private static String getNameComponent(NameComponent[] name, NameComponentEnum type) {

        for (NameComponent component : name)
            if (component.getType().getRfcValue()!=null &&  component.getType().getRfcValue() == type)
                return component.getValue();

        return null;
    }

    private static void addNameComponent(StringJoiner joiner, NameComponent[] name, NameComponentEnum type) {

        String component = getNameComponent(name, type);
        joiner.add(component);
    }

    private static void addNameComponent(StringJoiner joiner, List<String> values) {

        if (values != null && values.size() != 0 )
            joiner.add(String.join(DelimiterUtils.COMMA_ARRAY_DELIMITER, values));
    }

    private static void addNameComponent(StringJoiner joiner, String value) {

        if (value != null)
            joiner.add(value);
    }

    private static FormattedName getFormattedName(NameComponent[] name) {

        String separator = getNameComponent(name, NameComponentEnum.SEPARATOR);
        StringJoiner joiner = new StringJoiner((separator!=null) ? separator : StringUtils.SPACE);
        addNameComponent(joiner, name, NameComponentEnum.PREFIX);
        addNameComponent(joiner, name, NameComponentEnum.GIVEN);
        addNameComponent(joiner, name, NameComponentEnum.SURNAME);
        addNameComponent(joiner, name, NameComponentEnum.MIDDLE);
        addNameComponent(joiner, name, NameComponentEnum.SUFFIX);
        return new FormattedName(joiner.toString());
    }

    private static FormattedName getFormattedName(StructuredName sn, String separator) {

        StringJoiner joiner = new StringJoiner((separator!=null) ? separator : StringUtils.SPACE);
        addNameComponent(joiner, sn.getPrefixes());
        addNameComponent(joiner, sn.getGiven());
        addNameComponent(joiner, sn.getFamily());
        addNameComponent(joiner, sn.getAdditionalNames());
        addNameComponent(joiner, sn.getSuffixes());
        return new FormattedName(joiner.toString());
    }

    private static FormattedName getFormattedName(String name) {

        return new FormattedName(name);
    }

    private static void fillFormattedNames(VCard vcard, Card jsCard) {

        if (StringUtils.isEmpty(jsCard.getFullName())) {
            if (jsCard.getName() != null) {
                List<StructuredName> sns = getStructuredNames(jsCard);
                String separator = getNameComponent(jsCard.getName().getComponents(), NameComponentEnum.SEPARATOR);
                if (sns.size() == 1) {
                    FormattedName fn = getFormattedName(sns.get(0), separator);
                    fn.setParameter(VCardUtils.VCARD_DERIVED_PARAM_TAG, "true");
                    fn.setLanguage(jsCard.getLocale());
                    vcard.setFormattedName(fn);
                }
                else {
                    List<FormattedName> fns = new ArrayList<>();
                    for (StructuredName sn : sns) {
                        FormattedName fn = getFormattedName(sn, separator);
                        fn.setParameter(VCardUtils.VCARD_DERIVED_PARAM_TAG, "true");
                        fn.setLanguage(sn.getLanguage());
                        fns.add(fn);
                    }
                    vcard.setFormattedNameAlt(fns.toArray(new FormattedName[0]));
                }
            }
            else
                vcard.setFormattedName(jsCard.getUid());
            return;
        }

        if (jsCard.getLocalizationsPerPath("fullName") != null) {
            List<FormattedName> fns = new ArrayList<>();
            FormattedName fn = getFormattedName(jsCard.getFullName());
            fn.setLanguage(jsCard.getLocale());
            fns.add(fn);
            vcard.setFormattedName(fn);
            for (Map.Entry<String,JsonNode> localizations : jsCard.getLocalizationsPerPath("fullName").entrySet()) {
                fn = getFormattedName(localizations.getValue().asText());
                fn.setLanguage(localizations.getKey());
                fns.add(fn);
            }
            vcard.setFormattedNameAlt(fns.toArray(new FormattedName[0]));
        } else
            vcard.setFormattedName(getFormattedName(jsCard.getFullName()));

    }

    private static StructuredName getStructuredName(NameComponent[] nameComponents) {

        StructuredName name = new StructuredName();
        for (NameComponent component : nameComponents) {
            if (component.getType().getRfcValue() == null)
                continue;
            switch(component.getType().getRfcValue()) {
                case PREFIX:
                    name.getPrefixes().add(component.getValue());
                    break;
                case GIVEN:
                    name.setGiven(component.getValue());
                    break;
                case SURNAME:
                    name.setFamily(component.getValue());
                    break;
                case MIDDLE:
                    name.getAdditionalNames().add(component.getValue());
                    break;
                case SUFFIX:
                    name.getSuffixes().add(component.getValue());
                    break;
            }
        }

        return name;
    }

    private static List<StructuredName> getStructuredNames(Card jsCard) {

        List<StructuredName> sns = new ArrayList<>();
        if (jsCard.getLocalizationsPerPath("name") != null) {
            StructuredName sn = getStructuredName(jsCard.getName().getComponents());
            sn.setLanguage(jsCard.getLocale());
            VCardUtils.addVCardUnmatchedParameters(sn,jsCard.getName());
            sns.add(sn);
            for (Map.Entry<String, JsonNode> localizations : jsCard.getLocalizationsPerPath("name").entrySet()) {
                sn = getStructuredName(asNameComponentArray(localizations.getValue().get("components")));
                sn.setLanguage(localizations.getKey());
                sns.add(sn);
            }
        }
        else if (jsCard.getLocalizationsPerPath("name/components") != null) {
            StructuredName sn = getStructuredName(jsCard.getName().getComponents());
            sn.setLanguage(jsCard.getLocale());
            VCardUtils.addVCardUnmatchedParameters(sn,jsCard.getName());
            sns.add(sn);
            for (Map.Entry<String, JsonNode> localizations : jsCard.getLocalizationsPerPath("name/components").entrySet()) {
                sn = getStructuredName(asNameComponentArray(localizations.getValue()));
                sn.setLanguage(localizations.getKey());
                sns.add(sn);
            }
        }
        else {
            StructuredName sn = getStructuredName(jsCard.getName().getComponents());
            VCardUtils.addVCardUnmatchedParameters(sn,jsCard.getName());
            sns.add(sn);
        }

        return sns;

    }


    private static void fillNames(VCard vcard, Card jsCard) {

        if (jsCard.getName() == null)
            return;

        List<StructuredName> sns = getStructuredNames(jsCard);
        if (sns.size() == 1)
            vcard.setStructuredName(sns.get(0));
        else
            vcard.setStructuredNameAlt(sns.toArray(new StructuredName[0]));
    }

    private Nickname getNickname(NickName nickName, String language, String key) {

        if (nickName == null)
            return null;

        Nickname nickname = new Nickname();
        nickname.getValues().addAll(Arrays.asList(nickName.getName().split(DelimiterUtils.COMMA_ARRAY_DELIMITER)));
        nickname.setPref(nickname.getPref());
        nickname.setLanguage(language);
        VCardUtils.addVCardUnmatchedParameters(nickname,nickName);
        if (!nickName.hasNoContext()) {
            List<String> vCardTypeValues = toVCardTypeValues(ContextEnum.class, Context.toEnumValues(nickName.getContexts().keySet()));
            nickname.setType(String.join(DelimiterUtils.COMMA_ARRAY_DELIMITER, vCardTypeValues));
        }
        addPropId(nickname, key);
        return nickname;
    }

    private Nickname getNickname(String nickNames, String language, String key) {

        if (nickNames == null)
            return null;

        Nickname nickname = new Nickname();
        nickname.getValues().addAll(Arrays.asList(nickNames.split(DelimiterUtils.COMMA_ARRAY_DELIMITER)));
        nickname.setLanguage(language);
        addPropId(nickname, key);
        return nickname;
    }

    private Nickname asNickname(JsonNode node, String language, String key) {

        if (!node.isObject())
            return null;

        Nickname nickname = new Nickname();
        nickname.getValues().addAll(Arrays.asList(node.get("name").asText().split(DelimiterUtils.COMMA_ARRAY_DELIMITER)));
        nickname.setLanguage(language);
        addPropId(nickname, key);
        return nickname;
    }

    private void fillNickNames(VCard vcard, Card jsCard) {

        if (jsCard.getNickNames() == null)
            return;

        for (Map.Entry<String,NickName> entry : jsCard.getNickNames().entrySet()) {
            if (jsCard.getLocalizationsPerPath("nickNames/"+entry.getKey()) == null &&
                    jsCard.getLocalizationsPerPath("nickNames/"+entry.getKey()+"/name")==null)
                vcard.addNickname(getNickname(entry.getValue(), jsCard.getLocale(), entry.getKey()));
            else {
                List<ezvcard.property.Nickname> nicknames = new ArrayList<>();
                nicknames.add(getNickname(entry.getValue(), jsCard.getLocale(), entry.getKey()));

                Map<String,JsonNode> localizations = jsCard.getLocalizationsPerPath("nickNames/"+entry.getKey());
                if (localizations != null) {
                    for (Map.Entry<String, JsonNode> localization : localizations.entrySet())
                        nicknames.add(asNickname(localization.getValue(), localization.getKey(), entry.getKey()));
                }
                localizations = jsCard.getLocalizationsPerPath("nickNames/"+entry.getKey()+"/name");
                if (localizations != null) {
                    for (Map.Entry<String,JsonNode> localization : localizations.entrySet())
                        nicknames.add(getNickname(localization.getValue().asText(), localization.getKey(), entry.getKey()));
                }
                vcard.addNicknameAlt(nicknames.toArray(new ezvcard.property.Nickname[0]));
            }
        }
    }

    private static boolean isNullStructuredAddress(Address address) {

        return (address.getCountry() ==null &&
                address.getCountryCode() ==null &&
                address.getRegion() == null &&
                address.getLocality() == null &&
                address.getStreetDetails() == null &&
                address.getPostOfficeBox() == null &&
                address.getPostcode() == null &&
                address.getStreetExtensions() == null);
    }

    private static String getFullAddressFromStructuredAddress(Address addr) {

        StringJoiner joiner = new StringJoiner(DelimiterUtils.NEWLINE_DELIMITER);
        if (StringUtils.isNotEmpty(addr.getPostOfficeBox())) joiner.add(addr.getPostOfficeBox());
        if (StringUtils.isNotEmpty(addr.getStreetExtensions())) joiner.add(addr.getStreetExtensions());
        if (StringUtils.isNotEmpty(addr.getStreetDetails())) joiner.add(addr.getStreetDetails());
        if (StringUtils.isNotEmpty(addr.getLocality())) joiner.add(addr.getLocality());
        if (StringUtils.isNotEmpty(addr.getRegion())) joiner.add(addr.getRegion());
        if (StringUtils.isNotEmpty(addr.getPostcode())) joiner.add(addr.getPostcode());
        if (StringUtils.isNotEmpty(addr.getCountry())) joiner.add(addr.getCountry());
        return joiner.toString();
    }

    private static <E extends Enum<E> & VCardTypeDerivedEnum> List toVCardTypeValues(Class<E> enumType, Collection<E> enumValues) {

        List typeValues = new ArrayList();
        for (E value : enumValues) {
            try {
                String typeItem = (String) enumType.getDeclaredMethod("toVCardType", enumType).invoke(null, value);
                if (typeItem != null)
                    typeValues.add(typeItem);
            } catch (Exception e) {
                throw new InternalErrorException(e.getMessage());

            }
        }
        return typeValues;
    }

    private static <E extends Enum<E> & VCardTypeDerivedEnum> List toVCardTypeValues(Class<E> enumType, String[] stringValues) {
        List typeValues = new ArrayList();
        for (String value : stringValues) {
            try {
                String typeItem = (String) enumType.getDeclaredMethod("toVCardType", String.class).invoke(null, value);
                if (typeItem != null)
                    typeValues.add(typeItem);
            } catch (Exception e) {
                throw new InternalErrorException(e.getMessage());
            }
        }
        return typeValues;
    }

    private static String getOffsetFromTimezone(String timezone) {

        Pattern pattern = Pattern.compile("Etc/GMT(\\+|\\-)\\d{1,2}");
        Matcher matcher = pattern.matcher(timezone);

        if (!matcher.find())
            return timezone;

        String offset = "";
        char[] chars = timezone.toCharArray();
        if (chars[7] == '+')
            offset += "-";
        else
            offset += "+";

        return offset += String.format("%02d00", Integer.parseInt(timezone.substring(8)));
    }

    private String getTimezoneAsText(String timeZone)   {

      if (timeZone == null)
          return null;

      if (config.isConvertTimezoneToOffset())
          return getOffsetFromTimezone(timeZone);
      else
          return timeZone;
    }

    private Timezone getTimezone(String timeZone)   {

        if (timeZone == null)
            return null;

        if (config.isConvertTimezoneToOffset()) {
            try {
                return new Timezone(UtcOffset.parse(getOffsetFromTimezone(timeZone)));
            } catch (Exception e) {}
        }

        return new Timezone(timeZone);
    }



    private GeoUri getGeoUri(String coordinates) {

        if (coordinates == null)
            return null;

        return GeoUri.parse(coordinates);
    }

    private ezvcard.property.Address getAddress(Address address, Map<String, TimeZone> timeZones, String language) {

        ezvcard.property.Address addr = new ezvcard.property.Address();
        if (!isNullStructuredAddress(address)) {
            if (config.isSetAutoAddrLabel())
                addr.setLabel(getFullAddressFromStructuredAddress(address));
            addr.setCountry(address.getCountry());
            addr.setRegion(address.getRegion());
            addr.setLocality(address.getLocality());
            addr.setStreetAddress(address.getStreetDetails());
            addr.setExtendedAddress(address.getStreetExtensions());
            addr.setPoBox(address.getPostOfficeBox());
            addr.setPostalCode(address.getPostcode());
            if (address.getTimeZone() != null) {
                TimeZone timeZone = null;
                if (timeZones != null)
                    timeZone = timeZones.get(address.getTimeZone());
                if (timeZone != null) {
                    if (timeZone.getStandard() != null && timeZone.getStandard().size() > 0)
                        addr.setTimezone(timeZone.getStandard().get(0).getOffsetFrom());
                } else
                    addr.setTimezone(getTimezoneAsText(address.getTimeZone()));
            }
            addr.setGeo(getGeoUri(address.getCoordinates()));
            if (address.getCountryCode() != null)
                addr.setParameter("CC", address.getCountryCode());
            if (!address.hasNoContext()) {
                List<String> vCardTypeValues = toVCardTypeValues(AddressContextEnum.class, AddressContext.toEnumValues(address.getContexts().keySet()));
                for (String vCardTypeValue : vCardTypeValues)
                    addr.getTypes().add(AddressType.get(vCardTypeValue));
            }
        }

        if (address.getFullAddress() != null)
            addr.setLabel(address.getFullAddress());

        addr.setLanguage(language);
        VCardUtils.addVCardUnmatchedParameters(addr,address);

        return addr;
    }

    private static Object asObject(JsonNode jsonNode, Class classs) {

        if (!jsonNode.isObject())
            return null;
        try {
            return mapper.convertValue(jsonNode, classs);
        } catch (Exception e) {
            return null;
        }
    }


    private static Address asAddress(JsonNode jsonNode) {
        return (Address) asObject(jsonNode, Address.class);
    }

    private static Pronouns asPronouns(JsonNode jsonNode) {
        return (Pronouns) asObject(jsonNode, Pronouns.class);
    }

    private static NameComponent[] asNameComponentArray(JsonNode arrayNode) {

        if (!arrayNode.isArray())
            return null;
        List<NameComponent> ncs = new ArrayList<>();
        try {
            for (JsonNode node : arrayNode) {
                NameComponent nc = (NameComponent) asObject(node, NameComponent.class);
                if (nc!=null)
                    ncs.add(nc);
            }
            return (ncs.size() > 0) ? ncs.toArray(new NameComponent[0]) : null;
        } catch (Exception e) {
            return null;
        }

    }

    private void fillAddresses(VCard vcard, Card jsCard) {

        if (jsCard.getAddresses() == null)
            return;

        for (Map.Entry<String,Address> entry : jsCard.getAddresses().entrySet()) {

            Address address = entry.getValue();
            if (isNullStructuredAddress(address) && address.getFullAddress() == null)
                continue;

            if (jsCard.getLocalizationsPerPath("addresses/"+entry.getKey()) == null &&
                jsCard.getLocalizationsPerPath("addresses/"+entry.getKey()+"/fullAddress")==null) {
                ezvcard.property.Address addr = getAddress(address, jsCard.getCustomTimeZones(), jsCard.getLocale());
                addPropId(addr, entry.getKey());
                vcard.addAddress(addr);
            }
            else {
                List<ezvcard.property.Address> addrs = new ArrayList<>();
                ezvcard.property.Address addr = getAddress(address, jsCard.getCustomTimeZones(), jsCard.getLocale());
                addPropId(addr, entry.getKey());
                addrs.add(addr);

                Map<String,JsonNode> localizations = jsCard.getLocalizationsPerPath("addresses/"+entry.getKey());
                if (localizations != null) {
                    for (Map.Entry<String, JsonNode> localization : localizations.entrySet())
                        addrs.add(getAddress(asAddress(localization.getValue()), jsCard.getCustomTimeZones(),localization.getKey()));
                }
                localizations = jsCard.getLocalizationsPerPath("addresses/"+entry.getKey()+"/fullAddress");
                if (localizations != null) {
                    for (Map.Entry<String,JsonNode> localization : localizations.entrySet())
                        addrs.add(getAddress(Address.builder().fullAddress(localization.getValue().asText()).build(), jsCard.getCustomTimeZones(), localization.getKey()));
                }
                vcard.addAddressAlt(addrs.toArray(new ezvcard.property.Address[0]));
            }
        }
    }

    private static <T extends PlaceProperty> T getPlaceProperty(Class<T> classs, Anniversary anniversary) {

        if (anniversary.getPlace() == null)
            return null;

        try {
            Constructor<T> constructor;
            if (anniversary.getPlace().getFullAddress() != null) {
                constructor = classs.getDeclaredConstructor(String.class);
                return constructor.newInstance(anniversary.getPlace().getFullAddress());
            }

            if (!isNullStructuredAddress(anniversary.getPlace())) {
                constructor = classs.getDeclaredConstructor(String.class);
                return constructor.newInstance(getFullAddressFromStructuredAddress(anniversary.getPlace()));
            }

            if (anniversary.getPlace().getCoordinates() != null) {
                GeoUri geoUri = GeoUri.parse(anniversary.getPlace().getCoordinates());
                constructor = classs.getDeclaredConstructor(double.class, double.class);
                return constructor.newInstance(geoUri.getCoordA(), geoUri.getCoordB());
            }
        } catch (Exception e) {
            throw new InternalErrorException(e.getMessage());
        }

        return null;
    }


    private static <T extends DateOrTimeProperty> T getDateOrTimeProperty(Class<T> classs, Anniversary anniversary) {

        try {
            if (anniversary.getDate().getDate()!=null) {
                Constructor<T> constructor = classs.getDeclaredConstructor(Calendar.class, boolean.class);
                return constructor.newInstance(anniversary.getDate().getDate().getUtc(), true);
            }
            if (anniversary.getDate().getPartialDate()!=null) {
                Constructor<T> constructor = classs.getDeclaredConstructor(PartialDate.class);
                T property = constructor.newInstance(anniversary.getDate().getPartialDate().toVCardPartialDate());
                try {
                    property.setCalscale(Calscale.get(anniversary.getDate().getPartialDate().getCalendarScale()));
                } catch(Exception e) {}
                return property;
            }
        } catch (Exception e) {
            throw new InternalErrorException(e.getMessage());
        }

        return null;
    }

    private void fillAnniversaries(VCard vcard, Card jsCard) {

        if (jsCard.getAnniversaries() == null)
            return;

        for (Map.Entry<String,Anniversary> entry : jsCard.getAnniversaries().entrySet()) {

            Anniversary anniversary = entry.getValue();
            if (!anniversary.isOtherAnniversary()) {
                switch (anniversary.getType().getRfcValue()) {
                    case BIRTH:
                        vcard.setBirthday(getDateOrTimeProperty(Birthday.class, anniversary));
                        VCardUtils.addVCardUnmatchedParameters(vcard.getBirthday(),anniversary);
                        addPropId(vcard.getBirthday(), entry.getKey());
                        vcard.setBirthplace(getPlaceProperty(Birthplace.class, anniversary));
                        break;
                    case DEATH:
                        vcard.setDeathdate(getDateOrTimeProperty(Deathdate.class, anniversary));
                        VCardUtils.addVCardUnmatchedParameters(vcard.getDeathdate(),anniversary);
                        addPropId(vcard.getDeathdate(), entry.getKey());
                        vcard.setDeathplace(getPlaceProperty(Deathplace.class, anniversary));
                        break;
                    case MARRIAGE:
                        vcard.setAnniversary(getDateOrTimeProperty(ezvcard.property.Anniversary.class, anniversary));
                        VCardUtils.addVCardUnmatchedParameters(vcard.getAnniversary(),anniversary);
                        addPropId(vcard.getAnniversary(), entry.getKey());
                        break;
                }
            }
        }

    }

    private Expertise getExpertise(PersonalInformation pi) {

        Expertise e = new Expertise(pi.getValue());
        VCardUtils.addVCardUnmatchedParameters(e,pi);
        addPropId(e, pi.getPropId());
        if (pi.getLevel()!= null && pi.getLevel().isRfcValue())
            e.setLevel(ExpertiseLevel.get(PersonalInformationLevelEnum.getVCardExpertiseLevel(pi.getLevel().getRfcValue())));
        else
            e.setParameter(VCardUtils.VCARD_LEVEL_PARAM_TAG, pi.getLevel().getExtValue().toString().toUpperCase());
        return e;
    }

    private Hobby getHobby(PersonalInformation pi) {

        Hobby h = new Hobby(pi.getValue());
        VCardUtils.addVCardUnmatchedParameters(h,pi);
        addPropId(h, pi.getPropId());
        if (pi.getLevel()!= null && pi.getLevel().isRfcValue())
            h.setLevel(HobbyLevel.get(pi.getLevel().getRfcValue().name()));
        else
            h.setParameter(VCardUtils.VCARD_LEVEL_PARAM_TAG, pi.getLevel().getExtValue().toString().toUpperCase());
        return h;
    }

    private Interest getInterest(PersonalInformation pi) {

        Interest i = new Interest(pi.getValue());
        VCardUtils.addVCardUnmatchedParameters(i,pi);
        addPropId(i, pi.getPropId());
        if (pi.getLevel()!= null && pi.getLevel().isRfcValue())
            i.setLevel(InterestLevel.get(pi.getLevel().getRfcValue().name()));
        else
            i.setParameter(VCardUtils.VCARD_LEVEL_PARAM_TAG, pi.getLevel().getExtValue().toString().toUpperCase());
        return i;
    }

    private void fillPersonalInfos(VCard vcard, Card jsCard) {

        if (jsCard.getPersonalInfo() == null)
            return;

        for (Map.Entry<String,PersonalInformation> entry : jsCard.getPersonalInfo().entrySet()) {
            PersonalInformation pi = entry.getValue();
            pi.setPropId(entry.getKey());
            if (pi.getType()!=null && pi.getType().isRfcValue()) {
                switch (pi.getType().getRfcValue()) {
                    case EXPERTISE:
                        vcard.getExpertise().add(getExpertise(pi));
                        break;
                    case HOBBY:
                        vcard.getHobbies().add(getHobby(pi));
                        break;
                    case INTEREST:
                        vcard.getInterests().add(getInterest(pi));
                        break;
                }
            }
        }
    }

    private static Language getLanguage(String lang, LanguagePreference cl) {

        Language language = new Language(lang);
        String vCardTypeValue = getVCardType(cl);
        if (vCardTypeValue!=null)
            language.setParameter(VCardUtils.VCARD_TYPE_PARAM_TAG, vCardTypeValue);
        language.setPref(cl.getPref());
        return language;
    }

    private static void fillPreferredLanguages(VCard vcard, Card jsCard) {

        if (jsCard.getPreferredLanguages() == null)
            return;

        for (Map.Entry<String, LanguagePreference[]> clArray : jsCard.getPreferredLanguages().entrySet()) {
            for(LanguagePreference cl : clArray.getValue())
                vcard.addLanguage(getLanguage(clArray.getKey(), cl));
        }
    }

    private Telephone getTelephone(Phone phone) {

        Telephone tel;
        try {
            tel = new Telephone(TelUri.parse(phone.getPhone()));
        } catch(Exception e) {
            tel = new Telephone(phone.getPhone());
        }
        tel.setPref(phone.getPref());
        addPropId(tel, phone.getPropId());

        List<String> vCardTypeValues = new ArrayList<>();
        if (!phone.hasNoContext())
            vCardTypeValues.addAll(toVCardTypeValues(ContextEnum.class, Context.toEnumValues(phone.getContexts().keySet())));
        if (!phone.hasNoFeature())
            vCardTypeValues.addAll(toVCardTypeValues(PhoneFeatureEnum.class, PhoneFeature.toEnumValues(phone.getFeatures().keySet())));

        for (String vCardTypeValue : vCardTypeValues)
            tel.getTypes().add(TelephoneType.get(vCardTypeValue));
        VCardUtils.addVCardUnmatchedParameters(tel,phone);

        return tel;
    }

    private void fillPhones(VCard vcard, Card jsCard) {

        if (jsCard.getPhones() == null)
            return;

        for (Map.Entry<String, Phone> entry : jsCard.getPhones().entrySet()) {
            Phone phone = entry.getValue();
            phone.setPropId(entry.getKey());
            vcard.getTelephoneNumbers().add(getTelephone(phone));
        }
    }

    private Email getEmail(EmailAddress emailAddress) {

        Email email = new Email(emailAddress.getEmail());
        email.setPref(emailAddress.getPref());
        addPropId(email, emailAddress.getPropId());
        if (!emailAddress.hasNoContext()) {
            List<String> vCardTypeValues = toVCardTypeValues(ContextEnum.class, Context.toEnumValues(emailAddress.getContexts().keySet()));
            for (String vCardTypeValue : vCardTypeValues)
                email.getTypes().add(EmailType.get(vCardTypeValue));
        }
        VCardUtils.addVCardUnmatchedParameters(email,emailAddress);
        return email;
    }

    private void fillEmails(VCard vcard, Card jsCard) {

        if (jsCard.getEmails() == null)
            return;

        for (Map.Entry<String,EmailAddress> entry : jsCard.getEmails().entrySet()) {
            EmailAddress email = entry.getValue();
            email.setPropId(entry.getKey());
            vcard.getEmails().add(getEmail(email));
        }
    }

    private static ImageType getImageType(String mediaType) {

        if (mediaType == null)
            return null;

        for (ImageType it : ImageType.all()) {
            if (it.getMediaType().equals(mediaType))
                return it;
        }
        return null;
    }

    private static SoundType getSoundType(String mediaType) {

        if (mediaType == null)
            return null;

        for (SoundType it : SoundType.all()) {
            if (it.getMediaType().equals(mediaType))
                return it;
        }
        return null;
    }

    private static KeyType getKeyType(String mediaType) {

        if (mediaType == null)
            return null;

        for (KeyType it : KeyType.all()) {
            if (it.getMediaType().equals(mediaType))
                return it;
        }
        return null;
    }

    private static Photo getPhoto(MediaResource resource) {

        ImageType it = getImageType(resource.getMediaType());
        if (it == null) return null;
        Photo photo = new Photo(resource.getUri(), it);
        photo.setPref(resource.getPref());
        photo.setContentType(it);
        VCardUtils.addVCardUnmatchedParameters(photo,resource);
        return photo;
    }

    private static Impp getImpp(OnlineService onlineService) {

        Impp impp;
        if (onlineService.getUri() == null) {
            try {
                impp = new Impp(onlineService.getService(), onlineService.getUsername());
            }
            catch (IllegalArgumentException e) {
                return null;
            }
        } else
            impp = new Impp(onlineService.getUri());

        impp.setPref(onlineService.getPref());
        VCardUtils.addVCardUnmatchedParameters(impp,onlineService);

        return impp;
    }

    private static <T extends VCardProperty> void fillVCardProperty(T property, Resource resource) {

        if (resource.getMediaType()!=null)
            property.setParameter(VCardUtils.VCARD_MEDIATYPE_PARAM_TAG,resource.getMediaType());
        if (resource.getPref() != null)
            property.setParameter(VCardUtils.VCARD_PREF_PARAM_TAG, resource.getPref().toString());
        String vCardTypeValue = getVCardType(resource);
        if (vCardTypeValue!=null)
            property.setParameter(VCardUtils.VCARD_TYPE_PARAM_TAG, vCardTypeValue);
    }

    private <T extends UriProperty> T getUriProperty(Class<T> classs, Resource resource) {

        try {
            Constructor<T> constructor = classs.getDeclaredConstructor(String.class);
            T object = constructor.newInstance(resource.getUri());
            fillVCardProperty(object,resource);
            VCardUtils.addVCardUnmatchedParameters(object,resource);
            addPropId(object, resource.getPropId());
            return object;
        } catch (Exception e) {
            throw new InternalErrorException(e.getMessage());
        }
    }

    private <T extends BinaryProperty> T getBinaryProperty(Class<T> classs, Resource resource) {

        try {
            ImageType it = getImageType(resource.getMediaType());
            Constructor<T> constructor = classs.getDeclaredConstructor(String.class, ImageType.class);
            T object = constructor.newInstance(resource.getUri(), it);
            fillVCardProperty(object,resource);
            VCardUtils.addVCardUnmatchedParameters(object,resource);
            addPropId(object, resource.getPropId());
            return object;
        } catch (Exception e) {
            throw new InternalErrorException(e.getMessage());
        }

    }

    private void fillOnlineServices(VCard vcard, Card jsCard) {

        if (jsCard.getOnlineServices() == null)
            return;

        for(Map.Entry<String, OnlineService> entry : jsCard.getOnlineServices().entrySet()) {
            OnlineService onlineService = entry.getValue();
            Impp impp = getImpp(onlineService);
            if (impp == null) continue;
            addPropId(impp, entry.getKey());
            vcard.getImpps().add(impp);
            if (!onlineService.hasNoContext()) {
                List<String> vCardTypeValues = toVCardTypeValues(ContextEnum.class, Context.toEnumValues(onlineService.getContexts().keySet()));
                for (String vCardTypeValue : vCardTypeValues)
                    impp.getTypes().add(ImppType.get(vCardTypeValue));
            }

        }
    }

    private void fillSchedulingAddresses(VCard vcard, Card jsCard) {

        if (jsCard.getSchedulingAddresses() == null)
            return;

        for(Map.Entry<String, SchedulingAddress> entry : jsCard.getSchedulingAddresses().entrySet()) {
            SchedulingAddress s = entry.getValue();
            s.setPropId(entry.getKey());
            if (s.getType()==null || s.getType().isImip())
                vcard.getCalendarRequestUris().add(getUriProperty(CalendarRequestUri.class, s));
        }
    }

    private void fillCalendars(VCard vcard, Card jsCard) {

        if (jsCard.getCalendars() == null)
            return;

        for (Map.Entry<String,CalendarResource> entry : jsCard.getCalendars().entrySet()) {

            CalendarResource resource = entry.getValue();
            resource.setPropId(entry.getKey());
            if (resource.getType()!=null && resource.getType().isRfcValue()) {
                switch (resource.getType().getRfcValue()) {
                    case FREEBUSY:
                        vcard.getFbUrls().add(getUriProperty(FreeBusyUrl.class, resource));
                        break;
                    case CALENDAR:
                        vcard.getCalendarUris().add(getUriProperty(CalendarUri.class, resource));
                        break;
                }
            }
        }
    }


    private void fillCryptoKeys(VCard vcard, Card jsCard) {

        if (jsCard.getCryptoKeys() == null)
            return;

        for (Map.Entry<String,CryptoResource> entry : jsCard.getCryptoKeys().entrySet()) {
            Key key = new Key(entry.getValue().getUri(), getKeyType(entry.getValue().getMediaType()));
            VCardUtils.addVCardUnmatchedParameters(key,entry.getValue());
            addPropId(key, entry.getKey());
            vcard.getKeys().add(key);
            break;
        }
    }

    private void fillLinks(VCard vcard, Card jsCard) {

        if (jsCard.getLinks() == null)
            return;

        for (Map.Entry<String,LinkResource> entry : jsCard.getLinks().entrySet()) {

            LinkResource resource = entry.getValue();
            resource.setPropId(entry.getKey());
            if (resource.isGenericLink())
                vcard.getUrls().add(getUriProperty(Url.class,resource));
            else {
                if (resource.getType()!=null && resource.getType().isRfcValue()) {
                    switch (resource.getType().getRfcValue()) {
                        case CONTACT:
                            RawProperty rp = new RawProperty("CONTACT-URI", resource.getUri());
                            fillVCardProperty(rp, resource);
                            VCardUtils.addVCardUnmatchedParameters(rp,resource);
                            addPropId(rp, resource.getPropId());
                            vcard.getExtendedProperties().add(rp);
                            break;
                    }
                }
            }
        }
    }

    private void fillMedia(VCard vcard, Card jsCard) {

        if (jsCard.getMedia() == null)
            return;

        for (Map.Entry<String,MediaResource> entry : jsCard.getMedia().entrySet()) {

            MediaResource resource = entry.getValue();
            resource.setPropId(entry.getKey());
            if (resource.getType()!=null && resource.getType().isRfcValue()) {
                switch (resource.getType().getRfcValue()) {
                    case SOUND:
                        Sound sound = new Sound(resource.getUri(), getSoundType(resource.getMediaType()));
                        VCardUtils.addVCardUnmatchedParameters(sound,resource);
                        addPropId(sound, resource.getPropId());
                        vcard.getSounds().add(sound);
                        break;
                    case LOGO:
                        vcard.getLogos().add(getBinaryProperty(Logo.class, resource));
                        break;
                    case PHOTO:
                        Photo photo = getPhoto(resource);
                        if (photo == null) continue;
                        VCardUtils.addVCardUnmatchedParameters(photo,resource);
                        addPropId(photo, entry.getKey());
                        vcard.getPhotos().add(photo);
                }
            }
        }
    }

    private void fillDirectories(VCard vcard, Card jsCard) {

        if (jsCard.getDirectories() == null)
            return;

        for (Map.Entry<String,DirectoryResource> entry : jsCard.getDirectories().entrySet()) {

            DirectoryResource resource = entry.getValue();
            resource.setPropId(entry.getKey());
            if (resource.getType()!=null && resource.getType().isRfcValue()) {
                switch (resource.getType().getRfcValue()) {
                    case ENTRY:
                        vcard.getSources().add(getUriProperty(Source.class, resource));
                        break;
                    case DIRECTORY:
                        vcard.getOrgDirectories().add(getUriProperty(OrgDirectory.class, resource));
                        break;
                }
            }
        }
    }

    private <E extends TextProperty > E getTextProperty(E property, String language, String propId, Map<String,JCardParam> unmatchedParams) {

        if (language != null) property.getParameters().setLanguage(language);
        VCardUtils.addVCardUnmatchedParameters(property,unmatchedParams);
        addPropId(property, propId);
        return property;
    }

    private <E extends TextListProperty> E getTextListProperty(E property, List<String> textList, String language, String propId, Map<String,JCardParam> unmatchedParams) {

        property.getValues().addAll(textList);
        VCardUtils.addVCardUnmatchedParameters(property,unmatchedParams);
        addPropId(property, propId);
        if (language != null) property.getParameters().setLanguage(language);
        return property;
    }

    private void fillTitles(VCard vcard, Card jsCard) {

        if (jsCard.getTitles() == null)
            return;

        for (Map.Entry<String,Title> entry : jsCard.getTitles().entrySet()) {

            if (jsCard.getLocalizationsPerPath("titles/"+entry.getKey()) == null &&
                jsCard.getLocalizationsPerPath("titles/"+entry.getKey()+"/title")==null)

                if (entry.getValue().getType() == null || entry.getValue().getType().isTitle())
                    vcard.addTitle(getTextProperty(new ezvcard.property.Title(entry.getValue().getTitle()), jsCard.getLocale(), entry.getKey(), entry.getValue().getJCardParams()));
                else
                    vcard.addRole(getTextProperty(new ezvcard.property.Role(entry.getValue().getTitle()), jsCard.getLocale(), entry.getKey(), entry.getValue().getJCardParams()));

            else {
                List<ezvcard.property.Title> titles = new ArrayList<>();
                List<ezvcard.property.Role> roles = new ArrayList<>();

                if (entry.getValue().getType() == null || entry.getValue().getType().isTitle())
                    titles.add(getTextProperty(new ezvcard.property.Title(entry.getValue().getTitle()), jsCard.getLocale(), entry.getKey(), entry.getValue().getJCardParams()));
                else
                    roles.add(getTextProperty(new ezvcard.property.Role(entry.getValue().getTitle()), jsCard.getLocale(), entry.getKey(), entry.getValue().getJCardParams()));


                Map<String,JsonNode> localizations = jsCard.getLocalizationsPerPath("titles/"+entry.getKey());
                if (localizations != null) {
                    for (Map.Entry<String, JsonNode> localization : localizations.entrySet()) {
                        if (jsCard.getTitles().get(entry.getKey()).getType() == null || jsCard.getTitles().get(entry.getKey()).getType().isTitle())
                            titles.add(getTextProperty(new ezvcard.property.Title(localization.getValue().get("title").asText()), localization.getKey(), entry.getKey(), null));
                        else
                            roles.add(getTextProperty(new ezvcard.property.Role(localization.getValue().get("title").asText()), localization.getKey(), entry.getKey(), null));
                    }
                }
                localizations = jsCard.getLocalizationsPerPath("titles/"+entry.getKey()+"/title");
                if (localizations != null) {
                    for (Map.Entry<String,JsonNode> localization : localizations.entrySet()) {
                        if (jsCard.getTitles().get(entry.getKey()).getType() == null || jsCard.getTitles().get(entry.getKey()).getType().isTitle())
                            titles.add(getTextProperty(new ezvcard.property.Title(localization.getValue().asText()), localization.getKey(), entry.getKey(), null));
                        else
                            roles.add(getTextProperty(new ezvcard.property.Role(localization.getValue().asText()), localization.getKey(), entry.getKey(), null));
                    }
                }
                vcard.addTitleAlt(titles.toArray(new ezvcard.property.Title[0]));
                vcard.addRoleAlt(roles.toArray(new ezvcard.property.Role[0]));
            }
        }
    }


    private static void fillCategories(VCard vcard, Card jsCard) {

        if (jsCard.getKeywords() == null)
            return;

        vcard.setCategories(jsCard.getKeywords().keySet().toArray(new String[jsCard.getKeywords().size()]));
    }

    private static List<String> getOrganizationItems(String organization, String[] units) {

        List<String> organizationItems = new ArrayList<>();
        organizationItems.add((organization!=null) ? organization : "");
        if (units != null)
            organizationItems.addAll(Arrays.asList(units));
        return organizationItems;
    }

    private void fillOrganizations(VCard vcard, Card jsCard) {

        if (jsCard.getOrganizations() == null)
            return;

        for (Map.Entry<String,Organization> entry : jsCard.getOrganizations().entrySet()) {

            if (jsCard.getLocalizationsPerPath("organizations/"+entry.getKey()) == null &&
                jsCard.getLocalizationsPerPath("organizations/"+entry.getKey()+"/name")==null)

                vcard.getOrganizations().add(getTextListProperty(new ezvcard.property.Organization(), getOrganizationItems(entry.getValue().getName(), entry.getValue().getUnits()), jsCard.getLocale(), entry.getKey(), entry.getValue().getJCardParams()));

            else {
                List<ezvcard.property.Organization> organizations = new ArrayList<>();
                organizations.add(getTextListProperty(new ezvcard.property.Organization(), getOrganizationItems(entry.getValue().getName(), entry.getValue().getUnits()), jsCard.getLocale(), entry.getKey(), entry.getValue().getJCardParams()));

                Map<String,JsonNode> localizations = jsCard.getLocalizationsPerPath("organizations/"+entry.getKey());
                if (localizations != null) {
                    for (Map.Entry<String, JsonNode> localization : localizations.entrySet())
                        organizations.add(getTextListProperty(new ezvcard.property.Organization(), getOrganizationItems((localization.getValue().get("name")!=null) ? localization.getValue().get("name").asText() : null, JsonNodeUtils.asTextArray(localization.getValue().get("units"))), localization.getKey(), entry.getKey(), null));
                }
                localizations = jsCard.getLocalizationsPerPath("organizations/"+entry.getKey()+"/name");
                if (localizations != null) {
                    for (Map.Entry<String,JsonNode> localization : localizations.entrySet()) {
                        JsonNode units = jsCard.getLocalization(localization.getKey(),"organizations/"+entry.getKey()+"/units");
                        organizations.add(getTextListProperty(new ezvcard.property.Organization(), getOrganizationItems((localization.getValue().get("name")!=null) ? localization.getValue().get("name").asText() : null, JsonNodeUtils.asTextArray(units)), localization.getKey(), entry.getKey(), null));
                    }
                }
                vcard.addOrganizationAlt(organizations.toArray(new ezvcard.property.Organization[0]));
            }
        }
    }


    private static ezvcard.property.Note getNote(Note jsNote) {

        ezvcard.property.Note note = new ezvcard.property.Note(jsNote.getNote());
        note.setLanguage(jsNote.getLanguage());
        VCardUtils.addVCardUnmatchedParameters(note, jsNote);
        return note;
    }


    private void fillNotes(VCard vcard, Card jsCard) {

        if (jsCard.getNotes() == null)
            return;

        List<ezvcard.property.Note> notes = new ArrayList<>();
        boolean languageFound = false;
        for (Note note : jsCard.getNotes()) {
            if (!languageFound)
                languageFound = StringUtils.isNotEmpty(note.getLanguage());
            notes.add(getNote(note));
        }
        if (!languageFound)
            vcard.getNotes().addAll(notes);
        else
            vcard.addNoteAlt(notes.toArray(new ezvcard.property.Note[0]));
    }

    private static Related getRelated(String uriOrText, List<RelationType> types) {

        Related related = getRelated(uriOrText);
        for(RelationType type : types) {
            if (type.getRfcValue() != null)
                related.getTypes().add(RelatedType.get(type.getRfcValue().getValue()));
        }

        return related;
    }


    private static Related getRelated(String uriOrText) {
        Related related = new Related();
        try {
            URI uri = URI.create(uriOrText);
            if (uri.getScheme() == null)
                related.setText(uriOrText);
            else
                related.setUri(uriOrText);
        } catch (IllegalArgumentException e) {
            related.setText(uriOrText);
        }

        return related;
    }

    private static void fillRelations(VCard vcard, Card jsCard) {

        if (jsCard.getRelatedTo() == null)
            return;

        for (String key : jsCard.getRelatedTo().keySet()) {
            if (jsCard.getRelatedTo().get(key).getRelation() == null)
                vcard.addRelated(getRelated(key));
            else
                vcard.addRelated(getRelated(key, new ArrayList<>(jsCard.getRelatedTo().get(key).getRelation().keySet())));
        }
    }

    private static ClientPidMap getCliendPidMap(String pid, String uri) {

        return new ClientPidMap(Integer.parseInt(pid), uri);
    }


    private static String getPropertyNameFromClassName(String className) {

        for (Map.Entry<String,String> pair : ezclassesPerPropertiesMap.entrySet()) {

            if (pair.getKey().equals(className))
                return pair.getValue();
        }

        return className.toUpperCase();
    }


    private void fillVCardExtensions(VCard vcard, Card jsCard) {

        if (jsCard.getJCardExtensions() == null)
            return;

        for (JCardProp jCardProp : jsCard.getJCardExtensions()) {

            if (jCardProp.getName().equals(V_Extension.toV_Extension(VCardUtils.VCARD_CLIENTPIDMAP_TAG))) {
                String pid = ((String) jCardProp.getValue()).split(",")[0];
                String uri = ((String) jCardProp.getValue()).split(",")[1];
                ClientPidMap pidmap = getCliendPidMap(pid, uri);
                pidmap.setParameters(jCardProp.getVCardParameters());
                vcard.addClientPidMap(pidmap);
            }
            else if (jCardProp.getName().equals(V_Extension.toV_Extension(VCardUtils.VCARD_XML_TAG))) {
                try {
                    Xml xml = new Xml(((String) jCardProp.getValue()));
                    xml.setParameters(jCardProp.getVCardParameters());
                    vcard.getXmls().add(xml);
                } catch (Exception e) {
                    throw new InternalErrorException(e.getMessage());
                }
            }
            else if (jCardProp.getName().equals(V_Extension.toV_Extension(VCardUtils.VCARD_TZ_TAG))) {

                Timezone tz = null;
                TimeZone timeZone = null;
                if (jsCard.getCustomTimeZones() != null)
                    timeZone = jsCard.getCustomTimeZones().get((String) jCardProp.getValue());
                if (timeZone != null) {
                    if (timeZone.getStandard() != null && timeZone.getStandard().size() > 0)
                        tz = new Timezone(UtcOffset.parse((timeZone.getStandard().get(0).getOffsetFrom())));
                } else
                    tz = getTimezone((String) jCardProp.getValue());
                tz.setParameters(jCardProp.getVCardParameters());
                vcard.setTimezone(tz);
            }
            else if (jCardProp.getName().equals(V_Extension.toV_Extension(VCardUtils.VCARD_GEO_TAG))) {
                Geo geo = new Geo(getGeoUri((String) jCardProp.getValue()));
                geo.setParameters(jCardProp.getVCardParameters());
                vcard.setGeo(geo);
            }
            else {
                RawProperty property = new RawProperty(jCardProp.getName().toString().toUpperCase(), jCardProp.getValue().toString(), jCardProp.getType());
                property.setParameters(jCardProp.getVCardParameters());
                vcard.addProperty(property);
            }
        }
    }

    //TODO: replace XXXX with RFC number after draft-ietf-calext-vcard-jscontact-extensions
    private static void fillRFCXXXXProperties(VCard vCard, Card jsCard) {

        if (jsCard.getCreated() != null) {
            vCard.addExtendedProperty("CREATED", VCardDateFormat.UTC_DATE_TIME_BASIC.format(jsCard.getCreated().getTime()), VCardDataType.TIMESTAMP);
        }

        if (jsCard.getLocale() != null) {
            vCard.addExtendedProperty("LOCALE", jsCard.getLocale(), VCardDataType.LANGUAGE_TAG);
        }

        if (jsCard.getSpeakToAs()!= null && jsCard.getSpeakToAs().getPronouns() != null) {
            String propertyName = "PRONOUNS";
            String jsonPointer = fakeExtensionsMapping.get(propertyName.toLowerCase());
            Map<String,Pronouns> pronouns = jsCard.getSpeakToAs().getPronouns();
            for (Map.Entry<String,Pronouns> entry : pronouns.entrySet()) {
                RawProperty raw = new RawProperty(propertyName, entry.getValue().getPronouns());
                String vCardTypeValue = getVCardType(entry.getValue());
                if (vCardTypeValue!=null)
                    raw.setParameter(VCardUtils.VCARD_TYPE_PARAM_TAG, vCardTypeValue);
                if (entry.getValue().getPref()!=null)
                    raw.setParameter(VCardUtils.VCARD_PREF_PARAM_TAG, entry.getValue().getPref().toString());
                raw.setDataType(VCardDataType.TEXT);
                vCard.addProperty(raw);

                jsonPointer = String.format("%s/%s", jsonPointer, entry.getKey());
                Map<String,JsonNode> localizations = jsCard.getLocalizationsPerPath(jsonPointer);
                if (localizations != null) {
                    for (Map.Entry<String,JsonNode> entry2 : localizations.entrySet()) {
                        RawProperty raw2 = new RawProperty(propertyName, asPronouns(entry2.getValue()).getPronouns());
                        raw2.setParameter("LANGUAGE",entry2.getKey());
                        raw2.setDataType(VCardDataType.TEXT);
                        vCard.addProperty(raw2);
                    }
                }
            }
        }

        if (jsCard.getSpeakToAs()!= null && jsCard.getSpeakToAs().getGrammaticalGender() != null) {
            String propertyName = "GRAMMATICAL-GENDER";
            String jsonPointer = fakeExtensionsMapping.get(propertyName.toLowerCase());
            vCard.addExtendedProperty(propertyName, jsCard.getSpeakToAs().getGrammaticalGender().getValue().toUpperCase(), VCardDataType.TEXT);
            Map<String,JsonNode> localizations = jsCard.getLocalizationsPerPath(jsonPointer);
            if (localizations != null) {
                for (Map.Entry<String,JsonNode> entry : localizations.entrySet()) {
                    RawProperty raw = new RawProperty(propertyName, entry.getValue().asText());
                    raw.setParameter("LANGUAGE",entry.getKey());
                    raw.setDataType(VCardDataType.TEXT);
                    vCard.addProperty(raw);
                }
            }
        }

        if (jsCard.getPreferredContactChannels()!=null) {

            String propertyName = "CONTACT-CHANNEL-PREF";
            Map<ChannelType,ContactChannelPreference[]> preferredContactChannels = jsCard.getPreferredContactChannels();
            for (Map.Entry<ChannelType,ContactChannelPreference[]> entry: preferredContactChannels.entrySet()) {
                String value;
                if (entry.getKey().isRfcValue())
                    value = ChannelEnum.toVCardChannelType(entry.getKey().getRfcValue());
                else
                    value = entry.getKey().getExtValue().toString().toUpperCase();
                if (entry.getValue().length == 0) {
                    RawProperty raw = new RawProperty(propertyName, value);
                    raw.setDataType(VCardDataType.TEXT);
                    vCard.addProperty(raw);
                }
                else {
                    for (ContactChannelPreference pref : entry.getValue()) {
                        RawProperty raw = new RawProperty(propertyName, value);
                        String vCardTypeValue = getVCardType(pref);
                        if (vCardTypeValue!=null)
                            raw.setParameter(VCardUtils.VCARD_TYPE_PARAM_TAG, vCardTypeValue);
                        if (pref.getPref()!=null)
                            raw.setParameter(VCardUtils.VCARD_PREF_PARAM_TAG, pref.getPref().toString());
                        raw.setDataType(VCardDataType.TEXT);
                        vCard.addProperty(raw);
                    }
                }
            }
        }
    }

    private void fillJSContactExtensions(VCard vcard, Card jsCard) {

        Map<String,Object> allExtensionsMap = new HashMap<String,Object>();
        jsCard.buildAllExtensionsMap(allExtensionsMap,"");

        for(Map.Entry<String,Object> entry : allExtensionsMap.entrySet()) {
            try {
                RawProperty property = new RawProperty(VCardUtils.VCARD_X_RFC0000_JSPROP_TAG, X_RFC0000_JSPROP_Utils.toX_RFC0000_JSPROPValue(entry.getValue()), VCardDataType.URI);
                property.setParameter(VCardUtils.VCARD_X_RFC0000_JSPATH_PARAM_TAG, entry.getKey());
                vcard.addProperty(property);
            } catch (Exception e) {}
        }
    }

    /**
     * Converts a JSContact object into a basic vCard v4.0 [RFC6350].
     * JSContact objects are defined in draft-ietf-calext-jscontact.
     * Conversion rules are defined in draft-ietf-calext-jscontact-vcard.
     *
     * @param jsContact a JSContact object (Card or CardGroup)
     * @return a vCard as an instance of the ez-vcard library VCard class
     * @see <a href="https://github.com/mangstadt/ez-vcard">ez-vcard library</a>
     * @see <a href="https://datatracker.ietf.org/doc/draft-ietf-calext-jscontact-vcard/">draft-ietf-calext-jscontact-vcard</a>
     * @see <a href="https://datatracker.ietf.org/doc/draft-ietf-calext-jscontact/">draft-ietf-calext-jscontact</a>
     */
    protected VCard convert(JSContact jsContact) {

        if (jsContact == null)
            return null;

        VCard vCard = new VCard(VCardVersion.V4_0);
        vCard.setUid(getUid(jsContact.getUid()));
        Card jsCard = null;
        if (jsContact instanceof CardGroup) {
            CardGroup jsCardGroup = (CardGroup) jsContact;
            fillMembers(vCard, jsCardGroup);
            if (jsCardGroup.getCard()!=null)
                jsCard = jsCardGroup.getCard();
        } else
            jsCard = (Card) jsContact;

        if (jsCard == null) return vCard;

        vCard.setKind(getKind(jsCard.getKind()));
        vCard.setProductId(jsCard.getProdId());
        vCard.setRevision(getRevision(jsCard.getUpdated()));
        fillFormattedNames(vCard, jsCard);
        fillNames(vCard, jsCard);
        fillNickNames(vCard, jsCard);
        fillAddresses(vCard, jsCard);
        fillAnniversaries(vCard, jsCard);
        fillPersonalInfos(vCard, jsCard);
        fillPreferredLanguages(vCard, jsCard);
        fillPhones(vCard, jsCard);
        fillEmails(vCard, jsCard);
        fillSchedulingAddresses(vCard,jsCard);
        fillOnlineServices(vCard, jsCard);
        fillCalendars(vCard, jsCard);
        fillCryptoKeys(vCard, jsCard);
        fillLinks(vCard, jsCard);
        fillMedia(vCard, jsCard);
        fillDirectories(vCard, jsCard);
        fillTitles(vCard, jsCard);
        fillOrganizations(vCard, jsCard);
        fillCategories(vCard, jsCard);
        fillNotes(vCard, jsCard);
        fillRelations(vCard, jsCard);
        fillRFCXXXXProperties(vCard, jsCard);
        fillVCardExtensions(vCard, jsCard);
        fillJSContactExtensions(vCard,jsCard);

        return vCard;
    }

    /**
     * Converts a list of JSContact objects into a list of vCard v4.0 instances [RFC6350].
     * JSContact is defined in draft-ietf-calext-jscontact.
     * Conversion rules are defined in draft-ietf-calext-jscontact-vcard.
     * @param jsContacts a list of JSContact objects
     * @return a list of instances of the ez-vcard library VCard class
     * @throws CardException if one of JSContact objects is not valid
     * @see <a href="https://github.com/mangstadt/ez-vcard">ez-vcard library</a>
     * @see <a href="https://datatracker.ietf.org/doc/draft-ietf-calext-jscontact-vcard/">draft-ietf-calext-jscontact-vcard</a>
     * @see <a href="https://datatracker.ietf.org/doc/draft-ietf-calext-jscontact/">draft-ietf-calext-jscontact</a>
     */
    public List<VCard> convert(JSContact... jsContacts) throws CardException {

        List<VCard> vCards = new ArrayList<>();

        for (JSContact jsContact : jsContacts) {
            if (config.isSetCardMustBeValidated()) {
                if (!jsContact.isValid())
                    throw new CardException(jsContact.getValidationMessage());
            }
            vCards.add(convert(jsContact));
        }

        return vCards;
    }


    /**
     * Converts a JSON array of JSContact objects into a list of vCard v4.0 instances [RFC6350].
     * JSContact is defined in draft-ietf-calext-jscontact.
     * Conversion rules are defined in draft-ietf-calext-jscontact-vcard.
     * @param json a JSON array of JSContact objects
     * @return a list of instances of the ez-vcard library VCard class
     * @throws CardException if one of JSContact objects is not valid
     * @throws JsonProcessingException if json cannot be processed
     * @see <a href="https://github.com/mangstadt/ez-vcard">ez-vcard library</a>
     * @see <a href="https://datatracker.ietf.org/doc/draft-ietf-calext-jscontact-vcard/">draft-ietf-calext-jscontact-vcard</a>
     * @see <a href="https://datatracker.ietf.org/doc/draft-ietf-calext-jscontact/">draft-ietf-calext-jscontact</a>
     */
    public List<VCard> convert(String json) throws CardException, JsonProcessingException {

        SimpleModule module = new SimpleModule();
        module.addDeserializer(JSContact.class, new JSContactListDeserializer());
        mapper.registerModule(module);
        JsonNode jsonNode = mapper.readTree(json);
        JSContact[] jsContacts;
        if (jsonNode.isArray())
            jsContacts = mapper.treeToValue(jsonNode, JSContact[].class);
        else
            jsContacts = new JSContact[] { mapper.treeToValue(jsonNode, JSContact.class)};

        return convert(jsContacts);
    }

}

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
import it.cnr.iit.jscontact.tools.dto.VCardParamEnum;
import it.cnr.iit.jscontact.tools.dto.interfaces.HasContexts;
import it.cnr.iit.jscontact.tools.dto.interfaces.HasLabel;
import it.cnr.iit.jscontact.tools.dto.interfaces.VCardTypeDerivedEnum;
import it.cnr.iit.jscontact.tools.dto.utils.*;
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
 * Utility class for converting a Card object into a vCard 4.0 [RFC6350] instance represented as an Ezvcard VCard object.
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

    private void addX_ABLabel(HasLabel jsContactType, VCardProperty vcardProperty, VCard vcard) {

        if (jsContactType.getLabel()==null)
            return;

        String group = (vcardProperty.getGroup() != null) ? vcardProperty.getGroup() : "G-" + vcardProperty.getParameter(VCardParamEnum.PROP_ID.getValue());
        RawProperty rawProperty = new RawProperty(VCardPropEnum.X_ABLABEL.getValue(),jsContactType.getLabel(),VCardDataType.TEXT);
        rawProperty.setGroup(group);
        vcard.addProperty(rawProperty);
        if (vcardProperty.getGroup() == null) vcardProperty.setGroup(group);
    }

    private static String getVCardType(HasContexts o) {

        if (o.hasNoContext())
            return null;

        List<String> vCardTypeValues = toVCardTypeValues(ContextEnum.class, Context.toEnumValues(o.getContexts().keySet()));

        if (vCardTypeValues.isEmpty())
            return null;

        return String.join(DelimiterUtils.COMMA_ARRAY_DELIMITER, vCardTypeValues);
    }


    private void addPropId (VCardProperty vcardProperty, String propId) {

        if (propId != null && config.isSetPropIdParam())
            vcardProperty.addParameter(VCardParamEnum.PROP_ID.getValue(), propId);
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

    private static void fillMembers(VCard vcard, Card jsCard) {

        if (jsCard.getMembers() == null)
            return;

        for (String key : jsCard.getMembers().keySet())
            vcard.addMember(new Member(key));

    }


    private static String getNameComponent(NameComponent[] name, NameComponentEnum type) {

        for (NameComponent component : name)
            if (component.getType().getRfcValue()!=null &&  component.getType().getRfcValue() == type)
                return component.getValue();

        return null;
    }

    private static void addValuesToJoiner(StringJoiner joiner, NameComponent[] name, NameComponentEnum type) {

        String component = getNameComponent(name, type);
        joiner.add(component);
    }

    private static void addValuesToJoiner(StringJoiner joiner, List<String> values) {

        if (values != null && values.size() != 0 )
            joiner.add(String.join(DelimiterUtils.COMMA_ARRAY_DELIMITER, values));
    }

    private static void addValueToJoiner(StringJoiner joiner, String value) {

        if (value != null)
            joiner.add(value);
    }


    private static FormattedName getFormattedName(StructuredName sn, String separator) {

        StringJoiner joiner = new StringJoiner((separator!=null) ? separator : StringUtils.SPACE);
        addValuesToJoiner(joiner, sn.getPrefixes());
        addValueToJoiner(joiner, sn.getGiven());
        addValueToJoiner(joiner, sn.getFamily());
        addValuesToJoiner(joiner, sn.getAdditionalNames());
        addValuesToJoiner(joiner, sn.getSuffixes());
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
                    fn.setParameter(VCardParamEnum.DERIVED.getValue(), "true");
                    fn.setLanguage(jsCard.getLocale());
                    vcard.setFormattedName(fn);
                }
                else {
                    List<FormattedName> fns = new ArrayList<>();
                    for (StructuredName sn : sns) {
                        FormattedName fn = getFormattedName(sn, separator);
                        fn.setParameter(VCardParamEnum.DERIVED.getValue(), "true");
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

    private static String getRanksString(List<String> ranksList) {

        if (ranksList.isEmpty())
            return StringUtils.EMPTY;

        String result = "";
        for (int i=0; i < ranksList.size(); i++) {
            boolean notEmptyFound = false;
            for (int j=i; j < ranksList.size(); j++) {
                if (!ranksList.get(j).isEmpty()) {
                    notEmptyFound = true;
                    break;
                }
            }
            if (!ranksList.get(i).isEmpty())
            result = result + ((!ranksList.get(i).isEmpty()) ? ranksList.get(i) + DelimiterUtils.COMMA_ARRAY_DELIMITER : DelimiterUtils.COMMA_ARRAY_DELIMITER);
            if (!notEmptyFound) break;
        }

        return (result.isEmpty()) ? StringUtils.EMPTY : result.substring(0, result.length() - 1);
    }


    private static StructuredName getStructuredName(NameComponent[] nameComponents) {

        StructuredName name = new StructuredName();
        List<String> surnames = new ArrayList<>();
        List<String> givens = new ArrayList<>();
        List<String> surnamesRanksList=new ArrayList<>();
        List<String> givensRanksList=new ArrayList<>();
        List<String> middlesRanksList=new ArrayList<>();
        List<String> prefixesRanksList=new ArrayList<>();
        List<String> suffixesRanksList=new ArrayList<>();
        for (NameComponent component : nameComponents) {
            if (component.getType().getRfcValue() == null)
                continue;
            switch(component.getType().getRfcValue()) {
                case SURNAME:
                    surnames.add(component.getValue());
                    surnamesRanksList.add((component.getRank()!=null) ? component.getRank().toString() : StringUtils.EMPTY);
                    break;
                case GIVEN:
                    givens.add(component.getValue());
                    givensRanksList.add((component.getRank()!=null) ? component.getRank().toString() : StringUtils.EMPTY);
                    break;
                case MIDDLE:
                    name.getAdditionalNames().add(component.getValue());
                    middlesRanksList.add((component.getRank()!=null) ? component.getRank().toString() : StringUtils.EMPTY);
                    break;
                case PREFIX:
                    name.getPrefixes().add(component.getValue());
                    prefixesRanksList.add((component.getRank()!=null) ? component.getRank().toString() : StringUtils.EMPTY);
                    break;
                case SUFFIX:
                    name.getSuffixes().add(component.getValue());
                    suffixesRanksList.add((component.getRank()!=null) ? component.getRank().toString() : StringUtils.EMPTY);
                    break;
            }
        }
        name.setFamily((surnames.size()>0) ? String.join(DelimiterUtils.COMMA_ARRAY_DELIMITER,surnames) : null);
        name.setGiven((givens.size()>0) ? String.join(DelimiterUtils.COMMA_ARRAY_DELIMITER,givens) : null);
        String surnamesRanks = getRanksString(surnamesRanksList);
        String givensRanks = getRanksString(givensRanksList);
        String middlesRanks = getRanksString(middlesRanksList);
        String prefixesRanks = getRanksString(prefixesRanksList);
        String suffixesRanks = getRanksString(suffixesRanksList);
        if (!(suffixesRanks+givensRanks+middlesRanks+prefixesRanks+suffixesRanks).isEmpty()) {
            StringJoiner joiner = new StringJoiner(DelimiterUtils.SEMICOLON_ARRAY_DELIMITER);
            addValueToJoiner(joiner,surnamesRanks);
            if (!(givensRanks+middlesRanks+prefixesRanks+suffixesRanks).isEmpty()) {
                addValueToJoiner(joiner, givensRanks);
                if (!(middlesRanks+prefixesRanks+suffixesRanks).isEmpty()) {
                    addValueToJoiner(joiner, middlesRanks);
                    if (!(prefixesRanks+suffixesRanks).isEmpty()) {
                        addValueToJoiner(joiner, prefixesRanks);
                        if (!(prefixesRanks+suffixesRanks).isEmpty())
                            addValueToJoiner(joiner, suffixesRanks);
                    }
                }
            }
            name.setParameter(VCardParamEnum.RANKS.getValue(), joiner.toString());
        }

        return name;
    }

    private static String getNameSortAs(Map<String,String> jsContactSortAs) {

        if (jsContactSortAs == null)
            return null;

        return String.join(DelimiterUtils.COMMA_ARRAY_DELIMITER,jsContactSortAs.values());
    }

    private static List<StructuredName> getStructuredNames(Card jsCard) {

        List<StructuredName> sns = new ArrayList<>();
        if (jsCard.getLocalizationsPerPath("name") != null) {
            StructuredName sn = getStructuredName(jsCard.getName().getComponents());
            sn.setLanguage(jsCard.getLocale());
            sn.setParameter(VCardParamEnum.SORT_AS.getValue(), getNameSortAs(jsCard.getName().getSortAs())); // did this way because Ez-vcard allows to sort only for surname and given name
            VCardUtils.addVCardUnmatchedParams(sn,jsCard.getName());
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
            sn.setParameter(VCardParamEnum.SORT_AS.getValue(),getNameSortAs(jsCard.getName().getSortAs()));
            VCardUtils.addVCardUnmatchedParams(sn,jsCard.getName());
            sns.add(sn);
            for (Map.Entry<String, JsonNode> localizations : jsCard.getLocalizationsPerPath("name/components").entrySet()) {
                sn = getStructuredName(asNameComponentArray(localizations.getValue()));
                sn.setLanguage(localizations.getKey());
                sns.add(sn);
            }
        }
        else {
            StructuredName sn = getStructuredName(jsCard.getName().getComponents());
            sn.setParameter(VCardParamEnum.SORT_AS.getValue(),getNameSortAs(jsCard.getName().getSortAs()));
            VCardUtils.addVCardUnmatchedParams(sn,jsCard.getName());
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
        VCardUtils.addVCardUnmatchedParams(nickname,nickName);
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
                    jsCard.getLocalizationsPerPath("nickNames/"+entry.getKey()+"/name")==null) {
                Nickname nickname = getNickname(entry.getValue(), jsCard.getLocale(), entry.getKey());
                addX_ABLabel(entry.getValue(),nickname, vcard);
                vcard.addNickname(nickname);
            }
            else {
                List<ezvcard.property.Nickname> nicknames = new ArrayList<>();
                Nickname nickname = getNickname(entry.getValue(), jsCard.getLocale(), entry.getKey());
                addX_ABLabel(entry.getValue(),nickname, vcard);
                nicknames.add(nickname);

                Map<String,JsonNode> localizations = jsCard.getLocalizationsPerPath("nickNames/"+entry.getKey());
                if (localizations != null) {
                    for (Map.Entry<String, JsonNode> localization : localizations.entrySet()) {
                        nicknames.add(asNickname(localization.getValue(), localization.getKey(), entry.getKey()));
                    }
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

        String offset = StringUtils.EMPTY;
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
        VCardUtils.addVCardUnmatchedParams(addr,address);

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
    private static Note asNote(JsonNode jsonNode) {
        return (Note) asObject(jsonNode, Note.class);
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
                        VCardUtils.addVCardUnmatchedParams(vcard.getBirthday(),anniversary);
                        addPropId(vcard.getBirthday(), entry.getKey());
                        vcard.setBirthplace(getPlaceProperty(Birthplace.class, anniversary));
                        break;
                    case DEATH:
                        vcard.setDeathdate(getDateOrTimeProperty(Deathdate.class, anniversary));
                        VCardUtils.addVCardUnmatchedParams(vcard.getDeathdate(),anniversary);
                        addPropId(vcard.getDeathdate(), entry.getKey());
                        vcard.setDeathplace(getPlaceProperty(Deathplace.class, anniversary));
                        break;
                    case WEDDING:
                        vcard.setAnniversary(getDateOrTimeProperty(ezvcard.property.Anniversary.class, anniversary));
                        VCardUtils.addVCardUnmatchedParams(vcard.getAnniversary(),anniversary);
                        addPropId(vcard.getAnniversary(), entry.getKey());
                        break;
                }
            }
        }

    }

    private Expertise getExpertise(PersonalInformation pi) {

        Expertise e = new Expertise(pi.getValue());
        VCardUtils.addVCardUnmatchedParams(e,pi);
        addPropId(e, pi.getPropId());
        if (pi.getLevel()!= null && pi.getLevel().isRfcValue())
            e.setLevel(ExpertiseLevel.get(PersonalInformationLevelEnum.getVCardExpertiseLevel(pi.getLevel().getRfcValue())));
        else
            e.setParameter(VCardParamEnum.LEVEL.getValue(), pi.getLevel().getExtValue().toString().toUpperCase());
        return e;
    }

    private Hobby getHobby(PersonalInformation pi) {

        Hobby h = new Hobby(pi.getValue());
        VCardUtils.addVCardUnmatchedParams(h,pi);
        addPropId(h, pi.getPropId());
        if (pi.getLevel()!= null && pi.getLevel().isRfcValue())
            h.setLevel(HobbyLevel.get(pi.getLevel().getRfcValue().name()));
        else
            h.setParameter(VCardParamEnum.LEVEL.getValue(), pi.getLevel().getExtValue().toString().toUpperCase());
        return h;
    }

    private Interest getInterest(PersonalInformation pi) {

        Interest i = new Interest(pi.getValue());
        VCardUtils.addVCardUnmatchedParams(i,pi);
        addPropId(i, pi.getPropId());
        if (pi.getLevel()!= null && pi.getLevel().isRfcValue())
            i.setLevel(InterestLevel.get(pi.getLevel().getRfcValue().name()));
        else
            i.setParameter(VCardParamEnum.LEVEL.getValue(), pi.getLevel().getExtValue().toString().toUpperCase());
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
                        Expertise e = getExpertise(pi);
                        addX_ABLabel(pi,e,vcard);
                        vcard.getExpertise().add(e);
                        break;
                    case HOBBY:
                        Hobby h = getHobby(pi);
                        addX_ABLabel(pi,h,vcard);
                        vcard.getHobbies().add(h);
                        break;
                    case INTEREST:
                        Interest i = getInterest(pi);
                        addX_ABLabel(pi,i,vcard);
                        vcard.getInterests().add(i);
                        break;
                }
            }
        }
    }

    private static Language getLanguage(String lang, LanguagePreference cl) {

        Language language = new Language(lang);
        String vCardTypeValue = getVCardType(cl);
        if (vCardTypeValue!=null)
            language.setParameter(VCardParamEnum.TYPE.getValue(), vCardTypeValue);
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
            tel = new Telephone(TelUri.parse(phone.getNumber()));
        } catch(Exception e) {
            tel = new Telephone(phone.getNumber());
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
        VCardUtils.addVCardUnmatchedParams(tel,phone);

        return tel;
    }

    private void fillPhones(VCard vcard, Card jsCard) {

        if (jsCard.getPhones() == null)
            return;

        for (Map.Entry<String, Phone> entry : jsCard.getPhones().entrySet()) {
            Phone phone = entry.getValue();
            phone.setPropId(entry.getKey());
            Telephone vcardPhone = getTelephone(phone);
            addX_ABLabel(phone,vcardPhone,vcard);
            vcard.getTelephoneNumbers().add(vcardPhone);
        }
    }

    private Email getEmail(EmailAddress emailAddress) {

        Email email = new Email(emailAddress.getAddress());
        email.setPref(emailAddress.getPref());
        addPropId(email, emailAddress.getPropId());
        if (!emailAddress.hasNoContext()) {
            List<String> vCardTypeValues = toVCardTypeValues(ContextEnum.class, Context.toEnumValues(emailAddress.getContexts().keySet()));
            for (String vCardTypeValue : vCardTypeValues)
                email.getTypes().add(EmailType.get(vCardTypeValue));
        }
        VCardUtils.addVCardUnmatchedParams(email,emailAddress);
        return email;
    }

    private void fillEmails(VCard vcard, Card jsCard) {

        if (jsCard.getEmails() == null)
            return;

        for (Map.Entry<String,EmailAddress> entry : jsCard.getEmails().entrySet()) {
            EmailAddress email = entry.getValue();
            email.setPropId(entry.getKey());
            Email vcardEmail = getEmail(email);
            addX_ABLabel(email,vcardEmail,vcard);
            vcard.getEmails().add(vcardEmail);
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
        VCardUtils.addVCardUnmatchedParams(photo,resource);
        return photo;
    }

    private static Impp getImpp(OnlineService onlineService) {

        Impp impp = new Impp(onlineService.getUser());
        impp.setPref(onlineService.getPref());
        if (!onlineService.hasNoContext()) {
            List<String> vCardTypeValues = toVCardTypeValues(ContextEnum.class, Context.toEnumValues(onlineService.getContexts().keySet()));
            for (String vCardTypeValue : vCardTypeValues)
                impp.getTypes().add(ImppType.get(vCardTypeValue));
        }
        VCardUtils.addVCardUnmatchedParams(impp,onlineService);

        return impp;
    }

    private static RawProperty getSocialService(OnlineService onlineService) {

        RawProperty raw = new RawProperty(VCardPropEnum.SOCIALSERVICE.getValue(), onlineService.getUser());
        String vCardTypeValue = getVCardType(onlineService);
        if (vCardTypeValue!=null)
            raw.setParameter(VCardParamEnum.TYPE.getValue(), vCardTypeValue);
        if (onlineService.getPref()!=null)
            raw.setParameter(VCardParamEnum.PREF.getValue(), onlineService.getPref().toString());
        if (onlineService.getService()!=null)
            raw.setParameter(VCardParamEnum.SERVICE_TYPE.getValue(), onlineService.getService());
        raw.setDataType((onlineService.getType().isUri()) ? VCardDataType.URI : VCardDataType.TEXT);
        VCardUtils.addVCardUnmatchedParams(raw,onlineService);

        return raw;
    }

    private static <T extends VCardProperty> void fillVCardProperty(T property, Resource resource) {

        if (resource.getMediaType()!=null)
            property.setParameter(VCardParamEnum.MEDIATYPE.getValue(),resource.getMediaType());
        if (resource.getPref() != null)
            property.setParameter(VCardParamEnum.PREF.getValue(), resource.getPref().toString());
        String vCardTypeValue = getVCardType(resource);
        if (vCardTypeValue!=null)
            property.setParameter(VCardParamEnum.TYPE.getValue(), vCardTypeValue);
    }

    private <T extends UriProperty> T getUriProperty(Class<T> classs, Resource resource, VCard vcard) {

        try {
            Constructor<T> constructor = classs.getDeclaredConstructor(String.class);
            T object = constructor.newInstance(resource.getUri());
            fillVCardProperty(object,resource);
            VCardUtils.addVCardUnmatchedParams(object,resource);
            addPropId(object, resource.getPropId());
            addX_ABLabel(resource, object, vcard);
            return object;
        } catch (Exception e) {
            throw new InternalErrorException(e.getMessage());
        }
    }

    private <T extends BinaryProperty> T getBinaryProperty(Class<T> classs, Resource resource, VCard vcard) {

        try {
            ImageType it = getImageType(resource.getMediaType());
            Constructor<T> constructor = classs.getDeclaredConstructor(String.class, ImageType.class);
            T object = constructor.newInstance(resource.getUri(), it);
            fillVCardProperty(object,resource);
            VCardUtils.addVCardUnmatchedParams(object,resource);
            addPropId(object, resource.getPropId());
            addX_ABLabel(resource, object, vcard);
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
            if (onlineService.getType()!=null && onlineService.getType().isRfcValue()) {
                switch (onlineService.getType().getRfcValue()) {
                    case IMPP:
                        Impp impp = getImpp(onlineService);
                        addPropId(impp, entry.getKey());
                        addX_ABLabel(onlineService, impp,vcard);
                        vcard.getImpps().add(impp);
                        break;
                    case URI:
                    case USERNAME:
                        RawProperty rawProperty = getSocialService(onlineService);
                        addPropId(rawProperty, entry.getKey());
                        addX_ABLabel(onlineService, rawProperty, vcard);
                        vcard.addProperty(rawProperty);
                        break;
                }
            }
        }
    }

    private CalendarRequestUri getCaladruri(SchedulingAddress s) {

        CalendarRequestUri caladruri = new CalendarRequestUri(s.getUri());
        caladruri.setPref(s.getPref());
        addPropId(caladruri, s.getPropId());
        if (!s.hasNoContext()) {
            String vCardTypeValue = getVCardType(s);
            if (vCardTypeValue!=null)
                caladruri.setParameter(VCardParamEnum.TYPE.getValue(), vCardTypeValue);
        }
        VCardUtils.addVCardUnmatchedParams(caladruri,s);
        return caladruri;
    }


    private void fillSchedulingAddresses(VCard vcard, Card jsCard) {

        if (jsCard.getSchedulingAddresses() == null)
            return;

        for(Map.Entry<String, SchedulingAddress> entry : jsCard.getSchedulingAddresses().entrySet()) {
            SchedulingAddress s = entry.getValue();
            s.setPropId(entry.getKey());
            CalendarRequestUri vcardCaladruri = getCaladruri(s);
            VCardUtils.addVCardUnmatchedParams(vcardCaladruri,s);
            addX_ABLabel(s,vcardCaladruri,vcard);
            vcard.getCalendarRequestUris().add(vcardCaladruri);
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
                        vcard.getFbUrls().add(getUriProperty(FreeBusyUrl.class, resource, vcard));
                        break;
                    case CALENDAR:
                        vcard.getCalendarUris().add(getUriProperty(CalendarUri.class, resource, vcard));
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
            VCardUtils.addVCardUnmatchedParams(key,entry.getValue());
            addPropId(key, entry.getKey());
            addX_ABLabel(entry.getValue(),key,vcard);
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
                vcard.getUrls().add(getUriProperty(Url.class,resource,vcard));
            else {
                if (resource.getType()!=null && resource.getType().isRfcValue()) {
                    switch (resource.getType().getRfcValue()) {
                        case CONTACT:
                            RawProperty rp = new RawProperty("CONTACT-URI", resource.getUri());
                            fillVCardProperty(rp, resource);
                            VCardUtils.addVCardUnmatchedParams(rp,resource);
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
                        VCardUtils.addVCardUnmatchedParams(sound,resource);
                        addPropId(sound, resource.getPropId());
                        addX_ABLabel(resource, sound, vcard);
                        vcard.getSounds().add(sound);
                        break;
                    case LOGO:
                        vcard.getLogos().add(getBinaryProperty(Logo.class, resource, vcard));
                        break;
                    case PHOTO:
                        Photo photo = getPhoto(resource);
                        if (photo == null) continue;
                        VCardUtils.addVCardUnmatchedParams(photo,resource);
                        addPropId(photo, entry.getKey());
                        addX_ABLabel(resource, photo, vcard);
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
                        vcard.getSources().add(getUriProperty(Source.class, resource, vcard));
                        break;
                    case DIRECTORY:
                        vcard.getOrgDirectories().add(getUriProperty(OrgDirectory.class, resource, vcard));
                        break;
                }
            }
        }
    }

    private <E extends TextProperty > E getTextProperty(E property, String language, String propId, Map<String, VCardParam> unmatchedParams) {

        if (language != null) property.getParameters().setLanguage(language);
        VCardUtils.addVCardUnmatchedParams(property,unmatchedParams);
        addPropId(property, propId);
        return property;
    }

    private <E extends TextListProperty> E getTextListProperty(E property, List<String> textList, String language, String propId, Map<String, VCardParam> unmatchedParams) {

        property.getValues().addAll(textList);
        VCardUtils.addVCardUnmatchedParams(property,unmatchedParams);
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
                    vcard.addTitle(getTextProperty(new ezvcard.property.Title(entry.getValue().getName()), jsCard.getLocale(), entry.getKey(), entry.getValue().getVCardParams()));
                else
                    vcard.addRole(getTextProperty(new ezvcard.property.Role(entry.getValue().getName()), jsCard.getLocale(), entry.getKey(), entry.getValue().getVCardParams()));

            else {
                List<ezvcard.property.Title> titles = new ArrayList<>();
                List<ezvcard.property.Role> roles = new ArrayList<>();

                if (entry.getValue().getType() == null || entry.getValue().getType().isTitle())
                    titles.add(getTextProperty(new ezvcard.property.Title(entry.getValue().getName()), jsCard.getLocale(), entry.getKey(), entry.getValue().getVCardParams()));
                else
                    roles.add(getTextProperty(new ezvcard.property.Role(entry.getValue().getName()), jsCard.getLocale(), entry.getKey(), entry.getValue().getVCardParams()));


                Map<String,JsonNode> localizations = jsCard.getLocalizationsPerPath("titles/"+entry.getKey());
                if (localizations != null) {
                    for (Map.Entry<String, JsonNode> localization : localizations.entrySet()) {
                        if (jsCard.getTitles().get(entry.getKey()).getType() == null || jsCard.getTitles().get(entry.getKey()).getType().isTitle())
                            titles.add(getTextProperty(new ezvcard.property.Title(localization.getValue().get("name").asText()), localization.getKey(), entry.getKey(), null));
                        else
                            roles.add(getTextProperty(new ezvcard.property.Role(localization.getValue().get("name").asText()), localization.getKey(), entry.getKey(), null));
                    }
                }
                localizations = jsCard.getLocalizationsPerPath("titles/"+entry.getKey()+"/name");
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
        organizationItems.add((organization!=null) ? organization : StringUtils.EMPTY);
        if (units != null)
            organizationItems.addAll(Arrays.asList(units));
        return organizationItems;
    }

    private void fillOrganizations(VCard vcard, Card jsCard) {

        if (jsCard.getOrganizations() == null)
            return;

        for (Map.Entry<String,Organization> entry : jsCard.getOrganizations().entrySet()) {

            if (jsCard.getLocalizationsPerPath("organizations/"+entry.getKey()) == null &&
                jsCard.getLocalizationsPerPath("organizations/"+entry.getKey()+"/name")==null) {
                ezvcard.property.Organization org = getTextListProperty(new ezvcard.property.Organization(), getOrganizationItems(entry.getValue().getName(), entry.getValue().getUnits()), jsCard.getLocale(), entry.getKey(), entry.getValue().getVCardParams());
                if (entry.getValue().getSortAs()!=null)
                    org.setSortAs(String.join(DelimiterUtils.COMMA_ARRAY_DELIMITER, entry.getValue().getSortAs()));
                vcard.getOrganizations().add(org);
            }
            else {
                List<ezvcard.property.Organization> organizations = new ArrayList<>();
                ezvcard.property.Organization org = getTextListProperty(new ezvcard.property.Organization(), getOrganizationItems(entry.getValue().getName(), entry.getValue().getUnits()), jsCard.getLocale(), entry.getKey(), entry.getValue().getVCardParams());
                if (entry.getValue().getSortAs()!=null)
                    org.setSortAs(String.join(DelimiterUtils.COMMA_ARRAY_DELIMITER, entry.getValue().getSortAs()));
                organizations.add(org);

                Map<String,JsonNode> localizations = jsCard.getLocalizationsPerPath("organizations/"+entry.getKey());
                if (localizations != null) {
                    for (Map.Entry<String, JsonNode> localization : localizations.entrySet()) {
                        org = getTextListProperty(new ezvcard.property.Organization(), getOrganizationItems((localization.getValue().get("name") != null) ? localization.getValue().get("name").asText() : null, JsonNodeUtils.asTextArray(localization.getValue().get("units"))), localization.getKey(), entry.getKey(), null);
                        if (localization.getValue().get("sortAs") != null)
                            org.setSortAs(localization.getValue().get("sortAs").asText());
                        organizations.add(org);
                    }
                }
                localizations = jsCard.getLocalizationsPerPath("organizations/"+entry.getKey()+"/name");
                if (localizations != null) {
                    for (Map.Entry<String,JsonNode> localization : localizations.entrySet()) {
                        JsonNode units = jsCard.getLocalization(localization.getKey(),"organizations/"+entry.getKey()+"/units");
                        org = getTextListProperty(new ezvcard.property.Organization(), getOrganizationItems((localization.getValue().get("name")!=null) ? localization.getValue().get("name").asText() : null, JsonNodeUtils.asTextArray(units)), localization.getKey(), entry.getKey(), null);
                        if (localization.getValue().get("sortAs") != null)
                            org.setSortAs(localization.getValue().get("sortAs").asText());
                        organizations.add(org);
                    }
                }
                vcard.addOrganizationAlt(organizations.toArray(new ezvcard.property.Organization[0]));
            }
        }
    }


    private static ezvcard.property.Note getNote(Note jsNote) {

        ezvcard.property.Note note = new ezvcard.property.Note(jsNote.getNote());
        if (jsNote.getAuthor()!=null && jsNote.getAuthor().getUri()!=null)
            note.setParameter(VCardParamEnum.AUTHOR.getValue(), jsNote.getAuthor().getUri());
        if (jsNote.getAuthor()!=null && jsNote.getAuthor().getName()!=null)
            note.setParameter(VCardParamEnum.AUTHOR_NAME.getValue(), jsNote.getAuthor().getName());
        if (jsNote.getCreated()!=null)
            note.setParameter(VCardParamEnum.CREATED.getValue(), DateUtils.toString(jsNote.getCreated()));
        VCardUtils.addVCardUnmatchedParams(note, jsNote);
        return note;
    }


    private void fillNotes(VCard vcard, Card jsCard) {

        if (jsCard.getNotes() == null)
            return;

        for (Map.Entry<String,Note> entry : jsCard.getNotes().entrySet()) {

            if (jsCard.getLocalizationsPerPath("notes/"+entry.getKey()) == null &&
                    jsCard.getLocalizationsPerPath("notes/"+entry.getKey()+"/note")==null) {
                ezvcard.property.Note note = getNote(entry.getValue());
                addPropId(note, entry.getKey());
                vcard.addNote(note);
            }
            else {
                List<ezvcard.property.Note> notes = new ArrayList<>();
                ezvcard.property.Note note = getNote(entry.getValue());
                addPropId(note, entry.getKey());
                notes.add(note);

                Map<String,JsonNode> localizations = jsCard.getLocalizationsPerPath("notes/"+entry.getKey());
                if (localizations != null) {
                    for (Map.Entry<String, JsonNode> localization : localizations.entrySet()) {
                        note = getNote(asNote(localization.getValue()));
                        note.setLanguage(localization.getKey());
                        notes.add(note);
                    }
                }
                localizations = jsCard.getLocalizationsPerPath("notes/"+entry.getKey()+"/note");
                if (localizations != null) {
                    for (Map.Entry<String,JsonNode> localization : localizations.entrySet()) {
                        note = new ezvcard.property.Note(localization.getValue().asText());
                        note.setLanguage(localization.getKey());
                        notes.add(note);
                    }
                }
                vcard.addNoteAlt(notes.toArray(new ezvcard.property.Note[0]));
            }
        }
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

        if (jsCard.getVCardProps() == null)
            return;

        for (VCardProp vCardProp : jsCard.getVCardProps()) {

            if (vCardProp.getName().equals(V_Extension.toV_Extension(VCardPropEnum.CLIENTPIDMAP.getValue()))) {
                String pid = ((String) vCardProp.getValue()).split(",")[0];
                String uri = ((String) vCardProp.getValue()).split(",")[1];
                ClientPidMap pidmap = getCliendPidMap(pid, uri);
                pidmap.setParameters(vCardProp.getVCardParameters());
                vcard.addClientPidMap(pidmap);
            }
            else if (vCardProp.getName().equals(V_Extension.toV_Extension(VCardPropEnum.XML.getValue()))) {
                try {
                    Xml xml = new Xml(((String) vCardProp.getValue()));
                    xml.setParameters(vCardProp.getVCardParameters());
                    vcard.getXmls().add(xml);
                } catch (Exception e) {
                    throw new InternalErrorException(e.getMessage());
                }
            }
            else if (vCardProp.getName().equals(V_Extension.toV_Extension(VCardPropEnum.TZ.getValue()))) {

                Timezone tz = null;
                TimeZone timeZone = null;
                if (jsCard.getCustomTimeZones() != null)
                    timeZone = jsCard.getCustomTimeZones().get((String) vCardProp.getValue());
                if (timeZone != null) {
                    if (timeZone.getStandard() != null && timeZone.getStandard().size() > 0)
                        tz = new Timezone(UtcOffset.parse((timeZone.getStandard().get(0).getOffsetFrom())));
                } else
                    tz = getTimezone((String) vCardProp.getValue());
                tz.setParameters(vCardProp.getVCardParameters());
                vcard.setTimezone(tz);
            }
            else if (vCardProp.getName().equals(V_Extension.toV_Extension(VCardPropEnum.GEO.getValue()))) {
                Geo geo = new Geo(getGeoUri((String) vCardProp.getValue()));
                geo.setParameters(vCardProp.getVCardParameters());
                vcard.setGeo(geo);
            }
            else {
                RawProperty property = new RawProperty(vCardProp.getName().toString().toUpperCase(), vCardProp.getValue().toString(), vCardProp.getType());
                property.setParameters(vCardProp.getVCardParameters());
                vcard.addProperty(property);
            }
        }
    }

    //TODO: replace XXXX with RFC number after draft-ietf-calext-vcard-jscontact-extensions
    private void fillRFCXXXXProperties(VCard vCard, Card jsCard) {

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
                    raw.setParameter(VCardParamEnum.TYPE.getValue(), vCardTypeValue);
                if (entry.getValue().getPref()!=null)
                    raw.setParameter(VCardParamEnum.PREF.getValue(), entry.getValue().getPref().toString());
                raw.setDataType(VCardDataType.TEXT);
                addX_ABLabel(entry.getValue(), raw, vCard);
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
                            raw.setParameter(VCardParamEnum.TYPE.getValue(), vCardTypeValue);
                        if (pref.getPref()!=null)
                            raw.setParameter(VCardParamEnum.PREF.getValue(), pref.getPref().toString());
                        raw.setDataType(VCardDataType.TEXT);
                        vCard.addProperty(raw);
                    }
                }
            }
        }
    }

    private void fillJSContactExtensions(VCard vcard, Card jsCard) {

        Map<String,Object> allExtensionsMap = new HashMap<String,Object>();
        jsCard.buildAllExtensionsMap(allExtensionsMap,StringUtils.EMPTY);

        for(Map.Entry<String,Object> entry : allExtensionsMap.entrySet()) {
            try {
                RawProperty property = new RawProperty(VCardPropEnum.JSCONTACT_PROP.getValue(), JSContactPropUtils.toX_RFC0000_JSPROPValue(entry.getValue()), VCardDataType.TEXT);
                property.setParameter(VCardParamEnum.JSPTR.getValue(), entry.getKey());
                vcard.addProperty(property);
            } catch (Exception e) {}
        }
    }

    /**
     * Converts a Card object into a basic vCard v4.0 [RFC6350].
     * JSContact objects are defined in draft-ietf-calext-jscontact.
     * Conversion rules are defined in draft-ietf-calext-jscontact-vcard.
     *
     * @param jsCard a Card object (Card or CardGroup)
     * @return a vCard as an instance of the ez-vcard library VCard class
     * @see <a href="https://github.com/mangstadt/ez-vcard">ez-vcard library</a>
     * @see <a href="https://datatracker.ietf.org/doc/draft-ietf-calext-jscontact-vcard/">draft-ietf-calext-jscontact-vcard</a>
     * @see <a href="https://datatracker.ietf.org/doc/draft-ietf-calext-jscontact/">draft-ietf-calext-jscontact</a>
     */
    protected VCard convert(Card jsCard) {

        if (jsCard == null)
            return null;

        VCard vCard = new VCard(VCardVersion.V4_0);
        vCard.setUid(getUid(jsCard.getUid()));
        vCard.setKind(getKind(jsCard.getKind()));
        vCard.setProductId(jsCard.getProdId());
        vCard.setRevision(getRevision(jsCard.getUpdated()));
        fillMembers(vCard, jsCard);
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
     * Converts a list of Card objects into a list of vCard v4.0 instances [RFC6350].
     * JSContact is defined in draft-ietf-calext-jscontact.
     * Conversion rules are defined in draft-ietf-calext-jscontact-vcard.
     * @param jsCards a list of Card objects
     * @return a list of instances of the ez-vcard library VCard class
     * @throws CardException if one of Card objects is not valid
     * @see <a href="https://github.com/mangstadt/ez-vcard">ez-vcard library</a>
     * @see <a href="https://datatracker.ietf.org/doc/draft-ietf-calext-jscontact-vcard/">draft-ietf-calext-jscontact-vcard</a>
     * @see <a href="https://datatracker.ietf.org/doc/draft-ietf-calext-jscontact/">draft-ietf-calext-jscontact</a>
     */
    public List<VCard> convert(Card... jsCards) throws CardException {

        List<VCard> vCards = new ArrayList<>();

        for (Card jsCard : jsCards) {
            if (config.isSetCardMustBeValidated()) {
                if (!jsCard.isValid())
                    throw new CardException(jsCard.getValidationMessage());
            }
            vCards.add(convert(jsCard));
        }

        return vCards;
    }


    /**
     * Converts a JSON array of Card objects into a list of vCard v4.0 instances [RFC6350].
     * JSContact is defined in draft-ietf-calext-jscontact.
     * Conversion rules are defined in draft-ietf-calext-jscontact-vcard.
     * @param json a JSON array of Card objects
     * @return a list of instances of the ez-vcard library VCard class
     * @throws CardException if one of Card objects is not valid
     * @throws JsonProcessingException if json cannot be processed
     * @see <a href="https://github.com/mangstadt/ez-vcard">ez-vcard library</a>
     * @see <a href="https://datatracker.ietf.org/doc/draft-ietf-calext-jscontact-vcard/">draft-ietf-calext-jscontact-vcard</a>
     * @see <a href="https://datatracker.ietf.org/doc/draft-ietf-calext-jscontact/">draft-ietf-calext-jscontact</a>
     */
    public List<VCard> convert(String json) throws CardException, JsonProcessingException {

        SimpleModule module = new SimpleModule();
        mapper.registerModule(module);
        JsonNode jsonNode = mapper.readTree(json);
        Card[] jsCards;
        if (jsonNode.isArray())
            jsCards = mapper.treeToValue(jsonNode, Card[].class);
        else
            jsCards = new Card[] { mapper.treeToValue(jsonNode, Card.class)};

        return convert(jsCards);
    }

}

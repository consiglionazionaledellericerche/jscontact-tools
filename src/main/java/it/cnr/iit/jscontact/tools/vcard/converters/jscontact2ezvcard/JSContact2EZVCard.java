package it.cnr.iit.jscontact.tools.vcard.converters.jscontact2ezvcard;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.fasterxml.jackson.databind.node.ArrayNode;
import ezvcard.VCard;
import ezvcard.VCardDataType;
import ezvcard.VCardVersion;
import ezvcard.parameter.*;
import ezvcard.property.*;
import ezvcard.util.*;
import ezvcard.util.PartialDate;
import ezvcard.util.org.apache.commons.codec.binary.Base64;
import it.cnr.iit.jscontact.tools.dto.*;
import it.cnr.iit.jscontact.tools.dto.Address;
import it.cnr.iit.jscontact.tools.dto.Anniversary;
import it.cnr.iit.jscontact.tools.dto.Nickname;
import it.cnr.iit.jscontact.tools.dto.Note;
import it.cnr.iit.jscontact.tools.dto.Organization;
import it.cnr.iit.jscontact.tools.dto.TimeZone;
import it.cnr.iit.jscontact.tools.dto.Title;
import it.cnr.iit.jscontact.tools.dto.interfaces.HasContexts;
import it.cnr.iit.jscontact.tools.dto.interfaces.HasLabel;
import it.cnr.iit.jscontact.tools.dto.interfaces.VCardTypeDerivedEnum;
import it.cnr.iit.jscontact.tools.dto.utils.*;
import it.cnr.iit.jscontact.tools.exceptions.CardException;
import it.cnr.iit.jscontact.tools.exceptions.InternalErrorException;
import it.cnr.iit.jscontact.tools.vcard.converters.AbstractConverter;
import it.cnr.iit.jscontact.tools.vcard.converters.config.JSContact2VCardConfig;
import it.cnr.iit.jscontact.tools.vcard.extensions.property.ExtendedAddress;
import it.cnr.iit.jscontact.tools.vcard.extensions.property.ExtendedStructuredName;
import lombok.NoArgsConstructor;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;

import java.lang.reflect.Constructor;
import java.net.URI;
import java.nio.charset.StandardCharsets;
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

    protected JSContact2VCardConfig config;

    private void addVCardX_ABLabel(HasLabel jsContactType, VCardProperty vcardProperty, VCard vcard) {

        if (jsContactType.getLabel()==null)
            return;

        String group = (vcardProperty.getGroup() != null) ? vcardProperty.getGroup() : "G-" + vcardProperty.getParameter(VCardParamEnum.PROP_ID.getValue());
        RawProperty rawProperty = new RawProperty(VCardPropEnum.X_ABLABEL.getValue(),jsContactType.getLabel(),VCardDataType.TEXT);
        rawProperty.setGroup(group);
        vcard.addProperty(rawProperty);
        if (vcardProperty.getGroup() == null) vcardProperty.setGroup(group);
    }

    private static String toVCardTypeParam(HasContexts o) {

        if (o.hasNoContext())
            return null;

        List<String> vCardTypeValues = toVCardTypeParmaValues(ContextEnum.class, Context.toEnumValues(o.getContexts().keySet()));

        if (vCardTypeValues.isEmpty())
            return null;

        return String.join(DelimiterUtils.COMMA_ARRAY_DELIMITER, vCardTypeValues);
    }

    private void addVCardPropIdParam(VCardProperty vcardProperty, String propId) {

        if (propId != null && config.isSetPropIdParam())
            vcardProperty.addParameter(VCardParamEnum.PROP_ID.getValue(), propId);
    }

    private static Kind toVCardKind(KindType kind) {

        if (kind == null)
            return null;

        if (kind.getExtValue() != null)
            return new Kind(kind.getExtValue().toString());

        return new Kind(kind.getRfcValue().getValue());
    }

    private static Uid toVCardUid(String uid) {

        return (uid!=null) ? new Uid(uid) : null;
    }

    private static Revision toVCardRevision(Calendar update) {

        return (update!=null) ? new Revision(update) : null;
    }

    private static void fillVCardMembers(VCard vcard, Card jsCard) {

        if (jsCard.getMembers() == null)
            return;

        for (String key : jsCard.getMembers().keySet())
            vcard.addMember(new Member(key));

    }

    private static FormattedName toVCardFormattedName(NameComponent[] nameComponents, Boolean isOrdered, String defaultSeparator) {

        List<String> components = new ArrayList<>();
        String separator = (defaultSeparator != null) ? defaultSeparator : DelimiterUtils.SPACE_DELIMITER;
        boolean applySeparator = (isOrdered == Boolean.TRUE);
        for (NameComponent pair : nameComponents) {
            if (pair.getKind().isRfcValue()) {
                switch (pair.getKind().getRfcValue()) {
                    case SEPARATOR:
                        if (applySeparator)
                            components.set(components.size() - 1, pair.getValue());
                        break;
                    default:
                        components.add(pair.getValue());
                        components.add(separator);
                        applySeparator = true;
                }
            }
        }

        return (components.isEmpty()) ? null : new FormattedName(String.join("",components.subList(0,components.size()-1)));
    }

    private static FormattedName toVCardFormattedName(String name) {

        return new FormattedName(name);
    }

    private static String toVCardJSCompsParam(NameComponent[] components, String defaultSeparator) {

        if (ArrayUtils.isEmpty(components))
            return null;

        List<String> jscomps = new ArrayList<>();
        String adjustedDefaultSeparator = (defaultSeparator != null) ? DelimiterUtils.SEPARATOR_ID + ((defaultSeparator.startsWith(DelimiterUtils.COMMA_ARRAY_DELIMITER)) ? defaultSeparator.replace(DelimiterUtils.COMMA_ARRAY_DELIMITER, "\\,") : defaultSeparator ) : StringUtils.EMPTY;
        jscomps.add(adjustedDefaultSeparator);
        int[] count = new int[] {0,0,0,0,0,0,0};
        List<NameComponentEnum> enums = Arrays.asList(NameComponentEnum.values());
        for (NameComponent component : components) {
            if (!component.isExt()) {
                switch (component.getKind().getRfcValue()) {
                    case SEPARATOR:
                        jscomps.add(DelimiterUtils.SEPARATOR_ID + ((component.getValue().startsWith(DelimiterUtils.COMMA_ARRAY_DELIMITER)) ? component.getValue().replace(DelimiterUtils.COMMA_ARRAY_DELIMITER, "\\,") : component.getValue()) );
                        break;
                    default:
                        int j = enums.indexOf(component.getKind().getRfcValue());
                        jscomps.add((count[j] == 0) ? ""+j : j + "," + count[j]);
                        count[j]++;
                        break;
                }
            }
        }

        return String.join(DelimiterUtils.SEMICOLON_ARRAY_DELIMITER, jscomps);
    }


    private static String toVCardJSCompsParam(AddressComponent[] components, String defaultSeparator) {

        if (ArrayUtils.isEmpty(components))
            return null;

        List<String> jscomps = new ArrayList<>();
        String adjustedDefaultSeparator = (defaultSeparator != null) ? DelimiterUtils.SEPARATOR_ID + ((defaultSeparator.startsWith(DelimiterUtils.COMMA_ARRAY_DELIMITER)) ? defaultSeparator.replace(DelimiterUtils.COMMA_ARRAY_DELIMITER, "\\,") : defaultSeparator ) : StringUtils.EMPTY;
        jscomps.add(adjustedDefaultSeparator);
        int[] count = new int[] {0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0};
        List<AddressComponentEnum> enums = Arrays.asList(AddressComponentEnum.values());
        for (AddressComponent component : components) {
            if (!component.isExt()) {
                switch (component.getKind().getRfcValue()) {
                    case SEPARATOR:
                        jscomps.add(DelimiterUtils.SEPARATOR_ID + ((component.getValue().startsWith(DelimiterUtils.COMMA_ARRAY_DELIMITER)) ? component.getValue().replace(DelimiterUtils.COMMA_ARRAY_DELIMITER, "\\,") : component.getValue()) );
                        break;
                    default:
                        int j = enums.indexOf(component.getKind().getRfcValue());
                        jscomps.add((count[j] == 0) ? ""+j : j + "," + count[j]);
                        count[j]++;
                        break;
                }
            }
        }

        return String.join(DelimiterUtils.SEMICOLON_ARRAY_DELIMITER, jscomps);
    }

    private void fillVCardFormattedNames(VCard vcard, Card jsCard) {

        if (jsCard.getName() == null) {
            vcard.setFormattedName(jsCard.getUid());
            return;
        }

        List<FormattedName> fns = new ArrayList<>();
        if (StringUtils.isEmpty(jsCard.getName().getFull())) {
            if (jsCard.getName().getComponents()!=null) {
                if (jsCard.getLocalizationsPerPath("name") == null && jsCard.getLocalizationsPerPath("name/components") == null) {
                    FormattedName fn = toVCardFormattedName(jsCard.getName().getComponents(), jsCard.getName().getIsOrdered(), jsCard.getName().getDefaultSeparator());
                    fn.setParameter(VCardParamEnum.DERIVED.getValue(), "true");
                    fn.setLanguage(jsCard.getLanguage());
                    vcard.setFormattedName(fn);
                } else {
                    FormattedName fn = toVCardFormattedName(jsCard.getName().getComponents(), jsCard.getName().getIsOrdered(), jsCard.getName().getDefaultSeparator());
                    fn.setParameter(VCardParamEnum.DERIVED.getValue(), "true");
                    fn.setLanguage(jsCard.getLanguage());
                    fns.add(fn);
                    if (jsCard.getLocalizationsPerPath("name") != null) {
                        for (Map.Entry<String, JsonNode> localization : jsCard.getLocalizationsPerPath("name").entrySet()) {
                            NameComponent[] nameComponents = asJSCardNameComponentArray(localization.getValue().get("components"));
                            String defaultSeparator = (localization.getValue().get("defaultSeparator") != null) ? localization.getValue().get("defaultSeparator").asText() : null;
                            Boolean isOrdered = (localization.getValue().get("isOrdered") != null) ? localization.getValue().get("isOrdered").asBoolean() : null;
                            fn = toVCardFormattedName(nameComponents, isOrdered, defaultSeparator);
                            fn.setParameter(VCardParamEnum.DERIVED.getValue(), "true");
                            fn.setLanguage(localization.getKey());
                            fns.add(fn);
                        }
                    } else {
                        for (Map.Entry<String, JsonNode> localization : jsCard.getLocalizationsPerPath("name/components").entrySet()) {
                            NameComponent[] nameComponents = asJSCardNameComponentArray(localization.getValue());
                            fn = toVCardFormattedName(nameComponents, null, null);
                            fn.setParameter(VCardParamEnum.DERIVED.getValue(), "true");
                            fn.setLanguage(localization.getKey());
                            fns.add(fn);
                        }
                    }
                    vcard.setFormattedNameAlt(fns.toArray(new FormattedName[0]));
                }
            } else
                vcard.setFormattedName(toVCardFormattedName(jsCard.getUid()));
        }
        else {
            if (jsCard.getLocalizationsPerPath("name") != null || jsCard.getLocalizationsPerPath("name/full") != null) {
                FormattedName fn = toVCardFormattedName(jsCard.getName().getFull());
                fn.setLanguage(jsCard.getLanguage());
                fns.add(fn);
                vcard.setFormattedName(fn);
                if (jsCard.getLocalizationsPerPath("name") != null) {
                    for (Map.Entry<String, JsonNode> localization : jsCard.getLocalizationsPerPath("name").entrySet()) {
                        fn = toVCardFormattedName(localization.getValue().get("full").asText());
                        fn.setLanguage(localization.getKey());
                        fns.add(fn);
                    }
                } else {
                    for (Map.Entry<String, JsonNode> localization : jsCard.getLocalizationsPerPath("name/full").entrySet()) {
                        fn = toVCardFormattedName(localization.getValue().asText());
                        fn.setLanguage(localization.getKey());
                        fns.add(fn);
                    }
                }
                vcard.setFormattedNameAlt(fns.toArray(new FormattedName[0]));
            } else
                vcard.setFormattedName(toVCardFormattedName(jsCard.getName().getFull()));
        }
    }

    private static ExtendedStructuredName toVCardStructuredName(NameComponent[] nameComponents) {

        ExtendedStructuredName name = new ExtendedStructuredName();
        List<String> surnames = new ArrayList<>();
        List<String> givens = new ArrayList<>();
        for (NameComponent component : nameComponents) {
            if (component.isSurname())
                surnames.add(component.getValue());
            else if (component.isGiven())
                givens.add(component.getValue());
            else if (component.isGiven2())
                name.getAdditionalNames().add(component.getValue());
            else if (component.isTitle())
                name.getPrefixes().add(component.getValue());
            else if (component.isCredential())
                name.getSuffixes().add(component.getValue());
        }
        for (NameComponent component : nameComponents) {
            if (component.isSurname2()) {
                name.getSurname2().add(component.getValue());
                surnames.add(component.getValue());
            } else if (component.isGeneration()){
                name.getGeneration().add(component.getValue());
                name.getSuffixes().add(component.getValue());
            }
        }
        name.setFamilyNames((surnames.size()>0) ? surnames : null);
        name.setGiven((givens.size()>0) ? String.join(DelimiterUtils.COMMA_ARRAY_DELIMITER,givens) : null);
        return name;
    }

    private static String toVCardSortAsParam(Map<NameComponentKind, String> jsContactSortAs) {

        if (jsContactSortAs == null)
            return null;

        StringJoiner joiner = new StringJoiner(DelimiterUtils.COMMA_ARRAY_DELIMITER);
        if (jsContactSortAs.get(NameComponentKind.surname()) != null)
            joiner.add(jsContactSortAs.get(NameComponentKind.surname()));
        if (jsContactSortAs.get(NameComponentKind.given()) != null)
            joiner.add(jsContactSortAs.get(NameComponentKind.given()));
        if (jsContactSortAs.get(NameComponentKind.given2()) != null)
            joiner.add(jsContactSortAs.get(NameComponentKind.given2()));
        if (jsContactSortAs.get(NameComponentKind.title()) != null)
            joiner.add(jsContactSortAs.get(NameComponentKind.title()));
        if (jsContactSortAs.get(NameComponentKind.credential()) != null)
            joiner.add(jsContactSortAs.get(NameComponentKind.credential()));
        if (jsContactSortAs.get(NameComponentKind.surname2()) != null)
            joiner.add(jsContactSortAs.get(NameComponentKind.surname2()));
        if (jsContactSortAs.get(NameComponentKind.generation()) != null)
            joiner.add(jsContactSortAs.get(NameComponentKind.generation()));
        return joiner.toString();
    }

    private static NameComponent[] getNameComponentsOfPhonetics(NameComponent[] componentsWithPhonetic) {

        NameComponent[] components = new NameComponent[componentsWithPhonetic.length];
        int i = 0;
        for (NameComponent componentWithPhonetic : componentsWithPhonetic)
            components[i++] = NameComponent.builder()
                                        .value(componentWithPhonetic.getPhonetic())
                                        .kind(componentWithPhonetic.getKind())
                                        .build();
        return components;
    }

    private static List<String> getLanguagesOfPhoneticLocalizations(Card jsCard) {

        List<String> languages = new ArrayList<>();

        if (jsCard.getLocalizationsPerPath("name/phoneticSystem")!=null)
            languages.addAll(jsCard.getLocalizationsPerPath("name/phoneticSystem").keySet());

        if (jsCard.getLocalizationsPerPath("name/phoneticScript")!=null) {
            languages.removeAll(jsCard.getLocalizationsPerPath("name/phoneticScript").keySet());
            languages.addAll(jsCard.getLocalizationsPerPath("name/phoneticScript").keySet());
        }

        for (int i=0; i<= NameComponentEnum.values().length; i++) {
            String key = String.format("name/components/%d/phonetic", i);
            if (jsCard.getLocalizationsPerPath(key) != null) {
                languages.removeAll(jsCard.getLocalizationsPerPath(key).keySet());
                languages.addAll(jsCard.getLocalizationsPerPath(key).keySet());
            }
        }

        return (!languages.isEmpty()) ? languages : null;
    }

    private static NameComponent[] getNameComponentsOfPhoneticLocalizations(String language, Card jsCard) {


        List components = new ArrayList<NameComponent>();
        for (int i=0; i<= NameComponentEnum.values().length; i++) {
            String key = String.format("name/components/%d/phonetic", i);
            if (jsCard.getLocalization(language,key) != null) {
                components.add(NameComponent.builder()
                                            .value(jsCard.getLocalization(language,key).asText())
                                            .kind(jsCard.getName().getComponents()[i].getKind())
                                            .build());
            }
        }

        return (!components.isEmpty()) ? (NameComponent[]) components.toArray(new NameComponent[0]) : null;
    }

    private List<ExtendedStructuredName> toVCardStructuredNames(Card jsCard, VCard vcard) {

        List<ExtendedStructuredName> sns = new ArrayList<>();
        List<String> languagesOfPhoneticLocalizations = getLanguagesOfPhoneticLocalizations(jsCard);
        if (jsCard.getLocalizationsPerPath("name") != null ||
            jsCard.getLocalizationsPerPath("name/components") != null ||
            languagesOfPhoneticLocalizations != null) {
            ExtendedStructuredName sn = toVCardStructuredName(jsCard.getName().getComponents());
            sn.setLanguage(jsCard.getLanguage());
            sn.setParameter(VCardParamEnum.SORT_AS.getValue(), toVCardSortAsParam(jsCard.getName().getSortAs())); // did this way because Ez-vcard allows to sort only for surname and given name
            if (jsCard.getName().getIsOrdered())
                sn.setParameter(VCardParamEnum.JSCOMPS.getValue(), toVCardJSCompsParam(jsCard.getName().getComponents(),jsCard.getName().getDefaultSeparator())); // did this way because Ez-vcard allows to sort only for surname and given name
            VCardUtils.addVCardUnmatchedParams(sn,jsCard.getName());
            sns.add(sn);
            if (jsCard.getLocalizationsPerPath("name") != null) {

                for (Map.Entry<String, JsonNode> localization : jsCard.getLocalizationsPerPath("name").entrySet()) {

                    String phoneticSystem = ((localization.getValue().get("phoneticSystem")!=null) ? localization.getValue().get("phoneticSystem").asText() : null);
                    String phoneticScript = ((localization.getValue().get("phoneticScript")!=null) ? localization.getValue().get("phoneticScript").asText() : null);
                    NameComponent[] components = asJSCardNameComponentArray(localization.getValue().get("components"));
                    sn = toVCardStructuredName(components);
                    JsonNode isOrdered = (localization.getValue().get("components").get("isOrdered"));
                    if (isOrdered != null && isOrdered.asBoolean() == Boolean.TRUE) {
                        JsonNode defaultSeparator = (localization.getValue().get("components").get("defaultSeparator"));
                        sn.setParameter(VCardParamEnum.JSCOMPS.getValue(), toVCardJSCompsParam(asJSCardNameComponentArray(localization.getValue().get("components")), (defaultSeparator!=null) ? defaultSeparator.asText() : null)); // did this way because Ez-vcard allows to sort only for surname and given name
                    }
                    sn.setLanguage(localization.getKey());
                    sns.add(sn);

                    if (phoneticSystem!=null || phoneticScript !=null) {
                        sn = toVCardStructuredName(getNameComponentsOfPhonetics(components));
                        sn.setParameter(VCardParamEnum.PHONETIC.getValue(), phoneticSystem);
                        sn.setParameter(VCardParamEnum.SCRIPT.getValue(), phoneticScript);
                        sn.setLanguage(localization.getKey());
                        sns.add(sn);
                    }
                }
            }
            else if (jsCard.getLocalizationsPerPath("name/components") != null){
                for (Map.Entry<String, JsonNode> localization : jsCard.getLocalizationsPerPath("name/components").entrySet()) {
                    sn = toVCardStructuredName(asJSCardNameComponentArray(localization.getValue()));
                    sn.setLanguage(localization.getKey());
                    sns.add(sn);
                }
            }
            else {
                List<String> languages = getLanguagesOfPhoneticLocalizations(jsCard);
                for (String language : languages) {
                    String phoneticSystem = ((jsCard.getLocalization(language, "name/phoneticSystem") != null) ? jsCard.getLocalization(language, "name/phoneticSystem").asText() : null);
                    String phoneticScript = ((jsCard.getLocalization(language, "name/phoneticScript") != null) ? jsCard.getLocalization(language, "name/phoneticScript").asText() : null);
                    sn = toVCardStructuredName(getNameComponentsOfPhoneticLocalizations(language, jsCard));
                    sn.setParameter(VCardParamEnum.PHONETIC.getValue(), phoneticSystem);
                    sn.setParameter(VCardParamEnum.SCRIPT.getValue(), phoneticScript);
                    sn.setLanguage(language);
                    sns.add(sn);
                }
            }
        }
        else {
            ExtendedStructuredName sn = toVCardStructuredName(jsCard.getName().getComponents());
            sn.setParameter(VCardParamEnum.SORT_AS.getValue(), toVCardSortAsParam(jsCard.getName().getSortAs()));
            if (jsCard.getName().getIsOrdered() != null && jsCard.getName().getIsOrdered() == Boolean.TRUE)
                sn.setParameter(VCardParamEnum.JSCOMPS.getValue(), toVCardJSCompsParam(jsCard.getName().getComponents(),jsCard.getName().getDefaultSeparator())); // did this way because Ez-vcard allows to sort only for surname and given name
            VCardUtils.addVCardUnmatchedParams(sn,jsCard.getName());
            sns.add(sn);

            PhoneticSystem phoneticSystem = jsCard.getName().getPhoneticSystem();
            String phoneticScript = jsCard.getName().getPhoneticScript();
            if (phoneticSystem!=null || phoneticScript!=null) {
                sn = toVCardStructuredName(getNameComponentsOfPhonetics(jsCard.getName().getComponents()));
                sn.setParameter(VCardParamEnum.PHONETIC.getValue(), phoneticSystem.toJson());
                sn.setParameter(VCardParamEnum.SCRIPT.getValue(), phoneticScript);
                sns.add(sn);
            }
        }

        return sns;
    }


    private void fillVCardNames(VCard vcard, Card jsCard) {

        if (jsCard.getName() == null || jsCard.getName().getComponents() == null)
            return;

        List<ExtendedStructuredName> sns = toVCardStructuredNames(jsCard, vcard);
        if (sns.size() == 1)
            vcard.setProperty(sns.get(0));
        else
            vcard.addPropertyAlt(ExtendedStructuredName.class, sns.toArray(new ExtendedStructuredName[0]));
    }


    private static ezvcard.property.Nickname toVCardNickname(Nickname jsNickName) {

        ezvcard.property.Nickname nickname = new ezvcard.property.Nickname();
        nickname.getValues().add(jsNickName.getName());
        nickname.setPref(jsNickName.getPref());
        nickname.setType(toVCardTypeParam(jsNickName));
        VCardUtils.addVCardUnmatchedParams(nickname, jsNickName);
        return nickname;
    }


    private void fillVCardNickNames(VCard vcard, Card jsCard) {

        if (jsCard.getNicknames() == null)
            return;

        for (Map.Entry<String, Nickname> entry : jsCard.getNicknames().entrySet()) {
            if (jsCard.getLocalizationsPerPath("nicknames/"+entry.getKey()) == null &&
                    jsCard.getLocalizationsPerPath("nicknames/"+entry.getKey()+"/name")==null) {
                ezvcard.property.Nickname nickname = toVCardNickname(entry.getValue());
                addVCardPropIdParam(nickname, entry.getKey());
                vcard.addNickname(nickname);
            }
            else {
                List<ezvcard.property.Nickname> nicknames = new ArrayList<>();
                ezvcard.property.Nickname nickname = toVCardNickname(entry.getValue());
                addVCardPropIdParam(nickname, entry.getKey());
                nicknames.add(nickname);

                Map<String,JsonNode> localizations = jsCard.getLocalizationsPerPath("nicknames/"+entry.getKey());
                if (localizations != null) {
                    for (Map.Entry<String, JsonNode> localization : localizations.entrySet()) {
                        nickname = toVCardNickname(asJSCardNickName(localization.getValue()));
                        nickname.setLanguage(localization.getKey());
                        addVCardPropIdParam(nickname, entry.getKey());
                        nicknames.add(nickname);
                    }
                }
                localizations = jsCard.getLocalizationsPerPath("nicknames/"+entry.getKey()+"/name");
                if (localizations != null) {
                    for (Map.Entry<String,JsonNode> localization : localizations.entrySet()) {
                        nickname = new ezvcard.property.Nickname();
                        nickname.getValues().add(localization.getValue().asText());
                        nickname.setLanguage(localization.getKey());
                        addVCardPropIdParam(nickname, entry.getKey());
                        nicknames.add(nickname);
                    }
                }
                vcard.addNicknameAlt(nicknames.toArray(new ezvcard.property.Nickname[0]));
            }
        }
    }

    private static boolean isVCardNullAddress(Address address) {

        return (address.getCountry() ==null &&
                address.getCountryCode() ==null &&
                address.getRegion() == null &&
                address.getLocality() == null &&
                address.getStreetAddressItems() == null &&
                address.getPostOfficeBox() == null &&
                address.getPostcode() == null &&
                address.getStreetExtendedAddressItems() == null &&
                address.getStreetName() == null &&
                address.getStreetNumber() == null &&
                address.getDirection() == null &&
                address.getDistrict() == null &&
                address.getSubDistrict() == null &&
                address.getBlock() == null &&
                address.getLandmark() == null &&
                address.getApartment() == null &&
                address.getBuilding() == null &&
                address.getFloor() == null &&
                address.getRoom() == null);
    }

    private static String toVCardAddressLabelParam(Address addr) {

        List<String> components = new ArrayList<>();
        String separator = (StringUtils.isNotEmpty(addr.getDefaultSeparator())) ? addr.getDefaultSeparator() : DelimiterUtils.SPACE_DELIMITER;
        boolean applySeparator = (addr.getIsOrdered() == Boolean.TRUE) ;
        for (AddressComponent pair : addr.getComponents()) {
            if (pair.getKind().isRfcValue()) {
                switch (pair.getKind().getRfcValue()) {
                    case SEPARATOR:
                        if (applySeparator)
                            components.set(components.size() - 1, pair.getValue());
                        break;
                    default:
                        components.add(pair.getValue());
                        components.add(separator);
                        applySeparator = true;
                }
            }
        }

        return (components.isEmpty()) ? null : String.join("",components.subList(0,components.size()-1));

    }

    private static <E extends Enum<E> & VCardTypeDerivedEnum> List<String> toVCardTypeParmaValues(Class<E> enumType, Collection<E> enumValues) {

        List<String> typeValues = new ArrayList();
        for (E value : enumValues) {
            try {
                String typeItem = (String) enumType.getDeclaredMethod("toVCardTypeParam", enumType).invoke(null, value);
                if (typeItem != null)
                    typeValues.add(typeItem);
            } catch (Exception e) {
                throw new InternalErrorException(e.getMessage());

            }
        }
        return typeValues;
    }

    private static String toVCardOffsetValueFromTimezone(String timezone) {

        Pattern pattern = Pattern.compile("Etc/GMT([+\\-])\\d{1,2}");
        Matcher matcher = pattern.matcher(timezone);

        if (!matcher.find())
            return timezone;

        String offset = StringUtils.EMPTY;
        char[] chars = timezone.toCharArray();
        if (chars[7] == '+')
            offset += "-";
        else
            offset += "+";

        return offset + String.format("%02d00", Integer.parseInt(timezone.substring(8)));
    }

    private String toVCardTimezoneAsTextValue(String timeZone)   {

      if (timeZone == null)
          return null;

      if (config.isConvertTimezoneToOffset())
          return toVCardOffsetValueFromTimezone(timeZone);
      else
          return timeZone;
    }

    private Timezone toVCardTimezone(String timeZone)   {

        if (timeZone == null)
            return null;

        if (config.isConvertTimezoneToOffset()) {
            try {
                return new Timezone(UtcOffset.parse(toVCardOffsetValueFromTimezone(timeZone)));
            } catch (Exception e) {}
        }

        return new Timezone(timeZone);
    }

    private GeoUri toVCardGeoUri(String coordinates) {

        return (coordinates!=null) ? GeoUri.parse(coordinates) : null;
    }

    private ExtendedAddress toVCardAddress(Address address, Map<String, TimeZone> timeZones, String language) {

        ExtendedAddress addr = new ExtendedAddress();
        if (!isVCardNullAddress(address)) {
            if (config.isSetAutoAddrLabel())
                addr.setLabel(toVCardAddressLabelParam(address));
            addr.setPoBox(address.getPostOfficeBox());
            if (address.getStreetExtendedAddressItems()!=null)
                    addr.getExtendedAddresses().addAll(address.getStreetExtendedAddressItems());
            if (address.getStreetAddressItems()!=null)
                    addr.getStreetAddresses().addAll(address.getStreetAddressItems());
            addr.setLocality(address.getLocality());
            addr.setRegion(address.getRegion());
            addr.setPostalCode(address.getPostcode());
            addr.setCountry(address.getCountry());
            addr.setRoom(address.getRoom());
            addr.setApartment(address.getApartment());
            addr.setFloor(address.getFloor());
            addr.setStreetName(address.getStreetName());
            addr.setStreetNumber(address.getStreetNumber());
            addr.setBuilding(address.getBuilding());
            addr.setBlock(address.getBlock());
            addr.setDirection(address.getDirection());
            addr.setLandmark(address.getLandmark());
            addr.setSubDistrict(address.getSubDistrict());
            addr.setDistrict(address.getDistrict());
            addr.setPref(address.getPref());
            if (address.getTimeZone() != null) {
                TimeZone timeZone = null;
                if (timeZones != null)
                    timeZone = timeZones.get(address.getTimeZone());
                if (timeZone != null) {
                    if (timeZone.getStandard() != null && timeZone.getStandard().size() > 0)
                        addr.setTimezone(timeZone.getStandard().get(0).getOffsetFrom());
                } else
                    addr.setTimezone(toVCardTimezoneAsTextValue(address.getTimeZone()));
            }
            addr.setGeo(toVCardGeoUri(address.getCoordinates()));
            if (address.getCountryCode() != null)
                addr.setParameter(VCardParamEnum.CC.getValue(), address.getCountryCode());
            if (!address.hasNoContext()) {
                List<String> vCardTypeValues = toVCardTypeParmaValues(AddressContextEnum.class, AddressContext.toEnumValues(address.getContexts().keySet()));
                for (String vCardTypeValue : vCardTypeValues)
                    addr.getTypes().add(AddressType.get(vCardTypeValue));
            }
            if (address.getIsOrdered()!= null && address.getIsOrdered() == Boolean.TRUE)
                addr.setParameter(VCardParamEnum.JSCOMPS.getValue(), toVCardJSCompsParam(address.getComponents(),address.getDefaultSeparator()));

        }

        if (address.getFull() != null)
            addr.setLabel(address.getFull());

        addr.setLanguage(language);
        VCardUtils.addVCardUnmatchedParams(addr,address);

        return addr;
    }

    private static Object asJSCardTypeObject(JsonNode jsonNode, Class classs) {

        if (jsonNode == null || !jsonNode.isObject())
            return null;

        try {
            return mapper.convertValue(jsonNode, classs);
        } catch (Exception e) {
            return null;
        }
    }


    private static Address asJSCardAddress(JsonNode jsonNode) {
        return (Address) asJSCardTypeObject(jsonNode, Address.class);
    }

    private static Pronouns asJSCardPronouns(JsonNode jsonNode) {
        return (Pronouns) asJSCardTypeObject(jsonNode, Pronouns.class);
    }
    private static Note asJSCardNote(JsonNode jsonNode) {
        return (Note) asJSCardTypeObject(jsonNode, Note.class);
    }

    private static Title asJSCardTitle(JsonNode jsonNode) {
        return (Title) asJSCardTypeObject(jsonNode, Title.class);
    }

    private static Nickname asJSCardNickName(JsonNode jsonNode) {
        return (Nickname) asJSCardTypeObject(jsonNode, Nickname.class);
    }

    private static Organization asJSCardOrganization(JsonNode jsonNode) {
        return (Organization) asJSCardTypeObject(jsonNode, Organization.class);
    }

    private static NameComponent[] asJSCardNameComponentArray(JsonNode arrayNode) {

        if (arrayNode == null || !arrayNode.isArray())
            return null;

        List<NameComponent> ncs = new ArrayList<>();
        try {
            for (JsonNode node : arrayNode) {
                NameComponent nc = (NameComponent) asJSCardTypeObject(node, NameComponent.class);
                if (nc!=null)
                    ncs.add(nc);
            }
            return (ncs.size() > 0) ? ncs.toArray(new NameComponent[0]) : null;
        } catch (Exception e) {
            return null;
        }

    }

    private static String[] asJSCardOrgUnitValuesArray(JsonNode arrayNode) {

        if (arrayNode == null || !arrayNode.isArray())
            return null;

        List<String> ous = new ArrayList<>();
        try {
            for (JsonNode node : arrayNode) {
                OrgUnit ou = (OrgUnit) asJSCardTypeObject(node, OrgUnit.class);
                if (ou!=null)
                    ous.add(ou.getName());
            }
            return (ous.size() > 0) ? ous.toArray(new String[0]) : null;
        } catch (Exception e) {
            return null;
        }

    }


    private void fillVCardAddresses(VCard vcard, Card jsCard) {

        if (jsCard.getAddresses() == null)
            return;

        for (Map.Entry<String,Address> entry : jsCard.getAddresses().entrySet()) {

            Address address = entry.getValue();
            if (isVCardNullAddress(address) && address.getFull() == null)
                continue;

            if (jsCard.getLocalizationsPerPath("addresses/"+entry.getKey()) == null &&
                jsCard.getLocalizationsPerPath("addresses/"+entry.getKey()+"/full")==null) {
                ExtendedAddress addr = toVCardAddress(address, jsCard.getCustomTimeZones(), jsCard.getLanguage());
                addVCardPropIdParam(addr, entry.getKey());
                vcard.addProperty(addr);
            }
            else {
                List<ExtendedAddress> addrs = new ArrayList<>();
                ExtendedAddress addr = toVCardAddress(address, jsCard.getCustomTimeZones(), jsCard.getLanguage());
                addVCardPropIdParam(addr, entry.getKey());
                addrs.add(addr);

                Map<String,JsonNode> localizations = jsCard.getLocalizationsPerPath("addresses/"+entry.getKey());
                if (localizations != null) {
                    for (Map.Entry<String, JsonNode> localization : localizations.entrySet()) {
                        addr = toVCardAddress(asJSCardAddress(localization.getValue()), jsCard.getCustomTimeZones(), localization.getKey());
                        addVCardPropIdParam(addr, entry.getKey());
                        addrs.add(addr);
                    }
                }
                localizations = jsCard.getLocalizationsPerPath("addresses/"+entry.getKey()+"/full");
                if (localizations != null) {
                    for (Map.Entry<String,JsonNode> localization : localizations.entrySet()) {
                        addr = toVCardAddress(Address.builder().full(localization.getValue().asText()).build(), jsCard.getCustomTimeZones(), localization.getKey());
                        addVCardPropIdParam(addr, entry.getKey());
                        addrs.add(addr);
                    }
                }
                vcard.addPropertyAlt(ExtendedAddress.class, addrs.toArray(new ExtendedAddress[0]));
            }
        }
    }

    private static <T extends PlaceProperty> T toVCardPlaceProperty(Class<T> classs, Anniversary anniversary) {

        if (anniversary.getPlace() == null)
            return null;

        try {
            Constructor<T> constructor;
            if (anniversary.getPlace().getFull() != null) {
                constructor = classs.getDeclaredConstructor(String.class);
                return constructor.newInstance(anniversary.getPlace().getFull());
            }

            if (!isVCardNullAddress(anniversary.getPlace())) {
                constructor = classs.getDeclaredConstructor(String.class);
                return constructor.newInstance(toVCardAddressLabelParam(anniversary.getPlace()));
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


    private static <T extends DateOrTimeProperty> T toVCardDateOrTimeProperty(Class<T> classs, Anniversary anniversary) {

        try {
            if (anniversary.getDate().getDate()!=null) {
                Constructor<T> constructor = classs.getDeclaredConstructor(Calendar.class, boolean.class);
                return constructor.newInstance(anniversary.getDate().getDate().getUtc(), true);
            }
            if (anniversary.getDate().getPartialDate()!=null) {
                Constructor<T> constructor = classs.getDeclaredConstructor(PartialDate.class);
                T property = constructor.newInstance(anniversary.getDate().getPartialDate().toVCardPartialDate());
                try {
                    if (anniversary.getDate().getPartialDate().getCalendarScale()!=null)
                        property.setCalscale(Calscale.get(anniversary.getDate().getPartialDate().getCalendarScale()));
                } catch(Exception e) {}
                return property;
            }
        } catch (Exception e) {
            throw new InternalErrorException(String.format("Internal Error: toVCardDateOrTimeProperty anniversary=%s message=%s", anniversary.toString(), e.getMessage()));
        }

        return null;
    }

    private void fillVCardAnniversaries(VCard vcard, Card jsCard) {

        if (jsCard.getAnniversaries() == null)
            return;

        for (Map.Entry<String,Anniversary> entry : jsCard.getAnniversaries().entrySet()) {

            Anniversary anniversary = entry.getValue();
            if (!anniversary.isOtherAnniversary()) {
                switch (anniversary.getKind().getRfcValue()) {
                    case BIRTH:
                        vcard.setBirthday(toVCardDateOrTimeProperty(Birthday.class, anniversary));
                        VCardUtils.addVCardUnmatchedParams(vcard.getBirthday(),anniversary);
                        addVCardPropIdParam(vcard.getBirthday(), entry.getKey());
                        Birthplace birthplace = toVCardPlaceProperty(Birthplace.class, anniversary);
                        if (birthplace!=null) {
                            addVCardPropIdParam(birthplace, entry.getKey());
                            vcard.setBirthplace(birthplace);
                        }
                        break;
                    case DEATH:
                        vcard.setDeathdate(toVCardDateOrTimeProperty(Deathdate.class, anniversary));
                        VCardUtils.addVCardUnmatchedParams(vcard.getDeathdate(),anniversary);
                        addVCardPropIdParam(vcard.getDeathdate(), entry.getKey());
                        Deathplace deathplace = toVCardPlaceProperty(Deathplace.class, anniversary);
                        if (deathplace!=null) {
                            addVCardPropIdParam(deathplace, entry.getKey());
                            vcard.setDeathplace(deathplace);
                        }
                        break;
                    case WEDDING:
                        vcard.setAnniversary(toVCardDateOrTimeProperty(ezvcard.property.Anniversary.class, anniversary));
                        VCardUtils.addVCardUnmatchedParams(vcard.getAnniversary(),anniversary);
                        addVCardPropIdParam(vcard.getAnniversary(), entry.getKey());
                        break;
                }
            }
        }

    }

    private Expertise toVCardExpertise(PersonalInfo pi) {

        Expertise e = new Expertise(pi.getValue());
        VCardUtils.addVCardUnmatchedParams(e, pi);
        addVCardPropIdParam(e, pi.getPropId());
        e.setIndex(pi.getListAs());
        if (pi.getLevel() != null && pi.getLevel().isRfcValue())
            e.setLevel(ExpertiseLevel.get(PersonalInfoLevelEnum.getVCardExpertiseLevel(pi.getLevel().getRfcValue())));
        else
            e.setParameter(VCardParamEnum.LEVEL.getValue(), pi.getLevel().getExtValue().toString().toUpperCase());
        return e;
    }

    private Hobby toVCardHobby(PersonalInfo pi) {

        Hobby h = new Hobby(pi.getValue());
        VCardUtils.addVCardUnmatchedParams(h, pi);
        addVCardPropIdParam(h, pi.getPropId());
        h.setIndex(pi.getListAs());
        if (pi.getLevel() != null && pi.getLevel().isRfcValue())
            h.setLevel(HobbyLevel.get(pi.getLevel().getRfcValue().name()));
        else
            h.setParameter(VCardParamEnum.LEVEL.getValue(), pi.getLevel().getExtValue().toString().toUpperCase());
        return h;
    }

    private Interest toVCardInterest(PersonalInfo pi) {

        Interest i = new Interest(pi.getValue());
        VCardUtils.addVCardUnmatchedParams(i, pi);
        addVCardPropIdParam(i, pi.getPropId());
        i.setIndex(pi.getListAs());
        if (pi.getLevel() != null && pi.getLevel().isRfcValue())
            i.setLevel(InterestLevel.get(pi.getLevel().getRfcValue().name()));
        else
            i.setParameter(VCardParamEnum.LEVEL.getValue(), pi.getLevel().getExtValue().toString().toUpperCase());
        return i;
    }

    private void fillVCardPropsFromJSCardPersonalInfos(VCard vcard, Card jsCard) {

        if (jsCard.getPersonalInfo() == null)
            return;

        for (Map.Entry<String, PersonalInfo> entry : jsCard.getPersonalInfo().entrySet()) {
            PersonalInfo pi = entry.getValue();
            pi.setPropId(entry.getKey());
            if (pi.getKind() != null && pi.getKind().isRfcValue()) {
                switch (pi.getKind().getRfcValue()) {
                    case EXPERTISE:
                        Expertise e = toVCardExpertise(pi);
                        addVCardX_ABLabel(pi, e, vcard);
                        vcard.getExpertise().add(e);
                        break;
                    case HOBBY:
                        Hobby h = toVCardHobby(pi);
                        addVCardX_ABLabel(pi,h,vcard);
                        vcard.getHobbies().add(h);
                        break;
                    case INTEREST:
                        Interest i = toVCardInterest(pi);
                        addVCardX_ABLabel(pi,i,vcard);
                        vcard.getInterests().add(i);
                        break;
                }
            }
        }
    }

    private static Language toVCardLanguage(LanguagePref languagePref) {

        Language language = new Language(languagePref.getLanguage());
        String vCardTypeValue = toVCardTypeParam(languagePref);
        if (vCardTypeValue!=null)
            language.setParameter(VCardParamEnum.TYPE.getValue(), vCardTypeValue);
        language.setPref(languagePref.getPref());
        return language;
    }

    private void fillVCardLanguages(VCard vcard, Card jsCard) {

        if (jsCard.getPreferredLanguages() == null)
            return;

        for (Map.Entry<String, LanguagePref> entry : jsCard.getPreferredLanguages().entrySet()) {
            Language language = toVCardLanguage(entry.getValue());
            addVCardPropIdParam(language, entry.getKey());
            VCardUtils.addVCardUnmatchedParams(language,entry.getValue());
            vcard.addLanguage(language);
        }
    }

    private Telephone toVCardTelephone(Phone phone) {

        Telephone tel;
        try {
            tel = new Telephone(TelUri.parse(phone.getNumber()));
        } catch(Exception e) {
            tel = new Telephone(phone.getNumber());
        }
        tel.setPref(phone.getPref());
        addVCardPropIdParam(tel, phone.getPropId());

        List<String> vCardTypeValues = new ArrayList<>();
        if (!phone.hasNoContext())
            vCardTypeValues.addAll(toVCardTypeParmaValues(ContextEnum.class, Context.toEnumValues(phone.getContexts().keySet())));
        if (phone.hasNoFeature())
            vCardTypeValues.addAll(toVCardTypeParmaValues(PhoneFeatureEnum.class, PhoneFeature.toEnumValues(phone.getFeatures().keySet())));

        for (String vCardTypeValue : vCardTypeValues)
            tel.getTypes().add(TelephoneType.get(vCardTypeValue));
        VCardUtils.addVCardUnmatchedParams(tel,phone);

        return tel;
    }

    private void fillVCardTelephones(VCard vcard, Card jsCard) {

        if (jsCard.getPhones() == null)
            return;

        for (Map.Entry<String, Phone> entry : jsCard.getPhones().entrySet()) {
            Phone phone = entry.getValue();
            phone.setPropId(entry.getKey());
            Telephone vcardPhone = toVCardTelephone(phone);
            addVCardX_ABLabel(phone,vcardPhone,vcard);
            vcard.getTelephoneNumbers().add(vcardPhone);
        }
    }

    private Email toVCardEmail(EmailAddress emailAddress) {

        Email email = new Email(emailAddress.getAddress());
        email.setPref(emailAddress.getPref());
        addVCardPropIdParam(email, emailAddress.getPropId());
        if (!emailAddress.hasNoContext()) {
            List<String> vCardTypeValues = toVCardTypeParmaValues(ContextEnum.class, Context.toEnumValues(emailAddress.getContexts().keySet()));
            for (String vCardTypeValue : vCardTypeValues)
                email.getTypes().add(EmailType.get(vCardTypeValue));
        }
        VCardUtils.addVCardUnmatchedParams(email,emailAddress);
        return email;
    }

    private void fillVCardEmails(VCard vcard, Card jsCard) {

        if (jsCard.getEmails() == null)
            return;

        for (Map.Entry<String,EmailAddress> entry : jsCard.getEmails().entrySet()) {
            EmailAddress email = entry.getValue();
            email.setPropId(entry.getKey());
            Email vcardEmail = toVCardEmail(email);
            addVCardX_ABLabel(email,vcardEmail,vcard);
            vcard.getEmails().add(vcardEmail);
        }
    }

    private static ImageType toVCardImageTypeValue(String mediaType) {

        if (mediaType == null)
            return null;

        for (ImageType it : ImageType.all()) {
            if (it.getMediaType().equals(mediaType))
                return it;
        }
        return null;
    }

    private static SoundType toVCardSoundTypeValue(String mediaType) {

        if (mediaType == null)
            return null;

        for (SoundType it : SoundType.all()) {
            if (it.getMediaType().equals(mediaType))
                return it;
        }
        return null;
    }

    private static KeyType toVCardKeyTypeValue(String mediaType) {

        if (mediaType == null)
            return null;

        for (KeyType it : KeyType.all()) {
            if (it.getMediaType().equals(mediaType))
                return it;
        }
        return null;
    }


    private <T extends UriProperty> T toVCardUriProperty(Class<T> classs, Resource resource, VCard vcard) {

        try {
            Constructor<T> constructor = classs.getDeclaredConstructor(String.class);
            T object = constructor.newInstance(resource.getUri());
            fillVCardProperty(object,resource);
            VCardUtils.addVCardUnmatchedParams(object,resource);
            addVCardPropIdParam(object, resource.getPropId());
            addVCardX_ABLabel(resource, object, vcard);
            return object;
        } catch (Exception e) {
            throw new InternalErrorException(e.getMessage());
        }
    }


    private <T extends BinaryProperty> T toVCardImageProperty(Class<T> classs, Resource resource) {

        T object;
        Constructor<T> constructor;
        try {
            if (resource.getUri().startsWith("data:")) {
                DataUri data = DataUri.parse(resource.getUri());
                constructor = classs.getDeclaredConstructor(byte[].class, ImageType.class);
                ImageType imageType = toVCardImageTypeValue(data.getContentType());
                object = constructor.newInstance(data.getData(), imageType);
                if (data.getText() != null)
                    classs.getDeclaredMethod("setText", String.class, SoundType.class).invoke(object, data.getText(), imageType);
            } else {
                constructor = classs.getDeclaredConstructor(String.class, ImageType.class);
                object = constructor.newInstance(resource.getUri(), toVCardImageTypeValue(resource.getMediaType()));
            }
        } catch (Exception e) {
            e.printStackTrace();
            throw new InternalErrorException(e.getMessage());
        }

        return object;
    }


    private <T extends BinaryProperty> T toVCardSoundProperty(Class<T> classs, Resource resource) {

        T object;
        Constructor<T> constructor;
        try {
            if (resource.getUri().startsWith("data:")) {
                DataUri data = DataUri.parse(resource.getUri());
                constructor = classs.getDeclaredConstructor(byte[].class, SoundType.class);
                SoundType soundType = toVCardSoundTypeValue(data.getContentType());
                object = constructor.newInstance(data.getData(), soundType);
                if (data.getText() != null)
                    classs.getDeclaredMethod("setText", String.class, SoundType.class).invoke(object, data.getText(), soundType);
            } else {
                constructor = classs.getDeclaredConstructor(String.class, SoundType.class);
                object = constructor.newInstance(resource.getUri(), toVCardSoundTypeValue(resource.getMediaType()));
            }
        } catch (Exception e) {
            e.printStackTrace();
            throw new InternalErrorException(e.getMessage());
        }

        return object;
    }


    private <T extends BinaryProperty> T toVCardKeyProperty(Class<T> classs, Resource resource) {

        T object;
        Constructor<T> constructor;
        try {
            if (resource.getUri().startsWith("data:")) {
                DataUri data = DataUri.parse(resource.getUri());
                constructor = classs.getDeclaredConstructor(byte[].class, KeyType.class);
                KeyType keyType = toVCardKeyTypeValue(data.getContentType());
                object = constructor.newInstance(data.getData(), keyType);
                if (data.getText() != null)
                    classs.getDeclaredMethod("setText", String.class, KeyType.class).invoke(object, data.getText(), keyType);
            } else {
                constructor = classs.getDeclaredConstructor(String.class, KeyType.class);
                object = constructor.newInstance(resource.getUri(), toVCardKeyTypeValue(resource.getMediaType()));
            }
        } catch (Exception e) {
            throw new InternalErrorException(e.getMessage());
        }

        return object;
    }


    private <T extends BinaryProperty> T toVCardImageProperty(Class<T> classs, Resource resource, VCard vcard) {

        try {
            T object = toVCardImageProperty(classs, resource);
            fillVCardProperty(object,resource);
            VCardUtils.addVCardUnmatchedParams(object,resource);
            addVCardPropIdParam(object, resource.getPropId());
            addVCardX_ABLabel(resource, object, vcard);
            return object;
        } catch (Exception e) {
            throw new InternalErrorException(e.getMessage());
        }
    }

    private Photo toVCardPhoto(MediaResource resource) {

        Photo photo = toVCardImageProperty(Photo.class, resource);
        photo.setPref(resource.getPref());
        VCardUtils.addVCardUnmatchedParams(photo,resource);
        return photo;
    }

    private static Impp toVCardImpp(OnlineService onlineService) {

        Impp impp = new Impp(onlineService.getUri());
        impp.setPref(onlineService.getPref());
        if (!onlineService.hasNoContext()) {
            List<String> vCardTypeValues = toVCardTypeParmaValues(ContextEnum.class, Context.toEnumValues(onlineService.getContexts().keySet()));
            for (String vCardTypeValue : vCardTypeValues)
                impp.getTypes().add(ImppType.get(vCardTypeValue));
        }
        if (onlineService.getService()!=null)
            impp.setParameter(VCardParamEnum.SERVICE_TYPE.getValue(), onlineService.getService());
        if (onlineService.getUser()!=null)
            impp.setParameter(VCardParamEnum.USERNAME.getValue(), onlineService.getUser());

        VCardUtils.addVCardUnmatchedParams(impp,onlineService);

        return impp;
    }

    private static RawProperty toVCardSocialProfile(OnlineService onlineService) {

        RawProperty raw;
        if (onlineService.getUri() != null) {
            raw = new RawProperty(VCardPropEnum.SOCIALPROFILE.getValue(), onlineService.getUri(), VCardDataType.URI);
            if (onlineService.getUser()!=null)
                raw.setParameter(VCardParamEnum.USERNAME.getValue(), onlineService.getUser());
        }
        else
            raw = new RawProperty(VCardPropEnum.SOCIALPROFILE.getValue(), onlineService.getUser(), VCardDataType.TEXT);
        String vCardTypeValue = toVCardTypeParam(onlineService);
        if (vCardTypeValue!=null)
            raw.setParameter(VCardParamEnum.TYPE.getValue(), vCardTypeValue);
        if (onlineService.getPref()!=null)
            raw.setParameter(VCardParamEnum.PREF.getValue(), onlineService.getPref().toString());
        if (onlineService.getService()!=null)
            raw.setParameter(VCardParamEnum.SERVICE_TYPE.getValue(), onlineService.getService());
        VCardUtils.addVCardUnmatchedParams(raw,onlineService);

        return raw;
    }

    private static <T extends VCardProperty> void fillVCardProperty(T property, Resource resource) {

        if (resource.getMediaType()!=null)
            property.setParameter(VCardParamEnum.MEDIATYPE.getValue(),resource.getMediaType());
        if (resource.getPref() != null)
            property.setParameter(VCardParamEnum.PREF.getValue(), resource.getPref().toString());
        String vCardTypeValue = toVCardTypeParam(resource);
        if (vCardTypeValue!=null)
            property.setParameter(VCardParamEnum.TYPE.getValue(), vCardTypeValue);
    }



    private void fillVCardPropsFromJSCardOnlineServices(VCard vcard, Card jsCard) {

        if (jsCard.getOnlineServices() == null)
            return;

        for(Map.Entry<String, OnlineService> entry : jsCard.getOnlineServices().entrySet()) {
            OnlineService onlineService = entry.getValue();
            if (onlineService.getVCardName() != null && onlineService.getVCardName().equalsIgnoreCase(VCardPropEnum.IMPP.getValue())) {
                Impp impp = toVCardImpp(onlineService);
                addVCardPropIdParam(impp, entry.getKey());
                addVCardX_ABLabel(onlineService, impp, vcard);
                vcard.getImpps().add(impp);
            } else {
                RawProperty rawProperty = toVCardSocialProfile(onlineService);
                addVCardPropIdParam(rawProperty, entry.getKey());
                addVCardX_ABLabel(onlineService, rawProperty, vcard);
                vcard.addProperty(rawProperty);
            }
        }
    }

    private CalendarRequestUri toVCardCaladruri(SchedulingAddress s) {

        CalendarRequestUri caladruri = new CalendarRequestUri(s.getUri());
        caladruri.setPref(s.getPref());
        addVCardPropIdParam(caladruri, s.getPropId());
        if (!s.hasNoContext()) {
            String vCardTypeValue = toVCardTypeParam(s);
            if (vCardTypeValue!=null)
                caladruri.setParameter(VCardParamEnum.TYPE.getValue(), vCardTypeValue);
        }
        VCardUtils.addVCardUnmatchedParams(caladruri,s);
        return caladruri;
    }


    private void fillVCardCalendarRequestUris(VCard vcard, Card jsCard) {

        if (jsCard.getSchedulingAddresses() == null)
            return;

        for(Map.Entry<String, SchedulingAddress> entry : jsCard.getSchedulingAddresses().entrySet()) {
            SchedulingAddress s = entry.getValue();
            s.setPropId(entry.getKey());
            CalendarRequestUri vcardCaladruri = toVCardCaladruri(s);
            VCardUtils.addVCardUnmatchedParams(vcardCaladruri,s);
            addVCardX_ABLabel(s,vcardCaladruri,vcard);
            vcard.getCalendarRequestUris().add(vcardCaladruri);
        }
    }

    private void fillVCardPropsFromJSCardCalendars(VCard vcard, Card jsCard) {

        if (jsCard.getCalendars() == null)
            return;

        for (Map.Entry<String,CalendarResource> entry : jsCard.getCalendars().entrySet()) {

            CalendarResource resource = entry.getValue();
            resource.setPropId(entry.getKey());
            if (resource.getKind()!=null && resource.getKind().isRfcValue()) {
                switch (resource.getKind().getRfcValue()) {
                    case FREEBUSY:
                        vcard.getFbUrls().add(toVCardUriProperty(FreeBusyUrl.class, resource, vcard));
                        break;
                    case CALENDAR:
                        vcard.getCalendarUris().add(toVCardUriProperty(CalendarUri.class, resource, vcard));
                        break;
                }
            }
        }
    }


    private void fillVCardKeys(VCard vcard, Card jsCard) {

        if (jsCard.getCryptoKeys() == null)
            return;

        for (Map.Entry<String,CryptoResource> entry : jsCard.getCryptoKeys().entrySet()) {
            Key key = toVCardKeyProperty(Key.class, entry.getValue());
            VCardUtils.addVCardUnmatchedParams(key,entry.getValue());
            addVCardPropIdParam(key, entry.getKey());
            addVCardX_ABLabel(entry.getValue(),key,vcard);
            vcard.getKeys().add(key);
        }
    }

    private void fillVCardContactUris(VCard vcard, Card jsCard) {

        if (jsCard.getLinks() == null)
            return;

        for (Map.Entry<String,LinkResource> entry : jsCard.getLinks().entrySet()) {

            LinkResource resource = entry.getValue();
            resource.setPropId(entry.getKey());
            if (resource.isGenericLink())
                vcard.getUrls().add(toVCardUriProperty(Url.class,resource,vcard));
            else {
                if (resource.getKind()!=null && resource.getKind().isRfcValue()) {
                    switch (resource.getKind().getRfcValue()) {
                        case CONTACT:
                            RawProperty rp = new RawProperty(VCardPropEnum.CONTACT_URI.getValue(), resource.getUri());
                            fillVCardProperty(rp, resource);
                            VCardUtils.addVCardUnmatchedParams(rp,resource);
                            addVCardPropIdParam(rp, resource.getPropId());
                            vcard.getExtendedProperties().add(rp);
                            break;
                    }
                }
            }
        }
    }

    private void fillVCardPropsFromJSCardMedia(VCard vcard, Card jsCard) {

        if (jsCard.getMedia() == null)
            return;

        for (Map.Entry<String,MediaResource> entry : jsCard.getMedia().entrySet()) {

            MediaResource resource = entry.getValue();
            resource.setPropId(entry.getKey());
            if (resource.getKind()!=null && resource.getKind().isRfcValue()) {
                switch (resource.getKind().getRfcValue()) {
                    case SOUND:
                        Sound sound = toVCardSoundProperty(Sound.class, resource);
                        VCardUtils.addVCardUnmatchedParams(sound,resource);
                        addVCardPropIdParam(sound, resource.getPropId());
                        addVCardX_ABLabel(resource, sound, vcard);
                        vcard.getSounds().add(sound);
                        break;
                    case LOGO:
                        vcard.getLogos().add(toVCardImageProperty(Logo.class, resource, vcard));
                        break;
                    case PHOTO:
                        Photo photo = toVCardPhoto(resource);
                        if (photo == null) continue;
                        VCardUtils.addVCardUnmatchedParams(photo,resource);
                        addVCardPropIdParam(photo, entry.getKey());
                        addVCardX_ABLabel(resource, photo, vcard);
                        vcard.getPhotos().add(photo);
                }
            }
        }
    }

    private void fillVCardPropsFromJSCardDirectories(VCard vcard, Card jsCard) {

        if (jsCard.getDirectories() == null)
            return;

        for (Map.Entry<String,DirectoryResource> entry : jsCard.getDirectories().entrySet()) {

            DirectoryResource resource = entry.getValue();
            resource.setPropId(entry.getKey());
            if (resource.getKind()!=null && resource.getKind().isRfcValue()) {
                switch (resource.getKind().getRfcValue()) {
                    case ENTRY:
                        vcard.getSources().add(toVCardUriProperty(Source.class, resource, vcard));
                        break;
                    case DIRECTORY:
                        OrgDirectory od = toVCardUriProperty(OrgDirectory.class, resource, vcard);
                        od.setIndex(resource.getListAs());
                        vcard.getOrgDirectories().add(od);
                        break;
                }
            }
        }
    }

    private static ezvcard.property.Organization findOrganizationByPropId(List<ezvcard.property.Organization> vcardOrganizations, String jsCardOrgId) {

        if (vcardOrganizations == null || vcardOrganizations.isEmpty())
            return null;

        for (ezvcard.property.Organization vcardOrg : vcardOrganizations) {
            String vcardPropid = vcardOrg.getParameter(VCardParamEnum.PROP_ID.getValue());
            if (vcardPropid!=null && vcardPropid.equalsIgnoreCase(jsCardOrgId))
                return vcardOrg;
        }

        return null;
    }

    private static ezvcard.property.Title toVCardTitle(Title jsTitle, VCard vcard) {

        ezvcard.property.Title title = new ezvcard.property.Title(jsTitle.getName());
        if (jsTitle.getOrganization()!=null) {
            ezvcard.property.Organization vcardOrg = findOrganizationByPropId(vcard.getOrganizations(), jsTitle.getOrganization());
            if (vcardOrg!=null) {
                if (vcardOrg.getGroup()==null)
                    vcardOrg.setGroup("G-" + jsTitle.getOrganization());
                title.setGroup(vcardOrg.getGroup());
            }
        }
        VCardUtils.addVCardUnmatchedParams(title, jsTitle);
        return title;
    }

    private static ezvcard.property.Role toVCardRole(Title jsTitle, VCard vcard) {

        ezvcard.property.Role role = new ezvcard.property.Role(jsTitle.getName());
        if (jsTitle.getOrganization()!=null) {
            ezvcard.property.Organization vcardOrg = findOrganizationByPropId(vcard.getOrganizations(), jsTitle.getOrganization());
            if (vcardOrg!=null) {
                if (vcardOrg.getGroup()==null)
                    vcardOrg.setGroup("G-" + jsTitle.getOrganization());
                role.setGroup(vcardOrg.getGroup());
            }
        }
        VCardUtils.addVCardUnmatchedParams(role, jsTitle);
        return role;
    }


    private void fillVCardPropsFromJSCardTitles(VCard vcard, Card jsCard) {

        if (jsCard.getTitles() == null)
            return;

        for (Map.Entry<String,Title> entry : jsCard.getTitles().entrySet()) {

            if (jsCard.getLocalizationsPerPath("titles/" + entry.getKey()) == null &&
                jsCard.getLocalizationsPerPath("titles/" + entry.getKey() + "/name") == null) {
                if (entry.getValue().getKind() == null || entry.getValue().getKind().isTitle()) {
                    ezvcard.property.Title title = toVCardTitle(entry.getValue(), vcard);
                    addVCardPropIdParam(title, entry.getKey());
                    vcard.addTitle(title);
                } else {
                    ezvcard.property.Role role = toVCardRole(entry.getValue(), vcard);
                    addVCardPropIdParam(role, entry.getKey());
                    vcard.addRole(role);
                }
            }
            else {
                List<ezvcard.property.Title> titles = new ArrayList<>();
                List<ezvcard.property.Role> roles = new ArrayList<>();

                if (entry.getValue().getKind() == null || entry.getValue().getKind().isTitle()) {
                    ezvcard.property.Title title = toVCardTitle(entry.getValue(), vcard);
                    addVCardPropIdParam(title, entry.getKey());
                    titles.add(title);
                } else {
                    ezvcard.property.Role role = toVCardRole(entry.getValue(), vcard);
                    addVCardPropIdParam(role, entry.getKey());
                    roles.add(role);
                }

                Map<String,JsonNode> localizations = jsCard.getLocalizationsPerPath("titles/"+entry.getKey());
                if (localizations != null) {
                    for (Map.Entry<String, JsonNode> localization : localizations.entrySet()) {
                        if (jsCard.getTitles().get(entry.getKey()).getKind() == null || jsCard.getTitles().get(entry.getKey()).getKind().isTitle()) {
                            ezvcard.property.Title title = toVCardTitle(asJSCardTitle(localization.getValue()), vcard);
                            title.setLanguage(localization.getKey());
                            addVCardPropIdParam(title, entry.getKey());
                            titles.add(title);
                        }
                        else {
                            ezvcard.property.Role role = toVCardRole(asJSCardTitle(localization.getValue()), vcard);
                            role.setLanguage(localization.getKey());
                            addVCardPropIdParam(role, entry.getKey());
                            roles.add(role);
                        }
                    }
                }
                localizations = jsCard.getLocalizationsPerPath("titles/"+entry.getKey()+"/name");
                if (localizations != null) {
                    for (Map.Entry<String,JsonNode> localization : localizations.entrySet()) {
                        if (jsCard.getTitles().get(entry.getKey()).getKind() == null || jsCard.getTitles().get(entry.getKey()).getKind().isTitle()) {
                            ezvcard.property.Title title = new ezvcard.property.Title(localization.getValue().asText());
                            title.setLanguage(localization.getKey());
                            addVCardPropIdParam(title, entry.getKey());
                            titles.add(title);
                        }
                        else {
                            ezvcard.property.Role role = new ezvcard.property.Role(localization.getValue().asText());
                            role.setLanguage(localization.getKey());
                            addVCardPropIdParam(role, entry.getKey());
                            roles.add(role);
                        }
                    }
                }
                vcard.addTitleAlt(titles.toArray(new ezvcard.property.Title[0]));
                vcard.addRoleAlt(roles.toArray(new ezvcard.property.Role[0]));
            }
        }
    }

    private static void fillVCardCategories(VCard vcard, Card jsCard) {

        if (jsCard.getKeywords() == null)
            return;

        vcard.setCategories(jsCard.getKeywords().keySet().toArray(new String[0]));
    }


    private static ezvcard.property.Organization toVCardOrganization(Organization jsOrg) {

        ezvcard.property.Organization org = new ezvcard.property.Organization();
        org.getValues().add((jsOrg.getName()!=null) ? jsOrg.getName() : StringUtils.EMPTY);
        List<String> unitSortAsList = new ArrayList<>();
        List<String> unitNameList = new ArrayList<>();
        if (jsOrg.getUnits()!=null) {
            for (int i=0; i < jsOrg.getUnits().length; i++) {
                unitNameList.add(jsOrg.getUnits()[i].getName());
                if (jsOrg.getUnits()[i].getSortAs()!=null)
                    unitSortAsList.add(jsOrg.getUnits()[i].getSortAs());
            }
            org.getValues().addAll(unitNameList);
        }
//        org.setPref(jsOrg.getPref());
        org.setType(toVCardTypeParam(jsOrg));
        if (jsOrg.getSortAs()!=null || !unitSortAsList.isEmpty()) {
            List<String> sortAs = new ArrayList<>();
            sortAs.add((jsOrg.getSortAs()!=null) ? jsOrg.getSortAs() : StringUtils.EMPTY);
            sortAs.addAll(unitSortAsList);
            org.setSortAs(String.join(DelimiterUtils.COMMA_ARRAY_DELIMITER,sortAs));
        }
        VCardUtils.addVCardUnmatchedParams(org, jsOrg);
        return org;
    }


    private void fillVCardOrganizations(VCard vcard, Card jsCard) {

        if (jsCard.getOrganizations() == null)
            return;

        for (Map.Entry<String,Organization> entry : jsCard.getOrganizations().entrySet()) {

            if (jsCard.getLocalizationsPerPath("organizations/"+entry.getKey()) == null &&
                jsCard.getLocalizationsPerPath("organizations/"+entry.getKey()+"/name")==null &&
                jsCard.getLocalizationsPerPath("organizations/"+entry.getKey()+"/units")==null) {
                ezvcard.property.Organization org = toVCardOrganization(entry.getValue());
                addVCardPropIdParam(org, entry.getKey());
                vcard.getOrganizations().add(org);
            }
            else {
                List<ezvcard.property.Organization> organizations = new ArrayList<>();
                ezvcard.property.Organization org = toVCardOrganization(entry.getValue());
                addVCardPropIdParam(org, entry.getKey());
                organizations.add(org);

                Map<String,JsonNode> localizations = jsCard.getLocalizationsPerPath("organizations/"+entry.getKey());
                if (localizations != null) {
                    for (Map.Entry<String, JsonNode> localization : localizations.entrySet()) {
                        org = toVCardOrganization(asJSCardOrganization(localization.getValue()));
                        org.setLanguage(localization.getKey());
                        addVCardPropIdParam(org, entry.getKey());
                        organizations.add(org);
                   }
                }
                localizations = jsCard.getLocalizationsPerPath("organizations/"+entry.getKey()+"/name");
                if (localizations != null) {
                    for (Map.Entry<String,JsonNode> localization : localizations.entrySet()) {
                        ArrayNode units = (ArrayNode) jsCard.getLocalization(localization.getKey(),"organizations/"+entry.getKey()+"/units");
                        org = new ezvcard.property.Organization();
                        org.getValues().add(localization.getValue().asText());
                        if (units!=null)
                            org.getValues().addAll(Arrays.asList(asJSCardOrgUnitValuesArray(units)));
                        org.setLanguage(localization.getKey());
                        addVCardPropIdParam(org, entry.getKey());
                        organizations.add(org);
                    }
                }
                localizations = jsCard.getLocalizationsPerPath("organizations/"+entry.getKey()+"/units"); // check for only org units localized
                if (localizations != null) {
                    for (Map.Entry<String,JsonNode> localization : localizations.entrySet()) {
                        if (jsCard.getLocalization(localization.getKey(),"organizations/"+entry.getKey()+"/name") != null)
                            continue; //skip because already done
                        org = new ezvcard.property.Organization();
                        org.getValues().add(StringUtils.EMPTY);
                        org.getValues().addAll(Arrays.asList(asJSCardOrgUnitValuesArray(localization.getValue())));
                        org.setLanguage(localization.getKey());
                        addVCardPropIdParam(org, entry.getKey());
                        organizations.add(org);
                    }
                }
                vcard.addOrganizationAlt(organizations.toArray(new ezvcard.property.Organization[0]));
            }
        }
    }


    private static ezvcard.property.Note toVCardNote(Note jsNote) {

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


    private void fillVCardNotes(VCard vcard, Card jsCard) {

        if (jsCard.getNotes() == null)
            return;

        for (Map.Entry<String,Note> entry : jsCard.getNotes().entrySet()) {

            if (jsCard.getLocalizationsPerPath("notes/"+entry.getKey()) == null &&
                    jsCard.getLocalizationsPerPath("notes/"+entry.getKey()+"/note")==null) {
                ezvcard.property.Note note = toVCardNote(entry.getValue());
                addVCardPropIdParam(note, entry.getKey());
                vcard.addNote(note);
            }
            else {
                List<ezvcard.property.Note> notes = new ArrayList<>();
                ezvcard.property.Note note = toVCardNote(entry.getValue());
                addVCardPropIdParam(note, entry.getKey());
                notes.add(note);

                Map<String,JsonNode> localizations = jsCard.getLocalizationsPerPath("notes/"+entry.getKey());
                if (localizations != null) {
                    for (Map.Entry<String, JsonNode> localization : localizations.entrySet()) {
                        note = toVCardNote(asJSCardNote(localization.getValue()));
                        note.setLanguage(localization.getKey());
                        addVCardPropIdParam(note, entry.getKey());
                        notes.add(note);
                    }
                }
                localizations = jsCard.getLocalizationsPerPath("notes/"+entry.getKey()+"/note");
                if (localizations != null) {
                    for (Map.Entry<String,JsonNode> localization : localizations.entrySet()) {
                        note = new ezvcard.property.Note(localization.getValue().asText());
                        note.setLanguage(localization.getKey());
                        addVCardPropIdParam(note, entry.getKey());
                        notes.add(note);
                    }
                }
                vcard.addNoteAlt(notes.toArray(new ezvcard.property.Note[0]));
            }
        }
    }

    private static Related toVCardRelated(String uriOrText, List<RelationType> types) {

        Related related = toVCardRelated(uriOrText);
        for(RelationType type : types) {
            if (type.getRfcValue() != null)
                related.getTypes().add(RelatedType.get(type.getRfcValue().getValue()));
        }

        return related;
    }


    private static Related toVCardRelated(String uriOrText) {
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

    private static void fillVCardRelations(VCard vcard, Card jsCard) {

        if (jsCard.getRelatedTo() == null)
            return;

        for (String key : jsCard.getRelatedTo().keySet()) {
            if (jsCard.getRelatedTo().get(key).getRelation() == null)
                vcard.addRelated(toVCardRelated(key));
            else
                vcard.addRelated(toVCardRelated(key, new ArrayList<>(jsCard.getRelatedTo().get(key).getRelation().keySet())));
        }
    }

    private static ClientPidMap toVCardCliendPidMap(String pid, String uri) {

        return new ClientPidMap(Integer.parseInt(pid), uri);
    }

    private void fillVCardExtensions(VCard vcard, Card jsCard) {

        if (jsCard.getVCardProps() == null)
            return;

        for (VCardProp vCardProp : jsCard.getVCardProps()) {

            if (vCardProp.getName().equals(V_Extension.toV_Extension(VCardPropEnum.VERSION.getValue())))
                vcard.setVersion(VCardVersion.valueOfByStr((String) vCardProp.getValue()));
            else if (vCardProp.getName().equals(V_Extension.toV_Extension(VCardPropEnum.CLIENTPIDMAP.getValue()))) {
                String pid = ((String) vCardProp.getValue()).split(",")[0];
                String uri = ((String) vCardProp.getValue()).split(",")[1];
                ClientPidMap pidmap = toVCardCliendPidMap(pid, uri);
                pidmap.setGroup(vCardProp.getGroupParameterValue());
                pidmap.setParameters(vCardProp.getVCardParameters());
                vcard.addClientPidMap(pidmap);
            }
            else if (vCardProp.getName().equals(V_Extension.toV_Extension(VCardPropEnum.XML.getValue()))) {
                try {
                    Xml xml = new Xml(((String) vCardProp.getValue()));
                    xml.setGroup(vCardProp.getGroupParameterValue());
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
                    tz = toVCardTimezone((String) vCardProp.getValue());
                tz.setGroup(vCardProp.getGroupParameterValue());
                tz.setParameters(vCardProp.getVCardParameters());
                vcard.setTimezone(tz);
            }
            else if (vCardProp.getName().equals(V_Extension.toV_Extension(VCardPropEnum.GEO.getValue()))) {
                Geo geo = new Geo(toVCardGeoUri((String) vCardProp.getValue()));
                geo.setGroup(vCardProp.getGroupParameterValue());
                geo.setParameters(vCardProp.getVCardParameters());
                vcard.setGeo(geo);
            }
            else {
                RawProperty property = new RawProperty(vCardProp.getName().toString().toUpperCase(), vCardProp.getValue().toString(), vCardProp.getType());
                property.setGroup(vCardProp.getGroupParameterValue());
                property.setParameters(vCardProp.getVCardParameters());
                vcard.addProperty(property);
            }
        }
    }

    //TODO: replace XXXX with RFC number after draft-ietf-calext-vcard-jscontact-extensions
    private void fillVCardRFCXXXXProps(VCard vCard, Card jsCard) {

        if (jsCard.getCreated() != null) {
            vCard.addExtendedProperty(VCardPropEnum.CREATED.getValue(), VCardDateFormat.UTC_DATE_TIME_BASIC.format(jsCard.getCreated().getTime()), VCardDataType.TIMESTAMP);
        }

        if (jsCard.getLanguage() != null) {
            vCard.addExtendedProperty(VCardPropEnum.LANGUAGE.getValue(), jsCard.getLanguage(), VCardDataType.LANGUAGE_TAG);
        }

        if (jsCard.getSpeakToAs()!= null && jsCard.getSpeakToAs().getPronouns() != null) {
            String propertyName = VCardPropEnum.PRONOUNS.getValue();
            String jsonPointer = fakeExtensionsMapping.get(propertyName.toLowerCase());
            Map<String,Pronouns> pronouns = jsCard.getSpeakToAs().getPronouns();
            for (Map.Entry<String,Pronouns> entry : pronouns.entrySet()) {
                RawProperty raw = new RawProperty(propertyName, entry.getValue().getPronouns());
                String vCardTypeValue = toVCardTypeParam(entry.getValue());
                if (vCardTypeValue!=null)
                    raw.setParameter(VCardParamEnum.TYPE.getValue(), vCardTypeValue);
                if (entry.getValue().getPref()!=null)
                    raw.setParameter(VCardParamEnum.PREF.getValue(), entry.getValue().getPref().toString());
                raw.setDataType(VCardDataType.TEXT);
                addVCardPropIdParam(raw,entry.getKey());
                vCard.addProperty(raw);

                jsonPointer = String.format("%s/%s", jsonPointer, entry.getKey());
                Map<String,JsonNode> localizations = jsCard.getLocalizationsPerPath(jsonPointer);
                if (localizations != null) {
                    for (Map.Entry<String,JsonNode> entry2 : localizations.entrySet()) {
                        RawProperty raw2 = new RawProperty(propertyName, asJSCardPronouns(entry2.getValue()).getPronouns());
                        raw2.setParameter(VCardParamEnum.LANGUAGE.getValue(), entry2.getKey());
                        raw2.setDataType(VCardDataType.TEXT);
                        vCard.addProperty(raw2);
                    }
                }
            }
        }

        if (jsCard.getSpeakToAs()!= null && jsCard.getSpeakToAs().getGrammaticalGender() != null) {
            String propertyName = VCardPropEnum.GRAMGENDER.getValue();
            String jsonPointer = fakeExtensionsMapping.get(propertyName.toLowerCase());
            vCard.addExtendedProperty(propertyName, jsCard.getSpeakToAs().getGrammaticalGender().getValue().toUpperCase(), VCardDataType.TEXT);
            Map<String,JsonNode> localizations = jsCard.getLocalizationsPerPath(jsonPointer);
            if (localizations != null) {
                for (Map.Entry<String,JsonNode> entry : localizations.entrySet()) {
                    RawProperty raw = new RawProperty(propertyName, entry.getValue().asText());
                    raw.setParameter(VCardParamEnum.LANGUAGE.getValue(),entry.getKey());
                    raw.setDataType(VCardDataType.TEXT);
                    vCard.addProperty(raw);
                }
            }
        }

    }

    private void fillVCardPropsFromJSCardExtensions(VCard vcard, Card jsCard) {

        Map<String, Object> allExtensionsMap = new HashMap<>();
        jsCard.buildAllExtensionsMap(allExtensionsMap,StringUtils.EMPTY);

        for(Map.Entry<String,Object> entry : allExtensionsMap.entrySet()) {
            try {
                RawProperty property = new RawProperty(VCardPropEnum.JSPROP.getValue(), JSContactPropUtils.toJSContactPropValue(entry.getValue()), VCardDataType.TEXT);
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
        vCard.setUid(toVCardUid(jsCard.getUid()));
        vCard.setKind(toVCardKind(jsCard.getKind()));
        vCard.setProductId(jsCard.getProdId());
        vCard.setRevision(toVCardRevision(jsCard.getUpdated()));
        fillVCardMembers(vCard, jsCard);
        fillVCardFormattedNames(vCard, jsCard);
        fillVCardNames(vCard, jsCard);
        fillVCardNickNames(vCard, jsCard);
        fillVCardAddresses(vCard, jsCard);
        fillVCardAnniversaries(vCard, jsCard);
        fillVCardPropsFromJSCardPersonalInfos(vCard, jsCard);
        fillVCardLanguages(vCard, jsCard);
        fillVCardTelephones(vCard, jsCard);
        fillVCardEmails(vCard, jsCard);
        fillVCardCalendarRequestUris(vCard,jsCard);
        fillVCardPropsFromJSCardOnlineServices(vCard, jsCard);
        fillVCardPropsFromJSCardCalendars(vCard, jsCard);
        fillVCardKeys(vCard, jsCard);
        fillVCardContactUris(vCard, jsCard);
        fillVCardPropsFromJSCardMedia(vCard, jsCard);
        fillVCardPropsFromJSCardDirectories(vCard, jsCard);
        fillVCardOrganizations(vCard, jsCard);
        fillVCardPropsFromJSCardTitles(vCard, jsCard);
        fillVCardCategories(vCard, jsCard);
        fillVCardNotes(vCard, jsCard);
        fillVCardRelations(vCard, jsCard);
        fillVCardRFCXXXXProps(vCard, jsCard);
        fillVCardExtensions(vCard, jsCard);
        fillVCardPropsFromJSCardExtensions(vCard,jsCard);

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

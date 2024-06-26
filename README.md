# jscontact-tools

Java tools for **JSContact** [RFC9553](https://datatracker.ietf.org/doc/RFC9553/) creation, validation, serialization/deserialization and conversion from and to vCard 4.0 [RFC6350](https://datatracker.ietf.org/doc/rfc6350/), xCard [RFC6351](https://datatracker.ietf.org/doc/rfc6351/) and jCard [RFC7095](https://datatracker.ietf.org/doc/rfc7095/).
Conversion from JSContact to vCard formats and viceversa is based on the rules described in [RFC9555](https://datatracker.ietf.org/doc/RFC9555/)
Validation and conversion of vCard formats leverage the features provided by [ez-vcard](https://github.com/mangstadt/ez-vcard) Java library.


# Maven/Gradle

## Maven

```
      <dependency>
		  <groupId>it.cnr.iit.jscontact</groupId>
		  <artifactId>jscontact-tools</artifactId>
		  <version>1.0.1</version>
      </dependency>
```

## Gradle

```
  compile 'it.cnr.iit.jscontact:jscontact-tools:1.0.1'
```

# Features
1. [Creation](#creation)
2. [Validation](#validation)
3. [Serialization/Deserialization](#serialization-deserialization)
4. [Localization](#localization)
5. [vCard Conversion](#vcard-conversion)
6. [JSContact Conversion](#jscontact-conversion)
7. [Using JSContact in RDAP](#using-jscontact-in-rdap)
8. [Testing](#testing)
9. [ez-vcard extensions](#ez-vcard-extensions)
10. [ez-vcard bugs](#ez-vcard-bugs)
11. [JSContact Compliance](#jscontact-compliance)
12. [References](#references)


<a name="creation"></a>
## Creation

### Builders

Object creation is achieved through builders.
In addition to the class builders, the following collections builders are provided:

* ContextsBuilder
* AddressContextsBuilder
* PhoneFeaturesBuilder
* NameComponentsBuilder
* AddressComponentsBuilder

Simplest maps can be loaded by putting entries singularly.
Here in the following a successful creation of an EmailAddress instance is shown.
 
```

        EmailAddress email = EmailAddress.builder()
                                        .address("mario.loffredo@iit.cnr.it")
                                        .contexts(ContextsBuilder.builder().work().private_().build())
                                        .build();

```

The build method throws the `java.lang.NullPointerException` when a required property is missing.
Here in the following an unsuccessful creation of an `EmailAddress` instance is shown.

```

        // address is missing in EmailAddress
        EmailAddress.builder().contexts(ContextsBuilder.builder().work().build()).build();

```

### Cloning

Creation can be achieved through cloning as well.
Cloning can be only applied to Card.

Here in the following a test assessing a successful creation of a cloned Card instance.
 
```

    @Test
    public void testClone1() throws IOException {

        String json = IOUtils.toString(Objects.requireNonNull(getClass().getClassLoader().getResourceAsStream("jcard/jsCard-Multilingual.json")), StandardCharsets.UTF_8);
        Card jsCard = Card.toCard(json);
        assertTrue("testClone1", jsCard.equals(jsCard.clone()));

    }

```

<a name="validation"></a>
## Validation

### JSContact Card validation

Even if a JSContact **Card** is correctly created by builder, it might need to be validated as it were obtained from an external producer through deserialization.
Validation is performed on JSContact Card by invoking the method `isValid`.
This method returns a boolean value: `true` if the object satisfies all the constraints included in [RFC9553], `false` otherwise.
If the validation process doesn't end successfully, the list of error messages can be obtained by calling the `getValidationMessages` method.  
Here in the following a method testing an unsuccessfully ended validation is shown.

```

    @Test
    public void testInvalidCountryCode() {
        
        // a contry code must be two-character long
        Map addresses = new HashMap<String,Address>() {{ put("ADR-1", Address.builder()
                                                                             .countryCode("ita")
                                                                             .build());
                                                      }};
        Card jsCard = Card.builder()
                .uid(getUUID())
                .addresses(addresses)
                .build();
        assertTrue("testInvalidCountryCode-1", !jsCard.isValid());
        assertTrue("testInvalidCountryCode-2", jsCard.getValidationMessage().equals("invalid countryCode in Address"));
    }
        
```

### vCard validation

Validation of all vCard formats is supported as well.
At present, the following validation methods are available:

*   JCardValidator
    *   void validate(String json)
    *   void validate(JsonNode jsonNode) 
*   VCardValidator
    *   void validate(String vcf)
*   XCardValidator
    *   void validate(String xml)

All the methods can raise a `CardException`.
`JsonNode` represents the root node in Jackson library (`com.fasterxml.jackson.databind.JsonNode`).

With regard to UTCDateTime type validation, values including trailing zeros in fractional second values are not signaled as invalid but automatically converted in values not including milliseconds (e.g. "2010-10-10T10:10:10.000Z" is encoded and serialized as "2010-10-10T10:10:10Z") 

<a name="serialization-deserialization"></a>
## Serialization/Deserialization

JSContact Card serialization/deserializaion is performed through Jackson library annotations.

## Serialization

```

        Card jsCard = Card.builder.build();
        String serialized = mapper.writeValueAsString(jsCard);

```

To pretty print serialized Card objects, use the following: 

```

        Card jsCard = Card.builder.build();
        String serialized = PrettyPrintSerializer.print(jsCard);

```

## Deserialization

```

        String json = "{"uid": \"urn:uuid:c642b718-7c89-49f4-9497-d9fb279bb437\"}";
        ObjectMapper mapper = new ObjectMapper();
        Card jsCard = mapper.readValue(json, Card.class);

```

<a name="localization"></a>
## Localization

Localizations in JSContact are implemented through the `localizations` map in the Card object.

At present, the following methods support localizations handling:

*   void addLocalization(String language, String path, JsonNode object)
    
    This method allows for adding a new localization to the `localizations` map.
    The `path`  parameter is the JSON pointer [RFC6901](https://datatracker.ietf.org/doc/rfc6901/) to the property being localized.
    The `object`  parameter represents the JSON object in Jackson library.

*   Map<String,JsonNode> getLocalizationsPerPath(String path)
    
    This method returns all the localizations for a given path.
    The keys of the returned map are the language tags of the related localizations.

*   Map<String,JsonNode> getLocalizationsPerLanguage(String language)
    
    This method returns all the localizations for a given language.
    The keys of the returned map are the paths of the related localizations.

*   JsonNode getLocalization(String language, String path)
    
    This method returns a localization object for a given  <language,path> pair.

<a name="vcard-conversion"></a>
## vCard Conversion

At present, the following converting methods are available:

*   EZVCard2JSContact
    *   List<Card> convert(VCard... vcard)
*   VCard2JSContact
    *   List<Card> convert(String vcf)
*   JCard2JSContact
    *   List<Card> convert(String json)
    *   List<Card> convert(JsonNode jsonNode) 
*   XCard2JSContact
    *   List<Card> convert(String xml)

All the methods return a list of JSContact Card objects and can raise a `CardException`.
`VCard` is the class mapping a vCard in ez-vcard Java library.
`JsonNode` represents the root node in Jackson library (`com.fasterxml.jackson.databind.JsonNode`).

### Conversion Rules from vCard to JSContact Card

The conversion is executed according to the following rules:

1. The conversion is based on the content of the [JSContact RFCs](#jscontact-rfcs).

2. The card components (i.e. properties, parameters or values) considered in the [Other RFCs](#other-rfcs) as well as the additonal components defined in [RFC9554](https://datatracker.ietf.org/doc/RFC9554/) are matched.

3. An unmatched property is converted into an entry of the topmost Card `vCardProps` map. The following unmatched properties are considered:    
    CLIENTPIDMAP
    VERSION
    XML

4. An unmatched parameter is converted into an entry of a `vCardParams` map. The following unmatched parameters
   are considered:
   PID
   GROUP

5. Validation is performed before conversion if the  `validateCard` configuration property is set to `true`.

6. Default values for the configuration properties are:

    - `customTimeZonesPrefix = "tz"`
    - `validateCard = true`
    - `usePropIds = true`
    - `setAutoFullAddress = true`
    - `setAutoMediaType = true`
    - `convertGenderToSpeakToAs = true`

7. The sex information of the GENDER property can be mapped to the SpeakToAs object if GRAMGENDER is missing and
   if the `convertGenderToSpeakToAs` configuration value is set to true as in the following:

   GENDER SpeakToAs.grammaticalGender    
   M masculine
   F feminine
   O animate
   N common
   U SpeakToAs = null

8. Where a language is required to represent a localization and the language is not specified, `en` is used by default
   to set the `defaultLanguage` mapping configuration parameter.

9. Regardless of their positions inside the vCard, properties mapped as Anniversary objects appear in the following
   order:

   1. BDAY (BIRTHDATE)
   2. DEATHDAY (DEATHDATE)
   3. ANNIVERSARY

10. Regardless of their positions inside the vCard, properties mapped as PersonalInfo objects appear in the following
    order:

    1. HOBBY
    2. INTEREST
    3. EXPERTISE

11. Regardless of their positions inside the vCard, properties mapped as Media objects appear in the following
    order:

    1. PHOTO
    2. SOUND
    3. LOGO

12. Regardless of their positions inside the vCard, properties mapped as Calendar objects appear in the
    following order:

    1. CALURI
    2. FBURL

13. Regardless of their positions inside the vCard, properties mapped as Link objects appear in the following
    order:

    1. URL
    2. CONTACT-URI

14. Regardless of their positions inside the vCard, properties mapped as Directory objects appear in the
    following order:

    1. SOURCE
    2. ORG-DIRECTORY

15. Regardless of their positions inside the vCard, properties mapped as Title objects appear in the following order:

    1. TITLE
    2. ROLE

16. If an ADR element doesn't include the LABEL parameter, based on the value of `setAutoFullAddress` mapping
    configuration parameter , the full address results from the newline-delimited concatenation of the non-empty
    address components.

17. Categories appear in the "keywords" map according to the values of the PREF parameter of the CATEGORIES properties.

18. Members appear in the "members" map according to the values of the PREF parameter of the MEMBER properties.

19. JSContact UTCDateTime type is mapped to Java Calendar.

20. Media type information of `Media` objects is automatically detected when the MEDIATYPE parameter is missing.

21. A custom time zone (i.e. a time zone including non-zero minutes or non-IANA time zone) is transformed into
    a `customTimeZones` map entry whose key is prefixed the `customTimeZonesPrefix` configuration property concatenated
    with an incremental positive integer (e.g. "\tz1")

22. The TZ and GEO properties can be associated to an ADR property by grouping them together through the group
    construct.

23. Either the "ISO-3166-1-alpha-2" parameter (that maybe used in RDAP) is converted.

24. FN property with DERIVED parameter set to true is not converted if a corresponding N property exists.

### Conversion Profiles from vCard to JSContact Card

By default, where a collection of objects is mapped to a map of <key,object> entries, the key has the following format: <vCard Element Tag> + "-" + <index of the element among the vCard sibling elements (starting from 1)> (e.g. "ADR-1")
This setting schema can be modified by defining a different one assigning key values based on the positions of vCard elements.
To do that, the following steps must be followed:

1. set the `usePropIds` property of the `VCard2JSContactConfig` object to `false`

2. create a `VCard2JSContactIdsProfile` object and assign the `idsProfileToUse` of `VCard2JSContactConfig` object property with it


### RDAP Conversion Profile from jCard to JSContact Card

A pre-defined conversion profile to convert a jCard instance inside an RDAP response [RFC9083](https://datatracker.ietf.org/doc/rfc9083/) is available.
The values of the map keys used in such profile are defined in [draft-ietf-regext-rdap-jscontact](https://datatracker.ietf.org/doc/draft-ietf-regext-rdap-jscontact/).
Additional setting rules are shown in the class RdapJSContactIdsProfile.

<a name="jscontact-conversion"></a>
## JSContact Card Conversion

At present, the following converting methods are available:

*   JSContact2EZVCard
    *   List<VCard> convert(Card... jsContacts)
    *   List<VCard> convert(String json)
*   JSContact2VCard
    *   String convertToText(Card... jsContact)
*   JSContact2JCard
    *   String convertToJson(Card... jsContact)
    *   JsonNode convertToJsonNode(Card... jsContact)
*   JSContact2XCard
    *   String convertToXml(Card... jsContact)

All the methods take in input a list of JSContact Card objects and can raise the `CardException` exception.
`VCard` is the class mapping a vCard in ez-vcard Java library.
`JsonNode` represents the root node in Jackson library (`com.fasterxml.jackson.databind.JsonNode`).

### Conversion Rules from JSContact Card to vCard 

1. The conversion is based on the content of the [JSContact RFCs](#jscontact-rfcs).
 
2. An entry of the topmost Card/CardGroup `vCardProps` map is converted into the related vCard property  . The following properties are considered:
    CLIENTPIDMAP
    XML
 
3. An entry of an object `vCardParams` map is converted into a vCard parameter. The following parameters are considered:
    PID

4. Default values for the configuration properties are:

    - `validateCard = true`
    - `setAutoAddrLabel = true`
    - `setPropIdParam = true`
    - `convertTimezoneToOffset = true`

5. The "timeZone" property can be mapped to either a TZ parameter or the TZ property either preserving the time zone name or the time zone offset extracted from the `customTimeZones` map. Time zone names in the format "Etc/GMT(+|-).." can be mapped to offsets based on the value of mapping configuration parameter `convertTimezoneToOffset`    

6. If the "name.full" property is missing, the FN value is generated starting from the "name" property. The name components are separated by the "separator" value if present, space otherwise. If the "name" property is missing as well, the FN value is set to the "uid" property.

7. The "street" component of ADR property results from the concatenation of "district", "block", "name", "number" and "direction" non-empty values presented in the "street" member of the "Address" object. Such values are separated by the "defaultSeparator"/"separator" value if present, comma otherwise.

8. The "extension" component of ADR property results from the concatenation of "building", "floor", "apartment", "room", "landmark"
   and "extention" non-empty values presented in the "components" member of the "Address" object. Such values are separated
   by the "defaultSeparator"/"separator" value if present, comma otherwise.

9. The LABEL parameter of the ADR property is equal to the "full" property of the "Address" object. If the full
   address is missing, based on the value of mapping configuration parameter `setAutoAddrLabel`, the value of the LABEL
   parameter can result from the newline-delimited concatenation of the non-empty "Address" members or.

10. The "PROP-ID" parameter can be mapped to the value of a map key based on the value of the `setPropIdParam` mapping 
    configuration parameter.

10. The "countryCode" member of the Address type always converts to the vCard CC parameter.

### Conversion examples

Here in the following two examples of conversion between vCard and JSContact Card object.

```

    @Test
    public void testAddresses4() throws IOException, CardException {

        String vcard = "BEGIN:VCARD\n" +
                "VERSION:4.0\n" +
                "FN:test\n" +
                "GROUP1.ADR;CC=US:;;54321 Oak St;Reston;VA;20190;USA\n" +
                "GROUP1.GEO:geo:46.772673,-71.282945\n" +
                "END:VCARD";

        Card jsCard = vCard2JSContact.convert(vcard).get(0);
        assertNotNull("testAddresses4 - 1", jsCard.getAddresses());
        assertEquals("testAddresses4 - 2", 1, jsCard.getAddresses().size());
        assertEquals("testAddresses4 - 3", "US", jsCard.getAddresses().get("ADR-1").getCountryCode());
        assertEquals("testAddresses4 - 4", "USA", jsCard.getAddresses().get("ADR-1").getCountry());
        assertEquals("testAddresses4 - 5", "20190", jsCard.getAddresses().get("ADR-1").getPostcode());
        assertEquals("testAddresses4 - 6", "Reston", jsCard.getAddresses().get("ADR-1").getLocality());
        assertEquals("testAddresses4 - 7", "VA", jsCard.getAddresses().get("ADR-1").getRegion());
        assertEquals("testAddresses4 - 8", "54321 Oak St", jsCard.getAddresses().get("ADR-1").getStreetDetails());
        assertEquals("testAddresses4 - 9", "54321 Oak St\nReston\nVA\n20190\nUSA", jsCard.getAddresses().get("ADR-1").getFullAddress());
        assertEquals("testAddresses4 - 10", "geo:46.772673,-71.282945", jsCard.getAddresses().get("ADR-1").getCoordinates());

    }

```

```

    @Test
    public void testJCardGroup1() throws IOException, CardException {

        String jcard="[" +
                "[\"vcard\", [ " +
                "[\"version\", {}, \"text\", \"4.0\"], " +
                "[\"kind\", {}, \"text\", \"group\"], " +
                "[\"fn\", {}, \"text\", \"The Doe family\"], " +
                "[\"member\", {}, \"uri\", \"urn:uuid:03a0e51f-d1aa-4385-8a53-e29025acd8af\"], " +
                "[\"member\", {}, \"uri\", \"urn:uuid:b8767877-b4a1-4c70-9acc-505d3819e519\"] " +
                "]] ,"  +
                "[\"vcard\", [ " +
                "[\"version\", {}, \"text\", \"4.0\"], " +
                "[\"fn\", {}, \"text\", \"John Doe\"], " +
                "[\"uid\", {}, \"uri\", \"urn:uuid:03a0e51f-d1aa-4385-8a53-e29025acd8af\"] " +
                "]] ,"  +
                "[\"vcard\", [ " +
                "[\"version\", {}, \"text\", \"4.0\"], " +
                "[\"fn\", {}, \"text\", \"Jane Doe\"], " +
                "[\"uid\", {}, \"uri\", \"urn:uuid:b8767877-b4a1-4c70-9acc-505d3819e519\"] " +
                "]]" +
                "]";

        List<Card> jsCards = jCard2JSContact.convert(jcard);
        assertEquals("testJCardGroup1 - 1", 3, jsCards.size());
        Card jsCardGroup = jsCards.get(0);
        assertTrue("testJCardGroup1 - 2", jsCardGroup.getCard().getKind().isGroup());
        assertTrue("testJCardGroup1 - 3",StringUtils.isNotEmpty(jsCardGroup.getUid()));
        assertEquals("testJCardGroup1 - 4", "The Doe family", jsCardGroup.getCard().getName().getFull());
        assertEquals("testJCardGroup1 - 5", 2, jsCardGroup.getMembers().size());
        assertSame("testJCardGroup1 - 6", jsCardGroup.getMembers().get("urn:uuid:03a0e51f-d1aa-4385-8a53-e29025acd8af"), Boolean.TRUE);
        assertSame("testJCardGroup1 - 7", jsCardGroup.getMembers().get("urn:uuid:b8767877-b4a1-4c70-9acc-505d3819e519"), Boolean.TRUE);
        Card jsCard = jsCards.get(1);
        assertEquals("testJCardGroup1 - 8", "urn:uuid:03a0e51f-d1aa-4385-8a53-e29025acd8af", jsCard.getUid());
        assertEquals("testJCardGroup1 - 9", "John Doe", jsCard.getName().getFull());
        jsCard = jsCards.get(2);
        assertEquals("testJCardGroup1 - 10", "urn:uuid:b8767877-b4a1-4c70-9acc-505d3819e519", jsCard.getUid());
        assertEquals("testJCardGroup1 - 11", "Jane Doe", jsCard.getName().getFull());
        
    }

```

Here in the following two examples of conversion between JSContact Card and a vCard.

```

    @Test
    public void testAddresses4() throws IOException, CardException {

        String jscard = "{" +
                "\@type\": \"Card\","
                "\"uid\":\"urn:uuid:7e0636f5-e48f-4a32-ab96-b57e9c07c7aa\"," +
                "\"name\":{\"full\":\"test\"}," +
                "\"addresses\":{" +
                    "\"ADR-1\": {" +
                        "\"components\":[ " +
                           "{\"kind\":\"name\",\"value\":\"54321 Oak St\"}," +
                           "{\"kind\":\"locality\",\"value\":\"Reston\"}," +
                           "{\"kind\":\"region\",\"value\":\"VA\"}," +
                           "{\"kind\":\"country\",\"value\":\"USA\"}," +
                           "{\"kind\":\"postcode\",\"value\":\"20190\"}" +
                        "]," +
                        "\"countryCode\":\"US\"," +
                        "\"coordinates\":\"geo:46.772673,-71.282945\"" +
                    "}" +
                "}" +
                "}";
                
        VCard vcard = jsContact2VCard.convert(jscard).get(0);
        assertEquals("testAddresses4 - 1", 1, vcard.getAddresses().size());
        assertEquals("testAddresses4 - 2", "US", vcard.getAddresses().get(0).getParameter("CC"));
        assertEquals("testAddresses4 - 3", "USA", vcard.getAddresses().get(0).getCountry());
        assertEquals("testAddresses4 - 4", "20190", vcard.getAddresses().get(0).getPostalCode());
        assertEquals("testAddresses4 - 5", "Reston", vcard.getAddresses().get(0).getLocality());
        assertEquals("testAddresses4 - 6", "VA", vcard.getAddresses().get(0).getRegion());
        assertEquals("testAddresses4 - 7", "54321 Oak St", vcard.getAddresses().get(0).getStreetAddress());
        assertEquals("testAddresses4 - 8", "54321 Oak St\nReston\nVA\n20190\nUSA", vcard.getAddresses().get(0).getLabel());
        assertEquals("testAddresses4 - 9", vcard.getAddresses().get(0).getGeo(), GeoUri.parse("geo:46.772673,-71.282945"));           
        assertEquals("testAddresses4 - 10", "ADR-1", vcard.getAddresses().get(0).getParameter(VCardParamEnum.PROP_ID.getValue()));
        
    }

```

```

    @Test
    public void testCardGroup1() throws IOException, CardException {

        String jsCards = "[" +
                         "{" +
                             "\@type\": \"Card\","
                             "\"uid\":\"urn:uuid:2feb4102-f15f-4047-b521-190d4acd0d29\"," +
                             "\"kind\":\"group\"," +
                             "\"name\":{\"full\":\"The Doe family\"}," +
                             "\"members\": {" +
                                "\"urn:uuid:03a0e51f-d1aa-4385-8a53-e29025acd8af\":true," +
                                "\"urn:uuid:b8767877-b4a1-4c70-9acc-505d3819e519\":true" +
                             "}" +
                        "}," +
                        "{" +
                            "\@type\": \"Card\","
                            "\"uid\":\"urn:uuid:03a0e51f-d1aa-4385-8a53-e29025acd8af\"," +
                             "\"name\":{\"full\":\"John Doe\"}" +
                        "}," +
                        "{" +
                             "\@type\": \"Card\","
                            "\"uid\":\"urn:uuid:b8767877-b4a1-4c70-9acc-505d3819e519\"," +
                             "\"name\":{\"full\":\"Jane Doe\"}" +
                        "}" +
                        "]";

        List<VCard> vcards = jsContact2VCard.convert(jsCards);
        assertEquals("testCardGroup1 - 1", 3, vcards.size());
        assertTrue("testCardGroup1 - 3", vcards.get(0).getKind().isGroup());
        assertTrue("testCardGroup1 - 4",StringUtils.isNotEmpty(vcards.get(0).getUid().getValue()));
        assertEquals("testCardGroup1 - 5", "The Doe family", vcards.get(0).getFormattedName().getValue());
        assertEquals("testCardGroup1 - 6", 2, vcards.get(0).getMembers().size());
        assertEquals("testCardGroup1 - 7", vcards.get(0).getMembers().get(0).getUri().equals("urn:uuid:03a0e51f-d1aa-4385-8a53-e29025acd8af"), Boolean.TRUE);
        assertEquals("testCardGroup1 - 8", vcards.get(0).getMembers().get(1).getUri().equals("urn:uuid:b8767877-b4a1-4c70-9acc-505d3819e519"), Boolean.TRUE);
        assertEquals("testCardGroup1 - 9", "urn:uuid:03a0e51f-d1aa-4385-8a53-e29025acd8af", vcards.get(1).getUid().getValue());
        assertEquals("testCardGroup1 - 10", "John Doe", vcards.get(1).getFormattedName().getValue());
        assertEquals("testCardGroup1 - 11", "urn:uuid:b8767877-b4a1-4c70-9acc-505d3819e519", vcards.get(2).getUid().getValue());
        assertEquals("testCardGroup1 - 12", "Jane Doe", vcards.get(2).getFormattedName().getValue());
    }

```

### VCard Reading/Writing

VCards can be parsed/written through the methods of the VCardParser/VCardWiter classes which make use of the encoding scheme descirbed in [RFC6868](https://datatracker.ietf.org/doc/rfc6868/).

<a name="using-jscontact-in-rdap"></a>
## Using JSContact in RDAP

Using JSContact in RDAP is supported through JSContactForRdapBuilder and JSContactForRdapGetter classes as it is shown in the following example.
The uid property is set to a random value by default.
My apologizes for the misuse of the Japanese language.

```

    @Test
    public void testJSContactForRdapBuilderAndGetter() throws MissingFieldException, CardException {

        Card jsCard = JSContactForRdapBuilder.builder()
                .name(JSContactNameForRdapBuilder.builder()
                        .full("Mario Loffredo")
                        .surname("Loffredo")
                        .given("Mario")
                        .build())
                .org(".it Registry")
                .email("mario.loffredo@iit.cnr.it")
                .voice("+39.0503139811")
                .fax("+39.0503139800")
                .address(JSContactAddressForRdapBuilder.builder()
                        .cc("it")
                        .country("Italy")
                        .sp("PI")
                        .city("Pisa")
                        .pc("56124")
                        .street("Via Moruzzi, 1")
                        .build())
                .url("https://www.nic.it")
                .nameLoc("jp", JSContactNameForRdapBuilder.builder()
                        .full("マリオ ロフレド")
                        .surname("ロフレド")
                        .given("マリオ")
                        .build())
                .orgLoc("jp", ".itレジストリ")
                .addrLoc("jp", JSContactAddressForRdapBuilder.builder()
                        .cc("it")
                        .country("イタリア")
                        .sp("PI")
                        .city("ピサ")
                        .pc("56124")
                        .street("モルッツィ通り、1")
                        .build())
                .build();

        JSContactForRdapGetter rdapJSContactGetter = JSContactForRdapGetter.of(jsCard);
        assertNotNull("testJSContactForRdapBuilderAndGetter - 1", rdapJSContactGetter.uid());
        JSContactNameForRdapGetter rdapJSContactNameGetter = JSContactNameForRdapGetter.of(rdapJSContactGetter.name());
        assertEquals("testJSContactForRdapBuilderAndGetter - 2", "Mario Loffredo", rdapJSContactNameGetter.full());
        assertEquals("testJSContactForRdapBuilderAndGetter - 3", "Loffredo", rdapJSContactNameGetter.surname());
        assertEquals("testJSContactForRdapBuilderAndGetter - 4", "Mario", rdapJSContactNameGetter.given());
        assertEquals("testJSContactForRdapBuilderAndGetter - 5", ".it Registry", rdapJSContactGetter.org());
        assertEquals("testJSContactForRdapBuilderAndGetter - 6", "mario.loffredo@iit.cnr.it", rdapJSContactGetter.email());
        assertEquals("testJSContactForRdapBuilderAndGetter - 7", "+39.0503139811", rdapJSContactGetter.voice());
        assertEquals("testJSContactForRdapBuilderAndGetter - 8", "+39.0503139800", rdapJSContactGetter.fax());
        assertEquals("testJSContactForRdapBuilderAndGetter - 9", "https://www.nic.it", rdapJSContactGetter.url());
        JSContactAddressForRdapGetter rdapJSContactAddressGetter = JSContactAddressForRdapGetter.of(rdapJSContactGetter.address());
        assertEquals("testJSContactForRdapBuilderAndGetter - 10", "it", rdapJSContactAddressGetter.cc());
        assertEquals("testJSContactForRdapBuilderAndGetter - 11", "Italy", rdapJSContactAddressGetter.country());
        assertEquals("testJSContactForRdapBuilderAndGetter - 12", "PI", rdapJSContactAddressGetter.sp());
        assertEquals("testJSContactForRdapBuilderAndGetter - 13", "Pisa", rdapJSContactAddressGetter.city());
        assertEquals("testJSContactForRdapBuilderAndGetter - 14", "56124", rdapJSContactAddressGetter.pc());
        assertEquals("testJSContactForRdapBuilderAndGetter - 15", "Via Moruzzi, 1", rdapJSContactAddressGetter.street());
        JSContactNameForRdapGetter rdapJSContactNameLocGetter = JSContactNameForRdapGetter.of(rdapJSContactGetter.nameLoc("jp"));
        assertEquals("testJSContactForRdapBuilderAndGetter - 16", "マリオ ロフレド", rdapJSContactNameLocGetter.full());
        assertEquals("testJSContactForRdapBuilderAndGetter - 17", "ロフレド", rdapJSContactNameLocGetter.surname());
        assertEquals("testJSContactForRdapBuilderAndGetter - 18", "マリオ", rdapJSContactNameLocGetter.given());
        assertEquals("testJSContactForRdapBuilderAndGetter - 19", "マリオ", rdapJSContactNameLocGetter.given());
        JSContactAddressForRdapGetter rdapJSContactAddressLocGetter = JSContactAddressForRdapGetter.of(rdapJSContactGetter.addressLoc("jp"));
        assertEquals("testJSContactForRdapBuilderAndGetter - 20", "it", rdapJSContactAddressLocGetter.cc());
        assertEquals("testJSContactForRdapBuilderAndGetter - 21", "イタリア", rdapJSContactAddressLocGetter.country());
        assertEquals("testJSContactForRdapBuilderAndGetter - 22", "PI", rdapJSContactAddressLocGetter.sp());
        assertEquals("testJSContactForRdapBuilderAndGetter - 23", "ピサ", rdapJSContactAddressLocGetter.city());
        assertEquals("testJSContactForRdapBuilderAndGetter - 24", "56124", rdapJSContactAddressLocGetter.pc());
        assertEquals("testJSContactForRdapBuilderAndGetter - 25", "モルッツィ通り、1", rdapJSContactAddressLocGetter.street());
    }

```

<a name="testing"></a>
## Testing

Test cases are executed using [JUnit4](https://junit.org/junit4/) and cover all the features provided.

<a name="ez-vcard-extensions"></a>
## ez-vcard extensions

New scribers and properties have been defined to support the implementation of the extensions to vCard name and address components as dfined in [RFC9554](https://datatracker.ietf.org/doc/RFC9554/).
To parse and write vCard instances having such extensions, the methods provided by ez-vcard in the Ezvcard class cannot be used.
Similar methods considering those extensions have been defined in the classes VCardParser and VCardWriter.

<a name="ez-vcard-bugs"></a>
## ez-vcard bugs

As opposed to what is stated in section 6.1.1 of [RFC6350](https://datatracker.ietf.org/doc/rfc6350/), ez-vcard doesn't
represent both family and given names including multiple text values separated by comma.
In both cases, the text values after the first value are ignored.

<a name="jscontact-compliance"></a>
## JSContact Compliance

This jscontact-tools version is compliant with JSContact specification version -08 and JSContact-vCard mapping version -07.

<a name="references"></a>
## References

<a name="jscontact-rfcs"></a>
### JSContact RFCs

* [RFC9553](https://datatracker.ietf.org/doc/RFC9553/)
* [RFC9554](https://datatracker.ietf.org/doc/RFC9554/)
* [RFC9555](https://datatracker.ietf.org/doc/RFC9555/)

<a name="other-rfcs"></a>
### Other RFCs

* [RFC6350](https://datatracker.ietf.org/doc/rfc6350/)
* [RFC6351](https://datatracker.ietf.org/doc/rfc6351/)
* [RFC6473](https://datatracker.ietf.org/doc/rfc6473/)
* [RFC6474](https://datatracker.ietf.org/doc/rfc6474/)
* [RFC6715](https://datatracker.ietf.org/doc/rfc6715/)
* [RFC6869](https://datatracker.ietf.org/doc/rfc6869/)
* [RFC6901](https://datatracker.ietf.org/doc/rfc6901/)
* [RFC7095](https://datatracker.ietf.org/doc/rfc7095/)
* [RFC8605](https://datatracker.ietf.org/doc/rfc8605/)
* [RFC9083](https://datatracker.ietf.org/doc/rfc9083/)

<a name="drafts"></a>
### Drafts

* [draft-ietf-regext-rdap-jscontact](https://datatracker.ietf.org/doc/draft-ietf-regext-rdap-jscontact/)

# Build Instructions

## Java

jscontact-tools can be run on Java 8 or 11.

## Maven

jscontact-tools uses [Maven](http://maven.apache.org/) as its build tool.

To build the project: `mvn compile`
To run the unit tests: `mvn test`
To build a JAR: `mvn package`

# Questions / Feedback

Two options are available:

*   Raising an issue on [Issue tracker](https://git.tools.nic.it/rdap/jscontact-tools/-/issues)
*   Sending an email to [mailto:mario.loffredo@iit.cnr.it](mailto:mario.loffredo@iit.cnr.it)
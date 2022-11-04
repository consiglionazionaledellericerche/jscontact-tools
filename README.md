# jscontact-tools

Java tools for **JSContact** [draft-ietf-calext-jscontact](https://datatracker.ietf.org/doc/draft-ietf-calext-jscontact/) creation, validation, serialization/deserialization and conversion from and to vCard 4.0 [RFC6350](https://datatracker.ietf.org/doc/rfc6350/), xCard [RFC6351](https://datatracker.ietf.org/doc/rfc6351/) and jCard [RFC7095](https://datatracker.ietf.org/doc/rfc7095/).
Conversion from JSContact to vCard formats and viceversa is based on the rules described in [draft-ietf-calext-jscontact-vcard](https://datatracker.ietf.org/doc/draft-ietf-calext-jscontact-vcard/)
Validation and conversion of vCard formats leverage the features provided by [ez-vcard](https://github.com/mangstadt/ez-vcard) Java library.


# Maven/Gradle

## Maven

```
      <dependency>
		  <groupId>it.cnr.iit.jscontact</groupId>
		  <artifactId>jscontact-tools</artifactId>
		  <version>0.11.0</version>
      </dependency>
```

## Gradle

```
  compile 'it.cnr.iit.jscontact:jscontact-tools:0.11.0'
```

# Features
1. [Creation](#creation)
2. [Validation](#validation)
3. [Serialization/Deserialization](#serialization-deserialization)
4. [Localization](#localization)
5. [vCard Conversion](#vcard-conversion)
6. [JSContact Conversion](#jscontact-conversion)
7. [Testing](#testing)
8. [JSContact Compliance](#jscontact-compliance)
9. [References](#references)


<a name="creation"></a>
## Creation

### Builders

Object creation is achieved through builders.
Simplest maps can be loaded by putting entries singularly.
Here in the following a successful creation of an EmailAddress instance is shown.
 
```

        EmailAddress email = EmailAddress.builder()
                                        .context(Context.work(), Boolean.TRUE)
                                        .context(Context.private(), Boolean.TRUE)
                                        .email("mario.loffredo@iit.cnr.it")
                                        .build();

```

The build method throws the `java.lang.NullPointerException` when a required property is missing.
Here in the following an unsuccessful creation of an `EmailAddress` instance is shown.

```

        // email is missing
        EmailAddress.builder().context(Context.work(),Boolean.TRUE).build();

```

### Cloning

Creation can be achieved through cloning as well.
Cloning can be only applied to Card and CardGroup.

Here in the following a test assessing a successful creation of a cloned Card instance.
 
```

    @Test
    public void testClone1() throws IOException {

        String json = IOUtils.toString(getClass().getClassLoader().getResourceAsStream("jcard/jsCard-Multilingual.json"), Charset.forName("UTF-8"));
        ObjectMapper objectMapper = new ObjectMapper();
        Card jsCard = objectMapper.readValue(json, Card.class);
        assertTrue("testClone1", Objects.deepEquals(jsCard, jsCard.clone()));

    }

```

<a name="validation"></a>
## Validation

### JSContact validation

Even if topomost JSContact objects, namely **Card** and **CardGroup**, are correctly created by builders, they might need to be validated as they were obtained from an external producer through deserialization.
Validation is performed on both Card and CardGroup objects by invoking the method `isValid`.
This method returns a boolean value: `true` if the object satisfies all the constraints included in [draft-ietf-calext-jscontact], `false` otherwise.
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

<a name="serialization-deserialization"></a>
## Serialization/Deserialization

JSContact serialization/deserializaion is performed through Jackson library annotations.

## Serialization

```

        Card jsCard = Card.builder.build();
        String serialized = objectMapper.writeValueAsString(jsCard);

```

To pretty print serialized JSContact objects, use the following: 

```

        Card jsCard = Card.builder.build();
        String serialized = PrettyPrintSerializer.print(jsCard);

```

## Deserialization

```

        String json = "{"uid": \"c642b718-7c89-49f4-9497-d9fb279bb437\"}";
        ObjectMapper objectMapper = new ObjectMapper();
        Card jsCard = objectMapper.readValue(json, Card.class);

```

### Deserialization of a group of cards

Deserialization of a CardGroup object and the related cards is performed through a custom deserializer dealing with a list of polymorphic objects (i.e. Card and CardGroup instances).

```

    @Test
    public void testDeserialization4() throws IOException {

        String json = IOUtils.toString(getClass().getClassLoader().getResourceAsStream("jcard/jsCardGroup.json"), Charset.forName("UTF-8"));
        JSContact[] jsContacts = JSContact.toJSContacts(json);
        for (JSContact jsContact : jsContacts)
            assertTrue("testDeserialization4", jsContact.isValid());
    }

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
    *   List<JSContact> convert(VCard... vcard)
*   VCard2JSContact
    *   List<JSContact> convert(String vcf)
*   JCard2JSContact
    *   List<JSContact> convert(String json)
    *   List<JSContact> convert(JsonNode jsonNode) 
*   XCard2JSContact
    *   List<JSContact> convert(String xml)

All the methods return a list of JSContact top most objects and can raise a `CardException`.
`VCard` is the class mapping a vCard in ez-vcard Java library.
`JsonNode` represents the root node in Jackson library (`com.fasterxml.jackson.databind.JsonNode`).

### Conversion Rules from vCard to JSContact

The conversion is executed according to the following rules:

1. The conversion is based on the content of the [JSContact I-Ds](#drafts).

2. A card (i.e. vCard, xCard, jCard) is converted into a CardGroup object if it includes a MEMBER property, otherwise into a Card object.

3. The card components (i.e. properties, parameters or values) considered in the [RFCs](#rfcs) as well as the additonal components defined in [draft-ietf-calext-vcard-jscontact-extensions](https://datatracker.ietf.org/doc/draft-ietf-calext-vcard-jscontact-extensions/) are matched.

4. An unmatched property is converted into an entry of the topmost Card/CardGroup `ietf.org:rfc0000:props` map. The following unmatched properties are considered:    
    CLIENTPIDMAP
    XML

5. An unmatched parameter is converted into an entry of an object `ietf.org:rfc0000:params` map. The following unmatched parameters are considered:
    PID
    SORT-AS
 
6. Validation is performed before conversion if the configuration property `setCardMustBeValidated` is set to `true`.

7. Default values for the configuration properties are:
    
    - `customTimeZonesPrefix = "tz"`
    - `setCardMustBeValidated = true`
    - `setAutoIdsProfile = true`
    - `setPropIds = false`
    - `setAutoFullAddress = true`
    - `setVoiceAsDefaultPhoneFeature = true`
    - `convertGenderToSpeakToAs = true` 

8. The sex information of the GENDER property can be mapped to the SpeakToAs object if GRAMMATICAL-GENDER is missing and if the `convertGenderToSpeakToAs` configuration value is set to true as in the following:

    GENDER      SpeakToAs.grammaticalGender    
    M           male
    F           female
    O           animate
    N           neuter
    U           SpeakToAs = null

9. Where a language is required to represent a localization and the language is not specified, `en` is used by default to set the mapping configuration parameter `defaultLanguage`.

10. Regardless of their positions inside the vCard, properties mapped as Anniversary objects appear in the following order:

11. BDAY (BIRTHDATE)
12. DEATHDAY (DEATHDATE)
13. ANNIVERSARY

14. Regardless of their positions inside the vCard, properties mapped as PersonalInfo objects appear in the following order:

15. HOBBY
16. INTEREST
17. EXPERTISE

18. Regardless of their positions inside the vCard, properties mapped as MediaResource objects appear in the following order:

    1. PHOTO
    3. SOUND
    2. LOGO

19. Regardless of their positions inside the vCard, properties mapped as CalendarResource objects appear in the following order:

    1. CALURI
    2. FBURL

20. Regardless of their positions inside the vCard, properties mapped as LinkResource objects appear in the following order:

    1. URL
    2. CONTACT-URI

21. Regardless of their positions inside the vCard, properties mapped as DrectoryResource objects appear in the following order:

    4. SOURCE
    8. ORG-DIRECTORY

22. Regardless of their positions inside the vCard, properties mapped as Title objects appear in the following order:

    1. TITLE
    2. ROLE

23. If an ADR element doesn't include the LABEL parameter, based on the value of mapping configuration parameter `setAutoFullAddress`, the full address results from the newline-delimited concatenation of the non-empty address components.

24. If TZ and GEO properties contains the ALTID parameter, they are associated to the address with the same ALTID value. If the ALTID parameter is missing or inconsistent, they are associated to the first address included in the vCard.

25. Categories appear in the "keywords" map according to the values of the PREF parameter of the CATEGORIES properties. 

26. Members appear in the "members" map according to the values of the PREF parameter of the MEMBER properties.

27. JSContact UTCDateTime type is mapped to Java Calendar.

28. Media type information of `MediaResource` objects is automatically detected when the MEDIATYPE parameter is missing.

29. A custom time zone (i.e. a time zone including non-zero minutes or non-IANA time zone) is transformed into a `customTimeZones` map entry whose key is prefixed the configuration property `customTimeZonesPrefix` concatenated with an incremental positive integer (e.g. "\tz1") 

### Conversion Profiles from vCard to JSContact

By default, where a collection of objects is mapped to a map of <key,object> entries, the key has the following format: <vCard Element Tag> + "-" + <index of the element among the vCard sibling elements (starting from 1)> (e.g. "ADR-1")
This setting schema can be modified by defining a different one assigning key values based on the positions of vCard elements.
To do that, the following steps must be followed:

1. set the `setAutoIdsProfile` property of the `VCard2JSContactConfig` object to `false`

2. set the `setUsePropIds` property of the `VCard2JSContactConfig` object to `false`

3. create a `VCard2JSContactIdsProfile` object and assign the `idsProfileToUse` of `VCard2JSContactConfig` object property with it


### RDAP Conversion Profile from jCard to JSContact

A pre-defined conversion profile to convert a jCard instance inside an RDAP response [RFC9083](https://datatracker.ietf.org/doc/rfc9083/) is available.
The values of the map keys used in such profile are defined in [draft-ietf-regext-rdap-jscontact](https://datatracker.ietf.org/doc/draft-ietf-regext-rdap-jscontact/).
Additional setting rules are shown in the following code:

```

    public static final VCard2JSContactIdsProfile RDAP_PROFILE = VCard2JSContactIdsProfile.builder()
                                                                                    .id(JSContactId.organizationsId("org"))
                                                                                    .id(JSContactId.emailsId("email"))
                                                                                    .id(JSContactId.phonesId("voice"))  // 1st jCard phone number
                                                                                    .id(JSContactId.phonesId("fax"))    // 2nd jCard phone number
                                                                                    .id(JSContactId.addressesId("addr")) // 1st jCard address
                                                                                    .build();                                                                                    

```




<a name="jscontact-conversion"></a>
## JSContact Conversion

At present, the following converting methods are available:

*   JSContact2EZVCard
    *   List<VCard> convert(JSContact... jsContacts)
    *   List<VCard> convert(String json)
*   JSContact2VCard
    *   String convertToText(JSContact... jsContact)
*   JSContact2JCard
    *   String convertToJson(JSContact... jsContact)
    *   JsonNode convertToJsonNode(JSContact... jsContact)
*   JSContact2XCard
    *   String convertToXml(JSContact... jsContact)

All the methods take in input a list of JSContact top most objects and can raise a `CardException`.
`VCard` is the class mapping a vCard in ez-vcard Java library.
`JsonNode` represents the root node in Jackson library (`com.fasterxml.jackson.databind.JsonNode`).

### Conversion Rules from JSContact to vCard 

1. The conversion is based on the content of the [JSContact I-Ds](#drafts).
 
2. An entry of the topmost Card/CardGroup `ietf.org:rfc0000:props` map is converted into the related vCard property  . The following properties are considered:
    CLIENTPIDMAP
    XML
 
3. An entry of an object `ietf.org:rfc0000:params` map is converted into a vCard parameter. The following parameters are considered:
    PID
    SORT-AS

4. Default values for the configuration properties are:

    - `setCardMustBeValidated = true`
    - `setAutoAddrLabel = true`
    - `setPropIdParam = true`
    - `convertTimezoneToTZParam = true`
    - `convertCoordinatesToGEOParam = true`
    - `convertTimezoneToOffset = true`

5. The "timeZone" property can be mapped to either a TZ parameter or the TZ property based on the value of the mapping configuration parameter `convertTimezoneToTZParam` either preserving the time zone name or the time zone offset extracted from the `customTimeZones` map. Time zone names in the format "Etc/GMT(+|-).." can be mapped to offsets based on the value of mapping configuration parameter `convertTimezoneToOffset`    

6. The "cooridnates" property can be mapped to either a GEO parameter or the GEO property based on the value of the mapping configuration parameter `convertCoordinatesToGEOParam`.

7. If the "fullName" property is missing, the FN value is generated starting from the "name" property. The name components are separated by the "separator" value if present, space otherwise. If the "name" property is missing as well, the FN value is set to the "uid" property.

8. The "street" component of ADR property results from the concatenation of "name", "number" and "direction" non-empty values presented in the "street" member of the "Address" object. Such values are separated by the "separator" value if present, space otherwise.

9. The "extension" component of ADR property results from the concatenation of "building", "floor", "apartment", "room" and "extention" non-empty values presented in the "street" member of the "Address" object. Such values are separated by the "separator" value if present, space otherwise.

10. The LABEL parameter of the ADR property is equal to the "fullAddress" property of the "Address" object. If the full address is missing, based on the value of mapping configuration parameter `setAutoAddrLabel`, the value of the LABEL parameter can results from the newline-delimited concatenation of the non-empty "Address" members or.

11. The "PROP-ID" parameter can be mapped to the value of a map key based on the value of the mapping configuration parameter `setPropIdParam`.

### Conversion examples

Here in the following two examples of conversion between vCard and JSContact top most objects.

```

    @Test
    public void testAddresses4() throws IOException, CardException {

        String vcard = "BEGIN:VCARD\n" +
                "VERSION:4.0\n" +
                "FN:test\n" +
                "ADR;CC=US:;;54321 Oak St;Reston;VA;20190;USA\n" +
                "GEO:geo:46.772673,-71.282945\n" +
                "END:VCARD";

        Card jsCard = (Card) vCard2JSContact.convert(vcard).get(0);
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

        List<JSContact> jsContacts = jCard2JSContact.convert(jcard);
        assertEquals("testJCardGroup1 - 1", 3, jsContacts.size());
        assertTrue("testJCardGroup1 - 2",jsContacts.get(0) instanceof CardGroup);
        CardGroup jsCardGroup = (CardGroup) jsContacts.get(0);
        assertTrue("testJCardGroup1 - 3", jsCardGroup.getCard().getKind().isGroup());
        assertTrue("testJCardGroup1 - 4",StringUtils.isNotEmpty(jsCardGroup.getUid()));
        assertEquals("testJCardGroup1 - 5", "The Doe family", jsCardGroup.getCard().getFullName());
        assertEquals("testJCardGroup1 - 6", 2, jsCardGroup.getMembers().size());
        assertSame("testJCardGroup1 - 7", jsCardGroup.getMembers().get("urn:uuid:03a0e51f-d1aa-4385-8a53-e29025acd8af"), Boolean.TRUE);
        assertSame("testJCardGroup1 - 8", jsCardGroup.getMembers().get("urn:uuid:b8767877-b4a1-4c70-9acc-505d3819e519"), Boolean.TRUE);
        Card jsCard = (Card) jsContacts.get(1);
        assertEquals("testJCardGroup1 - 9", "urn:uuid:03a0e51f-d1aa-4385-8a53-e29025acd8af", jsCard.getUid());
        assertEquals("testJCardGroup1 - 10", "John Doe", jsCard.getFullName());
        jsCard = (Card) jsContacts.get(2);
        assertEquals("testJCardGroup1 - 11", "urn:uuid:b8767877-b4a1-4c70-9acc-505d3819e519", jsCard.getUid());
        assertEquals("testJCardGroup1 - 12", "Jane Doe", jsCard.getFullName());
        
    }

```

Here in the following two examples of conversion between JSContact top most objects and a vCard.

```

    @Test
    public void testAddresses4() throws IOException, CardException {

        String jscard = "{" +
                "\@type\": \"Card\","
                "\"uid\":\"7e0636f5-e48f-4a32-ab96-b57e9c07c7aa\"," +
                "\"fullName\":\"test\"," +
                "\"addresses\":{" +
                    "\"ADR-1\": {" +
                        "\@type\": \"Address\","
                        "\"street\":[{"@type":"StreetComponent",\"type\":\"name\",\"value\":\"54321 Oak St\"}]," +
                        "\"locality\":\"Reston\"," +
                        "\"region\":\"VA\"," +
                        "\"country\":\"USA\"," +
                        "\"postcode\":\"20190\"," +
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
        assertEquals("testAddresses4 - 10", "ADR-1", vcard.getAddresses().get(0).getParameter(PROP_ID_PARAM));
        
    }

```

```

    @Test
    public void testCardGroup1() throws IOException, CardException {

        String jsCards = "[" +
                         "{" +
                             "\@type\": \"CardGroup\","
                             "\"uid\":\"2feb4102-f15f-4047-b521-190d4acd0d29\"," +
                             "\"card\": {" +
                                 "\@type\": \"Card\","
                                 "\"uid\":\"2feb4102-f15f-4047-b521-190d4acd0d29\"," +
                                 "\"kind\":\"group\"," +
                                 "\"fullName\":\"The Doe family\"" +
                             "}," +
                             "\"members\": {" +
                                "\"urn:uuid:03a0e51f-d1aa-4385-8a53-e29025acd8af\":true," +
                                "\"urn:uuid:b8767877-b4a1-4c70-9acc-505d3819e519\":true" +
                             "}" +
                        "}," +
                        "{" +
                             "\@type\": \"Card\","
                            "\"uid\":\"urn:uuid:03a0e51f-d1aa-4385-8a53-e29025acd8af\"," +
                            "\"fullName\":\"John Doe\"" +
                        "}," +
                        "{" +
                             "\@type\": \"Card\","
                            "\"uid\":\"urn:uuid:b8767877-b4a1-4c70-9acc-505d3819e519\"," +
                            "\"fullName\":\"Jane Doe\"" +
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


<a name="testing"></a>
## Testing

Test cases are executed using [JUnit4](https://junit.org/junit4/) and cover all the features provided.

<a name="jscontact-compliance"></a>
## JSContact Compliance

This jscontact-tools version is compliant with JSContact specification version -08 and JSContact-vCard mapping version -07.

<a name="references"></a>
## References

<a name="rfcs"></a>
### RFCs

*   [RFC6350](https://datatracker.ietf.org/doc/rfc6350/)
*   [RFC6351](https://datatracker.ietf.org/doc/rfc6351/)
*   [RFC6473](https://datatracker.ietf.org/doc/rfc6473/)
*   [RFC6474](https://datatracker.ietf.org/doc/rfc6474/)
*   [RFC6715](https://datatracker.ietf.org/doc/rfc6715/)
*   [RFC6869](https://datatracker.ietf.org/doc/rfc6869/)
*   [RFC6901](https://datatracker.ietf.org/doc/rfc6901/)
*   [RFC7095](https://datatracker.ietf.org/doc/rfc7095/)
*   [RFC8605](https://datatracker.ietf.org/doc/rfc8605/)
*   [RFC9083](https://datatracker.ietf.org/doc/rfc9083/)

<a name="drafts"></a>
### JSContact I-Ds

* [draft-ietf-calext-jscontact](https://datatracker.ietf.org/doc/draft-ietf-calext-jscontact/)
* [draft-ietf-calext-jscontact-vcard](https://datatracker.ietf.org/doc/draft-ietf-calext-jscontact-vcard/)
* [draft-ietf-calext-vcard-jscontact-extensions](https://datatracker.ietf.org/doc/draft-ietf-calext-vcard-jscontact-extensions/)


# Build Instructions

## Java

Version 1.8 required.

## Maven

jscontact-tools uses [Maven](http://maven.apache.org/) as its build tool.

To build the project: `mvn compile`
To run the unit tests: `mvn test`
To build a JAR: `mvn package`

# Questions / Feedback

Two options are available:

*   Raising an issue on [Issue tracker](https://git.tools.nic.it/rdap/jscontact-tools/-/issues)
*   Sending an email to [mailto:mario.loffredo@iit.cnr.it](mailto:mario.loffredo@iit.cnr.it)
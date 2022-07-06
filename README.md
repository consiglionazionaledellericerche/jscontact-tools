# jscontact-tools

Java tools for **JSContact** [draft-ietf-calext-jscontact](https://datatracker.ietf.org/doc/draft-ietf-calext-jscontact/) creation, validation, serialization/deserialization and conversion from and to vCard 4.0 [RFC6350](https://datatracker.ietf.org/doc/rfc6350/), xCard [RFC6351](https://datatracker.ietf.org/doc/rfc6351/) and jCard [RFC7095](https://datatracker.ietf.org/doc/rfc7095/).
Validation and conversion of vCard formats leverage the features provided by [ez-vcard](https://github.com/mangstadt/ez-vcard) Java library.

# Maven/Gradle

## Maven

```
      <dependency>
		  <groupId>it.cnr.iit.jscontact</groupId>
		  <artifactId>jscontact-tools</artifactId>
		  <version>0.10.0</version>
      </dependency>
```

## Gradle

```
  compile 'it.cnr.iit.jscontact:jscontact-tools:0.10.0'
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
        ObjectMapper objectMapper = new ObjectMapper();
        SimpleModule module = new SimpleModule();
        module.addDeserializer(JSContact.class, new JSContactListDeserializer());
        objectMapper.registerModule(module);
        JSContact[] jsContacts = objectMapper.readValue(json, JSContact[].class);
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

1.  The conversion is based on the content of the [JSContact I-Ds](#drafts).

2.  A card (i.e. vCard, xCard, jCard) is converted into a CardGroup object if it includes a MEMBER property, otherwise into a Card object.

3.  The card components (i.e. properties, parameters or values) considered in the [RFCs](#rfcs) are matched.

4.  An unmatched property is converted into a topmost Card/CardGroup property with prefix `ietf.org:rfc6350`. The following unmatched properties are considered:    
    CLIENTPIDMAP
    XML

5.  The sex information of the GENDER property is mapped to the SpeakToAs object as in the following:

    GENDER      SpeakToAs.grammaticalGender    
    M           male
    F           female
    O           animate
    N           neuter
    U           SpeakToAs = null
    
    The gender identity information is mapped to the the Card property named `ietf.org:rfc6350:GENDER`
    
6.  An unmatched parameter is converted into a topmost Card/CardGroup property with prefix `ietf.org:rfc6350:<vCard Property Name>`. The following unmatched parameters are considered:
    GROUP
    PID
    SORT-AS (only for vCard N property)
    CALSCALE (only for vCard ANNIVERSARY, BDAY and DEATHDATE properties)

7.  An extension property is converted into a topmost Card/CardGroup property with prefix defined by the configuration property `extensionsPrefix`.
  
8.  Validation is performed before conversion if the configuration property `cardToValidate` is set to `true`.

9.  Default values for the configuration properties are:
    
    -  `extensionsPrefix = "extension:"`
    -  `customTimeZonesPrefix = "tz"`
    -  `cardToValidate = true`
    -  `applyAutoIdsProfile = true`

10.  Where a language is required to represent a localization and the language is not specified, `en` is used by default.

11.  Regardless of their positions inside the vCard, properties mapped as Anniversary objects appear in the following order:

    1. BDAY (BIRTHDATE)
    2. DEATHDAY (DEATHDATE)
    3. ANNIVERSARY

12.  Regardless of their positions inside the vCard, properties mapped as PersonalInfo objects appear in the following order:

    1. HOBBY
    2. INTEREST
    3. EXPERTISE

13. Regardless of their positions inside the vCard, properties mapped as online Resource objects appear in the following order:

    1. SOURCE
    2. LOGO
    3. SOUND
    4. URL
    5. KEY
    6. FBURL
    7. CALURI
    8. ORG-DIRECTORY
    9. CONTACT-URI

14. Regardless of their positions inside the vCard, properties mapped as Title objects appear in the following order:

    1. TITLE
    2. ROLE

15. If an ADR element doesn't include the LABEL parameter, the full address results from the newline-delimited concatenation of the non-empty address components.

16. If TZ and GEO properties contains the ALTID parameter, they are associated to the address with the same ALTID value. If the ALTID parameter is missing or inconsistent, they are associated to the first address included in the vCard.

17. Categories appear in the "categories" map according to the values of the PREF parameter of the CATEGORIES properties. 

18. Members appear n the "members" map according to the values of the PREF parameter of the MEMBER properties.

19. If no vCard tel-type value is specified, the "features" map of the "Phone" object includes the value "PhoneFeature.voice()" by default. 

20. JSContact UTCDateTime type is mapped to Java Calendar.

21. Media type information of `File` and `Resource` objects is automatically detected when the MEDIATYPE parameter is missing.

22. A custom time zone (i.e. a time zone including non-zero minutes or non-IANA time zone) is transformed into a `timeZones` map entry whose key is prefixed the configuration property `customTimeZonesPrefix` concatenated with an incremental positive integer (e.g. "\tz1") 

### Conversion Profiles from vCard to JSContact

By default, where a collection of objects is mapped to a map of <key,object> entries, the key has the following format: <vCard Element Tag> + "-" + <index of the element among the vCard sibling elements (starting from 1)> (e.g. "ADR-1")
This setting schema can be modified by defining a different one assigning key values based on the positions of vCard elements.
To do that, the following steps must be followed:

1. set the `applyAutoIdsProfile` property of the `VCard2JSContactConfig` object to `false`

2. create a `VCard2JSContactIdsProfile` object and assign the `idsProfileToApply` of `VCard2JSContactConfig` object property with it


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

1.  The conversion is based on the content of the [JSContact I-Ds](#drafts).

2.  The following vCard extension properties are generated by converting the related unmatched JSContact properties:
    `X-JSCONTACT-CREATED`
    `X-JSCONTACT-PREFERREDCONTACTMETHOD`
    `X-JSCONTACT-PRONOUNS`
        
3.  A topmost Card/CardGroup property with name `ietf.org:rfc6350:<vCard Property Name>` is converted into the related vCard property  . The following properties are considered:
    CLIENTPIDMAP
    XML

4.  The the SpeakToAs object is mapped to GENDER property as in the following:

    SpeakToAs.grammaticalGender     GENDER          
    male                            M
    female                          F
    animate                         O
    neuter                          N
    inanimate                       N;inanimate          
    null                            null
    
    If a SpeakToAs object includes only the "pronouns" property, it is mapped to the VCARD `X-JSCONTACT-PRONOUNS` extension property. 
    
5.  A topmost Card/CardGroup property with name `ietf.org:rfc6350:<vCard Property Name>:<vCard Parameter Name>` is converted into a vCard parameter. The following parameters are considered:
    GROUP
    PID
    SORT-AS (only for vCard N property)
    CALSCALE (only for vCard ANNIVERSARY, BDAY and DEATHDATE properties)

6.  A topmost Card/CardGroup property with prefix defined by the configuration property `extensionsPrefix` is converted into a vCard extension.

7.  The Card/CardGroup "titles" property is mapped to the vCard TITLE property.
    
8.  The "timeZone" property is always mapped to a TZ parameter either preserving the time zone name or the time zone offset extracted from the `timeZones` map.    

9.  It the "fullName" property is missing, the FN value is generated starting from the "name" property. The name components are separated by the "separator" value if present, space otherwise. If the "name" property is missing as well, the FN value is set to the "uid" property.

10. The "street" component of ADR property results from the concatenation of "name", "number" and "direction" non-empty values presented in the "street" member of the "Address" object. Such values are separated by the "separator" value if present, space otherwise.

11. The "extension" component of ADR property results from the concatenation of "building", "floor", "apartment", "room" and "extention" non-empty values presented in the "street" member of the "Address" object. Such values are separated by the "separator" value if present, space otherwise.

12. The LABEL parameter of the ADR property is equal to the "fullAddress" property of the "Address" object. If the full address is missing, the value of the LABEL parameter results from the newline-delimited concatenation of the non-empty "Address" members.

### Conversion examples

Here in the following two examples of conversion between vCard and JSContact top most objects.

```

    @Test
    public void testAddressesValid4() throws IOException, CardException {

        String vcard = "BEGIN:VCARD\n" +
                "VERSION:4.0\n" +
                "FN:test\n" +
                "ADR;CC=US:;;54321 Oak St;Reston;VA;20190;USA\n" +
                "GEO:geo:46.772673,-71.282945\n" +
                "END:VCARD";

        Card jsCard = (Card) vCard2JSContact.convert(vcard).get(0);
        assertTrue("testAddressesValid4 - 1",jsCard.getAddresses()!=null);
        assertTrue("testAddressesValid4 - 2",jsCard.getAddresses().size() == 1);
        assertTrue("testAddressesValid4 - 3",jsCard.getAddresses().get("ADR-1").getCountryCode().equals("US"));
        assertTrue("testAddressesValid4 - 4",jsCard.getAddresses().get("ADR-1").getCountry().equals("USA"));
        assertTrue("testAddressesValid4 - 5",jsCard.getAddresses().get("ADR-1").getPostcode().equals("20190"));
        assertTrue("testAddressesValid4 - 6",jsCard.getAddresses().get("ADR-1").getLocality().equals("Reston"));
        assertTrue("testAddressesValid4 - 7",jsCard.getAddresses().get("ADR-1").getRegion().equals("VA"));
        assertTrue("testAddressesValid4 - 8",jsCard.getAddresses().get("ADR-1").getStreetDetails().equals("54321 Oak St"));
        assertTrue("testAddressesValid4 - 9",jsCard.getAddresses().get("ADR-1").getFullAddress().equals("54321 Oak St\nReston\nVA\n20190\nUSA"));
        assertTrue("testAddressesValid4 - 10",jsCard.getAddresses().get("ADR-1").getCoordinates().equals("geo:46.772673,-71.282945"));

    }

```

```

    @Test
    public void testJCardGroupValid1() throws IOException, CardException {

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
        assertTrue("testJCardGroupValid1 - 1",jsContacts.size() == 3);
        assertTrue("testJCardGroupValid1 - 2",jsContacts.get(0) instanceof CardGroup);
        CardGroup jsCardGroup = (CardGroup) jsContacts.get(0);
        assertTrue("testJCardGroupValid1 - 3", jsCardGroup.getKind().isGroup());
        assertTrue("testJCardGroupValid1 - 4",StringUtils.isNotEmpty(jsCardGroup.getUid()));
        assertTrue("testJCardGroupValid1 - 5",jsCardGroup.getFullName().equals("The Doe family"));
        assertTrue("testJCardGroupValid1 - 6",jsCardGroup.getMembers().size() == 2);
        assertTrue("testJCardGroupValid1 - 7",jsCardGroup.getMembers().get("urn:uuid:03a0e51f-d1aa-4385-8a53-e29025acd8af") == Boolean.TRUE);
        assertTrue("testJCardGroupValid1 - 8",jsCardGroup.getMembers().get("urn:uuid:b8767877-b4a1-4c70-9acc-505d3819e519") == Boolean.TRUE);
        Card jsCard = (Card) jsContacts.get(1);
        assertTrue("testJCardGroupValid1 - 9",jsCard.getUid().equals("urn:uuid:03a0e51f-d1aa-4385-8a53-e29025acd8af"));
        assertTrue("testJCardGroupValid1 - 10",jsCard.getFullName().equals("John Doe"));
        jsCard = (Card) jsContacts.get(2);
        assertTrue("testJCardGroupValid1 - 11",jsCard.getUid().equals("urn:uuid:b8767877-b4a1-4c70-9acc-505d3819e519"));
        assertTrue("testJCardGroupValid1 - 12",jsCard.getFullName().equals("Jane Doe"));

    }

```

Here in the following two examples of conversion between JSContact top most objects and a vCard.

```

    @Test
    public void testAddressesValid4() throws IOException, CardException {

        String jscard = "{" +
                "\@type\": \"Card\","
                "\"uid\":\"7e0636f5-e48f-4a32-ab96-b57e9c07c7aa\"," +
                "\"fullName\":\"test\"," +
                "\"addresses\":{" +
                    "\"ADR-1\": {" +
                        "\@type\": \"Address\","
                        "\"street\":[{\"type\":\"name\",\"value\":\"54321 Oak St\"}]," +
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
        assertTrue("testAddressesValid4 - 1",vcard.getAddresses().size() == 1);
        assertTrue("testAddressesValid4 - 2",vcard.getAddresses().get(0).getParameter("CC").equals("US"));
        assertTrue("testAddressesValid4 - 3",vcard.getAddresses().get(0).getCountry().equals("USA"));
        assertTrue("testAddressesValid4 - 4",vcard.getAddresses().get(0).getPostalCode().equals("20190"));
        assertTrue("testAddressesValid4 - 5",vcard.getAddresses().get(0).getLocality().equals("Reston"));
        assertTrue("testAddressesValid4 - 6",vcard.getAddresses().get(0).getRegion().equals("VA"));
        assertTrue("testAddressesValid4 - 7",vcard.getAddresses().get(0).getStreetAddress().equals("54321 Oak St"));
        assertTrue("testAddressesValid4 - 8", vcard.getAddresses().get(0).getLabel().equals("54321 Oak St\nReston\nVA\n20190\nUSA"));
        assertTrue("testAddressesValid4 - 8", vcard.getAddresses().get(0).getGeo().equals(GeoUri.parse("geo:46.772673,-71.282945")));
    }

```

```

    @Test
    public void testCardGroupValid1() throws IOException, CardException {

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
        assertTrue("testCardGroupValid1 - 1",vcards.size() == 3);
        assertTrue("testCardGroupValid1 - 3", vcards.get(0).getKind().isGroup());
        assertTrue("testCardGroupValid1 - 4",StringUtils.isNotEmpty(vcards.get(0).getUid()));
        assertTrue("testCardGroupValid1 - 5",vcards.get(0).getFullName().equals("The Doe family"));
        assertTrue("testCardGroupValid1 - 6",vcards.get(0).getMembers().size() == 2);
        assertTrue("testCardGroupValid1 - 7",vcards.get(0).getMembers().get(0).getUri().equals("urn:uuid:03a0e51f-d1aa-4385-8a53-e29025acd8af") == Boolean.TRUE);
        assertTrue("testCardGroupValid1 - 8",vcards.get(0).getMembers().get(1).getUri().equals("urn:uuid:b8767877-b4a1-4c70-9acc-505d3819e519") == Boolean.TRUE);
        assertTrue("testCardGroupValid1 - 9",vcards.get(1).getUid().equals("urn:uuid:03a0e51f-d1aa-4385-8a53-e29025acd8af"));
        assertTrue("testCardGroupValid1 - 10",vcards.get(1).getFullName().equals("John Doe"));
        assertTrue("testCardGroupValid1 - 11",vcards.get(2).getUid().equals("urn:uuid:b8767877-b4a1-4c70-9acc-505d3819e519"));
        assertTrue("testCardGroupValid1 - 12",vcards.get(2).getFullName().equals("Jane Doe"));
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

*   [draft-ietf-calext-jscontact](https://datatracker.ietf.org/doc/draft-ietf-calext-jscontact/)
*   [draft-ietf-calext-jscontact-vcard](https://datatracker.ietf.org/doc/draft-ietf-calext-jscontact-vcard/)


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
# jscontact-tools

Java tools for **JSContact** [draft-ietf-jmap-jscontact](https://datatracker.ietf.org/doc/draft-ietf-jmap-jscontact/) creation, validation, serialization/deserialization and conversion from vCard [RFC6350](https://datatracker.ietf.org/doc/rfc6350/), xCard [RFC6351](https://datatracker.ietf.org/doc/rfc6351/) and jCard [RFC7095](https://datatracker.ietf.org/doc/rfc7095/).
Validation and conversion leverage the features provided by [ez-vcard](https://github.com/mangstadt/ez-vcard) Java library.

# Maven/Gradle

## Maven

```
      <dependency>
		  <groupId>it.cnr.iit.jscontact</groupId>
		  <artifactId>jscontact-tools</artifactId>
		  <version>0.3.1</version>
      </dependency>
```

## Gradle

```
  compile 'it.cnr.iit.jscontact:jscontact-tools:0.3.1'
```

# Features
1. [Creation](#creation)
2. [Validation](#validation)
3. [Serialization/Deserialization](#serialization-deserialization)
4. [vCard Conversion](#vcard-conversion)
5. [JSContact Conversion](#jscontact-conversion)
6. [Testing](#testing)
7. [References](#references)

<a name="creation"></a>
## Creation

###Builders

Object creation is achieved through builders.
Simplest maps can be loaded by putting items singularly.
Here in the following a successful creation of an EmailAddress instance is shown.
 
```
        EmailAddress email = EmailAddress.builder()
                                        .context(Context.WORK, Boolean.TRUE)
                                        .context(Context.PRIVATE, Boolean.TRUE)
                                        .email("mario.loffredo@iit.cnr.it")
                                        .build();
```

The build method throws the `java.lang.NullPointerException` when a not null property is missing.
Here in the following an unsuccessful creation of an `EmailAddress` instance is shown.

```
        // email is missing
        EmailAddress.builder().context(Context.WORK,Boolean.TRUE).build();
```

###Cloning

Creation can be achieved through cloning as well.
Cloning can be only applied to topmost JSContact objects, namely JSCard and JSCardGroup.

Here in the following a test assessing a successful creation of a cloned JSCard instane.
 
```

    @Test
    public void testClone1() throws IOException {

        String json = IOUtils.toString(getClass().getClassLoader().getResourceAsStream("jcard/jsCard-Multilingual.json"), Charset.forName("UTF-8"));
        ObjectMapper objectMapper = new ObjectMapper();
        JSCard jsCard = objectMapper.readValue(json, JSCard.class);
        assertTrue("testClone1", Objects.deepEquals(jsCard, jsCard.clone()));

    }

```

<a name="validation"></a>
## Validation

### JSContact validation

Even if topomost JSContact objects, namely **JSCard** and **JSCardGroup**, are correctly created by builders, they might need to be validated as they were obtained from an external producer through deserialization.
Validation is performed on both JSCard and JSCardGroup instances by invoking the method `isValid`.
This method returns a boolean value: `true` if the object satisfies all the constraints included in [draft-ietf-jmap-jscontact-05], `false` otherwise.
If the validation process doesn't end successfully, the list of errors/messages can be obtained by calling the `getValidationMessages` method.  
Here in the following a method testing an unsuccessfully ended validation is shown.

```

    @Test
    public void testInvalidCountryCode() {

        Map<String,Address> addresses = new HashMap<String,Address>() {{ put("ADR-1", Address.builder()
                                                                                .countryCode("ita")
                                                                                .build());
                                                                        }};
        JSCard jsCard = JSCard.builder()
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
`JsonNode` represents the root of `com.fasterxml.jackson.databind`.

<a name="serialization-deserialization"></a>
## Serialization/Deserialization

JSContact serialization/deserializaion is performed through Jackson library that relies on annotation.

## Serialization

```
        JSCard jsCard = JSCard.builder.build();
        String serialized = objectMapper.writeValueAsString(jsCard);
```

To pretty print serialized JSON, use the following: 

```
        JSCard jsCard = JSCard.builder.build();
        String serialized = PrettyPrintSerializer.print(jsCard);
```

## Deserialization

```
        String json = "{"uid": \"c642b718-7c89-49f4-9497-d9fb279bb437\"}";
        ObjectMapper objectMapper = new ObjectMapper();
        JSCard jsCard = objectMapper.readValue(json, JSCard.class);
```

### Deserialization of a card group

Deserialization of a card group and the related cards is performed through a custom deserializer dealing with a list of polymorphic objects (i.e. JSCard instances and JSCardGroup instances).

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

<a name="vcard-conversion"></a>
## vCard Conversion

At present, the following converting methods are available:

*   EZVCard2JSContact
    *   List<JSContact> convert(List<VCard>)
*   VCard2JSContact
    *   List<JSContact> convert(String vcf)
*   JCard2JSContact
    *   List<JSContact> convert(String json)
    *   List<JSContact> convert(JsonNode jsonNode) 
*   XCard2JSContact
    *   List<JSContact> convert(String xml)

All the methods return a list of JSContact (JSCard or JSCardGroup) instances and can raise a `CardException`.
`VCard` is the class mapping a vCard in ez-vcard Java library.
`JsonNode` represents the root of `com.fasterxml.jackson.databind`.

### Conversion Rules from vCard to JSContact

The conversion is executed according to the following rules:

1.  The conversion is based on the content of the [JSContact I-Ds](#drafts).

2.  A card (i.e. vCard, xCard, jCard) is converted into a JSCardGroup object if it includes a MEMBER property, otherwise into a JSCard object.

3.  The card components (i.e. properties, parameters or values) considered in the [RFCs](#rfcs) are matched.

4.  An unmatched property is converted into a topmost JSCard/JSCardGroup member with prefix `ietf.org/rfc6350`. The following unmatched properties are considered:
    GENDER
    CLIENTPIDMAP
    XML

5.  An unmatched parameter is converted into a topmost JSCard/JSCardGroup member with prefix `ietf.org/rfc6350/<vCard Property Name>`. The following unmatched parameters are considered:
    GROUP
    PID
    SORT-AS (only for vCard N property)
    CALSCALE (only for vCard ANNIVERSARY, BDAY and DEATHDATE properties)

6.  An extension property is converted into a topmost JSCard/JSCardGroup member with prefix defined by the configuration property `extensionPrefix`.
  
7.  Validation is performed before conversion if the configuration property `cardToValidate` is set to `true`.

8.  Default values for the configuration properties are:
    
    -  `extensionsPrefix = "extension/"`
    -  `cardToValidate = true`

9.  Where a language is required to represent a localization and the language is not specified, `en` is used by default.

10.  Regardless of their positions inside the card, properties mapped as Anniversary objects appear in the following order:

    1. BDAY (BIRTHDATE)
    2. DEATHDAY (DEATHDATE)
    3. ANNIVERSARY

11.  Regardless of their positions inside the card, properties mapped as PersonalInfo objects appear in the following order:

    1. HOBBY
    2. INTEREST
    3. EXPERTISE

12. Regardless of their positions inside the card, properties mapped as online Resource objects appear in the following order:

    1. SOURCE
    2. IMPP
    3. LOGO
    4. SOUND
    5. URL
    6. KEY
    7. FBURL
    8. CALADRURI
    9. CALURI
    10. ORG-DIRECTORY
    11. CONTACT-URI

13. Regardless of their positions inside the card, properties mapped as Title objects appear in the following order:

    1. TITLE
    2. ROLE

14. If an ADR element doesn't include the LABEL parameter, the full address is generated by concatenating the non-empty address components.

15. If TZ and GEO properties contains the ALTID parameter, they are associated to the address with the same ALTID value. If the ALTID parameter is missing or inconsistent, they are associated to the first address reported in the card.

16. Categories appear in the "categories" map according to the values of the PREF parameter of the CATEGORIES properties. 

17. Members appear n the "members" map according to the values of the PREF parameter of the MEMBER properties.

18. If no vCard tel-type value is specified, the "features" map of the "Phone" object includes PhoneFeature.VOICE by default. 

19. JSContact UTCDateTime type is mapped onto Java Calendar.

20. Where a collection of objects is mapped onto a map of <key,object> entries, the key has the following format: <vCard Element Tag> + "-" + <index of the element among the vCard sibling elements (starting from 1)> (e.g. "ADR-1")

<a name="jscontact-conversion"></a>
## JSContact Conversion

At present, the following converting methods are available:

*   JSContact2EZVCard
    *   List<VCard> convert(List<JSContact> jsContacts)
    *   List<VCard> convert(String json)
*   JSContact2VCard
    *   String convertToText(JSContact jsContact)
    *   String convertToText(List<JSContact> jsContacts)
*   JSContact2JCard
    *   String convertToJson(JSContact jsContact)
    *   String convertToJson(List<JSContact> jsContacts)
    *   JsonNode convertToJsonNode(JSContact jsContact)
    *   JsonNode convertToJsonNode(List<JSContact> jsContacts)
*   JSContact2XCard
    *   String convertToXml(JSContact jsContact)
    *   String convertToXml(List<JSContact> jsContacts)

All the methods take in input a list of JSContact (JSCard or JSCardGroup) instances and can raise a `CardException`.
`VCard` is the class mapping a vCard in ez-vcard Java library.
`JsonNode` represents the root of `com.fasterxml.jackson.databind`.

### Conversion Rules from JSContact to vCard 

1.  The conversion is based on the content of the [JSContact I-Ds](#drafts).

2.  The following vCard properties are generated by converting the related unmatched JSContact properties:
    `X-JSCONTACT-CREATED`
    `X-JSCONTACT-PREFERREDCONTACTMETHOD`
    
3.  A topmost JSCard/JSCardGroup member with name `ietf.org/rfc6350/<vCard Property Name>` is converted into the related vCard property  . The following properties are considered:
    GENDER
    CLIENTPIDMAP
    XML

5.  A topmost JSCard/JSCardGroup member with name `ietf.org/rfc6350/<vCard Property Name>/<vCard Parameter Name>` is converted into a vCard parameter. The following parameters are considered:
    GROUP
    PID
    SORT-AS (only for vCard N property)
    CALSCALE (only for vCard ANNIVERSARY, BDAY and DEATHDATE properties)

6.  A topmost JSCard/JSCardGroup member with prefix defined by the configuration property `extensionPrefix` is converted into a vCard extension.

7.  The JSCard/JSCardGroup "titles" property is mapped onto the vCard TITLE property.
    
8.  The "timeZone" property is always mapped onto a TZ parameter preserving the time zone name.     

9.  It the "fullName" property is missing, the FN value is generated starting from the "name" property. If the "name" property is missing as well, the FN value is set to the "uid" property.

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

        JSCard jsCard = (JSCard) vCard2JSContact.convert(vcard).get(0);
        assertTrue("testAddressesValid4 - 1",jsCard.getAddresses()!=null);
        assertTrue("testAddressesValid4 - 2",jsCard.getAddresses().size() == 1);
        assertTrue("testAddressesValid4 - 3",jsCard.getAddresses().get("ADR-1").getCountryCode().equals("US"));
        assertTrue("testAddressesValid4 - 4",jsCard.getAddresses().get("ADR-1").getCountry().equals("USA"));
        assertTrue("testAddressesValid4 - 5",jsCard.getAddresses().get("ADR-1").getPostcode().equals("20190"));
        assertTrue("testAddressesValid4 - 6",jsCard.getAddresses().get("ADR-1").getLocality().equals("Reston"));
        assertTrue("testAddressesValid4 - 7",jsCard.getAddresses().get("ADR-1").getRegion().equals("VA"));
        assertTrue("testAddressesValid4 - 8",jsCard.getAddresses().get("ADR-1").getStreet().equals("54321 Oak St"));
        assertTrue("testAddressesValid4 - 9",jsCard.getAddresses().get("ADR-1").getFullAddress().getValue().equals("54321 Oak St\nReston\nVA\n20190\nUSA"));
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
        assertTrue("testJCardGroupValid1 - 2",jsContacts.get(0) instanceof JSCardGroup);
        JSCardGroup jsCardGroup = (JSCardGroup) jsContacts.get(0);
        assertTrue("testJCardGroupValid1 - 3", jsCardGroup.getKind().isGroup());
        assertTrue("testJCardGroupValid1 - 4",StringUtils.isNotEmpty(jsCardGroup.getUid()));
        assertTrue("testJCardGroupValid1 - 5",jsCardGroup.getFullName().getValue().equals("The Doe family"));
        assertTrue("testJCardGroupValid1 - 6",jsCardGroup.getMembers().size() == 2);
        assertTrue("testJCardGroupValid1 - 7",jsCardGroup.getMembers().get("urn:uuid:03a0e51f-d1aa-4385-8a53-e29025acd8af") == Boolean.TRUE);
        assertTrue("testJCardGroupValid1 - 8",jsCardGroup.getMembers().get("urn:uuid:b8767877-b4a1-4c70-9acc-505d3819e519") == Boolean.TRUE);
        JSCard jsCard = (JSCard) jsContacts.get(1);
        assertTrue("testJCardGroupValid1 - 9",jsCard.getUid().equals("urn:uuid:03a0e51f-d1aa-4385-8a53-e29025acd8af"));
        assertTrue("testJCardGroupValid1 - 10",jsCard.getFullName().getValue().equals("John Doe"));
        jsCard = (JSCard) jsContacts.get(2);
        assertTrue("testJCardGroupValid1 - 11",jsCard.getUid().equals("urn:uuid:b8767877-b4a1-4c70-9acc-505d3819e519"));
        assertTrue("testJCardGroupValid1 - 12",jsCard.getFullName().getValue().equals("Jane Doe"));

    }

```

Here in the following two examples of conversion between JSContact top most objects and a vCard.

```

    @Test
    public void testAddressesValid4() throws IOException, CardException {

        String jscard = "{" +
                "\"uid\":\"7e0636f5-e48f-4a32-ab96-b57e9c07c7aa\"," +
                "\"fullName\":{\"value\":\"test\"}," +
                "\"addresses\":{" +
                    "\"ADR-1\": {" +
                        "\"street\":\"54321 Oak St\"," +
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
        assertTrue("testAddressesValid4 - 8", vcard.getAddresses().get(0).getLabel().equals("54321 Oak St Reston VA 20190 USA"));
        assertTrue("testAddressesValid4 - 8", vcard.getAddresses().get(0).getGeo().equals(GeoUri.parse("geo:46.772673,-71.282945")));
    }

```

```

    @Test
    public void testJCardGroupValid1() throws IOException, CardException {

        String jsCards = "[" +
                         "{" +
                             "\"uid\":\"2feb4102-f15f-4047-b521-190d4acd0d29\"," +
                             "\"kind\":\"group\"," +
                             "\"fullName\": {" +
                                "\"value\":\"The Doe family\"" +
                             "}," +
                             "\"members\": {" +
                                "\"urn:uuid:03a0e51f-d1aa-4385-8a53-e29025acd8af\":true," +
                                "\"urn:uuid:b8767877-b4a1-4c70-9acc-505d3819e519\":true" +
                             "}" +
                        "}," +
                        "{" +
                            "\"uid\":\"urn:uuid:03a0e51f-d1aa-4385-8a53-e29025acd8af\"," +
                            "\"fullName\": {" +
                                "\"value\":\"John Doe\"" +
                            "}" +
                        "}," +
                        "{" +
                            "\"uid\":\"urn:uuid:b8767877-b4a1-4c70-9acc-505d3819e519\"," +
                            "\"fullName\": {" +
                                "\"value\":\"Jane Doe\"" +
                            "}" +
                        "}" +
                        "]";

        List<VCard> vcards = jsContact2VCard.convert(jsCards);
        assertTrue("testJCardGroupValid1 - 1",vcards.size() == 3);
        assertTrue("testJCardGroupValid1 - 3", vcards.get(0).getKind().isGroup());
        assertTrue("testJCardGroupValid1 - 4",StringUtils.isNotEmpty(vcards.get(0).getUid().getValue()));
        assertTrue("testJCardGroupValid1 - 5",vcards.get(0).getFormattedName().getValue().equals("The Doe family"));
        assertTrue("testJCardGroupValid1 - 6",vcards.get(0).getMembers().size() == 2);
        assertTrue("testJCardGroupValid1 - 7",vcards.get(0).getMembers().get(0).getUri().equals("urn:uuid:03a0e51f-d1aa-4385-8a53-e29025acd8af") == Boolean.TRUE);
        assertTrue("testJCardGroupValid1 - 8",vcards.get(0).getMembers().get(1).getUri().equals("urn:uuid:b8767877-b4a1-4c70-9acc-505d3819e519") == Boolean.TRUE);
        assertTrue("testJCardGroupValid1 - 9",vcards.get(1).getUid().getValue().equals("urn:uuid:03a0e51f-d1aa-4385-8a53-e29025acd8af"));
        assertTrue("testJCardGroupValid1 - 10",vcards.get(1).getFormattedName().getValue().equals("John Doe"));
        assertTrue("testJCardGroupValid1 - 11",vcards.get(2).getUid().getValue().equals("urn:uuid:b8767877-b4a1-4c70-9acc-505d3819e519"));
        assertTrue("testJCardGroupValid1 - 12",vcards.get(2).getFormattedName().getValue().equals("Jane Doe"));
    }

```


<a name="testing"></a>
## Testing

Test cases are executed using [JUnit4](https://junit.org/junit4/) and cover all the features provided.

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
*   [RFC7095](https://datatracker.ietf.org/doc/rfc7095/)
*   [RFC8605](https://datatracker.ietf.org/doc/rfc8605/)

<a name="drafts"></a>
### JSContact I-Ds

*   [draft-ietf-jmap-jscontact-05](https://datatracker.ietf.org/doc/draft-ietf-jmap-jscontact/)
*   [draft-ietf-jmap-jscontact-vcard-03](https://datatracker.ietf.org/doc/draft-ietf-jmap-jscontact-vcard/)


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
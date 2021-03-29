# jscontact-tools

Java tools for **JSContact** [draft-ietf-jmap-jscontact](https://datatracker.ietf.org/doc/draft-ietf-jmap-jscontact/) creation, validation, serialization/deserialization and conversion from vCard [RFC6350](https://datatracker.ietf.org/doc/rfc6350/), xCard [RFC6351](https://datatracker.ietf.org/doc/rfc6351/) and jCard [RFC7095](https://datatracker.ietf.org/doc/rfc7095/).
Validation and conversion leverage the features provided by [ez-vcard](https://github.com/mangstadt/ez-vcard) Java library.

# Maven/Gradle

## Maven

```
      <dependency>
		  <groupId>it.cnr.iit.jscontact</groupId>
		  <artifactId>jscontact-tools</artifactId>
		  <version>0.2.1</version>
      </dependency>
```

## Gradle

```
  compile 'it.cnr.iit.jscontact:jscontact-tools:0.2.1'
```

# Features
1. [Creation](#creation)
2. [Validation](#validation)
3. [Serialization/Deserialization](#serialization-deserialization)
4. [vCard Conversion](#vcard-conversion)
5. [Testing](#testing)
6. [References](#references)

<a name="creation"></a>
## Creation

Object creation is achieved through builders.
Here in the following a successful creation of an email `Resource` instance is shown.
 
```
        Resource email = Resource.builder()
                         .context(ResourceContext.WORK)
                         .type(EmailResourceType.EMAIL.getValue())
                         .value("mario.loffredo@iit.cnr.it")
                         .build();
```

The build method throws the `java.lang.NullPointerException` when a not null property is missing.
Here in the following an unsuccessful creation of an email `Resource` instance is shown.

```
        // value is missing, java.lang.NullPointerException is thrown
        Resource.builder()
                .context(ResourceContext.WORK)
                .type(EmailResourceType.EMAIL.getValue())
                .build();
```

<a name="validation"></a>
## Validation

### JSContact validation

Even if topomost JSContact objects, namely **JSCard** and **JSCardGroup**, are correctly created by builders, they might need to be validated as they were obtained from an external producer through deserialization.
Validation is performed on both JSCard and JSCardGroup instances by invoking the method `isValid`.
This method returns a boolean value: `true` if the object is valid, `false` otherwise.
If the validation process doesn't end successfully, the list of errors/messages can be obtained by calling the `getValidationMessages` method.  
Here in the following an unsuccessfully ended validation is shown.

```
        Address address = Address.builder()
                                 .countryCode("ita")
                                 .build();
        JSCard jsCard = JSCard.builder()
                .uid(getUUID())
                .addresses(new Address[]{address})
                .build();
        if(!jsCard.isValid()) {
            for (String message : jsCard.getValidationMessages())
                System.out.println(message);
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
        JSCard jsCard = JsCard.builder.build();
        String serialized = objectMapper.writeValueAsString(jsCard);
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

*   JCard2JSContact
    *   List<JSContact> convert(String json)
    *   List<JSContact> convert(JsonNode jsonNode) 
*   VCard2JSContact
    *   List<JSContact> convert(String vcf)
*   XCard2JSContact
    *   List<JSContact> convert(String xml)

All the methods return a list of JSContact (JSCard or JSCardGroup) instances and can raise a `CardException`.
`JsonNode` represents the root of `com.fasterxml.jackson.databind`.

### Conversion Rules from vCard to JSContact

The conversion is executed according to the following rules:

1.  The conversion is based on the content of the [JSContact I-Ds](#drafts).

2.  A card (i.e. vCard, xCard, jCard) is converted into a JSCardGroup object if it includes a KIND property set to `group`, otherwise into a JSCard object.

3.  The card components (i.e. properties, parameters or values) considered in the [RFCs](#rfcs) are matched.

4.  An unmatched property is converted into a topmost JSCard/JSCardGroup member with prefix `ietf.org/rfc6350`

5.  An extension property is converted into a topmost JSCard/JSCardGroup member with prefix defined by the configuration property `extensionPrefix`.
  
6.  Validation is performed before conversion if the configuration property `cardToValidate` is set to `true`.

7.  Default values for the configuration properties are:
    
    -  `extensionsPrefix = "extension/"`
    -  `cardToValidate = true`

8.  Where a language is required to represent a localization and the language is not specified, `en` is used by default.

9.  Regardless of their positions inside the card, properties mapped as Anniversary objects appear in the following order:

    1. BDAY (BIRTHDATE)
    2. DEATHDAY (DEATHDATE)
    3. ANNIVERSARY

10.  Regardless of their positions inside the card, properties mapped as PersonalInfo objects appear in the following order:

    1. HOBBY
    2. INTEREST
    3. EXPERTISE

11. Regardless of their positions inside the card, properties mapped as online Resource objects appear in the following order:

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

12. If an ADR element doesn't include the LABEL parameter, the full address is generated by concatenating the non-empty address components.

13. If TZ and GEO properties contains the ALTID parameter, they are associated to the address with the same ALTID value. If the ALTID parameter is missing or inconsistent, they are associated to the first address reported in the card.

14. Categories appear in the "categories" map according to the values of the PREF parameter of the CATEGORIES properties. 

15. Members appear n the "members" map according to the values of the PREF parameter of the MEMBER properties.

16. JSContact UTCDateTime type is mapped onto Java Calendar.


<a name="vcard-conversion"></a>
## JSContact Conversion

At present, the following converting methods are available:

*   JSContact2JCard
    *   String convertToJson(List<JSContact> jsContacts)
    *   JsonNode convertToJsonNode(List<JSContact> jsContacts)
*   JSContact2VCard
    *   String convertToText(List<JSContact> jsContacts)
*   JSContact2XCard
    *   String convertToXml(List<JSContact> jsContacts)

All the methods take in input a list of JSContact (JSCard or JSCardGroup) instances and can raise a `CardException`.
`JsonNode` represents the root of `com.fasterxml.jackson.databind`.

### Conversion Rules from JSContact to vCard 

1.  An unmatched property is converted into a vCard element with prefix `X-JSCONTACT-`


### Conversion examples

Here in the following two examples of conversion between jCard and JSContact top most objects.

```
    @Test
    public void testAddressesValid5() throws IOException, CardException {

        String jcard="[\"vcard\",[ [\"version\", {}, \"text\", \"4.0\"], " +
                "[\"fn\", {}, \"text\", \"test\"], " +
                "[\"adr\", {\"cc\": \"US\"}, \"text\", [\"\", \"\", \"54321 Oak St\", \"Reston\", \"VA\", \"20190\", \"USA\"]], " +
                "[\"tz\", {}, \"utc-offset\", \"-05:00\"]" +
                "]]";
        JSCard jsCard = (JSCard) jCard2JSContact.convert(jcard).get(0);
        assertTrue("testAddressesValid5 - 1",jsCard.getAddresses()!=null);
        assertTrue("testAddressesValid5 - 2",jsCard.getAddresses().length == 1);
        assertTrue("testAddressesValid5 - 3",jsCard.getAddresses()[0].getCountryCode().equals("US"));
        assertTrue("testAddressesValid5 - 4",jsCard.getAddresses()[0].getCountry().equals("USA"));
        assertTrue("testAddressesValid5 - 5",jsCard.getAddresses()[0].getPostcode().equals("20190"));
        assertTrue("testAddressesValid5 - 6",jsCard.getAddresses()[0].getLocality().equals("Reston"));
        assertTrue("testAddressesValid5 - 7",jsCard.getAddresses()[0].getRegion().equals("VA"));
        assertTrue("testAddressesValid5 - 8",jsCard.getAddresses()[0].getStreet().equals("54321 Oak St"));
        assertTrue("testAddressesValid5 - 9",jsCard.getAddresses()[0].getFullAddress().getValue().equals("54321 Oak St\nReston\nVA\n20190\nUSA"));
        assertTrue("testAddressesValid5 - 10",jsCard.getAddresses()[0].getTimeZone().equals("Etc/GMT+5"));
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
                
        List<JSContact> jsCards = jCard2JSContact.convert(jcard);
        assertTrue("testJCardGroupValid1 - 1",jsCards.size() == 3);
        assertTrue("testJCardGroupValid1 - 2",jsCards.get(0) instanceof JSCardGroup);
        JSCardGroup jsCardGroup = (JSCardGroup) jsCards.get(0);
        assertTrue("testJCardGroupValid1 - 3", jsCardGroup.getKind().isGroup());
        assertTrue("testJCardGroupValid1 - 4",StringUtils.isNotEmpty(jsCardGroup.getUid()));
        assertTrue("testJCardGroupValid1 - 5",jsCardGroup.getFullName().getValue().equals("The Doe family"));
        assertTrue("testJCardGroupValid1 - 6",jsCardGroup.getMembers().size() == 2);
        assertTrue("testJCardGroupValid1 - 7",jsCardGroup.getMembers().get("urn:uuid:03a0e51f-d1aa-4385-8a53-e29025acd8af") == Boolean.TRUE);
        assertTrue("testJCardGroupValid1 - 8",jsCardGroup.getMembers().get("rn:uuid:b8767877-b4a1-4c70-9acc-505d3819e519") == Boolean.TRUE);
        JSCard jsCard = (JSCard) jsCards.get(1);
        assertTrue("testJCardGroupValid1 - 9",jsCard.getUid().equals("urn:uuid:03a0e51f-d1aa-4385-8a53-e29025acd8af"));
        assertTrue("testJCardGroupValid1 - 10",jsCard.getFullName().getValue().equals("John Doe"));
        jsCard = (JSCard) jsCards.get(2);
        assertTrue("testJCardGroupValid1 - 11",jsCard.getUid().equals("urn:uuid:b8767877-b4a1-4c70-9acc-505d3819e519"));
        assertTrue("testJCardGroupValid1 - 12",jsCard.getFullName().getValue().equals("Jane Doe"));

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

*   [draft-ietf-jmap-jscontact-04](https://datatracker.ietf.org/doc/draft-ietf-jmap-jscontact/)
*   [draft-ietf-jmap-jscontact-vcard-02](https://datatracker.ietf.org/doc/draft-ietf-jmap-jscontact-vcard/)


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
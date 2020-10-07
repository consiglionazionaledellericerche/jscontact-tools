# jscontact-tools

Java tools for **JSContact** [draft-ietf-jmap-jscontact](https://datatracker.ietf.org/doc/draft-ietf-jmap-jscontact/) creation, validation, serialization/deserialization and conversion from vCard [RFC6350](https://datatracker.ietf.org/doc/rfc6350/), xCard [RFC6351](https://datatracker.ietf.org/doc/rfc6351/) e jCard [RFC7095](https://datatracker.ietf.org/doc/rfc7095/).
Validation and conversion leverage the features provided by [ez-vcard](https://github.com/mangstadt/ez-vcard) Java library.

# Maven/Gradle

## Maven

```
      <dependency>
		  <groupId>it.cnr.iit.jscontact</groupId>
		  <artifactId>jscontact-tools</artifactId>
		  <version>0.1.0</version>
      </dependency>
```

## Gradle

```
  compile 'it.cnr.iit.jscontact:jscontact-tools:0.1.0'
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

<a name="vcard-conversion"></a>
## vCard Conversion

At present, the following converting methods are available:

*   it.cnr.iit.jscontact.tools.vcard.converters.jcard2jscontact.JCard2JSContact
    *   List<JSContact> convert(String json)
    *   List<JSContact> convert(JsonNode jsonNode) 
*   VCard2JSContact
    *   List<JSContact> convert(String vcf)
*   XCard2JSContact
    *   List<JSContact> convert(String xml)

All the methods return a list of JSContact (JSCard or JSCardGroup) instances and can raise a `CardException`.
`JsonNode` represents the root of `com.fasterxml.jackson.databind`.

### Conversion Rules

The conversion is executed according to the following rules:

1.  The conversion is based on the content of the [JSContact I-Ds](#drafts).

2.  A card (i.e. vCard, xCard, jCard) is converted into a JSCardGroup object if it includes a KIND property set to `group`, otherwise into a JSCard object.

3.  A group card without members is converted into a card where the KIND property is set to `org`.

4.  The card components (i.e. properties, parameters or values) considered in the [RFCs](#rfcs) are matched.

5.  An unmatched property is converted into a topmost JSCard/JSCardGroup member with prefix `ietf.org/rfc6350`

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
    2. PHOTO
    3. IMPP
    4. LOGO
    5. SOUND
    6. URL
    7. KEY
    8. FBURL
    9. CALADRURI
    10. CALURI
    11. ORG-DIRECTORY
    12. CONTACT-URI

13. If an ADR element doesn't include the LABEL parameter, the full address is generated by concatenating the non-empty address components.

14. If TZ and GEO properties contains the ALTID parameter, they are associated to the address with the same ALTID value. If the ALTID parameter is missing or inconsistent, they are associated to the first address reported in the card.

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
        JSCardGroup jsCardGroup = (JSCardGroup) jCard2JSContact.convert(jcard).get(0);
        assertTrue("testJCardGroupValid1 - 1",jsCardGroup != null);
        assertTrue("testJCardGroupValid1 - 2", StringUtils.isNotEmpty(jsCardGroup.getUid()));
        assertTrue("testJCardGroupValid1 - 3",jsCardGroup.getName().equals("The Doe family"));
        assertTrue("testJCardGroupValid1 - 4",jsCardGroup.getCards().length == 2);
        assertTrue("testJCardGroupValid1 - 5",jsCardGroup.getCards()[0].getUid().equals("urn:uuid:03a0e51f-d1aa-4385-8a53-e29025acd8af"));
        assertTrue("testJCardGroupValid1 - 6",jsCardGroup.getCards()[0].getFullName().getValue().equals("John Doe"));
        assertTrue("testJCardGroupValid1 - 7",jsCardGroup.getCards()[1].getUid().equals("urn:uuid:b8767877-b4a1-4c70-9acc-505d3819e519"));
        assertTrue("testJCardGroupValid1 - 8",jsCardGroup.getCards()[1].getFullName().getValue().equals("Jane Doe"));

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

*   [draft-ietf-jmap-jscontact](https://datatracker.ietf.org/doc/draft-ietf-jmap-jscontact/)
*   [draft-ietf-jmap-jscontact-vcard](https://datatracker.ietf.org/doc/draft-ietf-jmap-jscontact-vcard/)


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
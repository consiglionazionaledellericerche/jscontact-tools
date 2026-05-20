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
package it.cnr.iit.jscontact.tools.test.converters.jscontact2vcard;

import ezvcard.VCard;
import ezvcard.VCardDataType;
import ezvcard.parameter.*;
import ezvcard.util.TelUri;
import ezvcard.util.VCardDateFormat;
import it.cnr.iit.jscontact.tools.dto.VCardParamEnum;
import it.cnr.iit.jscontact.tools.dto.VCardPropEnum;
import it.cnr.iit.jscontact.tools.dto.utils.DateUtils;
import it.cnr.iit.jscontact.tools.exceptions.CardException;
import it.cnr.iit.jscontact.tools.vcard.extensions.property.ExtendedAddress;
import it.cnr.iit.jscontact.tools.vcard.extensions.property.ExtendedStructuredName;
import it.cnr.iit.jscontact.tools.vcard.extensions.utils.VCardWriter;
import org.junit.Test;

import java.io.IOException;

import static org.junit.Assert.*;

public class UnconvertedParamsTest extends JSContact2VCardTest {
    

    @Test
    public void testMemberUnconvertedParams() throws IOException, CardException {

        String jscard="{" +
                "\"@type\":\"Card\"," +
                "\"version\" : \"2.0\"," +
                "\"kind\" : \"group\"," +
                "\"members\" : {" +
                    "\"mailto:subscriber1@example.com\" : true, " +
                    "\"xmpp:subscriber2@example.com\" : true, " +
                    "\"sip:subscriber3@example.com\" : true, " +
                    "\"tel:+1-418-555-5555\" : true " +
                "}," +
                "\"name\" : { " +
                    "\"@type\" : \"Name\", " +
                    "\"full\" : \"Funky distribution list\" " +
                "}," +
                "\"vCard\": { " +
                    "\"convertedProperties\": { " +
                        "\"members/mailto:subscriber1@example.com\" : { " +
                                "\"name\" : \"member\", " +
                                "\"parameters\" : { " +
                                    "\"pref\" : \"1\" " +
                            "}" +
                        "}," +
                        "\"members/xmpp:subscriber2@example.com\" : { " +
                            "\"name\" : \"member\", " +
                            "\"parameters\" : { " +
                                "\"pref\" : \"2\" " +
                            "}" +
                        "}" +
                    "}" +
                "}" +
                "}";
        VCard vcard = jsContact2VCard.convert(jscard).get(0);
        assertEquals("testMemberUnconvertedParams - 1", "Funky distribution list", vcard.getFormattedName().getValue());
        assertEquals("testMemberUnconvertedParams - 2", 4, vcard.getMembers().size());
        assertEquals("testMemberUnconvertedParams - 3", "mailto:subscriber1@example.com", vcard.getMembers().get(0).getValue());
        assertEquals("testMemberUnconvertedParams - 4", 1, vcard.getMembers().get(0).getPref().intValue());
        assertEquals("testMemberUnconvertedParams - 5", "xmpp:subscriber2@example.com", vcard.getMembers().get(1).getValue());
        assertEquals("testMemberUnconvertedParams - 6", 2, vcard.getMembers().get(1).getPref().intValue());
        assertEquals("testMemberUnconvertedParams - 7", "sip:subscriber3@example.com", vcard.getMembers().get(2).getValue());
        assertEquals("testMemberUnconvertedParams - 8", "tel:+1-418-555-5555", vcard.getMembers().get(3).getValue());
    }


    @Test
    public void testRelatedUnconvertedParams() throws IOException, CardException {

        String jscard="{" +
                "\"@type\":\"Card\"," +
                "\"uid\":\"8626d863-8c3f-405c-a2cb-bbbb3e3b359f\"," +
                "\"name\": { \"full\": \"test\"}," +
                "\"relatedTo\": { " +
                    "\"urn:uuid:f81d4fae-7dec-11d0-a765-00a0c91e6bf6\": {\"@type\":\"Relation\",\"relation\": { \"friend\": true , \"colleague\": true } }," +
                    "\"http://example.com/directory/jdoe.vcf\": {\"@type\":\"Relation\",\"relation\": { \"contact\": true} }, " +
                    "\"Please contact my assistant Jane Doe for any inquiries.\": {\"@type\":\"Relation\",\"relation\": null } " +
                "}," +
                "\"vCard\": { " +
                    "\"convertedProperties\": { " +
                        "\"relatedTo/urn:uuid:f81d4fae-7dec-11d0-a765-00a0c91e6bf6\" : { " +
                            "\"name\" : \"related\", " +
                            "\"parameters\" : { " +
                                "\"pref\" : \"1\" " +
                            "}" +
                        "}," +
                        "\"relatedTo/Please contact my assistant Jane Doe for any inquiries.\" : { " +
                            "\"name\" : \"related\", " +
                            "\"parameters\" : { " +
                                "\"pref\" : \"2\" " +
                            "}" +
                        "}" +
                    "}" +
                "}" +
                "}";
        VCard vcard = jsContact2VCard.convert(jscard).get(0);
        assertEquals("testRelatedTo3 - 1", 3, vcard.getRelations().size());
        assertEquals("testRelatedTo3 - 2", "urn:uuid:f81d4fae-7dec-11d0-a765-00a0c91e6bf6", vcard.getRelations().get(0).getUri());
        assertEquals("testRelatedTo3 - 3", 2, vcard.getRelations().get(0).getTypes().size());
        assertTrue("testRelatedTo3 - 4",vcard.getRelations().get(0).getTypes().contains(RelatedType.FRIEND));
        assertTrue("testRelatedTo3 - 5",vcard.getRelations().get(0).getTypes().contains(RelatedType.COLLEAGUE));
        assertEquals("testRelatedTo3 - 6",1, vcard.getRelations().get(0).getPref().intValue());
        assertEquals("testRelatedTo3 - 7", "http://example.com/directory/jdoe.vcf", vcard.getRelations().get(1).getUri());
        assertEquals("testRelatedTo3 - 8", 1, vcard.getRelations().get(1).getTypes().size());
        assertTrue("testRelatedTo3 - 9",vcard.getRelations().get(1).getTypes().contains(RelatedType.CONTACT));
        assertEquals("testRelatedTo3 - 10", "Please contact my assistant Jane Doe for any inquiries.", vcard.getRelations().get(2).getText());
        assertEquals("testRelatedTo3 - 11",2, vcard.getRelations().get(2).getPref().intValue());
    }

    @Test
    public void testKindUnconvertedParams() throws IOException, CardException {

        String jscard="{" +
                "\"@type\":\"Card\"," +
                "\"uid\":\"8626d863-8c3f-405c-a2cb-bbbb3e3b359f\"," +
                "\"name\": { \"full\": \"test\"}," +
                "\"kind\":\"individual\"," +
                "\"vCard\": { " +
                    "\"convertedProperties\": { " +
                        "\"kind\" : { " +
                            "\"name\" : \"kind\", " +
                            "\"parameters\" : { " +
                                "\"x-param\" : \"test\" " +
                            "}" +
                        "}" +
                    "}" +
                "}" +
                "}";
        VCard vcard = jsContact2VCard.convert(jscard).get(0);
        assertTrue("testKindUnconvertedParams - 1",vcard.getKind().isIndividual());
        assertEquals("testKindUnconvertedParams - 11","test", vcard.getKind().getParameter("X-PARAM"));
    }


    @Test
    public void testProdidUnconvertedParams() throws IOException, CardException {

        String jscard="{" +
                "\"@type\":\"Card\"," +
                "\"uid\":\"8626d863-8c3f-405c-a2cb-bbbb3e3b359f\"," +
                "\"name\": { \"full\": \"test\"}," +
                "\"prodId\":\"-//ONLINE DIRECTORY//NONSGML Version 1//EN\"," +
                "\"vCard\": { " +
                    "\"convertedProperties\": { " +
                        "\"prodId\" : { " +
                            "\"name\" : \"prodid\", " +
                            "\"parameters\" : { " +
                                "\"x-param\" : \"test\" " +
                            "}" +
                        "}" +
                    "}" +
                "}" +
                "}";
        VCard vcard = jsContact2VCard.convert(jscard).get(0);
        assertEquals("testProdidUnconvertedParams - 1", "-//ONLINE DIRECTORY//NONSGML Version 1//EN", vcard.getProductId().getValue());
        String text1 = VCardWriter.write(vcard);
        assertFalse("testProdidUnconvertedParams - 2", text1.contains("-//ONLINE DIRECTORY//NONSGML Version 1//EN"));
        assertTrue("testProdidUnconvertedParams - 3", text1.contains("ez-vcard 0.11.3"));
        assertEquals("testProdidUnconvertedParams - 4","test", vcard.getProductId().getParameter("X-PARAM"));
    }


    @Test
    public void testUpdatedUnconvertedParams() throws IOException, CardException {

        String jscard="{" +
                "\"@type\":\"Card\"," +
                "\"uid\":\"8626d863-8c3f-405c-a2cb-bbbb3e3b359f\"," +
                "\"name\": { \"full\": \"test\"}," +
                "\"updated\":\"1995-10-31T22:27:10Z\"," +
                "\"vCard\": { " +
                    "\"convertedProperties\": { " +
                        "\"updated\" : { " +
                            "\"name\" : \"rev\", " +
                            "\"parameters\" : { " +
                                "\"x-param\" : \"test\" " +
                            "}" +
                        "}" +
                    "}" +
                "}" +
                "}";
        VCard vcard = jsContact2VCard.convert(jscard).get(0);
        assertEquals("testUpdatedUnconvertedParams - 1", 0, vcard.getRevision().getCalendar().compareTo(DateUtils.toCalendar("1995-10-31T22:27:10Z")));
        assertEquals("testUpdatedUnconvertedParams - 2","test", vcard.getRevision().getParameter("X-PARAM"));
    }

    @Test
    public void testUidUnconvertedParams() throws IOException, CardException {

        String jscard="{" +
                "\"@type\":\"Card\"," +
                "\"uid\":\"urn:uuid:03a0e51f-d1aa-4385-8a53-e29025acd8af\"," +
                "\"name\": { \"full\": \"test\"}," +
                "\"vCard\": { " +
                    "\"convertedProperties\": { " +
                        "\"uid\" : { " +
                            "\"name\" : \"uid\", " +
                            "\"parameters\" : { " +
                                "\"x-param\" : \"test\" " +
                            "}" +
                        "}" +
                    "}" +
                "}" +
                "}";
        VCard vcard = jsContact2VCard.convert(jscard).get(0);
        assertEquals("testUidUnconvertedParams - 1", "urn:uuid:03a0e51f-d1aa-4385-8a53-e29025acd8af", vcard.getUid().getValue());
        assertEquals("testUidUnconvertedParams - 2","test", vcard.getUid().getParameter("X-PARAM"));
    }

    @Test
    public void testEmailAddressUnconvertedParams() throws IOException, CardException {

        String jscard="{" +
                "\"@type\":\"Card\"," +
                "\"uid\":\"8626d863-8c3f-405c-a2cb-bbbb3e3b359f\"," +
                "\"name\": { \"full\": \"test\"}," +
                "\"emails\":{ \"EMAIL-1\": {\"@type\":\"EmailAddress\",\"contexts\": {\"work\": true},\"address\":\"jqpublic@xyz.example.com\"}}," +
                "\"vCard\": { " +
                    "\"convertedProperties\": { " +
                        "\"emails/EMAIL-1\" : { " +
                            "\"name\" : \"email\", " +
                            "\"parameters\" : { " +
                                "\"pid\" : \"1\" " +
                            "}" +
                        "}" +
                    "}" +
                "}" +
                "}";

        VCard vcard = jsContact2VCard.convert(jscard).get(0);
        assertEquals("testEmailAddressUnconvertedParams - 1", 1, vcard.getEmails().size());
        assertEquals("testEmailAddressUnconvertedParams - 2", "jqpublic@xyz.example.com", vcard.getEmails().get(0).getValue());
        assertEquals("testEmailAddressUnconvertedParams - 3", 1, vcard.getEmails().get(0).getTypes().size());
        assertEquals("testEmailAddressUnconvertedParams - 4", "work", vcard.getEmails().get(0).getTypes().get(0).getValue());
        assertEquals("testEmailAddressUnconvertedParams - 5", "EMAIL-1", vcard.getEmails().get(0).getParameter(VCardParamEnum.JSID.getValue()));
        assertEquals("testEmailAddressUnconvertedParams - 6","1", vcard.getEmails().get(0).getPids().get(0).toString());
    }

    @Test
    public void testPhoneUnconvertedParams() throws IOException, CardException {

        String jscard="{" +
                "\"@type\":\"Card\"," +
                "\"uid\":\"8626d863-8c3f-405c-a2cb-bbbb3e3b359f\"," +
                "\"name\": { \"full\": \"test\"}," +
                "\"phones\":{\"PHONE-1\": {\"@type\":\"Phone\",\"contexts\":{\"private\": true},\"features\":{\"voice\": true},\"number\":\"tel:+33-01-23-45-6\"}}," +
                "\"vCard\": { " +
                    "\"convertedProperties\": { " +
                        "\"phones/PHONE-1\" : { " +
                            "\"name\" : \"tel\", " +
                            "\"parameters\" : { " +
                                "\"pid\" : \"1\" " +
                            "}" +
                        "}" +
                    "}" +
                "}" +
                "}";
        VCard vcard = jsContact2VCard.convert(jscard).get(0);
        assertEquals("testPhoneUnconvertedParams - 1", 1, vcard.getTelephoneNumbers().size());
        assertEquals("testPhoneUnconvertedParams - 2", vcard.getTelephoneNumbers().get(0).getUri(), TelUri.parse("tel:+33-01-23-45-6"));
        assertTrue("testPhoneUnconvertedParams - 3", vcard.getTelephoneNumbers().get(0).getTypes().contains(TelephoneType.HOME));
        assertTrue("testPhoneUnconvertedParams - 4", vcard.getTelephoneNumbers().get(0).getTypes().contains(TelephoneType.VOICE));
        assertEquals("testPhoneUnconvertedParams - 5", "PHONE-1", vcard.getTelephoneNumbers().get(0).getParameter(VCardParamEnum.JSID.getValue()));
        assertEquals("testPhoneUnconvertedParams - 6","1", vcard.getTelephoneNumbers().get(0).getPids().get(0).toString());
    }

    @Test
    public void testPersonalInfoUnconvertedParams() throws IOException, CardException {

        String jscard="{" +
                "\"@type\":\"Card\"," +
                "\"uid\":\"8626d863-8c3f-405c-a2cb-bbbb3e3b359f\"," +
                "\"name\": { \"full\": \"test\"}," +
                "\"personalInfo\":{ " +
                    "\"PERSINFO-1\": {" +
                        "\"@type\":\"PersonalInfo\"," +
                        "\"kind\": \"expertise\"," +
                        "\"value\": \"chemistry\"," +
                        "\"level\": \"high\" " +
                    "}," +
                    "\"PERSINFO-2\": {" +
                        "\"@type\":\"PersonalInfo\"," +
                        "\"kind\": \"expertise\"," +
                        "\"value\": \"chinese literature\"," +
                        "\"level\": \"low\"" +
                    "}," +
                    "\"PERSINFO-3\": {" +
                        "\"@type\":\"PersonalInfo\"," +
                        "\"kind\": \"hobby\"," +
                        "\"value\": \"reading\"," +
                        "\"level\": \"high\"" +
                    "}," +
                    "\"PERSINFO-4\": {" +
                        "\"@type\":\"PersonalInfo\"," +
                        "\"kind\": \"hobby\"," +
                        "\"value\": \"sewing\"," +
                        "\"level\": \"high\"" +
                    "}," +
                    "\"PERSINFO-5\": {" +
                        "\"@type\":\"PersonalInfo\"," +
                        "\"kind\": \"interest\"," +
                        "\"value\": \"r&b music\"," +
                        "\"level\": \"medium\" " +
                    "}," +
                    "\"PERSINFO-6\": {" +
                        "\"@type\":\"PersonalInfo\"," +
                        "\"kind\": \"interest\"," +
                        "\"value\": \"rock 'n' roll music\"," +
                        "\"level\": \"high\" " +
                    "}" +
                "}," +
                "\"vCard\": { " +
                    "\"convertedProperties\": { " +
                        "\"personalInfo/PERSINFO-3\" : { " +
                            "\"name\" : \"hobby\", " +
                            "\"parameters\" : { " +
                                "\"pid\" : \"1\" " +
                            "}" +
                        "}," +
                        "\"personalInfo/PERSINFO-6\" : { " +
                            "\"name\" : \"interest\", " +
                            "\"parameters\" : { " +
                                "\"pid\" : \"2\" " +
                            "}" +
                        "}," +
                        "\"personalInfo/PERSINFO-1\" : { " +
                            "\"name\" : \"expertise\", " +
                            "\"parameters\" : { " +
                                "\"x-param\" : \"test\" " +
                            "}" +
                        "}" +
                    "}" +
                "}" +
                "}";
        VCard vcard = jsContact2VCard.convert(jscard).get(0);
        assertEquals("testPersonalInfoUnconvertedParams - 1", 2, vcard.getExpertise().size());
        assertEquals("testPersonalInfoUnconvertedParams - 2", "chemistry", vcard.getExpertise().get(0).getValue());
        assertSame("testPersonalInfoUnconvertedParams - 3", vcard.getExpertise().get(0).getLevel(), ExpertiseLevel.EXPERT);
        assertEquals("testPersonalInfoUnconvertedParams - 4", "chinese literature", vcard.getExpertise().get(1).getValue());
        assertSame("testPersonalInfoUnconvertedParams - 5", vcard.getExpertise().get(1).getLevel(), ExpertiseLevel.BEGINNER);
        assertEquals("testPersonalInfoUnconvertedParams - 6", 2, vcard.getHobbies().size());
        assertEquals("testPersonalInfoUnconvertedParams - 7", "reading", vcard.getHobbies().get(0).getValue());
        assertSame("testPersonalInfoUnconvertedParams - 8", vcard.getHobbies().get(0).getLevel(), HobbyLevel.HIGH);
        assertEquals("testPersonalInfoUnconvertedParams - 9", "sewing", vcard.getHobbies().get(1).getValue());
        assertSame("testPersonalInfoUnconvertedParams - 10", vcard.getHobbies().get(1).getLevel(), HobbyLevel.HIGH);
        assertEquals("testPersonalInfoUnconvertedParams - 11", 2, vcard.getInterests().size());
        assertEquals("testPersonalInfoUnconvertedParams - 12", "r&b music", vcard.getInterests().get(0).getValue());
        assertSame("testPersonalInfoUnconvertedParams - 13", vcard.getInterests().get(0).getLevel(), InterestLevel.MEDIUM);
        assertEquals("testPersonalInfoUnconvertedParams - 14", "rock 'n' roll music", vcard.getInterests().get(1).getValue());
        assertSame("testPersonalInfoUnconvertedParams - 15", vcard.getInterests().get(1).getLevel(), InterestLevel.HIGH);
        assertEquals("testPersonalInfoUnconvertedParams - 16", "PERSINFO-1", vcard.getExpertise().get(0).getParameter(VCardParamEnum.JSID.getValue()));
        assertEquals("testPersonalInfoUnconvertedParams - 17", "PERSINFO-2", vcard.getExpertise().get(1).getParameter(VCardParamEnum.JSID.getValue()));
        assertEquals("testPersonalInfoUnconvertedParams - 18", "PERSINFO-3", vcard.getHobbies().get(0).getParameter(VCardParamEnum.JSID.getValue()));
        assertEquals("testPersonalInfoUnconvertedParams - 19", "PERSINFO-4", vcard.getHobbies().get(1).getParameter(VCardParamEnum.JSID.getValue()));
        assertEquals("testPersonalInfoUnconvertedParams - 20", "PERSINFO-5", vcard.getInterests().get(0).getParameter(VCardParamEnum.JSID.getValue()));
        assertEquals("testPersonalInfoUnconvertedParams - 21", "PERSINFO-6", vcard.getInterests().get(1).getParameter(VCardParamEnum.JSID.getValue()));
        assertEquals("testPersonalInfoUnconvertedParams - 22","1", vcard.getHobbies().get(0).getParameter(VCardParamEnum.PID.getValue()));
        assertEquals("testPersonalInfoUnconvertedParams - 23","test", vcard.getExpertise().get(0).getParameter("X-PARAM"));
        assertEquals("testPersonalInfoUnconvertedParams - 24","2", vcard.getInterests().get(1).getParameter(VCardParamEnum.PID.getValue()));
    }

    public void testSourceUnconvertedParams() throws IOException, CardException {

        String jscard="{" +
                "\"@type\":\"Card\"," +
                "\"uid\":\"8626d863-8c3f-405c-a2cb-bbbb3e3b359f\"," +
                "\"name\": { \"full\": \"test\"}," +
                "\"directories\": {"+
                    "\"ENTRY-1\": {" +
                        "\"@type\":\"Directory\"," +
                        "\"kind\": \"entry\","+
                        "\"uri\": \"http://directory.example.com/addressbooks/jdoe/Jean%20Dupont.vcf\"" +
                    "}" +
                "}," +
                "\"vCard\": { " +
                    "\"convertedProperties\": { " +
                        "\"directories/ENTRY-1\" : { " +
                        "\"name\" : \"source\", " +
                            "\"parameters\" : { " +
                                "\"pid\" : \"1\" " +
                            "}" +
                        "}" +
                    "}" +
                "}" +
                "}";
        VCard vcard = jsContact2VCard.convert(jscard).get(0);
        assertEquals("testSourceUnconvertedParams - 1", 1, vcard.getSources().size());
        assertEquals("testSourceUnconvertedParams - 2", "http://directory.example.com/addressbooks/jdoe/Jean%20Dupont.vcf", vcard.getSources().get(0).getValue());
        assertEquals("testSourceUnconvertedParams - 3", "ENTRY-1", vcard.getSources().get(0).getParameter(VCardParamEnum.JSID.getValue()));
        assertEquals("testSourceUnconvertedParams - 4","1", vcard.getSources().get(0).getPids().get(0).toString());
    }

    @Test
    public void testLogoUnconvertedParams() throws IOException, CardException {

        String jscard="{" +
                "\"@type\":\"Card\"," +
                "\"uid\":\"8626d863-8c3f-405c-a2cb-bbbb3e3b359f\"," +
                "\"name\": { \"full\": \"test\"}," +
                "\"media\": {"+
                    "\"LOGO-1\": {" +
                        "\"@type\":\"Media\"," +
                        "\"kind\": \"logo\","+
                        "\"uri\": \"http://www.example.com/pub/logos/abccorp.jpg\"" +
                    "}" +
                "}," +
                "\"vCard\": { " +
                    "\"convertedProperties\": { " +
                        "\"media/LOGO-1\" : { " +
                            "\"name\" : \"logo\", " +
                            "\"parameters\" : { " +
                                "\"pid\" : \"1\" " +
                            "}" +
                        "}" +
                    "}" +
                "}" +
                "}";
        VCard vcard = jsContact2VCard.convert(jscard).get(0);
        assertEquals("testLogoUnconvertedParams - 1", 1, vcard.getLogos().size());
        assertEquals("testLogoUnconvertedParams - 2", "http://www.example.com/pub/logos/abccorp.jpg", vcard.getLogos().get(0).getUrl());
        assertEquals("testLogoUnconvertedParams - 3", "LOGO-1", vcard.getLogos().get(0).getParameter(VCardParamEnum.JSID.getValue()));
        assertEquals("testLogoUnconvertedParams - 4","1", vcard.getLogos().get(0).getPids().get(0).toString());
    }

    @Test
    public void testPhotoUnconvertedParams() throws IOException, CardException {

        String jscard="{" +
                "\"@type\":\"Card\"," +
                "\"uid\":\"8626d863-8c3f-405c-a2cb-bbbb3e3b359f\"," +
                "\"name\": { \"full\": \"test\"}," +
                "\"media\": {"+
                    "\"PHOTO-1\": {" +
                        "\"@type\":\"Media\"," +
                        "\"kind\":\"photo\"," +
                        "\"mediaType\": \"image/gif\","+
                        "\"uri\": \"http://www.example.com/pub/photos/jqpublic.gif\"" +
                    "}" +
                "}," +
                "\"vCard\": { " +
                    "\"convertedProperties\": { " +
                        "\"media/PHOTO-1\" : { " +
                            "\"name\" : \"photo\", " +
                            "\"parameters\" : { " +
                                "\"pid\" : \"1\" " +
                            "}" +
                        "}" +
                    "}" +
                "}" +
                "}";
        VCard vcard = jsContact2VCard.convert(jscard).get(0);
        assertEquals("testPhotoUnconvertedParams - 1", 1, vcard.getPhotos().size());
        assertEquals("testPhotoUnconvertedParams - 2", "http://www.example.com/pub/photos/jqpublic.gif", vcard.getPhotos().get(0).getUrl());
        assertSame("testPhotoUnconvertedParams - 3", vcard.getPhotos().get(0).getContentType(), ImageType.GIF);
        assertEquals("testPhotoUnconvertedParams - 4", "PHOTO-1", vcard.getPhotos().get(0).getParameter(VCardParamEnum.JSID.getValue()));
        assertEquals("testPhotoUnconvertedParams - 5","1", vcard.getPhotos().get(0).getPids().get(0).toString());
    }

    @Test
    public void testSoundUnconvertedParams() throws IOException, CardException {

        String jscard="{" +
                "\"@type\":\"Card\"," +
                "\"uid\":\"8626d863-8c3f-405c-a2cb-bbbb3e3b359f\"," +
                "\"name\": { \"full\": \"test\"}," +
                "\"media\": {"+
                    "\"SOUND-1\": {" +
                        "\"@type\":\"Media\"," +
                        "\"kind\": \"sound\","+
                        "\"mediaType\": \"audio/mp3\"," +
                        "\"uri\": \"android.resource:///com.my.android.sharesound/2130968609\"" +
                    "}" +
                "}," +
                "\"vCard\": { " +
                    "\"convertedProperties\": { " +
                        "\"media/SOUND-1\" : { " +
                            "\"name\" : \"sound\", " +
                            "\"parameters\" : { " +
                                "\"pid\" : \"1\" " +
                            "}" +
                        "}" +
                    "}" +
                "}" +
                "}";
        VCard vcard = jsContact2VCard.convert(jscard).get(0);
        assertEquals("testSoundUnconvertedParams - 1", 1, vcard.getSounds().size());
        assertEquals("testSoundUnconvertedParams - 2", "android.resource:///com.my.android.sharesound/2130968609", vcard.getSounds().get(0).getUrl());
        assertSame("testSoundUnconvertedParams - 3", vcard.getSounds().get(0).getContentType(), SoundType.MP3);
        assertEquals("testSoundUnconvertedParams - 4", "SOUND-1", vcard.getSounds().get(0).getParameter(VCardParamEnum.JSID.getValue()));
        assertEquals("testSoundUnconvertedParams - 5","1", vcard.getSounds().get(0).getPids().get(0).toString());
    }

    @Test
    public void testContactUriUnconvertedParams() throws IOException, CardException {

        String jscard="{" +
                "\"@type\":\"Card\"," +
                "\"uid\":\"8626d863-8c3f-405c-a2cb-bbbb3e3b359f\"," +
                "\"name\": { \"full\": \"test\"}," +
                "\"links\": {"+
                    "\"CONTACT-1\": {" +
                        "\"@type\":\"Link\"," +
                        "\"kind\": \"contact\","+
                        "\"uri\": \"mailto:contact@example.com\"" +
                    "}" +
                "}," +
                "\"vCard\": { " +
                    "\"convertedProperties\": { " +
                        "\"links/CONTACT-1\" : { " +
                            "\"name\" : \"contact-uri\", " +
                            "\"parameters\" : { " +
                                "\"x-param\" : \"test\" " +
                            "}" +
                        "}" +
                    "}" +
                "}" +
                "}";
        VCard vcard = jsContact2VCard.convert(jscard).get(0);
        assertEquals("testContactUriUnconvertedParams - 1", 1, vcard.getExtendedProperties().size());
        assertEquals("testContactUriUnconvertedParams - 2", "CONTACT-URI", vcard.getExtendedProperties().get(0).getPropertyName());
        assertEquals("testContactUriUnconvertedParams - 2", "mailto:contact@example.com", vcard.getExtendedProperties().get(0).getValue());
        assertEquals("testContactUriUnconvertedParams - 4", "CONTACT-1", vcard.getExtendedProperties().get(0).getParameter(VCardParamEnum.JSID.getValue()));
        assertEquals("testContactUriUnconvertedParams - 5","test", vcard.getExtendedProperties().get(0).getParameter("X-PARAM"));
    }

    @Test
    public void testUrlUnconvertedParams() throws IOException, CardException {

        String jscard="{" +
                "\"@type\":\"Card\"," +
                "\"uid\":\"8626d863-8c3f-405c-a2cb-bbbb3e3b359f\"," +
                "\"name\": { \"full\": \"test\"}," +
                "\"links\": {"+
                    "\"LINK-1\": {" +
                        "\"uri\": \"https://example.com\"" +
                    "}" +
                "}," +
                "\"vCard\": { " +
                    "\"convertedProperties\": { " +
                        "\"links/LINK-1\" : { " +
                            "\"name\" : \"url\", " +
                            "\"parameters\" : { " +
                                "\"pid\" : \"1\" " +
                            "}" +
                        "}" +
                    "}" +
                "}" +
                "}";
        VCard vcard = jsContact2VCard.convert(jscard).get(0);
        assertEquals("testUrlUnconvertedParams - 1", "LINK-1", vcard.getUrls().get(0).getParameter(VCardParamEnum.JSID.getValue()));
        assertEquals("testUrlUnconvertedParams - 2", "https://example.com", vcard.getUrls().get(0).getValue());
        assertEquals("testUrlUnconvertedParams - 3","1", vcard.getUrls().get(0).getPids().get(0).toString());
    }

    @Test
    public void testKeyUnconvertedParams() throws IOException, CardException {

        String jscard="{" +
                "\"@type\":\"Card\"," +
                "\"uid\":\"8626d863-8c3f-405c-a2cb-bbbb3e3b359f\"," +
                "\"name\": { \"full\": \"test\"}," +
                "\"cryptoKeys\": {"+
                    "\"KEY-1\": {" +
                        "\"@type\":\"CryptoKey\"," +
                        "\"uri\": \"http://www.example.com/keys/jdoe.cer\"" +
                    "}" +
                "}," +
                "\"vCard\": { " +
                    "\"convertedProperties\": { " +
                        "\"cryptoKeys/KEY-1\" : { " +
                            "\"name\" : \"key\", " +
                            "\"parameters\" : { " +
                                "\"pid\" : \"1\" " +
                            "}" +
                        "}" +
                    "}" +
                "}" +
                "}";
        VCard vcard = jsContact2VCard.convert(jscard).get(0);
        assertEquals("testKeyUnconvertedParams - 1", 1, vcard.getKeys().size());
        assertEquals("testKeyUnconvertedParams - 2", "http://www.example.com/keys/jdoe.cer", vcard.getKeys().get(0).getUrl());
        assertEquals("testKeyUnconvertedParams - 3", "KEY-1", vcard.getKeys().get(0).getParameter(VCardParamEnum.JSID.getValue()));
        assertEquals("testUrlUnconvertedParams - 3","1", vcard.getKeys().get(0).getPids().get(0).toString());
    }

    public void testOrgDirectoryUnconvertedParams() throws IOException, CardException {

        String jscard="{" +
                "\"@type\":\"Card\"," +
                "\"uid\":\"8626d863-8c3f-405c-a2cb-bbbb3e3b359f\"," +
                "\"name\": { \"full\": \"test\"}," +
                "\"directories\": {"+
                    "\"ENTRY-1\": {" +
                        "\"@type\":\"Directory\"," +
                        "\"kind\": \"directory\","+
                        "\"uri\": \"http://directory.example.com/addressbooks/jdoe/Jean%20Dupont.vcf\"" +
                    "}" +
                "}," +
                "\"vCard\": { " +
                    "\"convertedProperties\": { " +
                        "\"directories/ENTRY-1\" : { " +
                            "\"name\" : \"org-directory\", " +
                            "\"parameters\" : { " +
                                "\"pid\" : \"1\" " +
                            "}" +
                        "}" +
                    "}" +
                "}" +
                "}";
        VCard vcard = jsContact2VCard.convert(jscard).get(0);
        assertEquals("testOrgDirectoryUnconvertedParams - 1", 1, vcard.getSources().size());
        assertEquals("testOrgDirectoryUnconvertedParams - 2", "http://directory.example.com/addressbooks/jdoe/Jean%20Dupont.vcf", vcard.getSources().get(0).getValue());
        assertEquals("testOrgDirectoryUnconvertedParams - 3", "ENTRY-1", vcard.getOrgDirectories().get(0).getParameter(VCardParamEnum.JSID.getValue()));
        assertEquals("testOrgDirectoryUnconvertedParams - 4","1", vcard.getOrgDirectories().get(0).getParameter(VCardParamEnum.PID.getValue()));
    }

    @Test
    public void testFbUrlUnconvertedParams() throws IOException, CardException {

        String jscard="{" +
                "\"@type\":\"Card\"," +
                "\"uid\":\"8626d863-8c3f-405c-a2cb-bbbb3e3b359f\"," +
                "\"name\": { \"full\": \"test\"}," +
                "\"calendars\": {"+
                    "\"FREEBUSY-1\": {" +
                        "\"@type\":\"Calendar\"," +
                        "\"kind\": \"freeBusy\","+
                        "\"pref\": 1," +
                        "\"uri\": \"http://www.example.com/busy/janedoe\"" +
                    "}," +
                    "\"FREEBUSY-2\": {" +
                        "\"@type\":\"Calendar\"," +
                        "\"kind\": \"freeBusy\","+
                        "\"mediaType\": \"text/calendar\"," +
                        "\"uri\": \"ftp://example.com/busy/project-a.ifb\"" +
                    "}" +
                "}," +
                "\"vCard\": { " +
                    "\"convertedProperties\": { " +
                        "\"calendars/FREEBUSY-1\" : { " +
                            "\"name\" : \"fburl\", " +
                            "\"parameters\" : { " +
                                "\"pid\" : \"1\" " +
                            "}" +
                        "}" +
                    "}" +
                "}" +
                "}";
        VCard vcard = jsContact2VCard.convert(jscard).get(0);
        assertEquals("testFbUrlUnconvertedParams - 1", 2, vcard.getFbUrls().size());
        assertEquals("testFbUrlUnconvertedParams - 2", "http://www.example.com/busy/janedoe", vcard.getFbUrls().get(0).getValue());
        assertEquals("testFbUrlUnconvertedParams - 3", 1, (int) vcard.getFbUrls().get(0).getPref());
        assertEquals("testFbUrlUnconvertedParams - 4", "ftp://example.com/busy/project-a.ifb", vcard.getFbUrls().get(1).getValue());
        assertEquals("testFbUrlUnconvertedParams - 5", "text/calendar", vcard.getFbUrls().get(1).getMediaType());
        assertEquals("testFbUrlUnconvertedParams - 6", "FREEBUSY-1", vcard.getFbUrls().get(0).getParameter(VCardParamEnum.JSID.getValue()));
        assertEquals("testFbUrlUnconvertedParams - 7", "FREEBUSY-2", vcard.getFbUrls().get(1).getParameter(VCardParamEnum.JSID.getValue()));
        assertEquals("testFbUrlUnconvertedParams - 8","1", vcard.getFbUrls().get(0).getParameter(VCardParamEnum.PID.getValue()));
    }

    @Test
    public void testCaluriUnconvertedParams() throws IOException, CardException {

        String jscard="{" +
                "\"@type\":\"Card\"," +
                "\"uid\":\"8626d863-8c3f-405c-a2cb-bbbb3e3b359f\"," +
                "\"name\": { \"full\": \"test\"}," +
                "\"calendars\": {"+
                    "\"CALENDAR-1\": {" +
                        "\"@type\":\"Calendar\"," +
                        "\"kind\": \"calendar\","+
                        "\"pref\": 1," +
                        "\"uri\": \"http://cal.example.com/calA\"" +
                    "}," +
                    "\"CALENDAR-2\": {" +
                        "\"@type\":\"Calendar\"," +
                        "\"kind\": \"calendar\","+
                        "\"mediaType\": \"text/calendar\"," +
                        "\"uri\": \"ftp://ftp.example.com/calA.ics\"" +
                    "}" +
                "}," +
                "\"vCard\": { " +
                    "\"convertedProperties\": { " +
                        "\"calendars/CALENDAR-1\" : { " +
                            "\"name\" : \"fburl\", " +
                            "\"parameters\" : { " +
                                "\"pid\" : \"1\" " +
                            "}" +
                        "}" +
                    "}" +
                "}" +
                "}";
        VCard vcard = jsContact2VCard.convert(jscard).get(0);
        assertEquals("testCaluriUnconvertedParams - 1", 2, vcard.getCalendarUris().size());
        assertEquals("testCaluriUnconvertedParams - 2", "http://cal.example.com/calA", vcard.getCalendarUris().get(0).getValue());
        assertEquals("testCaluriUnconvertedParams - 3", 1, (int) vcard.getCalendarUris().get(0).getPref());
        assertEquals("testCaluriUnconvertedParams - 4", "ftp://ftp.example.com/calA.ics", vcard.getCalendarUris().get(1).getValue());
        assertEquals("testCaluriUnconvertedParams - 5", "text/calendar", vcard.getCalendarUris().get(1).getMediaType());
        assertEquals("testCaluriUnconvertedParams - 6", "CALENDAR-1", vcard.getCalendarUris().get(0).getParameter(VCardParamEnum.JSID.getValue()));
        assertEquals("testCaluriUnconvertedParams - 7", "CALENDAR-2", vcard.getCalendarUris().get(1).getParameter(VCardParamEnum.JSID.getValue()));
        assertEquals("testCaluriUnconvertedParams - 8","1", vcard.getCalendarUris().get(0).getParameter(VCardParamEnum.PID.getValue()));
    }

    @Test
    public void testCaladruriUnconvertedParams() throws IOException, CardException {

        String jscard="{" +
                "\"@type\":\"Card\"," +
                "\"uid\":\"8626d863-8c3f-405c-a2cb-bbbb3e3b359f\"," +
                "\"name\": { \"full\": \"test\"}," +
                "\"schedulingAddresses\": {"+
                    "\"SCHEDULING-1\": {" +
                        "\"@type\":\"SchedulingAddress\"," +
                        "\"pref\": 1," +
                        "\"uri\": \"mailto:janedoe@example.com\"" +
                    "}," +
                    "\"SCHEDULING-2\": {" +
                        "\"@type\":\"SchedulingAddress\"," +
                        "\"uri\": \"http://example.com/calendar/jdoe\"" +
                    "}" +
                "}," +
                "\"vCard\": { " +
                    "\"convertedProperties\": { " +
                        "\"schedulingAddresses/SCHEDULING-1\" : { " +
                            "\"name\" : \"caladruri\", " +
                            "\"parameters\" : { " +
                                "\"pid\" : \"1\" " +
                            "}" +
                        "}" +
                    "}" +
                "}" +
                "}";
        VCard vcard = jsContact2VCard.convert(jscard).get(0);
        assertEquals("testCaladruriUnconvertedParams - 1", 2, vcard.getCalendarRequestUris().size());
        assertEquals("testCaladruriUnconvertedParams - 2", "mailto:janedoe@example.com", vcard.getCalendarRequestUris().get(0).getValue());
        assertEquals("testCaladruriUnconvertedParams - 3", 1, (int) vcard.getCalendarRequestUris().get(0).getPref());
        assertEquals("testCaladruriUnconvertedParams - 4", "http://example.com/calendar/jdoe", vcard.getCalendarRequestUris().get(1).getValue());
        assertEquals("testCaladruriUnconvertedParams - 5", "SCHEDULING-1", vcard.getCalendarRequestUris().get(0).getParameter(VCardParamEnum.JSID.getValue()));
        assertEquals("testCaladruriUnconvertedParams - 6", "SCHEDULING-2", vcard.getCalendarRequestUris().get(1).getParameter(VCardParamEnum.JSID.getValue()));
        assertEquals("testCaladruriUnconvertedParams - 7","1", vcard.getCalendarRequestUris().get(0).getParameter(VCardParamEnum.PID.getValue()));
    }

    @Test
    public void testImppUnconvertedParams() throws IOException, CardException {

        String jscard="{" +
                "\"@type\":\"Card\"," +
                "\"uid\":\"8626d863-8c3f-405c-a2cb-bbbb3e3b359f\"," +
                "\"name\": { \"full\": \"test\"}," +
                "\"onlineServices\": {"+
                    "\"OS-1\": {" +
                        "\"@type\":\"OnlineService\"," +
                        "\"contexts\": {\"private\": true}," +
                        "\"pref\": 1, " +
                        "\"uri\": \"xmpp:alice@example.com\"" +
                    "}" +
                "}," +
                "\"vCard\": { " +
                    "\"convertedProperties\": { " +
                        "\"onlineServices/OS-1\": { " +
                            "\"name\": \"impp\", " +
                            "\"parameters\" : { " +
                                "\"pid\" : \"1\" " +
                            "}" +
                        "}" +
                    "}" +
                "}" +
                "}";
        VCard vcard = jsContact2VCard.convert(jscard).get(0);
        assertEquals("testImppUnconvertedParams - 1", 1, vcard.getImpps().size());
        assertEquals("testImppUnconvertedParams - 2", "xmpp:alice@example.com", vcard.getImpps().get(0).getUri().toString());
        assertEquals("testImppUnconvertedParams - 3", "home", vcard.getImpps().get(0).getParameter(VCardParamEnum.TYPE.getValue()));
        assertEquals("testImppUnconvertedParams - 4", 1, (int) vcard.getImpps().get(0).getPref());
        assertEquals("testImppUnconvertedParams - 5", "OS-1", vcard.getImpps().get(0).getParameter(VCardParamEnum.JSID.getValue()));
        assertEquals("testImppUnconvertedParams - 6","1", vcard.getImpps().get(0).getParameter(VCardParamEnum.PID.getValue()));
    }

    @Test
    public void testSocialProfileUnconvertedParams() throws IOException, CardException {

        String jscard="{" +
                "\"@type\":\"Card\"," +
                "\"uid\":\"8626d863-8c3f-405c-a2cb-bbbb3e3b359f\"," +
                "\"name\": { \"full\": \"test\"}," +
                "\"onlineServices\": {"+
                    "\"OS-1\": {" +
                        "\"@type\":\"OnlineService\"," +
                        "\"service\": \"Twitter\", " +
                        "\"contexts\": {\"private\": true}," +
                        "\"pref\": 1, " +
                        "\"uri\": \"https://twitter.com/ietf\"" +
                    "}" +
                "}," +
                "\"vCard\": { " +
                    "\"convertedProperties\": { " +
                        "\"onlineServices/OS-1\": { " +
                            "\"name\": \"socialprofile\", " +
                            "\"parameters\" : { " +
                                "\"pid\" : \"1\" " +
                            "}" +
                        "}" +
                    "}" +
                "}" +
                "}";
        VCard vcard = jsContact2VCard.convert(jscard).get(0);
        assertEquals("testSocialProfileUnconvertedParams - 1", 1, vcard.getExtendedProperties().size());
        assertEquals("testSocialProfileUnconvertedParams - 2", VCardPropEnum.SOCIALPROFILE.getValue(), vcard.getExtendedProperties().get(0).getPropertyName());
        assertEquals("testSocialProfileUnconvertedParams - 3", "home", vcard.getExtendedProperties().get(0).getParameter(VCardParamEnum.TYPE.getValue()));
        assertEquals("testSocialProfileUnconvertedParams - 4", "1", vcard.getExtendedProperties().get(0).getParameter(VCardParamEnum.PREF.getValue()));
        assertEquals("testSocialProfileUnconvertedParams - 5", "OS-1", vcard.getExtendedProperties().get(0).getParameter(VCardParamEnum.JSID.getValue()));
        assertEquals("testSocialProfileUnconvertedParams - 6", "Twitter", vcard.getExtendedProperties().get(0).getParameter(VCardParamEnum.SERVICE_TYPE.getValue()));
        assertEquals("testSocialProfileUnconvertedParams - 7", VCardDataType.URI, vcard.getExtendedProperties().get(0).getDataType());
        assertEquals("testSocialProfileUnconvertedParams - 8","1", vcard.getExtendedProperties().get(0).getParameter(VCardParamEnum.PID.getValue()));
    }
    
    @Test
    public void testCreatedUnconvertedParams() throws IOException, CardException {

        String jscard="{" +
                "\"@type\":\"Card\"," +
                "\"uid\":\"8626d863-8c3f-405c-a2cb-bbbb3e3b359f\"," +
                "\"name\": { \"full\": \"test\"}," +
                "\"created\":\"2010-10-10T10:10:10Z\"," +
                "\"vCard\": { " +
                    "\"convertedProperties\": { " +
                        "\"created\": { " +
                            "\"name\": \"created\", " +
                            "\"parameters\" : { " +
                                "\"pid\" : \"1\" " +
                            "}" +
                        "}" +
                    "}" +
                "}" +
                "}";
        VCard vcard = jsContact2VCard.convert(jscard).get(0);
        assertEquals("testCreatedUnconvertedParams - 1", 1, vcard.getExtendedProperties().size());
        assertEquals("testCreatedUnconvertedParams - 2", "CREATED", vcard.getExtendedProperties().get(0).getPropertyName());
        assertEquals("testCreatedUnconvertedParams - 3", "20101010T101010Z", vcard.getExtendedProperties().get(0).getValue());
        assertEquals("testCreatedUnconvertedParams - 4","1", vcard.getExtendedProperties().get(0).getParameter(VCardParamEnum.PID.getValue()));
    }

    @Test
    public void testLanguageUnconvertedParams() throws IOException, CardException {

        String jscard="{" +
                "\"@type\":\"Card\"," +
                "\"uid\":\"8626d863-8c3f-405c-a2cb-bbbb3e3b359f\"," +
                "\"name\": { \"full\": \"test\"}," +
                "\"language\":\"it\"," +
                "\"vCard\": { " +
                    "\"convertedProperties\": { " +
                        "\"language\": { " +
                            "\"name\": \"language\", " +
                            "\"parameters\" : { " +
                                "\"x-param\" : \"test\" " +
                            "}" +
                        "}" +
                    "}" +
                "}" +
                "}";
        VCard vcard = jsContact2VCard.convert(jscard).get(0);
        assertEquals("testLanguageUnconvertedParams - 1", 1, vcard.getExtendedProperties().size());
        assertEquals("testLanguageUnconvertedParams - 2", "LANGUAGE", vcard.getExtendedProperties().get(0).getPropertyName());
        assertEquals("testLanguageUnconvertedParams - 3", "it", vcard.getExtendedProperties().get(0).getValue());
        assertEquals("testLanguageUnconvertedParams - 4","test", vcard.getExtendedProperties().get(0).getParameter("X-PARAM"));
    }

    
    @Test
    public void testBdayUnconvertedParams() throws IOException, CardException {

        String jsCard = "{ " +
                "\"@type\":\"Card\"," +
                "\"uid\":\"ff7854c7-26e2-4adf-89b5-5bc8ac5d75ff\", " +
                "\"name\": { \"full\": \"test\"}," +
                "\"anniversaries\":{ \"ANNIVERSARY-1\": " +
                    "{" +
                        "\"@type\":\"Anniversary\"," +
                        "\"kind\":\"birth\", " +
                        "\"date\":{" +
                        "\"@type\":\"Timestamp\"," +
                        "\"utc\":\"1953-10-15T23:10:00Z\"" +
                        "}" +
                    "}" +
                "}," +
                "\"vCard\": { " +
                    "\"convertedProperties\": { " +
                        "\"anniversaries/ANNIVERSARY-1\": { " +
                            "\"name\": \"bday\", " +
                            "\"parameters\" : { " +
                                "\"x-param\" : \"test\" " +
                            "}" +
                        "}" +
                    "}" +
                "}" +
                "}";
        VCard vcard = jsContact2VCard.convert(jsCard).get(0);
        assertEquals("testBdayUnconvertedParams - 1", 0, vcard.getBirthday().getDate().compareTo(VCardDateFormat.parse("1953-10-15T23:10:00Z")));
        assertEquals("testBdayUnconvertedParams - 2", "ANNIVERSARY-1", vcard.getBirthday().getParameter(VCardParamEnum.JSID.getValue()));
        assertEquals("testBdayUnconvertedParams - 3","test", vcard.getBirthday().getParameter("X-PARAM"));
    }

    @Test
    public void testBirthplaceUnconvertedParams() throws IOException, CardException {

        String jsCard = "{ " +
                "\"@type\":\"Card\"," +
                "\"uid\":\"ff7854c7-26e2-4adf-89b5-5bc8ac5d75ff\", " +
                "\"name\": { \"full\": \"test\"}," +
                "\"anniversaries\":{ \"ANNIVERSARY-1\": " +
                    "{" +
                        "\"@type\":\"Anniversary\"," +
                        "\"kind\":\"birth\", " +
                        "\"date\":{" +
                        "\"@type\":\"Timestamp\"," +
                        "\"utc\":\"1953-10-15T23:10:00Z\"" +
                    "}," +
                        "\"place\":{ " +
                        "\"@type\":\"Address\"," +
                            "\"full\":\"Mail Drop: TNE QB 123 Main Street Any Town, CA 91921-1234 USA\"" +
                        "}" +
                    "}" +
                "}," +
                "\"vCard\": { " +
                    "\"convertedProperties\": { " +
                        "\"anniversaries/ANNIVERSARY-1/place\": { " +
                            "\"name\": \"birthplace\", " +
                            "\"parameters\" : { " +
                                "\"x-param\" : \"test\" " +
                            "}" +
                        "}" +
                    "}" +
                "}" +
                "}";
        VCard vcard = jsContact2VCard.convert(jsCard).get(0);
        assertEquals("testBirthplaceUnconvertedParams - 1", 0, vcard.getBirthday().getDate().compareTo(VCardDateFormat.parse("1953-10-15T23:10:00Z")));
        assertEquals("testBirthplaceUnconvertedParams - 2", "Mail Drop: TNE QB 123 Main Street Any Town, CA 91921-1234 USA", vcard.getBirthplace().getText());
        assertEquals("testBirthplaceUnconvertedParams - 3", "ANNIVERSARY-1", vcard.getBirthday().getParameter(VCardParamEnum.JSID.getValue()));
        assertEquals("testBirthplaceUnconvertedParams - 4", "ANNIVERSARY-1", vcard.getBirthplace().getParameter(VCardParamEnum.JSID.getValue()));
        assertEquals("testBirthplaceUnconvertedParams - 5","test", vcard.getBirthplace().getParameter("X-PARAM"));
    }

    @Test
    public void testDeathdateUnconvertedParams() throws IOException, CardException {

        String jsCard = "{ " +
                "\"@type\":\"Card\"," +
                "\"uid\":\"ff7854c7-26e2-4adf-89b5-5bc8ac5d75ff\", " +
                "\"name\": { \"full\": \"test\"}," +
                "\"anniversaries\":{ \"ANNIVERSARY-1\": " +
                    "{" +
                        "\"@type\":\"Anniversary\"," +
                        "\"kind\":\"death\", " +
                        "\"date\":{" +
                            "\"@type\":\"Timestamp\"," +
                            "\"utc\":\"1953-10-15T23:10:00Z\"" +
                        "}" +
                    "}" +
                "}," +
                "\"vCard\": { " +
                    "\"convertedProperties\": { " +
                        "\"anniversaries/ANNIVERSARY-1\": { " +
                            "\"name\": \"deathdate\", " +
                            "\"parameters\" : { " +
                                "\"x-param\" : \"test\" " +
                            "}" +
                        "}" +
                    "}" +
                "}" +
                "}";
        VCard vcard = jsContact2VCard.convert(jsCard).get(0);
        assertEquals("testDeathdateUnconvertedParams - 1", 0, vcard.getDeathdate().getDate().compareTo(VCardDateFormat.parse("1953-10-15T23:10:00Z")));
        assertEquals("testDeathdateUnconvertedParams - 2", "ANNIVERSARY-1", vcard.getDeathdate().getParameter(VCardParamEnum.JSID.getValue()));
        assertEquals("testDeathdateUnconvertedParams - 3","test", vcard.getDeathdate().getParameter("X-PARAM"));
    }

    @Test
    public void testDeathplaceUnconvertedParams() throws IOException, CardException {

        String jsCard = "{ " +
                "\"@type\":\"Card\"," +
                "\"uid\":\"ff7854c7-26e2-4adf-89b5-5bc8ac5d75ff\", " +
                "\"name\": { \"full\": \"test\"}," +
                "\"anniversaries\":{ \"ANNIVERSARY-1\": " +
                    "{" +
                            "\"@type\":\"Anniversary\"," +
                            "\"kind\":\"death\", " +
                            "\"date\":{" +
                            "\"@type\":\"Timestamp\"," +
                            "\"utc\":\"1953-10-15T23:10:00Z\"" +
                        "}," +
                        "\"place\":{ " +
                            "\"full\":\"Mail Drop: TNE QB 123 Main Street Any Town, CA 91921-1234 USA\"" +
                        "}" +
                    "}" +
                "}," +
                "\"vCard\": { " +
                    "\"convertedProperties\": { " +
                        "\"anniversaries/ANNIVERSARY-1/place\": { " +
                            "\"name\": \"deathplace\", " +
                            "\"parameters\" : { " +
                                "\"x-param\" : \"test\" " +
                            "}" +
                        "}" +
                    "}" +
                "}" +
                "}";
        VCard vcard = jsContact2VCard.convert(jsCard).get(0);
        assertEquals("testDeathplaceUnconvertedParams - 1", 0, vcard.getDeathdate().getDate().compareTo(VCardDateFormat.parse("1953-10-15T23:10:00Z")));
        assertEquals("testDeathplaceUnconvertedParams - 2", "Mail Drop: TNE QB 123 Main Street Any Town, CA 91921-1234 USA", vcard.getDeathplace().getText());
        assertEquals("testDeathplaceUnconvertedParams - 3", "ANNIVERSARY-1", vcard.getDeathdate().getParameter(VCardParamEnum.JSID.getValue()));
    }

    @Test
    public void testAnniversaryUnconvertedParams() throws IOException, CardException {

        String jsCard = "{ " +
                "\"@type\":\"Card\"," +
                "\"uid\":\"ff7854c7-26e2-4adf-89b5-5bc8ac5d75ff\", " +
                "\"name\": { \"full\": \"test\"}," +
                "\"anniversaries\":{ \"ANNIVERSARY-1\": " +
                    "{" +
                        "\"@type\":\"Anniversary\"," +
                        "\"kind\":\"birth\", " +
                        "\"date\":{" +
                            "\"@type\":\"Timestamp\"," +
                            "\"utc\":\"1953-10-15T23:10:00Z\"" +
                        "}," +
                        "\"place\":{ " +
                            "\"full\":\"Los Angeles CA USA\"" +
                        "}" +
                    "}," +
                    "\"ANNIVERSARY-2\": {" +
                        "\"@type\":\"Anniversary\"," +
                        "\"kind\":\"death\", " +
                        "\"date\":{" +
                            "\"@type\":\"Timestamp\"," +
                            "\"utc\":\"1993-10-15T23:10:00Z\"" +
                        "}," +
                        "\"place\":{ " +
                            "\"full\":\"Mail Drop: TNE QB 123 Main Street Any Town, CA 91921-1234 USA\"" +
                        "}" +
                    "}," +
                    "\"ANNIVERSARY-3\": {" +
                        "\"@type\":\"Anniversary\"," +
                        "\"kind\":\"wedding\", " +
                        "\"date\":{" +
                            "\"@type\":\"Timestamp\"," +
                            "\"utc\":\"1986-02-01T19:00:00Z\"" +
                        "}" +
                    "}" +
                "}," +
                "\"vCard\": { " +
                    "\"convertedProperties\": { " +
                        "\"anniversaries/ANNIVERSARY-3\": { " +
                            "\"name\": \"anniversary\", " +
                            "\"parameters\" : { " +
                                "\"x-param\" : \"test\" " +
                            "}" +
                        "}" +
                    "}" +
                "}" +
                "}";
        VCard vcard = jsContact2VCard.convert(jsCard).get(0);
        assertEquals("testAnniversaryUnconvertedParams - 1", 0, vcard.getBirthday().getDate().compareTo(VCardDateFormat.parse("1953-10-15T23:10:00Z")));
        assertEquals("testAnniversaryUnconvertedParams - 2", "Los Angeles CA USA", vcard.getBirthplace().getText());
        assertEquals("testAnniversaryUnconvertedParams - 3", 0, vcard.getDeathdate().getDate().compareTo(VCardDateFormat.parse("1993-10-15T23:10:00Z")));
        assertEquals("testAnniversaryUnconvertedParams - 4", "Mail Drop: TNE QB 123 Main Street Any Town, CA 91921-1234 USA", vcard.getDeathplace().getText());
        assertEquals("testAnniversaryUnconvertedParams - 5", 0, vcard.getAnniversary().getDate().compareTo(VCardDateFormat.parse("1986-02-01T19:00:00Z")));
        assertEquals("testAnniversaryUnconvertedParams - 6", "ANNIVERSARY-1", vcard.getBirthday().getParameter(VCardParamEnum.JSID.getValue()));
        assertEquals("testAnniversaryUnconvertedParams - 7", "ANNIVERSARY-2", vcard.getDeathdate().getParameter(VCardParamEnum.JSID.getValue()));
        assertEquals("testAnniversaryUnconvertedParams - 8", "ANNIVERSARY-3", vcard.getAnniversary().getParameter(VCardParamEnum.JSID.getValue()));
        assertEquals("testAnniversaryUnconvertedParams - 9","test", vcard.getAnniversary().getParameter("X-PARAM"));
    }


    @Test
    public void testGramgenderPronounsUnconvertedParams() throws IOException, CardException {

        String jscard="{" +
                "\"@type\":\"Card\"," +
                "\"uid\":\"urn:uuid:03a0e51f-d1aa-4385-8a53-e29025acd8af\"," +
                "\"language\":\"en\"," +
                "\"speakToAs\": {" +
                    "\"@type\":\"SpeakToAs\"," +
                    "\"grammaticalGender\":\"inanimate\"," +
                    "\"pronouns\": { " +
                        "\"PRONOUNS-1\": { " +
                            "\"@type\":\"Pronouns\"," +
                            "\"pronouns\":\"he/him\"" +
                        "}" +
                    "}" +
                "}," +
                "\"localizations\":{" +
                    "\"it\":{" +
                        "\"speakToAs/grammaticalGender\":\"masculine\"," +
                        "\"speakToAs/pronouns/PRONOUNS-1\":{ " +
                        "\"@type\":\"Pronouns\"," +
                            "\"pronouns\":\"egli/lui\"" +
                        "}" +
                    "}" +
                "}," +
                "\"vCard\" : { " +
                    "\"@type\" : \"VCard\", " +
                    "\"convertedProperties\" : { " +
                        "\"localizations/it/speakToAs~1grammaticalGender\" : {" +
                            "\"name\" : \"gramgender\"," +
                                    "\"parameters\" : {" +
                                "\"x-param2it\" : \"test2IT\"" +
                            "}"+
                        "},"+
                        "\"speakToAs/pronouns/PRONOUNS-1\" : { " +
                            "\"name\" : \"pronouns\"," +
                                    "\"parameters\" : {" +
                                "\"x-param1\" : \"test1\"" +
                            "}"+
                        "},"+
                        "\"speakToAs/grammaticalGender\" : {" +
                            "\"name\" : \"gramgender\"," +
                                    "\"parameters\" : {" +
                                "\"x-param2\" : \"test2\"" +
                            "}"+
                        "},"+
                        "\"localizations/it/speakToAs~1pronouns~1PRONOUNS-1\" : {" +
                            "\"name\" : \"pronouns\"," +
                                    "\"parameters\" : {" +
                                "\"x-param1it\" : \"test1IT\"" +
                            "}"+
                        "}"+
                    "}"+
                "}"+
                "}";
        VCard vcard = jsContact2VCard.convert(jscard).get(0);
        assertEquals("testGramgenderPronounsUnconvertedParams - 1","INANIMATE", vcard.getExtendedProperties("GRAMGENDER").get(0).getValue());
        assertEquals("testGramgenderPronounsUnconvertedParams - 2", "he/him", vcard.getExtendedProperties("PRONOUNS").get(0).getValue());
        assertEquals("testGramgenderPronounsUnconvertedParams - 3", "egli/lui", vcard.getExtendedProperties("PRONOUNS").get(1).getValue());
        assertEquals("testGramgenderPronounsUnconvertedParams - 4", "it", vcard.getExtendedProperties("PRONOUNS").get(1).getParameter(VCardParamEnum.LANGUAGE.getValue()));
        assertEquals("testGramgenderPronounsUnconvertedParams - 5","MASCULINE", vcard.getExtendedProperties("GRAMGENDER").get(1).getValue());
        assertEquals("testGramgenderPronounsUnconvertedParams - 6","it", vcard.getExtendedProperties("GRAMGENDER").get(1).getParameter(VCardParamEnum.LANGUAGE.getValue()));
        assertEquals("testGramgenderPronounsUnconvertedParams - 7","test2", vcard.getExtendedProperties("GRAMGENDER").get(0).getParameter("X-PARAM2"));
        assertEquals("testGramgenderPronounsUnconvertedParams - 8","test2IT", vcard.getExtendedProperties("GRAMGENDER").get(1).getParameter("X-PARAM2IT"));
        assertEquals("testGramgenderPronounsUnconvertedParams - 9","test1", vcard.getExtendedProperties("PRONOUNS").get(0).getParameter("X-PARAM1"));
        assertEquals("testGramgenderPronounsUnconvertedParams - 10","test1IT", vcard.getExtendedProperties("PRONOUNS").get(1).getParameter("X-PARAM1IT"));
    }
    
    @Test
    public void testTitleUnconvertedParams() throws IOException, CardException {

        String jscard="{" +
                "\"@type\":\"Card\"," +
                "\"uid\":\"8626d863-8c3f-405c-a2cb-bbbb3e3b359h\"," +
                "\"name\": { \"full\": \"test\"}," +
                "\"titles\": {" +
                    "\"TITLE-1\" : {" +
                        "\"@type\":\"Title\"," +
                        "\"name\": \"Research Scientist\"" +
                    "}" +
                "}," +
                "\"localizations\" : {" +
                    "\"it\" : { " +
                        "\"titles/TITLE-1\": { \"@type\":\"Title\",\"name\": \"Ricercatore\" } " +
                    "}" +
                "}," +
                "\"vCard\": { " +
                    "\"convertedProperties\": { " +
                        "\"titles/TITLE-1\": { " +
                            "\"name\": \"title\", " +
                            "\"parameters\" : { " +
                                "\"pid\" : \"1\" " +
                            "}" +
                        "}," +
                        "\"localizations/it/titles~1TITLE-1\": { " +
                            "\"name\": \"title\", " +
                            "\"parameters\" : { " +
                                "\"pid\" : \"2\" " +
                            "}" +
                        "}" +
                    "}" +
                "}" +
                "}";
        VCard vcard = jsContact2VCard.convert(jscard).get(0);
        assertEquals("testTitleUnconvertedParams - 1", 2, vcard.getTitles().size());
        assertEquals("testTitleUnconvertedParams - 2", "Research Scientist", vcard.getTitles().get(0).getValue());
        assertNull("testTitleUnconvertedParams - 3", vcard.getTitles().get(0).getLanguage());
        assertEquals("testTitleUnconvertedParams - 4", "1", vcard.getTitles().get(0).getAltId());
        assertEquals("testTitleUnconvertedParams - 5", "Ricercatore", vcard.getTitles().get(1).getValue());
        assertEquals("testTitleUnconvertedParams - 6", "it", vcard.getTitles().get(1).getLanguage());
        assertEquals("testTitleUnconvertedParams - 7", "1", vcard.getTitles().get(1).getAltId());
        assertEquals("testTitleUnconvertedParams - 8", "TITLE-1", vcard.getTitles().get(0).getParameter(VCardParamEnum.JSID.getValue()));
        assertEquals("testTitleUnconvertedParams - 9","1", vcard.getTitles().get(0).getParameter(VCardParamEnum.PID.getValue()));
        assertEquals("testTitleUnconvertedParams - 10","2", vcard.getTitles().get(1).getParameter(VCardParamEnum.PID.getValue()));
    }

    @Test
    public void testRoleUnconvertedParams() throws IOException, CardException {

        String jscard="{" +
                "\"@type\":\"Card\"," +
                "\"uid\":\"8626d863-8c3f-405c-a2cb-bbbb3e3b359g\"," +
                "\"name\": { \"full\": \"test\"}," +
                "\"titles\": {" +
                    "\"TITLE-1\" : {" +
                        "\"@type\":\"Title\"," +
                        "\"kind\":\"role\"," +
                        "\"name\": \"IETF Area Director\"" +
                    "}" +
                "}," +
                "\"localizations\" : {" +
                    "\"it\" : { " +
                        "\"titles/TITLE-1\": { \"@type\":\"Title\",\"kind\":\"role\",\"name\": \"Direttore Area IETF\" } " +
                    "}" +
                "}," +
                "\"vCard\": { " +
                    "\"convertedProperties\": { " +
                        "\"titles/TITLE-1\": { " +
                            "\"name\": \"title\", " +
                            "\"parameters\" : { " +
                                "\"pid\" : \"1\" " +
                            "}" +
                        "}," +
                        "\"localizations/it/titles~1TITLE-1\": { " +
                            "\"name\": \"title\", " +
                            "\"parameters\" : { " +
                                "\"pid\" : \"2\" " +
                            "}" +
                        "}" +
                    "}" +
                "}" +
                "}";
        VCard vcard = jsContact2VCard.convert(jscard).get(0);
        assertEquals("testRoleUnconvertedParams - 1", 2, vcard.getRoles().size());
        assertEquals("testRoleUnconvertedParams - 2", "IETF Area Director", vcard.getRoles().get(0).getValue());
        assertNull("testRoleUnconvertedParams - 3", vcard.getRoles().get(0).getLanguage());
        assertEquals("testRoleUnconvertedParams - 4", "1", vcard.getRoles().get(0).getAltId());
        assertEquals("testRoleUnconvertedParams - 5", "Direttore Area IETF", vcard.getRoles().get(1).getValue());
        assertEquals("testRoleUnconvertedParams - 6", "it", vcard.getRoles().get(1).getLanguage());
        assertEquals("testRoleUnconvertedParams - 7", "1", vcard.getRoles().get(1).getAltId());
        assertEquals("testRoleUnconvertedParams - 8", "TITLE-1", vcard.getRoles().get(0).getParameter(VCardParamEnum.JSID.getValue()));
        assertEquals("testRoleUnconvertedParams - 9","1", vcard.getRoles().get(0).getParameter(VCardParamEnum.PID.getValue()));
        assertEquals("testRoleUnconvertedParams - 10","2", vcard.getRoles().get(1).getParameter(VCardParamEnum.PID.getValue()));
    }
    
    @Test
    public void testNoteUnconvertedParams() throws IOException, CardException {

        String jscard="{" +
                "\"@type\":\"Card\"," +
                "\"uid\":\"8626d863-8c3f-405c-a2cb-bbbb3e3b359f\"," +
                "\"name\": { \"full\": \"test\"}," +
                "\"notes\": {" +
                    "\"NOTE-1\": { \"@type\": \"Note\", \"created\":\"2010-10-10T10:10:10Z\", \"note\": \"This fax number is operational 0800 to 1715 EST, Mon-Fri\"}" +
                "}," +
                "\"localizations\": { \"it\": { \"notes/NOTE-1\": { \"@type\": \"Note\", \"note\": \"Questo numero di fax è operativo dalle 8.00 alle 17.15, Lun-Ven\" } } }," +
                "\"vCard\": { " +
                    "\"convertedProperties\": { " +
                        "\"notes/NOTE-1\": { " +
                            "\"name\": \"note\", " +
                            "\"parameters\" : { " +
                                "\"pid\" : \"1\" " +
                            "}" +
                        "}," +
                        "\"localizations/it/notes~1NOTE-1\": { " +
                            "\"name\": \"note\", " +
                            "\"parameters\" : { " +
                                "\"pid\" : \"2\" " +
                            "}" +
                        "}" +
                    "}" +
                "}" +
                "}";
        VCard vcard = jsContact2VCard.convert(jscard).get(0);
        assertEquals("testNoteUnconvertedParams - 1", 2, vcard.getNotes().size());
        assertEquals("testNoteUnconvertedParams - 2", "This fax number is operational 0800 to 1715 EST, Mon-Fri", vcard.getNotes().get(0).getValue());
        assertNull("testNoteUnconvertedParams - 3", vcard.getNotes().get(0).getLanguage());
        assertEquals("testNoteUnconvertedParams - 4", "1", vcard.getNotes().get(0).getAltId());
        assertEquals("testNoteUnconvertedParams - 5", "20101010T101010Z", vcard.getNotes().get(0).getParameter(VCardParamEnum.CREATED.getValue()));
        assertEquals("testNoteUnconvertedParams - 6", "Questo numero di fax è operativo dalle 8.00 alle 17.15, Lun-Ven", vcard.getNotes().get(1).getValue());
        assertEquals("testNoteUnconvertedParams - 7", "it", vcard.getNotes().get(1).getLanguage());
        assertEquals("testNoteUnconvertedParams - 8", "1", vcard.getNotes().get(1).getAltId());
        assertEquals("testNoteUnconvertedParams - 9","1", vcard.getNotes().get(0).getParameter(VCardParamEnum.PID.getValue()));
        assertEquals("testNoteUnconvertedParams - 10","2", vcard.getNotes().get(1).getParameter(VCardParamEnum.PID.getValue()));
    }

    @Test
    public void testNicknameUnconvertedParams() throws IOException, CardException {

        String jscard="{" +
                "\"@type\":\"Card\"," +
                "\"uid\":\"8626d863-8c3f-405c-a2cb-bbbb3e3b359f\"," +
                "\"name\": { }," +
                "\"language\": \"en\"," +
                "\"name\":{ " +
                    "\"full\": \"Mr. John Q. Public, Esq.\"," +
                    "\"components\":[ " +
                        "{ \"value\":\"Mr.\", \"kind\": \"title\" }," +
                        "{ \"value\":\"John\", \"kind\": \"given\" }," +
                        "{ \"value\":\"Public\", \"kind\": \"surname\" }," +
                        "{ \"value\":\"Quinlan\", \"kind\": \"given2\" }," +
                        "{ \"value\":\"Esq.\", \"kind\": \"credential\" }" +
                    "] " +
                "}, " +
                "\"nicknames\": { " +
                    "\"NICK-1\" : {  \"@type\":\"Nickname\", \"name\": \"Johnny\" }, " +
                    "\"NICK-2\" : {  \"@type\":\"Nickname\", \"name\": \"Joe\" } " +
                "}," +
                "\"localizations\": { " +
                    "\"it\" : { " +
                        "\"nicknames/NICK-1\" : {  \"@type\":\"Nickname\", \"name\": \"Giovannino\" }, " +
                        "\"nicknames/NICK-2\" : {  \"@type\":\"Nickname\", \"name\": \"Giò\" } " +
                    "}" +
                "}," +
                "\"vCard\": { " +
                    "\"convertedProperties\": { " +
                        "\"nicknames/NICK-1\": { " +
                            "\"name\": \"nickname\", " +
                            "\"parameters\" : { " +
                                "\"pid\" : \"1\" " +
                        "}" +
                        "}," +
                        "\"localizations/it/nicknames~1NICK-1\": { " +
                            "\"name\": \"nickname\", " +
                            "\"parameters\" : { " +
                                "\"pid\" : \"2\" " +
                            "}" +
                        "}" +
                    "}" +
                "}" +
                "}";
        VCard vcard = jsContact2VCard.convert(jscard).get(0);
        assertEquals("testNicknameUnconvertedParams - 1", "Mr. John Q. Public, Esq.", vcard.getFormattedName().getValue());
        assertNotNull("testNicknameUnconvertedParams - 2", vcard.getProperty(ExtendedStructuredName.class));
        assertEquals("testNicknameUnconvertedParams - 3", "Public", vcard.getProperty(ExtendedStructuredName.class).getFamily());
        assertEquals("testNicknameUnconvertedParams - 4", "John", vcard.getProperty(ExtendedStructuredName.class).getGiven());
        assertEquals("testNicknameUnconvertedParams - 5", 1, vcard.getProperty(ExtendedStructuredName.class).getAdditionalNames().size());
        assertEquals("testNicknameUnconvertedParams - 6", "Quinlan", vcard.getProperty(ExtendedStructuredName.class).getAdditionalNames().get(0));
        assertEquals("testNicknameUnconvertedParams - 7", 1, vcard.getProperty(ExtendedStructuredName.class).getPrefixes().size());
        assertEquals("testNicknameUnconvertedParams - 8", "Mr.", vcard.getProperty(ExtendedStructuredName.class).getPrefixes().get(0));
        assertEquals("testNicknameUnconvertedParams - 9", 1, vcard.getProperty(ExtendedStructuredName.class).getSuffixes().size());
        assertEquals("testNicknameUnconvertedParams - 10", "Esq.", vcard.getProperty(ExtendedStructuredName.class).getSuffixes().get(0));
        assertEquals("testNicknameUnconvertedParams - 11", 4, vcard.getNicknames().size());
        assertEquals("testNicknameUnconvertedParams - 12", "Johnny", vcard.getNicknames().get(0).getValues().get(0));
        assertEquals("testNicknameUnconvertedParams - 13", "1", vcard.getNicknames().get(0).getAltId());
        assertEquals("testNicknameUnconvertedParams - 14", "Giovannino", vcard.getNicknames().get(1).getValues().get(0));
        assertEquals("testNicknameUnconvertedParams - 15", "it", vcard.getNicknames().get(1).getLanguage());
        assertEquals("testNicknameUnconvertedParams - 16", "1", vcard.getNicknames().get(1).getAltId());
        assertEquals("testNicknameUnconvertedParams - 17", "Joe", vcard.getNicknames().get(2).getValues().get(0));
        assertEquals("testNicknameUnconvertedParams - 18", "2", vcard.getNicknames().get(2).getAltId());
        assertEquals("testNicknameUnconvertedParams - 19", "Giò", vcard.getNicknames().get(3).getValues().get(0));
        assertEquals("testNicknameUnconvertedParams - 20", "it", vcard.getNicknames().get(3).getLanguage());
        assertEquals("testNicknameUnconvertedParams - 21", "2", vcard.getNicknames().get(3).getAltId());
        assertEquals("testNicknameUnconvertedParams - 22","1", vcard.getNicknames().get(0).getParameter(VCardParamEnum.PID.getValue()));
        assertEquals("testNicknameUnconvertedParams - 23","2", vcard.getNicknames().get(1).getParameter(VCardParamEnum.PID.getValue()));
    }

    @Test
    public void testAdrUnconvertedParams() throws IOException, CardException {

        String jscard = "{" +
                "\"@type\":\"Card\"," +
                "\"uid\":\"7e0636f5-e48f-4a32-ab96-b57e9c07c7aa\"," +
                "\"name\": { \"full\": \"test\"}," +
                "\"language\":\"en\"," +
                "\"addresses\":{" +
                    "\"ADR-1\": {" +
                        "\"@type\":\"Address\"," +
                        "\"components\":[ " +
                            "{\"kind\":\"name\",\"value\":\"54321 Oak St\"}," +
                            "{\"kind\":\"locality\",\"value\":\"Reston\"}," +
                            "{\"kind\":\"region\",\"value\":\"VA\"}," +
                            "{\"kind\":\"country\",\"value\":\"USA\"}," +
                            "{\"kind\":\"postcode\",\"value\":\"20190\"}" +
                        "]," +
                        "\"countryCode\":\"US\"" +
                    "}" +
                "}," +
                "\"localizations\":{" +
                    "\"it\":{" +
                        "\"addresses/ADR-1\":{" +
                            "\"@type\":\"Address\"," +
                            "\"components\":[ " +
                                "{\"kind\":\"name\",\"value\":\"Via Moruzzi,1\"}," +
                                "{\"kind\":\"locality\",\"value\":\"Pisa\"}," +
                                "{\"kind\":\"postcode\",\"value\":\"56124\"}," +
                                "{\"kind\":\"country\",\"value\":\"Italia\"}" +
                            "]," +
                            "\"countryCode\":\"IT\"" +
                        "}" +
                    "}" +
                "}," +
                "\"vCard\": { " +
                    "\"convertedProperties\": { " +
                        "\"addresses/ADR-1\": { " +
                            "\"name\": \"adr\", " +
                            "\"parameters\" : { " +
                                "\"pid\" : \"1\" " +
                            "}" +
                        "}," +
                        "\"localizations/it/addresses~1ADR-1\": { " +
                            "\"name\": \"adr\", " +
                            "\"parameters\" : { " +
                                "\"pid\" : \"2\" " +
                            "}" +
                        "}" +
                    "}" +
                "}" +
                "}";
        VCard vcard = jsContact2VCard.convert(jscard).get(0);
        assertEquals("testAdrUnconvertedParams - 1", 2, vcard.getProperties(ExtendedAddress.class).size());
        assertEquals("testAdrUnconvertedParams - 2", "US", vcard.getProperties(ExtendedAddress.class).get(0).getParameter("CC"));
        assertEquals("testAdrUnconvertedParams - 3", "USA", vcard.getProperties(ExtendedAddress.class).get(0).getCountry());
        assertEquals("testAdrUnconvertedParams - 4", "20190", vcard.getProperties(ExtendedAddress.class).get(0).getPostalCode());
        assertEquals("testAdrUnconvertedParams - 5", "Reston", vcard.getProperties(ExtendedAddress.class).get(0).getLocality());
        assertEquals("testAdrUnconvertedParams - 6", "VA", vcard.getProperties(ExtendedAddress.class).get(0).getRegion());
        assertEquals("testAdrUnconvertedParams - 7", "54321 Oak St", vcard.getProperties(ExtendedAddress.class).get(0).getStreetAddress());
        assertEquals("testAdrUnconvertedParams - 8", "54321 Oak St Reston VA USA 20190", vcard.getProperties(ExtendedAddress.class).get(0).getLabel());
        assertEquals("testAdrUnconvertedParams - 9", "en", vcard.getProperties(ExtendedAddress.class).get(0).getLanguage());
        assertEquals("testAdrUnconvertedParams - 10", "IT", vcard.getProperties(ExtendedAddress.class).get(1).getParameter("CC"));
        assertEquals("testAdrUnconvertedParams - 11", "Italia", vcard.getProperties(ExtendedAddress.class).get(1).getCountry());
        assertEquals("testAdrUnconvertedParams - 12", "56124", vcard.getProperties(ExtendedAddress.class).get(1).getPostalCode());
        assertEquals("testAdrUnconvertedParams - 13", "Pisa", vcard.getProperties(ExtendedAddress.class).get(1).getLocality());
        assertEquals("testAdrUnconvertedParams - 14", "Via Moruzzi,1", vcard.getProperties(ExtendedAddress.class).get(1).getStreetAddress());
        assertEquals("testAdrUnconvertedParams - 15", "Via Moruzzi,1 Pisa 56124 Italia", vcard.getProperties(ExtendedAddress.class).get(1).getLabel());
        assertEquals("testAdrUnconvertedParams - 16", "it", vcard.getProperties(ExtendedAddress.class).get(1).getLanguage());
        assertEquals("testAdrUnconvertedParams - 17", "ADR-1", vcard.getProperties(ExtendedAddress.class).get(0).getParameter(VCardParamEnum.JSID.getValue()));
        assertEquals("testAdrUnconvertedParams - 18","1", vcard.getProperties(ExtendedAddress.class).get(0).getParameter(VCardParamEnum.PID.getValue()));
        assertEquals("testAdrUnconvertedParams - 19","2", vcard.getProperties(ExtendedAddress.class).get(1).getParameter(VCardParamEnum.PID.getValue()));
    }

    @Test
    public void testOrgUnconvertedParams() throws IOException, CardException {

        String jscard="{" +
                "\"@type\":\"Card\"," +
                "\"uid\":\"8626d863-8c3f-405c-a2cb-bbbb3e3b359f\"," +
                "\"name\": { \"full\": \"test\"}," +
                "\"organizations\": {" +
                    "\"ORG-1\": {" +
                        "\"@type\":\"Organization\"," +
                        "\"units\": [ " +
                            "{\"@type\":\"OrgUnit\", \"name\":\"North American Division\"}," +
                            "{\"@type\":\"OrgUnit\", \"name\":\"Marketing\" }" +
                        "]" +
                    "}" +
                "}," +
                "\"localizations\": { " +
                    "\"it\" : { " +
                        "\"organizations/ORG-1\" : { " +
                            "\"@type\":\"Organization\"," +
                            "\"units\": [ " +
                                "{\"@type\":\"OrgUnit\", \"name\":\"Divisione Nord America\"}," +
                                "{\"@type\":\"OrgUnit\", \"name\":\"Marketing\" }" +
                            "]" +
                        "}" +
                    "}" +
                "}," +
                "\"vCard\": { " +
                    "\"convertedProperties\": { " +
                        "\"organizations/ORG-1\": { " +
                            "\"name\": \"org\", " +
                            "\"parameters\" : { " +
                                "\"pid\" : \"1\" " +
                            "}" +
                        "}," +
                        "\"localizations/it/organizations~1ORG-1\": { " +
                            "\"name\": \"org\", " +
                            "\"parameters\" : { " +
                            "\"pid\" : \"2\" " +
                                "}" +
                        "}" +
                    "}" +
                "}" +
                "}";
        VCard vcard = jsContact2VCard.convert(jscard).get(0);
        assertEquals("testOrgUnconvertedParams - 1", 2, vcard.getOrganizations().size());
        assertEquals("testOrgUnconvertedParams - 2", 3, vcard.getOrganizations().get(0).getValues().size());
        assertTrue("testOrgUnconvertedParams - 3",  vcard.getOrganizations().get(0).getValues().get(0).isEmpty());
        assertEquals("testOrgUnconvertedParams - 4", "North American Division", vcard.getOrganizations().get(0).getValues().get(1));
        assertEquals("testOrgUnconvertedParams - 5", "Marketing", vcard.getOrganizations().get(0).getValues().get(2));
        assertNull("testOrgUnconvertedParams - 6", vcard.getOrganizations().get(0).getLanguage());
        assertEquals("testOrgUnconvertedParams - 7", "1", vcard.getOrganizations().get(0).getAltId());
        assertEquals("testOrgUnconvertedParams - 8", 3, vcard.getOrganizations().get(1).getValues().size());
        assertTrue("testOrgUnconvertedParams - 9", vcard.getOrganizations().get(1).getValues().get(0).isEmpty());
        assertEquals("testOrgUnconvertedParams - 10", "Divisione Nord America", vcard.getOrganizations().get(1).getValues().get(1));
        assertEquals("testOrgUnconvertedParams - 11", "Marketing", vcard.getOrganizations().get(1).getValues().get(2));
        assertEquals("testOrgUnconvertedParams - 12", "it", vcard.getOrganizations().get(1).getLanguage());
        assertEquals("testOrgUnconvertedParams - 13", "1", vcard.getOrganizations().get(1).getAltId());
        assertEquals("testOrgUnconvertedParams - 14", "ORG-1", vcard.getOrganizations().get(0).getParameter(VCardParamEnum.JSID.getValue()));
    }


}

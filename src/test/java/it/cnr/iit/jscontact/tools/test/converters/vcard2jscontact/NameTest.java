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
package it.cnr.iit.jscontact.tools.test.converters.vcard2jscontact;

import it.cnr.iit.jscontact.tools.dto.Name;
import it.cnr.iit.jscontact.tools.dto.NameComponentKind;
import it.cnr.iit.jscontact.tools.dto.PhoneticSystem;
import it.cnr.iit.jscontact.tools.dto.utils.JsonNodeUtils;
import it.cnr.iit.jscontact.tools.exceptions.CardException;
import it.cnr.iit.jscontact.tools.dto.Card;
import org.junit.Test;

import static org.junit.Assert.*;

public class NameTest extends VCard2JSContactTest {

    @Test
    public void testName1() throws CardException {

        String vcard = "BEGIN:VCARD\n" +
                "VERSION:4.0\n" +
                "FN:John Q. Public, Esq.\n" +
                "N;SORT-AS=\"Public,John\":Public;John;Quinlan;Mr.;Esq.\n" +
                "END:VCARD";

        Card jsCard = vCard2JSContact.convert(vcard).get(0);
        assertEquals("testName1 - 1", "John Q. Public, Esq.", jsCard.getName().getFull());
        assertEquals("testName1 - 2", 5, jsCard.getName().getComponents().length);
        assertTrue("testName1 - 3", jsCard.getName().getComponents()[3].isTitle());
        assertEquals("testName1 - 4", "Mr.", jsCard.getName().getComponents()[3].getValue());
        assertTrue("testName1 - 5", jsCard.getName().getComponents()[1].isGiven());
        assertEquals("testName1 - 6", "John", jsCard.getName().getGiven());
        assertTrue("testName1 - 7", jsCard.getName().getComponents()[0].isSurname());
        assertEquals("testName1 - 8", "Public", jsCard.getName().getSurname());
        assertTrue("testName1 - 9", jsCard.getName().getComponents()[2].isGiven2());
        assertEquals("testName1 - 10", "Quinlan", jsCard.getName().getGiven2());
        assertTrue("testName1 - 11", jsCard.getName().getComponents()[4].isCredential());
        assertEquals("testName1 - 12", "Esq.", jsCard.getName().getComponents()[4].getValue());
        assertEquals("testName1 - 13", "Public", jsCard.getName().getSortAs().get(NameComponentKind.surname()));
        assertEquals("testName1 - 14", "John", jsCard.getName().getSortAs().get(NameComponentKind.given()));

    }


    @Test
    public void testName2() throws CardException {

        String vcard = "BEGIN:VCARD\n" +
                "VERSION:4.0\n" +
                "FN:John Q. Public, Esq.\n" +
                "N:Public;John;Quinlan;Mr.;Esq.\n" +
                "NICKNAME:Johnny\n" +
                "END:VCARD";

        Card jsCard = vCard2JSContact.convert(vcard).get(0);
        assertEquals("testName2 - 1", "John Q. Public, Esq.", jsCard.getName().getFull());
        assertEquals("testName2 - 2", 5, jsCard.getName().getComponents().length);
        assertTrue("testName2 - 3",jsCard.getName().getComponents()[3].isTitle());
        assertEquals("testName2 - 4", "Mr.", jsCard.getName().getComponents()[3].getValue());
        assertTrue("testName2 - 5",jsCard.getName().getComponents()[1].isGiven());
        assertEquals("testName2 - 6", "John", jsCard.getName().getGiven());
        assertTrue("testName2 - 7",jsCard.getName().getComponents()[0].isSurname());
        assertEquals("testName2 - 8", "Public", jsCard.getName().getSurname());
        assertTrue("testName2 - 9",jsCard.getName().getComponents()[2].isGiven2());
        assertEquals("testName2 - 10", "Quinlan", jsCard.getName().getGiven2());
        assertTrue("testName2 - 11",jsCard.getName().getComponents()[4].isCredential());
        assertEquals("testName2 - 12", "Esq.", jsCard.getName().getComponents()[4].getValue());
        assertEquals("testName2 - 12", 1, jsCard.getNicknames().size());
        assertEquals("testName2 - 13", "Johnny", jsCard.getNicknames().get("NICK-1").getName());

    }

    @Test
    public void testName3() throws CardException {

        String vcard = "BEGIN:VCARD\n" +
                "VERSION:4.0\n" +
                "FN:John Q. Public, Esq.\n" +
                "N:Public;John;Quinlan;Mr.;Esq.\n" +
                "NICKNAME:Johnny\n" +
                "NICKNAME;PREF=1:Kid\n" +
                "END:VCARD";

        Card jsCard = vCard2JSContact.convert(vcard).get(0);
        assertEquals("testName3 - 1", "John Q. Public, Esq.", jsCard.getName().getFull());
        assertEquals("testName3 - 2", 5, jsCard.getName().getComponents().length);
        assertTrue("testName3 - 3",jsCard.getName().getComponents()[3].isTitle());
        assertEquals("testName3 - 4", "Mr.", jsCard.getName().getComponents()[3].getValue());
        assertTrue("testName3 - 5",jsCard.getName().getComponents()[1].isGiven());
        assertEquals("testName3 - 6", "John", jsCard.getName().getGiven());
        assertTrue("testName3 - 7",jsCard.getName().getComponents()[0].isSurname());
        assertEquals("testName3 - 8", "Public", jsCard.getName().getSurname());
        assertTrue("testName3 - 9",jsCard.getName().getComponents()[2].isGiven2());
        assertEquals("testName3 - 10", "Quinlan", jsCard.getName().getComponents()[2].getValue());
        assertTrue("testName3 - 11",jsCard.getName().getComponents()[4].isCredential());
        assertEquals("testName3 - 12", "Esq.", jsCard.getName().getComponents()[4].getValue());
        assertEquals("testName3 - 12", 2, jsCard.getNicknames().size());
        assertEquals("testName3 - 13", "Johnny", jsCard.getNicknames().get("NICK-1").getName());
        assertEquals("testName3 - 14", "Kid", jsCard.getNicknames().get("NICK-2").getName());
        assertEquals("testName3 - 15", 1, jsCard.getNicknames().get("NICK-2").getPref().intValue());

    }

    @Test
    public void testName4() throws CardException {

        String vcard = "BEGIN:VCARD\n" +
                "VERSION:4.0\n" +
                "FN:John Q. Public, Esq.\n" +
                "N:Public;John;Quinlan;Mr.;Esq.\n" +
                "NICKNAME;ALTID=1:Johnny\n" +
                "NICKNAME;PREF=1;ALTID=2:Kid\n" +
                "NICKNAME;LANGUAGE=it;ALTID=1:Giovannino\n" +
                "NICKNAME;PREF=1;LANGUAGE=it;ALTID=2:Ragazzo\n" +
                "END:VCARD";

        Card jsCard = vCard2JSContact.convert(vcard).get(0);
        assertEquals("testName4 - 1", "John Q. Public, Esq.", jsCard.getName().getFull());
        assertEquals("testName4 - 2", 5, jsCard.getName().getComponents().length);
        assertTrue("testName4 - 3",jsCard.getName().getComponents()[3].isTitle());
        assertEquals("testName4 - 4", "Mr.", jsCard.getName().getComponents()[3].getValue());
        assertTrue("testName4 - 5",jsCard.getName().getComponents()[1].isGiven());
        assertEquals("testName4 - 6", "John", jsCard.getName().getGiven());
        assertTrue("testName4 - 7",jsCard.getName().getComponents()[0].isSurname());
        assertEquals("testName4 - 8", "Public", jsCard.getName().getSurname());
        assertTrue("testName4 - 9",jsCard.getName().getComponents()[2].isGiven2());
        assertEquals("testName4 - 10", "Quinlan", jsCard.getName().getGiven2());
        assertTrue("testName4 - 11",jsCard.getName().getComponents()[4].isCredential());
        assertEquals("testName4 - 12", "Esq.", jsCard.getName().getComponents()[4].getValue());
        assertEquals("testName4 - 12", 2, jsCard.getNicknames().size());
        assertEquals("testName4 - 13", "Johnny", jsCard.getNicknames().get("NICK-1").getName());
        assertEquals("testName4 - 14", "Kid", jsCard.getNicknames().get("NICK-2").getName());
        assertEquals("testName4 - 15", "Giovannino", jsCard.getLocalization("it", "nicknames/NICK-1").get("name").asText());
        assertEquals("testName4 - 16", "Ragazzo", jsCard.getLocalization("it", "nicknames/NICK-2").get("name").asText());

    }

    @Test //ez-vcard accepts only one family name and one given name
    public void testName5() throws CardException {

        String vcard = "BEGIN:VCARD\n" +
                "VERSION:4.0\n" +
                "FN:John Paul Philip Stevenson\n" +
                "N:Stevenson;John;Philip,Paul;Dr.;Jr.,M.D.,A.C.P.\n" +
                "END:VCARD";

        Card jsCard = vCard2JSContact.convert(vcard).get(0);
        assertEquals("testName5 - 1", 8, jsCard.getName().getComponents().length);
        assertEquals("testName5 - 2", "Dr.", jsCard.getName().getComponents()[4].getValue());
        assertTrue("testName5 - 3",  jsCard.getName().getComponents()[4].isTitle());
        assertEquals("testName5 - 5", "John", jsCard.getName().getGiven());
        assertTrue("testName5 - 6",  jsCard.getName().getComponents()[1].isGiven());
        assertEquals("testName5 - 8", "Stevenson", jsCard.getName().getSurname());
        assertTrue("testName5 - 9",  jsCard.getName().getComponents()[0].isSurname());
        assertEquals("testName5 - 11", "Philip", jsCard.getName().getComponents()[2].getValue());
        assertTrue("testName5 - 12",  jsCard.getName().getComponents()[2].isGiven2());
        assertEquals("testName5 - 14", "Paul", jsCard.getName().getComponents()[3].getValue());
        assertTrue("testName5 - 15",  jsCard.getName().getComponents()[3].isGiven2());
        assertEquals("testName5 - 17", "Jr.", jsCard.getName().getComponents()[5].getValue());
        assertTrue("testName5 - 18",  jsCard.getName().getComponents()[5].isCredential());
        assertEquals("testName5 - 20", "M.D.", jsCard.getName().getComponents()[6].getValue());
        assertTrue("testName5 - 21",  jsCard.getName().getComponents()[6].isCredential());
        assertEquals("testName5 - 23", "A.C.P.", jsCard.getName().getComponents()[7].getValue());
        assertTrue("testName5 - 24",  jsCard.getName().getComponents()[7].isCredential());

    }

    @Test
    public void testName6() throws CardException {

        String vcard = "BEGIN:VCARD\n" +
                "VERSION:4.0\n" +
                "FN:John Paul Philip Stevenson\n" +
                "N;ALTID=1;LANGUAGE=zh-Hant:孫;中山;文,逸仙;;\n" +
                "N;ALTID=1;PHONETIC=jyut;SCRIPT=Latn;LANGUAGE=yue:syun1;zung1saan1;man4,jat6sin1;;\n" +
                "END:VCARD";

        Card jsCard = vCard2JSContact.convert(vcard).get(0);
        assertEquals("testName6 - 1", "zh-Hant", jsCard.getLanguage());
        assertEquals("testName6 - 2", 4, jsCard.getName().getComponents().length);
        assertEquals("testName6 - 3", "孫", jsCard.getName().getSurname());
        assertTrue("testName6 - 4",  jsCard.getName().getComponents()[0].isSurname());
        assertEquals("testName6 - 5", "中山", jsCard.getName().getGiven());
        assertTrue("testName6 - 6",  jsCard.getName().getComponents()[1].isGiven());
        assertEquals("testName6 - 7", "文", jsCard.getName().getComponents()[2].getValue());
        assertTrue("testName6 - 8",  jsCard.getName().getComponents()[2].isGiven2());
        assertEquals("testName6 - 9", "逸仙", jsCard.getName().getComponents()[3].getValue());
        assertTrue("testName6 - 10",  jsCard.getName().getComponents()[3].isGiven2());
        assertEquals("testName6 - 11", 6, jsCard.getLocalizationsPerLanguage("yue").size());
        assertEquals("testName6 - 12", "Latn", jsCard.getLocalization("yue","name/phoneticScript").asText());
        assertEquals("testName6 - 13", "jyut", jsCard.getLocalization("yue","name/phoneticSystem").asText());
        assertEquals("testName6 - 14", "syun1", jsCard.getLocalization("yue","name/components/0/phonetic").asText());
        assertEquals("testName6 - 15", "zung1saan1", jsCard.getLocalization("yue","name/components/1/phonetic").asText());
        assertEquals("testName6 - 16", "man4", jsCard.getLocalization("yue","name/components/2/phonetic").asText());
        assertEquals("testName6 - 17", "jat6sin1", jsCard.getLocalization("yue","name/components/3/phonetic").asText());
    }

    @Test
    public void testName7() throws CardException {

        String vcard = "BEGIN:VCARD\n" +
                        "VERSION:4.0\n" +
                        "N;JSCOMPS=\";1;0\":Doe;Jane;;;;;;\n" +
                        "FN;DERIVED=TRUE:Jane Doe\n" +
                        "END:VCARD";

        Card jsCard = vCard2JSContact.convert(vcard).get(0);
        assertEquals("testName7 - 2", true, jsCard.getName().getIsOrdered());
        assertEquals("testName7 - 3", 2, jsCard.getName().getComponents().length);
        assertEquals("testName7 - 4", "Doe", jsCard.getName().getSurname());
        assertTrue("testName7 - 5",  jsCard.getName().getComponents()[1].isSurname());
        assertEquals("testName7 - 6", "Jane", jsCard.getName().getGiven());
        assertTrue("testName7 - 7",  jsCard.getName().getComponents()[0].isGiven());
    }

    @Test
    public void testName8() throws CardException {

        String vcard = "BEGIN:VCARD\n" +
                "VERSION:4.0\n" +
                "N;JSCOMPS=\";1;2;2,1;0;6;4,1\":Stevenson;John;Philip,Paul;;Jr.,M.D.;;Jr.\n" +
                "FN;DERIVED=TRUE:John Philip Paul Stevenson Jr. M.D.\n" +
                "END:VCARD";

        Card jsCard = vCard2JSContact.convert(vcard).get(0);
        assertEquals("testName8 - 2", true, jsCard.getName().getIsOrdered());
        assertEquals("testName8 - 3", 6, jsCard.getName().getComponents().length);
        assertEquals("testName8 - 4", "John", jsCard.getName().getGiven());
        assertTrue("testName8 - 5",  jsCard.getName().getComponents()[0].isGiven());
        assertEquals("testName8 - 6", "Philip", jsCard.getName().getComponents()[1].getValue());
        assertTrue("testName8 - 7",  jsCard.getName().getComponents()[1].isGiven2());
        assertEquals("testName8 - 8", "Paul", jsCard.getName().getComponents()[2].getValue());
        assertTrue("testName8 - 9",  jsCard.getName().getComponents()[2].isGiven2());
        assertEquals("testName8 - 10", "Stevenson", jsCard.getName().getSurname());
        assertTrue("testName8 - 11",  jsCard.getName().getComponents()[3].isSurname());
        assertEquals("testName8 - 10", "Jr.", jsCard.getName().getGeneration());
        assertTrue("testName8 - 11",  jsCard.getName().getComponents()[4].isGeneration());
        assertEquals("testName8 - 12", "M.D.", jsCard.getName().getComponents()[5].getValue());
        assertTrue("testName8 - 13",  jsCard.getName().getComponents()[5].isCredential());

    }

    @Test
    public void testName9() throws CardException {

        String vcard = "BEGIN:VCARD\n" +
                "VERSION:4.0\n" +
                "N;JSCOMPS=\";1;2;2,1;0;6;4\":Stevenson;John;Philip,Paul;;M.D.,Jr.;;Jr.\n" +
                "FN;DERIVED=TRUE:John Philip Paul Stevenson Jr. M.D.\n" +
                "END:VCARD";

        Card jsCard = vCard2JSContact.convert(vcard).get(0);
        assertEquals("testName9 - 2", true, jsCard.getName().getIsOrdered());
        assertEquals("testName9 - 3", 6, jsCard.getName().getComponents().length);
        assertEquals("testName9 - 4", "John", jsCard.getName().getGiven());
        assertTrue("testName9 - 5",  jsCard.getName().getComponents()[0].isGiven());
        assertEquals("testName9 - 6", "Philip", jsCard.getName().getComponents()[1].getValue());
        assertTrue("testName9 - 7",  jsCard.getName().getComponents()[1].isGiven2());
        assertEquals("testName9 - 8", "Paul", jsCard.getName().getComponents()[2].getValue());
        assertTrue("testName9 - 9",  jsCard.getName().getComponents()[2].isGiven2());
        assertEquals("testName9 - 10", "Stevenson", jsCard.getName().getSurname());
        assertTrue("testName9 - 11",  jsCard.getName().getComponents()[3].isSurname());
        assertEquals("testName9 - 10", "Jr.", jsCard.getName().getGeneration());
        assertTrue("testName9 - 11",  jsCard.getName().getComponents()[4].isGeneration());
        assertEquals("testName9 - 12", "M.D.", jsCard.getName().getComponents()[5].getValue());
        assertTrue("testName9 - 13",  jsCard.getName().getComponents()[5].isCredential());

    }

    @Test
    public void testName10() throws CardException {

        String vcard = "BEGIN:VCARD\n" +
                "VERSION:4.0\n" +
                "N;JSCOMPS=\";1;2;2,1;0;s,-;5;6;4\":Stevenson,Loffredo;John;Philip,Paul;;M.D.,Jr.;Loffredo;Jr.\n" +
                "FN;DERIVED=TRUE:John Philip Paul Stevenson-Loffredo Jr. M.D.\n" +
                "END:VCARD";

        Card jsCard = vCard2JSContact.convert(vcard).get(0);
        assertEquals("testName10 - 2", true, jsCard.getName().getIsOrdered());
        assertEquals("testName10 - 3", 8, jsCard.getName().getComponents().length);
        assertEquals("testName10 - 4", "John", jsCard.getName().getGiven());
        assertTrue("testName10 - 5",  jsCard.getName().getComponents()[0].isGiven());
        assertEquals("testName10 - 6", "Philip", jsCard.getName().getComponents()[1].getValue());
        assertTrue("testName10 - 7",  jsCard.getName().getComponents()[1].isGiven2());
        assertEquals("testName10 - 8", "Paul", jsCard.getName().getComponents()[2].getValue());
        assertTrue("testName10 - 9",  jsCard.getName().getComponents()[2].isGiven2());
        assertEquals("testName10 - 10", "Stevenson", jsCard.getName().getSurname());
        assertTrue("testName10 - 11",  jsCard.getName().getComponents()[3].isSurname());
        assertEquals("testName10 - 12", "-", jsCard.getName().getComponents()[4].getValue());
        assertTrue("testName10 - 13",  jsCard.getName().getComponents()[4].isSeparator());
        assertEquals("testName10 - 14", "Loffredo", jsCard.getName().getSurname2());
        assertTrue("testName10 - 15",  jsCard.getName().getComponents()[5].isSurname2());
        assertEquals("testName10 - 16", "Jr.", jsCard.getName().getGeneration());
        assertTrue("testName10 - 17",  jsCard.getName().getComponents()[6].isGeneration());
        assertEquals("testName10 - 18", "M.D.", jsCard.getName().getComponents()[7].getValue());
        assertTrue("testName10 - 19",  jsCard.getName().getComponents()[7].isCredential());

    }

    @Test
    public void testName11() throws CardException {

        String vcard = "BEGIN:VCARD\n" +
                "VERSION:4.0\n" +
                "FN;DERIVED=true;ALTID=1:Mr. Ivan Petrovich Vasiliev\n" +
                "FN;DERIVED=true;LANGUAGE=uk-Cyrl;ALTID=1:г-н Иван Петрович Васильев\n" +
                "N;ALTID=1:Vasiliev;Ivan;Petrovich;Mr.;;;\n" +
                "N;LANGUAGE=uk-Cyrl;ALTID=1:Васильев;Иван;Петрович;г-н;;;\n" +
                "END:VCARD";

        Card jsCard = vCard2JSContact.convert(vcard).get(0);
        assertEquals("testName11 - 1", 4, jsCard.getName().getComponents().length);
        assertEquals("testName11 - 2", "Vasiliev", jsCard.getName().getSurname());
        assertEquals("testName11 - 3", "Ivan", jsCard.getName().getGiven());
        assertEquals("testName11 - 4", "Petrovich", jsCard.getName().getGiven2());
        assertTrue("testName11 - 5",  jsCard.getName().getComponents()[3].isTitle());
        assertEquals("testName11 - 6", "Mr.", jsCard.getName().getComponents()[3].getValue());
        assertEquals("testName11 - 7", 1, jsCard.getLocalizationsPerLanguage("uk-Cyrl").size());
        assertNotNull("testName11 - 8",  jsCard.getLocalization("uk-Cyrl","name"));
        Name nameLocalization = (Name) JsonNodeUtils.toObject(jsCard.getLocalization("uk-Cyrl","name"), Name.class);
        assertEquals("testName11 - 9", "Васильев", nameLocalization.getSurname());
        assertEquals("testName11 - 10", "Иван", nameLocalization.getGiven());
        assertEquals("testName11 - 11", "Петрович", nameLocalization.getGiven2());
        assertTrue("testName11 - 12",  nameLocalization.getComponents()[3].isTitle());
        assertEquals("testName11 - 13", "г-н", nameLocalization.getComponents()[3].getValue());
    }

    @Test
    public void testName12() throws CardException {

        String vcard = "BEGIN:VCARD\n" +
                "VERSION:4.0\n" +
                "FN;DERIVED=true:John Smith\n" +
                "N;ALTID=1:Smith;John;;;;;\n" +
                "N;PHONETIC=ipa;ALTID=1:/smɪθ/;/d͡ʒɑn/;;;;\n" +
                "END:VCARD";

        Card jsCard = vCard2JSContact.convert(vcard).get(0);
        assertEquals("testName12 - 1", PhoneticSystem.ipa(), jsCard.getName().getPhoneticSystem());
        assertNull("testName12 - 2", jsCard.getName().getPhoneticScript());
        assertEquals("testName12 - 3", 2, jsCard.getName().getComponents().length);
        assertTrue("testName12 - 4",  jsCard.getName().getComponents()[0].isSurname());
        assertEquals("testName12 - 5", "Smith", jsCard.getName().getComponents()[0].getValue());
        assertEquals("testName12 - 6", "/smɪθ/", jsCard.getName().getComponents()[0].getPhonetic());
        assertTrue("testName12 - 7",  jsCard.getName().getComponents()[1].isGiven());
        assertEquals("testName12 - 8", "John", jsCard.getName().getComponents()[1].getValue());
        assertEquals("testName12 - 9", "/d͡ʒɑn/", jsCard.getName().getComponents()[1].getPhonetic());
    }

    @Test
    public void testName13() throws CardException {

        String vcard = "BEGIN:VCARD\n" +
                "VERSION:4.0\n" +
                "FN;DERIVED=true:Ms. Mary Jean Elizabeth van Halen Barrientos III\\, PhD\n" +
                "N;JSCOMPS=\";3;1;2;0;5;6;s,\\, ;4\":van Halen,Barrientos;Mary Jean;Elizabeth;Ms.;PhD,III;Barrientos;III\n" +
                "END:VCARD";

        Card jsCard = vCard2JSContact.convert(vcard).get(0);
        assertEquals("testName13 - 1", 8, jsCard.getName().getComponents().length);
        assertTrue("testName13 - 2", jsCard.getName().getIsOrdered());
        assertTrue("testName13 - 3", jsCard.getName().getComponents()[0].isTitle());
        assertEquals("testName13 - 4", "Ms.", jsCard.getName().getComponents()[0].getValue());
        assertTrue("testName13 - 5", jsCard.getName().getComponents()[1].isGiven());
        assertEquals("testName13 - 6", "Mary Jean", jsCard.getName().getComponents()[1].getValue());
        assertTrue("testName13 - 7", jsCard.getName().getComponents()[2].isGiven2());
        assertEquals("testName13 - 8", "Elizabeth", jsCard.getName().getComponents()[2].getValue());
        assertTrue("testName13 - 9", jsCard.getName().getComponents()[3].isSurname());
        assertEquals("testName13 - 10", "van Halen", jsCard.getName().getComponents()[3].getValue());
        assertTrue("testName13 - 11", jsCard.getName().getComponents()[4].isSurname2());
        assertEquals("testName13 - 12", "Barrientos", jsCard.getName().getComponents()[4].getValue());
        assertTrue("testName13 - 13", jsCard.getName().getComponents()[5].isGeneration());
        assertEquals("testName13 - 14", "III", jsCard.getName().getComponents()[5].getValue());
        assertTrue("testName13 - 15", jsCard.getName().getComponents()[6].isSeparator());
        assertEquals("testName13 - 16", ", ", jsCard.getName().getComponents()[6].getValue());
        assertTrue("testName13 - 17", jsCard.getName().getComponents()[7].isCredential());
        assertEquals("testName13 - 18", "PhD", jsCard.getName().getComponents()[7].getValue());

    }


}

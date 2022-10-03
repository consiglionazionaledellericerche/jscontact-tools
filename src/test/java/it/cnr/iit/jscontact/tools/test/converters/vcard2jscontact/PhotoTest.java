package it.cnr.iit.jscontact.tools.test.converters.vcard2jscontact;

import it.cnr.iit.jscontact.tools.dto.Card;
import it.cnr.iit.jscontact.tools.dto.utils.MimeTypeUtils;
import it.cnr.iit.jscontact.tools.exceptions.CardException;
import org.junit.Test;

import static org.junit.Assert.*;

public class PhotoTest extends VCard2JSContactTest {

    @Test
    public void testPhoto() throws CardException {

        String vcard = "BEGIN:VCARD\n" +
                "VERSION:4.0\n" +
                "FN:test\n" +
                "PHOTO:http://www.example.com/pub/photos/jqpublic.gif\n" +
                "END:VCARD";

        Card jsCard = (Card) vCard2JSContact.convert(vcard).get(0);
        assertEquals("testPhoto - 1", 1, jsCard.getPhotos().size());
        assertEquals("testPhoto - 2", "http://www.example.com/pub/photos/jqpublic.gif", jsCard.getPhotos().get("PHOTO-1").getHref());
        assertNull("testPhoto - 3", jsCard.getPhotos().get("PHOTO-1").getPref());
        assertEquals("testPhoto - 4", MimeTypeUtils.MIME_IMAGE_GIF, jsCard.getPhotos().get("PHOTO-1").getMediaType());
    }

}

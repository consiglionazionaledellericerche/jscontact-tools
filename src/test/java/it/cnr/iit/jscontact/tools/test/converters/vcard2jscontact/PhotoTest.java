package it.cnr.iit.jscontact.tools.test.converters.vcard2jscontact;

import it.cnr.iit.jscontact.tools.dto.Card;
import it.cnr.iit.jscontact.tools.dto.utils.MimeTypeUtils;
import it.cnr.iit.jscontact.tools.exceptions.CardException;
import org.junit.Test;

import static org.junit.Assert.*;

public class PhotoTest extends VCard2JSContactTest {

    @Test
    public void testPhoto1() throws CardException {

        String vcard = "BEGIN:VCARD\n" +
                "VERSION:4.0\n" +
                "FN:test\n" +
                "PHOTO:http://www.example.com/pub/photos/jqpublic.gif\n" +
                "END:VCARD";

        Card jsCard = vCard2JSContact.convert(vcard).get(0);
        assertEquals("testPhoto1 - 1", 1, jsCard.getMedia().size());
        assertEquals("testPhoto1 - 2", "http://www.example.com/pub/photos/jqpublic.gif", jsCard.getMedia().get("PHOTO-1").getUri());
        assertNull("testPhoto1 - 3", jsCard.getMedia().get("PHOTO-1").getPref());
        assertEquals("testPhoto1 - 4", MimeTypeUtils.MIME_IMAGE_GIF, jsCard.getMedia().get("PHOTO-1").getMediaType());
    }

    @Test
    public void testPhoto2() throws CardException {

        String vcard = "BEGIN:VCARD\n" +
                "VERSION:4.0\n" +
                "FN:test\n" +
                "PHOTO:data:image/jpeg;base64,/9j/4AAQSkZJRgABAQAASABIAAD/\n" +
                "END:VCARD";

        Card jsCard = vCard2JSContact.convert(vcard).get(0);
        assertEquals("testPhoto2 - 1", 1, jsCard.getMedia().size());
        assertEquals("testPhoto2 - 2", "data:image/jpeg;base64,/9j/4AAQSkZJRgABAQAASABIAAD/", jsCard.getMedia().get("PHOTO-1").getUri());
        assertNull("testPhoto2 - 3", jsCard.getMedia().get("PHOTO-1").getPref());
    }

}

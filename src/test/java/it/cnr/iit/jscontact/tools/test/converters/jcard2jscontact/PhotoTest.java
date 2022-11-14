package it.cnr.iit.jscontact.tools.test.converters.jcard2jscontact;

import it.cnr.iit.jscontact.tools.dto.Card;
import it.cnr.iit.jscontact.tools.dto.utils.MimeTypeUtils;
import it.cnr.iit.jscontact.tools.exceptions.CardException;
import org.junit.Test;

import static org.junit.Assert.*;

public class PhotoTest extends JCard2JSContactTest {

    @Test
    public void testPhoto() throws CardException {

        String jcard="[\"vcard\",[ [\"version\", {}, \"text\", \"4.0\"], " +
                "[\"fn\", {}, \"text\", \"test\"], " +
                "[\"photo\", {}, \"uri\", \"http://www.example.com/pub/photos/jqpublic.gif\"]" +
                "]]";
        Card jsCard = (Card) jCard2JSContact.convert(jcard).get(0);
        assertEquals("testPhoto - 1", 1, jsCard.getMedia().size());
        assertEquals("testPhoto - 2", "http://www.example.com/pub/photos/jqpublic.gif", jsCard.getMedia().get("PHOTO-1").getUri());
        assertNull("testPhoto - 3", jsCard.getMedia().get("PHOTO-1").getPref());
        assertEquals("testPhoto - 4", MimeTypeUtils.MIME_IMAGE_GIF, jsCard.getMedia().get("PHOTO-1").getMediaType());
    }

}

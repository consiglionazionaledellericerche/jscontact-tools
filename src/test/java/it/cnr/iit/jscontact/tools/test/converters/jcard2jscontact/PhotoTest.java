package it.cnr.iit.jscontact.tools.test.converters.jcard2jscontact;

import it.cnr.iit.jscontact.tools.dto.Card;
import it.cnr.iit.jscontact.tools.dto.utils.MimeTypeUtils;
import it.cnr.iit.jscontact.tools.exceptions.CardException;
import org.junit.Test;

import java.io.IOException;

import static org.junit.Assert.assertTrue;

public class PhotoTest extends JCard2JSContactTest {

    @Test
    public void testPhotoValid() throws CardException {

        String jcard="[\"vcard\",[ [\"version\", {}, \"text\", \"4.0\"], " +
                "[\"fn\", {}, \"text\", \"test\"], " +
                "[\"photo\", {}, \"uri\", \"http://www.example.com/pub/photos/jqpublic.gif\"]" +
                "]]";
        Card jsCard = (Card) jCard2JSContact.convert(jcard).get(0);
        assertTrue("testPhotoValid - 1",jsCard.getPhotos().size() == 1);
        assertTrue("testPhotoValid - 2",jsCard.getPhotos().get("PHOTO-1").getHref().equals("http://www.example.com/pub/photos/jqpublic.gif"));
        assertTrue("testPhotoValid - 3",jsCard.getPhotos().get("PHOTO-1").getPref() == null);
        assertTrue("testPhotoValid - 4",jsCard.getPhotos().get("PHOTO-1").getMediaType().equals(MimeTypeUtils.MIME_IMAGE_GIF));
    }

}

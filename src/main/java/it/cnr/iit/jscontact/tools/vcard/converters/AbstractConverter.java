package it.cnr.iit.jscontact.tools.vcard.converters;

import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.HashMap;
import java.util.Map;

/**
 * Abstract class for converting a JSContact object from/to a vCard 4.0 [RFC6350] and its transliterations.
 *
 * @see <a href="https://tools.ietf.org/html/rfc6350">RFC6350</a>
 * @author Mario Loffredo
 */
public abstract class AbstractConverter {

    protected static final Map<String,String> fakeExtensionsMapping = new HashMap<String, String>() {{
        put("contact-uri","links");
        put("created","created");
        put("locale","locale");
        put("grammatical-gender","speakToAs/grammaticalGender");
        put("pronouns","speakToAs/pronouns");
        put("contact-channel-pref","preferredContactChannels");
        put("x-rfc0000-jsprop","preferredContactChannels");
    }};

    protected static final ObjectMapper mapper = new ObjectMapper();
}

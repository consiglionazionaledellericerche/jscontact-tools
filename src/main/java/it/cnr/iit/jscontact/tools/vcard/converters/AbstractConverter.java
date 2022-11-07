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

    protected static final String VCARD_GENDER_TAG = "GENDER";
    protected static final String VCARD_CLIENTPIDMAP_TAG = "CLIENTPIDMAP";
    protected static final String VCARD_XML_TAG = "XML";

    protected static final String VCARD_TZ_TAG = "TZ";

    protected static final String VCARD_GEO_TAG = "GEO";

    protected static final String VCARD_PROP_ID_PARAM_TAG = "PROP-ID";

    protected static final String VCARD_DERIVED_PARAM_TAG = "DERIVED";

    protected static final String VCARD_INDEX_PARAM_TAG = "INDEX";

    protected static final String VCARD_PID_PARAM_TAG = "PID";

    protected static final String VCARD_LEVEL_PARAM_TAG = "LEVEL";

    protected static final String VCARD_CC_PARAM_TAG = "CC";

    protected static final String VCARD_SORT_AS_PARAM_TAG = "SORT-AS";

    public static final String VCARD_GROUP_PARAM_TAG = "GROUP";

    protected static final String VCARD_X_RFC0000_JSPROP_TAG = "X-RFC0000-JSPROP";
    protected static final String VCARD_X_RFC0000_JSPATH_PARAM_TAG = "X-RFC0000-JSPATH";

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

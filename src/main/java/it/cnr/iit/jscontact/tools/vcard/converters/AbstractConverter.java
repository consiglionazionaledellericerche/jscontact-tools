package it.cnr.iit.jscontact.tools.vcard.converters;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.lang3.StringUtils;

/**
 * Abstract class for converting a JSContact object from/to a vCard 4.0 [RFC6350] and its transliterations.
 * @see <a href="https://tools.ietf.org/html/rfc6350">RFC6350</a>
 *
 * @author Mario Loffredo
 */
public abstract class AbstractConverter {

    protected static final String VCARD_GENDER_TAG = "GENDER";
    protected static final String VCARD_CLIENTPIDMAP_TAG = "CLIENTPIDMAP";
    protected static final String VCARD_XML_TAG = "XML";
    protected static final String DEFAULT_CALSCALE = "gregorian";
    protected static final String UNMATCHED_PROPERTY_PREFIX = "ietf.org:rfc6350:";

    protected static final ObjectMapper mapper = new ObjectMapper();

    protected static String getUnmatchedPropertyName(String propertyName, Integer index) {

        return UNMATCHED_PROPERTY_PREFIX + propertyName + ((index != null) ? ":" + index : StringUtils.EMPTY);
    }

    protected static String getUnmatchedPropertyName(String propertyName) {

        return getUnmatchedPropertyName(propertyName, null);
    }

    private static String getUnmatchedParamName(String propertyName, Integer index, String paramName) {

        return getUnmatchedPropertyName(propertyName) + ":" + ((index != null) ? ":" + index : StringUtils.EMPTY) + paramName;
    }

    protected static String getUnmatchedParamName(String propertyName, String paramName) {

        return getUnmatchedParamName(propertyName, null, paramName);
    }

}

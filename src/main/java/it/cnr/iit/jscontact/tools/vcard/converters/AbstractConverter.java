package it.cnr.iit.jscontact.tools.vcard.converters;

public abstract class AbstractConverter {

    protected static final String SPACE_ARRAY_DELIMITER = " ";
    protected static final String COMMA_ARRAY_DELIMITER = ",";
    protected static final String SEMICOMMA_ARRAY_DELIMITER = ";";
    protected static final String AUTO_FULL_ADDRESS_DELIMITER = "\n";
    protected static final String ANNIVERSAY_MARRIAGE_LABEL = "marriage date";
    protected static final String VCARD_GENDER_TAG = "GENDER";
    protected static final String VCARD_CLIENTPIDMAP_TAG = "CLIENTPIDMAP";
    protected static final String VCARD_XML_TAG = "XML";
    protected static final String DEFAULT_CALSCALE = "gregorian";
    protected static final String UNMATCHED_PROPERTY_PREFIX = "ietf.org/rfc6350/";

    protected static String getUnmatchedPropertyName(String propertyName, Integer index) {

        return UNMATCHED_PROPERTY_PREFIX + propertyName + ((index != null) ? "/" + index : "");
    }

    protected static String getUnmatchedPropertyName(String propertyName) {

        return getUnmatchedPropertyName(propertyName, null);
    }

    private static String getUnmatchedParamName(String propertyName, Integer index, String paramName) {

        return getUnmatchedPropertyName(propertyName) + "/" + ((index != null) ? "/" + index : "") + paramName;
    }

    protected static String getUnmatchedParamName(String propertyName, String paramName) {

        return getUnmatchedParamName(propertyName, null, paramName);
    }

}

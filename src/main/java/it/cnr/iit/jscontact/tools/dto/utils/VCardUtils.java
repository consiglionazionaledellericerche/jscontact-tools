package it.cnr.iit.jscontact.tools.dto.utils;

import ezvcard.VCard;
import ezvcard.parameter.VCardParameters;
import ezvcard.property.RawProperty;
import ezvcard.property.VCardProperty;
import it.cnr.iit.jscontact.tools.dto.AbstractJSContactType;
import it.cnr.iit.jscontact.tools.dto.JCardParam;

import java.util.*;

/**
 * Utility class for handling Ezvcard objects.
 *
 * @author Mario Loffredo
 */
public class VCardUtils {

    public static final String VCARD_GENDER_TAG = "GENDER";
    public static final String VCARD_CLIENTPIDMAP_TAG = "CLIENTPIDMAP";
    public static final String VCARD_XML_TAG = "XML";
    public static final String VCARD_TZ_TAG = "TZ";
    public static final String VCARD_GEO_TAG = "GEO";
    public static final String VCARD_PROP_ID_PARAM_TAG = "PROP-ID";
    public static final String VCARD_DERIVED_PARAM_TAG = "DERIVED";
    public static final String VCARD_INDEX_PARAM_TAG = "INDEX";
    public static final String VCARD_PID_PARAM_TAG = "PID";
    public static final String VCARD_LEVEL_PARAM_TAG = "LEVEL";
    public static final String VCARD_CC_PARAM_TAG = "CC";
    public static final String VCARD_SORT_AS_PARAM_TAG = "SORT-AS";
    public static final String VCARD_GROUP_PARAM_TAG = "GROUP";
    public static final String VCARD_X_RFC0000_JSPROP_TAG = "X-RFC0000-JSPROP";
    public static final String VCARD_X_RFC0000_JSPATH_PARAM_TAG = "X-RFC0000-JSPATH";
    public static final String VCARD_TYPE_PARAM_TAG = "TYPE";
    public static final String VCARD_PREF_PARAM_TAG = "PREF";
    public static final String VCARD_MEDIATYPE_PARAM_TAG = "MEDIATYPE";

    /**
     * Gets the VCardParameter value of a given VCardParameter object identified by the parameter name in the Ezvcard VCardParameters object.
     *
     * @param vCardParameters the Ezvcard VCardParameters object
     * @param parameterName the parameter name
     * @return the VCardParameter value in a collection of VCardParameters, null if the parameter is missing
     */
    public static String getVCardParameterValue(VCardParameters vCardParameters, String parameterName) {
        try {
            List<String> values = vCardParameters.get(parameterName);
            if (values.size()==0) return null;
            return String.join(DelimiterUtils.COMMA_ARRAY_DELIMITER, values);
        } catch (NullPointerException e) {
            return null;
        }
    }

    /**
     * Gets the Ezvcard VCardParameters object corresponding to a JCardProp "parameters" map.
     *
     * @param jCardPropParameters the jCardProp "parameters" map
     * @return the Ezvcard VCardParameters object corresponding to a JCardProp "parameters" map
     */
    public static VCardParameters getVCardParameters(Map<String,Object> jCardPropParameters) {

        VCardParameters vCardParameters = new VCardParameters();

        for (Map.Entry<String,Object> entry : jCardPropParameters.entrySet())
            vCardParameters.put(entry.getKey(), entry.getValue().toString());

        return vCardParameters;
    }

    /**
     * Gets JCardProp "parameters" map corresponding to the Ezvcard VCardParameters object.
     *
     * @param vCardParameters the Ezvcard VCardParameters object
     * @return the JCardProp "parameters" map corresponding to the Ezvcard VCardParameters object
     */
    public static Map<String,Object> getJCardPropParameters(VCardParameters vCardParameters) {

        Map<String,Object> jCardPropParameters = new HashMap<>();
        for(String parameterName : vCardParameters.keySet())
            jCardPropParameters.put(parameterName,vCardParameters.get(parameterName));

        return jCardPropParameters;
    }

    /**
     * Gets the list of the Ezvcard RawProperty objects of a VCard with a given property name.
     *
     * @param vcard the VCard
     * @param propertyName the property name
     * @return the list of RawProperty objects
     */
    public static List<RawProperty> getRawProperties(VCard vcard, String propertyName) {

        List<RawProperty> rawProperties = new ArrayList<>();
        for (RawProperty extension : vcard.getExtendedProperties()) {
            if (extension.getPropertyName().equalsIgnoreCase(propertyName)) {
                rawProperties.add(extension);
            }
        }

        return rawProperties;
    }

    /**
     * Gets the "ietf.org:rfc0000:params" map corresponding to the Ezvcard unmatched VCardParameters object of a VCard property.
     *
     * @param property the VCard property
     * @param unmatchedParameterNames the list of unmatched parameter names
     * @return the "ietf.org:rfc0000:params" map corresponding to the Ezvcard VCardParameters object, null if the map is empty
     */
    public static Map<String, JCardParam> getVCardUnmatchedParameters(VCardProperty property, List<String> unmatchedParameterNames) {

        Map<String,JCardParam> jCardParams = new HashMap<>();
        if (property.getGroup()!=null)
            jCardParams.put("group", JCardParam.builder().value(property.getGroup()).build());
        for(String parameterName : property.getParameters().keySet()) {
            if (unmatchedParameterNames.contains(parameterName) || parameterName.startsWith("X-")) {
                String parameterValue = property.getParameter(parameterName);
                if (parameterValue.split(DelimiterUtils.COMMA_ARRAY_DELIMITER).length > 0)
                    jCardParams.put(parameterName.toLowerCase(), JCardParam.builder().values(parameterValue.split(DelimiterUtils.COMMA_ARRAY_DELIMITER)).build());
                else
                    jCardParams.put(parameterName.toLowerCase(), JCardParam.builder().value(parameterValue).build());
            }
        }
        return (jCardParams.size() > 0) ? jCardParams : null;
    }

    /**
     * Adds the unmatched Ezvcard VCardParameters to a VCard property.
     *
     * @param property the VCard property
     * @param unmatchedParams a "ietf.org:rfc0000:params" map
     */
    public static void addVCardUnmatchedParameters(VCardProperty property, Map<String, JCardParam> unmatchedParams) {

        if (unmatchedParams != null) {
            for(Map.Entry<String,JCardParam> jCardParam : unmatchedParams.entrySet()) {
                if (jCardParam.getKey().equalsIgnoreCase(VCARD_GROUP_PARAM_TAG))
                    property.setGroup(jCardParam.getValue().getValue());
                else if (jCardParam.getValue().getValues()!=null)
                    property.addParameter(jCardParam.getKey().toUpperCase(),String.join(DelimiterUtils.COMMA_ARRAY_DELIMITER,jCardParam.getValue().getValues()));
                else
                    property.addParameter(jCardParam.getKey().toUpperCase(),jCardParam.getValue().getValue());
            }
        }
    }

    public static void addVCardUnmatchedParameters(VCardProperty property, AbstractJSContactType jsContactType) {
        addVCardUnmatchedParameters(property, jsContactType.getJCardParams());
    }
}

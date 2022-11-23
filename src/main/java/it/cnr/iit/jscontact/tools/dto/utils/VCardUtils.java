package it.cnr.iit.jscontact.tools.dto.utils;

import ezvcard.VCard;
import ezvcard.parameter.VCardParameters;
import ezvcard.property.RawProperty;
import ezvcard.property.VCardProperty;
import it.cnr.iit.jscontact.tools.dto.AbstractJSContactType;
import it.cnr.iit.jscontact.tools.dto.VCardParam;

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
    public static final String VCARD_AUTHOR_PARAM_TAG = "AUTHOR";
    public static final String VCARD_AUTHOR_NAME_PARAM_TAG = "AUTHOR-NAME";
    public static final String VCARD_CREATED_PARAM_TAG = "CREATED";
    public static final String VCARD_INDEX_PARAM_TAG = "INDEX";
    public static final String VCARD_PID_PARAM_TAG = "PID";
    public static final String VCARD_LEVEL_PARAM_TAG = "LEVEL";
    public static final String VCARD_CC_PARAM_TAG = "CC";
    public static final String VCARD_GROUP_PARAM_TAG = "GROUP";
    public static final String VCARD_JSCONTACT_PROP_TAG = "JSCONTACT-PROP";
    public static final String VCARD_JSPTR_PARAM_TAG = "JSPTR";
    public static final String VCARD_TYPE_PARAM_TAG = "TYPE";
    public static final String VCARD_PREF_PARAM_TAG = "PREF";
    public static final String VCARD_MEDIATYPE_PARAM_TAG = "MEDIATYPE";

    public static final String VCARD_X_ABLABEL_TAG = "X-ABLabel";

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
     * Gets the Ezvcard VCardParameters object corresponding to a VCardProp "parameters" map.
     *
     * @param vCardPropParameters the vCardProp "parameters" map
     * @return the Ezvcard VCardParameters object corresponding to a VCardProp "parameters" map
     */
    public static VCardParameters getVCardParameters(Map<String,Object> vCardPropParameters) {

        VCardParameters vCardParameters = new VCardParameters();

        for (Map.Entry<String,Object> entry : vCardPropParameters.entrySet())
            vCardParameters.put(entry.getKey(), entry.getValue().toString());

        return vCardParameters;
    }

    /**
     * Gets VCardProp "parameters" map corresponding to the Ezvcard VCardParameters object.
     *
     * @param vCardParameters the Ezvcard VCardParameters object
     * @return the VCardProp "parameters" map corresponding to the Ezvcard VCardParameters object
     */
    public static Map<String,Object> getVCardPropParameters(VCardParameters vCardParameters) {

        Map<String,Object> vCardPropParameters = new HashMap<>();
        for(String parameterName : vCardParameters.keySet())
            vCardPropParameters.put(parameterName,vCardParameters.get(parameterName));

        return vCardPropParameters;
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
     * Gets the "vCardParams" map corresponding to the Ezvcard unmatched VCardParameters object of a VCard property.
     *
     * @param property the VCard property
     * @param unmatchedParameterNames the list of unmatched parameter names
     * @return the "vCardParams" map corresponding to the Ezvcard VCardParameters object, null if the map is empty
     */
    public static Map<String, VCardParam> getVCardUnmatchedParameters(VCardProperty property, List<String> unmatchedParameterNames) {

        Map<String, VCardParam> vCardParams = new HashMap<>();
        if (property.getGroup()!=null)
            vCardParams.put("group", VCardParam.builder().value(property.getGroup()).build());
        for(String parameterName : property.getParameters().keySet()) {
            if (unmatchedParameterNames.contains(parameterName) || parameterName.startsWith("X-")) {
                String parameterValue = property.getParameter(parameterName);
                if (parameterValue.split(DelimiterUtils.COMMA_ARRAY_DELIMITER).length > 0)
                    vCardParams.put(parameterName.toLowerCase(), VCardParam.builder().values(parameterValue.split(DelimiterUtils.COMMA_ARRAY_DELIMITER)).build());
                else
                    vCardParams.put(parameterName.toLowerCase(), VCardParam.builder().value(parameterValue).build());
            }
        }
        return (vCardParams.size() > 0) ? vCardParams : null;
    }

    /**
     * Adds the unmatched Ezvcard VCardParameters to a VCard property.
     *
     * @param property the VCard property
     * @param unmatchedParams a "vCardParams" map
     */
    public static void addVCardUnmatchedParameters(VCardProperty property, Map<String, VCardParam> unmatchedParams) {

        if (unmatchedParams != null) {
            for(Map.Entry<String, VCardParam> vCardParam : unmatchedParams.entrySet()) {
                if (vCardParam.getKey().equalsIgnoreCase(VCARD_GROUP_PARAM_TAG))
                    property.setGroup(vCardParam.getValue().getValue());
                else if (vCardParam.getValue().getValues()!=null)
                    property.addParameter(vCardParam.getKey().toUpperCase(),String.join(DelimiterUtils.COMMA_ARRAY_DELIMITER,vCardParam.getValue().getValues()));
                else
                    property.addParameter(vCardParam.getKey().toUpperCase(),vCardParam.getValue().getValue());
            }
        }
    }

    public static void addVCardUnmatchedParameters(VCardProperty property, AbstractJSContactType jsContactType) {
        addVCardUnmatchedParameters(property, jsContactType.getVCardParams());
    }
}

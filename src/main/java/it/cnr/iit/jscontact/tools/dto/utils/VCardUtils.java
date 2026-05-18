package it.cnr.iit.jscontact.tools.dto.utils;

import ezvcard.VCard;
import ezvcard.parameter.VCardParameters;
import ezvcard.property.RawProperty;
import ezvcard.property.VCardProperty;
import it.cnr.iit.jscontact.tools.dto.VCardParam;
import it.cnr.iit.jscontact.tools.dto.VCardParamEnum;

import java.util.*;


/**
 * Utility class for handling Ezvcard objects.
 *
 * @author Mario Loffredo
 */
public class VCardUtils {

    /**
     * Gets the VCardParameter value of a given VCardParameter object identified by the parameter name in the Ezvcard VCardParameters object.
     *
     * @param vCardParameters the Ezvcard VCardParameters object
     * @param parameter the parameter
     * @return the VCardParameter value in a collection of VCardParameters, null if the parameter is missing
     */
    public static String getVCardParamValue(VCardParameters vCardParameters, VCardParamEnum parameter) {
        try {
            List<String> values = vCardParameters.get(parameter.getValue());
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
     * @param ignoreGroup tif the GROUP parameter must be considered
     * @return the Ezvcard VCardParameters object corresponding to a VCardProp "parameters" map
     */
    public static VCardParameters getVCardParameters(Map<String,Object> vCardPropParameters, boolean ignoreGroup) {

        VCardParameters vCardParameters = new VCardParameters();

        for (Map.Entry<String,Object> entry : vCardPropParameters.entrySet()) {
            if (ignoreGroup && VCardParamEnum.GROUP.getValue().equalsIgnoreCase(entry.getKey()))
                continue;
            vCardParameters.put(entry.getKey(), entry.getValue().toString());
        }

        return vCardParameters;
    }

    /**
     * Gets VCardUnconvertedProperty "parameters" map corresponding to the Ezvcard VCardParameters object.
     *
     * @param vCardParameters the Ezvcard VCardParameters object
     * @return the VCardUnconvertedProperty "parameters" map corresponding to the Ezvcard VCardParameters object
     */
    public static Map<String,Object> getVCardUnconvertedPropParams(VCardParameters vCardParameters) {

        Map<String,Object> vCardUnconvertedPropParameters = new HashMap<>();
        for(String parameterName : vCardParameters.keySet()) {
            switch(parameterName) {
                case VCardParameters.PREF:
                case VCardParameters.INDEX:
                    vCardUnconvertedPropParameters.put(parameterName.toLowerCase(),Integer.parseInt(vCardParameters.get(parameterName).get(0)));
                    break;
                default:
                    vCardUnconvertedPropParameters.put(parameterName.toLowerCase(), String.join(DelimiterUtils.COMMA_ARRAY_DELIMITER,vCardParameters.get(parameterName)));
                    break;
            }
        }

        return vCardUnconvertedPropParameters;
    }


    /**
     * Gets VCardConvertedProperty "parameters" map corresponding to the Ezvcard VCardParameters object.
     *
     * @param vCardParameters the Ezvcard VCardParameters object
     * @return the VCardConvertedProperty "parameters" map corresponding to the Ezvcard VCardParameters object
     */
    public static Map<String,VCardParam> getVCardConvertedPropParams(VCardParameters vCardParameters) {

        Map<String,VCardParam> vCardConvertedPropParameters = new HashMap<>();
        for(String parameterName : vCardParameters.keySet()) {
            List<String> parameterValues = vCardParameters.get(parameterName);
            if (parameterValues.size() > 1)
                vCardConvertedPropParameters.put(parameterName.toLowerCase(), VCardParam.builder().values(parameterValues.toArray(new String[0])).build());
            else
                vCardConvertedPropParameters.put(parameterName.toLowerCase(), VCardParam.builder().value(parameterValues.get(0)).build());
        }

        return vCardConvertedPropParameters;
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
     * @param property          the VCard property
     * @param matchedParameters the list of matched parameter names
     * @return the "vCardParams" map corresponding to the Ezvcard VCardParameters object, null if the map is empty
     */
    public static Map<String, VCardParam> getVCardParamsOtherThan(VCardProperty property, VCardParamEnum... matchedParameters) {

        if (property == null)
            return null;

        Map<String, VCardParam> vCardParams = new HashMap<>();
        List<VCardParamEnum> matchedAsList = Arrays.asList(matchedParameters);
        if (property.getGroup() != null)
            vCardParams.put("group", VCardParam.builder().value(property.getGroup()).build());
        for (String parameterName : property.getParameters().keySet()) {
            if (parameterName.toUpperCase().startsWith("X-") || !matchedAsList.contains(VCardParamEnum.getEnum(parameterName))) {
                String parameterValue = property.getParameter(parameterName);
                if (parameterValue.split(DelimiterUtils.COMMA_ARRAY_DELIMITER).length > 1)
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
    public static void addVCardUnmatchedParams(VCardProperty property, Map<String, VCardParam> unmatchedParams) {

        if (unmatchedParams != null) {
            for(Map.Entry<String, VCardParam> vCardParam : unmatchedParams.entrySet()) {
                if (vCardParam.getKey().equalsIgnoreCase(VCardParamEnum.GROUP.getValue()))
                    property.setGroup(vCardParam.getValue().getValue());
                else if (vCardParam.getValue().getValues()!=null)
                    property.addParameter(vCardParam.getKey().toUpperCase(),String.join(DelimiterUtils.COMMA_ARRAY_DELIMITER,vCardParam.getValue().getValues()));
                else
                    property.addParameter(vCardParam.getKey().toUpperCase(),vCardParam.getValue().getValue());
            }
        }
    }

}

package it.cnr.iit.jscontact.tools.dto.utils.builders;

import it.cnr.iit.jscontact.tools.dto.PhoneFeature;
import java.util.HashMap;
import java.util.Map;

/**
 * Class for building the Phone.features map.
 *
 * @author Mario Loffredo
 */
public class PhoneFeaturesBuilder {

    private Map<PhoneFeature, Boolean> map;

    public PhoneFeaturesBuilder() {
        map = new HashMap<>();
    }


    /**
     * Adds the voice phone feature.
     * @return the phone features builder updated by adding the voice phone feature
     */
    public PhoneFeaturesBuilder voice() {
        map.put(PhoneFeature.voice(), true);
        return this;
    }

    /**
     * Adds the fax phone feature.
     * @return the phone features builder updated by adding the fax phone feature
     */
    public PhoneFeaturesBuilder fax() {
        map.put(PhoneFeature.fax(), true);
        return this;
    }

    /**
     * Adds the mobile phone feature.
     * @return the phone features builder updated by adding the mobile phone feature
     */
    public PhoneFeaturesBuilder mobile() {
        map.put(PhoneFeature.mobile(), true);
        return this;
    }

    /**
     * Adds the pager phone feature.
     * @return the phone features builder updated by adding the pager phone feature
     */
    public PhoneFeaturesBuilder pager() {
        map.put(PhoneFeature.pager(), true);
        return this;
    }

    /**
     * Adds the text phone feature.
     * @return the phone features builder updated by adding the text phone feature
     */
    public PhoneFeaturesBuilder text() {
        map.put(PhoneFeature.text(), true);
        return this;
    }

    /**
     * Adds the textphone phone feature.
     * @return the phone features builder updated by adding the textphone phone feature
     */
    public PhoneFeaturesBuilder textphone() {
        map.put(PhoneFeature.textphone(), true);
        return this;
    }

    /**
     * Adds the main-number phone feature.
     * @return the phone features builder updated by adding the main-number phone feature
     */
    public PhoneFeaturesBuilder mainNumber() {
        map.put(PhoneFeature.mainNumber(), true);
        return this;
    }

    /**
     * Adds an ext phone feature.
     * @param extValue the ext phone feature value
     * @return the phone features builder updated by adding the ext phone feature
     */
    public PhoneFeaturesBuilder ext(String extValue) {
        map.put(PhoneFeature.ext(extValue), true);
        return this;
    }

    /**
     * Returns the phone features map.
     * @return the phone features map
     */
    public Map build() {
        return map;
    }

}

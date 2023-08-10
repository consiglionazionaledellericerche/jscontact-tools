package it.cnr.iit.jscontact.tools.dto.utils.builders;

import it.cnr.iit.jscontact.tools.dto.PhoneFeature;
import java.util.HashMap;
import java.util.Map;

public class PhoneFeaturesBuilder {

    private Map<PhoneFeature, Boolean> map;

    public PhoneFeaturesBuilder() {
        map = new HashMap<>();
    }

    public PhoneFeaturesBuilder voice() {
        map.put(PhoneFeature.voice(), true);
        return this;
    }

    public PhoneFeaturesBuilder fax() {
        map.put(PhoneFeature.fax(), true);
        return this;
    }

    public PhoneFeaturesBuilder mobile() {
        map.put(PhoneFeature.mobile(), true);
        return this;
    }

    public PhoneFeaturesBuilder pager() {
        map.put(PhoneFeature.pager(), true);
        return this;
    }

    public PhoneFeaturesBuilder text() {
        map.put(PhoneFeature.text(), true);
        return this;
    }

    public PhoneFeaturesBuilder textphone() {
        map.put(PhoneFeature.textphone(), true);
        return this;
    }

    public PhoneFeaturesBuilder mainNumber() {
        map.put(PhoneFeature.mainNumber(), true);
        return this;
    }

    public PhoneFeaturesBuilder ext(String extValue) {
        map.put(PhoneFeature.ext(extValue), true);
        return this;
    }

    public Map build() {
        return map;
    }

}

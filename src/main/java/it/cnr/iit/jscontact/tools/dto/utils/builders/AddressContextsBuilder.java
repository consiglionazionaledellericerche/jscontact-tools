package it.cnr.iit.jscontact.tools.dto.utils.builders;

import it.cnr.iit.jscontact.tools.dto.AddressContext;
import it.cnr.iit.jscontact.tools.dto.Context;

import java.util.HashMap;
import java.util.Map;

public class AddressContextsBuilder {

    private Map<AddressContext, Boolean> map;

    public AddressContextsBuilder() {
        map = new HashMap<>();
    }

    public AddressContextsBuilder work() {
        map.put(AddressContext.work(), true);
        return this;
    }

    public AddressContextsBuilder private_() {
        map.put(AddressContext.private_(), true);
        return this;
    }

    public AddressContextsBuilder billing() {
        map.put(AddressContext.billing(), true);
        return this;
    }

    public AddressContextsBuilder delivery() {
        map.put(AddressContext.delivery(), true);
        return this;
    }

    public AddressContextsBuilder ext(String extValue) {
        map.put(AddressContext.ext(extValue), true);
        return this;
    }

    public Map build() {
        return map;
    }

}

package it.cnr.iit.jscontact.tools.dto.utils.builders;

import it.cnr.iit.jscontact.tools.dto.Context;
import java.util.HashMap;
import java.util.Map;

public class ContextsBuilder {

    private Map<Context, Boolean> map;

    public ContextsBuilder() {
        map = new HashMap<>();
    }

    public ContextsBuilder work() {
        map.put(Context.work(), true);
        return this;
    }

    public ContextsBuilder private_() {
        map.put(Context.private_(), true);
        return this;
    }

    public ContextsBuilder ext(String extValue) {
        map.put(Context.ext(extValue), true);
        return this;
    }

    public Map build() {
        return map;
    }

}

package it.cnr.iit.jscontact.tools.dto.utils.builders;

import it.cnr.iit.jscontact.tools.dto.Context;
import java.util.HashMap;
import java.util.Map;

/**
 * Class for building the contexts map.
 *
 * @author Mario Loffredo
 */
public class ContextsBuilder {

    private Map<Context, Boolean> map;

    public ContextsBuilder() {
        map = new HashMap<>();
    }

    public static ContextsBuilder builder() {
        return new ContextsBuilder();
    }

    /**
     * Adds the work context.
     * @return the contexts builder updated by adding the work context
     */
    public ContextsBuilder work() {
        map.put(Context.work(), true);
        return this;
    }

    /**
     * Adds the private context.
     * @return the contexts builder updated by adding the private context
     */
    public ContextsBuilder private_() {
        map.put(Context.private_(), true);
        return this;
    }

    /**
     * Adds an ext context.
     * @param extValue the ext context value
     * @return the contexts builder updated by adding the ext context
     */
    public ContextsBuilder ext(String extValue) {
        map.put(Context.ext(extValue), true);
        return this;
    }

    /**
     * Returns the contexts map.
     * @return the contexts map
     */
    public Map build() {
        return map;
    }

}

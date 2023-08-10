package it.cnr.iit.jscontact.tools.dto.interfaces;

import com.fasterxml.jackson.annotation.JsonIgnore;
import it.cnr.iit.jscontact.tools.dto.Context;
import org.apache.commons.lang3.ArrayUtils;

import java.util.HashMap;
import java.util.Map;

/**
 * This interface imposes that a class implementing it must include the "context" property.
 *
 * @author Mario Loffredo
C */
public interface HasContexts {

    /**
     * Tests if the context map includes a given context.
     *
     * @param context the given context to check
     * @return true if the context map includes the given context, false otherwise
     */
    default boolean asContext(Context context) { return !hasNoContext() && getContexts().containsKey(context); }
    /**
     * Tests if the context map includes the "work" context.
     *
     * @return true if the context map includes the "work" context, false otherwise
     */
    default boolean asWork() { return asContext(Context.work()); }
    /**
     * Tests if the context map includes the "private" context.
     *
     * @return true if the context map includes the "private" context, false otherwise
     */
    default boolean asPrivate() { return asContext(Context.private_()); }

    /**
     * Tests if the context map includes a custom context.
     *
     * @param extValue the custom context in text format
     * @return true if the context map includes a custom context, false otherwise
     */
    default boolean asExtContext(String extValue) { return asContext(Context.ext(extValue)); }
    /**
     * Tests if the context map is empty.
     *
     * @return true if the context map is empty, false otherwise
     */
    default boolean hasNoContext() { return getContexts() == null || getContexts().size() == 0; }

    /**
     * This method will be used to get the context map.
     *
     * @return the context map
     */
    Map<Context,Boolean> getContexts();

    /**
     * This method will be used to get the extended contexts in the "contexts" property.
     *
     * @return the extended contexts in the "contexts" property
     */
    @JsonIgnore
    default Context[] getExtContexts() {
        if (getContexts() == null)
            return null;
        Context[] extended = null;
        for(Context context : getContexts().keySet()) {
            if (context.isExtValue())
                extended = ArrayUtils.add(extended, context);
        }

        return extended;
    }

    /**
     * Adds a context to the object implementing this interface.
     *
     * @param context the context
     */
    default void addContext(Context context) {
        Map<Context, Boolean> clone;

        if (getContexts() == null)
            clone = new HashMap<>();
        else
            clone = new HashMap<>(getContexts());
        clone.put(context,Boolean.TRUE);
        setContexts(clone);
    }

    void setContexts(Map<Context,Boolean> contexts);
}

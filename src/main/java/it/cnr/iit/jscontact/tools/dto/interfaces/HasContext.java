package it.cnr.iit.jscontact.tools.dto.interfaces;

import it.cnr.iit.jscontact.tools.dto.Context;

import java.util.Map;

/**
 * This interface imposes that a class implementing it must include the "context" property.
 *
 * @author Mario Loffredo
 */
public interface HasContext {

    /**
     * Tests if the context map includes a given context.
     *
     * @param context the given context to check
     * @return true if the context map includes the given context, false otherwise
     */
    default boolean asContext(Context context) { return getContexts() != null && getContexts().containsKey(context); }
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
     * Tests if the context map includes the "other" context.
     *
     * @return true if the context map includes the "other" context, false otherwise
     */
    default boolean asOtherContext() { return asContext(Context.other()); }

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
}

package it.cnr.iit.jscontact.tools.dto.interfaces;

import it.cnr.iit.jscontact.tools.dto.Context;

import java.util.Map;

public interface HasContext {

    default boolean asContext(Context context) { return getContexts() != null && getContexts().containsKey(context); }
    default boolean asWork() { return asContext(Context.WORK); }
    default boolean asPrivate() { return asContext(Context.PRIVATE); }
    default boolean asOtherContext() { return asContext(Context.OTHER); }
    default boolean hasNoContext() { return getContexts() == null || getContexts().size() == 0; }

    Map<Context,Boolean> getContexts();
}

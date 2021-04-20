package it.cnr.iit.jscontact.tools.dto.interfaces;

import it.cnr.iit.jscontact.tools.dto.Context;

import java.util.Map;

public interface hasContext {

    default boolean hasWork() { return getContexts() != null && getContexts().containsKey(Context.WORK); }

    default boolean hasPrivate() { return getContexts() != null && getContexts().containsKey(Context.PRIVATE); }

    default boolean hasOtherContext() { return getContexts() != null && getContexts().containsKey(Context.OTHER); }

    Map<Context,Boolean> getContexts();
}

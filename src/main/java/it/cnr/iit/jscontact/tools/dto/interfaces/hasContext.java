package it.cnr.iit.jscontact.tools.dto.interfaces;

import it.cnr.iit.jscontact.tools.dto.Context;

import java.util.Map;

public interface hasContext {

    default boolean asWork() { return getContexts() != null && getContexts().containsKey(Context.WORK); }

    default boolean asPrivate() { return getContexts() != null && getContexts().containsKey(Context.PRIVATE); }

    default boolean asOtherContext() { return getContexts() != null && getContexts().containsKey(Context.OTHER); }

    Map<Context,Boolean> getContexts();
}

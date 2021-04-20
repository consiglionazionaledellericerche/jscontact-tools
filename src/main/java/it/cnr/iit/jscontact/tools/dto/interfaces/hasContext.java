package it.cnr.iit.jscontact.tools.dto.interfaces;

import it.cnr.iit.jscontact.tools.dto.Context;

import java.util.Map;

public interface hasContext {

    default boolean asWork() { return getContexts() != null && getContexts().containsKey(Context.WORK); }

    default boolean asPrivate() { return getContexts() != null && getContexts().containsKey(Context.PRIVATE); }

    default boolean asOtherContext() { return getContexts() != null && getContexts().containsKey(Context.OTHER); }

    default boolean hasNoContext() { return getContexts() == null || getContexts().size() == 0; }

    Map<Context,Boolean> getContexts();
}

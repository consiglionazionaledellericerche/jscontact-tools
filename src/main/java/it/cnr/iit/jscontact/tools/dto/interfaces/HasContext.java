package it.cnr.iit.jscontact.tools.dto.interfaces;

import it.cnr.iit.jscontact.tools.dto.Context;
import it.cnr.iit.jscontact.tools.dto.ContextEnum;

import java.util.Map;

public interface HasContext {

    default boolean asContext(Context context) { return getContexts() != null && getContexts().containsKey(context); }
    default boolean asWork() { return asContext(Context.work()); }
    default boolean asPrivate() { return asContext(Context.private_()); }
    default boolean asOtherContext() { return asContext(Context.other()); }
    default boolean hasNoContext() { return getContexts() == null || getContexts().size() == 0; }

    Map<Context,Boolean> getContexts();
}

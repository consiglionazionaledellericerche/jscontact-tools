package it.cnr.iit.jscontact.tools.dto.utils.builders;

import it.cnr.iit.jscontact.tools.dto.AddressContext;
import it.cnr.iit.jscontact.tools.dto.Context;

import java.util.HashMap;
import java.util.Map;

/**
 * Class for building the Address.contexts map.
 *
 * @author Mario Loffredo
 */
public class AddressContextsBuilder {

    private Map<AddressContext, Boolean> map;

    public AddressContextsBuilder() {
        map = new HashMap<>();
    }

    /**
     * Adds the work address context.
     * @return the address contexts builder updated by adding the work address context
     */
    public AddressContextsBuilder work() {
        map.put(AddressContext.work(), true);
        return this;
    }

    /**
     * Adds the private address context.
     * @return the address contexts builder updated by adding the private address context
     */
    public AddressContextsBuilder private_() {
        map.put(AddressContext.private_(), true);
        return this;
    }

    /**
     * Adds the billing address context.
     * @return the address contexts builder updated by adding the billing address context
     */
    public AddressContextsBuilder billing() {
        map.put(AddressContext.billing(), true);
        return this;
    }

    /**
     * Adds the delivery address context.
     * @return the address contexts builder updated by adding the delivery address context
     */
    public AddressContextsBuilder delivery() {
        map.put(AddressContext.delivery(), true);
        return this;
    }

    /**
     * Adds an ext address context.
     * @param extValue the ext context value
     * @return the address contexts builder updated by adding an ext address context
     */
    public AddressContextsBuilder ext(String extValue) {
        map.put(AddressContext.ext(extValue), true);
        return this;
    }

    /**
     * Returns the address contexts map.
     * @return the address contexts map
     */
    public Map build() {
        return map;
    }

}

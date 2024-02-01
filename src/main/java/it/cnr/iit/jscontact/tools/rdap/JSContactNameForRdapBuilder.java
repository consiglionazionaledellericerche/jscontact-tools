package it.cnr.iit.jscontact.tools.rdap;

import it.cnr.iit.jscontact.tools.dto.Name;
import it.cnr.iit.jscontact.tools.dto.NameComponent;
import lombok.AllArgsConstructor;

@AllArgsConstructor
public class JSContactNameForRdapBuilder {

    private Name name;

    /**
     * Returns a JSContactNameForRdapBuilder object used to build a JSContact Name object.
     *
     * @return the JSContactNameForRdapBuilder object
     */
    public static JSContactNameForRdapBuilder builder() {
        return new JSContactNameForRdapBuilder(Name.builder().build());
    }

    /**
     * Sets the full name and returns this JSContactNameForRdapBuilder object updated.
     *
     * @param fullName the full name value to be assigned
     * @return this JSContactNameForRdapBuilder object updated
     */
    public JSContactNameForRdapBuilder full(String fullName) {
        if (fullName == null) return this;
        this.name.setFull(fullName);
        return this;
    }

    /**
     * Sets the surname and returns this JSContactNameForRdapBuilder object updated.
     *
     * @param surname the full name value to be assigned
     * @return this JSContactNameForRdapBuilder object updated
     */
    public JSContactNameForRdapBuilder surname(String surname) {
        if (surname == null) return this;
        this.name.addComponent(NameComponent.surname(surname));
        return this;
    }

    /**
     * Sets the given name and returns this JSContactNameForRdapBuilder object updated.
     *
     * @param given the given name value to be assigned
     * @return this JSContactNameForRdapBuilder object updated
     */
    public JSContactNameForRdapBuilder given(String given) {
        if (given == null) return this;
        this.name.addComponent(NameComponent.given(given));
        return this;
    }

    /**
     * Returns the JSContact Name object with the given properties set
     *
     * @return the JSContact Name object
     */
    public Name build() { return name; }

}

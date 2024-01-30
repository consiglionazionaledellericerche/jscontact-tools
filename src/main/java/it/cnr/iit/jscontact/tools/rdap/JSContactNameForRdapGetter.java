package it.cnr.iit.jscontact.tools.rdap;

import it.cnr.iit.jscontact.tools.dto.Name;
import lombok.AllArgsConstructor;

@AllArgsConstructor
public class JSContactNameForRdapGetter {

    private Name name;

    /**
     * Returns a JSContactNameForRdapGetter object initialized with an JSContact Name object.
     *
     * @param name the JSContact Name object
     * @return a JSContactNameForRdapGetter object
     * @throws MissingFieldException if name is null
     */
    public static JSContactNameForRdapGetter of(Name name) throws MissingFieldException {
        if (name == null)
            throw new MissingFieldException("A Name object is required");
        return new JSContactNameForRdapGetter(name);
    }

    /**
     * Returns the full value of this JSContactNameForRdapGetter object
     *
     * @return the full name
     */
    public String full() { return name.getFull(); }

    /**
     * Returns the surname of this JSContactNameForRdapGetter object
     *
     * @return the surname
     */    public String surname() {
        return name.getSurname();
    }

    /**
     * Returns the given name of this JSContactNameForRdapGetter object
     *
     * @return the given name
     */
    public String given() {
        return name.getGiven();
    }

}

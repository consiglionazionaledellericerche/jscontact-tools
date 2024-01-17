package it.cnr.iit.jscontact.tools.rdap;

import it.cnr.iit.jscontact.tools.dto.Name;
import lombok.AllArgsConstructor;

@AllArgsConstructor
public class JSContactNameForRdapGetter {

    Name name;

    public static JSContactNameForRdapGetter of(Name name) throws MissingFieldException {
        if (name == null)
            throw new MissingFieldException("A Name object is required");
        return new JSContactNameForRdapGetter(name);
    }

    public String full() { return name.getFull(); }

    public String surname() {
        return name.getSurname();
    }

    public String given() {
        return name.getGiven();
    }

}

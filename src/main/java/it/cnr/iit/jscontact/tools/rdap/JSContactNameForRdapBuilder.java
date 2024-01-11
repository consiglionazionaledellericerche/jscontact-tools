package it.cnr.iit.jscontact.tools.rdap;

import it.cnr.iit.jscontact.tools.dto.Name;
import it.cnr.iit.jscontact.tools.dto.NameComponent;
import lombok.AllArgsConstructor;

@AllArgsConstructor
public class JSContactNameForRdapBuilder {

    private Name name;

    public static JSContactNameForRdapBuilder builder() {
        return new JSContactNameForRdapBuilder(Name.builder().build());
    }

    public JSContactNameForRdapBuilder full(String fullName) {
        this.name.setFull(fullName);
        return this;
    }

    public JSContactNameForRdapBuilder surname(String surname) {
        this.name.addComponent(NameComponent.surname(surname));
        return this;
    }

    public JSContactNameForRdapBuilder given(String given) {
        this.name.addComponent(NameComponent.given(given));
        return this;
    }

    public Name build() { return name; }

}

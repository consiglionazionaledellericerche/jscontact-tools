package it.cnr.iit.jscontact.tools.rdap;

import it.cnr.iit.jscontact.tools.dto.Address;
import it.cnr.iit.jscontact.tools.dto.AddressComponent;
import lombok.AllArgsConstructor;

@AllArgsConstructor
public class JSContactAddressForRdapBuilder {

    private Address address;

    public static JSContactAddressForRdapBuilder builder() {
        return new JSContactAddressForRdapBuilder(Address.builder().build());
    }

    public JSContactAddressForRdapBuilder full(String fullAddress) {
        this.address.setFull(fullAddress);
        return this;
    }

    public JSContactAddressForRdapBuilder cc(String cc) {
        this.address.setCountryCode(cc);
        return this;
    }

    public JSContactAddressForRdapBuilder country(String country) {
        this.address.addComponent(AddressComponent.country(country));
        return this;
    }

    public JSContactAddressForRdapBuilder sp(String sp) {
        this.address.addComponent(AddressComponent.region(sp));
        return this;
    }

    public JSContactAddressForRdapBuilder pc(String pc) {
        this.address.addComponent(AddressComponent.postcode(pc));
        return this;
    }

    public JSContactAddressForRdapBuilder city(String city) {
        this.address.addComponent(AddressComponent.locality(city));
        return this;
    }

    public JSContactAddressForRdapBuilder street(String street) {
        this.address.addComponent(AddressComponent.name(street));
        return this;
    }

    public Address build() { return address; }

}

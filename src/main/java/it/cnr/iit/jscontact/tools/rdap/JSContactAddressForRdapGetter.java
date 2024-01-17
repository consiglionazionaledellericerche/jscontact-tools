package it.cnr.iit.jscontact.tools.rdap;

import it.cnr.iit.jscontact.tools.dto.Address;
import it.cnr.iit.jscontact.tools.dto.AddressComponent;
import lombok.AllArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@AllArgsConstructor
public class JSContactAddressForRdapGetter {

    Address address;

    public static JSContactAddressForRdapGetter of(Address address) throws MissingFieldException {
        if (address == null)
            throw new MissingFieldException("An Address object is required");
        return new JSContactAddressForRdapGetter(address);
    }

    public String cc() {
        return address.getCountryCode();
    }

    public String country() {
        return address.getCountry();
    }

    public String sp() {
        return address.getRegion();
    }

    public String pc() {
        return address.getPostcode();
    }

    public String city() {
        return address.getLocality();
    }

    public String street() {
        return address.getStreetName();
    }

    public List<String> streets() {
        if (address.getStreetName() == null) return null;
        List<String> streets = new ArrayList<>();
        for(AddressComponent ac : address.getComponents()) {
            if (ac.isName())
                streets.add(ac.getValue());
        }

        return streets;
    }

}

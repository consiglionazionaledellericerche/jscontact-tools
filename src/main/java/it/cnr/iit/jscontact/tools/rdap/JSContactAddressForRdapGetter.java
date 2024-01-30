package it.cnr.iit.jscontact.tools.rdap;

import it.cnr.iit.jscontact.tools.dto.Address;
import it.cnr.iit.jscontact.tools.dto.AddressComponent;
import lombok.AllArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@AllArgsConstructor
public class JSContactAddressForRdapGetter {

    private Address address;

    /**
     * Returns a JSContactAddressForRdapGetter object initialized with an JSContact Address object.
     *
     * @param address the JSContact Address object
     * @return a JSContactAddressForRdapGetter object
     * @throws MissingFieldException if address is null
     */
    public static JSContactAddressForRdapGetter of(Address address) throws MissingFieldException {
        if (address == null)
            throw new MissingFieldException("An Address object is required");
        return new JSContactAddressForRdapGetter(address);
    }

    /**
     * Returns the country code of this JSContactAddressForRdapGetter object.
     *
     * @return the country code
     */
    public String cc() {
        return address.getCountryCode();
    }

    /**
     * Returns the country name of this JSContactAddressForRdapGetter object.
     *
     * @return the country name
     */
    public String country() {
        return address.getCountry();
    }

    /**
     * Returns the region name of this JSContactAddressForRdapGetter object.
     *
     * @return the region name
     */
    public String sp() {
        return address.getRegion();
    }

    /**
     * Returns the postcode of this JSContactAddressForRdapGetter object.
     *
     * @return the postcode
     */
    public String pc() {
        return address.getPostcode();
    }

    /**
     * Returns the locality name of this JSContactAddressForRdapGetter object.
     *
     * @return the locality name
     */
    public String city() {
        return address.getLocality();
    }

    /**
     * Returns the single street name of this JSContactAddressForRdapGetter object.
     *
     * @return the street name
     */
    public String street() {
        return address.getStreetName();
    }

    /**
     * Returns the collection of street details of this JSContactAddressForRdapGetter object
     *
     * @return the collection of street details
     */
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

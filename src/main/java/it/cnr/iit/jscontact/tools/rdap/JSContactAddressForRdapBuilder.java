package it.cnr.iit.jscontact.tools.rdap;

import it.cnr.iit.jscontact.tools.dto.Address;
import it.cnr.iit.jscontact.tools.dto.AddressComponent;
import lombok.AllArgsConstructor;

import java.util.List;

@AllArgsConstructor
public class JSContactAddressForRdapBuilder {

    private Address address;

    /**
     * Returns a JSContactAddressForRdapBuilder object used to build a JSContact Address object.
     *
     * @return the JSContactAddressForRdapBuilder object
     */
    public static JSContactAddressForRdapBuilder builder() {
        return new JSContactAddressForRdapBuilder(Address.builder().build());
    }

    /**
     * Sets the full address and returns this JSContactAddressForRdapBuilder object updated.
     *
     * @param fullAddress the full address value to be assigned
     * @return this JSContactAddressForRdapBuilder object updated
     */
    public JSContactAddressForRdapBuilder full(String fullAddress) {
        if (fullAddress == null) return this;
        this.address.setFull(fullAddress);
        return this;
    }

    /**
     * Sets the country code and returns this JSContactAddressForRdapBuilder object updated.
     *
     * @param cc the country code value to be assigned
     * @return this JSContactAddressForRdapBuilder object updated
     */
    public JSContactAddressForRdapBuilder cc(String cc) {
        if (cc == null) return this;
        this.address.setCountryCode(cc);
        return this;
    }

    /**
     * Sets the country name and returns this JSContactAddressForRdapBuilder object updated.
     *
     * @param country the country name to be assigned
     * @return this JSContactAddressForRdapBuilder object updated
     */
    public JSContactAddressForRdapBuilder country(String country) {
        if (country == null) return this;
        this.address.addComponent(AddressComponent.country(country));
        return this;
    }

    /**
     * Sets the region name and returns this JSContactAddressForRdapBuilder object updated.
     *
     * @param sp the region name to be assigned
     * @return this JSContactAddressForRdapBuilder object updated
     */
    public JSContactAddressForRdapBuilder sp(String sp) {
        if (sp == null) return this;
        this.address.addComponent(AddressComponent.region(sp));
        return this;
    }

    /**
     * Sets the postcode and returns this JSContactAddressForRdapBuilder object updated.
     *
     * @param pc the postcode to be assigned
     * @return this JSContactAddressForRdapBuilder object updated
     */
    public JSContactAddressForRdapBuilder pc(String pc) {
        if (pc == null) return this;
        this.address.addComponent(AddressComponent.postcode(pc));
        return this;
    }

    /**
     * Sets the locality name and returns this JSContactAddressForRdapBuilder object updated.
     *
     * @param city the locality name to be assigned
     * @return this JSContactAddressForRdapBuilder object updated
     */
    public JSContactAddressForRdapBuilder city(String city) {
        if (city == null) return this;
        this.address.addComponent(AddressComponent.locality(city));
        return this;
    }

    /**
     * Sets the single street name and returns this JSContactAddressForRdapBuilder object updated.
     *
     * @param street the single street name to be assigned
     * @return this JSContactAddressForRdapBuilder object updated
     */
    public JSContactAddressForRdapBuilder street(String street) {
        if (street == null) return this;
        this.address.addComponent(AddressComponent.name(street));
        return this;
    }

    /**
     * Sets the street details and returns this JSContactAddressForRdapBuilder object updated.
     *
     * @param streets the street details to be assigned
     * @return this JSContactAddressForRdapBuilder object updated
     */
    public JSContactAddressForRdapBuilder streets(List<String> streets) {
        if (streets == null) return this;
        for (String street : streets)
            this.address.addComponent(AddressComponent.name(street));
        return this;
    }

    /**
     * Returns the JSContact Address object with the given properties set
     *
     * @return the JSContact Address object
     */
    public Address build() { return address; }

}

package it.cnr.iit.jscontact.tools.dto.utils.builders;

import it.cnr.iit.jscontact.tools.dto.AddressComponent;
import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * Class for building the Address.components array.
 *
 * @author Mario Loffredo
 */
public class AddressComponentsBuilder {

    private List<AddressComponent> list;

    public AddressComponentsBuilder() {
        list = new ArrayList<>();
    }

    /**
     * Adds a name address component.
     * @param value the name address component value
     * @return the address components builder updated by adding the name address component
     */
    public AddressComponentsBuilder name(String value) {
        if (StringUtils.isEmpty(value)) return this;
        list.add(AddressComponent.name(value));
        return this;
    }

    /**
     * Adds a name address component with phonetic.
     * @param value the name address component value
     * @param phonetic the name address component phonetic value
     * @return the address components builder updated by adding the name address component
     */
    public AddressComponentsBuilder name(String value, String phonetic) {
        if (StringUtils.isEmpty(value)) return this;
        list.add(AddressComponent.name(value,phonetic));
        return this;
    }

    /**
     * Adds a locality address component.
     * @param value the locality address component value
     * @return the address components builder updated by adding the locality address component
     */
    public AddressComponentsBuilder locality(String value) {
        if (StringUtils.isEmpty(value)) return this;
        list.add(AddressComponent.locality(value));
        return this;
    }

    /**
     * Adds a locality address component with phonetic.
     * @param value the locality address component value
     * @param phonetic the locality address component phonetic value
     * @return the address components builder updated by adding the locality address component
     */
    public AddressComponentsBuilder locality(String value, String phonetic) {
        if (StringUtils.isEmpty(value)) return this;
        list.add(AddressComponent.locality(value,phonetic));
        return this;
    }

    /**
     * Adds a region address component.
     * @param value the region address component value
     * @return the address components builder updated by adding the region address component
     */
    public AddressComponentsBuilder region(String value) {
        if (StringUtils.isEmpty(value)) return this;
        list.add(AddressComponent.region(value));
        return this;
    }

    /**
     * Adds a region address component with phonetic.
     * @param value the region address component value
     * @param phonetic the region address component phonetic value
     * @return the address components builder updated by adding the region address component
     */
    public AddressComponentsBuilder region(String value, String phonetic) {
        if (StringUtils.isEmpty(value)) return this;
        list.add(AddressComponent.region(value,phonetic));
        return this;
    }

    /**
     * Adds a country address component.
     * @param value the country address component value
     * @return the address components builder updated by adding the country address component
     */
    public AddressComponentsBuilder country(String value) {
        if (StringUtils.isEmpty(value)) return this;
        list.add(AddressComponent.country(value));
        return this;
    }

    /**
     * Adds a country address component with phonetic.
     * @param value the country address component value
     * @param phonetic the country address component phonetic value
     * @return the address components builder updated by adding the country address component
     */
    public AddressComponentsBuilder country(String value, String phonetic) {
        if (StringUtils.isEmpty(value)) return this;
        list.add(AddressComponent.country(value,phonetic));
        return this;
    }

    /**
     * Adds a subdistrict address component.
     * @param value the subdistrict address component value
     * @return the address components builder updated by adding the subdistrict address component
     */
    public AddressComponentsBuilder subdistrict(String value) {
        if (StringUtils.isEmpty(value)) return this;
        list.add(AddressComponent.subdistrict(value));
        return this;
    }

    /**
     * Adds a subdistrict address component with phonetic.
     * @param value the subdistrict address component value
     * @param phonetic the subdistrict address component phonetic value
     * @return the address components builder updated by adding the subdistrict address component
     */
    public AddressComponentsBuilder subdistrict(String value, String phonetic) {
        if (StringUtils.isEmpty(value)) return this;
        list.add(AddressComponent.subdistrict(value,phonetic));
        return this;
    }

    /**
     * Adds a district address component.
     * @param value the district address component value
     * @return the address components builder updated by adding the district address component
     */
    public AddressComponentsBuilder district(String value) {
        if (StringUtils.isEmpty(value)) return this;
        list.add(AddressComponent.district(value));
        return this;
    }

    /**
     * Adds a district address component with phonetic.
     * @param value the district address component value
     * @param phonetic the district address component phonetic value
     * @return the address components builder updated by adding the district address component
     */
    public AddressComponentsBuilder district(String value, String phonetic) {
        if (StringUtils.isEmpty(value)) return this;
        list.add(AddressComponent.district(value,phonetic));
        return this;
    }

    /**
     * Adds a block address component.
     * @param value the block address component value
     * @return the address components builder updated by adding the block address component
     */
    public AddressComponentsBuilder block(String value) {
        if (StringUtils.isEmpty(value)) return this;
        list.add(AddressComponent.block(value));
        return this;
    }

    /**
     * Adds a block address component with phonetic.
     * @param value the block address component value
     * @param phonetic the block address component phonetic value
     * @return the address components builder updated by adding the block address component
     */
    public AddressComponentsBuilder block(String value, String phonetic) {
        if (StringUtils.isEmpty(value)) return this;
        list.add(AddressComponent.block(value,phonetic));
        return this;
    }

    /**
     * Adds an ext address component.
     * @param extValue the ext address component
     * @param value the ext address component name value
     * @return the address components builder updated by adding the ext address component
     */
    public AddressComponentsBuilder ext(String extValue, String value) {
        if (StringUtils.isEmpty(value)) return this;
        list.add(AddressComponent.ext(extValue, value));
        return this;
    }

    /**
     * Adds an ext address component with phonetic.
     * @param extValue the ext address component
     * @param value the ext address component value
     * @param phonetic the phonetic value
     * @return the address components builder updated by adding the ext address component
     */
    public AddressComponentsBuilder ext(String extValue, String value, String phonetic) {
        if (StringUtils.isEmpty(value)) return this;
        list.add(AddressComponent.ext(extValue, value,phonetic));
        return this;
    }

    /**
     * Adds a building address component.
     * @param value the building address component value
     * @return the address components builder updated by adding the building address component
     */
    public AddressComponentsBuilder building(String value) {
        if (StringUtils.isEmpty(value)) return this;
        list.add(AddressComponent.building(value));
        return this;
    }


    /**
     * Adds a building address component with phonetic.
     * @param value the building address component value
     * @param phonetic the building address component phonetic value
     * @return the address components builder updated by adding the building address component
     */
    public AddressComponentsBuilder building(String value, String phonetic) {
        if (StringUtils.isEmpty(value)) return this;
        list.add(AddressComponent.building(value,phonetic));
        return this;
    }

    /**
     * Adds a landmark address component.
     * @param value the landmark address component value
     * @return the address components builder updated by adding the landmark address component
     */
    public AddressComponentsBuilder landmark(String value) {
        if (StringUtils.isEmpty(value)) return this;
        list.add(AddressComponent.landmark(value));
        return this;
    }

    /**
     * Adds a landmark address component with phonetic.
     * @param value the landmark address component value
     * @param phonetic the landmark address component phonetic value
     * @return the address components builder updated by adding the landmark address component
     */
    public AddressComponentsBuilder landmark(String value, String phonetic) {
        if (StringUtils.isEmpty(value)) return this;
        list.add(AddressComponent.landmark(value,phonetic));
        return this;
    }

    /**
     * Adds a number address component.
     * @param value the number address component value
     * @return the address components builder updated by adding the number address component
     */
    public AddressComponentsBuilder number(String value) {
        if (StringUtils.isEmpty(value)) return this;
        list.add(AddressComponent.number(value));
        return this;
    }

    /**
     * Adds a postcode address component.
     * @param value the postcode address component value
     * @return the address components builder updated by adding the postcode address component
     */
    public AddressComponentsBuilder postcode(String value) {
        if (StringUtils.isEmpty(value)) return this;
        list.add(AddressComponent.postcode(value));
        return this;
    }

    /**
     * Adds a postOfficeBox address component.
     * @param value the postOfficeBox address component value
     * @return the address components builder updated by adding the postOfficeBox address component
     */
    public AddressComponentsBuilder postOfficeBox(String value) {
        if (StringUtils.isEmpty(value)) return this;
        list.add(AddressComponent.postOfficeBox(value));
        return this;
    }

    /**
     * Adds a floor address component.
     * @param value the floor address component value
     * @return the address components builder updated by adding the floor address component
     */
    public AddressComponentsBuilder floor(String value) {
        if (StringUtils.isEmpty(value)) return this;
        list.add(AddressComponent.floor(value));
        return this;
    }

    /**
     * Adds an apartment address component.
     * @param value the apartment address component value
     * @return the address components builder updated by adding the apartment address component
     */
    public AddressComponentsBuilder apartment(String value) {
        if (StringUtils.isEmpty(value)) return this;
        list.add(AddressComponent.apartment(value));
        return this;
    }

    /**
     * Adds a room address component.
     * @param value the room address component value
     * @return the address components builder updated by adding the room address component
     */
    public AddressComponentsBuilder room(String value) {
        if (StringUtils.isEmpty(value)) return this;
        list.add(AddressComponent.room(value));
        return this;
    }

    /**
     * Adds a direction address component.
     * @param value the direction address component value
     * @return the address components builder updated by adding the direction address component
     */
    public AddressComponentsBuilder direction(String value) {
        if (StringUtils.isEmpty(value)) return this;
        list.add(AddressComponent.direction(value));
        return this;
    }

    /**
     * Adds a separator address component.
     * @param value the separator address component value
     * @return the address components builder updated by adding the separator address component
     */
    public AddressComponentsBuilder separator(String value) {
        if (value == null) return this;
        list.add(AddressComponent.separator(value));
        return this;
    }

    /**
     * Returns the address components array.
     * @return the address components array
     */
    public AddressComponent[] build() {
        return list.toArray(new AddressComponent[0]);
    }

}

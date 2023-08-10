package it.cnr.iit.jscontact.tools.dto.utils.builders;

import it.cnr.iit.jscontact.tools.dto.AddressComponent;
import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.List;

public class AddressComponentsBuilder {

    private List<AddressComponent> list;

    public AddressComponentsBuilder() {
        list = new ArrayList<>();
    }

    public AddressComponentsBuilder name(String value) {
        if (StringUtils.isEmpty(value)) return this;
        list.add(AddressComponent.name(value));
        return this;
    }

    public AddressComponentsBuilder name(String value, String phonetic) {
        if (StringUtils.isEmpty(value)) return this;
        list.add(AddressComponent.name(value,phonetic));
        return this;
    }

    public AddressComponentsBuilder locality(String value) {
        if (StringUtils.isEmpty(value)) return this;
        list.add(AddressComponent.locality(value));
        return this;
    }

    public AddressComponentsBuilder locality(String value, String phonetic) {
        if (StringUtils.isEmpty(value)) return this;
        list.add(AddressComponent.locality(value,phonetic));
        return this;
    }

    public AddressComponentsBuilder region(String value) {
        if (StringUtils.isEmpty(value)) return this;
        list.add(AddressComponent.region(value));
        return this;
    }

    public AddressComponentsBuilder region(String value, String phonetic) {
        if (StringUtils.isEmpty(value)) return this;
        list.add(AddressComponent.region(value,phonetic));
        return this;
    }

    public AddressComponentsBuilder country(String value) {
        if (StringUtils.isEmpty(value)) return this;
        list.add(AddressComponent.country(value));
        return this;
    }

    public AddressComponentsBuilder country(String value, String phonetic) {
        if (StringUtils.isEmpty(value)) return this;
        list.add(AddressComponent.country(value,phonetic));
        return this;
    }

    public AddressComponentsBuilder subdistrict(String value) {
        if (StringUtils.isEmpty(value)) return this;
        list.add(AddressComponent.subdistrict(value));
        return this;
    }

    public AddressComponentsBuilder subdistrict(String value, String phonetic) {
        if (StringUtils.isEmpty(value)) return this;
        list.add(AddressComponent.subdistrict(value,phonetic));
        return this;
    }

    public AddressComponentsBuilder district(String value) {
        if (StringUtils.isEmpty(value)) return this;
        list.add(AddressComponent.district(value));
        return this;
    }

    public AddressComponentsBuilder district(String value, String phonetic) {
        if (StringUtils.isEmpty(value)) return this;
        list.add(AddressComponent.district(value,phonetic));
        return this;
    }

    public AddressComponentsBuilder block(String value) {
        if (StringUtils.isEmpty(value)) return this;
        list.add(AddressComponent.block(value));
        return this;
    }

    public AddressComponentsBuilder block(String value, String phonetic) {
        if (StringUtils.isEmpty(value)) return this;
        list.add(AddressComponent.block(value,phonetic));
        return this;
    }

    public AddressComponentsBuilder ext(String extValue, String value) {
        if (StringUtils.isEmpty(value)) return this;
        list.add(AddressComponent.ext(extValue, value));
        return this;
    }

    public AddressComponentsBuilder ext(String extValue, String value, String phonetic) {
        if (StringUtils.isEmpty(value)) return this;
        list.add(AddressComponent.ext(extValue, value,phonetic));
        return this;
    }

    public AddressComponentsBuilder building(String value) {
        if (StringUtils.isEmpty(value)) return this;
        list.add(AddressComponent.building(value));
        return this;
    }

    public AddressComponentsBuilder building(String value, String phonetic) {
        if (StringUtils.isEmpty(value)) return this;
        list.add(AddressComponent.building(value,phonetic));
        return this;
    }

    public AddressComponentsBuilder landmark(String value) {
        if (StringUtils.isEmpty(value)) return this;
        list.add(AddressComponent.landmark(value));
        return this;
    }

    public AddressComponentsBuilder landmark(String value, String phonetic) {
        if (StringUtils.isEmpty(value)) return this;
        list.add(AddressComponent.landmark(value,phonetic));
        return this;
    }

    public AddressComponentsBuilder number(String value) {
        if (StringUtils.isEmpty(value)) return this;
        list.add(AddressComponent.number(value));
        return this;
    }

    public AddressComponentsBuilder postcode(String value) {
        if (StringUtils.isEmpty(value)) return this;
        list.add(AddressComponent.postcode(value));
        return this;
    }

    public AddressComponentsBuilder postOfficeBox(String value) {
        if (StringUtils.isEmpty(value)) return this;
        list.add(AddressComponent.postOfficeBox(value));
        return this;
    }

    public AddressComponentsBuilder floor(String value) {
        if (StringUtils.isEmpty(value)) return this;
        list.add(AddressComponent.floor(value));
        return this;
    }

    public AddressComponentsBuilder apartment(String value) {
        if (StringUtils.isEmpty(value)) return this;
        list.add(AddressComponent.apartment(value));
        return this;
    }

    public AddressComponentsBuilder room(String value) {
        if (StringUtils.isEmpty(value)) return this;
        list.add(AddressComponent.room(value));
        return this;
    }

    public AddressComponentsBuilder direction(String value) {
        if (StringUtils.isEmpty(value)) return this;
        list.add(AddressComponent.direction(value));
        return this;
    }

    public AddressComponentsBuilder separator(String value) {
        if (value == null) return this;
        list.add(AddressComponent.separator(value));
        return this;
    }

    public AddressComponent[] build() {
        return list.toArray(new AddressComponent[0]);
    }

}

package it.cnr.iit.jscontact.tools.dto.utils.builders;

import it.cnr.iit.jscontact.tools.dto.NameComponent;
import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.List;

public class NameComponentsBuilder {

    private List<NameComponent> list;

    public NameComponentsBuilder() {
        list = new ArrayList<>();
    }

    public NameComponentsBuilder given(String value) {
        if (StringUtils.isEmpty(value)) return this;
        list.add(NameComponent.given(value));
        return this;
    }

    public NameComponentsBuilder given(String value, String phonetic) {
        if (StringUtils.isEmpty(value)) return this;
        list.add(NameComponent.given(value,phonetic));
        return this;
    }
    
    public NameComponentsBuilder surname(String value) {
        if (StringUtils.isEmpty(value)) return this;
        list.add(NameComponent.surname(value));
        return this;
    }

    public NameComponentsBuilder surname(String value, String phonetic) {
        if (StringUtils.isEmpty(value)) return this;
        list.add(NameComponent.surname(value,phonetic));
        return this;
    }

    public NameComponentsBuilder given2(String value) {
        if (StringUtils.isEmpty(value)) return this;
        list.add(NameComponent.surname(value));
        return this;
    }

    public NameComponentsBuilder given2(String value, String phonetic) {
        if (StringUtils.isEmpty(value)) return this;
        list.add(NameComponent.given2(value,phonetic));
        return this;
    }

    public NameComponentsBuilder title(String value) {
        if (StringUtils.isEmpty(value)) return this;
        list.add(NameComponent.title(value));
        return this;
    }

    public NameComponentsBuilder title(String value, String phonetic) {
        if (StringUtils.isEmpty(value)) return this;
        list.add(NameComponent.title(value,phonetic));
        return this;
    }

    public NameComponentsBuilder credential(String value) {
        if (StringUtils.isEmpty(value)) return this;
        list.add(NameComponent.credential(value));
        return this;
    }

    public NameComponentsBuilder credential(String value, String phonetic) {
        if (StringUtils.isEmpty(value)) return this;
        list.add(NameComponent.credential(value,phonetic));
        return this;
    }

    public NameComponentsBuilder generation(String value) {
        if (StringUtils.isEmpty(value)) return this;
        list.add(NameComponent.generation(value));
        return this;
    }

    public NameComponentsBuilder generation(String value, String phonetic) {
        if (StringUtils.isEmpty(value)) return this;
        list.add(NameComponent.generation(value,phonetic));
        return this;
    }

    public NameComponentsBuilder separator(String value) {
        if (value == null) return this;
        list.add(NameComponent.separator(value));
        return this;
    }
    
    public NameComponent[] build() {
        return list.toArray(new NameComponent[0]);
    }

}

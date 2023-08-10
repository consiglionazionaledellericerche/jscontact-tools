package it.cnr.iit.jscontact.tools.dto.utils.builders;

import it.cnr.iit.jscontact.tools.dto.NameComponent;
import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * Class for building the Name.components array.
 *
 * @author Mario Loffredo
 */
public class NameComponentsBuilder {

    private List<NameComponent> list;

    public NameComponentsBuilder() {
        list = new ArrayList<>();
    }

    /**
     * Adds a given name component.
     * @param value the given name component value
     * @return the name components builder updated by adding the given name component
     */
    public NameComponentsBuilder given(String value) {
        if (StringUtils.isEmpty(value)) return this;
        list.add(NameComponent.given(value));
        return this;
    }

    /**
     * Adds a given name component with phonetic.
     * @param value the given name component value
     * @param phonetic the given name component phonetic value
     * @return the name components builder updated by adding the given name component
     */
    public NameComponentsBuilder given(String value, String phonetic) {
        if (StringUtils.isEmpty(value)) return this;
        list.add(NameComponent.given(value,phonetic));
        return this;
    }

    /**
     * Adds a surname name component.
     * @param value the surname name component value
     * @return the name components builder updated by adding the surname name component
     */
    public NameComponentsBuilder surname(String value) {
        if (StringUtils.isEmpty(value)) return this;
        list.add(NameComponent.surname(value));
        return this;
    }

    /**
     * Adds a surname name component with phonetic.
     * @param value the surname name component value
     * @param phonetic the surname name component phonetic value
     * @return the name components builder updated by adding the surname name component
     */
    public NameComponentsBuilder surname(String value, String phonetic) {
        if (StringUtils.isEmpty(value)) return this;
        list.add(NameComponent.surname(value,phonetic));
        return this;
    }

    /**
     * Adds a given2 name component.
     * @param value the given2 name component value
     * @return the name components builder updated by adding the given2 name component
     */
    public NameComponentsBuilder given2(String value) {
        if (StringUtils.isEmpty(value)) return this;
        list.add(NameComponent.surname(value));
        return this;
    }

    /**
     * Adds a given2 name component with phonetic.
     * @param value the given2 name component value
     * @param phonetic the given2 name component phonetic value
     * @return the name components builder updated by adding the given2 name component
     */
    public NameComponentsBuilder given2(String value, String phonetic) {
        if (StringUtils.isEmpty(value)) return this;
        list.add(NameComponent.given2(value,phonetic));
        return this;
    }

    /**
     * Adds a surname2 name component.
     * @param value the surname2 name component value
     * @return the name components builder updated by adding the surname2 name component
     */
    public NameComponentsBuilder surname2(String value) {
        if (StringUtils.isEmpty(value)) return this;
        list.add(NameComponent.surname2(value));
        return this;
    }

    /**
     * Adds a surname2 name component with phonetic.
     * @param value the surname2 name component value
     * @param phonetic the surname2 name component phonetic value
     * @return the name components builder updated by adding the surname2 name component
     */
    public NameComponentsBuilder surname2(String value, String phonetic) {
        if (StringUtils.isEmpty(value)) return this;
        list.add(NameComponent.surname2(value,phonetic));
        return this;
    }

    /**
     * Adds a title name component.
     * @param value the title name component value
     * @return the name components builder updated by adding the title name component
     */
    public NameComponentsBuilder title(String value) {
        if (StringUtils.isEmpty(value)) return this;
        list.add(NameComponent.title(value));
        return this;
    }

    /**
     * Adds a title name component with phonetic.
     * @param value the title name component value
     * @param phonetic the title name component phonetic value
     * @return the name components builder updated by adding the title name component
     */
    public NameComponentsBuilder title(String value, String phonetic) {
        if (StringUtils.isEmpty(value)) return this;
        list.add(NameComponent.title(value,phonetic));
        return this;
    }

    /**
     * Adds a credential name component.
     * @param value the credential name component value
     * @return the name components builder updated by adding the credential name component
     */
    public NameComponentsBuilder credential(String value) {
        if (StringUtils.isEmpty(value)) return this;
        list.add(NameComponent.credential(value));
        return this;
    }

    /**
     * Adds a credential name component with phonetic.
     * @param value the credential name component value
     * @param phonetic the credential name component phonetic value
     * @return the name components builder updated by adding the credential name component
     */
    public NameComponentsBuilder credential(String value, String phonetic) {
        if (StringUtils.isEmpty(value)) return this;
        list.add(NameComponent.credential(value,phonetic));
        return this;
    }

    /**
     * Adds a generation name component.
     * @param value the generation name component value
     * @return the name components builder updated by adding the generation name component
     */
    public NameComponentsBuilder generation(String value) {
        if (StringUtils.isEmpty(value)) return this;
        list.add(NameComponent.generation(value));
        return this;
    }

    /**
     * Adds a generation name component with phonetic.
     * @param value the generation name component value
     * @param phonetic the generation name component phonetic value
     * @return the name components builder updated by adding the generation name component
     */
    public NameComponentsBuilder generation(String value, String phonetic) {
        if (StringUtils.isEmpty(value)) return this;
        list.add(NameComponent.generation(value,phonetic));
        return this;
    }

    /**
     * Adds an ext name component.
     * @param extValue the ext name component
     * @param value the ext name component value
     * @return the name components builder updated by adding the ext name component
     */
    public NameComponentsBuilder ext(String extValue, String value) {
        if (StringUtils.isEmpty(value)) return this;
        list.add(NameComponent.ext(extValue, value));
        return this;
    }

    /**
     * Adds an ext name component with phonetic.
     * @param extValue the ext name component
     * @param value the ext name component value
     * @param phonetic the ext name component phonetic value
     * @return the name components builder updated by adding the ext name component
     */
    public NameComponentsBuilder ext(String extValue, String value, String phonetic) {
        if (StringUtils.isEmpty(value)) return this;
        list.add(NameComponent.ext(extValue, value,phonetic));
        return this;
    }

    /**
     * Adds a separator name component.
     * @param value the separator name component value
     * @return the name components builder updated by adding the separator name component
     */
    public NameComponentsBuilder separator(String value) {
        if (value == null) return this;
        list.add(NameComponent.separator(value));
        return this;
    }

    /**
     * Returns the name components array.
     * @return the name components array
     */
    public NameComponent[] build() {
        return list.toArray(new NameComponent[0]);
    }

}

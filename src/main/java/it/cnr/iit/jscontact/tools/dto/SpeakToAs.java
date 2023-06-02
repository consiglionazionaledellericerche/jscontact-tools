package it.cnr.iit.jscontact.tools.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import it.cnr.iit.jscontact.tools.constraints.IdMapConstraint;
import it.cnr.iit.jscontact.tools.constraints.NotNullAnyConstraint;
import lombok.*;
import lombok.experimental.SuperBuilder;

import javax.validation.Valid;
import javax.validation.constraints.Pattern;
import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

/**
 * Class mapping the SpeakToAs type as defined in section 2.2.5 of [draft-ietf-calext-jscontact].
 *
 * @author Mario Loffredo
 * @see <a href="https://datatracker.ietf.org/doc/draft-ietf-calext-jscontact#section-2.2.5">draft-ietf-calext-jscontact</a>
 */
@JsonPropertyOrder({"@type","grammaticalGender","pronouns"})
@NotNullAnyConstraint(fieldNames={"grammaticalGender","pronouns"}, message = "at least one not null member other than @type is missing in SpeakToAs")
@JsonInclude(JsonInclude.Include.NON_NULL)
@SuperBuilder
@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper = false)
public class SpeakToAs extends AbstractJSContactType implements Serializable {

    @Pattern(regexp = "SpeakToAs", message="invalid @type value in SpeakToAs")
    @JsonProperty("@type")
    @Builder.Default
    String _type = "SpeakToAs";

    GrammaticalGenderType grammaticalGender;

    @JsonPropertyOrder(alphabetic = true)
    @Valid
    @IdMapConstraint(message = "invalid Id in Map<Id,Pronouns>")
    Map<String,Pronouns> pronouns;

    private boolean isRfc(GrammaticalGenderType type) { return grammaticalGender != null && grammaticalGender == type; }

    /**
     * Tests if the grammatical gender is masculine. See vCard 4.0 GENDER property as defined in section 6.2.7 of [RFC6350].
     *
     * @return true if the grammatical gender is masculine, false otherwise
     * @see <a href="https://datatracker.ietf.org/doc/html/rfc6350#section-6.2.7">RFC6350</a>
     */
    @JsonIgnore
    public boolean isMasculine() { return isRfc(GrammaticalGenderType.MASCULINE); }

    /**
     * Tests if the grammatical gender is feminine. See vCard 4.0 GENDER property as defined in section 6.2.7 of [RFC6350].
     *
     * @return true if the grammatical gender is feminine, false otherwise
     * @see <a href="https://datatracker.ietf.org/doc/html/rfc6350#section-6.2.7">RFC6350</a>
     */
    @JsonIgnore
    public boolean isFeminine() { return isRfc(GrammaticalGenderType.FEMININE); }

    /**
     * Tests if the grammatical gender is animate. See vCard 4.0 GENDER property as defined in section 6.2.7 of [RFC6350].
     *
     * @return true if the grammatical gender is animate, false otherwise
     * @see <a href="https://datatracker.ietf.org/doc/html/rfc6350#section-6.2.7">RFC6350</a>
     */
    @JsonIgnore
    public boolean isAnimate() { return isRfc(GrammaticalGenderType.ANIMATE); }

    /**
     * Tests if the grammatical gender is inanimate. See vCard 4.0 GENDER property as defined in section 6.2.7 of [RFC6350].
     *
     * @return true if the grammatical gender is inanimate, false otherwise
     * @see <a href="https://datatracker.ietf.org/doc/html/rfc6350#section-6.2.7">RFC6350</a>
     */
    @JsonIgnore
    public boolean isInanimate() { return isRfc(GrammaticalGenderType.INANIMATE); }

    /**
     * Tests if the grammatical gender is common. See vCard 4.0 GENDER property as defined in section 6.2.7 of [RFC6350].
     *
     * @return true if the grammatical gender is common, false otherwise
     * @see <a href="https://datatracker.ietf.org/doc/html/rfc6350#section-6.2.7">RFC6350</a>
     */
    @JsonIgnore
    public boolean isCommon() { return isRfc(GrammaticalGenderType.COMMON); }

    private static SpeakToAs gender(GrammaticalGenderType type) { return SpeakToAs.builder().grammaticalGender(type).build(); }

    /**
     * Returns a "masculine" gender.
     *
     * @return a "masculine" gender
     */
    public static SpeakToAs masculine() { return gender(GrammaticalGenderType.MASCULINE);}
    /**
     * Returns a "feminine" gender.
     *
     * @return a "feminine" gender
     */
    public static SpeakToAs feminine() { return gender(GrammaticalGenderType.FEMININE);}
    /**
     * Returns a "common" gender.
     *
     * @return a "common" gender
     */
    public static SpeakToAs common() { return gender(GrammaticalGenderType.COMMON);}
    /**
     * Returns a "animate" gender.
     *
     * @return a "animate" gender
     */
    public static SpeakToAs animate() { return gender(GrammaticalGenderType.ANIMATE);}
    /**
     * Returns a "animate" gender.
     *
     * @return a "animate" gender
     */
    public static SpeakToAs inanimate() { return gender(GrammaticalGenderType.INANIMATE);}


    /**
     * Adds a pronouns object to this object.
     *
     * @param id the pronouns object identifier
     * @param pronouns the pronouns object
     */
    public void addPronouns(String id, Pronouns pronouns) {

        if (this.pronouns == null)
            this.pronouns = new HashMap<>();

        this.pronouns.putIfAbsent(id, pronouns);
    }
}

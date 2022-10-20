package it.cnr.iit.jscontact.tools.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import it.cnr.iit.jscontact.tools.constraints.IdMapConstraint;
import it.cnr.iit.jscontact.tools.constraints.NotNullAnyConstraint;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

/**
 * Class mapping the SpeakToAs type as defined in section 2.2.6 of [draft-ietf-calext-jscontact].
 *
 * @see <a href="https://datatracker.ietf.org/doc/draft-ietf-calext-jscontact#section-2.2.6">draft-ietf-calext-jscontact</a>
 * @author Mario Loffredo
 */
@JsonPropertyOrder({"@type","grammaticalGender","pronouns"})
@NotNullAnyConstraint(fieldNames={"grammaticalGender","pronouns"}, message = "at least one not null member other than @type is missing in SpeakToAs")
@JsonInclude(JsonInclude.Include.NON_NULL)
@SuperBuilder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class SpeakToAs extends GroupableObject implements Serializable {

    @NotNull
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
     * Tests if the grammatical gender is male. See vCard 4.0 GENDER property as defined in section 6.2.7 of [RFC6350].
     *
     * @return true if the grammatical gender is male, false otherwise
     * @see <a href="https://datatracker.ietf.org/doc/html/rfc6350#section-6.2.7">RFC6350</a>
     */
    @JsonIgnore
    public boolean isMale() { return isRfc(GrammaticalGenderType.MALE); }

    /**
     * Tests if the grammatical gender is female. See vCard 4.0 GENDER property as defined in section 6.2.7 of [RFC6350].
     *
     * @return true if the grammatical gender is female, false otherwise
     * @see <a href="https://datatracker.ietf.org/doc/html/rfc6350#section-6.2.7">RFC6350</a>
     */
    @JsonIgnore
    public boolean isFemale() { return isRfc(GrammaticalGenderType.FEMALE); }

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
     * Tests if the grammatical gender is neuter. See vCard 4.0 GENDER property as defined in section 6.2.7 of [RFC6350].
     *
     * @return true if the grammatical gender is neuter, false otherwise
     * @see <a href="https://datatracker.ietf.org/doc/html/rfc6350#section-6.2.7">RFC6350</a>
     */
    @JsonIgnore
    public boolean isNeuter() { return isRfc(GrammaticalGenderType.NEUTER); }

    private static SpeakToAs gender(GrammaticalGenderType type) { return SpeakToAs.builder().grammaticalGender(type).build(); }

    /**
     * Returns a "male" gender.
     *
     * @return a "mail" gender
     */
    public static SpeakToAs male() { return gender(GrammaticalGenderType.MALE);}
    /**
     * Returns a "female" gender.
     *
     * @return a "female" gender
     */
    public static SpeakToAs female() { return gender(GrammaticalGenderType.FEMALE);}
    /**
     * Returns a "neuter" gender.
     *
     * @return a "neuter" gender
     */
    public static SpeakToAs neuter() { return gender(GrammaticalGenderType.NEUTER);}
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
            this.pronouns = new HashMap<String, Pronouns>();

        this.pronouns.putIfAbsent(id, pronouns);
    }
}

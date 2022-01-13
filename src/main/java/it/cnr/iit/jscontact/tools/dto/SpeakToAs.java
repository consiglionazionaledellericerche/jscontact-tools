package it.cnr.iit.jscontact.tools.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import it.cnr.iit.jscontact.tools.constraints.NotNullAnyConstraint;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import java.io.Serializable;

/**
 * Class mapping the SpeakToAs type as defined in section 2.2.6 of [draft-ietf-jmap-jscontact].
 *
 * @see <a href="https://datatracker.ietf.org/doc/draft-ietf-jmap-jscontact#section-2.2.6">draft-ietf-jmap-jscontact</a>
 * @author Mario Loffredo
 */
@JsonPropertyOrder({"@type","grammaticalGender","pronouns"})
@NotNullAnyConstraint(fieldNames={"grammaticalGender","pronouns"}, message = "at least one not null member other than @type is missing in SpeakToAs")
@JsonInclude(JsonInclude.Include.NON_NULL)
@Builder
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

    String pronouns;

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

}

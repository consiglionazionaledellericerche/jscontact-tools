package it.cnr.iit.jscontact.tools.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import it.cnr.iit.jscontact.tools.dto.annotations.ContainsExtensibleEnum;
import it.cnr.iit.jscontact.tools.dto.deserializers.TitleKindDeserializer;
import it.cnr.iit.jscontact.tools.dto.interfaces.HasKind;
import it.cnr.iit.jscontact.tools.dto.interfaces.HasOptionalKind;
import it.cnr.iit.jscontact.tools.dto.interfaces.IdMapValue;
import it.cnr.iit.jscontact.tools.dto.interfaces.IsIANAType;
import lombok.*;
import lombok.experimental.SuperBuilder;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import java.io.Serializable;


/**
 * Class mapping the Title type as defined in section 2.2.4 of [draft-ietf-calext-jscontact].
 *
 * @author Mario Loffredo
 * @see <a href="https://datatracker.ietf.org/doc/draft-ietf-calext-jscontact#section-2.2.4">draft-ietf-calext-jscontact</a>
 */
@JsonPropertyOrder({"@type","name","kind","organizationId"})
@JsonInclude(JsonInclude.Include.NON_NULL)
@SuperBuilder
@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper = false)
public class Title extends AbstractJSContactType implements HasKind, HasOptionalKind, IdMapValue, IsIANAType, Serializable {

    @Pattern(regexp = "Title", message="invalid @type value in Title")
    @JsonProperty("@type")
    @Builder.Default
    String _type = "Title";

    @NotNull(message = "name is missing in Title")
    @NonNull
    String name;

    @JsonDeserialize(using = TitleKindDeserializer.class)
    @ContainsExtensibleEnum(enumClass = TitleEnum.class, getMethod = "getKind")
    TitleKind kind;

    String organizationId;

}

/*
 *    Copyright (C) 2020  Consiglio Nazionale delle Ricerche
 *     This program is free software: you can redistribute it and/or modify
 *     it under the terms of the GNU Affero General Public License as
 *     published by the Free Software Foundation, either version 3 of the
 *     License, or (at your option) any later version.
 *
 *     This program is distributed in the hope that it will be useful,
 *     but WITHOUT ANY WARRANTY; without even the implied warranty of
 *     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *     GNU Affero General Public License for more details.
 *
 *     You should have received a copy of the GNU Affero General Public License
 *     along with this program.  If not, see <https://www.gnu.org/licenses/>.
 */
package it.cnr.iit.jscontact.tools.constraints.validators;

import com.fasterxml.jackson.core.JsonPointer;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import it.cnr.iit.jscontact.tools.constraints.LocalizationsConstraint;
import it.cnr.iit.jscontact.tools.dto.Card;
import it.cnr.iit.jscontact.tools.dto.utils.ClassUtils;
import it.cnr.iit.jscontact.tools.dto.utils.JsonPointerUtils;
import sun.util.locale.LanguageTag;
import sun.util.locale.ParseStatus;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.Map;

public class LocalizationsValidator implements ConstraintValidator<LocalizationsConstraint, Card> {

    public void initialize(LocalizationsConstraint constraintAnnotation) {
    }


    public boolean isValid(Card card, ConstraintValidatorContext context) {

        if (card.getLocalizations() == null)
            return true;

        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode root = objectMapper.valueToTree(card);

        for (String language : card.getLocalizations().keySet()) {
            ParseStatus parseStatus = new ParseStatus();
            LanguageTag.parse(language, parseStatus);
            if (parseStatus.getErrorMessage() != null) {
                context.buildConstraintViolationWithTemplate("invalid language tag in localizations").addConstraintViolation();
                return false;
            }
        }

        for (Map<String,JsonNode> localizationsPerlanguage : card.getLocalizations().values()){

            for(Map.Entry<String,JsonNode> localization : localizationsPerlanguage.entrySet()) {

                JsonPointer jsonPointer;
                try {
                    jsonPointer = JsonPointer.compile(JsonPointerUtils.toAbsolute(localization.getKey()));
                } catch (Exception e) {
                    context.buildConstraintViolationWithTemplate("invalid JSON pointer in localizations: " + localization.getKey()).addConstraintViolation();
                    return false;
                }

                try {
                    JsonNode localizedNode = localization.getValue();
                    JsonNode node = root.at(jsonPointer);
                    if (node.getNodeType() != localizedNode.getNodeType()) {
                        context.buildConstraintViolationWithTemplate("type mismatch of JSON pointer in localizations: " + localization.getKey()).addConstraintViolation();
                        return false;
                    }
                    if (localizedNode.isObject()) {
                        String nodeClassName = node.get("@type").asText();
                        if (localizedNode.get("@type") == null) {
                            context.buildConstraintViolationWithTemplate("type mismatch of JSON pointer in localizations: " + localization.getKey()).addConstraintViolation();
                            return false;
                        }
                        String localizedNodeClassName = localizedNode.get("@type").asText();
                        if (!nodeClassName.equals(localizedNodeClassName)) {
                            context.buildConstraintViolationWithTemplate("type mismatch of JSON pointer in localizations: " + localization.getKey()).addConstraintViolation();
                            return false;
                        }
                        objectMapper.convertValue(localizedNode, Class.forName(ClassUtils.getDtoPackageName()+"."+nodeClassName));
                    }
                } catch (Exception e) {
                    context.buildConstraintViolationWithTemplate("type mismatch of JSON pointer in localizations: " + localization.getKey()).addConstraintViolation();
                    return false;
                }
            }
        }

        return true;
    }

}

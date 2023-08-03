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

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.JsonNodeType;
import it.cnr.iit.jscontact.tools.constraints.LocalizationsConstraint;
import it.cnr.iit.jscontact.tools.dto.Card;
import it.cnr.iit.jscontact.tools.dto.utils.ClassUtils;
import it.cnr.iit.jscontact.tools.dto.utils.JsonPointerUtils;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.IllformedLocaleException;
import java.util.Locale;
import java.util.Map;

public class LocalizationsValidator implements ConstraintValidator<LocalizationsConstraint, Card> {

    private static final ObjectMapper mapper = new ObjectMapper();

    public void initialize(LocalizationsConstraint constraintAnnotation) {
    }

    public boolean isValid(Card card, ConstraintValidatorContext context) {

        if (card.getLocalizations() == null)
            return true;

        for (String language : card.getLocalizations().keySet()) {
            try {
                Locale locale = new Locale.Builder().setLanguageTag(language).build();
            } catch (IllformedLocaleException e) {
                context.buildConstraintViolationWithTemplate("invalid language tag in localizations").addConstraintViolation();
                return false;
            }
        }

        for (Map<String,JsonNode> localizationsPerlanguage : card.getLocalizations().values()){

            for(Map.Entry<String,JsonNode> localization : localizationsPerlanguage.entrySet()) {

                if (localization.getKey().contains("/-/") ||
                    localization.getKey().endsWith("/-")) {
                    context.buildConstraintViolationWithTemplate(String.format("array index in key %s is -", localization.getKey())).addConstraintViolation();
                    return false;
                }

                for (String key : localizationsPerlanguage.keySet()) {
                    if (localization.getKey().startsWith(key) && !localization.getKey().equals(key)) {
                        context.buildConstraintViolationWithTemplate(String.format("the key %s is prefix of %s", key, localization.getKey())).addConstraintViolation();
                        return false;
                    }
                }

                JsonNode node;
                try {
                    node = JsonPointerUtils.getPointedJsonNode(card, localization.getKey());
                } catch (Exception e) {
                    context.buildConstraintViolationWithTemplate("invalid JSON pointer in localizations: " + localization.getKey()).addConstraintViolation();
                    return false;
                }

                try {
                    JsonNode localizedNode = localization.getValue();

                    if (node.getNodeType() == JsonNodeType.MISSING)
                        continue;

                    if (node.getNodeType() != localizedNode.getNodeType()) {
                        context.buildConstraintViolationWithTemplate("type mismatch of JSON pointer in localizations: " + localization.getKey()).addConstraintViolation();
                        return false;
                    }
                    if (localizedNode.isObject()) {
                        String nodeClassName = (node.get("@type") != null) ? node.get("@type").asText() : null;
                        String localizedNodeClassName = (localizedNode.get("@type") != null) ? localizedNode.get("@type").asText() : null;
                        if (nodeClassName != null && localizedNodeClassName != null && !nodeClassName.equals(localizedNodeClassName)) {
                            context.buildConstraintViolationWithTemplate("type mismatch of JSON pointer in localizations: " + localization.getKey()).addConstraintViolation();
                            return false;
                        }
                        mapper.convertValue(localizedNode, ClassUtils.forName(nodeClassName));
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

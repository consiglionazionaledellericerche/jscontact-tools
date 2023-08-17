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
import it.cnr.iit.jscontact.tools.constraints.CaseInsensitiveConstraint;
import it.cnr.iit.jscontact.tools.dto.AbstractExtensibleJSContactType;
import it.cnr.iit.jscontact.tools.dto.utils.ClassUtils;
import it.cnr.iit.jscontact.tools.dto.utils.JsonNodeUtils;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.io.IOException;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class CaseInsensitiveValidator implements ConstraintValidator<CaseInsensitiveConstraint, AbstractExtensibleJSContactType> {

    public void initialize(CaseInsensitiveConstraint constraintAnnotation) {
    }



    private List<String> getAllTypeNamesInJsonNode(JsonNode node) {

        if (node == null)
            return new ArrayList<>();

        if (node.isObject() || node.isArray()) {
            List<String> typeNames = new ArrayList<>();
            if (node.isObject()) {
                String typeName = (node.get("@type") != null) ? node.get("@type").asText() : null;
                if (typeName != null)
                    typeNames.add(typeName);
                Iterator<String> iterator = node.fieldNames();
                while (iterator.hasNext()) {
                    typeNames.addAll(getAllTypeNamesInJsonNode(node.get(iterator.next())));
                }
                return typeNames;
            } else if (node.isArray()) {
                Iterator<JsonNode> iterator = node.elements();
                while (iterator.hasNext()) {
                    typeNames.addAll(getAllTypeNamesInJsonNode(iterator.next()));
                }
                return typeNames;
            }
        }

        return new ArrayList<>();
    }


    public boolean isValid(AbstractExtensibleJSContactType object, ConstraintValidatorContext context) {

        if (object.getExtensions() != null) {

            for (Map.Entry<String,Object> extension : object.getExtensions().entrySet()) {

                String propertyName = extension.getKey();

                for (Field field : object.getClass().getDeclaredFields()) {
                    if (propertyName.equalsIgnoreCase(field.getName())) {
                        context.buildConstraintViolationWithTemplate(String.format("the extension name %s differs in case of the IANA registered property %s for the object %s", propertyName, field.getName(), object.getClass().getSimpleName())).addConstraintViolation();
                        return false;
                    }
                }

                JsonNode node = JsonNodeUtils.toJsonNode(extension);
                if (node == null)
                    continue;

                List<String> typeNames = getAllTypeNamesInJsonNode(node);
                for (String typeName : typeNames) {
                    try {
                        List<String> IANATypes = ClassUtils.getIANATypes();
                        for (String IANAType : IANATypes) {
                            if (IANAType.equalsIgnoreCase(typeName)) {
                                context.buildConstraintViolationWithTemplate(String.format("the type %s of extension object differs in case of the IANA registered type %s", typeName, IANAType)).addConstraintViolation();
                                return false;
                            }
                        }
                    } catch (ClassNotFoundException | IOException e) {
                        continue;
                    }
                }
            }
        }

        return true;
    }

}

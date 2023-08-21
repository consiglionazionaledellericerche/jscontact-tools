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
import it.cnr.iit.jscontact.tools.constraints.CaseInsensitiveExtensionNamesConstraint;
import it.cnr.iit.jscontact.tools.dto.*;
import it.cnr.iit.jscontact.tools.dto.annotations.ContainsExtensibleEnum;
import it.cnr.iit.jscontact.tools.dto.interfaces.IsExtensibleEnum;
import it.cnr.iit.jscontact.tools.dto.utils.ClassUtils;
import it.cnr.iit.jscontact.tools.dto.utils.EnumUtils;
import it.cnr.iit.jscontact.tools.dto.utils.JsonNodeUtils;
import it.cnr.iit.jscontact.tools.exceptions.InternalErrorException;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.io.IOException;
import java.lang.reflect.Field;
import java.util.*;

public class CaseInsensitiveExtensionNamesValidator implements ConstraintValidator<CaseInsensitiveExtensionNamesConstraint, AbstractExtensibleJSContactType> {

    public void initialize(CaseInsensitiveExtensionNamesConstraint constraintAnnotation) {
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


        for (Field field : object.getClass().getDeclaredFields()) { // checking for no enum values case sensitivity duplicates

            if (field.isAnnotationPresent(ContainsExtensibleEnum.class)) {
                ContainsExtensibleEnum annotation = field.getAnnotation(ContainsExtensibleEnum.class);
                try {
                    if (Map.class.isAssignableFrom(field.getType())) { //Map with extensible enum keys
                        Map<ExtensibleEnumType<IsExtensibleEnum>, Object> map = (Map<ExtensibleEnumType<IsExtensibleEnum>, Object>) object.getClass().getMethod(annotation.getMethod()).invoke(object);
                        if (map == null) continue;
                        for (ExtensibleEnumType key : map.keySet()) {
                            if (key.isExtValue() && EnumUtils.containsIgnoreCase(annotation.enumClass().getEnumConstants(),key.toJson())) {
                                context.buildConstraintViolationWithTemplate(String.format("the extension value %s differs only in case with an IANA registered %s.%s key", key.toJson(), object.getClass().getSimpleName(), field.getName())).addConstraintViolation();
                                return false;
                            }

                            for (ExtensibleEnumType key2 : map.keySet()) {
                                if (key.isExtValue() &&
                                    key2.isExtValue() &&
                                    key.toJson().equalsIgnoreCase(key2.toJson()) &&
                                    !key.toJson().equals(key2.toJson())) {
                                    context.buildConstraintViolationWithTemplate(String.format("the extension key %s differs only in case with the extension %s key in %s.%s", key.toJson(), key2.toJson(), object.getClass().getSimpleName(), field.getName())).addConstraintViolation();
                                    return false;
                                }
                            }
                        }
                    } else { // extensible enum value
                        ExtensibleEnumType eet = (ExtensibleEnumType) object.getClass().getMethod(annotation.getMethod()).invoke(object);
                        if (eet == null) continue;
                        if (eet.isExtValue() && EnumUtils.containsIgnoreCase(annotation.enumClass().getEnumConstants(),eet.toJson())) {
                            context.buildConstraintViolationWithTemplate(String.format("the extension value %s differs only in case with an IANA registered %s.%s value", eet.toJson(), object.getClass().getSimpleName(), field.getName())).addConstraintViolation();
                            return false;
                        }
                    }
                } catch (Exception e) {
                    throw new InternalErrorException(String.format("Internal Error: CaseInsensitiveExtensionNamesValidator.isValid - message=%s", e.getMessage()));
                }
            }
        }

        if (object.getExtensions() != null) {

            for (Map.Entry<String,Object> extension : object.getExtensions().entrySet()) { // checking for no property names case sensitivity duplicates

                String propertyName = extension.getKey();


                for (String key : object.getExtensions().keySet()) {

                    if (key.equalsIgnoreCase(propertyName) && !key.equals(propertyName)) {
                        context.buildConstraintViolationWithTemplate(String.format("the extension name %s differs only in case with the extension name %s", propertyName, key)).addConstraintViolation();
                        return false;
                    }
                }

                for (Field field : object.getClass().getDeclaredFields()) {

                    if (propertyName.equalsIgnoreCase(field.getName())) {
                        context.buildConstraintViolationWithTemplate(String.format("the extension name %s differs only in case with the IANA registered property %s.%s", propertyName, object.getClass().getSimpleName(), field.getName())).addConstraintViolation();
                        return false;
                    }
                }

                JsonNode node = JsonNodeUtils.toJsonNode(extension);
                if (node == null)
                    continue;

                List<String> typeNames = getAllTypeNamesInJsonNode(node);
                for (String typeName : typeNames) { // checking for no object types case sensitivity duplicates
                    try {
                        List<String> IANATypes = ClassUtils.getIANATypes();
                        for (String IANAType : IANATypes) {
                            if (IANAType.equalsIgnoreCase(typeName) && !IANAType.equals(typeName)) {
                                context.buildConstraintViolationWithTemplate(String.format("the extended type %s differs only in case with the IANA registered type %s", typeName, IANAType)).addConstraintViolation();
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

package org.hl7.fhir.dstu2016may.terminologies;

/*-
 * #%L
 * org.hl7.fhir.dstu2016may
 * %%
 * Copyright (C) 2014 - 2019 Health Level 7
 * %%
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *      http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * #L%
 */


import java.util.List;

import org.hl7.fhir.dstu2016may.model.BooleanType;
import org.hl7.fhir.dstu2016may.model.CodeSystem;
import org.hl7.fhir.dstu2016may.model.CodeSystem.CodeSystemPropertyComponent;
import org.hl7.fhir.dstu2016may.model.CodeSystem.ConceptDefinitionComponent;
import org.hl7.fhir.dstu2016may.model.CodeSystem.ConceptDefinitionPropertyComponent;
import org.hl7.fhir.dstu2016may.model.CodeSystem.PropertyType;

public class CodeSystemUtilities {

  public static boolean isDeprecated(CodeSystem cs, ConceptDefinitionComponent def) {
    for (ConceptDefinitionPropertyComponent p : def.getProperty()) {
      if (p.getCode().equals("deprecated") && p.hasValue() && p.getValue() instanceof BooleanType) 
        return ((BooleanType) p.getValue()).getValue();
    }
    return false;
  }

  public static boolean isAbstract(CodeSystem cs, ConceptDefinitionComponent def) {
    for (ConceptDefinitionPropertyComponent p : def.getProperty()) {
      if (p.getCode().equals("abstract") && p.hasValue() && p.getValue() instanceof BooleanType) 
        return ((BooleanType) p.getValue()).getValue();
    }
    return false;
  }

  public static void setAbstract(CodeSystem cs, ConceptDefinitionComponent concept) {
    defineAbstractProperty(cs);
    concept.addProperty().setCode("abstract").setValue(new BooleanType(true));    
  }

  public static void setDeprecated(CodeSystem cs, ConceptDefinitionComponent concept) {
    defineAbstractProperty(cs);
    concept.addProperty().setCode("deprecated").setValue(new BooleanType(true));    
  }

  public static void defineAbstractProperty(CodeSystem cs) {
    defineCodeSystemProperty(cs, "abstract", "Indicates that the code is abstract - only intended to be used as a selector for other concepts", PropertyType.BOOLEAN);
  }

  public static void defineDeprecatedProperty(CodeSystem cs) {
    defineCodeSystemProperty(cs, "deprecated", "Indicates that the code should not longer be used", PropertyType.BOOLEAN);
  }

  public static void defineCodeSystemProperty(CodeSystem cs, String code, String description, PropertyType type) {
    for (CodeSystemPropertyComponent p : cs.getProperty()) {
      if (p.getCode().equals(code))
        return;
    }
    cs.addProperty().setCode(code).setDescription(description).setType(type);
  }

  public static String getCodeDefinition(CodeSystem cs, String code) {
    return getCodeDefinition(cs.getConcept(), code);
  }

  private static String getCodeDefinition(List<ConceptDefinitionComponent> list, String code) {
    for (ConceptDefinitionComponent c : list) {
      if (c.getCode().equals(code))
        return c.getDefinition();
      String s = getCodeDefinition(c.getConcept(), code);
      if (s != null)
        return s;
    }
    return null;
  }


}

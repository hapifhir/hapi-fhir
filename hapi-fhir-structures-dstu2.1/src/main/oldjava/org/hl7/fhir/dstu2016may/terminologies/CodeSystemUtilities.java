package org.hl7.fhir.dstu2016may.terminologies;

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

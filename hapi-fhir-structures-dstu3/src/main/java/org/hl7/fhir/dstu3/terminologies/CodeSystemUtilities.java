package org.hl7.fhir.dstu3.terminologies;

import java.util.List;

import org.hl7.fhir.dstu3.model.*;
import org.hl7.fhir.dstu3.model.CodeSystem.ConceptDefinitionComponent;
import org.hl7.fhir.dstu3.model.CodeSystem.ConceptPropertyComponent;
import org.hl7.fhir.dstu3.model.CodeSystem.PropertyComponent;
import org.hl7.fhir.dstu3.model.CodeSystem.PropertyType;

public class CodeSystemUtilities {

  public static boolean isDeprecated(CodeSystem cs, ConceptDefinitionComponent def) {
    for (ConceptPropertyComponent p : def.getProperty()) {
      if (p.getCode().equals("deprecated") && p.hasValue() && p.getValue() instanceof BooleanType) 
        return ((BooleanType) p.getValue()).getValue();
    }
    return false;
  }

  public static boolean isAbstract(CodeSystem cs, ConceptDefinitionComponent def) {
    for (ConceptPropertyComponent p : def.getProperty()) {
      if (p.getCode().equals("abstract") && p.hasValue() && p.getValue() instanceof BooleanType) 
        return ((BooleanType) p.getValue()).getValue();
    }
    return false;
  }

  public static void setNotSelectable(CodeSystem cs, ConceptDefinitionComponent concept) {
    defineNotSelectableProperty(cs);
    concept.addProperty().setCode("notSelectable").setValue(new BooleanType(true));    
  }

  public static void setInactive(CodeSystem cs, ConceptDefinitionComponent concept) {
    defineInactiveProperty(cs);
    concept.addProperty().setCode("notSelectable").setValue(new BooleanType(true));    
  }

  public static void setDeprecated(CodeSystem cs, ConceptDefinitionComponent concept, DateTimeType date) {
    defineDeprecatedProperty(cs);
    concept.addProperty().setCode("deprecated").setValue(date);    
  }

  public static void defineNotSelectableProperty(CodeSystem cs) {
    defineCodeSystemProperty(cs, "notSelectable", "Indicates that the code is abstract - only intended to be used as a selector for other concepts", PropertyType.BOOLEAN);
  }

  public static void defineInactiveProperty(CodeSystem cs) {
    defineCodeSystemProperty(cs, "inactive", "True if the concept is not considered active - e.g. not a valid concept any more", PropertyType.BOOLEAN);
  }

  public static void defineDeprecatedProperty(CodeSystem cs) {
    defineCodeSystemProperty(cs, "deprecated", "The date at which a concept was deprecated. Concepts that are deprecated but not inactive can still be used, but their use is discouraged", PropertyType.DATETIME);
  }

  public static void defineCodeSystemProperty(CodeSystem cs, String code, String description, PropertyType type) {
    for (PropertyComponent p : cs.getProperty()) {
      if (p.getCode().equals(code))
        return;
    }
    cs.addProperty().setCode(code).setDescription(description).setType(type).setUri("http://hl7.org/fhir/concept-properties#"+code);
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


  public static CodeSystem makeShareable(CodeSystem cs) {
    if (!cs.hasMeta())
      cs.setMeta(new Meta());
    for (UriType t : cs.getMeta().getProfile()) 
      if (t.getValue().equals("http://hl7.org/fhir/StructureDefinition/codesystem-shareable-definition"))
        return cs;
    cs.getMeta().getProfile().add(new UriType("http://hl7.org/fhir/StructureDefinition/codesystem-shareable-definition"));
    return cs;
  }

  public static void setOID(CodeSystem cs, String oid) {
    if (!oid.startsWith("urn:oid:"))
       oid = "urn:oid:" + oid;
    if (!cs.hasIdentifier())
      cs.setIdentifier(new Identifier().setSystem("urn:ietf:rfc:3986").setValue(oid));
    else if ("urn:ietf:rfc:3986".equals(cs.getIdentifier().getSystem()) && cs.getIdentifier().hasValue() && cs.getIdentifier().getValue().startsWith("urn:oid:"))
      cs.getIdentifier().setValue(oid);
    else
      throw new Error("unable to set OID on code system");
    
  }

  public static boolean hasOID(CodeSystem cs) {
    return getOID(cs) != null;
  }

  public static String getOID(CodeSystem cs) {
    if (cs.hasIdentifier() && "urn:ietf:rfc:3986".equals(cs.getIdentifier().getSystem()) && cs.getIdentifier().hasValue() && cs.getIdentifier().getValue().startsWith("urn:oid:"))
        return cs.getIdentifier().getValue().substring(8);
    return null;
  }
}

package org.hl7.fhir.r4.terminologies;

import java.util.Calendar;
import java.util.List;

import org.hl7.fhir.r4.model.BooleanType;
import org.hl7.fhir.r4.model.CanonicalType;
import org.hl7.fhir.r4.model.CodeSystem;
import org.hl7.fhir.r4.model.CodeSystem.ConceptDefinitionComponent;
import org.hl7.fhir.r4.model.CodeSystem.ConceptPropertyComponent;
import org.hl7.fhir.r4.model.CodeSystem.PropertyComponent;
import org.hl7.fhir.r4.model.CodeSystem.PropertyType;
import org.hl7.fhir.r4.terminologies.CodeSystemUtilities.ConceptStatus;
import org.hl7.fhir.r4.model.CodeType;
import org.hl7.fhir.r4.utils.ToolingExtensions;
import org.hl7.fhir.r4.model.DateTimeType;
import org.hl7.fhir.r4.model.Identifier;
import org.hl7.fhir.r4.model.Meta;
import org.hl7.fhir.r4.model.Type;
import org.hl7.fhir.r4.model.UriType;
import org.hl7.fhir.exceptions.FHIRException;
import org.hl7.fhir.exceptions.FHIRFormatError;
import org.hl7.fhir.utilities.StandardsStatus;
import org.hl7.fhir.utilities.Utilities;

public class CodeSystemUtilities {

  public static boolean isNotSelectable(CodeSystem cs, ConceptDefinitionComponent def) {
    for (ConceptPropertyComponent p : def.getProperty()) {
      if (p.getCode().equals("notSelectable") && p.hasValue() && p.getValue() instanceof BooleanType) 
        return ((BooleanType) p.getValue()).getValue();
    }
    return false;
  }

  public static void setNotSelectable(CodeSystem cs, ConceptDefinitionComponent concept) throws FHIRFormatError {
    defineNotSelectableProperty(cs);
    ConceptPropertyComponent p = getProperty(concept, "notSelectable");
    if (p != null)
      p.setValue(new BooleanType(true));
    else
      concept.addProperty().setCode("notSelectable").setValue(new BooleanType(true));    
  }

  public static void defineNotSelectableProperty(CodeSystem cs) {
    defineCodeSystemProperty(cs, "notSelectable", "Indicates that the code is abstract - only intended to be used as a selector for other concepts", PropertyType.BOOLEAN);
  }


  public enum ConceptStatus {
    Active, Experimental, Deprecated, Retired;

    public String toCode() {
      switch (this) {
      case Active: return "active";
      case Experimental: return "experimental";
      case Deprecated: return "deprecated";
      case Retired: return "retired";
      default: return null;
      }
    }
  }

  public static void setStatus(CodeSystem cs, ConceptDefinitionComponent concept, ConceptStatus status) throws FHIRFormatError {
    defineStatusProperty(cs);
    ConceptPropertyComponent p = getProperty(concept, "status");
    if (p != null)
      p.setValue(new CodeType(status.toCode()));
    else
      concept.addProperty().setCode("status").setValue(new CodeType(status.toCode()));    
  }

  public static void defineStatusProperty(CodeSystem cs) {
    defineCodeSystemProperty(cs, "status", "A property that indicates the status of the concept. One of active, experimental, deprecated, retired", PropertyType.CODE);
  }

  private static void defineDeprecatedProperty(CodeSystem cs) {
    defineCodeSystemProperty(cs, "deprecationDate", "The date at which a concept was deprecated. Concepts that are deprecated but not inactive can still be used, but their use is discouraged", PropertyType.DATETIME);
  }

  public static boolean isDeprecated(CodeSystem cs, ConceptDefinitionComponent def)  {
    try {
      for (ConceptPropertyComponent p : def.getProperty()) {
        if (p.getCode().equals("status") && p.hasValue() && p.hasValueCodeType() && p.getValueCodeType().getCode().equals("deprecated"))
          return true;
        // this, though status should also be set
        if (p.getCode().equals("deprecationDate") && p.hasValue() && p.getValue() instanceof DateTimeType) 
          return ((DateTimeType) p.getValue()).before(new DateTimeType(Calendar.getInstance()));
        // legacy  
        if (p.getCode().equals("deprecated") && p.hasValue() && p.getValue() instanceof BooleanType) 
          return ((BooleanType) p.getValue()).getValue();
      }
      return false;
    } catch (FHIRException e) {
      return false;
    }
  }

  public static void setDeprecated(CodeSystem cs, ConceptDefinitionComponent concept, DateTimeType date) throws FHIRFormatError {
    setStatus(cs, concept, ConceptStatus.Deprecated);
    defineDeprecatedProperty(cs);
    concept.addProperty().setCode("deprecationDate").setValue(date);    
  }
  
  public static boolean isInactive(CodeSystem cs, ConceptDefinitionComponent def) throws FHIRException {
    for (ConceptPropertyComponent p : def.getProperty()) {
      if (p.getCode().equals("status") && p.hasValueStringType()) 
        return "inactive".equals(p.getValueStringType());
    }
    return false;
  }
  
  public static boolean isInactive(CodeSystem cs, String code) throws FHIRException {
    ConceptDefinitionComponent def = findCode(cs.getConcept(), code);
    if (def == null)
      return true;
    return isInactive(cs, def);
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
      if (c.hasCode() &&  c.getCode().equals(code))
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
      if (t.getValue().equals("http://hl7.org/fhir/StructureDefinition/shareablecodesystem"))
        return cs;
    cs.getMeta().getProfile().add(new CanonicalType("http://hl7.org/fhir/StructureDefinition/shareablecodesystem"));
    return cs;
  }

  public static void setOID(CodeSystem cs, String oid) {
    if (!oid.startsWith("urn:oid:"))
       oid = "urn:oid:" + oid;
    if (!cs.hasIdentifier())
      cs.addIdentifier(new Identifier().setSystem("urn:ietf:rfc:3986").setValue(oid));
    else if ("urn:ietf:rfc:3986".equals(cs.getIdentifierFirstRep().getSystem()) && cs.getIdentifierFirstRep().hasValue() && cs.getIdentifierFirstRep().getValue().startsWith("urn:oid:"))
      cs.getIdentifierFirstRep().setValue(oid);
    else
      throw new Error("unable to set OID on code system");
    
  }

  public static boolean hasOID(CodeSystem cs) {
    return getOID(cs) != null;
  }

  public static String getOID(CodeSystem cs) {
    if (cs.hasIdentifier() && "urn:ietf:rfc:3986".equals(cs.getIdentifierFirstRep().getSystem()) && cs.getIdentifierFirstRep().hasValue() && cs.getIdentifierFirstRep().getValue().startsWith("urn:oid:"))
        return cs.getIdentifierFirstRep().getValue().substring(8);
    return null;
  }

  private static ConceptDefinitionComponent findCode(List<ConceptDefinitionComponent> list, String code) {
    for (ConceptDefinitionComponent c : list) {
      if (c.getCode().equals(code))
        return c;
      ConceptDefinitionComponent s = findCode(c.getConcept(), code);
      if (s != null)
        return s;
    }
    return null;
  }

  public static void markStatus(CodeSystem cs, String wg, StandardsStatus status, String pckage, String fmm) throws FHIRException {
    if (wg != null) {
      if (!ToolingExtensions.hasExtension(cs, ToolingExtensions.EXT_WORKGROUP) || 
          (Utilities.existsInList(ToolingExtensions.readStringExtension(cs, ToolingExtensions.EXT_WORKGROUP), "fhir", "vocab") && !Utilities.existsInList(wg, "fhir", "vocab"))) {
        ToolingExtensions.setCodeExtension(cs, ToolingExtensions.EXT_WORKGROUP, wg);
      }
    }
    if (status != null) {
      StandardsStatus ss = ToolingExtensions.getStandardsStatus(cs);
      if (ss == null || ss.isLowerThan(status)) 
        ToolingExtensions.setStandardsStatus(cs, status);
      if (pckage != null) {
        if (!cs.hasUserData("ballot.package"))
          cs.setUserData("ballot.package", pckage);
        else if (!pckage.equals(cs.getUserString("ballot.package")))
          if (!"infrastructure".equals(cs.getUserString("ballot.package")))
            System.out.println("Code System "+cs.getUrl()+": ownership clash "+pckage+" vs "+cs.getUserString("ballot.package"));
      }
      if (ss == StandardsStatus.NORMATIVE)
        cs.setExperimental(false);
    }
    if (fmm != null) {
      String sfmm = ToolingExtensions.readStringExtension(cs, ToolingExtensions.EXT_FMM_LEVEL);
      if (Utilities.noString(sfmm) || Integer.parseInt(sfmm) < Integer.parseInt(fmm)) 
        ToolingExtensions.setIntegerExtension(cs, ToolingExtensions.EXT_FMM_LEVEL, Integer.parseInt(fmm));
    }
  }

 
  public static Type readProperty(ConceptDefinitionComponent concept, String code) {
    for (ConceptPropertyComponent p : concept.getProperty())
      if (p.getCode().equals(code))
        return p.getValue(); 
    return null;
  }

  public static ConceptPropertyComponent getProperty(ConceptDefinitionComponent concept, String code) {
    for (ConceptPropertyComponent p : concept.getProperty())
      if (p.getCode().equals(code))
        return p; 
    return null;
  }

}

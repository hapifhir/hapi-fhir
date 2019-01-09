package org.hl7.fhir.r4.utils;

import java.util.ArrayList;
import java.util.List;

import org.hl7.fhir.r4.utils.TypesUtilities.TypeClassification;
import org.hl7.fhir.utilities.Utilities;


public class TypesUtilities {

  public enum TypeClassification {
    PRIMITIVE, DATATYPE, METADATATYPE, SPECIAL;

    public String toDisplay() {
      switch (this) {
      case DATATYPE: return "Data Type";
      case METADATATYPE: return "MetaDataType";
      case PRIMITIVE: return "Primitive Type";
      case SPECIAL: return "Special Type";
      }
      return "??";
    }
  }

  public static class WildcardInformation {
    private TypeClassification classification;
    private String typeName;
    private String comment;
    public WildcardInformation(String typeName, String comment, TypeClassification classification) {
      super();
      this.typeName = typeName;
      this.comment = comment;
      this.classification = classification;
    }
    public WildcardInformation(String typeName, TypeClassification classification) {
      super();
      this.typeName = typeName;
      this.classification = classification;
    }
    public String getTypeName() {
      return typeName;
    }
    public String getComment() {
      return comment;
    }
    public TypeClassification getClassification() {
      return classification;
    }
    
  }
  
  public static List<String> wildcardTypes() {
    List<String> res = new ArrayList<String>();
    for (WildcardInformation wi : wildcards())
      res.add(wi.getTypeName());
    return res;
  }
  
  // this is the master list for what data types are allowed where the types = *
  // that this list is incomplete means that the following types cannot have fixed values in a profile:
  //   Narrative
  //   Meta
  //   Any of the IDMP data types
  // You have to walk into them to profile them.
  //
  public static List<WildcardInformation> wildcards() {
    List<WildcardInformation> res = new ArrayList<WildcardInformation>();

    // primitive types
    res.add(new WildcardInformation("base64Binary", TypeClassification.PRIMITIVE));
    res.add(new WildcardInformation("boolean", TypeClassification.PRIMITIVE));
    res.add(new WildcardInformation("canonical", TypeClassification.PRIMITIVE));
    res.add(new WildcardInformation("code", "(only if the extension definition provides a <a href=\"terminologies.html#code\">fixed</a> binding to a suitable set of codes)", TypeClassification.PRIMITIVE));
    res.add(new WildcardInformation("date", TypeClassification.PRIMITIVE));
    res.add(new WildcardInformation("dateTime", TypeClassification.PRIMITIVE));
    res.add(new WildcardInformation("decimal", TypeClassification.PRIMITIVE));
    res.add(new WildcardInformation("id", TypeClassification.PRIMITIVE));
    res.add(new WildcardInformation("instant", TypeClassification.PRIMITIVE));
    res.add(new WildcardInformation("integer", TypeClassification.PRIMITIVE));
    res.add(new WildcardInformation("markdown", TypeClassification.PRIMITIVE));
    res.add(new WildcardInformation("oid", TypeClassification.PRIMITIVE));
    res.add(new WildcardInformation("positiveInt", TypeClassification.PRIMITIVE));
    res.add(new WildcardInformation("string", TypeClassification.PRIMITIVE));
    res.add(new WildcardInformation("time", TypeClassification.PRIMITIVE));
    res.add(new WildcardInformation("unsignedInt", TypeClassification.PRIMITIVE));
    res.add(new WildcardInformation("uri", TypeClassification.PRIMITIVE));
    res.add(new WildcardInformation("url", TypeClassification.PRIMITIVE));
    res.add(new WildcardInformation("uuid", TypeClassification.PRIMITIVE));

    // Complex general purpose data types
    res.add(new WildcardInformation("Address", TypeClassification.DATATYPE));
    res.add(new WildcardInformation("Age", TypeClassification.DATATYPE));
    res.add(new WildcardInformation("Annotation", TypeClassification.DATATYPE));
    res.add(new WildcardInformation("Attachment", TypeClassification.DATATYPE));
    res.add(new WildcardInformation("CodeableConcept", TypeClassification.DATATYPE));
    res.add(new WildcardInformation("Coding", TypeClassification.DATATYPE));
    res.add(new WildcardInformation("ContactPoint", TypeClassification.DATATYPE));
    res.add(new WildcardInformation("Count", TypeClassification.DATATYPE));
    res.add(new WildcardInformation("Distance", TypeClassification.DATATYPE));
    res.add(new WildcardInformation("Duration", TypeClassification.DATATYPE));
    res.add(new WildcardInformation("HumanName", TypeClassification.DATATYPE));
    res.add(new WildcardInformation("Identifier", TypeClassification.DATATYPE));
    res.add(new WildcardInformation("Money", TypeClassification.DATATYPE));
    res.add(new WildcardInformation("Period", TypeClassification.DATATYPE));
    res.add(new WildcardInformation("Quantity", TypeClassification.DATATYPE));
    res.add(new WildcardInformation("Range", TypeClassification.DATATYPE));
    res.add(new WildcardInformation("Ratio", TypeClassification.DATATYPE));
    res.add(new WildcardInformation("Reference", " - a reference to another resource", TypeClassification.DATATYPE));
    res.add(new WildcardInformation("SampledData", TypeClassification.DATATYPE));
    res.add(new WildcardInformation("Signature", TypeClassification.DATATYPE));
    res.add(new WildcardInformation("Timing", TypeClassification.DATATYPE));
    
    // metadata types
    res.add(new WildcardInformation("ContactDetail", TypeClassification.METADATATYPE));
    res.add(new WildcardInformation("Contributor", TypeClassification.METADATATYPE));
    res.add(new WildcardInformation("DataRequirement", TypeClassification.METADATATYPE));
    res.add(new WildcardInformation("Expression", TypeClassification.METADATATYPE));
    res.add(new WildcardInformation("ParameterDefinition", TypeClassification.METADATATYPE));
    res.add(new WildcardInformation("RelatedArtifact", TypeClassification.METADATATYPE));
    res.add(new WildcardInformation("TriggerDefinition", TypeClassification.METADATATYPE));
    res.add(new WildcardInformation("UsageContext", TypeClassification.METADATATYPE));
    
    // special cases
    res.add(new WildcardInformation("Dosage", TypeClassification.SPECIAL));
    return res;
  }

  public static boolean isPrimitive(String code) {
    return Utilities.existsInList(code, "boolean", "integer", "string", "decimal", "uri", "url", "canonical", "base64Binary", "instant", "date", "dateTime", "time", "code", "oid", "id", "markdown", "unsignedInt", "positiveInt", "xhtml");
  }
}


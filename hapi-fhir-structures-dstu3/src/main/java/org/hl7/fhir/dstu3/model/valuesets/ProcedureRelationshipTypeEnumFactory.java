package org.hl7.fhir.dstu3.model.valuesets;

import org.hl7.fhir.dstu3.model.EnumFactory;

public class ProcedureRelationshipTypeEnumFactory implements EnumFactory<ProcedureRelationshipType> {

  public ProcedureRelationshipType fromCode(String codeString) throws IllegalArgumentException {
    if (codeString == null || "".equals(codeString))
      return null;
    if ("caused-by".equals(codeString))
      return ProcedureRelationshipType.CAUSEDBY;
    if ("because-of".equals(codeString))
      return ProcedureRelationshipType.BECAUSEOF;
    throw new IllegalArgumentException("Unknown ProcedureRelationshipType code '"+codeString+"'");
  }

  public String toCode(ProcedureRelationshipType code) {
    if (code == ProcedureRelationshipType.CAUSEDBY)
      return "caused-by";
    if (code == ProcedureRelationshipType.BECAUSEOF)
      return "because-of";
    return "?";
  }

    public String toSystem(ProcedureRelationshipType code) {
      return code.getSystem();
      }

}


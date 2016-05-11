package org.hl7.fhir.dstu3.model.valuesets;

import org.hl7.fhir.dstu3.model.EnumFactory;

public class V3ActRelationshipCheckpointEnumFactory implements EnumFactory<V3ActRelationshipCheckpoint> {

  public V3ActRelationshipCheckpoint fromCode(String codeString) throws IllegalArgumentException {
    if (codeString == null || "".equals(codeString))
      return null;
    if ("B".equals(codeString))
      return V3ActRelationshipCheckpoint.B;
    if ("E".equals(codeString))
      return V3ActRelationshipCheckpoint.E;
    if ("S".equals(codeString))
      return V3ActRelationshipCheckpoint.S;
    if ("T".equals(codeString))
      return V3ActRelationshipCheckpoint.T;
    if ("X".equals(codeString))
      return V3ActRelationshipCheckpoint.X;
    throw new IllegalArgumentException("Unknown V3ActRelationshipCheckpoint code '"+codeString+"'");
  }

  public String toCode(V3ActRelationshipCheckpoint code) {
    if (code == V3ActRelationshipCheckpoint.B)
      return "B";
    if (code == V3ActRelationshipCheckpoint.E)
      return "E";
    if (code == V3ActRelationshipCheckpoint.S)
      return "S";
    if (code == V3ActRelationshipCheckpoint.T)
      return "T";
    if (code == V3ActRelationshipCheckpoint.X)
      return "X";
    return "?";
  }

    public String toSystem(V3ActRelationshipCheckpoint code) {
      return code.getSystem();
      }

}


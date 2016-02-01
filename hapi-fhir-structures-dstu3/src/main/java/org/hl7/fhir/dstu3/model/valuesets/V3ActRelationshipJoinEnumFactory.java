package org.hl7.fhir.dstu3.model.valuesets;

import org.hl7.fhir.dstu3.model.EnumFactory;

public class V3ActRelationshipJoinEnumFactory implements EnumFactory<V3ActRelationshipJoin> {

  public V3ActRelationshipJoin fromCode(String codeString) throws IllegalArgumentException {
    if (codeString == null || "".equals(codeString))
      return null;
    if ("D".equals(codeString))
      return V3ActRelationshipJoin.D;
    if ("K".equals(codeString))
      return V3ActRelationshipJoin.K;
    if ("W".equals(codeString))
      return V3ActRelationshipJoin.W;
    if ("X".equals(codeString))
      return V3ActRelationshipJoin.X;
    throw new IllegalArgumentException("Unknown V3ActRelationshipJoin code '"+codeString+"'");
  }

  public String toCode(V3ActRelationshipJoin code) {
    if (code == V3ActRelationshipJoin.D)
      return "D";
    if (code == V3ActRelationshipJoin.K)
      return "K";
    if (code == V3ActRelationshipJoin.W)
      return "W";
    if (code == V3ActRelationshipJoin.X)
      return "X";
    return "?";
  }

    public String toSystem(V3ActRelationshipJoin code) {
      return code.getSystem();
      }

}


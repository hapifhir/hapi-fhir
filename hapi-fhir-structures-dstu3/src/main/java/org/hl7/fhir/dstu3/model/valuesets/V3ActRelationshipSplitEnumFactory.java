package org.hl7.fhir.dstu3.model.valuesets;

import org.hl7.fhir.dstu3.model.EnumFactory;

public class V3ActRelationshipSplitEnumFactory implements EnumFactory<V3ActRelationshipSplit> {

  public V3ActRelationshipSplit fromCode(String codeString) throws IllegalArgumentException {
    if (codeString == null || "".equals(codeString))
      return null;
    if ("E1".equals(codeString))
      return V3ActRelationshipSplit.E1;
    if ("EW".equals(codeString))
      return V3ActRelationshipSplit.EW;
    if ("I1".equals(codeString))
      return V3ActRelationshipSplit.I1;
    if ("IW".equals(codeString))
      return V3ActRelationshipSplit.IW;
    throw new IllegalArgumentException("Unknown V3ActRelationshipSplit code '"+codeString+"'");
  }

  public String toCode(V3ActRelationshipSplit code) {
    if (code == V3ActRelationshipSplit.E1)
      return "E1";
    if (code == V3ActRelationshipSplit.EW)
      return "EW";
    if (code == V3ActRelationshipSplit.I1)
      return "I1";
    if (code == V3ActRelationshipSplit.IW)
      return "IW";
    return "?";
  }

    public String toSystem(V3ActRelationshipSplit code) {
      return code.getSystem();
      }

}


package org.hl7.fhir.dstu3.model.valuesets;

import org.hl7.fhir.dstu3.model.EnumFactory;

public class V3ProcessingIDEnumFactory implements EnumFactory<V3ProcessingID> {

  public V3ProcessingID fromCode(String codeString) throws IllegalArgumentException {
    if (codeString == null || "".equals(codeString))
      return null;
    if ("D".equals(codeString))
      return V3ProcessingID.D;
    if ("P".equals(codeString))
      return V3ProcessingID.P;
    if ("T".equals(codeString))
      return V3ProcessingID.T;
    throw new IllegalArgumentException("Unknown V3ProcessingID code '"+codeString+"'");
  }

  public String toCode(V3ProcessingID code) {
    if (code == V3ProcessingID.D)
      return "D";
    if (code == V3ProcessingID.P)
      return "P";
    if (code == V3ProcessingID.T)
      return "T";
    return "?";
  }

    public String toSystem(V3ProcessingID code) {
      return code.getSystem();
      }

}


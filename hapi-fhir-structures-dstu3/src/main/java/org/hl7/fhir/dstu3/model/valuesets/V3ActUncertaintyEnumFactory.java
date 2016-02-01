package org.hl7.fhir.dstu3.model.valuesets;

import org.hl7.fhir.dstu3.model.EnumFactory;

public class V3ActUncertaintyEnumFactory implements EnumFactory<V3ActUncertainty> {

  public V3ActUncertainty fromCode(String codeString) throws IllegalArgumentException {
    if (codeString == null || "".equals(codeString))
      return null;
    if ("N".equals(codeString))
      return V3ActUncertainty.N;
    if ("U".equals(codeString))
      return V3ActUncertainty.U;
    throw new IllegalArgumentException("Unknown V3ActUncertainty code '"+codeString+"'");
  }

  public String toCode(V3ActUncertainty code) {
    if (code == V3ActUncertainty.N)
      return "N";
    if (code == V3ActUncertainty.U)
      return "U";
    return "?";
  }

    public String toSystem(V3ActUncertainty code) {
      return code.getSystem();
      }

}


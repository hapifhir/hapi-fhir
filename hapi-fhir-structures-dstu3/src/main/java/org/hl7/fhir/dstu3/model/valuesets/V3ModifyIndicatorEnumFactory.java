package org.hl7.fhir.dstu3.model.valuesets;

import org.hl7.fhir.dstu3.model.EnumFactory;

public class V3ModifyIndicatorEnumFactory implements EnumFactory<V3ModifyIndicator> {

  public V3ModifyIndicator fromCode(String codeString) throws IllegalArgumentException {
    if (codeString == null || "".equals(codeString))
      return null;
    if ("M".equals(codeString))
      return V3ModifyIndicator.M;
    if ("N".equals(codeString))
      return V3ModifyIndicator.N;
    throw new IllegalArgumentException("Unknown V3ModifyIndicator code '"+codeString+"'");
  }

  public String toCode(V3ModifyIndicator code) {
    if (code == V3ModifyIndicator.M)
      return "M";
    if (code == V3ModifyIndicator.N)
      return "N";
    return "?";
  }

    public String toSystem(V3ModifyIndicator code) {
      return code.getSystem();
      }

}


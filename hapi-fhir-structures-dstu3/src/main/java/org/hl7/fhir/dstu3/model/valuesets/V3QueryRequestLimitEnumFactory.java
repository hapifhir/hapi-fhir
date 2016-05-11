package org.hl7.fhir.dstu3.model.valuesets;

import org.hl7.fhir.dstu3.model.EnumFactory;

public class V3QueryRequestLimitEnumFactory implements EnumFactory<V3QueryRequestLimit> {

  public V3QueryRequestLimit fromCode(String codeString) throws IllegalArgumentException {
    if (codeString == null || "".equals(codeString))
      return null;
    if ("_QueryRequestLimit".equals(codeString))
      return V3QueryRequestLimit._QUERYREQUESTLIMIT;
    if ("RD".equals(codeString))
      return V3QueryRequestLimit.RD;
    throw new IllegalArgumentException("Unknown V3QueryRequestLimit code '"+codeString+"'");
  }

  public String toCode(V3QueryRequestLimit code) {
    if (code == V3QueryRequestLimit._QUERYREQUESTLIMIT)
      return "_QueryRequestLimit";
    if (code == V3QueryRequestLimit.RD)
      return "RD";
    return "?";
  }

    public String toSystem(V3QueryRequestLimit code) {
      return code.getSystem();
      }

}


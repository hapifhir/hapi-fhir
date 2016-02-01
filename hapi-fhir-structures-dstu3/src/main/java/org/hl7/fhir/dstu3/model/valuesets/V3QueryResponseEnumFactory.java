package org.hl7.fhir.dstu3.model.valuesets;

import org.hl7.fhir.dstu3.model.EnumFactory;

public class V3QueryResponseEnumFactory implements EnumFactory<V3QueryResponse> {

  public V3QueryResponse fromCode(String codeString) throws IllegalArgumentException {
    if (codeString == null || "".equals(codeString))
      return null;
    if ("AE".equals(codeString))
      return V3QueryResponse.AE;
    if ("NF".equals(codeString))
      return V3QueryResponse.NF;
    if ("OK".equals(codeString))
      return V3QueryResponse.OK;
    if ("QE".equals(codeString))
      return V3QueryResponse.QE;
    throw new IllegalArgumentException("Unknown V3QueryResponse code '"+codeString+"'");
  }

  public String toCode(V3QueryResponse code) {
    if (code == V3QueryResponse.AE)
      return "AE";
    if (code == V3QueryResponse.NF)
      return "NF";
    if (code == V3QueryResponse.OK)
      return "OK";
    if (code == V3QueryResponse.QE)
      return "QE";
    return "?";
  }

    public String toSystem(V3QueryResponse code) {
      return code.getSystem();
      }

}


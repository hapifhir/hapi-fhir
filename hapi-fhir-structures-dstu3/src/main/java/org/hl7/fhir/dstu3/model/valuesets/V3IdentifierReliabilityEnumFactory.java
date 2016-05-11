package org.hl7.fhir.dstu3.model.valuesets;

import org.hl7.fhir.dstu3.model.EnumFactory;

public class V3IdentifierReliabilityEnumFactory implements EnumFactory<V3IdentifierReliability> {

  public V3IdentifierReliability fromCode(String codeString) throws IllegalArgumentException {
    if (codeString == null || "".equals(codeString))
      return null;
    if ("ISS".equals(codeString))
      return V3IdentifierReliability.ISS;
    if ("UNV".equals(codeString))
      return V3IdentifierReliability.UNV;
    if ("VRF".equals(codeString))
      return V3IdentifierReliability.VRF;
    throw new IllegalArgumentException("Unknown V3IdentifierReliability code '"+codeString+"'");
  }

  public String toCode(V3IdentifierReliability code) {
    if (code == V3IdentifierReliability.ISS)
      return "ISS";
    if (code == V3IdentifierReliability.UNV)
      return "UNV";
    if (code == V3IdentifierReliability.VRF)
      return "VRF";
    return "?";
  }

    public String toSystem(V3IdentifierReliability code) {
      return code.getSystem();
      }

}


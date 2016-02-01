package org.hl7.fhir.dstu3.model.valuesets;

import org.hl7.fhir.dstu3.model.EnumFactory;

public class V3Hl7V3ConformanceEnumFactory implements EnumFactory<V3Hl7V3Conformance> {

  public V3Hl7V3Conformance fromCode(String codeString) throws IllegalArgumentException {
    if (codeString == null || "".equals(codeString))
      return null;
    if ("I".equals(codeString))
      return V3Hl7V3Conformance.I;
    if ("NP".equals(codeString))
      return V3Hl7V3Conformance.NP;
    if ("R".equals(codeString))
      return V3Hl7V3Conformance.R;
    if ("RC".equals(codeString))
      return V3Hl7V3Conformance.RC;
    if ("RI".equals(codeString))
      return V3Hl7V3Conformance.RI;
    if ("U".equals(codeString))
      return V3Hl7V3Conformance.U;
    throw new IllegalArgumentException("Unknown V3Hl7V3Conformance code '"+codeString+"'");
  }

  public String toCode(V3Hl7V3Conformance code) {
    if (code == V3Hl7V3Conformance.I)
      return "I";
    if (code == V3Hl7V3Conformance.NP)
      return "NP";
    if (code == V3Hl7V3Conformance.R)
      return "R";
    if (code == V3Hl7V3Conformance.RC)
      return "RC";
    if (code == V3Hl7V3Conformance.RI)
      return "RI";
    if (code == V3Hl7V3Conformance.U)
      return "U";
    return "?";
  }

    public String toSystem(V3Hl7V3Conformance code) {
      return code.getSystem();
      }

}


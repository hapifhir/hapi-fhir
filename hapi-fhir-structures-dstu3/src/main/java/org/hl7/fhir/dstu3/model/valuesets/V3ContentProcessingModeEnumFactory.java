package org.hl7.fhir.dstu3.model.valuesets;

import org.hl7.fhir.dstu3.model.EnumFactory;

public class V3ContentProcessingModeEnumFactory implements EnumFactory<V3ContentProcessingMode> {

  public V3ContentProcessingMode fromCode(String codeString) throws IllegalArgumentException {
    if (codeString == null || "".equals(codeString))
      return null;
    if ("SEQL".equals(codeString))
      return V3ContentProcessingMode.SEQL;
    if ("UNOR".equals(codeString))
      return V3ContentProcessingMode.UNOR;
    throw new IllegalArgumentException("Unknown V3ContentProcessingMode code '"+codeString+"'");
  }

  public String toCode(V3ContentProcessingMode code) {
    if (code == V3ContentProcessingMode.SEQL)
      return "SEQL";
    if (code == V3ContentProcessingMode.UNOR)
      return "UNOR";
    return "?";
  }

    public String toSystem(V3ContentProcessingMode code) {
      return code.getSystem();
      }

}


package org.hl7.fhir.dstu3.model.valuesets;

import org.hl7.fhir.dstu3.model.EnumFactory;

public class V3DocumentStorageEnumFactory implements EnumFactory<V3DocumentStorage> {

  public V3DocumentStorage fromCode(String codeString) throws IllegalArgumentException {
    if (codeString == null || "".equals(codeString))
      return null;
    if ("AC".equals(codeString))
      return V3DocumentStorage.AC;
    if ("AA".equals(codeString))
      return V3DocumentStorage.AA;
    if ("AR".equals(codeString))
      return V3DocumentStorage.AR;
    if ("PU".equals(codeString))
      return V3DocumentStorage.PU;
    throw new IllegalArgumentException("Unknown V3DocumentStorage code '"+codeString+"'");
  }

  public String toCode(V3DocumentStorage code) {
    if (code == V3DocumentStorage.AC)
      return "AC";
    if (code == V3DocumentStorage.AA)
      return "AA";
    if (code == V3DocumentStorage.AR)
      return "AR";
    if (code == V3DocumentStorage.PU)
      return "PU";
    return "?";
  }

    public String toSystem(V3DocumentStorage code) {
      return code.getSystem();
      }

}


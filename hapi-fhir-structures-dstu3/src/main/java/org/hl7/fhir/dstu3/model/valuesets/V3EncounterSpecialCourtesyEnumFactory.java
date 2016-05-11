package org.hl7.fhir.dstu3.model.valuesets;

import org.hl7.fhir.dstu3.model.EnumFactory;

public class V3EncounterSpecialCourtesyEnumFactory implements EnumFactory<V3EncounterSpecialCourtesy> {

  public V3EncounterSpecialCourtesy fromCode(String codeString) throws IllegalArgumentException {
    if (codeString == null || "".equals(codeString))
      return null;
    if ("EXT".equals(codeString))
      return V3EncounterSpecialCourtesy.EXT;
    if ("NRM".equals(codeString))
      return V3EncounterSpecialCourtesy.NRM;
    if ("PRF".equals(codeString))
      return V3EncounterSpecialCourtesy.PRF;
    if ("STF".equals(codeString))
      return V3EncounterSpecialCourtesy.STF;
    if ("VIP".equals(codeString))
      return V3EncounterSpecialCourtesy.VIP;
    throw new IllegalArgumentException("Unknown V3EncounterSpecialCourtesy code '"+codeString+"'");
  }

  public String toCode(V3EncounterSpecialCourtesy code) {
    if (code == V3EncounterSpecialCourtesy.EXT)
      return "EXT";
    if (code == V3EncounterSpecialCourtesy.NRM)
      return "NRM";
    if (code == V3EncounterSpecialCourtesy.PRF)
      return "PRF";
    if (code == V3EncounterSpecialCourtesy.STF)
      return "STF";
    if (code == V3EncounterSpecialCourtesy.VIP)
      return "VIP";
    return "?";
  }

    public String toSystem(V3EncounterSpecialCourtesy code) {
      return code.getSystem();
      }

}


package org.hl7.fhir.dstu3.model.valuesets;

import org.hl7.fhir.dstu3.model.EnumFactory;

public class EncounterTypeEnumFactory implements EnumFactory<EncounterType> {

  public EncounterType fromCode(String codeString) throws IllegalArgumentException {
    if (codeString == null || "".equals(codeString))
      return null;
    if ("ADMS".equals(codeString))
      return EncounterType.ADMS;
    if ("BD/BM-clin".equals(codeString))
      return EncounterType.BD_BMCLIN;
    if ("CCS60".equals(codeString))
      return EncounterType.CCS60;
    if ("OKI".equals(codeString))
      return EncounterType.OKI;
    throw new IllegalArgumentException("Unknown EncounterType code '"+codeString+"'");
  }

  public String toCode(EncounterType code) {
    if (code == EncounterType.ADMS)
      return "ADMS";
    if (code == EncounterType.BD_BMCLIN)
      return "BD/BM-clin";
    if (code == EncounterType.CCS60)
      return "CCS60";
    if (code == EncounterType.OKI)
      return "OKI";
    return "?";
  }

    public String toSystem(EncounterType code) {
      return code.getSystem();
      }

}


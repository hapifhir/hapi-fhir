package org.hl7.fhir.dstu3.model.valuesets;

import org.hl7.fhir.dstu3.model.EnumFactory;

public class EncounterAdmitSourceEnumFactory implements EnumFactory<EncounterAdmitSource> {

  public EncounterAdmitSource fromCode(String codeString) throws IllegalArgumentException {
    if (codeString == null || "".equals(codeString))
      return null;
    if ("hosp-trans".equals(codeString))
      return EncounterAdmitSource.HOSPTRANS;
    if ("emd".equals(codeString))
      return EncounterAdmitSource.EMD;
    if ("outp".equals(codeString))
      return EncounterAdmitSource.OUTP;
    if ("born".equals(codeString))
      return EncounterAdmitSource.BORN;
    if ("gp".equals(codeString))
      return EncounterAdmitSource.GP;
    if ("mp".equals(codeString))
      return EncounterAdmitSource.MP;
    if ("nursing".equals(codeString))
      return EncounterAdmitSource.NURSING;
    if ("psych".equals(codeString))
      return EncounterAdmitSource.PSYCH;
    if ("rehab".equals(codeString))
      return EncounterAdmitSource.REHAB;
    if ("other".equals(codeString))
      return EncounterAdmitSource.OTHER;
    throw new IllegalArgumentException("Unknown EncounterAdmitSource code '"+codeString+"'");
  }

  public String toCode(EncounterAdmitSource code) {
    if (code == EncounterAdmitSource.HOSPTRANS)
      return "hosp-trans";
    if (code == EncounterAdmitSource.EMD)
      return "emd";
    if (code == EncounterAdmitSource.OUTP)
      return "outp";
    if (code == EncounterAdmitSource.BORN)
      return "born";
    if (code == EncounterAdmitSource.GP)
      return "gp";
    if (code == EncounterAdmitSource.MP)
      return "mp";
    if (code == EncounterAdmitSource.NURSING)
      return "nursing";
    if (code == EncounterAdmitSource.PSYCH)
      return "psych";
    if (code == EncounterAdmitSource.REHAB)
      return "rehab";
    if (code == EncounterAdmitSource.OTHER)
      return "other";
    return "?";
  }

    public String toSystem(EncounterAdmitSource code) {
      return code.getSystem();
      }

}


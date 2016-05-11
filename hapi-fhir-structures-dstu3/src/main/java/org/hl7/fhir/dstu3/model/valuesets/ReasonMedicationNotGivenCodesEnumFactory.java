package org.hl7.fhir.dstu3.model.valuesets;

import org.hl7.fhir.dstu3.model.EnumFactory;

public class ReasonMedicationNotGivenCodesEnumFactory implements EnumFactory<ReasonMedicationNotGivenCodes> {

  public ReasonMedicationNotGivenCodes fromCode(String codeString) throws IllegalArgumentException {
    if (codeString == null || "".equals(codeString))
      return null;
    if ("a".equals(codeString))
      return ReasonMedicationNotGivenCodes.A;
    if ("b".equals(codeString))
      return ReasonMedicationNotGivenCodes.B;
    if ("c".equals(codeString))
      return ReasonMedicationNotGivenCodes.C;
    if ("d".equals(codeString))
      return ReasonMedicationNotGivenCodes.D;
    throw new IllegalArgumentException("Unknown ReasonMedicationNotGivenCodes code '"+codeString+"'");
  }

  public String toCode(ReasonMedicationNotGivenCodes code) {
    if (code == ReasonMedicationNotGivenCodes.A)
      return "a";
    if (code == ReasonMedicationNotGivenCodes.B)
      return "b";
    if (code == ReasonMedicationNotGivenCodes.C)
      return "c";
    if (code == ReasonMedicationNotGivenCodes.D)
      return "d";
    return "?";
  }

    public String toSystem(ReasonMedicationNotGivenCodes code) {
      return code.getSystem();
      }

}


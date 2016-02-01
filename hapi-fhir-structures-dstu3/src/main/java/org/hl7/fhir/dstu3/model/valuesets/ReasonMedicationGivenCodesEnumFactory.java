package org.hl7.fhir.dstu3.model.valuesets;

import org.hl7.fhir.dstu3.model.EnumFactory;

public class ReasonMedicationGivenCodesEnumFactory implements EnumFactory<ReasonMedicationGivenCodes> {

  public ReasonMedicationGivenCodes fromCode(String codeString) throws IllegalArgumentException {
    if (codeString == null || "".equals(codeString))
      return null;
    if ("a".equals(codeString))
      return ReasonMedicationGivenCodes.A;
    if ("b".equals(codeString))
      return ReasonMedicationGivenCodes.B;
    if ("c".equals(codeString))
      return ReasonMedicationGivenCodes.C;
    throw new IllegalArgumentException("Unknown ReasonMedicationGivenCodes code '"+codeString+"'");
  }

  public String toCode(ReasonMedicationGivenCodes code) {
    if (code == ReasonMedicationGivenCodes.A)
      return "a";
    if (code == ReasonMedicationGivenCodes.B)
      return "b";
    if (code == ReasonMedicationGivenCodes.C)
      return "c";
    return "?";
  }

    public String toSystem(ReasonMedicationGivenCodes code) {
      return code.getSystem();
      }

}


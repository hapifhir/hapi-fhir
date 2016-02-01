package org.hl7.fhir.dstu3.model.valuesets;

import org.hl7.fhir.dstu3.model.EnumFactory;

public class ContractTermSubtypeEnumFactory implements EnumFactory<ContractTermSubtype> {

  public ContractTermSubtype fromCode(String codeString) throws IllegalArgumentException {
    if (codeString == null || "".equals(codeString))
      return null;
    if ("OralHealth-Basic".equals(codeString))
      return ContractTermSubtype.ORALHEALTHBASIC;
    if ("OralHealth-Major".equals(codeString))
      return ContractTermSubtype.ORALHEALTHMAJOR;
    if ("OralHealth-Orthodontic".equals(codeString))
      return ContractTermSubtype.ORALHEALTHORTHODONTIC;
    throw new IllegalArgumentException("Unknown ContractTermSubtype code '"+codeString+"'");
  }

  public String toCode(ContractTermSubtype code) {
    if (code == ContractTermSubtype.ORALHEALTHBASIC)
      return "OralHealth-Basic";
    if (code == ContractTermSubtype.ORALHEALTHMAJOR)
      return "OralHealth-Major";
    if (code == ContractTermSubtype.ORALHEALTHORTHODONTIC)
      return "OralHealth-Orthodontic";
    return "?";
  }

    public String toSystem(ContractTermSubtype code) {
      return code.getSystem();
      }

}


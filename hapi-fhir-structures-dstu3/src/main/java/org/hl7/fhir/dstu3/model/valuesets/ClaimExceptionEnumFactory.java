package org.hl7.fhir.dstu3.model.valuesets;

import org.hl7.fhir.dstu3.model.EnumFactory;

public class ClaimExceptionEnumFactory implements EnumFactory<ClaimException> {

  public ClaimException fromCode(String codeString) throws IllegalArgumentException {
    if (codeString == null || "".equals(codeString))
      return null;
    if ("student".equals(codeString))
      return ClaimException.STUDENT;
    if ("disabled".equals(codeString))
      return ClaimException.DISABLED;
    throw new IllegalArgumentException("Unknown ClaimException code '"+codeString+"'");
  }

  public String toCode(ClaimException code) {
    if (code == ClaimException.STUDENT)
      return "student";
    if (code == ClaimException.DISABLED)
      return "disabled";
    return "?";
  }

    public String toSystem(ClaimException code) {
      return code.getSystem();
      }

}


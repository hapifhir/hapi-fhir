package org.hl7.fhir.dstu3.model.valuesets;

import org.hl7.fhir.dstu3.model.EnumFactory;

public class FlagPriorityEnumFactory implements EnumFactory<FlagPriority> {

  public FlagPriority fromCode(String codeString) throws IllegalArgumentException {
    if (codeString == null || "".equals(codeString))
      return null;
    if ("PN".equals(codeString))
      return FlagPriority.PN;
    if ("PL".equals(codeString))
      return FlagPriority.PL;
    if ("PM".equals(codeString))
      return FlagPriority.PM;
    if ("PH".equals(codeString))
      return FlagPriority.PH;
    throw new IllegalArgumentException("Unknown FlagPriority code '"+codeString+"'");
  }

  public String toCode(FlagPriority code) {
    if (code == FlagPriority.PN)
      return "PN";
    if (code == FlagPriority.PL)
      return "PL";
    if (code == FlagPriority.PM)
      return "PM";
    if (code == FlagPriority.PH)
      return "PH";
    return "?";
  }

    public String toSystem(FlagPriority code) {
      return code.getSystem();
      }

}


package org.hl7.fhir.dstu3.model.valuesets;

import org.hl7.fhir.dstu3.model.EnumFactory;

public class MissingToothReasonEnumFactory implements EnumFactory<MissingToothReason> {

  public MissingToothReason fromCode(String codeString) throws IllegalArgumentException {
    if (codeString == null || "".equals(codeString))
      return null;
    if ("E".equals(codeString))
      return MissingToothReason.E;
    if ("C".equals(codeString))
      return MissingToothReason.C;
    if ("U".equals(codeString))
      return MissingToothReason.U;
    if ("O".equals(codeString))
      return MissingToothReason.O;
    throw new IllegalArgumentException("Unknown MissingToothReason code '"+codeString+"'");
  }

  public String toCode(MissingToothReason code) {
    if (code == MissingToothReason.E)
      return "E";
    if (code == MissingToothReason.C)
      return "C";
    if (code == MissingToothReason.U)
      return "U";
    if (code == MissingToothReason.O)
      return "O";
    return "?";
  }

    public String toSystem(MissingToothReason code) {
      return code.getSystem();
      }

}


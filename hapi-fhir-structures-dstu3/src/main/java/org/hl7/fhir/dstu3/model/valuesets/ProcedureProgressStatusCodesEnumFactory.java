package org.hl7.fhir.dstu3.model.valuesets;

import org.hl7.fhir.dstu3.model.EnumFactory;

public class ProcedureProgressStatusCodesEnumFactory implements EnumFactory<ProcedureProgressStatusCodes> {

  public ProcedureProgressStatusCodes fromCode(String codeString) throws IllegalArgumentException {
    if (codeString == null || "".equals(codeString))
      return null;
    if ("a".equals(codeString))
      return ProcedureProgressStatusCodes.A;
    if ("b".equals(codeString))
      return ProcedureProgressStatusCodes.B;
    if ("c".equals(codeString))
      return ProcedureProgressStatusCodes.C;
    if ("d".equals(codeString))
      return ProcedureProgressStatusCodes.D;
    if ("e".equals(codeString))
      return ProcedureProgressStatusCodes.E;
    if ("f".equals(codeString))
      return ProcedureProgressStatusCodes.F;
    throw new IllegalArgumentException("Unknown ProcedureProgressStatusCodes code '"+codeString+"'");
  }

  public String toCode(ProcedureProgressStatusCodes code) {
    if (code == ProcedureProgressStatusCodes.A)
      return "a";
    if (code == ProcedureProgressStatusCodes.B)
      return "b";
    if (code == ProcedureProgressStatusCodes.C)
      return "c";
    if (code == ProcedureProgressStatusCodes.D)
      return "d";
    if (code == ProcedureProgressStatusCodes.E)
      return "e";
    if (code == ProcedureProgressStatusCodes.F)
      return "f";
    return "?";
  }

    public String toSystem(ProcedureProgressStatusCodes code) {
      return code.getSystem();
      }

}


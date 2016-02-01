package org.hl7.fhir.dstu3.model.valuesets;

import org.hl7.fhir.dstu3.model.EnumFactory;

public class RulesetEnumFactory implements EnumFactory<Ruleset> {

  public Ruleset fromCode(String codeString) throws IllegalArgumentException {
    if (codeString == null || "".equals(codeString))
      return null;
    if ("x12-4010".equals(codeString))
      return Ruleset.X124010;
    if ("x12-5010".equals(codeString))
      return Ruleset.X125010;
    if ("x12-7010".equals(codeString))
      return Ruleset.X127010;
    if ("cdanet-v2".equals(codeString))
      return Ruleset.CDANETV2;
    if ("cdanet-v4".equals(codeString))
      return Ruleset.CDANETV4;
    if ("cpha-3".equals(codeString))
      return Ruleset.CPHA3;
    throw new IllegalArgumentException("Unknown Ruleset code '"+codeString+"'");
  }

  public String toCode(Ruleset code) {
    if (code == Ruleset.X124010)
      return "x12-4010";
    if (code == Ruleset.X125010)
      return "x12-5010";
    if (code == Ruleset.X127010)
      return "x12-7010";
    if (code == Ruleset.CDANETV2)
      return "cdanet-v2";
    if (code == Ruleset.CDANETV4)
      return "cdanet-v4";
    if (code == Ruleset.CPHA3)
      return "cpha-3";
    return "?";
  }

    public String toSystem(Ruleset code) {
      return code.getSystem();
      }

}


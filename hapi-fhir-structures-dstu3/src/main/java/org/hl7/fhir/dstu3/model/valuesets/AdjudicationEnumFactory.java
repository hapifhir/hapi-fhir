package org.hl7.fhir.dstu3.model.valuesets;

import org.hl7.fhir.dstu3.model.EnumFactory;

public class AdjudicationEnumFactory implements EnumFactory<Adjudication> {

  public Adjudication fromCode(String codeString) throws IllegalArgumentException {
    if (codeString == null || "".equals(codeString))
      return null;
    if ("total".equals(codeString))
      return Adjudication.TOTAL;
    if ("copay".equals(codeString))
      return Adjudication.COPAY;
    if ("eligible".equals(codeString))
      return Adjudication.ELIGIBLE;
    if ("deductible".equals(codeString))
      return Adjudication.DEDUCTIBLE;
    if ("eligpercent".equals(codeString))
      return Adjudication.ELIGPERCENT;
    if ("tax".equals(codeString))
      return Adjudication.TAX;
    if ("benefit".equals(codeString))
      return Adjudication.BENEFIT;
    throw new IllegalArgumentException("Unknown Adjudication code '"+codeString+"'");
  }

  public String toCode(Adjudication code) {
    if (code == Adjudication.TOTAL)
      return "total";
    if (code == Adjudication.COPAY)
      return "copay";
    if (code == Adjudication.ELIGIBLE)
      return "eligible";
    if (code == Adjudication.DEDUCTIBLE)
      return "deductible";
    if (code == Adjudication.ELIGPERCENT)
      return "eligpercent";
    if (code == Adjudication.TAX)
      return "tax";
    if (code == Adjudication.BENEFIT)
      return "benefit";
    return "?";
  }

    public String toSystem(Adjudication code) {
      return code.getSystem();
      }

}


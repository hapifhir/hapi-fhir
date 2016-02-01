package org.hl7.fhir.dstu3.model.valuesets;

import org.hl7.fhir.dstu3.model.EnumFactory;

public class BenefitTypeEnumFactory implements EnumFactory<BenefitType> {

  public BenefitType fromCode(String codeString) throws IllegalArgumentException {
    if (codeString == null || "".equals(codeString))
      return null;
    if ("deductable".equals(codeString))
      return BenefitType.DEDUCTABLE;
    if ("visit".equals(codeString))
      return BenefitType.VISIT;
    if ("copay".equals(codeString))
      return BenefitType.COPAY;
    if ("vision-exam".equals(codeString))
      return BenefitType.VISIONEXAM;
    if ("vision-glasses".equals(codeString))
      return BenefitType.VISIONGLASSES;
    if ("vision-contacts".equals(codeString))
      return BenefitType.VISIONCONTACTS;
    if ("medical-primarycare".equals(codeString))
      return BenefitType.MEDICALPRIMARYCARE;
    if ("pharmacy-dispense".equals(codeString))
      return BenefitType.PHARMACYDISPENSE;
    throw new IllegalArgumentException("Unknown BenefitType code '"+codeString+"'");
  }

  public String toCode(BenefitType code) {
    if (code == BenefitType.DEDUCTABLE)
      return "deductable";
    if (code == BenefitType.VISIT)
      return "visit";
    if (code == BenefitType.COPAY)
      return "copay";
    if (code == BenefitType.VISIONEXAM)
      return "vision-exam";
    if (code == BenefitType.VISIONGLASSES)
      return "vision-glasses";
    if (code == BenefitType.VISIONCONTACTS)
      return "vision-contacts";
    if (code == BenefitType.MEDICALPRIMARYCARE)
      return "medical-primarycare";
    if (code == BenefitType.PHARMACYDISPENSE)
      return "pharmacy-dispense";
    return "?";
  }

    public String toSystem(BenefitType code) {
      return code.getSystem();
      }

}


package org.hl7.fhir.dstu3.model.valuesets;

import org.hl7.fhir.dstu3.model.EnumFactory;

public class BenefitSubcategoryEnumFactory implements EnumFactory<BenefitSubcategory> {

  public BenefitSubcategory fromCode(String codeString) throws IllegalArgumentException {
    if (codeString == null || "".equals(codeString))
      return null;
    if ("oral-basic".equals(codeString))
      return BenefitSubcategory.ORALBASIC;
    if ("oral-major".equals(codeString))
      return BenefitSubcategory.ORALMAJOR;
    if ("oral-ortho".equals(codeString))
      return BenefitSubcategory.ORALORTHO;
    if ("vision-exam".equals(codeString))
      return BenefitSubcategory.VISIONEXAM;
    if ("vision-glasses".equals(codeString))
      return BenefitSubcategory.VISIONGLASSES;
    if ("vision-contacts".equals(codeString))
      return BenefitSubcategory.VISIONCONTACTS;
    if ("medical-primarycare".equals(codeString))
      return BenefitSubcategory.MEDICALPRIMARYCARE;
    if ("pharmacy-dispense".equals(codeString))
      return BenefitSubcategory.PHARMACYDISPENSE;
    throw new IllegalArgumentException("Unknown BenefitSubcategory code '"+codeString+"'");
  }

  public String toCode(BenefitSubcategory code) {
    if (code == BenefitSubcategory.ORALBASIC)
      return "oral-basic";
    if (code == BenefitSubcategory.ORALMAJOR)
      return "oral-major";
    if (code == BenefitSubcategory.ORALORTHO)
      return "oral-ortho";
    if (code == BenefitSubcategory.VISIONEXAM)
      return "vision-exam";
    if (code == BenefitSubcategory.VISIONGLASSES)
      return "vision-glasses";
    if (code == BenefitSubcategory.VISIONCONTACTS)
      return "vision-contacts";
    if (code == BenefitSubcategory.MEDICALPRIMARYCARE)
      return "medical-primarycare";
    if (code == BenefitSubcategory.PHARMACYDISPENSE)
      return "pharmacy-dispense";
    return "?";
  }

    public String toSystem(BenefitSubcategory code) {
      return code.getSystem();
      }

}


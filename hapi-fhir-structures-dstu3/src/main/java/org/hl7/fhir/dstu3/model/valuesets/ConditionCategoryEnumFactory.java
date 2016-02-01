package org.hl7.fhir.dstu3.model.valuesets;

import org.hl7.fhir.dstu3.model.EnumFactory;

public class ConditionCategoryEnumFactory implements EnumFactory<ConditionCategory> {

  public ConditionCategory fromCode(String codeString) throws IllegalArgumentException {
    if (codeString == null || "".equals(codeString))
      return null;
    if ("complaint".equals(codeString))
      return ConditionCategory.COMPLAINT;
    if ("symptom".equals(codeString))
      return ConditionCategory.SYMPTOM;
    if ("finding".equals(codeString))
      return ConditionCategory.FINDING;
    if ("diagnosis".equals(codeString))
      return ConditionCategory.DIAGNOSIS;
    throw new IllegalArgumentException("Unknown ConditionCategory code '"+codeString+"'");
  }

  public String toCode(ConditionCategory code) {
    if (code == ConditionCategory.COMPLAINT)
      return "complaint";
    if (code == ConditionCategory.SYMPTOM)
      return "symptom";
    if (code == ConditionCategory.FINDING)
      return "finding";
    if (code == ConditionCategory.DIAGNOSIS)
      return "diagnosis";
    return "?";
  }

    public String toSystem(ConditionCategory code) {
      return code.getSystem();
      }

}


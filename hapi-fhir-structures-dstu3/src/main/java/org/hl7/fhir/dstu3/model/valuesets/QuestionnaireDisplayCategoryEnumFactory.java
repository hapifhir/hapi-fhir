package org.hl7.fhir.dstu3.model.valuesets;

import org.hl7.fhir.dstu3.model.EnumFactory;

public class QuestionnaireDisplayCategoryEnumFactory implements EnumFactory<QuestionnaireDisplayCategory> {

  public QuestionnaireDisplayCategory fromCode(String codeString) throws IllegalArgumentException {
    if (codeString == null || "".equals(codeString))
      return null;
    if ("instructions".equals(codeString))
      return QuestionnaireDisplayCategory.INSTRUCTIONS;
    if ("security".equals(codeString))
      return QuestionnaireDisplayCategory.SECURITY;
    throw new IllegalArgumentException("Unknown QuestionnaireDisplayCategory code '"+codeString+"'");
  }

  public String toCode(QuestionnaireDisplayCategory code) {
    if (code == QuestionnaireDisplayCategory.INSTRUCTIONS)
      return "instructions";
    if (code == QuestionnaireDisplayCategory.SECURITY)
      return "security";
    return "?";
  }

    public String toSystem(QuestionnaireDisplayCategory code) {
      return code.getSystem();
      }

}


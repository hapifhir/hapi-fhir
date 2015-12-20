package org.hl7.fhir.dstu21.model.valuesets;

import org.hl7.fhir.dstu21.model.EnumFactory;

public class QuestionnaireDisplayCategoryEnumFactory implements EnumFactory<QuestionnaireDisplayCategory> {

  public QuestionnaireDisplayCategory fromCode(String codeString) throws IllegalArgumentException {
    if (codeString == null || "".equals(codeString))
      return null;
    if ("instructions".equals(codeString))
      return QuestionnaireDisplayCategory.INSTRUCTIONS;
    if ("units".equals(codeString))
      return QuestionnaireDisplayCategory.UNITS;
    if ("security".equals(codeString))
      return QuestionnaireDisplayCategory.SECURITY;
    if ("lookup".equals(codeString))
      return QuestionnaireDisplayCategory.LOOKUP;
    if ("radio-button".equals(codeString))
      return QuestionnaireDisplayCategory.RADIOBUTTON;
    if ("slider".equals(codeString))
      return QuestionnaireDisplayCategory.SLIDER;
    if ("spinner".equals(codeString))
      return QuestionnaireDisplayCategory.SPINNER;
    if ("text-box".equals(codeString))
      return QuestionnaireDisplayCategory.TEXTBOX;
    throw new IllegalArgumentException("Unknown QuestionnaireDisplayCategory code '"+codeString+"'");
  }

  public String toCode(QuestionnaireDisplayCategory code) {
    if (code == QuestionnaireDisplayCategory.INSTRUCTIONS)
      return "instructions";
    if (code == QuestionnaireDisplayCategory.UNITS)
      return "units";
    if (code == QuestionnaireDisplayCategory.SECURITY)
      return "security";
    if (code == QuestionnaireDisplayCategory.LOOKUP)
      return "lookup";
    if (code == QuestionnaireDisplayCategory.RADIOBUTTON)
      return "radio-button";
    if (code == QuestionnaireDisplayCategory.SLIDER)
      return "slider";
    if (code == QuestionnaireDisplayCategory.SPINNER)
      return "spinner";
    if (code == QuestionnaireDisplayCategory.TEXTBOX)
      return "text-box";
    return "?";
  }


}


package org.hl7.fhir.dstu3.model.valuesets;

import org.hl7.fhir.dstu3.model.EnumFactory;

public class QuestionMaxOccursEnumFactory implements EnumFactory<QuestionMaxOccurs> {

  public QuestionMaxOccurs fromCode(String codeString) throws IllegalArgumentException {
    if (codeString == null || "".equals(codeString))
      return null;
    if ("*".equals(codeString))
      return QuestionMaxOccurs.ASTERISK;
    throw new IllegalArgumentException("Unknown QuestionMaxOccurs code '"+codeString+"'");
  }

  public String toCode(QuestionMaxOccurs code) {
    if (code == QuestionMaxOccurs.ASTERISK)
      return "*";
    return "?";
  }

    public String toSystem(QuestionMaxOccurs code) {
      return code.getSystem();
      }

}


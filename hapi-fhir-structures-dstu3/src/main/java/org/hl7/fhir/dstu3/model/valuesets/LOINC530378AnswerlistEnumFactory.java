package org.hl7.fhir.dstu3.model.valuesets;

import org.hl7.fhir.dstu3.model.EnumFactory;

public class LOINC530378AnswerlistEnumFactory implements EnumFactory<LOINC530378Answerlist> {

  public LOINC530378Answerlist fromCode(String codeString) throws IllegalArgumentException {
    if (codeString == null || "".equals(codeString))
      return null;
    if ("LA6668-3".equals(codeString))
      return LOINC530378Answerlist.LA66683;
    if ("LA6669-1".equals(codeString))
      return LOINC530378Answerlist.LA66691;
    if ("LA6682-4".equals(codeString))
      return LOINC530378Answerlist.LA66824;
    if ("LA6675-8".equals(codeString))
      return LOINC530378Answerlist.LA66758;
    if ("LA6674-1".equals(codeString))
      return LOINC530378Answerlist.LA66741;
    throw new IllegalArgumentException("Unknown LOINC530378Answerlist code '"+codeString+"'");
  }

  public String toCode(LOINC530378Answerlist code) {
    if (code == LOINC530378Answerlist.LA66683)
      return "LA6668-3";
    if (code == LOINC530378Answerlist.LA66691)
      return "LA6669-1";
    if (code == LOINC530378Answerlist.LA66824)
      return "LA6682-4";
    if (code == LOINC530378Answerlist.LA66758)
      return "LA6675-8";
    if (code == LOINC530378Answerlist.LA66741)
      return "LA6674-1";
    return "?";
  }

    public String toSystem(LOINC530378Answerlist code) {
      return code.getSystem();
      }

}


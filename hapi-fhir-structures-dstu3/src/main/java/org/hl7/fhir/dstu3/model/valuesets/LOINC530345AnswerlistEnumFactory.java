package org.hl7.fhir.dstu3.model.valuesets;

import org.hl7.fhir.dstu3.model.EnumFactory;

public class LOINC530345AnswerlistEnumFactory implements EnumFactory<LOINC530345Answerlist> {

  public LOINC530345Answerlist fromCode(String codeString) throws IllegalArgumentException {
    if (codeString == null || "".equals(codeString))
      return null;
    if ("LA6703-8".equals(codeString))
      return LOINC530345Answerlist.LA67038;
    if ("LA6704-6".equals(codeString))
      return LOINC530345Answerlist.LA67046;
    if ("LA6705-3".equals(codeString))
      return LOINC530345Answerlist.LA67053;
    if ("LA6706-1".equals(codeString))
      return LOINC530345Answerlist.LA67061;
    if ("LA6707-9".equals(codeString))
      return LOINC530345Answerlist.LA67079;
    throw new IllegalArgumentException("Unknown LOINC530345Answerlist code '"+codeString+"'");
  }

  public String toCode(LOINC530345Answerlist code) {
    if (code == LOINC530345Answerlist.LA67038)
      return "LA6703-8";
    if (code == LOINC530345Answerlist.LA67046)
      return "LA6704-6";
    if (code == LOINC530345Answerlist.LA67053)
      return "LA6705-3";
    if (code == LOINC530345Answerlist.LA67061)
      return "LA6706-1";
    if (code == LOINC530345Answerlist.LA67079)
      return "LA6707-9";
    return "?";
  }

    public String toSystem(LOINC530345Answerlist code) {
      return code.getSystem();
      }

}


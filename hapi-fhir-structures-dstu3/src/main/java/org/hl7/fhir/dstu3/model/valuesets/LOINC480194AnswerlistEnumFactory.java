package org.hl7.fhir.dstu3.model.valuesets;

import org.hl7.fhir.dstu3.model.EnumFactory;

public class LOINC480194AnswerlistEnumFactory implements EnumFactory<LOINC480194Answerlist> {

  public LOINC480194Answerlist fromCode(String codeString) throws IllegalArgumentException {
    if (codeString == null || "".equals(codeString))
      return null;
    if ("LA9658-1".equals(codeString))
      return LOINC480194Answerlist.LA96581;
    if ("LA6692-3".equals(codeString))
      return LOINC480194Answerlist.LA66923;
    if ("LA6686-5".equals(codeString))
      return LOINC480194Answerlist.LA66865;
    if ("LA6687-3".equals(codeString))
      return LOINC480194Answerlist.LA66873;
    if ("LA6688-1".equals(codeString))
      return LOINC480194Answerlist.LA66881;
    if ("LA6689-9".equals(codeString))
      return LOINC480194Answerlist.LA66899;
    if ("LA6690-7".equals(codeString))
      return LOINC480194Answerlist.LA66907;
    throw new IllegalArgumentException("Unknown LOINC480194Answerlist code '"+codeString+"'");
  }

  public String toCode(LOINC480194Answerlist code) {
    if (code == LOINC480194Answerlist.LA96581)
      return "LA9658-1";
    if (code == LOINC480194Answerlist.LA66923)
      return "LA6692-3";
    if (code == LOINC480194Answerlist.LA66865)
      return "LA6686-5";
    if (code == LOINC480194Answerlist.LA66873)
      return "LA6687-3";
    if (code == LOINC480194Answerlist.LA66881)
      return "LA6688-1";
    if (code == LOINC480194Answerlist.LA66899)
      return "LA6689-9";
    if (code == LOINC480194Answerlist.LA66907)
      return "LA6690-7";
    return "?";
  }

    public String toSystem(LOINC480194Answerlist code) {
      return code.getSystem();
      }

}


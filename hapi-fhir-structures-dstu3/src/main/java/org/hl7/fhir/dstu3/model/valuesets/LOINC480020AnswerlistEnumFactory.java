package org.hl7.fhir.dstu3.model.valuesets;

import org.hl7.fhir.dstu3.model.EnumFactory;

public class LOINC480020AnswerlistEnumFactory implements EnumFactory<LOINC480020Answerlist> {

  public LOINC480020Answerlist fromCode(String codeString) throws IllegalArgumentException {
    if (codeString == null || "".equals(codeString))
      return null;
    if ("LA6683-2".equals(codeString))
      return LOINC480020Answerlist.LA66832;
    if ("LA6684-0".equals(codeString))
      return LOINC480020Answerlist.LA66840;
    if ("LA6685-7".equals(codeString))
      return LOINC480020Answerlist.LA66857;
    if ("LA18194-3".equals(codeString))
      return LOINC480020Answerlist.LA181943;
    if ("LA18195-0".equals(codeString))
      return LOINC480020Answerlist.LA181950;
    if ("LA18196-8".equals(codeString))
      return LOINC480020Answerlist.LA181968;
    if ("LA18197-6".equals(codeString))
      return LOINC480020Answerlist.LA181976;
    throw new IllegalArgumentException("Unknown LOINC480020Answerlist code '"+codeString+"'");
  }

  public String toCode(LOINC480020Answerlist code) {
    if (code == LOINC480020Answerlist.LA66832)
      return "LA6683-2";
    if (code == LOINC480020Answerlist.LA66840)
      return "LA6684-0";
    if (code == LOINC480020Answerlist.LA66857)
      return "LA6685-7";
    if (code == LOINC480020Answerlist.LA181943)
      return "LA18194-3";
    if (code == LOINC480020Answerlist.LA181950)
      return "LA18195-0";
    if (code == LOINC480020Answerlist.LA181968)
      return "LA18196-8";
    if (code == LOINC480020Answerlist.LA181976)
      return "LA18197-6";
    return "?";
  }

    public String toSystem(LOINC480020Answerlist code) {
      return code.getSystem();
      }

}


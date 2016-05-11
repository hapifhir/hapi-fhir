package org.hl7.fhir.dstu3.model.valuesets;

import org.hl7.fhir.dstu3.model.EnumFactory;

public class QuestionnaireItemControlEnumFactory implements EnumFactory<QuestionnaireItemControl> {

  public QuestionnaireItemControl fromCode(String codeString) throws IllegalArgumentException {
    if (codeString == null || "".equals(codeString))
      return null;
    if ("group".equals(codeString))
      return QuestionnaireItemControl.GROUP;
    if ("list".equals(codeString))
      return QuestionnaireItemControl.LIST;
    if ("table".equals(codeString))
      return QuestionnaireItemControl.TABLE;
    if ("header".equals(codeString))
      return QuestionnaireItemControl.HEADER;
    if ("footer".equals(codeString))
      return QuestionnaireItemControl.FOOTER;
    if ("text".equals(codeString))
      return QuestionnaireItemControl.TEXT;
    if ("inline".equals(codeString))
      return QuestionnaireItemControl.INLINE;
    if ("prompt".equals(codeString))
      return QuestionnaireItemControl.PROMPT;
    if ("unit".equals(codeString))
      return QuestionnaireItemControl.UNIT;
    if ("lower".equals(codeString))
      return QuestionnaireItemControl.LOWER;
    if ("upper".equals(codeString))
      return QuestionnaireItemControl.UPPER;
    if ("flyover".equals(codeString))
      return QuestionnaireItemControl.FLYOVER;
    if ("help".equals(codeString))
      return QuestionnaireItemControl.HELP;
    if ("question".equals(codeString))
      return QuestionnaireItemControl.QUESTION;
    if ("autocomplete".equals(codeString))
      return QuestionnaireItemControl.AUTOCOMPLETE;
    if ("drop-down".equals(codeString))
      return QuestionnaireItemControl.DROPDOWN;
    if ("check-box".equals(codeString))
      return QuestionnaireItemControl.CHECKBOX;
    if ("lookup".equals(codeString))
      return QuestionnaireItemControl.LOOKUP;
    if ("radio-button".equals(codeString))
      return QuestionnaireItemControl.RADIOBUTTON;
    if ("slider".equals(codeString))
      return QuestionnaireItemControl.SLIDER;
    if ("spinner".equals(codeString))
      return QuestionnaireItemControl.SPINNER;
    if ("text-box".equals(codeString))
      return QuestionnaireItemControl.TEXTBOX;
    throw new IllegalArgumentException("Unknown QuestionnaireItemControl code '"+codeString+"'");
  }

  public String toCode(QuestionnaireItemControl code) {
    if (code == QuestionnaireItemControl.GROUP)
      return "group";
    if (code == QuestionnaireItemControl.LIST)
      return "list";
    if (code == QuestionnaireItemControl.TABLE)
      return "table";
    if (code == QuestionnaireItemControl.HEADER)
      return "header";
    if (code == QuestionnaireItemControl.FOOTER)
      return "footer";
    if (code == QuestionnaireItemControl.TEXT)
      return "text";
    if (code == QuestionnaireItemControl.INLINE)
      return "inline";
    if (code == QuestionnaireItemControl.PROMPT)
      return "prompt";
    if (code == QuestionnaireItemControl.UNIT)
      return "unit";
    if (code == QuestionnaireItemControl.LOWER)
      return "lower";
    if (code == QuestionnaireItemControl.UPPER)
      return "upper";
    if (code == QuestionnaireItemControl.FLYOVER)
      return "flyover";
    if (code == QuestionnaireItemControl.HELP)
      return "help";
    if (code == QuestionnaireItemControl.QUESTION)
      return "question";
    if (code == QuestionnaireItemControl.AUTOCOMPLETE)
      return "autocomplete";
    if (code == QuestionnaireItemControl.DROPDOWN)
      return "drop-down";
    if (code == QuestionnaireItemControl.CHECKBOX)
      return "check-box";
    if (code == QuestionnaireItemControl.LOOKUP)
      return "lookup";
    if (code == QuestionnaireItemControl.RADIOBUTTON)
      return "radio-button";
    if (code == QuestionnaireItemControl.SLIDER)
      return "slider";
    if (code == QuestionnaireItemControl.SPINNER)
      return "spinner";
    if (code == QuestionnaireItemControl.TEXTBOX)
      return "text-box";
    return "?";
  }

    public String toSystem(QuestionnaireItemControl code) {
      return code.getSystem();
      }

}


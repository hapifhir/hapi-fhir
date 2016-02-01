package org.hl7.fhir.dstu3.model.valuesets;

import org.hl7.fhir.dstu3.model.EnumFactory;

public class MessageReasonEncounterEnumFactory implements EnumFactory<MessageReasonEncounter> {

  public MessageReasonEncounter fromCode(String codeString) throws IllegalArgumentException {
    if (codeString == null || "".equals(codeString))
      return null;
    if ("admit".equals(codeString))
      return MessageReasonEncounter.ADMIT;
    if ("discharge".equals(codeString))
      return MessageReasonEncounter.DISCHARGE;
    if ("absent".equals(codeString))
      return MessageReasonEncounter.ABSENT;
    if ("return".equals(codeString))
      return MessageReasonEncounter.RETURN;
    if ("moved".equals(codeString))
      return MessageReasonEncounter.MOVED;
    if ("edit".equals(codeString))
      return MessageReasonEncounter.EDIT;
    throw new IllegalArgumentException("Unknown MessageReasonEncounter code '"+codeString+"'");
  }

  public String toCode(MessageReasonEncounter code) {
    if (code == MessageReasonEncounter.ADMIT)
      return "admit";
    if (code == MessageReasonEncounter.DISCHARGE)
      return "discharge";
    if (code == MessageReasonEncounter.ABSENT)
      return "absent";
    if (code == MessageReasonEncounter.RETURN)
      return "return";
    if (code == MessageReasonEncounter.MOVED)
      return "moved";
    if (code == MessageReasonEncounter.EDIT)
      return "edit";
    return "?";
  }

    public String toSystem(MessageReasonEncounter code) {
      return code.getSystem();
      }

}


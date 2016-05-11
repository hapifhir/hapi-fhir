package org.hl7.fhir.dstu3.model.valuesets;

import org.hl7.fhir.dstu3.model.EnumFactory;

public class ListEmptyReasonEnumFactory implements EnumFactory<ListEmptyReason> {

  public ListEmptyReason fromCode(String codeString) throws IllegalArgumentException {
    if (codeString == null || "".equals(codeString))
      return null;
    if ("nilknown".equals(codeString))
      return ListEmptyReason.NILKNOWN;
    if ("notasked".equals(codeString))
      return ListEmptyReason.NOTASKED;
    if ("withheld".equals(codeString))
      return ListEmptyReason.WITHHELD;
    if ("unavailable".equals(codeString))
      return ListEmptyReason.UNAVAILABLE;
    if ("notstarted".equals(codeString))
      return ListEmptyReason.NOTSTARTED;
    if ("closed".equals(codeString))
      return ListEmptyReason.CLOSED;
    throw new IllegalArgumentException("Unknown ListEmptyReason code '"+codeString+"'");
  }

  public String toCode(ListEmptyReason code) {
    if (code == ListEmptyReason.NILKNOWN)
      return "nilknown";
    if (code == ListEmptyReason.NOTASKED)
      return "notasked";
    if (code == ListEmptyReason.WITHHELD)
      return "withheld";
    if (code == ListEmptyReason.UNAVAILABLE)
      return "unavailable";
    if (code == ListEmptyReason.NOTSTARTED)
      return "notstarted";
    if (code == ListEmptyReason.CLOSED)
      return "closed";
    return "?";
  }

    public String toSystem(ListEmptyReason code) {
      return code.getSystem();
      }

}


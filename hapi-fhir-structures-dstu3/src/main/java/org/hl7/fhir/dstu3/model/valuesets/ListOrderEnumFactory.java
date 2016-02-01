package org.hl7.fhir.dstu3.model.valuesets;

import org.hl7.fhir.dstu3.model.EnumFactory;

public class ListOrderEnumFactory implements EnumFactory<ListOrder> {

  public ListOrder fromCode(String codeString) throws IllegalArgumentException {
    if (codeString == null || "".equals(codeString))
      return null;
    if ("user".equals(codeString))
      return ListOrder.USER;
    if ("system".equals(codeString))
      return ListOrder.SYSTEM;
    if ("event-date".equals(codeString))
      return ListOrder.EVENTDATE;
    if ("entry-date".equals(codeString))
      return ListOrder.ENTRYDATE;
    if ("priority".equals(codeString))
      return ListOrder.PRIORITY;
    if ("alphabetic".equals(codeString))
      return ListOrder.ALPHABETIC;
    if ("category".equals(codeString))
      return ListOrder.CATEGORY;
    if ("patient".equals(codeString))
      return ListOrder.PATIENT;
    throw new IllegalArgumentException("Unknown ListOrder code '"+codeString+"'");
  }

  public String toCode(ListOrder code) {
    if (code == ListOrder.USER)
      return "user";
    if (code == ListOrder.SYSTEM)
      return "system";
    if (code == ListOrder.EVENTDATE)
      return "event-date";
    if (code == ListOrder.ENTRYDATE)
      return "entry-date";
    if (code == ListOrder.PRIORITY)
      return "priority";
    if (code == ListOrder.ALPHABETIC)
      return "alphabetic";
    if (code == ListOrder.CATEGORY)
      return "category";
    if (code == ListOrder.PATIENT)
      return "patient";
    return "?";
  }

    public String toSystem(ListOrder code) {
      return code.getSystem();
      }

}


package org.hl7.fhir.dstu3.model.valuesets;

import org.hl7.fhir.dstu3.model.EnumFactory;

public class ListExampleCodesEnumFactory implements EnumFactory<ListExampleCodes> {

  public ListExampleCodes fromCode(String codeString) throws IllegalArgumentException {
    if (codeString == null || "".equals(codeString))
      return null;
    if ("alerts".equals(codeString))
      return ListExampleCodes.ALERTS;
    if ("adverserxns".equals(codeString))
      return ListExampleCodes.ADVERSERXNS;
    if ("allergies".equals(codeString))
      return ListExampleCodes.ALLERGIES;
    if ("medications".equals(codeString))
      return ListExampleCodes.MEDICATIONS;
    if ("problems".equals(codeString))
      return ListExampleCodes.PROBLEMS;
    if ("worklist".equals(codeString))
      return ListExampleCodes.WORKLIST;
    if ("waiting".equals(codeString))
      return ListExampleCodes.WAITING;
    if ("protocols".equals(codeString))
      return ListExampleCodes.PROTOCOLS;
    if ("plans".equals(codeString))
      return ListExampleCodes.PLANS;
    throw new IllegalArgumentException("Unknown ListExampleCodes code '"+codeString+"'");
  }

  public String toCode(ListExampleCodes code) {
    if (code == ListExampleCodes.ALERTS)
      return "alerts";
    if (code == ListExampleCodes.ADVERSERXNS)
      return "adverserxns";
    if (code == ListExampleCodes.ALLERGIES)
      return "allergies";
    if (code == ListExampleCodes.MEDICATIONS)
      return "medications";
    if (code == ListExampleCodes.PROBLEMS)
      return "problems";
    if (code == ListExampleCodes.WORKLIST)
      return "worklist";
    if (code == ListExampleCodes.WAITING)
      return "waiting";
    if (code == ListExampleCodes.PROTOCOLS)
      return "protocols";
    if (code == ListExampleCodes.PLANS)
      return "plans";
    return "?";
  }

    public String toSystem(ListExampleCodes code) {
      return code.getSystem();
      }

}


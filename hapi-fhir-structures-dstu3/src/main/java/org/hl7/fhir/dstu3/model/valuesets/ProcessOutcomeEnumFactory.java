package org.hl7.fhir.dstu3.model.valuesets;

import org.hl7.fhir.dstu3.model.EnumFactory;

public class ProcessOutcomeEnumFactory implements EnumFactory<ProcessOutcome> {

  public ProcessOutcome fromCode(String codeString) throws IllegalArgumentException {
    if (codeString == null || "".equals(codeString))
      return null;
    if ("complete".equals(codeString))
      return ProcessOutcome.COMPLETE;
    if ("pended".equals(codeString))
      return ProcessOutcome.PENDED;
    if ("error".equals(codeString))
      return ProcessOutcome.ERROR;
    throw new IllegalArgumentException("Unknown ProcessOutcome code '"+codeString+"'");
  }

  public String toCode(ProcessOutcome code) {
    if (code == ProcessOutcome.COMPLETE)
      return "complete";
    if (code == ProcessOutcome.PENDED)
      return "pended";
    if (code == ProcessOutcome.ERROR)
      return "error";
    return "?";
  }

    public String toSystem(ProcessOutcome code) {
      return code.getSystem();
      }

}


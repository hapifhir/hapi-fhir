package org.hl7.fhir.dstu3.model.valuesets;

import org.hl7.fhir.dstu3.model.EnumFactory;

public class ProcessPriorityEnumFactory implements EnumFactory<ProcessPriority> {

  public ProcessPriority fromCode(String codeString) throws IllegalArgumentException {
    if (codeString == null || "".equals(codeString))
      return null;
    if ("stat".equals(codeString))
      return ProcessPriority.STAT;
    if ("normal".equals(codeString))
      return ProcessPriority.NORMAL;
    if ("deferred".equals(codeString))
      return ProcessPriority.DEFERRED;
    throw new IllegalArgumentException("Unknown ProcessPriority code '"+codeString+"'");
  }

  public String toCode(ProcessPriority code) {
    if (code == ProcessPriority.STAT)
      return "stat";
    if (code == ProcessPriority.NORMAL)
      return "normal";
    if (code == ProcessPriority.DEFERRED)
      return "deferred";
    return "?";
  }

    public String toSystem(ProcessPriority code) {
      return code.getSystem();
      }

}


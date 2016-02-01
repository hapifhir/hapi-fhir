package org.hl7.fhir.dstu3.model.valuesets;

import org.hl7.fhir.dstu3.model.EnumFactory;

public class ConditionStateEnumFactory implements EnumFactory<ConditionState> {

  public ConditionState fromCode(String codeString) throws IllegalArgumentException {
    if (codeString == null || "".equals(codeString))
      return null;
    if ("active".equals(codeString))
      return ConditionState.ACTIVE;
    if ("inactive".equals(codeString))
      return ConditionState.INACTIVE;
    if ("resolved".equals(codeString))
      return ConditionState.RESOLVED;
    throw new IllegalArgumentException("Unknown ConditionState code '"+codeString+"'");
  }

  public String toCode(ConditionState code) {
    if (code == ConditionState.ACTIVE)
      return "active";
    if (code == ConditionState.INACTIVE)
      return "inactive";
    if (code == ConditionState.RESOLVED)
      return "resolved";
    return "?";
  }

    public String toSystem(ConditionState code) {
      return code.getSystem();
      }

}


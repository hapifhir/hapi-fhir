package org.hl7.fhir.dstu3.model.valuesets;

import org.hl7.fhir.dstu3.model.EnumFactory;

public class GoalPriorityEnumFactory implements EnumFactory<GoalPriority> {

  public GoalPriority fromCode(String codeString) throws IllegalArgumentException {
    if (codeString == null || "".equals(codeString))
      return null;
    if ("high".equals(codeString))
      return GoalPriority.HIGH;
    if ("medium".equals(codeString))
      return GoalPriority.MEDIUM;
    if ("low".equals(codeString))
      return GoalPriority.LOW;
    throw new IllegalArgumentException("Unknown GoalPriority code '"+codeString+"'");
  }

  public String toCode(GoalPriority code) {
    if (code == GoalPriority.HIGH)
      return "high";
    if (code == GoalPriority.MEDIUM)
      return "medium";
    if (code == GoalPriority.LOW)
      return "low";
    return "?";
  }

    public String toSystem(GoalPriority code) {
      return code.getSystem();
      }

}


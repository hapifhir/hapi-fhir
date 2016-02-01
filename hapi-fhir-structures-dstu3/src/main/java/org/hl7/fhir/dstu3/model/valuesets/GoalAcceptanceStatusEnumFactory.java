package org.hl7.fhir.dstu3.model.valuesets;

import org.hl7.fhir.dstu3.model.EnumFactory;

public class GoalAcceptanceStatusEnumFactory implements EnumFactory<GoalAcceptanceStatus> {

  public GoalAcceptanceStatus fromCode(String codeString) throws IllegalArgumentException {
    if (codeString == null || "".equals(codeString))
      return null;
    if ("agree".equals(codeString))
      return GoalAcceptanceStatus.AGREE;
    if ("disagree".equals(codeString))
      return GoalAcceptanceStatus.DISAGREE;
    if ("pending".equals(codeString))
      return GoalAcceptanceStatus.PENDING;
    throw new IllegalArgumentException("Unknown GoalAcceptanceStatus code '"+codeString+"'");
  }

  public String toCode(GoalAcceptanceStatus code) {
    if (code == GoalAcceptanceStatus.AGREE)
      return "agree";
    if (code == GoalAcceptanceStatus.DISAGREE)
      return "disagree";
    if (code == GoalAcceptanceStatus.PENDING)
      return "pending";
    return "?";
  }

    public String toSystem(GoalAcceptanceStatus code) {
      return code.getSystem();
      }

}


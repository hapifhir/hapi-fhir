package org.hl7.fhir.dstu3.model.valuesets;

import org.hl7.fhir.dstu3.model.EnumFactory;

public class GoalCategoryEnumFactory implements EnumFactory<GoalCategory> {

  public GoalCategory fromCode(String codeString) throws IllegalArgumentException {
    if (codeString == null || "".equals(codeString))
      return null;
    if ("dietary".equals(codeString))
      return GoalCategory.DIETARY;
    if ("safety".equals(codeString))
      return GoalCategory.SAFETY;
    if ("behavioral".equals(codeString))
      return GoalCategory.BEHAVIORAL;
    if ("nursing".equals(codeString))
      return GoalCategory.NURSING;
    if ("physiotherapy".equals(codeString))
      return GoalCategory.PHYSIOTHERAPY;
    throw new IllegalArgumentException("Unknown GoalCategory code '"+codeString+"'");
  }

  public String toCode(GoalCategory code) {
    if (code == GoalCategory.DIETARY)
      return "dietary";
    if (code == GoalCategory.SAFETY)
      return "safety";
    if (code == GoalCategory.BEHAVIORAL)
      return "behavioral";
    if (code == GoalCategory.NURSING)
      return "nursing";
    if (code == GoalCategory.PHYSIOTHERAPY)
      return "physiotherapy";
    return "?";
  }

    public String toSystem(GoalCategory code) {
      return code.getSystem();
      }

}


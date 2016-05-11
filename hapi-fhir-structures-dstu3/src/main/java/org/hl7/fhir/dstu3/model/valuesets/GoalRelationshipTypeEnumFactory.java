package org.hl7.fhir.dstu3.model.valuesets;

import org.hl7.fhir.dstu3.model.EnumFactory;

public class GoalRelationshipTypeEnumFactory implements EnumFactory<GoalRelationshipType> {

  public GoalRelationshipType fromCode(String codeString) throws IllegalArgumentException {
    if (codeString == null || "".equals(codeString))
      return null;
    if ("predecessor".equals(codeString))
      return GoalRelationshipType.PREDECESSOR;
    if ("successor".equals(codeString))
      return GoalRelationshipType.SUCCESSOR;
    if ("replacement".equals(codeString))
      return GoalRelationshipType.REPLACEMENT;
    if ("component".equals(codeString))
      return GoalRelationshipType.COMPONENT;
    if ("other".equals(codeString))
      return GoalRelationshipType.OTHER;
    throw new IllegalArgumentException("Unknown GoalRelationshipType code '"+codeString+"'");
  }

  public String toCode(GoalRelationshipType code) {
    if (code == GoalRelationshipType.PREDECESSOR)
      return "predecessor";
    if (code == GoalRelationshipType.SUCCESSOR)
      return "successor";
    if (code == GoalRelationshipType.REPLACEMENT)
      return "replacement";
    if (code == GoalRelationshipType.COMPONENT)
      return "component";
    if (code == GoalRelationshipType.OTHER)
      return "other";
    return "?";
  }

    public String toSystem(GoalRelationshipType code) {
      return code.getSystem();
      }

}


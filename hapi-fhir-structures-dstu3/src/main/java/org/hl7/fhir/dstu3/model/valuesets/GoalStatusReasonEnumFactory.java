package org.hl7.fhir.dstu3.model.valuesets;

import org.hl7.fhir.dstu3.model.EnumFactory;

public class GoalStatusReasonEnumFactory implements EnumFactory<GoalStatusReason> {

  public GoalStatusReason fromCode(String codeString) throws IllegalArgumentException {
    if (codeString == null || "".equals(codeString))
      return null;
    if ("surgery".equals(codeString))
      return GoalStatusReason.SURGERY;
    if ("life-event".equals(codeString))
      return GoalStatusReason.LIFEEVENT;
    if ("replaced".equals(codeString))
      return GoalStatusReason.REPLACED;
    if ("patient-request".equals(codeString))
      return GoalStatusReason.PATIENTREQUEST;
    throw new IllegalArgumentException("Unknown GoalStatusReason code '"+codeString+"'");
  }

  public String toCode(GoalStatusReason code) {
    if (code == GoalStatusReason.SURGERY)
      return "surgery";
    if (code == GoalStatusReason.LIFEEVENT)
      return "life-event";
    if (code == GoalStatusReason.REPLACED)
      return "replaced";
    if (code == GoalStatusReason.PATIENTREQUEST)
      return "patient-request";
    return "?";
  }

    public String toSystem(GoalStatusReason code) {
      return code.getSystem();
      }

}


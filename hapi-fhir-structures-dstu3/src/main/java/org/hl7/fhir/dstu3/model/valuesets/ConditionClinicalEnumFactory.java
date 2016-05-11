package org.hl7.fhir.dstu3.model.valuesets;

import org.hl7.fhir.dstu3.model.EnumFactory;

public class ConditionClinicalEnumFactory implements EnumFactory<ConditionClinical> {

  public ConditionClinical fromCode(String codeString) throws IllegalArgumentException {
    if (codeString == null || "".equals(codeString))
      return null;
    if ("active".equals(codeString))
      return ConditionClinical.ACTIVE;
    if ("relapse".equals(codeString))
      return ConditionClinical.RELAPSE;
    if ("remission".equals(codeString))
      return ConditionClinical.REMISSION;
    if ("resolved".equals(codeString))
      return ConditionClinical.RESOLVED;
    throw new IllegalArgumentException("Unknown ConditionClinical code '"+codeString+"'");
  }

  public String toCode(ConditionClinical code) {
    if (code == ConditionClinical.ACTIVE)
      return "active";
    if (code == ConditionClinical.RELAPSE)
      return "relapse";
    if (code == ConditionClinical.REMISSION)
      return "remission";
    if (code == ConditionClinical.RESOLVED)
      return "resolved";
    return "?";
  }

    public String toSystem(ConditionClinical code) {
      return code.getSystem();
      }

}


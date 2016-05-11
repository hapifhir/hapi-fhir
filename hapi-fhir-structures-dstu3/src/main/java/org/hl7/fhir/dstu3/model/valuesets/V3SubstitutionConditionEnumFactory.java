package org.hl7.fhir.dstu3.model.valuesets;

import org.hl7.fhir.dstu3.model.EnumFactory;

public class V3SubstitutionConditionEnumFactory implements EnumFactory<V3SubstitutionCondition> {

  public V3SubstitutionCondition fromCode(String codeString) throws IllegalArgumentException {
    if (codeString == null || "".equals(codeString))
      return null;
    if ("_Conditional".equals(codeString))
      return V3SubstitutionCondition._CONDITIONAL;
    if ("CONFIRM".equals(codeString))
      return V3SubstitutionCondition.CONFIRM;
    if ("NOTIFY".equals(codeString))
      return V3SubstitutionCondition.NOTIFY;
    if ("NOSUB".equals(codeString))
      return V3SubstitutionCondition.NOSUB;
    if ("UNCOND".equals(codeString))
      return V3SubstitutionCondition.UNCOND;
    throw new IllegalArgumentException("Unknown V3SubstitutionCondition code '"+codeString+"'");
  }

  public String toCode(V3SubstitutionCondition code) {
    if (code == V3SubstitutionCondition._CONDITIONAL)
      return "_Conditional";
    if (code == V3SubstitutionCondition.CONFIRM)
      return "CONFIRM";
    if (code == V3SubstitutionCondition.NOTIFY)
      return "NOTIFY";
    if (code == V3SubstitutionCondition.NOSUB)
      return "NOSUB";
    if (code == V3SubstitutionCondition.UNCOND)
      return "UNCOND";
    return "?";
  }

    public String toSystem(V3SubstitutionCondition code) {
      return code.getSystem();
      }

}


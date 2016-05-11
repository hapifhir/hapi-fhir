package org.hl7.fhir.dstu3.model.valuesets;

import org.hl7.fhir.dstu3.model.EnumFactory;

public class RiskProbabilityEnumFactory implements EnumFactory<RiskProbability> {

  public RiskProbability fromCode(String codeString) throws IllegalArgumentException {
    if (codeString == null || "".equals(codeString))
      return null;
    if ("negligible".equals(codeString))
      return RiskProbability.NEGLIGIBLE;
    if ("low".equals(codeString))
      return RiskProbability.LOW;
    if ("moderate".equals(codeString))
      return RiskProbability.MODERATE;
    if ("high".equals(codeString))
      return RiskProbability.HIGH;
    if ("certain".equals(codeString))
      return RiskProbability.CERTAIN;
    throw new IllegalArgumentException("Unknown RiskProbability code '"+codeString+"'");
  }

  public String toCode(RiskProbability code) {
    if (code == RiskProbability.NEGLIGIBLE)
      return "negligible";
    if (code == RiskProbability.LOW)
      return "low";
    if (code == RiskProbability.MODERATE)
      return "moderate";
    if (code == RiskProbability.HIGH)
      return "high";
    if (code == RiskProbability.CERTAIN)
      return "certain";
    return "?";
  }

    public String toSystem(RiskProbability code) {
      return code.getSystem();
      }

}


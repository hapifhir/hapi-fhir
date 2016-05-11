package org.hl7.fhir.dstu3.model.valuesets;

import org.hl7.fhir.dstu3.model.EnumFactory;

public class V3ProbabilityDistributionTypeEnumFactory implements EnumFactory<V3ProbabilityDistributionType> {

  public V3ProbabilityDistributionType fromCode(String codeString) throws IllegalArgumentException {
    if (codeString == null || "".equals(codeString))
      return null;
    if ("B".equals(codeString))
      return V3ProbabilityDistributionType.B;
    if ("E".equals(codeString))
      return V3ProbabilityDistributionType.E;
    if ("F".equals(codeString))
      return V3ProbabilityDistributionType.F;
    if ("G".equals(codeString))
      return V3ProbabilityDistributionType.G;
    if ("LN".equals(codeString))
      return V3ProbabilityDistributionType.LN;
    if ("N".equals(codeString))
      return V3ProbabilityDistributionType.N;
    if ("T".equals(codeString))
      return V3ProbabilityDistributionType.T;
    if ("U".equals(codeString))
      return V3ProbabilityDistributionType.U;
    if ("X2".equals(codeString))
      return V3ProbabilityDistributionType.X2;
    throw new IllegalArgumentException("Unknown V3ProbabilityDistributionType code '"+codeString+"'");
  }

  public String toCode(V3ProbabilityDistributionType code) {
    if (code == V3ProbabilityDistributionType.B)
      return "B";
    if (code == V3ProbabilityDistributionType.E)
      return "E";
    if (code == V3ProbabilityDistributionType.F)
      return "F";
    if (code == V3ProbabilityDistributionType.G)
      return "G";
    if (code == V3ProbabilityDistributionType.LN)
      return "LN";
    if (code == V3ProbabilityDistributionType.N)
      return "N";
    if (code == V3ProbabilityDistributionType.T)
      return "T";
    if (code == V3ProbabilityDistributionType.U)
      return "U";
    if (code == V3ProbabilityDistributionType.X2)
      return "X2";
    return "?";
  }

    public String toSystem(V3ProbabilityDistributionType code) {
      return code.getSystem();
      }

}


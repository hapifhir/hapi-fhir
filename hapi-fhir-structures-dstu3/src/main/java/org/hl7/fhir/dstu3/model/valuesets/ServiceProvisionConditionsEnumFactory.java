package org.hl7.fhir.dstu3.model.valuesets;

import org.hl7.fhir.dstu3.model.EnumFactory;

public class ServiceProvisionConditionsEnumFactory implements EnumFactory<ServiceProvisionConditions> {

  public ServiceProvisionConditions fromCode(String codeString) throws IllegalArgumentException {
    if (codeString == null || "".equals(codeString))
      return null;
    if ("free".equals(codeString))
      return ServiceProvisionConditions.FREE;
    if ("disc".equals(codeString))
      return ServiceProvisionConditions.DISC;
    if ("cost".equals(codeString))
      return ServiceProvisionConditions.COST;
    throw new IllegalArgumentException("Unknown ServiceProvisionConditions code '"+codeString+"'");
  }

  public String toCode(ServiceProvisionConditions code) {
    if (code == ServiceProvisionConditions.FREE)
      return "free";
    if (code == ServiceProvisionConditions.DISC)
      return "disc";
    if (code == ServiceProvisionConditions.COST)
      return "cost";
    return "?";
  }

    public String toSystem(ServiceProvisionConditions code) {
      return code.getSystem();
      }

}


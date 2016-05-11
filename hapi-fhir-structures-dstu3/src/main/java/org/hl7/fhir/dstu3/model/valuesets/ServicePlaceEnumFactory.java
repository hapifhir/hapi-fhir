package org.hl7.fhir.dstu3.model.valuesets;

import org.hl7.fhir.dstu3.model.EnumFactory;

public class ServicePlaceEnumFactory implements EnumFactory<ServicePlace> {

  public ServicePlace fromCode(String codeString) throws IllegalArgumentException {
    if (codeString == null || "".equals(codeString))
      return null;
    if ("emergency".equals(codeString))
      return ServicePlace.EMERGENCY;
    if ("clinic".equals(codeString))
      return ServicePlace.CLINIC;
    throw new IllegalArgumentException("Unknown ServicePlace code '"+codeString+"'");
  }

  public String toCode(ServicePlace code) {
    if (code == ServicePlace.EMERGENCY)
      return "emergency";
    if (code == ServicePlace.CLINIC)
      return "clinic";
    return "?";
  }

    public String toSystem(ServicePlace code) {
      return code.getSystem();
      }

}


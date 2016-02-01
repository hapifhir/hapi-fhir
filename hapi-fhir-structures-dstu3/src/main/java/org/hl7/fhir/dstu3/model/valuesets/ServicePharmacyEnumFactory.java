package org.hl7.fhir.dstu3.model.valuesets;

import org.hl7.fhir.dstu3.model.EnumFactory;

public class ServicePharmacyEnumFactory implements EnumFactory<ServicePharmacy> {

  public ServicePharmacy fromCode(String codeString) throws IllegalArgumentException {
    if (codeString == null || "".equals(codeString))
      return null;
    if ("smokecess".equals(codeString))
      return ServicePharmacy.SMOKECESS;
    if ("flushot".equals(codeString))
      return ServicePharmacy.FLUSHOT;
    throw new IllegalArgumentException("Unknown ServicePharmacy code '"+codeString+"'");
  }

  public String toCode(ServicePharmacy code) {
    if (code == ServicePharmacy.SMOKECESS)
      return "smokecess";
    if (code == ServicePharmacy.FLUSHOT)
      return "flushot";
    return "?";
  }

    public String toSystem(ServicePharmacy code) {
      return code.getSystem();
      }

}


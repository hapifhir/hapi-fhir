package org.hl7.fhir.dstu3.model.valuesets;

import org.hl7.fhir.dstu3.model.EnumFactory;

public class ServiceProductEnumFactory implements EnumFactory<ServiceProduct> {

  public ServiceProduct fromCode(String codeString) throws IllegalArgumentException {
    if (codeString == null || "".equals(codeString))
      return null;
    if ("exam".equals(codeString))
      return ServiceProduct.EXAM;
    if ("flushot".equals(codeString))
      return ServiceProduct.FLUSHOT;
    throw new IllegalArgumentException("Unknown ServiceProduct code '"+codeString+"'");
  }

  public String toCode(ServiceProduct code) {
    if (code == ServiceProduct.EXAM)
      return "exam";
    if (code == ServiceProduct.FLUSHOT)
      return "flushot";
    return "?";
  }

    public String toSystem(ServiceProduct code) {
      return code.getSystem();
      }

}


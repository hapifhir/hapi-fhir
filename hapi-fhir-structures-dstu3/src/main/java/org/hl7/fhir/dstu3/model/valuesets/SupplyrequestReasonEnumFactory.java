package org.hl7.fhir.dstu3.model.valuesets;

import org.hl7.fhir.dstu3.model.EnumFactory;

public class SupplyrequestReasonEnumFactory implements EnumFactory<SupplyrequestReason> {

  public SupplyrequestReason fromCode(String codeString) throws IllegalArgumentException {
    if (codeString == null || "".equals(codeString))
      return null;
    if ("patient-care".equals(codeString))
      return SupplyrequestReason.PATIENTCARE;
    if ("ward-stock".equals(codeString))
      return SupplyrequestReason.WARDSTOCK;
    throw new IllegalArgumentException("Unknown SupplyrequestReason code '"+codeString+"'");
  }

  public String toCode(SupplyrequestReason code) {
    if (code == SupplyrequestReason.PATIENTCARE)
      return "patient-care";
    if (code == SupplyrequestReason.WARDSTOCK)
      return "ward-stock";
    return "?";
  }

    public String toSystem(SupplyrequestReason code) {
      return code.getSystem();
      }

}


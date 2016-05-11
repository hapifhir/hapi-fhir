package org.hl7.fhir.dstu3.model.valuesets;

import org.hl7.fhir.dstu3.model.EnumFactory;

public class SupplyrequestKindEnumFactory implements EnumFactory<SupplyrequestKind> {

  public SupplyrequestKind fromCode(String codeString) throws IllegalArgumentException {
    if (codeString == null || "".equals(codeString))
      return null;
    if ("central".equals(codeString))
      return SupplyrequestKind.CENTRAL;
    if ("nonstock".equals(codeString))
      return SupplyrequestKind.NONSTOCK;
    throw new IllegalArgumentException("Unknown SupplyrequestKind code '"+codeString+"'");
  }

  public String toCode(SupplyrequestKind code) {
    if (code == SupplyrequestKind.CENTRAL)
      return "central";
    if (code == SupplyrequestKind.NONSTOCK)
      return "nonstock";
    return "?";
  }

    public String toSystem(SupplyrequestKind code) {
      return code.getSystem();
      }

}


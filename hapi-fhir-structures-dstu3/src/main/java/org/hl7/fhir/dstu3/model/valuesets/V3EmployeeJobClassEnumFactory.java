package org.hl7.fhir.dstu3.model.valuesets;

import org.hl7.fhir.dstu3.model.EnumFactory;

public class V3EmployeeJobClassEnumFactory implements EnumFactory<V3EmployeeJobClass> {

  public V3EmployeeJobClass fromCode(String codeString) throws IllegalArgumentException {
    if (codeString == null || "".equals(codeString))
      return null;
    if ("FT".equals(codeString))
      return V3EmployeeJobClass.FT;
    if ("PT".equals(codeString))
      return V3EmployeeJobClass.PT;
    throw new IllegalArgumentException("Unknown V3EmployeeJobClass code '"+codeString+"'");
  }

  public String toCode(V3EmployeeJobClass code) {
    if (code == V3EmployeeJobClass.FT)
      return "FT";
    if (code == V3EmployeeJobClass.PT)
      return "PT";
    return "?";
  }

    public String toSystem(V3EmployeeJobClass code) {
      return code.getSystem();
      }

}


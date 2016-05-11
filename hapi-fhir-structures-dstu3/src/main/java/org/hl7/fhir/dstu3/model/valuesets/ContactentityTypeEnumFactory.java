package org.hl7.fhir.dstu3.model.valuesets;

import org.hl7.fhir.dstu3.model.EnumFactory;

public class ContactentityTypeEnumFactory implements EnumFactory<ContactentityType> {

  public ContactentityType fromCode(String codeString) throws IllegalArgumentException {
    if (codeString == null || "".equals(codeString))
      return null;
    if ("BILL".equals(codeString))
      return ContactentityType.BILL;
    if ("ADMIN".equals(codeString))
      return ContactentityType.ADMIN;
    if ("HR".equals(codeString))
      return ContactentityType.HR;
    if ("PAYOR".equals(codeString))
      return ContactentityType.PAYOR;
    if ("PATINF".equals(codeString))
      return ContactentityType.PATINF;
    if ("PRESS".equals(codeString))
      return ContactentityType.PRESS;
    throw new IllegalArgumentException("Unknown ContactentityType code '"+codeString+"'");
  }

  public String toCode(ContactentityType code) {
    if (code == ContactentityType.BILL)
      return "BILL";
    if (code == ContactentityType.ADMIN)
      return "ADMIN";
    if (code == ContactentityType.HR)
      return "HR";
    if (code == ContactentityType.PAYOR)
      return "PAYOR";
    if (code == ContactentityType.PATINF)
      return "PATINF";
    if (code == ContactentityType.PRESS)
      return "PRESS";
    return "?";
  }

    public String toSystem(ContactentityType code) {
      return code.getSystem();
      }

}


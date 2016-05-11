package org.hl7.fhir.dstu3.model.valuesets;

import org.hl7.fhir.dstu3.model.EnumFactory;

public class V3PaymentTermsEnumFactory implements EnumFactory<V3PaymentTerms> {

  public V3PaymentTerms fromCode(String codeString) throws IllegalArgumentException {
    if (codeString == null || "".equals(codeString))
      return null;
    if ("COD".equals(codeString))
      return V3PaymentTerms.COD;
    if ("N30".equals(codeString))
      return V3PaymentTerms.N30;
    if ("N60".equals(codeString))
      return V3PaymentTerms.N60;
    if ("N90".equals(codeString))
      return V3PaymentTerms.N90;
    throw new IllegalArgumentException("Unknown V3PaymentTerms code '"+codeString+"'");
  }

  public String toCode(V3PaymentTerms code) {
    if (code == V3PaymentTerms.COD)
      return "COD";
    if (code == V3PaymentTerms.N30)
      return "N30";
    if (code == V3PaymentTerms.N60)
      return "N60";
    if (code == V3PaymentTerms.N90)
      return "N90";
    return "?";
  }

    public String toSystem(V3PaymentTerms code) {
      return code.getSystem();
      }

}


package org.hl7.fhir.dstu3.model.valuesets;

import org.hl7.fhir.dstu3.model.EnumFactory;

public class V3ActUSPrivacyLawEnumFactory implements EnumFactory<V3ActUSPrivacyLaw> {

  public V3ActUSPrivacyLaw fromCode(String codeString) throws IllegalArgumentException {
    if (codeString == null || "".equals(codeString))
      return null;
    if ("_ActUSPrivacyLaw".equals(codeString))
      return V3ActUSPrivacyLaw._ACTUSPRIVACYLAW;
    if ("42CFRPart2".equals(codeString))
      return V3ActUSPrivacyLaw._42CFRPART2;
    if ("CommonRule".equals(codeString))
      return V3ActUSPrivacyLaw.COMMONRULE;
    if ("HIPAANOPP".equals(codeString))
      return V3ActUSPrivacyLaw.HIPAANOPP;
    if ("HIPAAPsyNotes".equals(codeString))
      return V3ActUSPrivacyLaw.HIPAAPSYNOTES;
    if ("HIPAASelfPay".equals(codeString))
      return V3ActUSPrivacyLaw.HIPAASELFPAY;
    if ("Title38Section7332".equals(codeString))
      return V3ActUSPrivacyLaw.TITLE38SECTION7332;
    throw new IllegalArgumentException("Unknown V3ActUSPrivacyLaw code '"+codeString+"'");
  }

  public String toCode(V3ActUSPrivacyLaw code) {
    if (code == V3ActUSPrivacyLaw._ACTUSPRIVACYLAW)
      return "_ActUSPrivacyLaw";
    if (code == V3ActUSPrivacyLaw._42CFRPART2)
      return "42CFRPart2";
    if (code == V3ActUSPrivacyLaw.COMMONRULE)
      return "CommonRule";
    if (code == V3ActUSPrivacyLaw.HIPAANOPP)
      return "HIPAANOPP";
    if (code == V3ActUSPrivacyLaw.HIPAAPSYNOTES)
      return "HIPAAPsyNotes";
    if (code == V3ActUSPrivacyLaw.HIPAASELFPAY)
      return "HIPAASelfPay";
    if (code == V3ActUSPrivacyLaw.TITLE38SECTION7332)
      return "Title38Section7332";
    return "?";
  }

    public String toSystem(V3ActUSPrivacyLaw code) {
      return code.getSystem();
      }

}


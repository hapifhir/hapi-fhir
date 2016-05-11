package org.hl7.fhir.dstu3.model.valuesets;

import org.hl7.fhir.dstu3.model.EnumFactory;

public class ServiceReferralMethodEnumFactory implements EnumFactory<ServiceReferralMethod> {

  public ServiceReferralMethod fromCode(String codeString) throws IllegalArgumentException {
    if (codeString == null || "".equals(codeString))
      return null;
    if ("fax".equals(codeString))
      return ServiceReferralMethod.FAX;
    if ("phone".equals(codeString))
      return ServiceReferralMethod.PHONE;
    if ("elec".equals(codeString))
      return ServiceReferralMethod.ELEC;
    if ("semail".equals(codeString))
      return ServiceReferralMethod.SEMAIL;
    if ("mail".equals(codeString))
      return ServiceReferralMethod.MAIL;
    throw new IllegalArgumentException("Unknown ServiceReferralMethod code '"+codeString+"'");
  }

  public String toCode(ServiceReferralMethod code) {
    if (code == ServiceReferralMethod.FAX)
      return "fax";
    if (code == ServiceReferralMethod.PHONE)
      return "phone";
    if (code == ServiceReferralMethod.ELEC)
      return "elec";
    if (code == ServiceReferralMethod.SEMAIL)
      return "semail";
    if (code == ServiceReferralMethod.MAIL)
      return "mail";
    return "?";
  }

    public String toSystem(ServiceReferralMethod code) {
      return code.getSystem();
      }

}


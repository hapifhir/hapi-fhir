package org.hl7.fhir.dstu3.model.valuesets;

import org.hl7.fhir.dstu3.model.EnumFactory;

public class RestfulSecurityServiceEnumFactory implements EnumFactory<RestfulSecurityService> {

  public RestfulSecurityService fromCode(String codeString) throws IllegalArgumentException {
    if (codeString == null || "".equals(codeString))
      return null;
    if ("OAuth".equals(codeString))
      return RestfulSecurityService.OAUTH;
    if ("SMART-on-FHIR".equals(codeString))
      return RestfulSecurityService.SMARTONFHIR;
    if ("NTLM".equals(codeString))
      return RestfulSecurityService.NTLM;
    if ("Basic".equals(codeString))
      return RestfulSecurityService.BASIC;
    if ("Kerberos".equals(codeString))
      return RestfulSecurityService.KERBEROS;
    if ("Certificates".equals(codeString))
      return RestfulSecurityService.CERTIFICATES;
    throw new IllegalArgumentException("Unknown RestfulSecurityService code '"+codeString+"'");
  }

  public String toCode(RestfulSecurityService code) {
    if (code == RestfulSecurityService.OAUTH)
      return "OAuth";
    if (code == RestfulSecurityService.SMARTONFHIR)
      return "SMART-on-FHIR";
    if (code == RestfulSecurityService.NTLM)
      return "NTLM";
    if (code == RestfulSecurityService.BASIC)
      return "Basic";
    if (code == RestfulSecurityService.KERBEROS)
      return "Kerberos";
    if (code == RestfulSecurityService.CERTIFICATES)
      return "Certificates";
    return "?";
  }

    public String toSystem(RestfulSecurityService code) {
      return code.getSystem();
      }

}


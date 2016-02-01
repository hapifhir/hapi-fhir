package org.hl7.fhir.dstu3.model.valuesets;

import org.hl7.fhir.dstu3.exceptions.FHIRException;

public enum RestfulSecurityService {

        /**
         * Oauth (unspecified version see oauth.net).
         */
        OAUTH, 
        /**
         * OAuth2 using SMART-on-FHIR profile (see http://docs.smarthealthit.org/).
         */
        SMARTONFHIR, 
        /**
         * Microsoft NTLM Authentication.
         */
        NTLM, 
        /**
         * Basic authentication defined in HTTP specification.
         */
        BASIC, 
        /**
         * see http://www.ietf.org/rfc/rfc4120.txt.
         */
        KERBEROS, 
        /**
         * SSL where client must have a certificate registered with the server.
         */
        CERTIFICATES, 
        /**
         * added to help the parsers
         */
        NULL;
        public static RestfulSecurityService fromCode(String codeString) throws FHIRException {
            if (codeString == null || "".equals(codeString))
                return null;
        if ("OAuth".equals(codeString))
          return OAUTH;
        if ("SMART-on-FHIR".equals(codeString))
          return SMARTONFHIR;
        if ("NTLM".equals(codeString))
          return NTLM;
        if ("Basic".equals(codeString))
          return BASIC;
        if ("Kerberos".equals(codeString))
          return KERBEROS;
        if ("Certificates".equals(codeString))
          return CERTIFICATES;
        throw new FHIRException("Unknown RestfulSecurityService code '"+codeString+"'");
        }
        public String toCode() {
          switch (this) {
            case OAUTH: return "OAuth";
            case SMARTONFHIR: return "SMART-on-FHIR";
            case NTLM: return "NTLM";
            case BASIC: return "Basic";
            case KERBEROS: return "Kerberos";
            case CERTIFICATES: return "Certificates";
            default: return "?";
          }
        }
        public String getSystem() {
          return "http://hl7.org/fhir/restful-security-service";
        }
        public String getDefinition() {
          switch (this) {
            case OAUTH: return "Oauth (unspecified version see oauth.net).";
            case SMARTONFHIR: return "OAuth2 using SMART-on-FHIR profile (see http://docs.smarthealthit.org/).";
            case NTLM: return "Microsoft NTLM Authentication.";
            case BASIC: return "Basic authentication defined in HTTP specification.";
            case KERBEROS: return "see http://www.ietf.org/rfc/rfc4120.txt.";
            case CERTIFICATES: return "SSL where client must have a certificate registered with the server.";
            default: return "?";
          }
        }
        public String getDisplay() {
          switch (this) {
            case OAUTH: return "OAuth";
            case SMARTONFHIR: return "SMART-on-FHIR";
            case NTLM: return "NTLM";
            case BASIC: return "Basic";
            case KERBEROS: return "Kerberos";
            case CERTIFICATES: return "Certificates";
            default: return "?";
          }
    }


}

